package pl.edu.icm.saos.persistence.common;

import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_CASE_NUMBER;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_COURT_TYPE;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_DATE_DAY;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_DATE_MONTH;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_DATE_YEAR;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_DECISION;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_FIRST_APPEAL_DIVISION_CODE;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_FIRST_APPEAL_DIVISION_NAME;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_FIRST_COURT_REPORTER;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_FIRST_DISTRICT_DIVISION_CODE;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_FIRST_DISTRICT_DIVISION_NAME;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_FIRST_DIVISION_TYPE_CODE;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_FIRST_DIVISION_TYPE_NAME;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_FIRST_JUDGE_FUNCTION;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_FIRST_JUDGE_NAME;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_FIRST_JUDGE_ROLE;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_FIRST_KEYWORD;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_FIRST_LEGAL_BASE;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_FIRST_LOWER_COURT_JUDGMENT;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_FIRST_REFERENCED_REGULATION_ENTRY;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_FIRST_REFERENCED_REGULATION_JOURNAL_NO;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_FIRST_REFERENCED_REGULATION_TEXT;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_FIRST_REFERENCED_REGULATION_TITLE;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_FIRST_REFERENCED_REGULATION_YEAR;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_FIRST_REGIONAL_DIVISION_CODE;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_FIRST_REGIONAL_DIVISION_NAME;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_JUDGMENT_RESULT;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_JUDGMENT_TYPE;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_MEANS_OF_APPEAL;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_RECEIPT_DATE_DAY;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_RECEIPT_DATE_MONTH;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_RECEIPT_DATE_YEAR;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_SECOND_APPEAL_DIVISION_CODE;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_SECOND_APPEAL_DIVISION_NAME;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_SECOND_COURT_REPORTER;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_SECOND_DISTRICT_DIVISION_NAME;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_SECOND_DIVISION_TYPE_CODE;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_SECOND_DIVISION_TYPE_NAME;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_SECOND_JUDGE_NAME;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_SECOND_KEYWORD;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_SECOND_LEGAL_BASE;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_SECOND_LOWER_COURT_JUDGMENT;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_SECOND_REFERENCED_REGULATION_ENTRY;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_SECOND_REFERENCED_REGULATION_JOURNAL_NO;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_SECOND_REFERENCED_REGULATION_TEXT;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_SECOND_REFERENCED_REGULATION_TITLE;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_SECOND_REFERENCED_REGULATION_YEAR;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_SECOND_REGIONAL_DIVISION_CODE;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_SECOND_REGIONAL_DIVISION_NAME;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_SOURCE_CODE;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_SOURCE_JUDGMENT_ID;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_SOURCE_JUDGMENT_URL;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_SOURCE_PUBLICATION_DATE_IN_MILLISECONDS;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_SOURCE_PUBLISHER;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_SOURCE_REVISER;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_SUMMARY;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_TEXT_CONTENT;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_TEXT_CONTENT_PATH;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_TEXT_CONTENT_TYPE;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_THIRD_JUDGE_NAME;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_THIRD_REFERENCED_REGULATION_ENTRY;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_THIRD_REFERENCED_REGULATION_JOURNAL_NO;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_THIRD_REFERENCED_REGULATION_TEXT;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_THIRD_REFERENCED_REGULATION_TITLE;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_THIRD_REFERENCED_REGULATION_YEAR;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;

import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.CommonCourt.CommonCourtType;
import pl.edu.icm.saos.persistence.model.CommonCourtDivision;
import pl.edu.icm.saos.persistence.model.CommonCourtDivisionType;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.CourtCase;
import pl.edu.icm.saos.persistence.model.CourtType;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.JudgmentKeyword;
import pl.edu.icm.saos.persistence.model.JudgmentReferencedRegulation;
import pl.edu.icm.saos.persistence.model.JudgmentResult;
import pl.edu.icm.saos.persistence.model.JudgmentSourceInfo;
import pl.edu.icm.saos.persistence.model.JudgmentTextContent;
import pl.edu.icm.saos.persistence.model.LawJournalEntry;
import pl.edu.icm.saos.persistence.model.MeansOfAppeal;
import pl.edu.icm.saos.persistence.model.SourceCode;

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

        CommonCourt commonCourt = createCcCourt(CC_COURT_TYPE);
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
        ccJudgment.setJudgmentDate(new LocalDate(CC_DATE_YEAR, CC_DATE_MONTH, CC_DATE_DAY));
        
        JudgmentTextContent textContent = new JudgmentTextContent();
        textContent.setRawTextContent(CC_TEXT_CONTENT);
        textContent.setType(CC_TEXT_CONTENT_TYPE);
        textContent.setFilePath(CC_TEXT_CONTENT_PATH);
        ccJudgment.setTextContent(textContent);


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
        sourceInfo.setPublicationDate(new DateTime(CC_SOURCE_PUBLICATION_DATE_IN_MILLISECONDS, DateTimeZone.UTC));

        ccJudgment.setSourceInfo(sourceInfo);


        JudgmentKeyword firstKeyword = new JudgmentKeyword(CourtType.COMMON, CC_FIRST_KEYWORD);
        JudgmentKeyword secondKeyword = new JudgmentKeyword(CourtType.COMMON, CC_SECOND_KEYWORD);
        ccJudgment.addKeyword(firstKeyword);
        ccJudgment.addKeyword(secondKeyword);
        
        ccJudgment.setReceiptDate(new LocalDate(CC_RECEIPT_DATE_YEAR, CC_RECEIPT_DATE_MONTH, CC_RECEIPT_DATE_DAY));
        
        ccJudgment.setMeansOfAppeal(new MeansOfAppeal(CourtType.COMMON, CC_MEANS_OF_APPEAL));
        ccJudgment.setJudgmentResult(new JudgmentResult(CourtType.COMMON, CC_JUDGMENT_RESULT));
        
        ccJudgment.addLowerCourtJudgment(CC_FIRST_LOWER_COURT_JUDGMENT);
        ccJudgment.addLowerCourtJudgment(CC_SECOND_LOWER_COURT_JUDGMENT);


        return ccJudgment;
    }



    /**
     * Creates {@link CommonCourt} hierarchy with default field data.
     * @return CommonCourt
     */

    static CommonCourt createCcCourt(CommonCourtType courtType){
        
        CommonCourtDivisionType ccDivisionType1 = commonCourtDivisionType(CC_FIRST_DIVISION_TYPE_NAME, CC_FIRST_DIVISION_TYPE_CODE);
        CommonCourtDivisionType ccDivisionType2 = commonCourtDivisionType(CC_SECOND_DIVISION_TYPE_NAME, CC_SECOND_DIVISION_TYPE_CODE);
        
        
        CommonCourt commonCourt = null;

        
        if(courtType == CommonCourtType.DISTRICT) {
            commonCourt = new CommonCourt();
            commonCourt.setType(courtType);
            commonCourt.setName(TextObjectDefaultData.CC_DISTRICT_COURT_NAME);
            commonCourt.setCode(TextObjectDefaultData.CC_DISTRICT_COURT_CODE);
            
            CommonCourtDivision firstCcDivision = createCcDivision(CC_FIRST_DISTRICT_DIVISION_CODE, CC_FIRST_DISTRICT_DIVISION_NAME, ccDivisionType1);
            commonCourt.addDivision(firstCcDivision);

            CommonCourtDivision secondCcDivision = createCcDivision(TextObjectDefaultData.CC_SECOND_DISTRICT_DIVISION_CODE, CC_SECOND_DISTRICT_DIVISION_NAME, ccDivisionType2);
            commonCourt.addDivision(secondCcDivision);
            
            CommonCourt regional = createRegionalCourt(ccDivisionType1, ccDivisionType2);
            commonCourt.setParentCourt(regional);
            
            CommonCourt appeal = createAppealCourt(ccDivisionType1, ccDivisionType2);
            regional.setParentCourt(appeal);
        }
        
        if(courtType == CommonCourtType.REGIONAL) {
            commonCourt = createRegionalCourt(ccDivisionType1, ccDivisionType2);
            
            CommonCourt appeal = createAppealCourt(ccDivisionType1, ccDivisionType2);
            commonCourt.setParentCourt(appeal);
        }

        if(courtType == CommonCourtType.APPEAL) {
            commonCourt = createAppealCourt(ccDivisionType1, ccDivisionType2);
        }

        
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
            String prefix = i + "_CC_";
            CommonCourtJudgment judgment = createCcJudgmentWithRandomData(prefix, i * (CourtType.COMMON.ordinal() + 1));
            judgments.add(judgment);
        }

        return judgments;
    }



    //------------------------ PRIVATE --------------------------
    private static CommonCourtDivisionType commonCourtDivisionType(String name, String code){
        CommonCourtDivisionType commonCourtDivisionType = new CommonCourtDivisionType();
        commonCourtDivisionType.setName(name);
        commonCourtDivisionType.setCode(code);

        return commonCourtDivisionType;
    }


    private static CommonCourtJudgment createCcJudgmentWithRandomData(String prefix, int numericPrefix){
        CommonCourtJudgment ccJudgment = new CommonCourtJudgment();
        TestInMemoryObjectFactoryHelper.fillJudgmentWithRandomData(ccJudgment, prefix, numericPrefix);
        
        ccJudgment.getSourceInfo().setSourceCode(SourceCode.COMMON_COURT);


        JudgmentKeyword firstKeyword = new JudgmentKeyword(CourtType.COMMON, RandomStringUtils.randomAlphanumeric(18));
        JudgmentKeyword secondKeyword = new JudgmentKeyword(CourtType.COMMON, RandomStringUtils.randomAlphanumeric(19));
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

    private static CommonCourt createAppealCourt(CommonCourtDivisionType ccDivisionType1, CommonCourtDivisionType ccDivisionType2) {

        CommonCourt appeal = new CommonCourt();
        appeal.setName(TextObjectDefaultData.CC_APPEAL_COURT_NAME);
        appeal.setCode(TextObjectDefaultData.CC_APPEAL_COURT_CODE);
        appeal.setType(CommonCourtType.APPEAL);
        
        
        CommonCourtDivision firstCcDivision = createCcDivision(CC_FIRST_APPEAL_DIVISION_CODE, CC_FIRST_APPEAL_DIVISION_NAME, ccDivisionType1);
        appeal.addDivision(firstCcDivision);

        CommonCourtDivision secondCcDivision = createCcDivision(CC_SECOND_APPEAL_DIVISION_CODE, CC_SECOND_APPEAL_DIVISION_NAME, ccDivisionType2);
        appeal.addDivision(secondCcDivision);
        
        return appeal;
    }
    
    private static CommonCourt createRegionalCourt(CommonCourtDivisionType ccDivisionType1, CommonCourtDivisionType ccDivisionType2) {
        
        CommonCourt regional = new CommonCourt();
        regional.setName(TextObjectDefaultData.CC_REGIONAL_COURT_NAME);
        regional.setCode(TextObjectDefaultData.CC_REGIONAL_COURT_CODE);
        regional.setType(CommonCourtType.REGIONAL);
        
        CommonCourtDivision firstCcDivision = createCcDivision(CC_FIRST_REGIONAL_DIVISION_CODE, CC_FIRST_REGIONAL_DIVISION_NAME, ccDivisionType1);
        regional.addDivision(firstCcDivision);

        CommonCourtDivision secondCcDivision = createCcDivision(CC_SECOND_REGIONAL_DIVISION_CODE, CC_SECOND_REGIONAL_DIVISION_NAME, ccDivisionType2);
        regional.addDivision(secondCcDivision);

        
        return regional;
    }

    private static CommonCourtDivision createCcDivision(String code, String name, CommonCourtDivisionType ccDivisionType) {
        CommonCourtDivision ccDivision = new CommonCourtDivision();
        ccDivision.setCode(code);
        ccDivision.setName(name);
        ccDivision.setType(ccDivisionType);
        return ccDivision;
    }


}
