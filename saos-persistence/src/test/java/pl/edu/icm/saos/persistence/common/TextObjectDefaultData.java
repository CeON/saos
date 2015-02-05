package pl.edu.icm.saos.persistence.common;

import static pl.edu.icm.saos.common.json.JsonNormalizer.normalizeJson;
import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.SourceCode;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment;

/**
 * @author pavtel
 */
public final class TextObjectDefaultData {

    private TextObjectDefaultData() {
    }

    public static final String CC_CASE_NUMBER = "00112233";
    public static final String SC_CASE_NUMBER = "00223344";
    public static final String CT_CASE_NUMBER = "00334455";
    public static final String NAC_CASE_NUMBER = "00445566";
    public static final String CASE_NUMBER = CC_CASE_NUMBER;
    public static final String CC_DECISION = "oddala apelacje";
    public static final String SC_DECISION = "przyjeto apelacje";
    public static final String CT_DECISION = "uznano wszystko";
    public static final String NAC_DECISION = "nie uznaje apelacji";
    public static final String DECISION = CC_DECISION;

    public static final String CC_FIRST_LEGAL_BASE ="art. 88 ust. 1 ustawy z dnia 26 stycznia 1982 r. Karta Nauczyciela";
    public static final String SC_FIRST_LEGAL_BASE ="art. 5 ust. 2 ustawy z dnia 22 listopada 2013 r. o zmianie ustawy o systemie informacji oswiatowej";
    public static final String CT_FIRST_LEGAL_BASE ="art. 28 i art. 17 ustawy z dn. 16.07.2004r. Prawo telekomunikacyjne";
    public static final String NAC_FIRST_LEGAL_BASE ="art. 28 i art. 17 ustawy z dn. 16.07.2004r. Prawo telekomunikacyjne";
    public static final String FIRST_LEGAL_BASE =CC_FIRST_LEGAL_BASE;
    public static final String CC_SECOND_LEGAL_BASE="art. 30 i art. 17 ustawy z dn. 16.07.2004r. Prawo telekomunikacyjne";
    public static final String SC_SECOND_LEGAL_BASE="art. 3 i art. 12 ustawa z dnia 18 sierpnia 2011 r. o bezpieczenstwie morskim";
    public static final String CT_SECOND_LEGAL_BASE="art. 4 i art. 13 ustawa z dnia 18 sierpnia 2011 r. o bezpieczenstwie morskim";
    public static final String NAC_SECOND_LEGAL_BASE="art. 4 i art. 13 ustawa z dnia 18 sierpnia 2011 r. o bezpieczenstwie morskim";
    public static final String SECOND_LEGAL_BASE=CC_SECOND_LEGAL_BASE;

    public static final String CC_FIRST_REFERENCED_REGULATION_TITLE="Ustawa z dnia 14 czerwca 1960 r. - Kodeks postepowania administracyjnego (Dz. U. z 1960 r. Nr 30, poz. 168)";
    public static final String CC_SECOND_REFERENCED_REGULATION_TITLE="Ustawa z dnia 17 listopada 1964 r. - Kodeks postepowania cywilnego (Dz. U. z 1964 r. Nr 43, poz. 296)";
    public static final String CC_THIRD_REFERENCED_REGULATION_TITLE="Ustawa z dnia 30 sierpnia 2002 r. - Prawo o postepowaniu przed sadami administracyjnymi (Dz. U. z 2002 r. Nr 153, poz. 1270)";

    public static final String SC_FIRST_REFERENCED_REGULATION_TITLE="first sc Ustawa z dnia 14 czerwca 1960 r. - Kodeks postepowania administracyjnego (Dz. U. z 1960 r. Nr 30, poz. 168)";
    public static final String SC_SECOND_REFERENCED_REGULATION_TITLE="second sc Ustawa z dnia 17 listopada 1964 r. - Kodeks postepowania cywilnego (Dz. U. z 1964 r. Nr 43, poz. 296)";
    public static final String SC_THIRD_REFERENCED_REGULATION_TITLE="third sc Ustawa z dnia 30 sierpnia 2002 r. - Prawo o postepowaniu przed sadami administracyjnymi (Dz. U. z 2002 r. Nr 153, poz. 1270)";

    public static final String CT_FIRST_REFERENCED_REGULATION_TITLE="first ct Ustawa z dnia 14 czerwca 1960 r. - Kodeks postepowania administracyjnego (Dz. U. z 1960 r. Nr 30, poz. 168)";
    public static final String CT_SECOND_REFERENCED_REGULATION_TITLE="second ct Ustawa z dnia 17 listopada 1964 r. - Kodeks postepowania cywilnego (Dz. U. z 1964 r. Nr 43, poz. 296)";
    
    public static final String NAC_FIRST_REFERENCED_REGULATION_TITLE="first nac Ustawa z dnia 14 czerwca 1960 r. - Kodeks postepowania administracyjnego (Dz. U. z 1960 r. Nr 30, poz. 168)";
    public static final String NAC_SECOND_REFERENCED_REGULATION_TITLE="second nac Ustawa z dnia 17 listopada 1964 r. - Kodeks postepowania cywilnego (Dz. U. z 1964 r. Nr 43, poz. 296)";

    public static final String FIRST_REFERENCED_REGULATION_TITLE=CC_FIRST_REFERENCED_REGULATION_TITLE;
    public static final String SECOND_REFERENCED_REGULATION_TITLE=CC_SECOND_REFERENCED_REGULATION_TITLE;
    public static final String THIRD_REFERENCED_REGULATION_TITLE=CC_THIRD_REFERENCED_REGULATION_TITLE;


    public static final String CC_FIRST_REFERENCED_REGULATION_TEXT = "some first referenced regulation text";
    public static final int CC_FIRST_REFERENCED_REGULATION_YEAR = 1960;
    public static final int CC_FIRST_REFERENCED_REGULATION_JOURNAL_NO = 30;
    public static final int CC_FIRST_REFERENCED_REGULATION_ENTRY = 168;

    public static final String SC_FIRST_REFERENCED_REGULATION_TEXT = "SC some first referenced regulation text";
    public static final int SC_FIRST_REFERENCED_REGULATION_YEAR = 1981;
    public static final int SC_FIRST_REFERENCED_REGULATION_JOURNAL_NO = 21;
    public static final int SC_FIRST_REFERENCED_REGULATION_ENTRY = 111;

    public static final String CT_FIRST_REFERENCED_REGULATION_TEXT = "CT some first referenced regulation text";
    public static final int CT_FIRST_REFERENCED_REGULATION_YEAR = 1971;
    public static final int CT_FIRST_REFERENCED_REGULATION_JOURNAL_NO = 11;
    public static final int CT_FIRST_REFERENCED_REGULATION_ENTRY = 222;
    
    public static final String NAC_FIRST_REFERENCED_REGULATION_TEXT = "NAC some first referenced regulation text";
    public static final int NAC_FIRST_REFERENCED_REGULATION_YEAR = 1961;
    public static final int NAC_FIRST_REFERENCED_REGULATION_JOURNAL_NO = 1;
    public static final int NAC_FIRST_REFERENCED_REGULATION_ENTRY = 212;

    public static final String CC_SECOND_REFERENCED_REGULATION_TEXT = "some second referenced regulation text";
    public static final int CC_SECOND_REFERENCED_REGULATION_YEAR = 1964;
    public static final int CC_SECOND_REFERENCED_REGULATION_JOURNAL_NO = 43;
    public static final int CC_SECOND_REFERENCED_REGULATION_ENTRY = 296;

    public static final String CT_SECOND_REFERENCED_REGULATION_TEXT = "CT some second referenced regulation text";
    public static final int CT_SECOND_REFERENCED_REGULATION_YEAR = 1964;
    public static final int CT_SECOND_REFERENCED_REGULATION_JOURNAL_NO = 44;
    public static final int CT_SECOND_REFERENCED_REGULATION_ENTRY = 413;

    public static final String SC_SECOND_REFERENCED_REGULATION_TEXT = "SC some second referenced regulation text";
    public static final int SC_SECOND_REFERENCED_REGULATION_YEAR = 1975;
    public static final int SC_SECOND_REFERENCED_REGULATION_JOURNAL_NO = 45;
    public static final int SC_SECOND_REFERENCED_REGULATION_ENTRY = 355;
    
    public static final String NAC_SECOND_REFERENCED_REGULATION_TEXT = "NAC some second referenced regulation text";
    public static final int NAC_SECOND_REFERENCED_REGULATION_YEAR = 1976;
    public static final int NAC_SECOND_REFERENCED_REGULATION_JOURNAL_NO = 46;
    public static final int NAC_SECOND_REFERENCED_REGULATION_ENTRY = 359;

    public static final String CC_THIRD_REFERENCED_REGULATION_TEXT = "CC some third referenced regulation text";
    public static final int CC_THIRD_REFERENCED_REGULATION_YEAR = 2002;
    public static final int CC_THIRD_REFERENCED_REGULATION_JOURNAL_NO = 153;
    public static final int CC_THIRD_REFERENCED_REGULATION_ENTRY = 1270;

    public static final String SC_THIRD_REFERENCED_REGULATION_TEXT = "SS some third referenced regulation text";
    public static final int SC_THIRD_REFERENCED_REGULATION_YEAR = 2033;
    public static final int SC_THIRD_REFERENCED_REGULATION_JOURNAL_NO = 133;
    public static final int SC_THIRD_REFERENCED_REGULATION_ENTRY = 1233;

    public static final String CC_FIRST_COURT_REPORTER = "Adam Nowak";
    public static final String CC_SECOND_COURT_REPORTER = "Jan Kowalski";

    public static final String SC_FIRST_COURT_REPORTER = "Ewa Wesolowska";
    public static final String SC_SECOND_COURT_REPORTER = "Dominik Wesolowski";

    public static final String CT_FIRST_COURT_REPORTER = "Adam Niewinski";
    public static final String CT_SECOND_COURT_REPORTER = "Piotr Przybylski";
    
    public static final String NAC_FIRST_COURT_REPORTER = "Norbert Zawadzki";
    public static final String NAC_SECOND_COURT_REPORTER = "Kazimierz Dudek";

    public static final String CC_SUMMARY = "CC Apelacja jest bezzasadna";
    public static final String SC_SUMMARY = "SC Apelacja jest zasadna";
    public static final String CT_SUMMARY = "CT Apelacja jest zasadna";
    public static final String NAC_SUMMARY = "NAC Apelacja jest bezzasadna";

    public static final Judgment.JudgmentType CC_JUDGMENT_TYPE = Judgment.JudgmentType.SENTENCE;
    public static final Judgment.JudgmentType SC_JUDGMENT_TYPE = Judgment.JudgmentType.REASONS;
    public static final Judgment.JudgmentType CT_JUDGMENT_TYPE = Judgment.JudgmentType.REGULATION;
    public static final Judgment.JudgmentType NAC_JUDGMENT_TYPE = Judgment.JudgmentType.DECISION;

    public static final String CC_TEXT_CONTENT = "CC some text content";
    public static final String SC_TEXT_CONTENT = "SC some text content";
    public static final String CT_TEXT_CONTENT = "CT some text content";
    public static final String NAC_TEXT_CONTENT = "NAC some text content";

    public static final int CC_DATE_YEAR = 2011;
    public static final int CC_DATE_MONTH = 12;
    public static final int CC_DATE_DAY = 14;

    public static final int SC_DATE_YEAR = 2012;
    public static final int SC_DATE_MONTH = 11;
    public static final int SC_DATE_DAY = 11;

    public static final int CT_DATE_YEAR = 2013;
    public static final int CT_DATE_MONTH = 10;
    public static final int CT_DATE_DAY = 22;
    
    public static final int NAC_DATE_YEAR = 2014;
    public static final int NAC_DATE_MONTH = 9;
    public static final int NAC_DATE_DAY = 23;

    public static final String CC_FIRST_JUDGE_NAME = "Ewa Sniegocka";
    public static final Judge.JudgeRole CC_FIRST_JUDGE_ROLE = Judge.JudgeRole.PRESIDING_JUDGE;
    public static final String CC_FIRST_JUDGE_FUNCTION = "CC first function";
    public static final String CC_SECOND_JUDGE_NAME = "Irena Piotrowska";
    public static final String CC_THIRD_JUDGE_NAME = "Ewa Zalewska";

    public static final String SC_FIRST_JUDGE_NAME = "Katarzyna Zima";
    public static final Judge.JudgeRole SC_FIRST_JUDGE_ROLE = Judge.JudgeRole.PRESIDING_JUDGE;
    public static final String SC_FIRST_JUDGE_FUNCTION = "SC first function";
    public static final String SC_SECOND_JUDGE_NAME = "Urszula Zawadzka";
    public static final Judge.JudgeRole SC_SECOND_JUDGE_ROLE = Judge.JudgeRole.REASONS_FOR_JUDGMENT_AUTHOR;

    public static final String CT_FIRST_JUDGE_NAME = "Agnieszka Kowalska";
    public static final Judge.JudgeRole CT_FIRST_JUDGE_ROLE = Judge.JudgeRole.PRESIDING_JUDGE;
    public static final String CT_FIRST_JUDGE_FUNCTION = "CT first function";
    public static final String CT_SECOND_JUDGE_NAME = "Urszula Nieznana";
    
    public static final String NAC_FIRST_JUDGE_NAME = "Tadeusz Kowalski";
    public static final Judge.JudgeRole NAC_FIRST_JUDGE_ROLE = Judge.JudgeRole.PRESIDING_JUDGE;
    public static final String NAC_SECOND_JUDGE_NAME = "Anna Kowalska";
    public static final String NAC_THIRD_JUDGE_NAME = "Ernest Kowalski";

    public static final SourceCode CC_SOURCE_CODE = SourceCode.COMMON_COURT;
    public static final SourceCode SC_SOURCE_CODE = SourceCode.SUPREME_COURT;
    public static final SourceCode CT_SOURCE_CODE = SourceCode.CONSTITUTIONAL_TRIBUNAL;
    public static final SourceCode NAC_SOURCE_CODE = SourceCode.NATIONAL_APPEAL_CHAMBER;
    public static final SourceCode SOURCE_CODE = CC_SOURCE_CODE;


    public static final String CC_SOURCE_JUDGMENT_ID = "155515000001003_II_Cz_000561_2014_Uz_2014-04-28_001";
    public static final String SC_SOURCE_JUDGMENT_ID = "155515000001003_III_Cz_000561_2014_Uz_2014-04-29_002";
    public static final String CT_SOURCE_JUDGMENT_ID = "155515000001003_II_Cz_000561_2014_Uz_2014-04-29_003";
    public static final String NAC_SOURCE_JUDGMENT_ID = "155515000001003_II_Cz_000561_2014_Uz_2014-04-29_004";
    public static final String SOURCE_JUDGMENT_ID = CC_SOURCE_JUDGMENT_ID;
    public static final String CC_SOURCE_JUDGMENT_URL = "http://orzeczenia.ms.gov.pl/ncourt-api/judgement/details?id=155515000001003_II_Cz_000561_2014_Uz_2014-04-28_001";
    public static final String SC_SOURCE_JUDGMENT_URL = "http://orzeczenia.ms.gov.pl/ncourt-api/judgement/details?id=155515000001003_II_Cz_000561_2014_Uz_2014-04-29_002";
    public static final String CT_SOURCE_JUDGMENT_URL = "http://orzeczenia.ms.gov.pl/ncourt-api/judgement/details?id=155515000001003_II_Cz_000561_2014_Uz_2014-04-29_003";
    public static final String NAC_SOURCE_JUDGMENT_URL = "http://orzeczenia.ms.gov.pl/ncourt-api/judgement/details?id=155515000001003_II_Cz_000561_2014_Uz_2014-04-29_004";
    public static final String SOURCE_JUDGMENT_URL = CC_SOURCE_JUDGMENT_URL;
    public static final String CC_SOURCE_PUBLISHER = "Grazyna Magryta-Golaszewska";
    public static final String SC_SOURCE_PUBLISHER = "Anna Wesolowska";
    public static final String CT_SOURCE_PUBLISHER = "Adam Nowak";
    public static final String NAC_SOURCE_PUBLISHER = "Aleksander Kowalczyk";
    public static final String SOURCE_PUBLISHER = CC_SOURCE_PUBLISHER;
    public static final String CC_SOURCE_REVISER = "Mariola Baran";
    public static final String SC_SOURCE_REVISER = "Agnieszka Nowacka";
    public static final String CT_SOURCE_REVISER = "Sylwia Hojna";
    public static final String NAC_SOURCE_REVISER = "Mirosław Grabowski";
    public static final String SOURCE_REVISER = CC_SOURCE_REVISER;
    public static final long CC_SOURCE_PUBLICATION_DATE_IN_MILLISECONDS = 12849384939383L;
    public static final long SC_SOURCE_PUBLICATION_DATE_IN_MILLISECONDS = 12849384939999L;
    public static final long CT_SOURCE_PUBLICATION_DATE_IN_MILLISECONDS = 12849384939955L;
    public static final long NAC_SOURCE_PUBLICATION_DATE_IN_MILLISECONDS = 12849384939944L;
    public static final long SOURCE_PUBLICATION_DATE_IN_MILLISECONDS = CC_SOURCE_PUBLICATION_DATE_IN_MILLISECONDS;

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

    public static final String CC_FIRST_KEYWORD = "konsument";
    public static final String CC_SECOND_KEYWORD = "klauzule niedozwolone";

    public static final String SC_FIRST_KEYWORD = "pracownik";
    public static final String SC_SECOND_KEYWORD = "klazule";

    public static final String CT_FIRST_KEYWORD = "obywatel";
    public static final String CT_SECOND_KEYWORD = "niedozwolone";
    
    public static final String NAC_FIRST_KEYWORD = "prawo cywilne";
    public static final String NAC_SECOND_KEYWORD = "producent";

    public static final String SC_FIRST_DIVISION_NAME ="Wydzial karny I";
    public static final String SC_FIRST_DIVISION_FULL_NAME ="Wydzial karny I pelna nazwa";
    public static final String SC_CHAMBER_NAME="Izba Cywilna";

    public static final String SC_JUDGMENT_FORM_NAME = "postanowienie SC SN";
    public static final SupremeCourtJudgment.PersonnelType SC_PERSONNEL_TYPE = SupremeCourtJudgment.PersonnelType.FIVE_PERSON;

    public static final String SC_FIRST_CHAMBER_NAME = SC_CHAMBER_NAME;
    public static final String SC_SECOND_CHAMBER_NAME = "Izba karna";

    public static final String CT_FIRST_DISSENTING_OPINION_TEXT = "ct first opinion text";
    public static final String CT_FIRST_DISSENTING_OPINION_FIRST_AUTHOR = "ct Adam Kozlowski";
    public static final String CT_FIRST_DISSENTING_OPINION_SECOND_AUTHOR = "ct Marek Nowak";

    public static final String CT_SECOND_DISSENTING_OPINION_TEXT = "ct second opinion text";
    public static final String CT_SECOND_DISSENTING_OPINION_FIRST_AUTHOR = "ct Agnieszka Sok";
    
    
    
    public static final String FIRST_ENRICHMENT_TAG_TYPE = "REFERENCED_REGULATIONS";
    public static final String FIRST_ENRICHMENT_TAG_VALUE_KEY = "ref";
    public static final String FIRST_ENRICHMENT_TAG_VALUE_VALUE = "AAA1";
    public static final String FIRST_ENRICHMENT_TAG_VALUE = normalizeJson("{'" + FIRST_ENRICHMENT_TAG_VALUE_KEY + "':'" + FIRST_ENRICHMENT_TAG_VALUE_VALUE + "'}");
    
    public static final String SECOND_ENRICHMENT_TAG_TYPE = "REFERENCED_CASE_NUMBERS";
    public static final String SECOND_ENRICHMENT_TAG_VALUE_KEY = "caseNumbers";
    public static final String SECOND_ENRICHMENT_TAG_FIRST_ARRAY_VALUE = "XYZ1";
    public static final String SECOND_ENRICHMENT_TAG_SECOND_ARRAY_VALUE = "XYZ2";
    public static final String SECOND_ENRICHMENT_TAG_VALUE = normalizeJson("{'" +
            SECOND_ENRICHMENT_TAG_VALUE_KEY + "':['" +
                SECOND_ENRICHMENT_TAG_FIRST_ARRAY_VALUE + "','" +
                SECOND_ENRICHMENT_TAG_SECOND_ARRAY_VALUE +
            "']}");
    
    public static final String THIRD_ENRICHMENT_TAG_TYPE = "KEYWORDS";
    public static final String THIRD_ENRICHMENT_TAG_VALUE_KEY = "keywords";
    public static final String THIRD_ENRICHMENT_TAG_FIRST_ARRAY_VALUE = "val11";
    public static final String THIRD_ENRICHMENT_TAG_SECOND_ARRAY_VALUE = "val21";
    public static final String THIRD_ENRICHMENT_TAG_THIRD_ARRAY_VALUE = "val32";
    public static final String THIRD_ENRICHMENT_TAG_VALUE = normalizeJson("{'" +
            THIRD_ENRICHMENT_TAG_VALUE_KEY + "':['" +
                THIRD_ENRICHMENT_TAG_FIRST_ARRAY_VALUE + "','" +
                THIRD_ENRICHMENT_TAG_SECOND_ARRAY_VALUE + "','" +
                THIRD_ENRICHMENT_TAG_THIRD_ARRAY_VALUE +
            "']}");
    
    
}
