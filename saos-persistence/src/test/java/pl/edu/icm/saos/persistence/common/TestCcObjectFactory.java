package pl.edu.icm.saos.persistence.common;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.icm.saos.persistence.model.*;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.*;

/**
 * @author pavtel
 */
@Service
public class TestCcObjectFactory {

    @Autowired
    private EntityManager entityManager;


    //------------------------ LOGIC --------------------------

    /**
     * Creates {@link CommonCourtJudgment} hierarchy with default field data.
     * @return CommonCourtJudgment.
     */
    @Transactional
    public CommonCourtJudgment createCcJudgment(boolean save){
        CommonCourtJudgment ccJudgment = new CommonCourtJudgment();

        CommonCourt commonCourt = createCcCourt(save);
        ccJudgment.setCourtDivision(commonCourt.getDivisions().get(0));

        CourtCase courtCase = new CourtCase(CC_CASE_NUMBER);
        ccJudgment.addCourtCase(courtCase);

        ccJudgment.addLegalBase(CC_FIRST_LEGAL_BASE);
        ccJudgment.addLegalBase(CC_SECOND_LEGAL_BASE);

        ccJudgment.addCourtReporter(CC_FIRST_COURT_REPORTER);
        ccJudgment.addCourtReporter(CC_SECOND_COURT_REPORTER);

        ccJudgment.setDecision(CC_DECISION);
        ccJudgment.setSummary(CC_SUMMARY);
        ccJudgment.setJudgmentType(CC_JUDGMENT_TYPE);
        ccJudgment.setTextContent(CC_TEXT_CONTENT);
        ccJudgment.setJudgmentDate(new LocalDate(CC_DATE_YEAR, CC_DATE_MONTH, CC_DATE_DAY));


        LawJournalEntry firstLawJournalEntry = new LawJournalEntry();
        firstLawJournalEntry.setTitle(CC_FIRST_REFERENCED_REGULATION_TITLE);
        firstLawJournalEntry.setYear(CC_FIRST_REFERENCED_REGULATION_YEAR);
        firstLawJournalEntry.setJournalNo(CC_FIRST_REFERENCED_REGULATION_JOURNAL_NO);
        firstLawJournalEntry.setEntry(CC_FIRST_REFERENCED_REGULATION_ENTRY);

        JudgmentReferencedRegulation firstReferencedRegulation = new JudgmentReferencedRegulation();
        firstReferencedRegulation.setRawText(CC_FIRST_REFERENCED_REGULATION_TEXT);
        firstReferencedRegulation.setLawJournalEntry(firstLawJournalEntry);

        LawJournalEntry secondLawJournalEntry = new LawJournalEntry();
        secondLawJournalEntry.setTitle(CC_SECOND_REFERENCED_REGULATION_TITLE);
        secondLawJournalEntry.setYear(CC_SECOND_REFERENCED_REGULATION_YEAR);
        secondLawJournalEntry.setJournalNo(CC_SECOND_REFERENCED_REGULATION_JOURNAL_NO);
        secondLawJournalEntry.setEntry(CC_SECOND_REFERENCED_REGULATION_ENTRY);

        JudgmentReferencedRegulation secondReferencedRegulation = new JudgmentReferencedRegulation();
        secondReferencedRegulation.setRawText(CC_SECOND_REFERENCED_REGULATION_TEXT);
        secondReferencedRegulation.setLawJournalEntry(secondLawJournalEntry);

        LawJournalEntry thirdLawJournalEntry = new LawJournalEntry();
        thirdLawJournalEntry.setTitle(CC_THIRD_REFERENCED_REGULATION_TITLE);
        thirdLawJournalEntry.setYear(CC_THIRD_REFERENCED_REGULATION_YEAR);
        thirdLawJournalEntry.setJournalNo(CC_THIRD_REFERENCED_REGULATION_JOURNAL_NO);
        thirdLawJournalEntry.setEntry(CC_THIRD_REFERENCED_REGULATION_ENTRY);

        JudgmentReferencedRegulation thirdReferencedRegulation = new JudgmentReferencedRegulation();
        thirdReferencedRegulation.setRawText(CC_THIRD_REFERENCED_REGULATION_TEXT);
        thirdReferencedRegulation.setLawJournalEntry(thirdLawJournalEntry);

        ccJudgment.addReferencedRegulation(firstReferencedRegulation);
        ccJudgment.addReferencedRegulation(secondReferencedRegulation);
        ccJudgment.addReferencedRegulation(thirdReferencedRegulation);


        Judge firstJudge = new Judge(CC_FIRST_JUDGE_NAME, CC_FIRST_JUDGE_ROLE);
        Judge secondJudge = new Judge(CC_SECOND_JUDGE_NAME);
        Judge thirdJudge = new Judge(CC_THIRD_JUDGE_NAME);
        ccJudgment.addJudge(firstJudge);
        ccJudgment.addJudge(secondJudge);
        ccJudgment.addJudge(thirdJudge);


        JudgmentSourceInfo sourceInfo = createJudgmentSourceInfo();
        ccJudgment.setSourceInfo(sourceInfo);


        CcJudgmentKeyword firstKeyword = new CcJudgmentKeyword(CC_FIRST_KEYWORD);
        CcJudgmentKeyword secondKeyword = new CcJudgmentKeyword(CC_SECOND_KEYWORD);
        ccJudgment.addKeyword(firstKeyword);
        ccJudgment.addKeyword(secondKeyword);

        if(save){
            entityManager.persist(firstKeyword);
            entityManager.persist(secondKeyword);

            entityManager.persist(firstJudge);
            entityManager.persist(secondJudge);
            entityManager.persist(thirdJudge);

            entityManager.persist(firstLawJournalEntry);
            entityManager.persist(secondLawJournalEntry);
            entityManager.persist(thirdLawJournalEntry);

            entityManager.persist(firstReferencedRegulation);
            entityManager.persist(secondReferencedRegulation);
            entityManager.persist(thirdReferencedRegulation);

            entityManager.persist(courtCase);
            entityManager.persist(ccJudgment);

            entityManager.flush();
        }

        return ccJudgment;
    }



    /**
     * Creates {@link CommonCourt} hierarchy with default field data.
     * @return CommonCourt
     */
    @Transactional
    public CommonCourt createCcCourt(boolean save){
        CommonCourt commonCourt = new CommonCourt();

        commonCourt.setName(CC_COURT_NAME);
        commonCourt.setCode(CC_COURT_CODE);
        commonCourt.setType(CC_COURT_TYPE);

        CommonCourt parent = new CommonCourt();
        parent.setName(CC_COURT_PARENT_NAME);
        parent.setType(CC_COURT_PARENT_TYPE);
        parent.setCode(CC_COURT_PARENT_CODE);
        commonCourt.setParentCourt(parent);

        CommonCourtDivision firstCcDivision = new CommonCourtDivision();
        firstCcDivision.setCode(CC_FIRST_DIVISION_CODE);
        firstCcDivision.setName(CC_FIRST_DIVISION_NAME);
        CommonCourtDivisionType firstCcDivisionType = commonCourtDivisionType(CC_FIRST_DIVISION_TYPE_NAME, CC_FIRST_DIVISION_TYPE_CODE);
        firstCcDivision.setType(firstCcDivisionType);
        commonCourt.addDivision(firstCcDivision);

        CommonCourtDivision secondCcDivision = new CommonCourtDivision();
        secondCcDivision.setCode(CC_SECOND_DIVISION_CODE);
        secondCcDivision.setName(CC_SECOND_DIVISION_NAME);
        CommonCourtDivisionType secondCcDivisionType = commonCourtDivisionType(CC_SECOND_DIVISION_TYPE_NAME, CC_SECOND_DIVISION_TYPE_CODE);
        secondCcDivision.setType(secondCcDivisionType);
        commonCourt.addDivision(secondCcDivision);

        if(save){
            entityManager.persist(commonCourt);
            entityManager.persist(parent);
            entityManager.persist(firstCcDivision);
            entityManager.persist(secondCcDivision);
            entityManager.persist(firstCcDivisionType);
            entityManager.persist(secondCcDivisionType);
            entityManager.flush();
        }

        return commonCourt;
    }

    /**
     * Creates {@link JudgmentSourceInfo} with default field data.
     * @return JudgmentSourceInfo.
     */
    public JudgmentSourceInfo createJudgmentSourceInfo(){
        JudgmentSourceInfo judgmentSourceInfo = new JudgmentSourceInfo();

        judgmentSourceInfo.setSourceCode(CC_SOURCE_CODE);
        judgmentSourceInfo.setSourceJudgmentId(CC_SOURCE_JUDGMENT_ID);
        judgmentSourceInfo.setSourceJudgmentUrl(CC_SOURCE_JUDGMENT_URL);
        judgmentSourceInfo.setPublisher(CC_SOURCE_PUBLISHER);
        judgmentSourceInfo.setReviser(CC_SOURCE_REVISER);
        judgmentSourceInfo.setPublicationDate(new DateTime(SC_SOURCE_PUBLICATION_DATE_IN_MILLISECONDS, DateTimeZone.UTC));

        return judgmentSourceInfo;
    }


    /**
     * Creates {@link CommonCourtJudgment} with minimal set of fields (necessaries for storing in db)
     * filled with random data.
     * @return CommonCourtJudgment
     */
    @Transactional
    public CommonCourtJudgment createSimpleCcJudgment(boolean save){
        CommonCourtJudgment judgment = new CommonCourtJudgment();
        judgment.addCourtCase(new CourtCase(RandomStringUtils.randomAlphanumeric(10)));
        judgment.getSourceInfo().setSourceCode(SourceCode.COMMON_COURT);
        judgment.getSourceInfo().setSourceJudgmentId(RandomStringUtils.randomAlphabetic(20));

        if(save){
            entityManager.persist(judgment);
            entityManager.flush();
        }

        return judgment;
    }


    /**
     * Creates list of {@link CommonCourtJudgment} with fields filled with random values.
     * @param size of the list.
     * @return CommonCourtJudgment
     */
    @Transactional
    public List<CommonCourtJudgment> createCcJudgmentListWithRandomData(int size, boolean save){
        List<CommonCourtJudgment> judgments = new ArrayList<>(size);
        for(int i=0; i<size; ++i){
            String prefix = i + "__";
            CommonCourtJudgment judgment = createCcJudgmentWithRandomData(prefix, save);
            judgments.add(judgment);
        }

        return judgments;

    }


    /**
     * Creates list of {@link CcJudgmentKeyword} with random data and given size.
     * @param size of the list.
     * @return keywords list.
     */
    public List<CcJudgmentKeyword> createCcKeywordListWith(int size, boolean save){
        Preconditions.checkArgument(size <= 0, "size should be positive");

        List<CcJudgmentKeyword> ccKeywords = new ArrayList<>(size);

        for(int i=0; i<size; ++i){
            CcJudgmentKeyword keyword = new CcJudgmentKeyword(UUID.randomUUID().toString());
            ccKeywords.add(keyword);
        }

        if(save){
            ccKeywords.forEach(keyword -> entityManager.persist(keyword));
            entityManager.flush();
        }

        return ccKeywords;
    }

    //------------------------ PRIVATE --------------------------
    private CommonCourtDivisionType commonCourtDivisionType(String name, String code){
        CommonCourtDivisionType commonCourtDivisionType = new CommonCourtDivisionType();
        commonCourtDivisionType.setName(name);
        commonCourtDivisionType.setCode(code);

        return commonCourtDivisionType;
    }

    @Transactional
    private CommonCourtJudgment createCcJudgmentWithRandomData(String prefix, boolean save){
        CommonCourtJudgment ccJudgment = new CommonCourtJudgment();

        ccJudgment.addCourtCase(new CourtCase(prefix+RandomStringUtils.randomAlphanumeric(10)));

        ccJudgment.addLegalBase(RandomStringUtils.randomAlphabetic(8));
        ccJudgment.addLegalBase(RandomStringUtils.randomAlphabetic(8));

        ccJudgment.addCourtReporter(RandomStringUtils.randomAlphabetic(4)+" "+RandomStringUtils.randomAlphabetic(5));
        ccJudgment.addCourtReporter(RandomStringUtils.randomAlphabetic(5)+" "+RandomStringUtils.randomAlphabetic(6));

        ccJudgment.setDecision(prefix+RandomStringUtils.randomAlphabetic(5));
        ccJudgment.setSummary(prefix+RandomStringUtils.randomAlphabetic(5));
        ccJudgment.setJudgmentType(CC_JUDGMENT_TYPE);
        ccJudgment.setTextContent(prefix+RandomStringUtils.randomAlphabetic(5));

        int month = 1 + (int)(Math.random()*12);
        int day = 1 +(int)(Math.random()*28);
        ccJudgment.setJudgmentDate(new LocalDate(2000, month, day));




        Judge firstJudge = new Judge(RandomStringUtils.randomAlphabetic(4)+" "+RandomStringUtils.randomAlphabetic(5),
                Judge.JudgeRole.PRESIDING_JUDGE);
        Judge secondJudge = new Judge(RandomStringUtils.randomAlphabetic(4)+" "+RandomStringUtils.randomAlphabetic(5),
                Judge.JudgeRole.REPORTING_JUDGE);
        Judge thirdJudge = new Judge(RandomStringUtils.randomAlphabetic(4)+" "+RandomStringUtils.randomAlphabetic(5));
        ccJudgment.addJudge(firstJudge);
        ccJudgment.addJudge(secondJudge);
        ccJudgment.addJudge(thirdJudge);


        ccJudgment.getSourceInfo().setSourceCode(SourceCode.COMMON_COURT);
        ccJudgment.getSourceInfo().setSourceJudgmentId(prefix+RandomStringUtils.randomAlphabetic(20));
        ccJudgment.getSourceInfo().setSourceJudgmentUrl("http://example.com/" + RandomStringUtils.randomAlphabetic(20));


        CcJudgmentKeyword firstKeyword = new CcJudgmentKeyword(RandomStringUtils.randomAlphanumeric(18));
        CcJudgmentKeyword secondKeyword = new CcJudgmentKeyword(RandomStringUtils.randomAlphanumeric(19));
        ccJudgment.addKeyword(firstKeyword);
        ccJudgment.addKeyword(secondKeyword);

        if(save){
            entityManager.persist(firstKeyword);
            entityManager.persist(secondKeyword);

            entityManager.persist(firstJudge);
            entityManager.persist(secondJudge);
            entityManager.persist(thirdJudge);

            entityManager.persist(ccJudgment);

            entityManager.flush();
        }

        return ccJudgment;
    }






}
