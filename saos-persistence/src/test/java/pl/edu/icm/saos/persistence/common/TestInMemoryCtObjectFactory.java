package pl.edu.icm.saos.persistence.common;

import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import pl.edu.icm.saos.persistence.model.*;

import java.util.ArrayList;
import java.util.List;

import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.*;

/**
 * @author pavtel
 */
final class TestInMemoryCtObjectFactory {
    private TestInMemoryCtObjectFactory() {
    }

    //------------------------ LOGIC --------------------------
    /**
     * Creates {@link pl.edu.icm.saos.persistence.model.ConstitutionalTribunalJudgment} hierarchy
     * with default field data.
     * @return ConstitutionalTribunalJudgment.
     */
    static ConstitutionalTribunalJudgment createCtJudgment(){
        ConstitutionalTribunalJudgment ctJudgment = new ConstitutionalTribunalJudgment();

        CourtCase courtCase = new CourtCase(CT_CASE_NUMBER);
        ctJudgment.addCourtCase(courtCase);

        ctJudgment.addLegalBase(CT_FIRST_LEGAL_BASE);
        ctJudgment.addLegalBase(CT_SECOND_LEGAL_BASE);

        ctJudgment.addCourtReporter(CT_FIRST_COURT_REPORTER);
        ctJudgment.addCourtReporter(CT_SECOND_COURT_REPORTER);

        ctJudgment.setDecision(CT_DECISION);
        ctJudgment.setSummary(CT_SUMMARY);
        ctJudgment.setJudgmentType(CT_JUDGMENT_TYPE);
        ctJudgment.setTextContent(CT_TEXT_CONTENT);
        ctJudgment.setJudgmentDate(new LocalDate(CT_DATE_YEAR, CT_DATE_MONTH, CT_DATE_DAY));


        LawJournalEntry firstLawJournalEntry = new LawJournalEntry();
        firstLawJournalEntry.setTitle(CT_FIRST_REFERENCED_REGULATION_TITLE);
        firstLawJournalEntry.setYear(CT_FIRST_REFERENCED_REGULATION_YEAR);
        firstLawJournalEntry.setJournalNo(CT_FIRST_REFERENCED_REGULATION_JOURNAL_NO);
        firstLawJournalEntry.setEntry(CT_FIRST_REFERENCED_REGULATION_ENTRY);

        JudgmentReferencedRegulation firstReferencedRegulation = new JudgmentReferencedRegulation();
        firstReferencedRegulation.setRawText(CT_FIRST_REFERENCED_REGULATION_TEXT);
        firstReferencedRegulation.setLawJournalEntry(firstLawJournalEntry);

        LawJournalEntry secondLawJournalEntry = new LawJournalEntry();
        secondLawJournalEntry.setTitle(CT_SECOND_REFERENCED_REGULATION_TITLE);
        secondLawJournalEntry.setYear(CT_SECOND_REFERENCED_REGULATION_YEAR);
        secondLawJournalEntry.setJournalNo(CT_SECOND_REFERENCED_REGULATION_JOURNAL_NO);
        secondLawJournalEntry.setEntry(CT_SECOND_REFERENCED_REGULATION_ENTRY);

        JudgmentReferencedRegulation secondReferencedRegulation = new JudgmentReferencedRegulation();
        secondReferencedRegulation.setRawText(CT_SECOND_REFERENCED_REGULATION_TEXT);
        secondReferencedRegulation.setLawJournalEntry(secondLawJournalEntry);


        ctJudgment.addReferencedRegulation(firstReferencedRegulation);
        ctJudgment.addReferencedRegulation(secondReferencedRegulation);

        JudgmentSourceInfo sourceInfo = new JudgmentSourceInfo();
        sourceInfo.setSourceCode(CT_SOURCE_CODE);
        sourceInfo.setSourceJudgmentId(CT_SOURCE_JUDGMENT_ID);
        sourceInfo.setSourceJudgmentUrl(CT_SOURCE_JUDGMENT_URL);
        sourceInfo.setPublisher(CT_SOURCE_PUBLISHER);
        sourceInfo.setReviser(CT_SOURCE_REVISER);
        sourceInfo.setPublicationDate(new DateTime(CT_SOURCE_PUBLICATION_DATE_IN_MILLISECONDS, DateTimeZone.UTC));

        ctJudgment.setSourceInfo(sourceInfo);


        Judge firstJudge = new Judge(CT_FIRST_JUDGE_NAME, CT_FIRST_JUDGE_ROLE);
        firstJudge.setFunction(CT_FIRST_JUDGE_FUNCTION);
        Judge secondJudge = new Judge(CT_SECOND_JUDGE_NAME);
        ctJudgment.addJudge(firstJudge);
        ctJudgment.addJudge(secondJudge);



        JudgmentKeyword firstKeyword = new JudgmentKeyword(CourtType.CONSTITUTIONAL_TRIBUNAL, CT_FIRST_KEYWORD);
        JudgmentKeyword secondKeyword = new JudgmentKeyword(CourtType.CONSTITUTIONAL_TRIBUNAL, CT_SECOND_KEYWORD);
        ctJudgment.addKeyword(firstKeyword);
        ctJudgment.addKeyword(secondKeyword);

        ConstitutionalTribunalJudgmentDissentingOpinion firstOpinion = new ConstitutionalTribunalJudgmentDissentingOpinion();
        firstOpinion.setTextContent(CT_FIRST_DISSENTING_OPINION_TEXT);
        firstOpinion.addAuthor(CT_FIRST_DISSENTING_OPINION_FIRST_AUTHOR);
        firstOpinion.addAuthor(CT_FIRST_DISSENTING_OPINION_SECOND_AUTHOR);

        ConstitutionalTribunalJudgmentDissentingOpinion secondOpinion = new ConstitutionalTribunalJudgmentDissentingOpinion();
        secondOpinion.setTextContent(CT_SECOND_DISSENTING_OPINION_TEXT);
        secondOpinion.addAuthor(CT_SECOND_DISSENTING_OPINION_FIRST_AUTHOR);

        ctJudgment.addDissentingOpinion(firstOpinion);
        ctJudgment.addDissentingOpinion(secondOpinion);

        return ctJudgment;
    }


    /**
     * Creates {@link ConstitutionalTribunalJudgment} with minimal set of fields (necessaries for storing in db)
     * filled with random data.
     * @return ConstitutionalTribunalJudgment
     */
    static ConstitutionalTribunalJudgment createSimpleCtJudgment(){
        ConstitutionalTribunalJudgment judgment = new ConstitutionalTribunalJudgment();
        judgment.addCourtCase(new CourtCase(RandomStringUtils.randomAlphanumeric(10)));
        judgment.getSourceInfo().setSourceCode(SourceCode.SUPREME_COURT);
        judgment.getSourceInfo().setSourceJudgmentId(RandomStringUtils.randomAlphabetic(20));


        return judgment;
    }

    /**
     * Creates list of {@link ConstitutionalTribunalJudgment} with fields filled with random values.
     * @param size of the list.
     * @return list of ConstitutionalTribunalJudgment
     */
    static List<ConstitutionalTribunalJudgment> createCtJudgmentListWithRandomData(int size){
        List<ConstitutionalTribunalJudgment> judgments = new ArrayList<>(size);
        for(int i=0; i<size; ++i){
            String prefix = i + "__";
            ConstitutionalTribunalJudgment judgment = createCtJudgmentWithRandomData(prefix, i);
            judgments.add(judgment);
        }

        return judgments;
    }

    //------------------------ PRIVATE --------------------------
    private static ConstitutionalTribunalJudgment createCtJudgmentWithRandomData(String prefix, int numericPrefix) {
        ConstitutionalTribunalJudgment ccJudgment = new ConstitutionalTribunalJudgment();
        TestInMemoryObjectFactoryHelper.fillJudgmentWithRandomData(ccJudgment, prefix, numericPrefix);

        ccJudgment.getSourceInfo().setSourceCode(SourceCode.COMMON_COURT);


        JudgmentKeyword firstKeyword = new JudgmentKeyword(CourtType.CONSTITUTIONAL_TRIBUNAL, RandomStringUtils.randomAlphanumeric(18));
        JudgmentKeyword secondKeyword = new JudgmentKeyword(CourtType.CONSTITUTIONAL_TRIBUNAL, RandomStringUtils.randomAlphanumeric(19));
        ccJudgment.addKeyword(firstKeyword);
        ccJudgment.addKeyword(secondKeyword);

        return ccJudgment;
    }
}
