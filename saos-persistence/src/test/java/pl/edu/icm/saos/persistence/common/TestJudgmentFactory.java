package pl.edu.icm.saos.persistence.common;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.CcJudgmentKeyword;
import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.CommonCourtDivision;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judge.JudgeRole;
import pl.edu.icm.saos.persistence.model.JudgmentReasoning;
import pl.edu.icm.saos.persistence.model.JudgmentReferencedRegulation;
import pl.edu.icm.saos.persistence.model.JudgmentSourceInfo;
import pl.edu.icm.saos.persistence.model.LawJournalEntry;
import pl.edu.icm.saos.persistence.model.SourceCode;

import com.google.common.collect.Lists;

/**
 * @author Łukasz Dumiszewski
 */
@Service("testJudgmentFactory")
public class TestJudgmentFactory {

    @Autowired
    private EntityManager entityManager;
    
    
    public static CommonCourtJudgment createSimpleCcJudgment() {
        CommonCourtJudgment judgment = new CommonCourtJudgment();
        judgment.setCaseNumber(RandomStringUtils.randomAlphanumeric(10));
        return judgment;
    }
    
    @Transactional
    public CommonCourtJudgment createFullCcJudgment(boolean save) {
        CommonCourtJudgment judgment = new CommonCourtJudgment();
        judgment.setCaseNumber(RandomStringUtils.randomAlphanumeric(10));
        judgment.addCourtReporter("Adam Nowak");
        judgment.addCourtReporter("Jan Kowalski");
        Judge judge = new Judge();
        judge.setName("Monkey Donkey");
        judge.setSpecialRoles(Lists.newArrayList(JudgeRole.PRESIDING_JUDGE));
        judgment.addJudge(judge);
        
        LawJournalEntry entry = new LawJournalEntry();
        entry.setYear(2011);
        entry.setJournalNo(1223);
        entry.setTitle("sldlskds");
        entry.setEntry(123);
        JudgmentReferencedRegulation regulation = new JudgmentReferencedRegulation();
        regulation.setLawJournalEntry(entry);
        regulation.setRawText("dsdsdsd sd s ds d sd s d");
        judgment.addReferencedRegulation(regulation);
        
        CommonCourt court = new CommonCourt();
        court.setCode("ABC");
        court.setName("ABC Common court");
        CommonCourtDivision division = new CommonCourtDivision();
        division.setCode("ABC_I");
        division.setName("ABC_I Division");
        court.addDivision(division);
        judgment.setCourtDivision(division);
        
        CcJudgmentKeyword keyword1 = new CcJudgmentKeyword();
        keyword1.setPhrase("Abc1");
        
        CcJudgmentKeyword keyword2 = new CcJudgmentKeyword();
        keyword1.setPhrase("Abc2");
        
        judgment.addKeyword(keyword1);
        judgment.addKeyword(keyword2);
        
        
        JudgmentReasoning reasoning = new JudgmentReasoning();
        judgment.setReasoning(reasoning);
        reasoning.setJudgment(judgment);
        reasoning.setText("Reasoning of the judgment");
        JudgmentSourceInfo sourceInfo = new JudgmentSourceInfo();
        sourceInfo.setPublisher("J Publisher");
        sourceInfo.setPublicationDate(new DateTime());
        sourceInfo.setReviser("J Reviser");
        sourceInfo.setSourceCode(SourceCode.COMMON_COURT);
        sourceInfo.setSourceJudgmentId("1234565");
        sourceInfo.setSourceJudgmentUrl("http://sssss/sssss/pl");
        reasoning.setSourceInfo(sourceInfo);
        
        judgment.addLegalBase("ABC");
        judgment.addLegalBase("BCA");
        
        if (save) {
            entityManager.persist(keyword1);
            entityManager.persist(keyword2);
            entityManager.persist(court);
            entityManager.persist(division);
            entityManager.persist(entry);
            entityManager.persist(judgment);
            entityManager.persist(reasoning);
            entityManager.flush();
        }
        return judgment;
    }
}
