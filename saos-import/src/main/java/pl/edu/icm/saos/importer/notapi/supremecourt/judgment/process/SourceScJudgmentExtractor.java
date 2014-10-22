package pl.edu.icm.saos.importer.notapi.supremecourt.judgment.process;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.importer.common.JudgmentDataExtractor;
import pl.edu.icm.saos.importer.notapi.supremecourt.judgment.json.SourceScJudgment;
import pl.edu.icm.saos.importer.notapi.supremecourt.judgment.json.SourceScJudgment.SourceScJudge;
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
    
     
    
    @Override
    public SupremeCourtJudgment createNewJudgment() {
        return new SupremeCourtJudgment();
    }

    @Override
    public List<CourtCase> extractCourtCases(SourceScJudgment sourceJudgment) {
        return Lists.newArrayList(new CourtCase(sourceJudgment.getCaseNumber()));
    }

    @Override
    public String extractTextContent(SourceScJudgment sourceJudgment) {
        return sourceJudgment.getTextContent();
    }

    @Override
    public DateTime extractPublicationDate(SourceScJudgment sourceJudgment) {
        return sourceJudgment.getSource().getPublicationDateTime();
    }

    @Override
    public String extractPublisher(SourceScJudgment sourceJudgment) {
        return null;
    }

    @Override
    public String extractReviser(SourceScJudgment sourceJudgment) {
        return null;
    }

    @Override
    public List<Judge> extractJudges(SourceScJudgment sourceJudgment) {
        List<Judge> judges = Lists.newArrayList();
        
        for (SourceScJudge scJudge : sourceJudgment.getJudges()) {
            List<JudgeRole> roles = scJudge.getSpecialRoles().stream().map(role->JudgeRole.valueOf(role)).collect(Collectors.toList());
            Judge judge = new Judge(scJudge.getName(), roles);
            judge.setFunction(scJudge.getFunction());
            judges.add(judge);
        }
        
        return judges;
    }

    
    @Override
    public List<JudgmentReferencedRegulation> extractReferencedRegulations(SourceScJudgment sourceJudgment) {
        return Lists.newArrayList();
    }

    
    @Override
    public JudgmentType extractJudgmentType(SourceScJudgment sourceJudgment) {
        //TODO: SAVE CORRECTION
        
        String judgmentFormName = scJudgmentFormNameNormalizer.normalize(sourceJudgment.getSupremeCourtJudgmentForm());
        
        return scJudgmentFormConverter.convertToType(judgmentFormName);
        
    }

    
    @Override
    public List<String> extractLegalBases(SourceScJudgment sourceJudgment) {
        return Lists.newArrayList();
    }

    
    @Override
    public String extractSummary(SourceScJudgment sourceJudgment) {
        return null;
    }

    @Override
    public String extractDecision(SourceScJudgment sourceJudgment) {
        return null;
    }

    @Override
    public List<String> extractCourtReporters(SourceScJudgment sourceJudgment) {
        return Lists.newArrayList();
    }

    @Override
    public LocalDate extractJudgmentDate(SourceScJudgment sourceJudgment) {
        return sourceJudgment.getJudgmentDate();
    }

    @Override
    public String extractSourceJudgmentId(SourceScJudgment sourceJudgment) {
        return sourceJudgment.getSource().getSourceJudgmentId();
    }

    @Override
    public String extractSourceJudgmentUrl(SourceScJudgment sourceJudgment) {
        return sourceJudgment.getSource().getSourceJudgmentUrl();
    }

    @Override
    public SourceCode getSourceCode() {
        return SourceCode.SUPREME_COURT;
    }

    @Override
    public void convertSpecific(SupremeCourtJudgment judgment, SourceScJudgment sourceJudgment) {
        judgment.setPersonnelType(extractPersonnelType(sourceJudgment));
        judgment.setScJudgmentForm(extractSupremeCourtJudgmentForm(sourceJudgment));
        extractSupremeCourtChambers(sourceJudgment).stream().forEach(scChamber->addScChamber(judgment, scChamber));
        judgment.setScChamberDivision(extractSupremeCourtChamberDivision(sourceJudgment));
    }

    
    
    
    //------------------------ PRIVATE --------------------------
    
    private void addScChamber(SupremeCourtJudgment judgment, SupremeCourtChamber scChamber) {
        if (!judgment.containsScChamber(scChamber)) {
            judgment.addScChamber(scChamber);
        }
    }
    
    private PersonnelType extractPersonnelType(SourceScJudgment sourceJudgment) {
        if (sourceJudgment.getPersonnelType() == null) {
            return null;
        }
        return PersonnelType.valueOf(sourceJudgment.getPersonnelType());
    }
    
    
    private List<SupremeCourtChamber> extractSupremeCourtChambers(SourceScJudgment sourceJudgment) {
        // TODO: SAVE CORRECTIONS
        
        List<SupremeCourtChamber> scChambers = Lists.newArrayList();
        
        for (String scChamberName : sourceJudgment.getSupremeCourtChambers()) {
            scChamberName = scChamberNameNormalizer.normalize(scChamberName);
            scChambers.add(scChamberCreator.getOrCreateScChamber(scChamberName));
        }
        
        return scChambers;
    }

    
    private SupremeCourtChamberDivision extractSupremeCourtChamberDivision(SourceScJudgment sourceJudgment) {
        String scChamberDivisionFullName = sourceJudgment.getSupremeCourtChamberDivision();
        if (StringUtils.isBlank(scChamberDivisionFullName)) {
            return null;
        }
        
        return scChamberDivisionCreator.getOrCreateScChamberDivision(scChamberDivisionFullName);
        
    }
    
    
    private SupremeCourtJudgmentForm extractSupremeCourtJudgmentForm(SourceScJudgment sourceJudgment) {
        if (StringUtils.isBlank(sourceJudgment.getSupremeCourtJudgmentForm())) {
            return null;
        }
        
        String judgmentFormName = scJudgmentFormNameNormalizer.normalize(sourceJudgment.getSupremeCourtJudgmentForm());
        
        return scJudgmentFormCreator.getOrCreateScJudgmentForm(judgmentFormName);
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

}
