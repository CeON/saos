package pl.edu.icm.saos.api.search.judgments.services
import org.joda.time.LocalDate
import pl.edu.icm.saos.api.search.judgments.parameters.JudgmentsParameters
import pl.edu.icm.saos.api.search.parameters.Pagination
import pl.edu.icm.saos.persistence.model.CommonCourt
import pl.edu.icm.saos.persistence.model.CourtType
import pl.edu.icm.saos.persistence.model.Judgment
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment
import pl.edu.icm.saos.search.search.model.JudgmentCriteria
import pl.edu.icm.saos.search.search.model.Paging
import pl.edu.icm.saos.search.search.model.Sorting
import spock.lang.Specification

import static pl.edu.icm.saos.search.config.model.JudgmentIndexField.DATABASE_ID
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
            def expected = new Paging(pageNumber, pageSize, new Sorting(DATABASE_ID.getFieldName(), Sorting.Direction.ASC))
            actual == expected
    }

    def "it should use params fields to construct criteria"(){
        given:

            def all = "some all value"
            def caseNumber = "013424"
            def referencedRegulation = "some referenced regulation"
            def judgeName = "some judge name"
            def legalBases = "some legal bases"
            def personnelType = SupremeCourtJudgment.PersonnelType.FIVE_PERSON
            def judgmentDateFrom = new LocalDate(2020, 11, 22)
            def judgmentDateTo = new LocalDate(2030, 10, 3)

            def courtType = CourtType.COMMON
            def commonCourtType = CommonCourt.CommonCourtType.DISTRICT
            def commonCourtId = 111
            def commonCourtCode = "1010101"
            def commonCourtName = "some court name"
            def ccDivisionId = 222
            def ccDivisionCode = "2020202"
            def ccDivisionName = "some division name"

            def scChamberId = 333
            def scChamberName = "some chamber name"
            def scDivisionId = 444
            def scDivisionName = "some sc division name"
            def judgmentTypes = [Judgment.JudgmentType.DECISION, Judgment.JudgmentType.REASONS]
            def keywords = ["someFirstKeyword", "someSecondKeyword"]

            JudgmentsParameters params = new JudgmentsParameters();
            params.setAll(all)
            params.setCaseNumber(caseNumber)
            params.setReferencedRegulation(referencedRegulation)
            params.setJudgeName(judgeName)
            params.setLegalBase(legalBases)
            params.setJudgmentDateFrom(judgmentDateFrom)
            params.setJudgmentDateTo(judgmentDateTo)
            params.setPersonnelType(personnelType)
            params.setCourtType(courtType)

            params.setCommonCourtType(commonCourtType)
            params.setCommonCourtId(commonCourtId)
            params.setCommonCourtCode(commonCourtCode)
            params.setCommonCourtName(commonCourtName)

            params.setCcDivisionId(ccDivisionId)
            params.setCcDivisionCode(ccDivisionCode)
            params.setCcDivisionName(ccDivisionName)

            params.setScChamberId(scChamberId)
            params.setScChamberName(scChamberName)
            params.setScDivisionId(scDivisionId)
            params.setScDivisionName(scDivisionName)
            params.setJudgmentTypes(judgmentTypes)
            params.setKeywords(keywords)

        when:
            JudgmentCriteria actual = converter.toCriteria(params)


        then:
            JudgmentCriteria expected = new JudgmentCriteria();
            expected.setAll(all)
            expected.setCaseNumber(caseNumber)
            expected.setReferencedRegulation(referencedRegulation)
            expected.setJudgeName(judgeName)
            expected.setLegalBase(legalBases)

            expected.setDateFrom(judgmentDateFrom)
            expected.setDateTo(judgmentDateTo)
            expected.setScPersonnelType(personnelType)

            expected.setCcCourtType(commonCourtType)
            expected.setCcCourtId(commonCourtId)
            expected.setCcCourtCode(commonCourtCode)
            expected.setCcCourtName(commonCourtName)

            expected.setCcCourtDivisionId(ccDivisionId)
            expected.setCcCourtDivisionCode(ccDivisionCode)
            expected.setCcCourtDivisionName(ccDivisionName)

            expected.setScCourtChamberId(scChamberId)
            expected.setScCourtChamberName(scChamberName)
            expected.setScCourtChamberDivisionId(scDivisionId)
            expected.setScCourtChamberDivisionName(scDivisionName)
            expected.setJudgmentType(judgmentTypes)
            expected.setKeyword(keywords)

            actual == expected
    }


}
