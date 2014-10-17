package pl.edu.icm.saos.api.search.judgments.extractors;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.chrono.ISOChronology;
import org.junit.Before;
import org.junit.Test;
import pl.edu.icm.saos.api.search.judgments.parameters.JudgmentsParameters;
import pl.edu.icm.saos.api.search.parameters.Pagination;
import pl.edu.icm.saos.api.search.parameters.ParametersExtractor;

import static pl.edu.icm.saos.api.search.judgments.extractors.JudgmentsParametersExtractor.*;
import static pl.edu.icm.saos.api.services.FieldsDefinition.JC;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

public class JudgmentsParametersExtractorTest {

    private JudgmentsParametersExtractor judgmentsParametersExtractor;



    @Before
    public void setUp(){
        ParametersExtractor parametersExtractor = new ParametersExtractor();

        judgmentsParametersExtractor = new JudgmentsParametersExtractor();
        judgmentsParametersExtractor.setParametersExtractor(parametersExtractor);
    }

    @Test
    public void extractFrom__it_should_extract_parameters() throws Exception {
        //given
        String allValue = "some all value";
        int pageSizeValue = 30;
        int pageNumberValue = 10;

        int fromDateYear = 2000;
        int fromDateMonth = 10;
        int fromDateDay = 21;

        int toDateYear = 2010;
        int toDateMonth = 11;
        int toDateDay = 18;


        InputParametersBuilder inputParBuilder = inputParameters()
                .all(allValue)
                .courtName(JC.COURT_NAME)
                .keyword(JC.FIRST_KEYWORD)
                .judgeName(JC.THIRD_JUDGE_NAME)
                .legalBase(JC.FIRST_LEGAL_BASE)
                .referencedRegulation(JC.FIRST_REFERENCED_REGULATION_TEXT)
                .pageSize(pageSizeValue)
                .pageNumber(pageNumberValue)
                .judgmentDateFrom(fromDateYear+"-"+fromDateMonth+"-"+fromDateDay)
                .judgmentDateTo(toDateYear+"-"+toDateMonth+"-"+toDateDay)
                ;

        //when
        JudgmentsParameters actual =judgmentsParametersExtractor.extractFrom(inputParBuilder);

        //then
        JudgmentsParameters expected = new JudgmentsParameters();
        expected.setAll(allValue);
        expected.setCourtName(JC.COURT_NAME);
        expected.setKeyword(JC.FIRST_KEYWORD);
        expected.setJudgeName(JC.THIRD_JUDGE_NAME);
        expected.setLegalBase(JC.FIRST_LEGAL_BASE);
        expected.setReferencedRegulation(JC.FIRST_REFERENCED_REGULATION_TEXT);
        expected.setPagination(new Pagination(pageSizeValue, pageNumberValue));
        expected.setJudgmentDateFrom(new LocalDate(fromDateYear, fromDateMonth, fromDateDay, europeWarsawChronology()));
        expected.setJudgmentDateTo(new LocalDate(toDateYear, toDateMonth, toDateDay, europeWarsawChronology()));

        assertThat(actual, is(expected));
    }

    @Test
    public void extractFrom__it_should_set_null_values_if_parameters_not_present() throws Exception {
        //given
        int pageSize = 3;
        int pageNumber = 0;

        InputParametersBuilder inputParametersBuilder = inputParameters()
                .pageSize(pageSize)
                .pageNumber(pageNumber)
                ;

        //when
        JudgmentsParameters actual = judgmentsParametersExtractor.extractFrom(inputParametersBuilder);

        //then
        assertThat("pagination", actual.getPagination(), is(new Pagination(pageSize, pageNumber)));
        assertThat("all ", actual.getAll(), nullValue());
        assertThat("court name ", actual.getCourtName(), nullValue());
        assertThat("keyword ", actual.getKeyword(), nullValue());
        assertThat("judge name ", actual.getJudgeName(), nullValue());
        assertThat("legal base ", actual.getLegalBase(), nullValue());
        assertThat("referenced regulation ", actual.getReferencedRegulation(), nullValue());
        assertThat("judgment's date from ", actual.getJudgmentDateFrom(), nullValue());
        assertThat("judgment's date to ", actual.getJudgmentDateTo(), nullValue());
    }

    //*** utils methods *****

    private static ISOChronology europeWarsawChronology(){
        String warsawTimeZoneId = "Europe/Warsaw";
        return ISOChronology.getInstance(DateTimeZone.forID(warsawTimeZoneId));
    }




}