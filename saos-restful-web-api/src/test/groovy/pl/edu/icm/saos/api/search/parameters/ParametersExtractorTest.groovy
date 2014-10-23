package pl.edu.icm.saos.api.search.parameters

import org.joda.time.LocalDate
import pl.edu.icm.saos.api.services.exceptions.WrongRequestParameterException
import spock.lang.Specification
/**
 * @author pavtel
 */
class ParametersExtractorTest extends Specification {

    ParametersExtractor parametersExtractor

    private static final int MAX_PAGE_SIZE = 77
    private static final int DEFAULT_PAGE_SIZE = 33

    void setup() {
        parametersExtractor = new ParametersExtractor()
        parametersExtractor.setDefaultPageSize(DEFAULT_PAGE_SIZE)
        parametersExtractor.setMaxPageSize(MAX_PAGE_SIZE)
    }

    def "it should return default page size if page size is zero"(){
        given:
            def pageSize = 0
            def somePageNumber = 10

        when:
            def actual = parametersExtractor.extractAndValidatePagination(pageSize, somePageNumber)

        then:
            actual.pageSize == DEFAULT_PAGE_SIZE
    }

    def "it shouldn't change page number value"(){
        given:
            def somePageSize = 5
            def pageNumber = 10

        when:
            def actual = parametersExtractor.extractAndValidatePagination(somePageSize, pageNumber)

        then:
            actual.pageNumber == pageNumber
    }


    def "it should throw WrongRequestParameterException if pageNumber is negative"(){
        given:
            def somePageSize = 5
            def pageNumber = -123

        when:
            parametersExtractor.extractAndValidatePagination(somePageSize, pageNumber)

        then:
            thrown(WrongRequestParameterException)
    }

    def "it should throw WrongRequestParameterException if pageSize is grater than maxPageSize"(){
        given:
            def pageSize = MAX_PAGE_SIZE + 1
            def somePageNumber = 10

        when:
            parametersExtractor.extractAndValidatePagination(pageSize, somePageNumber)

        then:
            thrown(WrongRequestParameterException)
    }

    def "it should throw WrongRequestParameterException if pageSize is negative"(){
        given:
            def pageSize = -123
            def somePageNumber = 10

        when:
            parametersExtractor.extractAndValidatePagination(pageSize, somePageNumber)

        then:
            thrown(WrongRequestParameterException)
    }

    def "it should return empty joined parameter for blank value"(){
        given:
            def blankValue = null

        when:
            def actual = parametersExtractor.extractJoinedParameter(blankValue)

        then:
            actual == new JoinedParameter("", [])
    }

    def "it should return values sorted by their names"(){
        given:
            def first = "court"
            def second = "all"
            def parameterValue = first + ","+ second

        when:
            def actual = parametersExtractor.extractJoinedParameter(parameterValue)

        then:
            actual.values == [second, first]
    }

    def "it should parse date in the format yyyy-MM-DD"(){
        given:
            def value = "2024-04-09"

        when:
            def actual = parametersExtractor.extractLocalDate(value, "some param name")

        then:
            actual == new LocalDate(2024, 4, 9)
    }

    def "it should throw WrongRequestParameterException if date format is incorrect"(){
        given:
            def incorrectDateFormat = "2024-04"

        when:
            parametersExtractor.extractLocalDate(incorrectDateFormat, "some param name")

        then:
            thrown(WrongRequestParameterException)
    }

    def "it should return null for blank input"(){
        given:
            def blankValue = null

        when:
            def actual = parametersExtractor.extractLocalDate(blankValue, "some param name")

        then:
            actual == null
    }

}
