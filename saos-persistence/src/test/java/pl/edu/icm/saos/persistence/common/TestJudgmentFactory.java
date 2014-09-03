package pl.edu.icm.saos.persistence.common;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.*;
import pl.edu.icm.saos.persistence.model.Judge.JudgeRole;

import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;

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

        judgment.setDecision("judgment decision");
        
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
        keyword2.setPhrase("Abc2");
        
        judgment.addKeyword(keyword1);
        judgment.addKeyword(keyword2);

        JudgmentSourceInfo sourceInfo = new JudgmentSourceInfo();
        sourceInfo.setPublisher("S I Publisher");
        sourceInfo.setPublicationDate(new DateTime());
        sourceInfo.setReviser("S I Reviser");
        sourceInfo.setSourceCode(SourceCode.COMMON_COURT);
        sourceInfo.setSourceJudgmentId("999666");
        sourceInfo.setSourceJudgmentUrl("http://iiiiiii/sssss/pl");
        judgment.setSourceInfo(sourceInfo);
        
        
        JudgmentReasoning reasoning = new JudgmentReasoning();
        judgment.setReasoning(reasoning);
        reasoning.setJudgment(judgment);
        reasoning.setText("Reasoning of the judgment");
        JudgmentSourceInfo reasoningSourceInfo = new JudgmentSourceInfo();
        reasoningSourceInfo.setPublisher("J Publisher");
        reasoningSourceInfo.setPublicationDate(new DateTime());
        reasoningSourceInfo.setReviser("J Reviser");
        reasoningSourceInfo.setSourceCode(SourceCode.COMMON_COURT);
        reasoningSourceInfo.setSourceJudgmentId("1234565");
        reasoningSourceInfo.setSourceJudgmentUrl("http://sssss/sssss/pl");
        reasoning.setSourceInfo(reasoningSourceInfo);
        
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

    @Transactional
    public List<CommonCourtJudgment> createSimpleCcJudgments(boolean save){
        CommonCourtJudgment firstJudgment = new CommonCourtJudgment();
        firstJudgment.setCaseNumber("A");
        firstJudgment.setJudgmentDate(new LocalDate(10000000000L));

        CommonCourtJudgment secondJudgment = new CommonCourtJudgment();
        secondJudgment.setCaseNumber("B");
        secondJudgment.setJudgmentDate(new LocalDate(20000000000L));


        CommonCourtJudgment thirdJudgment = new CommonCourtJudgment();
        thirdJudgment.setCaseNumber("C");
        thirdJudgment.setJudgmentDate(new LocalDate(30000000000L));


        CommonCourtJudgment fourthJudgment = new CommonCourtJudgment();
        fourthJudgment.setCaseNumber("D");
        fourthJudgment.setJudgmentDate(new LocalDate(40000000000L));


        if(save){
            entityManager.persist(firstJudgment);
            entityManager.persist(secondJudgment);
            entityManager.persist(thirdJudgment);
            entityManager.persist(fourthJudgment);
            entityManager.flush();
        }


        return Arrays.asList(firstJudgment, secondJudgment, thirdJudgment, fourthJudgment);
    }

}
