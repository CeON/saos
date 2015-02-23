package pl.edu.icm.saos.persistence.common;

import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;

import pl.edu.icm.saos.persistence.model.CourtCase;
import pl.edu.icm.saos.persistence.model.CourtType;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.JudgmentKeyword;
import pl.edu.icm.saos.persistence.model.JudgmentReferencedRegulation;
import pl.edu.icm.saos.persistence.model.JudgmentResult;
import pl.edu.icm.saos.persistence.model.JudgmentSourceInfo;
import pl.edu.icm.saos.persistence.model.LawJournalEntry;
import pl.edu.icm.saos.persistence.model.MeansOfAppeal;
import pl.edu.icm.saos.persistence.model.NationalAppealChamberJudgment;
import pl.edu.icm.saos.persistence.model.SourceCode;

/**
 * @author madryk
 */
final class TestInMemoryNacObjectFactory {

    //------------------------ CONSTRUCTORS --------------------------
    
    private TestInMemoryNacObjectFactory() { }
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Creates {@link NationalAppealChamberJudgment} hierarchy
     * with default field data.
     * @return NationalAppealChamberJudgment.
     */
    public static NationalAppealChamberJudgment createNacJudgment(){
        NationalAppealChamberJudgment nacJudgment = new NationalAppealChamberJudgment();

        CourtCase courtCase = new CourtCase(NAC_CASE_NUMBER);
        nacJudgment.addCourtCase(courtCase);

        nacJudgment.addLegalBase(NAC_FIRST_LEGAL_BASE);
        nacJudgment.addLegalBase(NAC_SECOND_LEGAL_BASE);

        nacJudgment.addCourtReporter(NAC_FIRST_COURT_REPORTER);
        nacJudgment.addCourtReporter(NAC_SECOND_COURT_REPORTER);

        nacJudgment.setDecision(NAC_DECISION);
        nacJudgment.setSummary(NAC_SUMMARY);
        nacJudgment.setJudgmentType(NAC_JUDGMENT_TYPE);
        nacJudgment.setTextContent(NAC_TEXT_CONTENT);
        nacJudgment.setJudgmentDate(new LocalDate(NAC_DATE_YEAR, NAC_DATE_MONTH, NAC_DATE_DAY));


        LawJournalEntry firstLawJournalEntry = new LawJournalEntry();
        firstLawJournalEntry.setTitle(NAC_FIRST_REFERENCED_REGULATION_TITLE);
        firstLawJournalEntry.setYear(NAC_FIRST_REFERENCED_REGULATION_YEAR);
        firstLawJournalEntry.setJournalNo(NAC_FIRST_REFERENCED_REGULATION_JOURNAL_NO);
        firstLawJournalEntry.setEntry(NAC_FIRST_REFERENCED_REGULATION_ENTRY);

        JudgmentReferencedRegulation firstReferencedRegulation = new JudgmentReferencedRegulation();
        firstReferencedRegulation.setRawText(NAC_FIRST_REFERENCED_REGULATION_TEXT);
        firstReferencedRegulation.setLawJournalEntry(firstLawJournalEntry);

        LawJournalEntry secondLawJournalEntry = new LawJournalEntry();
        secondLawJournalEntry.setTitle(NAC_SECOND_REFERENCED_REGULATION_TITLE);
        secondLawJournalEntry.setYear(NAC_SECOND_REFERENCED_REGULATION_YEAR);
        secondLawJournalEntry.setJournalNo(NAC_SECOND_REFERENCED_REGULATION_JOURNAL_NO);
        secondLawJournalEntry.setEntry(NAC_SECOND_REFERENCED_REGULATION_ENTRY);

        JudgmentReferencedRegulation secondReferencedRegulation = new JudgmentReferencedRegulation();
        secondReferencedRegulation.setRawText(NAC_SECOND_REFERENCED_REGULATION_TEXT);
        secondReferencedRegulation.setLawJournalEntry(secondLawJournalEntry);


        nacJudgment.addReferencedRegulation(firstReferencedRegulation);
        nacJudgment.addReferencedRegulation(secondReferencedRegulation);

        JudgmentSourceInfo sourceInfo = new JudgmentSourceInfo();
        sourceInfo.setSourceCode(NAC_SOURCE_CODE);
        sourceInfo.setSourceJudgmentId(NAC_SOURCE_JUDGMENT_ID);
        sourceInfo.setSourceJudgmentUrl(NAC_SOURCE_JUDGMENT_URL);
        sourceInfo.setPublisher(NAC_SOURCE_PUBLISHER);
        sourceInfo.setReviser(NAC_SOURCE_REVISER);
        sourceInfo.setPublicationDate(new DateTime(NAC_SOURCE_PUBLICATION_DATE_IN_MILLISECONDS, DateTimeZone.UTC));

        nacJudgment.setSourceInfo(sourceInfo);


        Judge firstJudge = new Judge(NAC_FIRST_JUDGE_NAME, NAC_FIRST_JUDGE_ROLE);
        Judge secondJudge = new Judge(NAC_SECOND_JUDGE_NAME);
        Judge thirdJudge = new Judge(NAC_THIRD_JUDGE_NAME);
        nacJudgment.addJudge(firstJudge);
        nacJudgment.addJudge(secondJudge);
        nacJudgment.addJudge(thirdJudge);



        JudgmentKeyword firstKeyword = new JudgmentKeyword(CourtType.NATIONAL_APPEAL_CHAMBER, NAC_FIRST_KEYWORD);
        JudgmentKeyword secondKeyword = new JudgmentKeyword(CourtType.NATIONAL_APPEAL_CHAMBER, NAC_SECOND_KEYWORD);
        nacJudgment.addKeyword(firstKeyword);
        nacJudgment.addKeyword(secondKeyword);
        
        nacJudgment.setReceiptDate(new LocalDate(NAC_RECEIPT_DATE_YEAR, NAC_RECEIPT_DATE_MONTH, NAC_RECEIPT_DATE_DAY));
        
        nacJudgment.setMeansOfAppeal(new MeansOfAppeal(CourtType.NATIONAL_APPEAL_CHAMBER, NAC_MEANS_OF_APPEAL));
        nacJudgment.setJudgmentResult(new JudgmentResult(CourtType.NATIONAL_APPEAL_CHAMBER, NAC_JUDGMENT_RESULT));
        
        nacJudgment.addLowerCourtJudgment(NAC_FIRST_LOWER_COURT_JUDGMENT);
        nacJudgment.addLowerCourtJudgment(NAC_SECOND_LOWER_COURT_JUDGMENT);

        return nacJudgment;
    }


    /**
     * Creates {@link NationalAppealChamberJudgment} with minimal set of fields (necessaries for storing in db)
     * filled with random data.
     * @return NationalAppealChamberJudgment
     */
    public static NationalAppealChamberJudgment createSimpleNacJudgment(){
        NationalAppealChamberJudgment judgment = new NationalAppealChamberJudgment();
        judgment.addCourtCase(new CourtCase(RandomStringUtils.randomAlphanumeric(10)));
        judgment.getSourceInfo().setSourceCode(SourceCode.NATIONAL_APPEAL_CHAMBER);
        judgment.getSourceInfo().setSourceJudgmentId(RandomStringUtils.randomAlphabetic(20));


        return judgment;
    }

    /**
     * Creates list of {@link NationalAppealChamberJudgment} with fields filled with random values.
     * @param size of the list.
     * @return list of NationalAppealChamberJudgment
     */
    public static List<NationalAppealChamberJudgment> createNacJudgmentListWithRandomData(int size){
        List<NationalAppealChamberJudgment> judgments = new ArrayList<>(size);
        for(int i=0; i<size; ++i){
            String prefix = i + "__";
            NationalAppealChamberJudgment judgment = createNacJudgmentWithRandomData(prefix, i);
            judgments.add(judgment);
        }

        return judgments;
    }


    //------------------------ PRIVATE --------------------------

    private static NationalAppealChamberJudgment createNacJudgmentWithRandomData(String prefix, int numericPrefix) {
        NationalAppealChamberJudgment nacJudgment = new NationalAppealChamberJudgment();
        TestInMemoryObjectFactoryHelper.fillJudgmentWithRandomData(nacJudgment, prefix, numericPrefix);

        nacJudgment.getSourceInfo().setSourceCode(SourceCode.NATIONAL_APPEAL_CHAMBER);


        JudgmentKeyword firstKeyword = new JudgmentKeyword(CourtType.NATIONAL_APPEAL_CHAMBER, RandomStringUtils.randomAlphanumeric(18));
        JudgmentKeyword secondKeyword = new JudgmentKeyword(CourtType.NATIONAL_APPEAL_CHAMBER, RandomStringUtils.randomAlphanumeric(19));
        nacJudgment.addKeyword(firstKeyword);
        nacJudgment.addKeyword(secondKeyword);

        return nacJudgment;
    }
    
}
