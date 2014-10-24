package pl.edu.icm.saos.api.services.mapping

import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.LocalDate
import pl.edu.icm.saos.api.services.dates.DateMapping
import spock.lang.Specification
import spock.lang.Unroll

/**
 * @author pavtel
 */
class DateMappingTest extends Specification {

    DateMapping dateMapping

    void setup() {
        dateMapping = new DateMapping()
    }

    @Unroll
    def "should return 'yyyy-MM-dd'T'HH:mm:ss.SSS' format '#expectedValue' for DateTime '#dateTime'"(){
        when:
            def result = dateMapping.toStringWithZoneUTC dateTime

        then:
            result == expectedValue

        where:
            dateTime                                                    || expectedValue
            new DateTime(2014, 1, 3, 11, 22, 33, 404, DateTimeZone.UTC) || "2014-01-03T11:22:33.404"
            null                                                        || ""
    }

    @Unroll
    def "should return ISO8601 format '#expectedValue' for LocalDate '#localDate'"(){
        when:
            def result = dateMapping.toISO8601Format localDate

        then:
            result == expectedValue

        where:
            localDate                                    || expectedValue
            new LocalDate(2020, 10, 3)                   || "2020-10-03"
            null                                         || ""
    }

    @Unroll
    def "should return ISO8601 format '#expectedValue' for DateTime '#dateTime'"(){
        when:
            def result = dateMapping.toISO8601Format dateTime

        then:
            result == expectedValue

        where:
        dateTime                                  || expectedValue
        new DateTime(2014, 1, 3, 11, 22, 33, 404) || "2014-01-03"
        null                                      || ""
    }

    //this test only for shoving spock
    @Unroll
    def "should return ISO8601 format  for LocalDate   - extra test"(){
        given:
            LocalDate localDate = new LocalDate(2020, 10, 3);


        when:
            def result = dateMapping.toISO8601Format(localDate)

        then:
            result == "2020-10-03"
    }

}
