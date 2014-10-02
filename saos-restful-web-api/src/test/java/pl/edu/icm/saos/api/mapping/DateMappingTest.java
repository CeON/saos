package pl.edu.icm.saos.api.mapping;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyString;

public class DateMappingTest {

    private static final int YEAR = 2020;
    private static final int MONTH = 1;
    private static final int DAY = 7;


    private DateMapping dateMapping;

    @Before
    public void setUp(){
        dateMapping = new DateMapping();
    }

    @Test
    public void toStringInISO8601Format_LocalDate__it_should_return_correct_value(){
        //given
        LocalDate localDate = new LocalDate(YEAR, MONTH, DAY);

        //when
        String actual = dateMapping.toStringInISO8601Format(localDate);

        //then
        assertThat(actual, is("2020-01-07"));
    }

    @Test
    public void toStringInISO8601Format_LocalDate__it_should_return_empty_value_for_null(){
        //given
        LocalDate localDate = null;

        //when
        String actual = dateMapping.toStringInISO8601Format(localDate);

        //then
        assertThat(actual, isEmptyString());
    }

    @Test
    public void toStringInISO8601Format_DateTime__it_should_return_correct_value(){
        //given
        DateTime dateTime = new DateTime(YEAR, MONTH, DAY, 10, 20, 30, 400);

        //when
        String actual = dateMapping.toStringInISO8601Format(dateTime);

        //then
        assertThat(actual, is("2020-01-07"));
    }

    @Test
    public void toStringInISO8601Format_DateTime__it_should_return_empty_value_for_null(){
        //given
        DateTime dateTime = null;

        //when
        String actual = dateMapping.toStringInISO8601Format(dateTime);

        //then
        assertThat(actual, isEmptyString());
    }

}