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

import static pl.edu.icm.saos.persistence.common.TestObjectsDefaultData.*;

/**
 * Provides factory methods for creating domain objects with default values
 * which can be used in tests.
 * @author pavtel
 */
@Service
public class TestObjectsFactory {


    @Autowired
    private EntityManager entityManager;


    //------------------------ LOGIC --------------------------

    /**
     * Creates {@link TestObjectContext} hierarchy with default field data.
     * @return TestObjectContext.
     */
    @Transactional
    public TestObjectContext createTestObjectContext(boolean save){
        CommonCourtJudgment ccJudgment = createCcJudgment(save);
        SupremeCourtJudgment scJudgment = createScJudgment(save);

        TestObjectContext testObjectContext = new TestObjectContext();
        testObjectContext.setCcJudgment(ccJudgment);
        testObjectContext.setScJudgment(scJudgment);

        return testObjectContext;
    }

    /**
     * Creates {@link CommonCourtJudgment} hierarchy with default field data.
     * @return CommonCourtJudgment.
     */
    @Transactional
    public CommonCourtJudgment createCcJudgment(boolean save){
        CommonCourtJudgment ccJudgment = new CommonCourtJudgment();

        CommonCourt commonCourt = createCcCourt(save);
        ccJudgment.setCourtDivision(commonCourt.getDivisions().get(0));

        CourtCase courtCase = new CourtCase(CASE_NUMBER);
        ccJudgment.addCourtCase(courtCase);

        ccJudgment.addLegalBase(FIRST_LEGAL_BASE);
        ccJudgment.addLegalBase(SECOND_LEGAL_BASE);

        ccJudgment.addCourtReporter(FIRST_COURT_REPORTER);
        ccJudgment.addCourtReporter(SECOND_COURT_REPORTER);

        ccJudgment.setDecision(DECISION);
        ccJudgment.setSummary(SUMMARY);
        ccJudgment.setJudgmentType(JUDGMENT_TYPE);
        ccJudgment.setTextContent(TEXT_CONTENT);
        ccJudgment.setJudgmentDate(new LocalDate(DATE_YEAR, DATE_MONTH, DATE_DAY));


        LawJournalEntry firstLawJournalEntry = new LawJournalEntry();
        firstLawJournalEntry.setTitle(FIRST_REFERENCED_REGULATION_TITLE);
        firstLawJournalEntry.setYear(FIRST_REFERENCED_REGULATION_YEAR);
        firstLawJournalEntry.setJournalNo(FIRST_REFERENCED_REGULATION_JOURNAL_NO);
        firstLawJournalEntry.setEntry(FIRST_REFERENCED_REGULATION_ENTRY);

        JudgmentReferencedRegulation firstReferencedRegulation = new JudgmentReferencedRegulation();
        firstReferencedRegulation.setRawText(FIRST_REFERENCED_REGULATION_TEXT);
        firstReferencedRegulation.setLawJournalEntry(firstLawJournalEntry);

        LawJournalEntry secondLawJournalEntry = new LawJournalEntry();
        secondLawJournalEntry.setTitle(SECOND_REFERENCED_REGULATION_TITLE);
        secondLawJournalEntry.setYear(SECOND_REFERENCED_REGULATION_YEAR);
        secondLawJournalEntry.setJournalNo(SECOND_REFERENCED_REGULATION_JOURNAL_NO);
        secondLawJournalEntry.setEntry(SECOND_REFERENCED_REGULATION_ENTRY);

        JudgmentReferencedRegulation secondReferencedRegulation = new JudgmentReferencedRegulation();
        secondReferencedRegulation.setRawText(SECOND_REFERENCED_REGULATION_TEXT);
        secondReferencedRegulation.setLawJournalEntry(secondLawJournalEntry);

        LawJournalEntry thirdLawJournalEntry = new LawJournalEntry();
        thirdLawJournalEntry.setTitle(THIRD_REFERENCED_REGULATION_TITLE);
        thirdLawJournalEntry.setYear(THIRD_REFERENCED_REGULATION_YEAR);
        thirdLawJournalEntry.setJournalNo(THIRD_REFERENCED_REGULATION_JOURNAL_NO);
        thirdLawJournalEntry.setEntry(THIRD_REFERENCED_REGULATION_ENTRY);

        JudgmentReferencedRegulation thirdReferencedRegulation = new JudgmentReferencedRegulation();
        thirdReferencedRegulation.setRawText(THIRD_REFERENCED_REGULATION_TEXT);
        thirdReferencedRegulation.setLawJournalEntry(thirdLawJournalEntry);

        ccJudgment.addReferencedRegulation(firstReferencedRegulation);
        ccJudgment.addReferencedRegulation(secondReferencedRegulation);
        ccJudgment.addReferencedRegulation(thirdReferencedRegulation);


        Judge firstJudge = new Judge(FIRST_JUDGE_NAME, FIRST_JUDGE_ROLE);
        Judge secondJudge = new Judge(SECOND_JUDGE_NAME);
        Judge thirdJudge = new Judge(THIRD_JUDGE_NAME);
        ccJudgment.addJudge(firstJudge);
        ccJudgment.addJudge(secondJudge);
        ccJudgment.addJudge(thirdJudge);


        JudgmentSourceInfo sourceInfo = createJudgmentSourceInfo();
        sourceInfo.setSourceCode(CC_SOURCE_CODE);
        sourceInfo.setSourceJudgmentId(CC_SOURCE_JUDGMENT_ID);
        ccJudgment.setSourceInfo(sourceInfo);


        CcJudgmentKeyword firstKeyword = new CcJudgmentKeyword(FIRST_KEYWORD);
        CcJudgmentKeyword secondKeyword = new CcJudgmentKeyword(SECOND_KEYWORD);
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
     * Creates {@link SupremeCourtJudgment} hierarchy with default field data.
     * @return SupremeCourtJudgment.
     */
    @Transactional
    public SupremeCourtJudgment createScJudgment(boolean save){
        SupremeCourtJudgment scJudgment = new SupremeCourtJudgment();

        SupremeCourtChamber scChamber = createScChamber(save);
        scJudgment.setScChamberDivision(scChamber.getDivisions().get(0));
        scJudgment.addScChamber(scChamber);


        CourtCase courtCase = new CourtCase(CASE_NUMBER);
        scJudgment.addCourtCase(courtCase);

        scJudgment.addLegalBase(FIRST_LEGAL_BASE);
        scJudgment.addLegalBase(SECOND_LEGAL_BASE);

        scJudgment.addCourtReporter(FIRST_COURT_REPORTER);
        scJudgment.addCourtReporter(SECOND_COURT_REPORTER);

        scJudgment.setDecision(DECISION);
        scJudgment.setSummary(SUMMARY);
        scJudgment.setJudgmentType(JUDGMENT_TYPE);
        scJudgment.setTextContent(TEXT_CONTENT);
        scJudgment.setJudgmentDate(new LocalDate(DATE_YEAR, DATE_MONTH, DATE_DAY));
        scJudgment.setPersonnelType(SC_PERSONNEL_TYPE);


        JudgmentSourceInfo sourceInfo = createJudgmentSourceInfo();
        sourceInfo.setSourceCode(SC_SOURCE_CODE);
        sourceInfo.setSourceJudgmentId(SC_SOURCE_JUDGMENT_ID);
        scJudgment.setSourceInfo(sourceInfo);

        SupremeCourtJudgmentForm scForm = new SupremeCourtJudgmentForm();
        scForm.setName(SC_JUDGMENT_FORM_NAME);
        scJudgment.setScJudgmentForm(scForm);

        if(save){
            entityManager.persist(scForm);
            entityManager.persist(courtCase);
            entityManager.persist(scJudgment);

            entityManager.flush();
        }

        return scJudgment;
    }

    /**
     * Creates {@link SupremeCourtChamber} hierarchy with default field data.
     * @return SupremeCourtJudgment.
     */
    @Transactional
    public SupremeCourtChamber createScChamber(boolean save){
        SupremeCourtChamber scChamber = new SupremeCourtChamber();
        scChamber.setName(SC_CHAMBER_NAME);

        SupremeCourtChamberDivision scDivision = new SupremeCourtChamberDivision();
        scDivision.setName(SC_FIRST_DIVISION_NAME);
        scDivision.setFullName(SC_FIRST_DIVISION_FULL_NAME);
        scChamber.addDivision(scDivision);

        if(save){
            entityManager.persist(scChamber);
            entityManager.persist(scDivision);

            entityManager.flush();
        }

        return scChamber;
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

        judgmentSourceInfo.setSourceCode(SOURCE_CODE);
        judgmentSourceInfo.setSourceJudgmentId(SOURCE_JUDGMENT_ID);
        judgmentSourceInfo.setSourceJudgmentUrl(SOURCE_JUDGMENT_URL);
        judgmentSourceInfo.setPublisher(SOURCE_PUBLISHER);
        judgmentSourceInfo.setReviser(SOURCE_REVISER);
        judgmentSourceInfo.setPublicationDate(new DateTime(SOURCE_PUBLICATION_DATE_IN_MILLISECONDS, DateTimeZone.UTC));

        return judgmentSourceInfo;
    }



    /**
     * Creates {@link SupremeCourtJudgment} with minimal set of fields (necessaries for storing in db)
     * filled with random data.
     * @return SupremeCourtJudgment
     */
    @Transactional
    public SupremeCourtJudgment createSimpleScJudgment(boolean save){
        SupremeCourtJudgment judgment = new SupremeCourtJudgment();
        judgment.addCourtCase(new CourtCase(RandomStringUtils.randomAlphanumeric(10)));
        judgment.getSourceInfo().setSourceCode(SourceCode.SUPREME_COURT);
        judgment.getSourceInfo().setSourceJudgmentId(RandomStringUtils.randomAlphabetic(20));

        if(save){
            entityManager.persist(judgment);
            entityManager.flush();
        }

        return judgment;
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
     * @return CommonCourtJudgment
     */
    @Transactional
    public List<CommonCourtJudgment> createCcJudgmentListWithRandomData(boolean save){
        List<CommonCourtJudgment> judgments = createCcJudgmentListWithRandomData(10, save);
        return judgments;

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
        ccJudgment.setJudgmentType(JUDGMENT_TYPE);
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






}
