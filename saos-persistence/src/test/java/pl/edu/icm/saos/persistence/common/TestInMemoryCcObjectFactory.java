package pl.edu.icm.saos.persistence.common;

import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import pl.edu.icm.saos.persistence.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.*;

/**
 * @author pavtel
 */
final class TestInMemoryCcObjectFactory {
    private TestInMemoryCcObjectFactory() {
    }
    //------------------------ LOGIC --------------------------
    /**
     * Creates {@link pl.edu.icm.saos.persistence.model.CommonCourtJudgment} hierarchy with default field data.
     * @return CommonCourtJudgment.
     */
    static CommonCourtJudgment createCcJudgment(){
        CommonCourtJudgment ccJudgment = new CommonCourtJudgment();

        CommonCourt commonCourt = createCcCourt(true);
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
        firstJudge.setFunction(CC_FIRST_JUDGE_FUNCTION);
        Judge secondJudge = new Judge(CC_SECOND_JUDGE_NAME);
        Judge thirdJudge = new Judge(CC_THIRD_JUDGE_NAME);
        ccJudgment.addJudge(firstJudge);
        ccJudgment.addJudge(secondJudge);
        ccJudgment.addJudge(thirdJudge);


        JudgmentSourceInfo sourceInfo = new JudgmentSourceInfo();
        sourceInfo.setSourceCode(CC_SOURCE_CODE);
        sourceInfo.setSourceJudgmentId(CC_SOURCE_JUDGMENT_ID);
        sourceInfo.setSourceJudgmentUrl(CC_SOURCE_JUDGMENT_URL);
        sourceInfo.setPublisher(CC_SOURCE_PUBLISHER);
        sourceInfo.setReviser(CC_SOURCE_REVISER);
        sourceInfo.setPublicationDate(new DateTime(SC_SOURCE_PUBLICATION_DATE_IN_MILLISECONDS, DateTimeZone.UTC));

        ccJudgment.setSourceInfo(sourceInfo);


        CcJudgmentKeyword firstKeyword = new CcJudgmentKeyword(CC_FIRST_KEYWORD);
        CcJudgmentKeyword secondKeyword = new CcJudgmentKeyword(CC_SECOND_KEYWORD);
        ccJudgment.addKeyword(firstKeyword);
        ccJudgment.addKeyword(secondKeyword);


        return ccJudgment;
    }



    /**
     * Creates {@link CommonCourt} hierarchy with default field data.
     * @return CommonCourt
     */

    static CommonCourt createCcCourt(boolean withParent){
        CommonCourt commonCourt = new CommonCourt();

        commonCourt.setName(CC_COURT_NAME);
        commonCourt.setCode(CC_COURT_CODE);
        commonCourt.setType(CC_COURT_TYPE);

        if(withParent) {
            CommonCourt parent = new CommonCourt();
            parent.setName(CC_COURT_PARENT_NAME);
            parent.setType(CC_COURT_PARENT_TYPE);
            parent.setCode(CC_COURT_PARENT_CODE);
            commonCourt.setParentCourt(parent);
        }

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


        return commonCourt;
    }


    /**
     * Creates list of {@link CommonCourt} with fields filled with random values.
     * @param size of the list.
     * @return list of CommonCourt
     */
    static List<CommonCourt> createCcCourtListWithRandomData(int size){
        List<CommonCourt> courts = new ArrayList<>(size);
        for(int i=0; i<size; ++i){
            CommonCourt ccCourt = createCcCourtWithRandomDataForIndex(i);
            courts.add(ccCourt);
        }

        return courts;
    }


    /**
     * Creates {@link CommonCourtJudgment} with minimal set of fields (necessaries for storing in db)
     * filled with random data.
     * @return CommonCourtJudgment
     */
    static CommonCourtJudgment createSimpleCcJudgment(){
        CommonCourtJudgment judgment = new CommonCourtJudgment();
        judgment.addCourtCase(new CourtCase(RandomStringUtils.randomAlphanumeric(10)));
        judgment.getSourceInfo().setSourceCode(SourceCode.COMMON_COURT);
        judgment.getSourceInfo().setSourceJudgmentId(RandomStringUtils.randomAlphabetic(20));


        return judgment;
    }


    /**
     * Creates list of {@link CommonCourtJudgment} with fields filled with random values.
     * @param size of the list.
     * @return list of CommonCourtJudgment
     */
    static List<CommonCourtJudgment> createCcJudgmentListWithRandomData(int size){
        List<CommonCourtJudgment> judgments = new ArrayList<>(size);
        for(int i=0; i<size; ++i){
            String prefix = i + "__";
            CommonCourtJudgment judgment = createCcJudgmentWithRandomData(prefix);
            judgments.add(judgment);
        }

        return judgments;
    }


    /**
     * Creates list of {@link CcJudgmentKeyword} with random data and given size.
     * @param size of the list.
     * @return keywords list.
     */
    static List<CcJudgmentKeyword> createCcKeywordListWithRandomData(int size){
        List<CcJudgmentKeyword> ccKeywords = new ArrayList<>(size);

        for(int i=0; i<size; ++i){
            CcJudgmentKeyword keyword = new CcJudgmentKeyword(UUID.randomUUID().toString());
            ccKeywords.add(keyword);
        }

        return ccKeywords;
    }

    //------------------------ PRIVATE --------------------------
    private static CommonCourtDivisionType commonCourtDivisionType(String name, String code){
        CommonCourtDivisionType commonCourtDivisionType = new CommonCourtDivisionType();
        commonCourtDivisionType.setName(name);
        commonCourtDivisionType.setCode(code);

        return commonCourtDivisionType;
    }


    private static CommonCourtJudgment createCcJudgmentWithRandomData(String prefix){
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




        Judge firstJudge = new Judge(RandomStringUtils.randomAlphabetic(4)+" "+RandomStringUtils.randomAlphabetic(8),
                Judge.JudgeRole.PRESIDING_JUDGE);
        Judge secondJudge = new Judge(RandomStringUtils.randomAlphabetic(5)+" "+RandomStringUtils.randomAlphabetic(7),
                Judge.JudgeRole.REPORTING_JUDGE);
        Judge thirdJudge = new Judge(RandomStringUtils.randomAlphabetic(6)+" "+RandomStringUtils.randomAlphabetic(6));
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


        return ccJudgment;
    }

    private static CommonCourt createCcCourtWithRandomDataForIndex(int i) {
        CommonCourt commonCourt = new CommonCourt();

        String prefix = i+"__";

        commonCourt.setName(prefix + RandomStringUtils.randomAlphabetic(5));
        commonCourt.setCode(RandomStringUtils.randomNumeric(8));
        commonCourt.setType(getRandomCourtType());

        return commonCourt;
    }

    private static CommonCourt.CommonCourtType getRandomCourtType(){
        int nr_of_commonCourtTypes = CommonCourt.CommonCourtType.values().length;
        int randomIndex  = (int)(Math.random()*nr_of_commonCourtTypes);
        int i = 0;
        for(CommonCourt.CommonCourtType courtType : CommonCourt.CommonCourtType.values()){
            if(i == randomIndex){
                return courtType;
            }
            ++i;
        }

        return CommonCourt.CommonCourtType.REGIONAL;
    }



}
