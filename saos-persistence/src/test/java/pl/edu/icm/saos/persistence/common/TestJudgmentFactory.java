package pl.edu.icm.saos.persistence.common;

import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.chrono.ISOChronology;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.icm.saos.persistence.model.*;
import pl.edu.icm.saos.persistence.model.Judge.JudgeRole;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
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
        judgment.addCourtCase(new CourtCase(RandomStringUtils.randomAlphanumeric(10)));
        return judgment;
    }

    @Transactional
    public CommonCourt createFullCommonCourt(boolean save){
        CommonCourt court = new CommonCourt();
        court.setCode("ABC_COURT_CODE");
        court.setName("ABC cour name");
        court.setType(CommonCourt.CommonCourtType.APPEAL);

        CommonCourtDivision firstDivision = new CommonCourtDivision();
        firstDivision.setCode("F_D");
        firstDivision.setName("F_D Division name");
        CommonCourtDivisionType firstType = new CommonCourtDivisionType();
        firstType.setCode("00003333");
        firstType.setName("Karny");
        firstDivision.setType(firstType);
        court.addDivision(firstDivision);

        CommonCourtDivision secondDivision = new CommonCourtDivision();
        secondDivision.setCode("S_D");
        secondDivision.setName("S_D Division name");
        CommonCourtDivisionType secondType = new CommonCourtDivisionType();
        secondType.setCode("00006666");
        secondType.setName("Cywilny");
        secondDivision.setType(secondType);
        court.addDivision(secondDivision);

        if(save){
            entityManager.persist(firstType);
            entityManager.persist(secondType);
            entityManager.persist(firstDivision);
            entityManager.persist(secondDivision);
            entityManager.persist(court);
            entityManager.flush();
        }



        return court;
    }

    @Transactional
    public List<CommonCourt> createSimpleCommonCourts(boolean save){
        CommonCourt aCourt = new CommonCourt();
        aCourt.setCode("A_CODE");
        aCourt.setName("A name");
        aCourt.setType(CommonCourt.CommonCourtType.APPEAL);

        CommonCourt bCourt = new CommonCourt();
        bCourt.setCode("B_CODE");
        bCourt.setName("B name");
        bCourt.setType(CommonCourt.CommonCourtType.DISTRICT);

        CommonCourt cCourt = new CommonCourt();
        cCourt.setCode("C_CODE");
        cCourt.setName("C name");
        cCourt.setType(CommonCourt.CommonCourtType.DISTRICT);


        CommonCourt dCourt = new CommonCourt();
        dCourt.setCode("D_CODE");
        dCourt.setName("D name");
        dCourt.setType(CommonCourt.CommonCourtType.REGIONAL);

        if(save){
            entityManager.persist(aCourt);
            entityManager.persist(bCourt);
            entityManager.persist(cCourt);
            entityManager.persist(dCourt);
            entityManager.flush();
        }

        return Arrays.asList(aCourt, bCourt, cCourt, dCourt);
    }
    
    @Transactional
    public CommonCourtJudgment createFullCcJudgment(boolean save) {
        CommonCourtJudgment judgment = new CommonCourtJudgment();
        judgment.addCourtCase(new CourtCase(RandomStringUtils.randomAlphanumeric(10)));

        judgment.setJudgmentDate(new LocalDate(2020, 10, 24 , ISOChronology.getInstanceUTC()));

        judgment.addCourtReporter("Adam Nowak");
        judgment.addCourtReporter("Jan Kowalski");
        Judge judge = new Judge("Monkey Donkey", JudgeRole.PRESIDING_JUDGE);
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
        
        
        judgment.addLegalBase("ABC");
        judgment.addLegalBase("BCA");

        if (save) {
            entityManager.persist(keyword1);
            entityManager.persist(keyword2);
            entityManager.persist(court);
            entityManager.persist(division);
            entityManager.persist(entry);
            entityManager.persist(judgment);
            entityManager.flush();
        }
        return judgment;
    }

    @Transactional
    public List<CommonCourtJudgment> createSimpleCcJudgments(boolean save){
        CommonCourtJudgment firstJudgment = new CommonCourtJudgment();
        
        firstJudgment.addCourtCase(new CourtCase("A"));
        firstJudgment.setJudgmentDate(new LocalDate(10000000000L));

        CommonCourtJudgment secondJudgment = new CommonCourtJudgment();
        secondJudgment.addCourtCase(new CourtCase("B"));
        secondJudgment.setJudgmentDate(new LocalDate(20000000000L));


        CommonCourtJudgment fourthJudgment = new CommonCourtJudgment();
        fourthJudgment.addCourtCase(new CourtCase("D"));
        fourthJudgment.setJudgmentDate(new LocalDate(40000000000L));


        CommonCourtJudgment thirdJudgment = new CommonCourtJudgment();
        thirdJudgment.addCourtCase(new CourtCase("C"));
        thirdJudgment.setJudgmentDate(new LocalDate(30000000000L));


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
