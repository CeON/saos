package pl.edu.icm.saos.importer.commoncourt.process;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.common.util.CommonCourtDivisionUtils;
import pl.edu.icm.saos.importer.common.AbstractJudgmentConverter;
import pl.edu.icm.saos.importer.commoncourt.xml.SourceCcJudgment;
import pl.edu.icm.saos.persistence.model.CcJudgmentKeyword;
import pl.edu.icm.saos.persistence.model.CommonCourtData;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judge.JudgeRole;
import pl.edu.icm.saos.persistence.model.Judgment.JudgmentType;
import pl.edu.icm.saos.persistence.model.JudgmentSourceType;
import pl.edu.icm.saos.persistence.model.LawJournalEntry;
import pl.edu.icm.saos.persistence.model.ReferencedRegulation;
import pl.edu.icm.saos.persistence.repository.CcDivisionTypeRepository;
import pl.edu.icm.saos.persistence.repository.CommonCourtRepository;

import com.google.common.collect.Lists;

/**
 * @author Łukasz Dumiszewski
 */

@Service("sourceCcJudgmentConverter")
public class SourceCcJudgmentConverter extends AbstractJudgmentConverter<CommonCourtJudgment, SourceCcJudgment> {

    private CommonCourtRepository commonCourtRepository;
    
    private CcDivisionTypeRepository ccDivisionTypeRepository;
    
    private CcJudgmentKeywordCreator ccJudgmentKeywordCreator;
    
    private LawJournalEntryCreator lawJournalEntryCreator;
    
    private LawJournalEntryExtractor lawJournalEntryExtractor;
    
    private CcjReasoningExtractor ccjReasoningExtractor;
    
    
    
    
    //------------------------ JudgmentConverterTemplate impl --------------------------

    /** 
     * Converts data specific to the judgment type
     * */
    @Override
    protected void extractSpecific(SourceCcJudgment sourceJudgment, CommonCourtJudgment ccJudgment) {
        
        List<CcJudgmentKeyword> keywords = extractKeywords(sourceJudgment);
        for (CcJudgmentKeyword keyword : keywords) {
            ccJudgment.addKeyword(keyword);
        }
        
        ccJudgment.setCourtData(extractCommonCourtData(sourceJudgment));
    }

    @Override
    protected CommonCourtJudgment createNewJudgment() {
        return new CommonCourtJudgment();
    }
   
    @Override
    protected ArrayList<String> extractLegalBases(SourceCcJudgment sourceJudgment) {
        return Lists.newArrayList(sourceJudgment.getLegalBases());
    }

    @Override
    protected String extractThesis(SourceCcJudgment sourceJudgment) {
        return sourceJudgment.getThesis();
    }

    @Override
    protected String extractDecision(SourceCcJudgment sourceJudgment) {
        return sourceJudgment.getDecision();
    }

    @Override
    protected ArrayList<String> extractCourtReporters(SourceCcJudgment sourceJudgment) {
        return Lists.newArrayList(sourceJudgment.getRecorder());
    }

    @Override
    protected LocalDate extractJudgmentDate(SourceCcJudgment sourceJudgment) {
        return sourceJudgment.getJudgmentDate();
    }

    @Override
    protected String extractCaseNumber(SourceCcJudgment sourceJudgment) {
        return sourceJudgment.getSignature();
    }
        
    @Override
    protected String extractTextContent(SourceCcJudgment sourceJudgment) {
        return ccjReasoningExtractor.removeReasoningText(sourceJudgment.getTextContent());
    }
    
    @Override
    protected String extractReasoningText(SourceCcJudgment sourceJudgment) {
        return ccjReasoningExtractor.extractReasoningText(sourceJudgment.getTextContent());
    }
    
    
    @Override
    protected List<ReferencedRegulation> extractReferencedRegulations(SourceCcJudgment sourceJudgment) {
        
        List<ReferencedRegulation> regulations = Lists.newArrayList();
        
        for (String reference : sourceJudgment.getReferences()) {
            
            ReferencedRegulation regulation = new ReferencedRegulation();
            LawJournalEntryData entryData = lawJournalEntryExtractor.extractLawJournalEntry(reference);
            
            regulation.setRawText(reference);
            
            if (entryData != null) {
                LawJournalEntry lawJournalEntry = lawJournalEntryCreator.getOrCreateLawJournalEntry(entryData);
                regulation.setLawJournalEntry(lawJournalEntry);
            }
            
            regulations.add(regulation);
        
        }
        
        return regulations;
    }

    @Override
    protected JudgmentSourceType getSourceType() {
        return JudgmentSourceType.COMMON_COURT;
    }


    @Override
    protected String extractSourceJudgmentId(SourceCcJudgment sourceJudgment) {
        return sourceJudgment.getId();
    }

    @Override
    protected String extractSourceUrl(SourceCcJudgment sourceJudgment) {
        return sourceJudgment.getSourceUrl();
    }

    @Override
    protected String extractPublisher(SourceCcJudgment sourceJudgment) {
        return sourceJudgment.getPublisher();
    }

    @Override
    protected String extractReviser(SourceCcJudgment sourceJudgment) {
        return sourceJudgment.getReviser();
    }

    @Override
    protected DateTime extractPublicationDate(SourceCcJudgment sourceJudgment) {
        return sourceJudgment.getPublicationDate();
    }
    
    @Override
    protected List<Judge> extractJudges(SourceCcJudgment sourceJudgment) {
        
        List<Judge> judges = Lists.newArrayList();
        
        if (!StringUtils.isBlank(sourceJudgment.getChairman())) {
            judges.add(new Judge(sourceJudgment.getChairman(), JudgeRole.PRESIDING_JUDGE));
        }
        for (String judgeName : sourceJudgment.getJudges()) {
            if (!StringUtils.equalsIgnoreCase(sourceJudgment.getChairman(), judgeName)) {
                judges.add(new Judge(judgeName));
            }
        }
        
        return judges;
    }
    
    /**
     * If the type cannot be resolved then returns null
     */
    @Override
    protected JudgmentType extractJudgmentType(SourceCcJudgment sourceJudgment) {
        for (String sourceType : sourceJudgment.getTypes()) {
            String sType = sourceType.trim().toUpperCase();
            if (sType.equalsIgnoreCase("DECISION")) {
                return JudgmentType.DECISION;
            }
            if (sType.equalsIgnoreCase("SENTENCE")) {
                return JudgmentType.SENTENCE;
            } 
            
        }
        return null;
    }

    

    //------------------------ PRIVATE --------------------------
    
    private CommonCourtData extractCommonCourtData(SourceCcJudgment sourceJudgment) {
        CommonCourtData commonCourtData = new CommonCourtData();
        commonCourtData.setCourt(commonCourtRepository.getOneByCode(sourceJudgment.getCourtId()));
        commonCourtData.setDivisionType(ccDivisionTypeRepository.findByCode(CommonCourtDivisionUtils.extractDivisionTypeCode(sourceJudgment.getDepartmentId())));
        commonCourtData.setDivisionNumber(CommonCourtDivisionUtils.extractNormalizedDivisionNumber(sourceJudgment.getDepartmentId()));
        return commonCourtData;
    }
    
    private List<CcJudgmentKeyword> extractKeywords(SourceCcJudgment sourceJudgment) {
        List<CcJudgmentKeyword> keywords = Lists.newArrayList();
        for (String themePhrase : sourceJudgment.getThemePhrases()) {
            themePhrase = themePhrase.toLowerCase();
            CcJudgmentKeyword keyword = ccJudgmentKeywordCreator.getOrCreateCcJudgmentKeyword(themePhrase);
            keywords.add(keyword);
        }
        return keywords;
    }
    
    


    
    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setCommonCourtRepository(CommonCourtRepository commonCourtRepository) {
        this.commonCourtRepository = commonCourtRepository;
    }

    @Autowired
    public void setCcDivisionTypeRepository(CcDivisionTypeRepository ccDivisionTypeRepository) {
        this.ccDivisionTypeRepository = ccDivisionTypeRepository;
    }

    @Autowired
    public void setCcJudgmentKeywordCreator(CcJudgmentKeywordCreator ccJudgmentKeywordCreator) {
        this.ccJudgmentKeywordCreator = ccJudgmentKeywordCreator;
    }

    @Autowired
    public void setLawJournalEntryCreator(LawJournalEntryCreator lawJournalEntryCreator) {
        this.lawJournalEntryCreator = lawJournalEntryCreator;
    }
    
    @Autowired
    public void setLawJournalEntryExtractor(LawJournalEntryExtractor lawJournalEntryExtractor) {
        this.lawJournalEntryExtractor = lawJournalEntryExtractor;
    }

    @Autowired
    public void setCcjReasoningExtractor(CcjReasoningExtractor ccjReasoningExtractor) {
        this.ccjReasoningExtractor = ccjReasoningExtractor;
    }
}
