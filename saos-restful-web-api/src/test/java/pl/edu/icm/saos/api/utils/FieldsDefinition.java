package pl.edu.icm.saos.api.utils;


import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import pl.edu.icm.saos.persistence.model.*;

import java.util.Arrays;

/**
 * @author pavtel
 */
public abstract class FieldsDefinition {

    public static CommonCourtJudgment createCommonJudgment(){
        CommonCourtJudgment judgment = commonCourtJudgmentWrapper(JC.JUDGMENT_ID);
        judgment.setCaseNumber(JC.CASE_NUMBER);
        judgment.setDecision(JC.DECISION);

        judgment.addLegalBase(JC.FIRST_LEGAL_BASE);
        judgment.addLegalBase(JC.SECOND_LEGAL_BASE);

        judgment.addCourtReporter(JC.FIRST_COURT_REPORTER);
        judgment.addCourtReporter(JC.SECOND_COURT_REPORTER);

        judgment.setSummary(JC.SUMMARY);

        JudgmentReasoning judgmentReasoning = judgmentReasoning(JC.REASONING_TEXT)
                .sourceInfo(
                        judgmentSourceInfo(SourceCode.COMMON_COURT)
                        .sourceJudgmentId(JC.REASONING_JUDGMENT_ID)
                        .sourceJudgmentUrl(JC.REASONING_JUDGMENT_URL)
                        .publicationDate(new DateTime(JC.REASONING_PUBLICATION_DATE_IN_MILLISECONDS))
                        .publisher(JC.REASONING_PUBLISHER)
                        .reviser(JC.REASONING_REVISER)
                );
        judgment.setReasoning(judgmentReasoning);

        judgment.setJudgmentType(Judgment.JudgmentType.SENTENCE);

        //*** references ***
        JudgmentReferencedRegulation judgmentReferencedRegulation = judgmentReferencedRegulation()
                .rawText(JC.FIRST_REFERENCED_REGULATION_TEXT)
                .lawJournalEntry(
                         lawJournalEntry()
                        .title(JC.FIRST_REFERENCED_REGULATION_TITLE)
                        .year(JC.FIRST_REFERENCED_REGULATION_YEAR)
                        .journalNo(JC.FIRST_REFERENCED_REGULATION_JOURNAL_NO)
                        .entry(JC.FIRST_REFERENCED_REGULATION_ENTRY)
                );

        JudgmentReferencedRegulation secondJudgmentReferencedRegulation = judgmentReferencedRegulation()
                .rawText(JC.SECOND_REFERENCED_REGULATION_TEXT)
                .lawJournalEntry(
                        lawJournalEntry()
                        .title(JC.SECOND_REFERENCED_REGULATION_TITLE)
                        .year(JC.SECOND_REFERENCED_REGULATION_YEAR)
                        .journalNo(JC.SECOND_REFERENCED_REGULATION_JOURNAL_NO)
                        .entry(JC.SECOND_REFERENCED_REGULATION_ENTRY)
                );

        JudgmentReferencedRegulation thirdJudgmentReferencedRegulation = judgmentReferencedRegulation()
                .rawText(JC.THIRD_REFERENCED_REGULATION_TEXT)
                .lawJournalEntry(
                        lawJournalEntry()
                        .title(JC.THIRD_REFERENCED_REGULATION_TITLE)
                        .year(JC.THIRD_REFERENCED_REGULATION_YEAR)
                        .journalNo(JC.THIRD_REFERENCED_REGULATION_JOURNAL_NO)
                        .entry(JC.THIRD_REFERENCED_REGULATION_ENTRY)
                );

        judgment.addReferencedRegulation(judgmentReferencedRegulation);
        judgment.addReferencedRegulation(secondJudgmentReferencedRegulation);
        judgment.addReferencedRegulation(thirdJudgmentReferencedRegulation);
        //*** end references ***


        judgment.setTextContent(JC.TEXT_CONTENT);

        judgment.setJudgmentDate(new LocalDate(JC.DATE_YEAR, JC.DATE_MONTH, JC.DATE_DAY));


        judgment.addJudge(
                judge(JC.PRESIDING_JUDGE_NAME)
                .judgesRoles(Judge.JudgeRole.PRESIDING_JUDGE)
        );

        judgment.addJudge(
                judge(JC.SECOND_JUDGE_NAME)
        );

        judgment.addJudge(
                judge(JC.THIRD_JUDGE_NAME)
        );


        JudgmentSourceInfo judgmentSourceInfo = judgmentSourceInfo(SourceCode.COMMON_COURT)
                .sourceJudgmentId(JC.SOURCE_JUDGMENT_ID)
                .sourceJudgmentUrl(JC.SOURCE_JUDGMENT_URL)
                .publisher(JC.SOURCE_PUBLISHER)
                .reviser(JC.SOURCE_REVISER)
                .publicationDate(new DateTime(JC.SOURCE_PUBLICATION_DATE_IN_MILLISECONDS))
                ;

        judgment.setSourceInfo(judgmentSourceInfo);

        judgment.setCourtDivision(
                commonCourtDivision(JC.DIVISION_ID)
                        .code(JC.DIVISION_CODE)
                        .name(JC.DIVISION_NAME)
                        .type(commonCourtDivisionType(JC.DIVISION_TYPE_NAME, JC.DIVISION_TYPE_CODE))
                        .court(
                                commonCourt(JC.COURT_ID)
                                .name(JC.COURT_NAME)
                                .code(JC.COURT_CODE)
                                .type(JC.COURT_TYPE)
                        )
        );

        judgment.addKeyword(keyword(JC.FIRST_KEYWORD));
        judgment.addKeyword(keyword(JC.SECOND_KEYWORD));



        return judgment;
    }

    public static final class JC {
        public static final int JUDGMENT_ID = 333;
        public static final String CASE_NUMBER = "00112233";
        public static final String DECISION = "oddala apelacje";

        public static final String FIRST_LEGAL_BASE ="art. 88 ust. 1 ustawy z dnia 26 stycznia 1982 r. Karta Nauczyciela";
        public static final String SECOND_LEGAL_BASE="art. 30 i art. 17 ustawy z dn. 16.07.2004r. Prawo telekomunikacyjne";

        public static final String FIRST_REFERENCED_REGULATION_TITLE="Ustawa z dnia 14 czerwca 1960 r. - Kodeks postepowania administracyjnego (Dz. U. z 1960 r. Nr 30, poz. 168)";
        public static final String SECOND_REFERENCED_REGULATION_TITLE="Ustawa z dnia 17 listopada 1964 r. - Kodeks postepowania cywilnego (Dz. U. z 1964 r. Nr 43, poz. 296)";
        public static final String THIRD_REFERENCED_REGULATION_TITLE="Ustawa z dnia 30 sierpnia 2002 r. - Prawo o postepowaniu przed sadami administracyjnymi (Dz. U. z 2002 r. Nr 153, poz. 1270)";

        public static final String FIRST_REFERENCED_REGULATION_TEXT = "some first referenced regulation text";
        public static final int FIRST_REFERENCED_REGULATION_YEAR = 1960;
        public static final int FIRST_REFERENCED_REGULATION_JOURNAL_NO = 30;
        public static final int FIRST_REFERENCED_REGULATION_ENTRY = 168;

        public static final String SECOND_REFERENCED_REGULATION_TEXT = "some second referenced regulation text";
        public static final int SECOND_REFERENCED_REGULATION_YEAR = 1964;
        public static final int SECOND_REFERENCED_REGULATION_JOURNAL_NO = 43;
        public static final int SECOND_REFERENCED_REGULATION_ENTRY = 296;

        public static final String THIRD_REFERENCED_REGULATION_TEXT = "some third referenced regulation text";
        public static final int THIRD_REFERENCED_REGULATION_YEAR = 2002;
        public static final int THIRD_REFERENCED_REGULATION_JOURNAL_NO = 153;
        public static final int THIRD_REFERENCED_REGULATION_ENTRY = 1270;

        public static final String FIRST_COURT_REPORTER = "Adam Nowak";
        public static final String SECOND_COURT_REPORTER = "Jan Kowalski";

        public static final String SUMMARY = "Apelacja jest bezzasadna";

        public static final String REASONING_TEXT = "Prezes Urzedu Komunikacji Elektronicznej (dalej Prezes UKE, pozwany)" +
                " na podstawie art. 28 ust. 1 w zw. z art. 30 i art. 17 ustawy z dn. 16.07.2004r. Prawo telekomunikacyjne " +
                "(Dz. U. nr 171, poz. 1800 ze zm., dalej Pt) oraz art. 104  1 kpa w zw. z art. 206 ust. 1 Pt po rozpatrzeniu " +
                "wniosku (...) sp. z o.o. z/s w W. (dalej (...), powod) o wydanie decyzji zmieniajacej Umowe o Polaczeniu Sieci " +
                "i wzajemnych zasadach rozliczen (dalej Umowa) zawarta w dniu 18.03.2005r. pomiedzy (...) a (...) sp. z o.o. z/s " +
                "w W. (dalej (...), zainteresowany) w zakresie zmiany stawki z tytulu zakanczania polaczen glosowych w publicznej " +
                "ruchomej sieci telefonicznej (...) (dalej stawka (...)) zgodnie z decyzja Prezesa UKE z dn. 22.10.2008r. nr (...) " +
                "(dalej Decyzja (...) 2008) decyzja z dn. 13.02.2009r. nr (...) zmienil w Zalaczniku Finansowym Umowy Rozdzial 2, ";

        public static final String TEXT_CONTENT = "some text content";

        public static final int DATE_YEAR = 2011;
        public static final int DATE_MONTH = 12;
        public static final int DATE_DAY = 14;

        public static final String PRESIDING_JUDGE_NAME = "Ewa Sniegocka";
        public static final String SECOND_JUDGE_NAME = "Irena Piotrowska";
        public static final String THIRD_JUDGE_NAME = "Ewa Zalewska";

        public static final String SOURCE_JUDGMENT_ID = "155515000001003_II_Cz_000561_2014_Uz_2014-04-28_001";
        public static final String SOURCE_JUDGMENT_URL = "http://orzeczenia.ms.gov.pl/ncourt-api/judgement/details?id=155515000001003_II_Cz_000561_2014_Uz_2014-04-28_001";
        public static final String SOURCE_PUBLISHER = "Grazyna Magryta-Golaszewska";
        public static final String SOURCE_REVISER = "Mariola Baran";
        public static final long SOURCE_PUBLICATION_DATE_IN_MILLISECONDS = 12849384939383L;

        public static final String REASONING_JUDGMENT_ID = "150505050004027_VIII_GC_000050_2014_Uz_2014-04-16_002";
        public static final String REASONING_JUDGMENT_URL = "http://orzeczenia.ms.gov.pl/ncourt-api/judgement/details?id=150505050004027_VIII_GC_000050_2014_Uz_2014-04-16_002";
        public static final String REASONING_PUBLISHER = "Agnieszka Grusza";
        public static final String REASONING_REVISER = "Marek Gruszka";
        public static final long REASONING_PUBLICATION_DATE_IN_MILLISECONDS = 73L;


        public static final int DIVISION_ID = 444;
        public static final String DIVISION_NAME = "I Wydzial Cywilny";
        public static final String DIVISION_CODE = "0000503";

        public static final String DIVISION_TYPE_NAME = "Cywilny";
        public static final String DIVISION_TYPE_CODE = "03";

        public static final int COURT_ID = 555;
        public static final String COURT_NAME = "Sad Apelacyjny we Wroclawiu";
        public static final String COURT_CODE = "15500000";
        public static final CommonCourt.CommonCourtType COURT_TYPE = CommonCourt.CommonCourtType.APPEAL;

        public static final String FIRST_KEYWORD = "konsument";
        public static final String SECOND_KEYWORD = "klauzule niedozwolone";




    }


    private static LawJournalEntryWrapper lawJournalEntry(){
        return new LawJournalEntryWrapper();
    }

    private static JudgmentReferencedRegulationWrapper judgmentReferencedRegulation(){
        return new JudgmentReferencedRegulationWrapper();
    }

    private static JudgeWrapper judge(String name){
        return new JudgeWrapper(name);
    }

    private static JudgmentSourceInfoWrapper judgmentSourceInfo(SourceCode sourceCode){
        return new JudgmentSourceInfoWrapper(sourceCode);
    }

    private static JudgmentReasoningWrapper judgmentReasoning(String text){
        return new JudgmentReasoningWrapper(text);
    }

    private static CommonCourtDivisionWrapper commonCourtDivision(int divisionId){
        return new CommonCourtDivisionWrapper(divisionId);
    }

    private static CommonCourtDivisionType commonCourtDivisionType(String name, String code){
        CommonCourtDivisionType divisionType = new CommonCourtDivisionType();
        divisionType.setCode(code);
        divisionType.setName(name);

        return divisionType;
    }

    private static CommonCourtJudgmentWrapper commonCourtJudgmentWrapper(int id){
        return new CommonCourtJudgmentWrapper(id);
    }

    private static CommonCourtWrapper commonCourt(int courtId){
        return new CommonCourtWrapper(courtId);
    }

    private static CcJudgmentKeyword keyword(String keyword){
        return new CcJudgmentKeyword(keyword);
    }


    private static class LawJournalEntryWrapper extends LawJournalEntry {

        public LawJournalEntryWrapper title(String title){
            setTitle(title);
            return this;
        }

        public LawJournalEntryWrapper year(int year){
            setYear(year);
            return this;
        }

        public LawJournalEntryWrapper entry(int entry){
            setEntry(entry);
            return this;
        }

        public LawJournalEntryWrapper journalNo(int journalNo){
            setJournalNo(journalNo);
            return this;
        }

    }


    private static class JudgmentReferencedRegulationWrapper extends JudgmentReferencedRegulation {


        public JudgmentReferencedRegulationWrapper rawText(String rawText){
            setRawText(rawText);
            return this;
        }

        public JudgmentReferencedRegulationWrapper lawJournalEntry(LawJournalEntry lawJournalEntry){
            setLawJournalEntry(lawJournalEntry);
            return this;
        }
    }

    private static class JudgeWrapper extends Judge {

        private JudgeWrapper(String name){
            setName(name);
        }

        public JudgeWrapper judgesRoles(JudgeRole ... judgeRoles){
            setSpecialRoles(Arrays.asList(judgeRoles));
            return this;
        }

    }

    private static class JudgmentSourceInfoWrapper extends JudgmentSourceInfo {

        private JudgmentSourceInfoWrapper(SourceCode sourceCode) {
            setSourceCode(sourceCode);
        }

        public JudgmentSourceInfoWrapper sourceJudgmentUrl(String sourceJudgmentUrl){
            setSourceJudgmentUrl(sourceJudgmentUrl);
            return this;
        }

        public JudgmentSourceInfoWrapper sourceJudgmentId(String sourceJudgmentId){
            setSourceJudgmentId(sourceJudgmentId);
            return this;
        }

        public JudgmentSourceInfoWrapper publisher(String publisher){
            setPublisher(publisher);
            return this;
        }

        public JudgmentSourceInfoWrapper reviser(String reviser){
            setReviser(reviser);
            return this;
        }

        public JudgmentSourceInfoWrapper publicationDate(DateTime publicationDate){
            setPublicationDate(publicationDate);
            return this;
        }
    }

    private static class JudgmentReasoningWrapper extends JudgmentReasoning {

        private JudgmentReasoningWrapper(String text) {
            setText(text);
        }

        public JudgmentReasoningWrapper sourceInfo(JudgmentSourceInfo sourceInfo){
            setSourceInfo(sourceInfo);
            return this;
        }

    }

    private static class CommonCourtDivisionWrapper extends CommonCourtDivision {

        public CommonCourtDivisionWrapper(int divisionId){
            setId(divisionId);
        }

        public CommonCourtDivisionWrapper court(CommonCourt court){
            setCourt(court);
            return this;
        }

        public CommonCourtDivisionWrapper name(String name){
            setName(name);
            return this;
        }

        public CommonCourtDivisionWrapper code(String code){
            setCode(code);
            return this;
        }

        public CommonCourtDivisionWrapper type(CommonCourtDivisionType type){
            setType(type);
            return this;
        }

    }

    private static class CommonCourtWrapper extends CommonCourt {

        public CommonCourtWrapper(int courtId) {
            setId(courtId);
        }

        public CommonCourtWrapper code(String code){
            setCode(code);
            return this;
        }

        public CommonCourtWrapper name(String name){
            setName(name);
            return this;
        }

        public CommonCourtWrapper type(CommonCourtType type){
            setType(type);
            return this;
        }

    }

    private static class CommonCourtJudgmentWrapper extends CommonCourtJudgment {
        public CommonCourtJudgmentWrapper(int id){
            setId(id);
        }
    }
}
