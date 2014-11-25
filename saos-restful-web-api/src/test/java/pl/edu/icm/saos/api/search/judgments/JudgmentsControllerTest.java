package pl.edu.icm.saos.api.search.judgments;


import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
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

import pl.edu.icm.saos.api.ApiTestConfiguration;
import pl.edu.icm.saos.api.search.judgments.services.JudgmentsApiSearchService;
import pl.edu.icm.saos.api.search.parameters.ParametersExtractor;
import pl.edu.icm.saos.api.services.FieldsDefinition.JC;
import pl.edu.icm.saos.api.support.TestPersistenceObjectsContext;
import pl.edu.icm.saos.api.support.TestPersistenceObjectsFactory;
import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.PersistenceTestSupport;
import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.search.indexing.JudgmentIndexingProcessor;
import pl.edu.icm.saos.search.search.model.CourtType;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static pl.edu.icm.saos.api.ApiConstants.*;
import static pl.edu.icm.saos.api.search.judgments.JudgmentJsonRepresentationVerifier.verifyBasicFields;
import static pl.edu.icm.saos.api.services.Constansts.*;
import static pl.edu.icm.saos.persistence.model.Judgment.JudgmentType;
import static pl.edu.icm.saos.persistence.model.SupremeCourtJudgment.PersonnelType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes =  {ApiTestConfiguration.class})
@Category(SlowTest.class)
public class JudgmentsControllerTest extends PersistenceTestSupport {

    private static final int NR_OF_JUDGMENTS_STORED_IN_DB = 2;


    private MockMvc mockMvc;


    @Autowired
    private TestPersistenceObjectsFactory testPersistenceObjectsFactory;

    private TestPersistenceObjectsContext objectsContext;


    @Autowired
    private JudgmentsListSuccessRepresentationBuilder listSuccessRepresentationBuilder;

    @Autowired
    private ParametersExtractor parametersExtractor;

    @Autowired
    private JudgmentsApiSearchService apiSearchService;

    @Before
    public void setUp() throws Exception{
        objectsContext = testPersistenceObjectsFactory.createPersistenceObjectsContext();

        clearAndIndexJudgmentsInSolr(objectsContext);


        JudgmentsController judgmentsController = new JudgmentsController();
        judgmentsController.setApiSearchService(apiSearchService);
        judgmentsController.setListSuccessRepresentationBuilder(listSuccessRepresentationBuilder);
        judgmentsController.setParametersExtractor(parametersExtractor);

        mockMvc = standaloneSetup(judgmentsController)
                .build();
    }


    @Autowired
    @Qualifier("solrJudgmentsServer")
    private SolrServer judgmentsServer;

    @Autowired
    private JudgmentIndexingProcessor judgmentIndexingProcessor;

    private void clearAndIndexJudgmentsInSolr(TestPersistenceObjectsContext objectsContext) throws Exception{
        judgmentsServer.deleteByQuery("*:*");
        judgmentsServer.commit();

        SolrInputDocument ccJudgmentDoc = judgmentIndexingProcessor.process(objectsContext.getCcJudgment());
        SolrInputDocument scJudgmentDoc = judgmentIndexingProcessor.process(objectsContext.getScJudgment());
        judgmentsServer.add(
                Arrays.asList(ccJudgmentDoc, scJudgmentDoc)
        );

        judgmentsServer.commit();
    }


    //*** END CONFIGURATION ***

    @Test
    public void showJudgments__it_should_show_all_basic_judgment_fields() throws Exception {
        //when
        ResultActions actions = mockMvc.perform(get(JUDGMENTS_PATH)
                .param(PAGE_SIZE, "2")
                .param(PAGE_NUMBER, "0")
                .accept(MediaType.APPLICATION_JSON));
        //then

        verifyBasicFields(actions, "$.items.[0]", objectsContext);

        actions.andExpect(status().isOk());

        actions
                .andExpect(jsonPath("$.items.[0].source").doesNotExist())
                .andExpect(jsonPath("$.items.[0].courtReporters").doesNotExist())
                .andExpect(jsonPath("$.items.[0].decision").doesNotExist())
                .andExpect(jsonPath("$.items.[0].summary").doesNotExist())

                .andExpect(jsonPath("$.items.[0].reasoning").doesNotExist())
                .andExpect(jsonPath("$.items.[0].legalBases").doesNotExist())
                .andExpect(jsonPath("$.items.[0].referencedRegulations").doesNotExist())


                .andExpect(jsonPath("$.items.[0].division.type").doesNotExist())


                .andExpect(jsonPath("$.items.[0].division.court.type").doesNotExist())

                .andExpect(jsonPath("$.items.[0].textContent").value(JC.TEXT_CONTENT))

                .andExpect(jsonPath("$.items.[0].division.href").value(endsWith(SINGLE_DIVISIONS_PATH + "/" + objectsContext.getFirstDivisionId())))
                .andExpect(jsonPath("$.items.[0].division.name").value(JC.DIVISION_NAME))
                .andExpect(jsonPath("$.items.[0].division.code").value(JC.DIVISION_CODE))

                .andExpect(jsonPath("$.items.[0].division.court.href").value(endsWith(SINGLE_COURTS_PATH + "/" + objectsContext.getCommonCourtId())))
                .andExpect(jsonPath("$.items.[0].division.court.name").value(JC.COURT_NAME))
                .andExpect(jsonPath("$.items.[0].division.court.code").value(JC.COURT_CODE))


                .andExpect(jsonPath("$.items.[1].href").value(endsWith("/api/judgments/" + objectsContext.getScJudgmentId())))
                .andExpect(jsonPath("$.items.[1].personnelType").value(PersonnelType.FIVE_PERSON.name()))

                .andExpect(jsonPath("$.items.[1].division.href").value(endsWith("/api/scDivisions/" + objectsContext.getScDivisionId())))
                .andExpect(jsonPath("$.items.[1].division.name").value(JC.SC_CHAMBER_DIVISION_NAME))

                .andExpect(jsonPath("$.items.[1].division.chambers.[0].href").value(endsWith("/api/scChambers/" + objectsContext.getScChamberId())))
                .andExpect(jsonPath("$.items.[1].division.chambers.[0].name").value(JC.SC_FIRST_CHAMBER_NAME))

        ;
    }



    @Test
    public void showJudgments__it_should_show_request_parameters() throws Exception {
        //given
        int pageSize = 11;
        int pageNumber = 5;
        String allValue = "someAllValue";
        String judgmentDateFrom = "2010-01-21";
        String judgmentDateTo = "2020-10-13";
        String personnelTypeValue = PersonnelType.FIVE_PERSON.name();

        String legalBaseValue = "someLegalBase";
        String referencedRegulationValue = "someReferencedRegulation";
        String judgeNameValue = "someJudgeName";
        String caseNumberValue = "someCaseNumber";
        String courtTypeValue = CourtType.ADMINISTRATIVE.name();
        String commonCourtTypeValue = CommonCourt.CommonCourtType.APPEAL.name();

        Integer commonCourtIdValue = 33;
        String commonCourtCodeValue = "someCcCode";
        String commonCourtNameValue = "someCcName";

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
                .param(PAGE_SIZE, String.valueOf(pageSize))
                .param(PAGE_NUMBER, String.valueOf(pageNumber))
                .param(ALL, allValue)
                .param(PERSONNEL_TYPE, personnelTypeValue)
                .param(LEGAL_BASE, legalBaseValue)
                .param(REFERENCED_REGULATION, referencedRegulationValue)
                .param(JUDGE_NAME, judgeNameValue)
                .param(CASE_NUMBER, caseNumberValue)
                .param(COURT_TYPE, courtTypeValue)
                .param(COMMON_COURT_TYPE, commonCourtTypeValue)
                .param(COMMON_COURT_ID, commonCourtIdValue.toString())
                .param(COMMON_COURT_CODE, commonCourtCodeValue)
                .param(COMMON_COURT_NAME, commonCourtNameValue)
                .param(CC_DIVISION_ID, ccDivisionIdValue.toString())
                .param(CC_DIVISION_CODE, ccDivisionCodeValue)
                .param(CC_DIVISION_NAME, ccDivisionNameValue)
                .param(SC_CHAMBER_ID, scChamberIdValue.toString())
                .param(SC_CHAMBER_NAME, scChamberNameValue)
                .param(SC_DIVISION_ID, scDivisionIdValue.toString())
                .param(SC_DIVISION_NAME, scDivisionNameValue)
                .param(JUDGMENT_TYPES, judgmentTypes.toArray(new String[0]))
                .param(KEYWORDS, keywords.toArray(new String[0]))
                .param(JUDGMENT_DATE_FROM, judgmentDateFrom)
                .param(JUDGMENT_DATE_TO, judgmentDateTo)
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
                .andExpect(jsonPath(prefix + ".courtType").value(courtTypeValue))

                .andExpect(jsonPath(prefix + ".commonCourtType").value(commonCourtTypeValue))
                .andExpect(jsonPath(prefix + ".commonCourtId").value(commonCourtIdValue))
                .andExpect(jsonPath(prefix + ".commonCourtCode").value(commonCourtCodeValue))
                .andExpect(jsonPath(prefix + ".commonCourtName").value(commonCourtNameValue))

                .andExpect(jsonPath(prefix + ".ccDivisionId").value(ccDivisionIdValue))
                .andExpect(jsonPath(prefix + ".ccDivisionCode").value(ccDivisionCodeValue))
                .andExpect(jsonPath(prefix + ".ccDivisionName").value(ccDivisionNameValue))

                .andExpect(jsonPath(prefix + ".scChamberId").value(scChamberIdValue))
                .andExpect(jsonPath(prefix + ".scChamberName").value(scChamberNameValue))

                .andExpect(jsonPath(prefix + ".scDivisionId").value(scDivisionIdValue))
                .andExpect(jsonPath(prefix + ".scDivisionName").value(scDivisionNameValue))

                .andExpect(jsonPath(prefix + ".judgmentTypes.[0]").value(firstType))
                .andExpect(jsonPath(prefix + ".judgmentTypes.[1]").value(secondType))

                .andExpect(jsonPath(prefix + ".keywords.[0]").value(firstKeyword))
                .andExpect(jsonPath(prefix + ".keywords.[1]").value(secondKeyword))
                .andExpect(jsonPath(prefix + ".judgmentDateFrom").value(judgmentDateFrom))
                .andExpect(jsonPath(prefix + ".judgmentDateTo").value(judgmentDateTo))
                .andExpect(jsonPath(prefix + ".pageSize").value(pageSize))
                .andExpect(jsonPath(prefix + ".pageNumber").value(pageNumber))

        ;
    }

    @Test
    public void showJudgments__it_should_show_only_pagination_parameters_if_none_is_specified() throws Exception{
        //given
        int pageSize = 11;
        int pageNumber = 5;

        //when
        ResultActions actions = mockMvc.perform(get(JUDGMENTS_PATH)
                .param(PAGE_SIZE, String.valueOf(pageSize))
                .param(PAGE_NUMBER, String.valueOf(pageNumber))
                .accept(MediaType.APPLICATION_JSON));

        //then
        actions.andExpect(status().isOk());

        String prefix = "$.queryTemplate";


        actions
                .andExpect(jsonPath(prefix + ".pageSize").value(pageSize))
                .andExpect(jsonPath(prefix + ".pageNumber").value(pageNumber))

                .andExpect(jsonPath(prefix + ".all").value(isEmptyOrNullString()))
                .andExpect(jsonPath(prefix + ".legalBase").value(isEmptyOrNullString()))
                .andExpect(jsonPath(prefix + ".referencedRegulation").value(isEmptyOrNullString()))
                .andExpect(jsonPath(prefix + ".judgeName").value(isEmptyOrNullString()))
                .andExpect(jsonPath(prefix + ".caseNumber").value(isEmptyOrNullString()))
                .andExpect(jsonPath(prefix + ".courtType").value(isEmptyOrNullString()))

                .andExpect(jsonPath(prefix + ".commonCourtType").value(isEmptyOrNullString()))
                .andExpect(jsonPath(prefix + ".commonCourtId").value(nullValue()))
                .andExpect(jsonPath(prefix + ".commonCourtCode").value(isEmptyOrNullString()))
                .andExpect(jsonPath(prefix + ".commonCourtName").value(isEmptyOrNullString()))

                .andExpect(jsonPath(prefix + ".ccDivisionId").value(nullValue()))
                .andExpect(jsonPath(prefix + ".ccDivisionCode").value(isEmptyOrNullString()))
                .andExpect(jsonPath(prefix + ".ccDivisionName").value(isEmptyOrNullString()))

                .andExpect(jsonPath(prefix + ".scChamberId").value(nullValue()))
                .andExpect(jsonPath(prefix + ".scChamberName").value(isEmptyOrNullString()))

                .andExpect(jsonPath(prefix + ".scDivisionId").value(nullValue()))
                .andExpect(jsonPath(prefix + ".scDivisionName").value(isEmptyOrNullString()))

                .andExpect(jsonPath(prefix + ".judgmentTypes").value(iterableWithSize(0)))

                .andExpect(jsonPath(prefix + ".keywords").value(iterableWithSize(0)))

                .andExpect(jsonPath(prefix + ".judgmentDateFrom").value(isEmptyOrNullString()))
                .andExpect(jsonPath(prefix + ".judgmentDateTo").value(isEmptyOrNullString()))

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
                .andExpect(jsonPath(prefix+".totalResults").value(is(NR_OF_JUDGMENTS_STORED_IN_DB)))
                ;
    }

    @Test
    public void showJudgments__it_should_not_show_next_link() throws Exception {
        //given
        int pageSize = 2;
        int pageNumber = NR_OF_JUDGMENTS_STORED_IN_DB /pageSize;

        //when
        ResultActions actions = mockMvc.perform(get(JUDGMENTS_PATH)
                .param(PAGE_SIZE, String.valueOf(pageSize))
                .param(PAGE_NUMBER, String.valueOf(pageNumber))
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
        int pageNumber = (NR_OF_JUDGMENTS_STORED_IN_DB - nrOfElementsOnTheNextPage) / pageSize - 1; // minus one as page numbers starts from 0


        //when
        ResultActions actions = mockMvc.perform(get(JUDGMENTS_PATH)
                .param(PAGE_SIZE, String.valueOf(pageSize))
                .param(PAGE_NUMBER, String.valueOf(pageNumber))
        );

        //then
        actions.andExpect(status().isOk());

        String prefix = "$.links";

        actions
                .andExpect((jsonPath(prefix + "[?(@.rel==next)].href[0]").exists()))
        ;

    }
}
