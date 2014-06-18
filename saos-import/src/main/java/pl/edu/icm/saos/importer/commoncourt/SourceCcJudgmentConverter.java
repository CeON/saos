package pl.edu.icm.saos.importer.commoncourt;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.common.util.CommonCourtDivisionUtils;
import pl.edu.icm.saos.importer.commoncourt.xml.SourceCcJudgment;
import pl.edu.icm.saos.persistence.model.CcJudgmentKeyword;
import pl.edu.icm.saos.persistence.model.CommonCourtData;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judge.JudgeRole;
import pl.edu.icm.saos.persistence.model.Judgment.JudgmentType;
import pl.edu.icm.saos.persistence.model.JudgmentSource;
import pl.edu.icm.saos.persistence.model.JudgmentSourceType;
import pl.edu.icm.saos.persistence.model.ReferencedRegulation;
import pl.edu.icm.saos.persistence.repository.CcDivisionTypeRepository;
import pl.edu.icm.saos.persistence.repository.CcJudgmentKeywordRepository;
import pl.edu.icm.saos.persistence.repository.CommonCourtRepository;
import pl.edu.icm.saos.persistence.repository.LawJournalEntryRepository;

import com.google.common.collect.Lists;

/**
 * @author Łukasz Dumiszewski
 */

@Service("sourceCcJudgmentConverter")
public class SourceCcJudgmentConverter {

    private CommonCourtRepository commonCourtRepository;
    
    private CcDivisionTypeRepository ccDivisionTypeRepository;
    
    private SourceCcJudgmentUrlFactory sourceCcJudgmentUrlFactory;
    
    private CcJudgmentKeywordRepository ccJudgmentKeywordRepository;
    
    private LawJournalEntryRepository lawJournalEntryRepository;
    
    
    public CommonCourtJudgment convertJudgment(SourceCcJudgment sourceJudgment) {
        CommonCourtJudgment ccJudgment = new CommonCourtJudgment();
        ccJudgment.setCaseNumber(sourceJudgment.getSignature());
        ccJudgment.setCourtData(convertCommonCourt(sourceJudgment));
        ccJudgment.setJudgmentDate(sourceJudgment.getJudgmentDate());
        ccJudgment.setJudgmentSource(convertJudgmentSource(sourceJudgment));
        ccJudgment.setCourtReporters(Lists.newArrayList(sourceJudgment.getRecorder()));
        convertAndAddJudges(sourceJudgment, ccJudgment);
        ccJudgment.setReviser(sourceJudgment.getReviser());
        ccJudgment.setPublisher(sourceJudgment.getPublisher());
        convertAndAddKeywords(sourceJudgment, ccJudgment);
        ccJudgment.setSummary(sourceJudgment.getThesis());
        convertAndSetType(sourceJudgment, ccJudgment);
        
        for (String reference : sourceJudgment.getReferences()) {
            ReferencedRegulation regulation = new ReferencedRegulation();
            LawJournalEntryData entryData = LawJournalEntryExtractor.extractLawJournalEntry(reference);
            if (entryData == null) {
                regulation.setRawText(reference);
            }
            if (entryData != null) {
                //lawJournalEntryRepository.f
            }
        }
        
        return ccJudgment;
    }

    

    
    //------------------------ PRIVATE --------------------------
    

    private JudgmentSource convertJudgmentSource(SourceCcJudgment sourceJudgment) {
        JudgmentSource judgmentSource = new JudgmentSource();
        judgmentSource.setSourceJudgmentId(sourceJudgment.getId());
        judgmentSource.setSourceJudgmentUrl(sourceCcJudgmentUrlFactory.createSourceJudgmentUrl(sourceJudgment.getId()));
        judgmentSource.setSourcePublicationDate(sourceJudgment.getPublicationDate());
        judgmentSource.setSourceType(JudgmentSourceType.COMMON_COURT);
        return judgmentSource;
    }
    
    
    private CommonCourtData convertCommonCourt(SourceCcJudgment sourceJudgment) {
        CommonCourtData commonCourtData = new CommonCourtData();
        commonCourtData.setCourt(commonCourtRepository.getOneByCode(sourceJudgment.getCourtId()));
        commonCourtData.setCourtDivisionType(ccDivisionTypeRepository.findByCode(CommonCourtDivisionUtils.extractDivisionTypeCode(sourceJudgment.getDepartmentId())));
        commonCourtData.setCourtDivisionNumber(CommonCourtDivisionUtils.extractNormalizedDivisionNumber(sourceJudgment.getDepartmentId()));
        return commonCourtData;
    }


    private void convertAndAddJudges(SourceCcJudgment sourceJudgment, CommonCourtJudgment ccJudgment) {
        if (!StringUtils.isBlank(sourceJudgment.getChairman())) {
            ccJudgment.addJudge(new Judge(sourceJudgment.getChairman(), JudgeRole.PRESIDING_JUDGE));
        }
        for (String judgeName : sourceJudgment.getJudges()) {
            if (!StringUtils.equalsIgnoreCase(sourceJudgment.getChairman(), judgeName)) {
                ccJudgment.addJudge(new Judge(judgeName));
            }
        }
    }

    
    private void convertAndAddKeywords(SourceCcJudgment sourceJudgment, CommonCourtJudgment ccJudgment) {
        for (String themePhrase : sourceJudgment.getThemePhrases()) {
            themePhrase = themePhrase.trim().toLowerCase();
            CcJudgmentKeyword keyword = ccJudgmentKeywordRepository.findOneByName(themePhrase);
            if (keyword == null) {
                keyword = new CcJudgmentKeyword();
                keyword.setName(themePhrase);
                ccJudgmentKeywordRepository.save(keyword);
            }
            ccJudgment.addKeyword(keyword);
        }
    }
    
    
    private void convertAndSetType(SourceCcJudgment sourceJudgment, CommonCourtJudgment ccJudgment) {
        
        for (String sourceType : sourceJudgment.getTypes()) {
            String sType = sourceType.trim().toUpperCase();
            if (sType.equalsIgnoreCase("REASON") && sourceJudgment.getTypes().size() == 1) {
                ccJudgment.setJudgmentType(JudgmentType.REASON);
                return;
            }
            if (sType.equalsIgnoreCase("DECISION")) {
                ccJudgment.setJudgmentType(JudgmentType.DECISION);
                return;
            }
            if (sType.equalsIgnoreCase("SENTENCE")) {
                ccJudgment.setJudgmentType(JudgmentType.SENTENCE);
                return;
            }
            
        }
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
    public void setSourceCcJudgmentUrlFactory(SourceCcJudgmentUrlFactory sourceCcJudgmentUrlFactory) {
        this.sourceCcJudgmentUrlFactory = sourceCcJudgmentUrlFactory;
    }

    @Autowired
    public void setCcJudgmentKeywordRepository(CcJudgmentKeywordRepository ccJudgmentKeywordRepository) {
        this.ccJudgmentKeywordRepository = ccJudgmentKeywordRepository;
    }
    
}
