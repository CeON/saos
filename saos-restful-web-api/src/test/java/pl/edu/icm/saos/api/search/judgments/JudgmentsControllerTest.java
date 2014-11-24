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
import pl.edu.icm.saos.api.config.ApiTestConfiguration;
import pl.edu.icm.saos.api.search.judgments.services.JudgmentsApiSearchService;
import pl.edu.icm.saos.api.search.parameters.ParametersExtractor;
import pl.edu.icm.saos.api.services.FieldsDefinition.JC;
import pl.edu.icm.saos.api.support.TestPersistenceObjectsContext;
import pl.edu.icm.saos.api.support.TestPersistenceObjectsFactory;
import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.PersistenceTestSupport;
import pl.edu.icm.saos.search.SearchTestConfiguration;
import pl.edu.icm.saos.search.indexing.JudgmentIndexingProcessor;

import java.util.Arrays;

import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static pl.edu.icm.saos.api.ApiConstants.*;
import static pl.edu.icm.saos.api.search.judgments.JudgmentJsonRepresentationVerifier.verifyBasicFields;
import static pl.edu.icm.saos.api.services.Constansts.*;
import static pl.edu.icm.saos.persistence.model.SupremeCourtJudgment.PersonnelType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes =  {ApiTestConfiguration.class, SearchTestConfiguration.class})
@Category(SlowTest.class)
public class JudgmentsControllerTest extends PersistenceTestSupport {

    private static final int NR_OF_JUDGMENTS_STORED_IN_DBS = 2;


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

        setUpSolrRelatedServices(objectsContext);


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

    private void setUpSolrRelatedServices(TestPersistenceObjectsContext objectsContext) throws Exception{
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
    public void showJudgments__it_should_show_all_basics_judgments_fields() throws Exception {
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
        String personnelType = PersonnelType.FIVE_PERSON.name();

        //when
        ResultActions actions = mockMvc.perform(get(JUDGMENTS_PATH)
                .param(PAGE_SIZE, String.valueOf(pageSize))
                .param(PAGE_NUMBER, String.valueOf(pageNumber))
                .param(ALL, allValue)
                .param(COURT_NAME, JC.COURT_NAME)
                .param(LEGAL_BASE, JC.FIRST_LEGAL_BASE)
                .param(REFERENCED_REGULATION, JC.FIRST_REFERENCED_REGULATION_TEXT)
                .param(JUDGE_NAME, JC.SECOND_JUDGE_NAME)
                .param(KEYWORD, JC.SECOND_KEYWORD)
                .param(JUDGMENT_DATE_FROM, judgmentDateFrom)
                .param(JUDGMENT_DATE_TO, judgmentDateTo)
                .param(PERSONNEL_TYPE, personnelType)
                .accept(MediaType.APPLICATION_JSON));

        //then
        actions.andExpect(status().isOk());

        String prefix = "$.queryTemplate";

        actions
                .andExpect(jsonPath(prefix+".all").value(allValue))
                .andExpect(jsonPath(prefix+".courtName").value(JC.COURT_NAME))
                .andExpect(jsonPath(prefix+".legalBase").value(JC.FIRST_LEGAL_BASE))
                .andExpect(jsonPath(prefix+".referencedRegulation").value(JC.FIRST_REFERENCED_REGULATION_TEXT))
                .andExpect(jsonPath(prefix+".judgeName").value(JC.SECOND_JUDGE_NAME))
                .andExpect(jsonPath(prefix+".keyword").value(JC.SECOND_KEYWORD))
                .andExpect(jsonPath(prefix+".personnelType").value(personnelType))
                .andExpect(jsonPath(prefix+".judgmentDateFrom").value(judgmentDateFrom))
                .andExpect(jsonPath(prefix+".judgmentDateTo").value(judgmentDateTo))
                .andExpect(jsonPath(prefix+".pageSize").value(pageSize))
                .andExpect(jsonPath(prefix+".pageNumber").value(pageNumber))

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


        String EMPTY_STRING = "";

        actions
                .andExpect(jsonPath(prefix + ".pageSize").value(pageSize))
                .andExpect(jsonPath(prefix + ".pageNumber").value(pageNumber))

                .andExpect(jsonPath(prefix+".all").value(EMPTY_STRING))
                .andExpect(jsonPath(prefix+".courtName").value(EMPTY_STRING))
                .andExpect(jsonPath(prefix+".legalBase").value(EMPTY_STRING))
                .andExpect(jsonPath(prefix+".referencedRegulation").value(EMPTY_STRING))
                .andExpect(jsonPath(prefix+".judgeName").value(EMPTY_STRING))
                .andExpect(jsonPath(prefix + ".keyword").value(EMPTY_STRING))
                .andExpect(jsonPath(prefix+".judgmentDateFrom").value(EMPTY_STRING))
                .andExpect(jsonPath(prefix + ".judgmentDateTo").value(EMPTY_STRING))

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
                .andExpect(jsonPath(prefix+".totalResults").value(is(NR_OF_JUDGMENTS_STORED_IN_DBS)))
                ;
    }

    @Test
    public void showJudgments__it_should_not_show_next_link() throws Exception {
        //given
        int pageSize = 2;
        int pageNumber = NR_OF_JUDGMENTS_STORED_IN_DBS /pageSize;

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
        int pageNumber = (NR_OF_JUDGMENTS_STORED_IN_DBS - nrOfElementsOnTheNextPage) / pageSize - 1; // minus one as page numbers starts from 0


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
