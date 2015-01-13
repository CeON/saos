package pl.edu.icm.saos.importer.notapi.supremecourt.judgment.process;

import static pl.edu.icm.saos.importer.common.correction.ImportCorrectionBuilder.createUpdate;
import static pl.edu.icm.saos.persistence.correction.model.CorrectedProperty.NAME;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.importer.common.converter.JudgeConverter;
import pl.edu.icm.saos.importer.common.converter.JudgmentDataExtractor;
import pl.edu.icm.saos.importer.common.converter.JudgmentDataExtractorAdapter;
import pl.edu.icm.saos.importer.common.correction.ImportCorrectionList;
import pl.edu.icm.saos.importer.notapi.common.SourceJudgment.SourceJudge;
import pl.edu.icm.saos.importer.notapi.supremecourt.judgment.json.SourceScJudgment;
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
public class SourceScJudgmentExtractor extends JudgmentDataExtractorAdapter<SupremeCourtJudgment, SourceScJudgment> {

    
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
    public JudgmentType extractJudgmentType(SourceScJudgment sourceJudgment, ImportCorrectionList correctionList) {
        return scJudgmentFormConverter.convertToJudgmentType(sourceJudgment.getSupremeCourtJudgmentForm(), correctionList);
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
            correctionList.addCorrection(createUpdate(scChamber)
                                           .ofProperty(NAME).oldValue(scChamberName.trim()).newValue(normalizedChamberName)
                                           .build());
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
            correctionList.addCorrection(createUpdate(scJudgmentForm)
                                           .ofProperty(NAME).oldValue(sourceScJudgmentFormName).newValue(normalizedJudgmentFormName)
                                           .build());
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
