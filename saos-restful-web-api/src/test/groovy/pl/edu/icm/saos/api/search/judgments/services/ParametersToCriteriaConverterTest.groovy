package pl.edu.icm.saos.api.search.judgments.services

import org.joda.time.LocalDate
import pl.edu.icm.saos.api.search.judgments.parameters.JudgmentsParameters
import pl.edu.icm.saos.api.search.parameters.Pagination
import pl.edu.icm.saos.search.search.model.JudgmentCriteria
import pl.edu.icm.saos.search.search.model.Paging
import pl.edu.icm.saos.search.search.model.Sorting
import spock.lang.Specification

import static pl.edu.icm.saos.search.config.model.JudgmentIndexField.ID

/**
 * @author pavtel
 */
class ParametersToCriteriaConverterTest extends Specification {

    ParametersToCriteriaConverter converter;

    void setup(){
        converter = new ParametersToCriteriaConverter();
    }


    def "it should return Paging sorted by id ascending"(){
        given:
            int pageSize = 22
            int pageNumber = 10
            def pagination = new Pagination(pageSize, pageNumber)

        when:
            Paging actual = converter.toPaging(pagination)

        then:
            def expected = new Paging(pageNumber, pageSize, new Sorting(ID.getFieldName(), Sorting.Direction.ASC))
            actual == expected
    }

    def "it should use params fields to construct criteria"(){
        given:

            def all = "some all value"
            def courtName = "some court name"
            def referencedRegulation = "some referenced regulation"
            def judgeName = "some judge name"
            def legalBases = "some legal bases"
            def keyword = "someKeyword"
            def judgmentDateFrom = new LocalDate(2020, 11, 22)
            def judgmentDateTo = new LocalDate(2030, 10, 3)

            JudgmentsParameters params = new JudgmentsParameters();
            params.setAll(all)
            params.setCourtName(courtName)
            params.setReferencedRegulation(referencedRegulation)
            params.setJudgeName(judgeName)
            params.setKeyword(keyword)
            params.setLegalBase(legalBases)
            params.setJudgmentDateFrom(judgmentDateFrom)
            params.setJudgmentDateTo(judgmentDateTo)

        when:
            JudgmentCriteria actual = converter.toCriteria(params)


        then:
            JudgmentCriteria expected = new JudgmentCriteria();
            expected.setAll(all)
            expected.setCcCourtName(courtName)
            expected.setReferencedRegulation(referencedRegulation)
            expected.setJudgeName(judgeName)
            expected.setLegalBase(legalBases)
            expected.addKeyword(keyword)
            expected.setDateFrom(judgmentDateFrom)
            expected.setDateTo(judgmentDateTo)

            actual == expected
    }


}
