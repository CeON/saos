package pl.edu.icm.saos.persistence.common;

import pl.edu.icm.saos.persistence.model.*;

/**
 * @author pavtel
 */
public abstract class TestObjectsDefaultData {

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

    public static final Judgment.JudgmentType JUDGMENT_TYPE = Judgment.JudgmentType.SENTENCE;

    public static final String TEXT_CONTENT = "some text content";

    public static final int DATE_YEAR = 2011;
    public static final int DATE_MONTH = 12;
    public static final int DATE_DAY = 14;

    public static final String FIRST_JUDGE_NAME = "Ewa Sniegocka";
    public static final Judge.JudgeRole FIRST_JUDGE_ROLE = Judge.JudgeRole.PRESIDING_JUDGE;
    public static final String SECOND_JUDGE_NAME = "Irena Piotrowska";
    public static final String THIRD_JUDGE_NAME = "Ewa Zalewska";

    public static final SourceCode CC_SOURCE_CODE = SourceCode.COMMON_COURT;
    public static final SourceCode SC_SOURCE_CODE = SourceCode.SUPREME_COURT;
    public static final SourceCode SOURCE_CODE = CC_SOURCE_CODE;


    public static final String CC_SOURCE_JUDGMENT_ID = "155515000001003_II_Cz_000561_2014_Uz_2014-04-28_001";
    public static final String SC_SOURCE_JUDGMENT_ID = "155515000001003_III_Cz_000561_2014_Uz_2014-04-29_002";
    public static final String SOURCE_JUDGMENT_ID = CC_SOURCE_JUDGMENT_ID;
    public static final String SOURCE_JUDGMENT_URL = "http://orzeczenia.ms.gov.pl/ncourt-api/judgement/details?id=155515000001003_II_Cz_000561_2014_Uz_2014-04-28_001";
    public static final String SOURCE_PUBLISHER = "Grazyna Magryta-Golaszewska";
    public static final String SOURCE_REVISER = "Mariola Baran";
    public static final long SOURCE_PUBLICATION_DATE_IN_MILLISECONDS = 12849384939383L;

    public static final String CC_DIVISION_NAME = "I Wydzial Cywilny";
    public static final String CC_DIVISION_CODE = "0000503";

    public static final String CC_DIVISION_TYPE_NAME = "Cywilny";
    public static final String CC_DIVISION_TYPE_CODE = "03";

    public static final String CC_FIRST_DIVISION_NAME = CC_DIVISION_NAME;
    public static final String CC_FIRST_DIVISION_CODE = CC_DIVISION_CODE;

    public static final String CC_FIRST_DIVISION_TYPE_NAME = CC_DIVISION_TYPE_NAME;
    public static final String CC_FIRST_DIVISION_TYPE_CODE = CC_DIVISION_TYPE_CODE;

    public static final String CC_SECOND_DIVISION_NAME = "II Wydzial Karny";
    public static final String CC_SECOND_DIVISION_CODE = "0000604";

    public static final String CC_SECOND_DIVISION_TYPE_NAME = "Karny";
    public static final String CC_SECOND_DIVISION_TYPE_CODE = "04";

    public static final String CC_COURT_NAME = "Sad Rejonowy we Wroclawiu";
    public static final String CC_COURT_CODE = "15500000";
    public static final CommonCourt.CommonCourtType CC_COURT_TYPE = CommonCourt.CommonCourtType.REGIONAL;

    public static final String CC_COURT_PARENT_NAME = "Sad Apelacyjny we Wroclawiu";
    public static final CommonCourt.CommonCourtType CC_COURT_PARENT_TYPE = CommonCourt.CommonCourtType.APPEAL;
    public static final String CC_COURT_PARENT_CODE = "16660000";

    public static final String FIRST_KEYWORD = "konsument";
    public static final String SECOND_KEYWORD = "klauzule niedozwolone";


    public static final String SC_FIRST_DIVISION_NAME ="Wydzial karny I";
    public static final String SC_FIRST_DIVISION_FULL_NAME ="Wydzial karny I pelna nazwa";
    public static final String SC_CHAMBER_NAME="Izba Cywilna";

    public static final String SC_JUDGMENT_FORM_NAME = "postanowienie SC SN";
    public static final SupremeCourtJudgment.PersonnelType SC_PERSONNEL_TYPE = SupremeCourtJudgment.PersonnelType.FIVE_PERSON;

    public static final String SC_FIRST_CHAMBER_NAME = SC_CHAMBER_NAME;
    public static final String SC_SECOND_CHAMBER_NAME = "Izba karna";
}
