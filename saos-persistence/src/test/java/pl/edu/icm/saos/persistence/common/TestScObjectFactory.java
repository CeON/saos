package pl.edu.icm.saos.persistence.common;

import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.icm.saos.persistence.model.*;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.*;

/**
 * @author pavtel
 */
@Service
public class TestScObjectFactory {

    @Autowired
    private EntityManager entityManager;

    //------------------------ LOGIC --------------------------
    /**
     * Creates {@link pl.edu.icm.saos.persistence.model.SupremeCourtJudgment} hierarchy with default field data.
     * @return SupremeCourtJudgment.
     */
    @Transactional
    public SupremeCourtJudgment createScJudgment(boolean save){
        SupremeCourtJudgment scJudgment = new SupremeCourtJudgment();

        SupremeCourtChamber scChamber = createScChamber(save);
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
        Judge secondJudge = new Judge(SC_SECOND_JUDGE_NAME, SC_SECOND_JUDGE_ROLE);
        scJudgment.addJudge(firstJudge);
        scJudgment.addJudge(secondJudge);

        JudgmentSourceInfo sourceInfo = createJudgmentSourceInfo();
        scJudgment.setSourceInfo(sourceInfo);

        SupremeCourtJudgmentForm scForm = new SupremeCourtJudgmentForm();
        scForm.setName(SC_JUDGMENT_FORM_NAME);
        scJudgment.setScJudgmentForm(scForm);

        if(save){
            entityManager.persist(firstJudge);
            entityManager.persist(secondJudge);

            entityManager.persist(firstLawJournalEntry);
            entityManager.persist(secondLawJournalEntry);
            entityManager.persist(thirdLawJournalEntry);

            entityManager.persist(firstReferencedRegulation);
            entityManager.persist(secondReferencedRegulation);
            entityManager.persist(thirdReferencedRegulation);

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
     * Creates {@link JudgmentSourceInfo} with default field data.
     * @return JudgmentSourceInfo.
     */
    public JudgmentSourceInfo createJudgmentSourceInfo(){
        JudgmentSourceInfo judgmentSourceInfo = new JudgmentSourceInfo();

        judgmentSourceInfo.setSourceCode(SC_SOURCE_CODE);
        judgmentSourceInfo.setSourceJudgmentId(SC_SOURCE_JUDGMENT_ID);
        judgmentSourceInfo.setSourceJudgmentUrl(SC_SOURCE_JUDGMENT_URL);
        judgmentSourceInfo.setPublisher(SC_SOURCE_PUBLISHER);
        judgmentSourceInfo.setReviser(SC_SOURCE_REVISER);
        judgmentSourceInfo.setPublicationDate(new DateTime(SC_SOURCE_PUBLICATION_DATE_IN_MILLISECONDS, DateTimeZone.UTC));

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
}
