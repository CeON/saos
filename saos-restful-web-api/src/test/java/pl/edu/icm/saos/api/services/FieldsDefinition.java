package pl.edu.icm.saos.api.services;


import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import pl.edu.icm.saos.persistence.builder.CourtCaseBuilder;
import pl.edu.icm.saos.persistence.model.*;

import static pl.edu.icm.saos.persistence.builder.BuildersFactory.*;

/**
 * @author pavtel
 */
public abstract class FieldsDefinition {

    public static CommonCourtJudgment createCommonJudgment(){
        CommonCourtJudgment judgment = commonCourtJudgmentWrapper(JC.JUDGMENT_ID).build();
        judgment.addCourtCase(CourtCaseBuilder.create(JC.CASE_NUMBER).build());
        judgment.setDecision(JC.DECISION);

        judgment.addLegalBase(JC.FIRST_LEGAL_BASE);
        judgment.addLegalBase(JC.SECOND_LEGAL_BASE);

        judgment.addCourtReporter(JC.FIRST_COURT_REPORTER);
        judgment.addCourtReporter(JC.SECOND_COURT_REPORTER);

        judgment.setSummary(JC.SUMMARY);

        judgment.setJudgmentType(Judgment.JudgmentType.SENTENCE);

        //*** references ***
        JudgmentReferencedRegulation judgmentReferencedRegulation = judgmentReferencedRegulation()
                .rawText(JC.FIRST_REFERENCED_REGULATION_TEXT)
                .lawJournalEntry(
                         lawJournalEntry()
                        .title(JC.FIRST_REFERENCED_REGULATION_TITLE)
                        .year(JC.FIRST_REFERENCED_REGULATION_YEAR)
                        .journalNo(JC.FIRST_REFERENCED_REGULATION_JOURNAL_NO)
                        .entry(JC.FIRST_REFERENCED_REGULATION_ENTRY).build()
                ).build();

        JudgmentReferencedRegulation secondJudgmentReferencedRegulation = judgmentReferencedRegulation()
                .rawText(JC.SECOND_REFERENCED_REGULATION_TEXT)
                .lawJournalEntry(
                        lawJournalEntry()
                        .title(JC.SECOND_REFERENCED_REGULATION_TITLE)
                        .year(JC.SECOND_REFERENCED_REGULATION_YEAR)
                        .journalNo(JC.SECOND_REFERENCED_REGULATION_JOURNAL_NO)
                        .entry(JC.SECOND_REFERENCED_REGULATION_ENTRY).build()
                ).build();

        JudgmentReferencedRegulation thirdJudgmentReferencedRegulation = judgmentReferencedRegulation()
                .rawText(JC.THIRD_REFERENCED_REGULATION_TEXT)
                .lawJournalEntry(
                        lawJournalEntry()
                        .title(JC.THIRD_REFERENCED_REGULATION_TITLE)
                        .year(JC.THIRD_REFERENCED_REGULATION_YEAR)
                        .journalNo(JC.THIRD_REFERENCED_REGULATION_JOURNAL_NO)
                        .entry(JC.THIRD_REFERENCED_REGULATION_ENTRY).build()
                ).build();

        judgment.addReferencedRegulation(judgmentReferencedRegulation);
        judgment.addReferencedRegulation(secondJudgmentReferencedRegulation);
        judgment.addReferencedRegulation(thirdJudgmentReferencedRegulation);
        //*** end references ***


        judgment.setTextContent(JC.TEXT_CONTENT);

        judgment.setJudgmentDate(new LocalDate(JC.DATE_YEAR, JC.DATE_MONTH, JC.DATE_DAY));


        judgment.addJudge(
                judge(JC.PRESIDING_JUDGE_NAME)
                .judgesRoles(Judge.JudgeRole.PRESIDING_JUDGE)
                .build()
        );

        judgment.addJudge(
                judge(JC.SECOND_JUDGE_NAME)
                .build()
        );

        judgment.addJudge(
                judge(JC.THIRD_JUDGE_NAME)
                .build()
        );


        JudgmentSourceInfo judgmentSourceInfo = judgmentSourceInfo(SourceCode.COMMON_COURT)
                .sourceJudgmentId(JC.SOURCE_JUDGMENT_ID)
                .sourceJudgmentUrl(JC.SOURCE_JUDGMENT_URL)
                .publisher(JC.SOURCE_PUBLISHER)
                .reviser(JC.SOURCE_REVISER)
                .publicationDate(new DateTime(JC.SOURCE_PUBLICATION_DATE_IN_MILLISECONDS))
                .build()
                ;

        judgment.setSourceInfo(judgmentSourceInfo);

        judgment.setCourtDivision(
                createCommonDivision()
        );

        judgment.addKeyword(keyword(JC.FIRST_KEYWORD));
        judgment.addKeyword(keyword(JC.SECOND_KEYWORD));



        return judgment;
    }

    public static CommonCourtDivision createCommonDivision(){
        return
                commonCourtDivision(JC.DIVISION_ID)
                        .code(JC.DIVISION_CODE)
                        .name(JC.DIVISION_NAME)
                        .type(commonCourtDivisionType(JC.DIVISION_TYPE_NAME, JC.DIVISION_TYPE_CODE))
                        .court(
                                commonCourt(JC.COURT_ID)
                                        .name(JC.COURT_NAME)
                                        .code(JC.COURT_CODE)
                                        .type(JC.COURT_TYPE)
                                        .parent(
                                                commonCourt(JC.COURT_PARENT_ID)
                                                        .name(JC.COURT_PARENT_NAME)
                                                        .type(JC.COURT_PARENT_TYPE)
                                                        .build()
                                        ).build()
                        ).build();
    }

    private static CommonCourtDivision createSecondCommonDivision(){
        return
                commonCourtDivision(JC.SECOND_DIVISION_ID)
                        .code(JC.SECOND_DIVISION_CODE)
                        .name(JC.SECOND_DIVISION_NAME)
                        .type(commonCourtDivisionType(JC.SECOND_DIVISION_TYPE_NAME, JC.SECOND_DIVISION_TYPE_CODE))
                        .build()
                        ;
    }

    public static CommonCourt createCommonCourt(){
        return
                commonCourt(JC.COURT_ID)
                        .name(JC.COURT_NAME)
                        .code(JC.COURT_CODE)
                        .type(JC.COURT_TYPE)
                        .parent(
                                commonCourt(JC.COURT_PARENT_ID)
                                        .name(JC.COURT_PARENT_NAME)
                                        .type(JC.COURT_PARENT_TYPE)
                                        .build()
                        )
                        .divisions(
                                createCommonDivision(),
                                createSecondCommonDivision()
                        )
                        .build()
                ;
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

        public static final int DIVISION_ID = 444;
        public static final String DIVISION_NAME = "I Wydzial Cywilny";
        public static final String DIVISION_CODE = "0000503";

        public static final String DIVISION_TYPE_NAME = "Cywilny";
        public static final String DIVISION_TYPE_CODE = "03";

        public static final int SECOND_DIVISION_ID = 555;
        public static final String SECOND_DIVISION_NAME = "II Wydzial Karny";
        public static final String SECOND_DIVISION_CODE = "0000604";

        public static final String SECOND_DIVISION_TYPE_NAME = "Karny";
        public static final String SECOND_DIVISION_TYPE_CODE = "04";

        public static final int COURT_ID = 555;
        public static final String COURT_NAME = "Sad Rejonowy we Wroclawiu";
        public static final String COURT_CODE = "15500000";
        public static final CommonCourt.CommonCourtType COURT_TYPE = CommonCourt.CommonCourtType.REGIONAL;

        public static final int COURT_PARENT_ID = 2222;
        public static final String COURT_PARENT_NAME = "Sad Apelacyjny we Wroclawiu";
        public static final CommonCourt.CommonCourtType COURT_PARENT_TYPE = CommonCourt.CommonCourtType.APPEAL;

        public static final String FIRST_KEYWORD = "konsument";
        public static final String SECOND_KEYWORD = "klauzule niedozwolone";




    }



}
