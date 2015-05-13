package pl.edu.icm.saos.api.search.judgments.services
import org.joda.time.LocalDate
import pl.edu.icm.saos.api.search.judgments.parameters.JudgmentsParameters
import pl.edu.icm.saos.api.search.judgments.parameters.Sort
import pl.edu.icm.saos.api.search.parameters.Pagination
import pl.edu.icm.saos.persistence.model.CommonCourt
import pl.edu.icm.saos.persistence.model.CourtType
import pl.edu.icm.saos.persistence.model.Judgment
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment
import pl.edu.icm.saos.search.config.model.JudgmentIndexField
import pl.edu.icm.saos.search.search.model.JudgmentCriteria
import pl.edu.icm.saos.search.search.model.Paging
import pl.edu.icm.saos.search.search.model.Sorting
import spock.lang.Specification
/**
 * @author pavtel
 */
class JudgmentParametersToCriteriaConverterTest extends Specification {

    JudgmentParametersToCriteriaConverter converter;

    void setup(){
        converter = new JudgmentParametersToCriteriaConverter();
    }


    def "it should return Paging"(){
        given:
            int pageSize = 22
            int pageNumber = 10
            def pagination = new Pagination(pageSize, pageNumber)

            JudgmentIndexField field = JudgmentIndexField.JUDGMENT_DATE
            Sorting.Direction direction = Sorting.Direction.DESC
            def sort = new Sort();
            sort.setSortingField(field)
            sort.setSortingDirection(direction)

        when:
            Paging actual = converter.toPaging(pagination, sort)

        then:
            def expected = new Paging(pageNumber, pageSize, new Sorting(field.fieldName, direction))
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
            def scJudgmentForm = "some judgment form"
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
            params.setScPersonnelType(personnelType)
            params.setCourtType(courtType)

            params.setCcCourtType(commonCourtType)
            params.setCcCourtId(commonCourtId)
            params.setCcCourtCode(commonCourtCode)
            params.setCcCourtName(commonCourtName)

            params.setCcDivisionId(ccDivisionId)
            params.setCcDivisionCode(ccDivisionCode)
            params.setCcDivisionName(ccDivisionName)

            params.setScChamberId(scChamberId)
            params.setScChamberName(scChamberName)
            params.setScDivisionId(scDivisionId)
            params.setScDivisionName(scDivisionName)
            params.setScJudgmentForm(scJudgmentForm)
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
            expected.setCourtType(courtType)

            expected.setJudgmentDateFrom(judgmentDateFrom)
            expected.setJudgmentDateTo(judgmentDateTo)
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
            expected.setScJudgmentFormName(scJudgmentForm)
            expected.setJudgmentTypes(judgmentTypes)
            expected.setKeywords(keywords)

            actual == expected
    }


}
