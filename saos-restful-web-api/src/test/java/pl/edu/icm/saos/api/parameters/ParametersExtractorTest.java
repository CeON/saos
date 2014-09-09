package pl.edu.icm.saos.api.parameters;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import pl.edu.icm.saos.api.exceptions.WrongRequestParameterException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.nullValue;

public class ParametersExtractorTest {

    private ParametersExtractor parametersExtractor;

    private static final int MAX_LIMIT = 77;
    private static final int DEFAULT_LIMIT = 33;

    @Before
    public void setUp(){
        parametersExtractor = new ParametersExtractor();
        parametersExtractor.setMaxLimit(MAX_LIMIT);
        parametersExtractor.setDefaultLimit(DEFAULT_LIMIT);
    }


    @Test
    public void it_should_return_default_limit_value_if_zero_is_given() throws Exception {
        //given
        int zero = 0;
        int someOffset = 11;

        //when
        Pagination pagination = parametersExtractor.extractAndValidatePagination(zero, someOffset);

        //then
        assertEquals(DEFAULT_LIMIT, pagination.getLimit());
    }

    @Test
    public void it_should_return_given_offset_value() throws Exception {
        //given
        int someLimit = 1;
        int givenOffset = 11;

        //when
        Pagination pagination = parametersExtractor.extractAndValidatePagination(someLimit, givenOffset);

        //then
        assertEquals(givenOffset, pagination.getOffset());
    }

    @Test (expected = WrongRequestParameterException.class)
    public void it_should_throw_WRPException_if_offset_is_negative() throws Exception {
        //given
        int someLimit = 1;
        int negativeOffset = - 20;

        //when
        parametersExtractor.extractAndValidatePagination(someLimit, negativeOffset);
    }

    @Test (expected = WrongRequestParameterException.class)
    public void it_should_throw_WRPException_if_limit_is_greater_than_maxLimit() throws Exception {
        //given
        int bigLimit = MAX_LIMIT +1;
        int someOffset = 1;

        //when
        parametersExtractor.extractAndValidatePagination(bigLimit, someOffset);
    }

    @Test (expected = WrongRequestParameterException.class)
    public void it_should_throw_WRPException_if_limit_is_negative() throws Exception {
        //given
        int negativeLimit = -1;
        int someOffset = 10;

        //when
        parametersExtractor.extractAndValidatePagination(negativeLimit, someOffset);
    }

    @Test
    public void it_should_return_empty_values_for_blank_string() throws Exception {
        //given
        String blankValue = null;

        //when
        JoinedParameter actual = parametersExtractor.extractJoinedParameter(blankValue);

        //then
        JoinedParameter expected = new JoinedParameter("", Collections.emptyList());
        assertEquals(expected, actual);
    }

    @Test
    public void it_should_return_values_sorted_by_names() throws Exception {
        //given
        String first = "court";
        String second = "all";
        String parameterValue = first+","+second;

        //when
        JoinedParameter actual = parametersExtractor.extractJoinedParameter(parameterValue);

        //then
        JoinedParameter expected = new JoinedParameter(parameterValue, Arrays.asList(second, first));
        assertEquals(expected, actual);
    }

    @Test
    public void it_should_parse_date_in_the_format__yyyy_minus_MM_minus_DD() throws Exception{
        //given
        int year = 2014;
        int month = 9;
        int day = 5;

        //when
        LocalDate actual = parametersExtractor.extractLocalDate(year + "-" + "0"+month + "-" + "0"+day, "");

        //then
        assertEquals(new LocalDate(year, month, day), actual);
    }

    @Test(expected = WrongRequestParameterException.class)
    public void it_should_throw_WPRException_if_date_format_is_incorrect() throws Exception {
        //given
        String wrongDateFormat = 2014+"-"+8;

        //when
        parametersExtractor.extractLocalDate(wrongDateFormat, "");
    }

    @Test
    public void it_should_return_null_local_date_for_blank_value() throws Exception {
        //given
        String blankValue = "";

        //when
        LocalDate localDate = parametersExtractor.extractLocalDate(blankValue, "");

        //then
        assertThat(localDate, nullValue());
    }



}