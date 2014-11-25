package pl.edu.icm.saos.importer.notapi.supremecourt.judgment.process;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.importer.common.converter.JudgeConverter;
import pl.edu.icm.saos.importer.common.converter.JudgmentDataExtractor;
import pl.edu.icm.saos.importer.common.correction.ImportCorrection;
import pl.edu.icm.saos.importer.common.correction.ImportCorrectionList;
import pl.edu.icm.saos.importer.notapi.supremecourt.judgment.json.SourceScJudgment;
import pl.edu.icm.saos.importer.notapi.supremecourt.judgment.json.SourceScJudgment.SourceScJudge;
import pl.edu.icm.saos.persistence.correction.model.CorrectedProperty;
import pl.edu.icm.saos.persistence.model.CourtCase;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judge.JudgeRole;
import pl.edu.icm.saos.persistence.model.Judgment.JudgmentType;
import pl.edu.icm.saos.persistence.model.JudgmentReferencedRegulation;
import pl.edu.icm.saos.persistence.model.SourceCode;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamber;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamberDivision;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment.PersonnelType;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgmentForm;

import com.google.common.collect.Lists;

/**
 * @author Łukasz Dumiszewski
 */
@Service("sourceScJudgmentExtractor")
public class SourceScJudgmentExtractor implements JudgmentDataExtractor<SupremeCourtJudgment, SourceScJudgment> {

    
    private ScJudgmentFormConverter scJudgmentFormConverter;
    
    private ScJudgmentFormNameNormalizer scJudgmentFormNameNormalizer;
    
    private ScJudgmentFormCreator scJudgmentFormCreator;
    
    
    private ScChamberNameNormalizer scChamberNameNormalizer;
    
    private ScChamberCreator scChamberCreator;
    
    
    private ScChamberDivisionCreator scChamberDivisionCreator;
    
    private JudgeConverter judgeConverter;
    
    
    
    
    //------------------------ LOGIC --------------------------
    
    
    @Override
    public SupremeCourtJudgment createNewJudgment() {
        return new SupremeCourtJudgment();
    }

    @Override
    public List<CourtCase> extractCourtCases(SourceScJudgment sourceJudgment, ImportCorrectionList correctionList) {
        return Lists.newArrayList(new CourtCase(sourceJudgment.getCaseNumber()));
    }

    @Override
    public String extractTextContent(SourceScJudgment sourceJudgment, ImportCorrectionList correctionList) {
        return sourceJudgment.getTextContent();
    }

    @Override
    public DateTime extractPublicationDate(SourceScJudgment sourceJudgment, ImportCorrectionList correctionList) {
        return sourceJudgment.getSource().getPublicationDateTime();
    }

    @Override
    public String extractPublisher(SourceScJudgment sourceJudgment, ImportCorrectionList correctionList) {
        return null;
    }

    @Override
    public String extractReviser(SourceScJudgment sourceJudgment, ImportCorrectionList correctionList) {
        return null;
    }

    @Override
    public List<Judge> extractJudges(SourceScJudgment sourceJudgment, ImportCorrectionList correctionList) {
        List<Judge> judges = Lists.newArrayList();
        
        for (SourceScJudge scJudge : sourceJudgment.getJudges()) {
            
            if (StringUtils.isBlank(scJudge.getName())) {
                continue;
            }
            
            List<JudgeRole> roles = scJudge.getSpecialRoles().stream().map(role->JudgeRole.valueOf(role)).collect(Collectors.toList());
            Judge judge = judgeConverter.convertJudge(scJudge.getName(), roles, correctionList);
            
            if (judge != null) {
                judge.setFunction(scJudge.getFunction());
                judges.add(judge);
            }
        
        }
        
        return judges;
    }

    
    @Override
    public List<JudgmentReferencedRegulation> extractReferencedRegulations(SourceScJudgment sourceJudgment, ImportCorrectionList correctionList) {
        return Lists.newArrayList();
    }

    
    @Override
    public JudgmentType extractJudgmentType(SourceScJudgment sourceJudgment, ImportCorrectionList correctionList) {
        return scJudgmentFormConverter.convertToJudgmentType(sourceJudgment.getSupremeCourtJudgmentForm(), correctionList);
    }

    
    @Override
    public List<String> extractLegalBases(SourceScJudgment sourceJudgment, ImportCorrectionList correctionList) {
        return Lists.newArrayList();
    }

    
    @Override
    public String extractSummary(SourceScJudgment sourceJudgment, ImportCorrectionList correctionList) {
        return null;
    }

    @Override
    public String extractDecision(SourceScJudgment sourceJudgment, ImportCorrectionList correctionList) {
        return null;
    }

    @Override
    public List<String> extractCourtReporters(SourceScJudgment sourceJudgment, ImportCorrectionList correctionList) {
        return Lists.newArrayList();
    }

    @Override
    public LocalDate extractJudgmentDate(SourceScJudgment sourceJudgment, ImportCorrectionList correctionList) {
        return sourceJudgment.getJudgmentDate();
    }

    @Override
    public String extractSourceJudgmentId(SourceScJudgment sourceJudgment, ImportCorrectionList correctionList) {
        return sourceJudgment.getSource().getSourceJudgmentId();
    }

    @Override
    public String extractSourceJudgmentUrl(SourceScJudgment sourceJudgment, ImportCorrectionList correctionList) {
        return sourceJudgment.getSource().getSourceJudgmentUrl();
    }

    @Override
    public SourceCode getSourceCode() {
        return SourceCode.SUPREME_COURT;
    }

    @Override
    public void convertSpecific(SupremeCourtJudgment judgment, SourceScJudgment sourceJudgment, ImportCorrectionList correctionList) {
        judgment.setPersonnelType(extractPersonnelType(sourceJudgment, correctionList));
        judgment.setScJudgmentForm(extractSupremeCourtJudgmentForm(sourceJudgment, correctionList));
        extractSupremeCourtChambers(sourceJudgment, correctionList).stream().filter(scChamber->!judgment.containsScChamber(scChamber)).forEach(scChamber->judgment.addScChamber(scChamber));
        judgment.setScChamberDivision(extractSupremeCourtChamberDivision(sourceJudgment, correctionList));
    }

    
    
    
    //------------------------ PRIVATE --------------------------
    
     
    private PersonnelType extractPersonnelType(SourceScJudgment sourceJudgment, ImportCorrectionList correctionList) {
        if (sourceJudgment.getPersonnelType() == null) {
            return null;
        }
        return PersonnelType.valueOf(sourceJudgment.getPersonnelType());
    }
    
    
    private List<SupremeCourtChamber> extractSupremeCourtChambers(SourceScJudgment sourceJudgment, ImportCorrectionList correctionList) {
        
        List<SupremeCourtChamber> scChambers = Lists.newArrayList();
        
        for (String scChamberName : sourceJudgment.getSupremeCourtChambers()) {
            SupremeCourtChamber scChamber = extractScChamber(scChamberName, correctionList);
            scChambers.add(scChamber);
        }
        
        return scChambers;
    }

    
    private SupremeCourtChamber extractScChamber(String scChamberName, ImportCorrectionList correctionList) {
        
        String normalizedChamberName = scChamberNameNormalizer.normalize(scChamberName);
        SupremeCourtChamber scChamber = scChamberCreator.getOrCreateScChamber(normalizedChamberName);
        
        checkAndSaveCorrection(correctionList, scChamber, scChamberName, normalizedChamberName);
        
        return scChamber;
    }

    
    private void checkAndSaveCorrection(ImportCorrectionList correctionList, SupremeCourtChamber scChamber, String scChamberName, String normalizedChamberName) {
        
        if (scChamberNameNormalizer.isChangedByNormalization(scChamberName)) {
            correctionList.addCorrection(new ImportCorrection(scChamber, CorrectedProperty.SC_CHAMBER_NAME, scChamberName.trim(), normalizedChamberName));
        }
    }

    
    private SupremeCourtChamberDivision extractSupremeCourtChamberDivision(SourceScJudgment sourceJudgment, ImportCorrectionList correctionList) {
        String scChamberDivisionFullName = sourceJudgment.getSupremeCourtChamberDivision();
        if (StringUtils.isBlank(scChamberDivisionFullName)) {
            return null;
        }
        
        return scChamberDivisionCreator.getOrCreateScChamberDivision(scChamberDivisionFullName);
        
    }
    
    
    private SupremeCourtJudgmentForm extractSupremeCourtJudgmentForm(SourceScJudgment sourceJudgment, ImportCorrectionList correctionList) {
        
        String sourceScJudgmentFormName = sourceJudgment.getSupremeCourtJudgmentForm();
        
        if (StringUtils.isBlank(sourceScJudgmentFormName)) {
            return null;
        }
        
        String normalizedJudgmentFormName = scJudgmentFormNameNormalizer.normalize(sourceScJudgmentFormName);
        
                
        SupremeCourtJudgmentForm scJudgmentForm = scJudgmentFormCreator.getOrCreateScJudgmentForm(normalizedJudgmentFormName);
        
        checkAndSaveCorrection(correctionList, scJudgmentForm, sourceScJudgmentFormName, normalizedJudgmentFormName);
        
        return scJudgmentForm;
        
    }

    
    private void checkAndSaveCorrection(ImportCorrectionList correctionList, SupremeCourtJudgmentForm scJudgmentForm, String sourceScJudgmentFormName, String normalizedJudgmentFormName) {
        
        if (scJudgmentFormNameNormalizer.isChangedByNormalization(sourceScJudgmentFormName)) {
            correctionList.addCorrection(new ImportCorrection(scJudgmentForm, CorrectedProperty.SC_JUDGMENT_FORM_NAME, sourceScJudgmentFormName, normalizedJudgmentFormName));
        }
        
    }
    
    
    //------------------------ SETTERS --------------------------
    
    
    @Autowired
    public void setScJudgmentFormCreator(ScJudgmentFormCreator scJudgmentFormCreator) {
        this.scJudgmentFormCreator = scJudgmentFormCreator;
    }

    @Autowired
    public void setScChamberNameNormalizer(ScChamberNameNormalizer scChamberNameNormalizer) {
        this.scChamberNameNormalizer = scChamberNameNormalizer;
    }

    @Autowired
    public void setScChamberCreator(ScChamberCreator scChamberCreator) {
        this.scChamberCreator = scChamberCreator;
    }

    @Autowired
    public void setScJudgmentFormConverter(ScJudgmentFormConverter scJudgmentFormConverter) {
        this.scJudgmentFormConverter = scJudgmentFormConverter;
    }

    @Autowired
    public void setScJudgmentFormNameNormalizer(ScJudgmentFormNameNormalizer scJudgmentFormNameNormalizer) {
        this.scJudgmentFormNameNormalizer = scJudgmentFormNameNormalizer;
    }

    @Autowired
    public void setScChamberDivisionCreator(ScChamberDivisionCreator scChamberDivisionCreator) {
        this.scChamberDivisionCreator = scChamberDivisionCreator;
    }

    @Autowired
    public void setJudgeConverter(JudgeConverter judgeConverter) {
        this.judgeConverter = judgeConverter;
    }

}
