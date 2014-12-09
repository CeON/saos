package pl.edu.icm.saos.persistence.common;

import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.SC_JUDGMENT_TYPE;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.joda.time.LocalDate;

import pl.edu.icm.saos.persistence.model.CourtCase;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.JudgmentReferencedRegulation;
import pl.edu.icm.saos.persistence.model.LawJournalEntry;

/**
 * @author madryk
 */
final class TestInMemoryObjectFactoryHelper {

    private TestInMemoryObjectFactoryHelper() { }
    
    /**
     * Fills {@link Judgment} with random values
     * @param judgment to fill
     * @param prefix - unique String prefix among randomly created judgments
     * @param numericPrefix - unique int prefix among randomly created judgments
     */
    static void fillJudgmentWithRandomData(Judgment judgment, String prefix, int numericPrefix){
        judgment.addCourtCase(new CourtCase(prefix+RandomStringUtils.randomAlphanumeric(10)));

        judgment.addLegalBase(RandomStringUtils.randomAlphabetic(8));
        judgment.addLegalBase(RandomStringUtils.randomAlphabetic(8));

        JudgmentReferencedRegulation firstReferencedRegulation = new JudgmentReferencedRegulation();

        LawJournalEntry firstLawJournalEntry = new  LawJournalEntry(
                RandomUtils.nextInt(1950, 2000),
                RandomUtils.nextInt(1, 30),
                RandomUtils.nextInt(1, 200) + (numericPrefix*1000),
                RandomStringUtils.randomAlphabetic(10));
        firstReferencedRegulation.setRawText(RandomStringUtils.randomAlphabetic(30));
        firstReferencedRegulation.setLawJournalEntry(firstLawJournalEntry);

        JudgmentReferencedRegulation secondReferencedRegulation = new JudgmentReferencedRegulation();
        LawJournalEntry secondLawJournalEntry = new  LawJournalEntry(
                RandomUtils.nextInt(1950, 2000),
                RandomUtils.nextInt(30, 60),
                RandomUtils.nextInt(1, 200) + (numericPrefix*1000),
                RandomStringUtils.randomAlphabetic(10));
        secondReferencedRegulation.setRawText(RandomStringUtils.randomAlphabetic(30));
        secondReferencedRegulation.setLawJournalEntry(secondLawJournalEntry);

        judgment.addReferencedRegulation(firstReferencedRegulation);
        judgment.addReferencedRegulation(secondReferencedRegulation);


        judgment.addCourtReporter(RandomStringUtils.randomAlphabetic(4)+" "+RandomStringUtils.randomAlphabetic(5));
        judgment.addCourtReporter(RandomStringUtils.randomAlphabetic(5)+" "+RandomStringUtils.randomAlphabetic(6));

        judgment.setDecision(prefix+RandomStringUtils.randomAlphabetic(5));
        judgment.setSummary(prefix+RandomStringUtils.randomAlphabetic(5));
        judgment.setJudgmentType(SC_JUDGMENT_TYPE);
        judgment.setTextContent(prefix+RandomStringUtils.randomAlphabetic(5));

        int month = 1 + (int)(Math.random()*12);
        int day = 1 +(int)(Math.random()*28);
        judgment.setJudgmentDate(new LocalDate(2000, month, day));




        Judge firstJudge = new Judge(RandomStringUtils.randomAlphabetic(4)+" "+RandomStringUtils.randomAlphabetic(8),
                Judge.JudgeRole.PRESIDING_JUDGE);
        Judge secondJudge = new Judge(RandomStringUtils.randomAlphabetic(5)+" "+RandomStringUtils.randomAlphabetic(7),
                Judge.JudgeRole.REPORTING_JUDGE);
        Judge thirdJudge = new Judge(RandomStringUtils.randomAlphabetic(6)+" "+RandomStringUtils.randomAlphabetic(6));
        judgment.addJudge(firstJudge);
        judgment.addJudge(secondJudge);
        judgment.addJudge(thirdJudge);

    }
}
