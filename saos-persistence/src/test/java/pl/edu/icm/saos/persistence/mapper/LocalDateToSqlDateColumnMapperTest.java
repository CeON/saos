package pl.edu.icm.saos.persistence.mapper;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class LocalDateToSqlDateColumnMapperTest {

    private static final int YEAR = 2020;
    private static final int MONTH = 10;
    private static final int DAY = 15;
    private static final int ZERO = 0;


    private LocalDateToSqlDateColumnMapper dateColumnMapper;


    @Before
    public void setUp(){
        dateColumnMapper = new LocalDateToSqlDateColumnMapper();
    }

    @Test
    public void toNonNullValue__it_should_return_the_same_string_representation(){
        //given
        LocalDate localDate = new LocalDate(YEAR, MONTH, DAY);

        //when
        Date date = dateColumnMapper.toNonNullValue(localDate);

        //then
        assertThat("asString ", date.toString(), is(localDate.toString()));
    }

    @Test
    public void fromNonNullString__it_should_return_correct_date_in_utc_zone(){
        //given
        String s = YEAR + "-" + MONTH + "-" +DAY;

        //when
        LocalDate localDate = dateColumnMapper.fromNonNullString(s);

        //then
        assertThat("local date ", localDate, is(new LocalDate(YEAR, MONTH, DAY)));
    }

    @Test
    public void toNonNullString__it_should_return_string_in_ISO8601_format(){
        //given
        LocalDate value = new LocalDate(YEAR, MONTH, DAY);

        //when
        String actualValue = dateColumnMapper.toNonNullString(value);

        //then
        assertThat("local date formatting ", actualValue, is(YEAR+"-"+MONTH+"-"+DAY));
    }

    @Test
    public void fromNonNullValue__it_should_return_correct_date_in_utc_zone(){
        //given
        LocalDate localDate = new LocalDate(YEAR, MONTH, DAY);
        Date date = new Date(localDate.toDateTimeAtStartOfDay(DateTimeZone.UTC).getMillis());

        //when
        LocalDate value = dateColumnMapper.fromNonNullValue(date);

        //then
        assertThat("local date value ", value, is(localDate));
    }

    @Test
    public void toNonNullValue_and_fromNonNullValue_should_be_inverse_operations(){
        //given
        LocalDate localDate = new LocalDate(YEAR, MONTH, DAY);

        //when
        Date date = dateColumnMapper.toNonNullValue(localDate);
        LocalDate actual = dateColumnMapper.fromNonNullValue(date);

        //then
        assertThat("match ", actual, is(localDate));
    }



    @Test
    public void test_for_different_default_timeZones(){
        //given
        List<String> timeZonesIDs = Arrays.asList("GMT-23:00", "GMT-8:00", "GMT", "GMT+8:00", "GMT+23:00");
        for(String timeZoneId: timeZonesIDs){
            assertTimeZones(timeZoneId);
        }
    }

    private void assertTimeZones(String timeZoneName){
        //given
        //change globally default time zone but we assume that tests run in separate JVM, java.sql.Date use default time zone
        TimeZone.setDefault(TimeZone.getTimeZone(timeZoneName));
        LocalDate localDate = new LocalDate(YEAR, MONTH, DAY);

        //when
        Date date = dateColumnMapper.toNonNullValue(localDate);
        LocalDate actualDate = dateColumnMapper.fromNonNullValue(date);

        //then
        assertThat("timeZone : " + timeZoneName, date.toString(), is(YEAR + "-" + MONTH + "-" + DAY));
        assertThat("localDate ", actualDate, is(localDate));

    }





    //****** dependent classes tests ***********



    @Test
    public void it_should_return_without_hours_minutes_seconds(){
        //given
        LocalDate localDate = new LocalDate(YEAR, MONTH, DAY);

        //when
        DateTime dateTime = localDate.toDateTimeAtStartOfDay(DateTimeZone.UTC);

        //then
        assertThat("hours should be zero", dateTime.getHourOfDay(), is(ZERO));
        assertThat("minutes should be zero", dateTime.getMinuteOfHour(), is(ZERO));
        assertThat("seconds should be zero", dateTime.getSecondOfMinute(), is(ZERO));
        assertThat("milliseconds should be zero", dateTime.getMillisOfSecond(), is(ZERO));
    }

    @Test
    public void it_should_print_correct_values(){
        //when
        LocalDate localDate = new LocalDate(YEAR, MONTH, DAY);

        //then
        assertThat("localDate format ", localDate.toString(), is(YEAR + "-" + MONTH + "-" + DAY));
    }

    @Test
    public void it_should_does_not_change_millis(){
        //given
        LocalDate localDate = new LocalDate(YEAR, MONTH, DAY);
        DateTime dateTime = localDate.toDateTimeAtStartOfDay(DateTimeZone.UTC);

        //when
        Date dateFromMillis = new Date(dateTime.getMillis());

        //then
        assertThat("millis ", dateFromMillis.getTime(), is(dateTime.getMillis()));
    }



}