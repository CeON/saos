package pl.edu.icm.saos.api.search.parameters

import spock.lang.Specification
import spock.lang.Unroll

/**
 * @author pavtel
 */
class PaginationTest extends Specification {


    def "to calculate next page it should increment pageNumber by one"(){
        given:
            def pageNumber = 3
            def pageSize = 5
            def pagination = new Pagination(pageSize, pageNumber)

        when:
            def actual = pagination.next

        then:
            actual == new Pagination(pageSize, pageNumber+1)
    }

    def "to calculate previous page it should decrement pageNumber by one"(){
        given:
            def pageNumber = 3
            def pageSize = 5
            def pagination = new Pagination(pageSize, pageNumber)

        when:
            def actual = pagination.previous

        then:
            actual == new Pagination(pageSize, pageNumber-1)
    }

    def "previous page for pageNumber=0 should be the same page"(){
        given:
            def pageNumber = 0
            def pageSize = 5
            def pagination = new Pagination(pageSize, pageNumber)

        when:
            def actual = pagination.previous

        then:
            actual == pagination
    }

    @Unroll
    def "it should have next page for pageSize='#pageSize' pageNumber='#pageNumber', allElementsCount='#allElementsCount'"(){
        given:
            def pagination = new Pagination(pageSize, pageNumber)

        when:
            def hasNext = pagination.hasNextIn(allElementsCount)

        then:
            hasNext

        where:
            pageSize | pageNumber | allElementsCount
            5        | 10         | (10+1) * 5 + 1
            5        | 10         | 100

    }

    @Unroll
    def "it shouldn't have next page for pageSize='#pageSize' pageNumber='#pageNumber', allElementsCount='#allElementsCount'"(){
        given:
            def pagination = new Pagination(pageSize, pageNumber)

        when:
            def hasNext = pagination.hasNextIn(allElementsCount)

        then:
            !hasNext

        where:
            pageSize | pageNumber | allElementsCount
            5        | 10         | (10+1)*5
            5        | 10         | 33
    }

}
