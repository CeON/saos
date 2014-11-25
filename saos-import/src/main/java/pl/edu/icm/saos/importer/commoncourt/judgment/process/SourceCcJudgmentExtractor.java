package pl.edu.icm.saos.importer.commoncourt.judgment.process;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.importer.common.converter.JudgeConverter;
import pl.edu.icm.saos.importer.common.converter.JudgmentDataExtractor;
import pl.edu.icm.saos.importer.common.correction.ImportCorrection;
import pl.edu.icm.saos.importer.common.correction.ImportCorrectionList;
import pl.edu.icm.saos.importer.commoncourt.judgment.xml.SourceCcJudgment;
import pl.edu.icm.saos.persistence.correction.model.CorrectedProperty;
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

@Service("sourceCcJudgmentExtractor")
public class SourceCcJudgmentExtractor implements JudgmentDataExtractor<CommonCourtJudgment, SourceCcJudgment> {

    private CommonCourtRepository commonCourtRepository;
    
    private CcDivisionRepository ccDivisionRepository;
    
    private CcJudgmentKeywordCreator ccJudgmentKeywordCreator;
    
    private LawJournalEntryCreator lawJournalEntryCreator;
    
    private LawJournalEntryExtractor lawJournalEntryExtractor;
    
    private JudgeConverter judgeConverter;
    
    
    
    //------------------------ LOGIC --------------------------

    /** 
     * Converts data specific to the judgment type
     * */
    @Override
    public void convertSpecific(CommonCourtJudgment ccJudgment, SourceCcJudgment sourceJudgment, ImportCorrectionList correctionList) {
        
        List<CcJudgmentKeyword> keywords = extractKeywords(sourceJudgment, correctionList);
        for (CcJudgmentKeyword keyword : keywords) {
            if (!ccJudgment.containsKeyword(keyword)) {
                ccJudgment.addKeyword(keyword);
            }
        }
        
        ccJudgment.setCourtDivision(extractCommonCourtDivision(sourceJudgment, correctionList));
    }

    @Override
    public CommonCourtJudgment createNewJudgment() {
        return new CommonCourtJudgment();
    }
   
    @Override
    public ArrayList<String> extractLegalBases(SourceCcJudgment sourceJudgment, ImportCorrectionList correctionList) {
        return Lists.newArrayList(sourceJudgment.getLegalBases());
    }

    @Override
    public String extractSummary(SourceCcJudgment sourceJudgment, ImportCorrectionList correctionList) {
        return sourceJudgment.getThesis();
    }

    @Override
    public String extractDecision(SourceCcJudgment sourceJudgment, ImportCorrectionList correctionList) {
        return sourceJudgment.getDecision();
    }

    @Override
    public ArrayList<String> extractCourtReporters(SourceCcJudgment sourceJudgment, ImportCorrectionList correctionList) {
        if (StringUtils.isBlank(sourceJudgment.getRecorder())) {
            return Lists.newArrayList();
        }
        return Lists.newArrayList(sourceJudgment.getRecorder());
    }

    @Override
    public LocalDate extractJudgmentDate(SourceCcJudgment sourceJudgment, ImportCorrectionList correctionList) {
        return sourceJudgment.getJudgmentDate();
    }

    @Override
    public List<CourtCase> extractCourtCases(SourceCcJudgment sourceJudgment, ImportCorrectionList correctionList) {
        return Lists.newArrayList(new CourtCase(sourceJudgment.getSignature()));
    }
        
    @Override
    public String extractTextContent(SourceCcJudgment sourceJudgment, ImportCorrectionList correctionList) {
        return sourceJudgment.getTextContent();
    }
    
    
    @Override
    public List<JudgmentReferencedRegulation> extractReferencedRegulations(SourceCcJudgment sourceJudgment, ImportCorrectionList correctionList) {
        
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
    public SourceCode getSourceCode() {
        return SourceCode.COMMON_COURT;
    }


    @Override
    public String extractSourceJudgmentId(SourceCcJudgment sourceJudgment, ImportCorrectionList correctionList) {
        return sourceJudgment.getId();
    }

    @Override
    public String extractSourceJudgmentUrl(SourceCcJudgment sourceJudgment, ImportCorrectionList correctionList) {
        return sourceJudgment.getSourceUrl();
    }

    @Override
    public String extractPublisher(SourceCcJudgment sourceJudgment, ImportCorrectionList correctionList) {
        return sourceJudgment.getPublisher();
    }

    @Override
    public String extractReviser(SourceCcJudgment sourceJudgment, ImportCorrectionList correctionList) {
        return sourceJudgment.getReviser();
    }

    @Override
    public DateTime extractPublicationDate(SourceCcJudgment sourceJudgment, ImportCorrectionList correctionList) {
        return sourceJudgment.getPublicationDate();
    }
    
    @Override
    public List<Judge> extractJudges(SourceCcJudgment sourceJudgment, ImportCorrectionList correctionList) {
        
        List<Judge> judges = Lists.newArrayList();
        
        if (StringUtils.isNotBlank(sourceJudgment.getChairman())) {
            Judge judge = judgeConverter.convertJudge(sourceJudgment.getChairman(), Lists.newArrayList(JudgeRole.PRESIDING_JUDGE), correctionList);
            if (judge != null) {
                judges.add(judge);
            }
        }
        
        if (CollectionUtils.isEmpty(sourceJudgment.getJudges())) {
            return judges;
        }
        
        boolean chairmanAppeared = false;
        for (String judgeName : sourceJudgment.getJudges()) {
            
            // first occurrence of judge with name equal to chairman's name is a normal situation (it's always doubled) - omit it
            // the next time - consider it to be a different person or mistake
            if (StringUtils.equalsIgnoreCase(sourceJudgment.getChairman(), judgeName) && !chairmanAppeared) {
                chairmanAppeared = true;
                continue;
            }
            
            if (StringUtils.isBlank(judgeName)) {
                continue;
            }

            Judge judge = judgeConverter.convertJudge(judgeName, correctionList);
            
            if (judge != null) {
                judges.add(judge);
            }

        }
        
        return judges;
    }
    
    /**
     * Returns {@link JudgmentType} corresponding to the {@link SourceCcJudgment#getTypes()} <br/>
     * Saves a relevant {@link ImportCorrection} if the source judgment type cannot be directly mapped
     * into an appropriate {@link JudgmentType}
     */
    @Override
    public JudgmentType extractJudgmentType(SourceCcJudgment sourceJudgment, ImportCorrectionList correctionList) {
        List<String> sjTypes = Lists.newArrayList(sourceJudgment.getTypes());
        sjTypes.replaceAll(sType->sType.toUpperCase().trim());
        
        JudgmentType judgmentType = null;
        
        if (sjTypes.contains("SENTENCE")) {
            judgmentType = JudgmentType.SENTENCE;
        }
        
        else if (sjTypes.contains("DECISION")) {
            judgmentType = JudgmentType.DECISION;
        }
        
        else if (sjTypes.contains("REGULATION")) {
            judgmentType = JudgmentType.REGULATION;
        }
        
        else if (sjTypes.contains("REASON")) {
            judgmentType = JudgmentType.REASONS;
        }
        
        
        
        if (sjTypes.size() > 1 || judgmentType == null) {
            String oldValue = sjTypes.stream().reduce((t, u) -> t + ", " + u).get();
            judgmentType = judgmentType==null? JudgmentType.SENTENCE: judgmentType;
            correctionList.addCorrection(new ImportCorrection(null, CorrectedProperty.JUDGMENT_TYPE, oldValue, judgmentType.name()));
        }
        
        return judgmentType;
      
     }

    

    //------------------------ PRIVATE --------------------------
    
    private CommonCourtDivision extractCommonCourtDivision(SourceCcJudgment sourceJudgment, ImportCorrectionList correctionList) {
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
    
    private List<CcJudgmentKeyword> extractKeywords(SourceCcJudgment sourceJudgment, ImportCorrectionList correctionList) {
        List<CcJudgmentKeyword> keywords = Lists.newArrayList();
        if (CollectionUtils.isEmpty(sourceJudgment.getThemePhrases())) {
            return keywords;
        }
        for (String themePhrase : sourceJudgment.getThemePhrases()) {
            themePhrase = themePhrase.toLowerCase(Locale.ROOT);
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

    @Autowired
    public void setJudgeConverter(JudgeConverter judgeConverter) {
        this.judgeConverter = judgeConverter;
    }

    
}
