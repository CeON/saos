package pl.edu.icm.saos.api.search.judgments;


import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static pl.edu.icm.saos.api.services.Constants.JUDGMENTS_PATH;
import static pl.edu.icm.saos.api.services.Constants.SINGLE_COURTS_PATH;
import static pl.edu.icm.saos.api.services.Constants.SINGLE_DIVISIONS_PATH;
import static pl.edu.icm.saos.api.services.Constants.SINGLE_JUDGMENTS_PATH;
import static pl.edu.icm.saos.common.testcommon.IntToLongMatcher.equalsLong;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CASE_NUMBER;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_COURT_CODE;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_COURT_NAME;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_DATE_DAY;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_DATE_MONTH;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_DATE_YEAR;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_FIRST_DIVISION_CODE;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_FIRST_DIVISION_NAME;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_FIRST_JUDGE_NAME;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_FIRST_JUDGE_ROLE;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_JUDGMENT_TYPE;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_SECOND_JUDGE_NAME;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_TEXT_CONTENT;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_THIRD_JUDGE_NAME;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.SC_DATE_DAY;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.SC_DATE_MONTH;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.SC_DATE_YEAR;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.SC_FIRST_CHAMBER_NAME;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.SC_FIRST_DIVISION_NAME;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.SC_PERSONNEL_TYPE;

import java.util.Arrays;
import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import pl.edu.icm.saos.api.ApiConstants;
import pl.edu.icm.saos.api.ApiTestConfiguration;
import pl.edu.icm.saos.api.search.judgments.services.JudgmentsApiSearchService;
import pl.edu.icm.saos.api.search.parameters.ParametersExtractor;
import pl.edu.icm.saos.api.services.interceptor.RestrictParamsHandlerInterceptor;
import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.PersistenceTestSupport;
import pl.edu.icm.saos.persistence.common.TestObjectContext;
import pl.edu.icm.saos.persistence.common.TestPersistenceObjectFactory;
import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.CourtType;
import pl.edu.icm.saos.persistence.model.Judgment.JudgmentType;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment.PersonnelType;
import pl.edu.icm.saos.search.config.model.JudgmentIndexField;
import pl.edu.icm.saos.search.indexing.JudgmentIndexingProcessor;
import pl.edu.icm.saos.search.search.model.Sorting;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes =  {ApiTestConfiguration.class})
@Category(SlowTest.class)
public class JudgmentsControllerTest extends PersistenceTestSupport {

    private static final int NR_OF_JUDGMENTS_STORED_IN_SOLR_INDEX = 2;


    private MockMvc mockMvc;



    @Autowired
    private JudgmentsListSuccessRepresentationBuilder listSuccessRepresentationBuilder;

    @Autowired
    private ParametersExtractor parametersExtractor;

    @Autowired
    private JudgmentsApiSearchService apiSearchService;

    @Autowired
    private TestPersistenceObjectFactory testPersistenceObjectFactory;

    private TestObjectContext testObjectContext;

    @Before
    public void setUp() throws Exception{
        testObjectContext = testPersistenceObjectFactory.createTestObjectContext();

        clearAndIndexJudgmentsInSolr(testObjectContext);


        JudgmentsController judgmentsController = new JudgmentsController();
        judgmentsController.setApiSearchService(apiSearchService);
        judgmentsController.setListSuccessRepresentationBuilder(listSuccessRepresentationBuilder);
        judgmentsController.setParametersExtractor(parametersExtractor);

        mockMvc = standaloneSetup(judgmentsController)
                .addInterceptors(new RestrictParamsHandlerInterceptor())
                .build();
    }


    @Autowired
    @Qualifier("solrJudgmentsServer")
    private SolrServer judgmentsServer;

    @Autowired
    private JudgmentIndexingProcessor judgmentIndexingProcessor;

    private void clearAndIndexJudgmentsInSolr(TestObjectContext testObjectContext) throws Exception{
        judgmentsServer.deleteByQuery("*:*");
        judgmentsServer.commit();

        SolrInputDocument ccJudgmentDoc = judgmentIndexingProcessor.process(testObjectContext.getCcJudgment());
        SolrInputDocument scJudgmentDoc = judgmentIndexingProcessor.process(testObjectContext.getScJudgment());
        judgmentsServer.add(
                Arrays.asList(ccJudgmentDoc, scJudgmentDoc)
        );

        judgmentsServer.commit();
    }


    //------------------------ TESTS --------------------------

    @Test
    public void showJudgments__it_should_show_all_basic_judgment_fields() throws Exception {
        //when
        ResultActions actions = mockMvc.perform(get(JUDGMENTS_PATH)
                .param(ApiConstants.PAGE_SIZE, "2")
                .param(ApiConstants.PAGE_NUMBER, "0")
                .accept(MediaType.APPLICATION_JSON));
        //then


        actions.andExpect(status().isOk());


        actions
                .andExpect(jsonPath("$.items.[0].href").value(endsWith(SINGLE_JUDGMENTS_PATH + "/" + testObjectContext.getCcJudgmentId())))
                .andExpect(jsonPath("$.items.[0].courtCases").value(iterableWithSize(1)))
                .andExpect(jsonPath("$.items.[0].courtCases.[0].caseNumber").value(CASE_NUMBER))
                .andExpect(jsonPath("$.items.[0].judgmentType").value(CC_JUDGMENT_TYPE.name()))

                .andExpect(jsonPath("$.items.[0].judgmentDate").value(CC_DATE_YEAR + "-" + CC_DATE_MONTH + "-" + CC_DATE_DAY))

                .andExpect(jsonPath("$.items.[0].judges").isArray())
                .andExpect(jsonPath("$.items.[0].judges").value(iterableWithSize(3)))
                .andExpect(jsonPath("$.items.[0].judges.[0].name").value(CC_FIRST_JUDGE_NAME))
                .andExpect(jsonPath("$.items.[0].judges.[0].specialRoles").value(iterableWithSize(1)))
                .andExpect(jsonPath("$.items.[0].judges.[0].specialRoles.[0]").value(CC_FIRST_JUDGE_ROLE.name()))
                .andExpect(jsonPath("$.items.[0].judges.[1].name").value(CC_SECOND_JUDGE_NAME))
                .andExpect(jsonPath("$.items.[0].judges.[1].specialRoles").value(emptyIterable()))
                .andExpect(jsonPath("$.items.[0].judges.[2].name").value(CC_THIRD_JUDGE_NAME))
                .andExpect(jsonPath("$.items.[0].judges.[2].specialRoles").value(emptyIterable()))


                .andExpect(jsonPath("$.items.[0].source").doesNotExist())
                .andExpect(jsonPath("$.items.[0].courtReporters").doesNotExist())
                .andExpect(jsonPath("$.items.[0].decision").doesNotExist())
                .andExpect(jsonPath("$.items.[0].summary").doesNotExist())

                .andExpect(jsonPath("$.items.[0].reasoning").doesNotExist())
                .andExpect(jsonPath("$.items.[0].legalBases").doesNotExist())
                .andExpect(jsonPath("$.items.[0].referencedRegulations").doesNotExist())


                .andExpect(jsonPath("$.items.[0].division.type").doesNotExist())

                .andExpect(jsonPath("$.items.[0].division.court.type").doesNotExist())

                .andExpect(jsonPath("$.items.[0].textContent").value(CC_TEXT_CONTENT))

                .andExpect(jsonPath("$.items.[0].division.href").value(endsWith(SINGLE_DIVISIONS_PATH + "/" + testObjectContext.getCcFirstDivisionId())))
                .andExpect(jsonPath("$.items.[0].division.id").value(equalsLong(testObjectContext.getCcFirstDivisionId())))
                .andExpect(jsonPath("$.items.[0].division.name").value(CC_FIRST_DIVISION_NAME))
                .andExpect(jsonPath("$.items.[0].division.code").value(CC_FIRST_DIVISION_CODE))

                .andExpect(jsonPath("$.items.[0].division.court.href").value(endsWith(SINGLE_COURTS_PATH + "/" + testObjectContext.getCcCourtId())))
                .andExpect(jsonPath("$.items.[0].division.court.id").value(equalsLong(testObjectContext.getCcCourtId())))
                .andExpect(jsonPath("$.items.[0].division.court.name").value(CC_COURT_NAME))
                .andExpect(jsonPath("$.items.[0].division.court.code").value(CC_COURT_CODE))


                .andExpect(jsonPath("$.items.[1].href").value(endsWith("/api/judgments/" + testObjectContext.getScJudgmentId())))
                .andExpect(jsonPath("$.items.[1].personnelType").value(SC_PERSONNEL_TYPE.name()))

                .andExpect(jsonPath("$.items.[1].division.href").value(endsWith("/api/scDivisions/" + testObjectContext.getScFirstDivisionId())))
                .andExpect(jsonPath("$.items.[1].division.id").value(equalsLong(testObjectContext.getScFirstDivisionId())))
                .andExpect(jsonPath("$.items.[1].division.name").value(SC_FIRST_DIVISION_NAME))

                .andExpect(jsonPath("$.items.[1].division.chambers.[0].href").value(endsWith("/api/scChambers/" + testObjectContext.getScChamberId())))
                .andExpect(jsonPath("$.items.[1].division.chambers.[0].id").value(equalsLong(testObjectContext.getScChamberId())))
                .andExpect(jsonPath("$.items.[1].division.chambers.[0].name").value(SC_FIRST_CHAMBER_NAME))

        ;

    }


    @Test
    public void showJudgments__it_should_sort_by_judgment_date_descending() throws Exception{
        //when
        ResultActions actions = mockMvc.perform(get(JUDGMENTS_PATH)
                .param(ApiConstants.PAGE_SIZE, "1")
                .param(ApiConstants.PAGE_NUMBER, "0")
                .param("sortingField", JudgmentIndexField.JUDGMENT_DATE.name())
                .param("sortingDirection", Sorting.Direction.DESC.name())
                .accept(MediaType.APPLICATION_JSON));

        //then
        LocalDate ccJudgmentDate = new LocalDate(CC_DATE_YEAR, CC_DATE_MONTH, CC_DATE_DAY);
        LocalDate scJudgmentDate = new LocalDate(SC_DATE_YEAR, SC_DATE_MONTH, SC_DATE_DAY);

        if(ccJudgmentDate.isAfter(scJudgmentDate)){
            actions
                    .andExpect(jsonPath("$.items.[0].href").value(endsWith(SINGLE_JUDGMENTS_PATH + "/" + testObjectContext.getCcJudgmentId())));
        } else {
            actions
                    .andExpect(jsonPath("$.items.[0].href").value(endsWith(SINGLE_JUDGMENTS_PATH + "/" + testObjectContext.getScJudgmentId())));
        }

    }

    @Test
    public void showJudgments__it_should_sort_by_judgment_date_ascending() throws Exception{
        //when
        ResultActions actions = mockMvc.perform(get(JUDGMENTS_PATH)
                .param(ApiConstants.PAGE_SIZE, "1")
                .param(ApiConstants.PAGE_NUMBER, "0")
                .param("sortingField", JudgmentIndexField.JUDGMENT_DATE.name())
                .param("sortingDirection", Sorting.Direction.ASC.name())
                .accept(MediaType.APPLICATION_JSON));

        //then
        LocalDate ccJudgmentDate = new LocalDate(CC_DATE_YEAR, CC_DATE_MONTH, CC_DATE_DAY);
        LocalDate scJudgmentDate = new LocalDate(SC_DATE_YEAR, SC_DATE_MONTH, SC_DATE_DAY);

        if(ccJudgmentDate.isAfter(scJudgmentDate)){
            actions
                    .andExpect(jsonPath("$.items.[0].href").value(endsWith(SINGLE_JUDGMENTS_PATH + "/" + testObjectContext.getScJudgmentId())));
        } else {
            actions
                    .andExpect(jsonPath("$.items.[0].href").value(endsWith(SINGLE_JUDGMENTS_PATH + "/" + testObjectContext.getCcJudgmentId())));
        }

    }



    @Test
    public void showJudgments__it_should_show_request_parameters() throws Exception {
        //given
        int pageSize = 11;
        int pageNumber = 5;

        String sortingFieldValue = JudgmentIndexField.JUDGMENT_DATE.name();
        String sortingDirectionValue = Sorting.Direction.ASC.name();

        String allValue = "someAllValue";
        String judgmentDateFrom = "2010-01-21";
        String judgmentDateTo = "2020-10-13";
        String personnelTypeValue = PersonnelType.FIVE_PERSON.name();

        String legalBaseValue = "someLegalBase";
        String referencedRegulationValue = "someReferencedRegulation";
        String judgeNameValue = "someJudgeName";
        String caseNumberValue = "someCaseNumber";
        String courtTypeValue = CourtType.ADMINISTRATIVE.name();
        String ccCourtTypeValue = CommonCourt.CommonCourtType.APPEAL.name();

        Integer ccCourtIdValue = 33;
        String ccCourtCodeValue = "someCcCode";
        String ccCourtNameValue = "someCcName";

        Integer ccDivisionIdValue = 44;
        String ccDivisionCodeValue = "someCcDivisionCode";
        String ccDivisionNameValue = "someCcDivisionName";

        Integer scChamberIdValue = 55;
        String scChamberNameValue = "someScChamberName";
        Integer scDivisionIdValue = 66;
        String scDivisionNameValue = "someScDivisionName";

        String firstType = JudgmentType.DECISION.name();
        String secondType = JudgmentType.REGULATION.name();
        List<String> judgmentTypes = Arrays.asList(firstType, secondType);

        String firstKeyword="someFirstKeyword";
        String secondKeyword="someSecondKeyword";
        List<String> keywords = Arrays.asList(firstKeyword, secondKeyword);

        //when
        ResultActions actions = mockMvc.perform(get(JUDGMENTS_PATH)
                .param(ApiConstants.PAGE_SIZE, String.valueOf(pageSize))
                .param(ApiConstants.PAGE_NUMBER, String.valueOf(pageNumber))
                .param(ApiConstants.SORTING_FIELD, sortingFieldValue)
                .param(ApiConstants.SORTING_DIRECTION, sortingDirectionValue)
                .param(ApiConstants.ALL, allValue)
                .param(ApiConstants.SC_PERSONNEL_TYPE, personnelTypeValue)
                .param(ApiConstants.LEGAL_BASE, legalBaseValue)
                .param(ApiConstants.REFERENCED_REGULATION, referencedRegulationValue)
                .param(ApiConstants.JUDGE_NAME, judgeNameValue)
                .param(ApiConstants.CASE_NUMBER, caseNumberValue)
                .param(ApiConstants.COURT_TYPE, courtTypeValue)
                .param(ApiConstants.CC_COURT_TYPE, ccCourtTypeValue)
                .param(ApiConstants.CC_COURT_ID, ccCourtIdValue.toString())
                .param(ApiConstants.CC_COURT_CODE, ccCourtCodeValue)
                .param(ApiConstants.CC_COURT_NAME, ccCourtNameValue)
                .param(ApiConstants.CC_DIVISION_ID, ccDivisionIdValue.toString())
                .param(ApiConstants.CC_DIVISION_CODE, ccDivisionCodeValue)
                .param(ApiConstants.CC_DIVISION_NAME, ccDivisionNameValue)
                .param(ApiConstants.SC_CHAMBER_ID, scChamberIdValue.toString())
                .param(ApiConstants.SC_CHAMBER_NAME, scChamberNameValue)
                .param(ApiConstants.SC_DIVISION_ID, scDivisionIdValue.toString())
                .param(ApiConstants.SC_DIVISION_NAME, scDivisionNameValue)
                .param(ApiConstants.JUDGMENT_TYPES, judgmentTypes.toArray(new String[0]))
                .param(ApiConstants.KEYWORDS, keywords.toArray(new String[0]))
                .param(ApiConstants.JUDGMENT_DATE_FROM, judgmentDateFrom)
                .param(ApiConstants.JUDGMENT_DATE_TO, judgmentDateTo)
                .accept(MediaType.APPLICATION_JSON));

        //then
        actions.andExpect(status().isOk());

        String prefix = "$.queryTemplate";

        actions
                .andExpect(jsonPath(prefix + ".all").value(allValue))
                .andExpect(jsonPath(prefix + ".legalBase").value(legalBaseValue))
                .andExpect(jsonPath(prefix + ".referencedRegulation").value(referencedRegulationValue))
                .andExpect(jsonPath(prefix + ".judgeName").value(judgeNameValue))
                .andExpect(jsonPath(prefix + ".caseNumber").value(caseNumberValue))
                .andExpect(jsonPath(prefix + ".courtType.value").value(courtTypeValue))

                .andExpect(jsonPath(prefix + ".ccCourtType.value").value(ccCourtTypeValue))
                .andExpect(jsonPath(prefix + ".ccCourtId").value(ccCourtIdValue))
                .andExpect(jsonPath(prefix + ".ccCourtCode").value(ccCourtCodeValue))
                .andExpect(jsonPath(prefix + ".ccCourtName").value(ccCourtNameValue))

                .andExpect(jsonPath(prefix + ".ccDivisionId").value(ccDivisionIdValue))
                .andExpect(jsonPath(prefix + ".ccDivisionCode").value(ccDivisionCodeValue))
                .andExpect(jsonPath(prefix + ".ccDivisionName").value(ccDivisionNameValue))

                .andExpect(jsonPath(prefix + ".scChamberId").value(scChamberIdValue))
                .andExpect(jsonPath(prefix + ".scChamberName").value(scChamberNameValue))

                .andExpect(jsonPath(prefix + ".scDivisionId").value(scDivisionIdValue))
                .andExpect(jsonPath(prefix + ".scDivisionName").value(scDivisionNameValue))

                .andExpect(jsonPath(prefix + ".judgmentTypes.value.[0]").value(firstType))
                .andExpect(jsonPath(prefix + ".judgmentTypes.value.[1]").value(secondType))

                .andExpect(jsonPath(prefix + ".keywords.[0]").value(firstKeyword))
                .andExpect(jsonPath(prefix + ".keywords.[1]").value(secondKeyword))
                .andExpect(jsonPath(prefix + ".judgmentDateFrom.value").value(judgmentDateFrom))
                .andExpect(jsonPath(prefix + ".judgmentDateTo.value").value(judgmentDateTo))

                .andExpect(jsonPath(prefix + ".pageSize.value").value(pageSize))
                .andExpect(jsonPath(prefix + ".pageNumber.value").value(pageNumber))

                .andExpect(jsonPath(prefix + ".sortingField.value").value(sortingFieldValue))
                .andExpect(jsonPath(prefix + ".sortingDirection.value").value(sortingDirectionValue))
        ;
    }

    @Test
    public void showJudgments__it_should_show_only_pagination_and_sort_parameters_if_none_is_specified() throws Exception{
        //given
        int pageSize = 11;
        int pageNumber = 5;

        //when
        ResultActions actions = mockMvc.perform(get(JUDGMENTS_PATH)
                .param(ApiConstants.PAGE_SIZE, String.valueOf(pageSize))
                .param(ApiConstants.PAGE_NUMBER, String.valueOf(pageNumber))
                .accept(MediaType.APPLICATION_JSON));

        //then
        actions.andExpect(status().isOk());

        String prefix = "$.queryTemplate";

        actions

                .andExpect(jsonPath(prefix + ".pageSize.value").value(pageSize))
                .andExpect(jsonPath(prefix + ".pageNumber.value").value(pageNumber))

                .andExpect(jsonPath(prefix + ".all").value(isEmptyOrNullString()))
                .andExpect(jsonPath(prefix + ".legalBase").value(isEmptyOrNullString()))
                .andExpect(jsonPath(prefix + ".referencedRegulation").value(isEmptyOrNullString()))
                .andExpect(jsonPath(prefix + ".judgeName").value(isEmptyOrNullString()))
                .andExpect(jsonPath(prefix + ".caseNumber").value(isEmptyOrNullString()))
                .andExpect(jsonPath(prefix + ".courtType.value").value(isEmptyOrNullString()))

                .andExpect(jsonPath(prefix + ".ccCourtType.value").value(isEmptyOrNullString()))
                .andExpect(jsonPath(prefix + ".ccCourtId").value(nullValue()))
                .andExpect(jsonPath(prefix + ".ccCourtCode").value(isEmptyOrNullString()))
                .andExpect(jsonPath(prefix + ".ccCourtName").value(isEmptyOrNullString()))

                .andExpect(jsonPath(prefix + ".ccDivisionId").value(nullValue()))
                .andExpect(jsonPath(prefix + ".ccDivisionCode").value(isEmptyOrNullString()))
                .andExpect(jsonPath(prefix + ".ccDivisionName").value(isEmptyOrNullString()))

                .andExpect(jsonPath(prefix + ".scChamberId").value(nullValue()))
                .andExpect(jsonPath(prefix + ".scChamberName").value(isEmptyOrNullString()))

                .andExpect(jsonPath(prefix + ".scDivisionId").value(nullValue()))
                .andExpect(jsonPath(prefix + ".scDivisionName").value(isEmptyOrNullString()))

                .andExpect(jsonPath(prefix + ".judgmentTypes.value").value(iterableWithSize(0)))

                .andExpect(jsonPath(prefix + ".keywords").value(iterableWithSize(0)))
                .andExpect(jsonPath(prefix + ".judgmentDateFrom.value").value(isEmptyOrNullString()))
                .andExpect(jsonPath(prefix + ".judgmentDateTo.value").value(isEmptyOrNullString()))

                .andExpect(jsonPath(prefix + ".sortingField.value").value(JudgmentIndexField.DATABASE_ID.name()))
                .andExpect(jsonPath(prefix + ".sortingDirection.value").value(Sorting.Direction.ASC.name()))
        ;

    }

    @Test
    public void showJudgments__it_should_show_info() throws Exception {

        //when
        ResultActions actions = mockMvc.perform(get(JUDGMENTS_PATH)
                .accept(MediaType.APPLICATION_JSON)
        );

        //then
        actions.andExpect(status().isOk());

        String prefix = "$.info";

        actions
                .andExpect(jsonPath(prefix+".totalResults").value(is(NR_OF_JUDGMENTS_STORED_IN_SOLR_INDEX)))
                ;
    }

    @Test
    public void showJudgments__it_should_not_show_next_link() throws Exception {
        //given
        int pageSize = 2;
        int pageNumber = NR_OF_JUDGMENTS_STORED_IN_SOLR_INDEX /pageSize;

        //when
        ResultActions actions = mockMvc.perform(get(JUDGMENTS_PATH)
                .param(ApiConstants.PAGE_SIZE, String.valueOf(pageSize))
                .param(ApiConstants.PAGE_NUMBER, String.valueOf(pageNumber))
                .accept(MediaType.APPLICATION_JSON)
        );

        //then
        actions.andExpect(status().isOk());

        String prefix = "$.links";

        actions
                .andExpect(jsonPath(prefix + "[?(@.rel==next)].href[0]").doesNotExist())
                ;
    }

    @Test
    public void showJudgments__it_should_show_next_link() throws Exception {
        //given
        int pageSize = 1;
        int nrOfElementsOnTheNextPage = 1;
        int pageNumber = (NR_OF_JUDGMENTS_STORED_IN_SOLR_INDEX - nrOfElementsOnTheNextPage) / pageSize - 1; // minus one as page numbers starts from 0


        //when
        ResultActions actions = mockMvc.perform(get(JUDGMENTS_PATH)
                .param(ApiConstants.PAGE_SIZE, String.valueOf(pageSize))
                .param(ApiConstants.PAGE_NUMBER, String.valueOf(pageNumber))
        );

        //then
        actions.andExpect(status().isOk());

        String prefix = "$.links";

        actions
                .andExpect((jsonPath(prefix + "[?(@.rel==next)].href[0]").exists()))
        ;

    }

    @Test
    public void showJudgments__it_should_not_allow_incorrect_request_parameter_name() throws Exception {
        //when
        ResultActions actions = mockMvc.perform(get(JUDGMENTS_PATH)
                .param("some_incorrect_parameter_name", "")
                .accept(MediaType.APPLICATION_JSON));

        //then
        actions.andExpect(status().isBadRequest());
    }
}
