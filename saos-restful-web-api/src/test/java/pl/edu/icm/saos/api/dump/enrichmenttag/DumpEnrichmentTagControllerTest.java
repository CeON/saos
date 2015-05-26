package pl.edu.icm.saos.api.dump.enrichmenttag;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static pl.edu.icm.saos.api.ApiConstants.PAGE_NUMBER;
import static pl.edu.icm.saos.api.ApiConstants.PAGE_SIZE;
import static pl.edu.icm.saos.api.ApiResponseAssertUtils.assertError;
import static pl.edu.icm.saos.api.ApiResponseAssertUtils.assertIncorrectParamNameError;
import static pl.edu.icm.saos.api.ApiResponseAssertUtils.assertNegativePageNumberError;
import static pl.edu.icm.saos.api.ApiResponseAssertUtils.assertOk;
import static pl.edu.icm.saos.api.ApiResponseAssertUtils.assertTooBigPageSizeError;
import static pl.edu.icm.saos.api.ApiResponseAssertUtils.assertTooSmallPageSizeError;
import static pl.edu.icm.saos.common.testcommon.IntToLongMatcher.equalsLong;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.FIRST_ENRICHMENT_TAG_TYPE;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.FIRST_ENRICHMENT_TAG_VALUE_VALUE;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.REFERENCED_COURT_CASES_TAG_FIRST_CASE_NUMBER;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.REFERENCED_COURT_CASES_TAG_FIRST_JUDGMENT_IDS;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.REFERENCED_COURT_CASES_TAG_SECOND_CASE_NUMBER;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.THIRD_ENRICHMENT_TAG_FIRST_ARRAY_VALUE;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.THIRD_ENRICHMENT_TAG_SECOND_ARRAY_VALUE;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.THIRD_ENRICHMENT_TAG_THIRD_ARRAY_VALUE;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.THIRD_ENRICHMENT_TAG_TYPE;
import static pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTagTypes.REFERENCED_COURT_CASES;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import pl.edu.icm.saos.api.ApiTestConfiguration;
import pl.edu.icm.saos.api.search.parameters.ParametersExtractor;
import pl.edu.icm.saos.api.services.exceptions.status.ErrorReason;
import pl.edu.icm.saos.api.services.interceptor.AccessControlHeaderHandlerInterceptor;
import pl.edu.icm.saos.api.services.interceptor.RestrictParamsHandlerInterceptor;
import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.PersistenceTestSupport;
import pl.edu.icm.saos.persistence.common.TestObjectContext;
import pl.edu.icm.saos.persistence.common.TestPersistenceObjectFactory;
import pl.edu.icm.saos.persistence.enrichment.EnrichmentTagRepository;
import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTag;
import pl.edu.icm.saos.persistence.search.DatabaseSearchService;

import com.google.common.collect.Lists;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes =  {ApiTestConfiguration.class})
@Category(SlowTest.class)
public class DumpEnrichmentTagControllerTest extends PersistenceTestSupport {

    @Autowired
    private ParametersExtractor parametersExtractor;

    @Autowired
    private DatabaseSearchService databaseSearchService;

    @Autowired
    private DumpEnrichmentTagsListSuccessRepresentationBuilder dumpEnrichmentTagsListSuccessRepresentationBuilder;

    @Autowired
    private TestPersistenceObjectFactory testPersistenceObjectFactory;
    
    @Autowired
    private EnrichmentTagRepository enrichmentTagRepository;

    private TestObjectContext testObjectContext;

    private MockMvc mockMvc;
    
    
    private List<EnrichmentTag> enrichmentTags = Lists.newArrayList();
    
    private final static String DUMP_ENRICHMENT_PATH = "/api/dump/enrichments";
    
    
    @Before
    public void setUp(){
        testObjectContext = testPersistenceObjectFactory.createTestObjectContext();
        enrichmentTags = testPersistenceObjectFactory.createEnrichmentTagsForJudgment(testObjectContext.getCcJudgmentId());
        
        DumpEnrichmentTagController dumpEnrichmentTagController = new DumpEnrichmentTagController();
        
        dumpEnrichmentTagController.setParametersExtractor(parametersExtractor);
        dumpEnrichmentTagController.setDatabaseSearchService(databaseSearchService);
        dumpEnrichmentTagController.setDumpEnrichmentTagsListSuccessRepresentationBuilder(dumpEnrichmentTagsListSuccessRepresentationBuilder);


        mockMvc = standaloneSetup(dumpEnrichmentTagController)
                .addInterceptors(new RestrictParamsHandlerInterceptor())
                .addInterceptors(new AccessControlHeaderHandlerInterceptor())
                .build();
    }
    
    
    //------------------------ TESTS --------------------------

    @Test
    public void showEnrichmentTags() throws Exception {
        
        // execute
        
        ResultActions result = mockMvc.perform(get(DUMP_ENRICHMENT_PATH)
                .accept(MediaType.APPLICATION_JSON));
        
        
        // assert
        assertOk(result);
        result
            .andExpect(jsonPath("$.items", hasSize(3)))
            
            .andExpect(jsonPath("$.items.[0].id").value(equalsLong(enrichmentTags.get(0).getId())))
            .andExpect(jsonPath("$.items.[0].judgmentId").value(equalsLong(enrichmentTags.get(0).getJudgmentId())))
            .andExpect(jsonPath("$.items.[0].tagType").value(FIRST_ENRICHMENT_TAG_TYPE))
            .andExpect(jsonPath("$.items.[0].value.ref").value(FIRST_ENRICHMENT_TAG_VALUE_VALUE))
        
            .andExpect(jsonPath("$.items.[1].id").value(equalsLong(enrichmentTags.get(1).getId())))
            .andExpect(jsonPath("$.items.[1].judgmentId").value(equalsLong(enrichmentTags.get(1).getJudgmentId())))
            .andExpect(jsonPath("$.items.[1].tagType").value(REFERENCED_COURT_CASES))
            
            .andExpect(jsonPath("$.items.[1].value.[0].caseNumber").value(REFERENCED_COURT_CASES_TAG_FIRST_CASE_NUMBER))
            .andExpect(jsonPath("$.items.[1].value.[0].judgmentIds", hasSize(2)))
            .andExpect(jsonPath("$.items.[1].value.[0].judgmentIds.[0]").value(equalsLong(REFERENCED_COURT_CASES_TAG_FIRST_JUDGMENT_IDS[0])))
            .andExpect(jsonPath("$.items.[1].value.[0].judgmentIds.[1]").value(equalsLong(REFERENCED_COURT_CASES_TAG_FIRST_JUDGMENT_IDS[1])))
            
            .andExpect(jsonPath("$.items.[1].value.[1].caseNumber").value(REFERENCED_COURT_CASES_TAG_SECOND_CASE_NUMBER))
            .andExpect(jsonPath("$.items.[1].value.[1].judgmentIds", hasSize(0)))
            
            .andExpect(jsonPath("$.items.[2].id").value(equalsLong(enrichmentTags.get(2).getId())))
            .andExpect(jsonPath("$.items.[2].judgmentId").value(equalsLong(enrichmentTags.get(2).getJudgmentId())))
            .andExpect(jsonPath("$.items.[2].tagType").value(THIRD_ENRICHMENT_TAG_TYPE))
            .andExpect(jsonPath("$.items.[2].value.keywords.[0]").value(THIRD_ENRICHMENT_TAG_FIRST_ARRAY_VALUE))
            .andExpect(jsonPath("$.items.[2].value.keywords.[1]").value(THIRD_ENRICHMENT_TAG_SECOND_ARRAY_VALUE))
            .andExpect(jsonPath("$.items.[2].value.keywords.[2]").value(THIRD_ENRICHMENT_TAG_THIRD_ARRAY_VALUE));
        
    }
    
    @Test
    public void showEnrichmentTags_PAGING() throws Exception {
        
        // given
        
        int pageSize = 2;
        int pageNumber = 1;
        
        
        // execute
        
        ResultActions result = mockMvc.perform(get(DUMP_ENRICHMENT_PATH)
                .param(PAGE_SIZE, String.valueOf(pageSize))
                .param(PAGE_NUMBER, String.valueOf(pageNumber))
                .accept(MediaType.APPLICATION_JSON));
        
        
        // assert
        assertOk(result);
        result
            .andExpect(jsonPath("$.items", hasSize(1)))
            
            .andExpect(jsonPath("$.items.[0].id").value(equalsLong(enrichmentTags.get(2).getId())))
            .andExpect(jsonPath("$.items.[0].judgmentId").value(equalsLong(enrichmentTags.get(2).getJudgmentId())))
            .andExpect(jsonPath("$.items.[0].tagType").value(THIRD_ENRICHMENT_TAG_TYPE))
            .andExpect(jsonPath("$.items.[0].value.keywords.[0]").value(THIRD_ENRICHMENT_TAG_FIRST_ARRAY_VALUE))
            .andExpect(jsonPath("$.items.[0].value.keywords.[1]").value(THIRD_ENRICHMENT_TAG_SECOND_ARRAY_VALUE))
            .andExpect(jsonPath("$.items.[0].value.keywords.[2]").value(THIRD_ENRICHMENT_TAG_THIRD_ARRAY_VALUE))

            .andExpect(jsonPath("$.queryTemplate.pageSize.value").value(pageSize))
            .andExpect(jsonPath("$.queryTemplate.pageNumber.value").value(pageNumber));
    }
    
    
    @Test
    public void showEnrichmentTags_WRONG_PARAM() throws Exception {
        // execute
        ResultActions actions = mockMvc.perform(get(DUMP_ENRICHMENT_PATH)
                .param("some_incorrect_parameter_name", ""));

        // assert
        assertIncorrectParamNameError(actions, "some_incorrect_parameter_name");
    }
    
    
    @Test
    public void showEnrichmentTags_TOO_SMALL_PAGE_SIZE() throws Exception {
        // execute
        ResultActions actions = mockMvc.perform(get(DUMP_ENRICHMENT_PATH)
                .param(PAGE_SIZE, String.valueOf(1)));
        
        // assert
        assertTooSmallPageSizeError(actions, 2);
    }
    
    @Test
    public void showEnrichmentTags_TOO_BIG_PAGE_SIZE() throws Exception {
        // execute
        ResultActions actions = mockMvc.perform(get(DUMP_ENRICHMENT_PATH)
                .param(PAGE_SIZE, String.valueOf(101)));
        
        // assert
        assertTooBigPageSizeError(actions, 100);
    }
    
    @Test
    public void showEnrichmentTags_INVALID_PAGE_SIZE() throws Exception {
        // execute
        ResultActions actions = mockMvc.perform(get(DUMP_ENRICHMENT_PATH)
                .param(PAGE_SIZE, "invalid"));
        
        // assert
        assertError(actions, HttpStatus.INTERNAL_SERVER_ERROR, ErrorReason.GENERAL_INTERNAL_ERROR .errorReason(),
                null, allOf(containsString("invalid"), containsString("Failed to convert value")));
    }
    
    
    @Test
    public void showEnrichmentTags_INVALID_PAGE_NUMBER() throws Exception {
        // execute
        ResultActions actions = mockMvc.perform(get(DUMP_ENRICHMENT_PATH)
                .param(PAGE_NUMBER, "invalid"));
        
        // assert
        assertError(actions, HttpStatus.INTERNAL_SERVER_ERROR, ErrorReason.GENERAL_INTERNAL_ERROR .errorReason(),
                null, allOf(containsString("invalid"), containsString("Failed to convert value")));
    }
    
    @Test
    public void showEnrichmentTags_NEGATIVE_PAGE_NUMBER() throws Exception {
        // execute
        ResultActions actions = mockMvc.perform(get(DUMP_ENRICHMENT_PATH)
                .param(PAGE_NUMBER, "-1"));
        
        // assert
        assertNegativePageNumberError(actions);
    }
    
    @Test
    public void should_respond_in_iso8859_1_charset() throws Exception {
        // execute
        ResultActions actions = mockMvc.perform(get(DUMP_ENRICHMENT_PATH)
                .accept(MediaType.APPLICATION_JSON+";charset=ISO-8859-1"));
        
        // assert
        assertOk(actions, "ISO-8859-1");
    }
    
}
