package pl.edu.icm.saos.persistence.common;

import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.SC_JUDGMENT_TYPE;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.joda.time.LocalDate;

import pl.edu.icm.saos.persistence.model.CourtCase;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.JudgmentReferencedRegulation;
import pl.edu.icm.saos.persistence.model.JudgmentTextContent;
import pl.edu.icm.saos.persistence.model.LawJournalEntry;
import pl.edu.icm.saos.persistence.model.JudgmentTextContent.ContentType;

/**
 * @author madryk
 */
final class TestInMemoryObjectFactoryHelper {

    private TestInMemoryObjectFactoryHelper() { }
    
    /**
     * Fills {@link Judgment} with random values.
     * This method requires to provide prefixes. When these prefixes are
     * unique then method guarantees uniqueness of created values that have
     * to be different from each other when adding to database.
     * 
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

        int month = 1 + (int)(Math.random()*12);
        int day = 1 +(int)(Math.random()*28);
        judgment.setJudgmentDate(new LocalDate(2000, month, day));
        
        JudgmentTextContent textContent = new JudgmentTextContent();
        textContent.setRawTextContent(prefix+RandomStringUtils.randomAlphabetic(5));
        textContent.setType(ContentType.PDF);
        textContent.setFilePath("/" + prefix+RandomStringUtils.randomAlphabetic(5) + ".pdf");
        judgment.setTextContent(textContent);




        Judge firstJudge = new Judge(RandomStringUtils.randomAlphabetic(4)+" "+RandomStringUtils.randomAlphabetic(8),
                Judge.JudgeRole.PRESIDING_JUDGE);
        Judge secondJudge = new Judge(RandomStringUtils.randomAlphabetic(5)+" "+RandomStringUtils.randomAlphabetic(7),
                Judge.JudgeRole.REPORTING_JUDGE);
        Judge thirdJudge = new Judge(RandomStringUtils.randomAlphabetic(6)+" "+RandomStringUtils.randomAlphabetic(6));
        judgment.addJudge(firstJudge);
        judgment.addJudge(secondJudge);
        judgment.addJudge(thirdJudge);


        judgment.getSourceInfo().setSourceJudgmentId(prefix+RandomStringUtils.randomAlphabetic(20));
        judgment.getSourceInfo().setSourceJudgmentUrl("http://example.com/" + RandomStringUtils.randomAlphabetic(20));
        
    }
}
