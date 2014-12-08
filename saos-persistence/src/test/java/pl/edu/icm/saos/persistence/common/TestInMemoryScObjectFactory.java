package pl.edu.icm.saos.persistence.common;

import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import pl.edu.icm.saos.persistence.model.*;

import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.*;

/**
 * @author pavtel
 */
final class TestInMemoryScObjectFactory {
    private TestInMemoryScObjectFactory() {
    }

    //------------------------ LOGIC --------------------------
    /**
     * Creates {@link pl.edu.icm.saos.persistence.model.SupremeCourtJudgment} hierarchy with default field data.
     * @return SupremeCourtJudgment.
     */
    static SupremeCourtJudgment createScJudgment(){
        SupremeCourtJudgment scJudgment = new SupremeCourtJudgment();

        SupremeCourtChamber scChamber = createScChamber();
        scJudgment.setScChamberDivision(scChamber.getDivisions().get(0));
        scJudgment.addScChamber(scChamber);


        CourtCase courtCase = new CourtCase(SC_CASE_NUMBER);
        scJudgment.addCourtCase(courtCase);

        scJudgment.addLegalBase(SC_FIRST_LEGAL_BASE);
        scJudgment.addLegalBase(SC_SECOND_LEGAL_BASE);

        scJudgment.addCourtReporter(SC_FIRST_COURT_REPORTER);
        scJudgment.addCourtReporter(SC_SECOND_COURT_REPORTER);

        scJudgment.setDecision(SC_DECISION);
        scJudgment.setSummary(SC_SUMMARY);
        scJudgment.setJudgmentType(SC_JUDGMENT_TYPE);
        scJudgment.setTextContent(SC_TEXT_CONTENT);
        scJudgment.setJudgmentDate(new LocalDate(SC_DATE_YEAR, SC_DATE_MONTH, SC_DATE_DAY));
        scJudgment.setPersonnelType(SC_PERSONNEL_TYPE);

        LawJournalEntry firstLawJournalEntry = new LawJournalEntry();
        firstLawJournalEntry.setTitle(SC_FIRST_REFERENCED_REGULATION_TITLE);
        firstLawJournalEntry.setYear(SC_FIRST_REFERENCED_REGULATION_YEAR);
        firstLawJournalEntry.setJournalNo(SC_FIRST_REFERENCED_REGULATION_JOURNAL_NO);
        firstLawJournalEntry.setEntry(SC_FIRST_REFERENCED_REGULATION_ENTRY);

        JudgmentReferencedRegulation firstReferencedRegulation = new JudgmentReferencedRegulation();
        firstReferencedRegulation.setRawText(SC_FIRST_REFERENCED_REGULATION_TEXT);
        firstReferencedRegulation.setLawJournalEntry(firstLawJournalEntry);

        LawJournalEntry secondLawJournalEntry = new LawJournalEntry();
        secondLawJournalEntry.setTitle(SC_SECOND_REFERENCED_REGULATION_TITLE);
        secondLawJournalEntry.setYear(SC_SECOND_REFERENCED_REGULATION_YEAR);
        secondLawJournalEntry.setJournalNo(SC_SECOND_REFERENCED_REGULATION_JOURNAL_NO);
        secondLawJournalEntry.setEntry(SC_SECOND_REFERENCED_REGULATION_ENTRY);

        JudgmentReferencedRegulation secondReferencedRegulation = new JudgmentReferencedRegulation();
        secondReferencedRegulation.setRawText(SC_SECOND_REFERENCED_REGULATION_TEXT);
        secondReferencedRegulation.setLawJournalEntry(secondLawJournalEntry);

        LawJournalEntry thirdLawJournalEntry = new LawJournalEntry();
        thirdLawJournalEntry.setTitle(SC_THIRD_REFERENCED_REGULATION_TITLE);
        thirdLawJournalEntry.setYear(SC_THIRD_REFERENCED_REGULATION_YEAR);
        thirdLawJournalEntry.setJournalNo(SC_THIRD_REFERENCED_REGULATION_JOURNAL_NO);
        thirdLawJournalEntry.setEntry(SC_THIRD_REFERENCED_REGULATION_ENTRY);

        JudgmentReferencedRegulation thirdReferencedRegulation = new JudgmentReferencedRegulation();
        thirdReferencedRegulation.setRawText(SC_THIRD_REFERENCED_REGULATION_TEXT);
        thirdReferencedRegulation.setLawJournalEntry(thirdLawJournalEntry);

        scJudgment.addReferencedRegulation(firstReferencedRegulation);
        scJudgment.addReferencedRegulation(secondReferencedRegulation);
        scJudgment.addReferencedRegulation(thirdReferencedRegulation);


        Judge firstJudge = new Judge(SC_FIRST_JUDGE_NAME, SC_FIRST_JUDGE_ROLE);
        firstJudge.setFunction(SC_FIRST_JUDGE_FUNCTION);
        Judge secondJudge = new Judge(SC_SECOND_JUDGE_NAME, SC_SECOND_JUDGE_ROLE);
        scJudgment.addJudge(firstJudge);
        scJudgment.addJudge(secondJudge);

        JudgmentSourceInfo sourceInfo = new JudgmentSourceInfo();
        sourceInfo.setSourceCode(SC_SOURCE_CODE);
        sourceInfo.setSourceJudgmentId(SC_SOURCE_JUDGMENT_ID);
        sourceInfo.setSourceJudgmentUrl(SC_SOURCE_JUDGMENT_URL);
        sourceInfo.setPublisher(SC_SOURCE_PUBLISHER);
        sourceInfo.setReviser(SC_SOURCE_REVISER);
        sourceInfo.setPublicationDate(new DateTime(SC_SOURCE_PUBLICATION_DATE_IN_MILLISECONDS, DateTimeZone.UTC));

        scJudgment.setSourceInfo(sourceInfo);

        SupremeCourtJudgmentForm scForm = new SupremeCourtJudgmentForm();
        scForm.setName(SC_JUDGMENT_FORM_NAME);
        scJudgment.setScJudgmentForm(scForm);


        return scJudgment;
    }


    /**
     * Creates {@link SupremeCourtChamber} hierarchy with default field data.
     * @return SupremeCourtJudgment.
     */
    static SupremeCourtChamber createScChamber(){
        SupremeCourtChamber scChamber = new SupremeCourtChamber();
        scChamber.setName(SC_CHAMBER_NAME);

        SupremeCourtChamberDivision scDivision = new SupremeCourtChamberDivision();
        scDivision.setName(SC_FIRST_DIVISION_NAME);
        scDivision.setFullName(SC_FIRST_DIVISION_FULL_NAME);
        scChamber.addDivision(scDivision);

        return scChamber;
    }


    /**
     * Creates {@link SupremeCourtJudgment} with minimal set of fields (necessaries for storing in db)
     * filled with random data.
     * @return SupremeCourtJudgment
     */
    static SupremeCourtJudgment createSimpleScJudgment(){

        SupremeCourtJudgment judgment = new SupremeCourtJudgment();
        judgment.addCourtCase(new CourtCase(RandomStringUtils.randomAlphanumeric(10)));
        judgment.getSourceInfo().setSourceCode(SourceCode.SUPREME_COURT);
        judgment.getSourceInfo().setSourceJudgmentId(RandomStringUtils.randomAlphabetic(20));

        return judgment;
    }


}
