package pl.edu.icm.saos.importer.commoncourt.judgment.process;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.importer.common.AbstractJudgmentConverter;
import pl.edu.icm.saos.importer.commoncourt.judgment.xml.SourceCcJudgment;
import pl.edu.icm.saos.persistence.model.CcJudgmentKeyword;
import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.CommonCourtDivision;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.CourtCase;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judge.JudgeRole;
import pl.edu.icm.saos.persistence.model.Judgment.JudgmentType;
import pl.edu.icm.saos.persistence.model.JudgmentReferencedRegulation;
import pl.edu.icm.saos.persistence.model.LawJournalEntry;
import pl.edu.icm.saos.persistence.model.SourceCode;
import pl.edu.icm.saos.persistence.model.importer.ImportProcessingSkipReason;
import pl.edu.icm.saos.persistence.repository.CcDivisionRepository;
import pl.edu.icm.saos.persistence.repository.CommonCourtRepository;

import com.google.common.collect.Lists;

/**
 * @author Łukasz Dumiszewski
 */

@Service("sourceCcJudgmentConverter")
class SourceCcJudgmentConverter extends AbstractJudgmentConverter<CommonCourtJudgment, SourceCcJudgment> {

    private CommonCourtRepository commonCourtRepository;
    
    private CcDivisionRepository ccDivisionRepository;
    
    private CcJudgmentKeywordCreator ccJudgmentKeywordCreator;
    
    private LawJournalEntryCreator lawJournalEntryCreator;
    
    private LawJournalEntryExtractor lawJournalEntryExtractor;
    
    
    
    
    
    //------------------------ JudgmentConverterTemplate impl --------------------------

    /** 
     * Converts data specific to the judgment type
     * */
    @Override
    protected void extractSpecific(CommonCourtJudgment ccJudgment, SourceCcJudgment sourceJudgment) {
        
        List<CcJudgmentKeyword> keywords = extractKeywords(sourceJudgment);
        for (CcJudgmentKeyword keyword : keywords) {
            if (!ccJudgment.containsKeyword(keyword)) {
                ccJudgment.addKeyword(keyword);
            }
        }
        
        ccJudgment.setCourtDivision(extractCommonCourtDivision(sourceJudgment));
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
    protected String extractSummary(SourceCcJudgment sourceJudgment) {
        return sourceJudgment.getThesis();
    }

    @Override
    protected String extractDecision(SourceCcJudgment sourceJudgment) {
        return sourceJudgment.getDecision();
    }

    @Override
    protected ArrayList<String> extractCourtReporters(SourceCcJudgment sourceJudgment) {
        if (StringUtils.isBlank(sourceJudgment.getRecorder())) {
            return Lists.newArrayList();
        }
        return Lists.newArrayList(sourceJudgment.getRecorder());
    }

    @Override
    protected LocalDate extractJudgmentDate(SourceCcJudgment sourceJudgment) {
        return sourceJudgment.getJudgmentDate();
    }

    @Override
    protected List<CourtCase> extractCourtCases(SourceCcJudgment sourceJudgment) {
        return Lists.newArrayList(new CourtCase(sourceJudgment.getSignature()));
    }
        
    @Override
    protected String extractTextContent(SourceCcJudgment sourceJudgment) {
        return sourceJudgment.getTextContent();
    }
    
    
    @Override
    protected List<JudgmentReferencedRegulation> extractReferencedRegulations(SourceCcJudgment sourceJudgment) {
        
        List<JudgmentReferencedRegulation> regulations = Lists.newArrayList();
        
        for (String reference : sourceJudgment.getReferences()) {
            
            JudgmentReferencedRegulation regulation = new JudgmentReferencedRegulation();
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
    protected SourceCode getSourceType() {
        return SourceCode.COMMON_COURT;
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
            if (!StringUtils.isBlank(judgeName) && !StringUtils.equalsIgnoreCase(sourceJudgment.getChairman(), judgeName)) {
                judges.add(new Judge(judgeName));
            }
        }
        
        return judges;
    }
    
    /**
     * Returns null in case of REASON type
     * @throws CcjImportProcessSkippableException if the type cannot be resolved
     */
    @Override
    protected JudgmentType extractJudgmentType(SourceCcJudgment sourceJudgment) {
        List<String> sjTypes = Lists.newArrayList(sourceJudgment.getTypes());
        CollectionUtils.transform(sjTypes, new Transformer() {
            @Override
            public Object transform(Object object) {
                return ((String)object).toUpperCase().trim();
            }
        });
        if (sjTypes.contains("SENTENCE")) {
            return JudgmentType.SENTENCE;
        }
        
        if (sjTypes.contains("DECISION")) {
            return JudgmentType.DECISION;
        }
        
        if (sjTypes.contains("REGULATION")) {
            return JudgmentType.DECISION;
        }
        
        if (sjTypes.contains("REASON")) {
            return JudgmentType.REASONS;
        }
        
        throw new CcjImportProcessSkippableException("no proper judgment type found in judgment " + sourceJudgment, ImportProcessingSkipReason.UNKNOWN_JUDGMENT_TYPE);
     }

    

    //------------------------ PRIVATE --------------------------
    
    private CommonCourtDivision extractCommonCourtDivision(SourceCcJudgment sourceJudgment) {
        CommonCourt court = commonCourtRepository.findOneByCode(sourceJudgment.getCourtId());
        if (court == null) {
            throw new CcjImportProcessSkippableException("court not found, code = " + sourceJudgment.getCourtId(), ImportProcessingSkipReason.COURT_NOT_FOUND);
        }
        CommonCourtDivision division = ccDivisionRepository.findOneByCourtIdAndCode(court.getId(), StringUtils.leftPad(sourceJudgment.getDepartmentId(), 7, '0'));
        if (division == null) {
            throw new CcjImportProcessSkippableException("court division not found, code = " + sourceJudgment.getDepartmentId(), ImportProcessingSkipReason.COURT_DIVISION_NOT_FOUND);
        }
        return division;
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
    public void setCcDivisionRepository(CcDivisionRepository ccDivisionRepository) {
        this.ccDivisionRepository = ccDivisionRepository;
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

    
}
