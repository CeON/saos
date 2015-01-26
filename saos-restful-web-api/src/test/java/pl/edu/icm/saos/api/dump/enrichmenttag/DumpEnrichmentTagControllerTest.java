package pl.edu.icm.saos.api.dump.enrichmenttag;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static pl.edu.icm.saos.api.ApiConstants.PAGE_NUMBER;
import static pl.edu.icm.saos.api.ApiConstants.PAGE_SIZE;
import static pl.edu.icm.saos.common.json.JsonNormalizer.normalizeJson;

import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import pl.edu.icm.saos.api.ApiTestConfiguration;
import pl.edu.icm.saos.api.search.parameters.ParametersExtractor;
import pl.edu.icm.saos.api.services.interceptor.RestrictParamsHandlerInterceptor;
import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.PersistenceTestSupport;
import pl.edu.icm.saos.persistence.common.TestObjectContext;
import pl.edu.icm.saos.persistence.common.TestPersistenceObjectFactory;
import pl.edu.icm.saos.persistence.enrichment.EnrichmentTagRepository;
import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTag;
import pl.edu.icm.saos.persistence.search.DatabaseSearchService;

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
    
    private final static String DUMP_ENRICHMENT_PATH = "/api/dump/enrichment";
    
    
    @Before
    public void setUp(){
        testObjectContext = testPersistenceObjectFactory.createTestObjectContext();
        createEnrichmentTags();
        
        
        DumpEnrichmentTagController dumpEnrichmentTagController = new DumpEnrichmentTagController();
        
        dumpEnrichmentTagController.setParametersExtractor(parametersExtractor);
        dumpEnrichmentTagController.setDatabaseSearchService(databaseSearchService);
        dumpEnrichmentTagController.setDumpEnrichmentTagsListSuccessRepresentationBuilder(dumpEnrichmentTagsListSuccessRepresentationBuilder);


        mockMvc = standaloneSetup(dumpEnrichmentTagController)
                .addInterceptors(new RestrictParamsHandlerInterceptor())
                .build();
    }
    
    
    //------------------------ TESTS --------------------------

    @Test
    public void showEnrichmentTags() throws Exception {
        
        // execute
        
        ResultActions result = mockMvc.perform(get(DUMP_ENRICHMENT_PATH)
                .accept(MediaType.APPLICATION_JSON));
        
        
        // assert
        
        result
            .andExpect(jsonPath("$.items", hasSize(3)))
            
            .andExpect(jsonPath("$.items.[0].id").value(enrichmentTags.get(0).getId()))
            .andExpect(jsonPath("$.items.[0].judgmentId").value(enrichmentTags.get(0).getJudgmentId()))
            .andExpect(jsonPath("$.items.[0].tagType").value("REFERENCED_REGULATIONS"))
            .andExpect(jsonPath("$.items.[0].value.ref").value("AAA1"))
        
            .andExpect(jsonPath("$.items.[1].id").value(enrichmentTags.get(1).getId()))
            .andExpect(jsonPath("$.items.[1].judgmentId").value(enrichmentTags.get(1).getJudgmentId()))
            .andExpect(jsonPath("$.items.[1].tagType").value("REFERENCED_CASE_NUMBERS"))
            .andExpect(jsonPath("$.items.[1].value.caseNumbers.[0]").value("XYZ1"))
            .andExpect(jsonPath("$.items.[1].value.caseNumbers.[1]").value("XYZ2"))
        
            .andExpect(jsonPath("$.items.[2].id").value(enrichmentTags.get(2).getId()))
            .andExpect(jsonPath("$.items.[2].judgmentId").value(enrichmentTags.get(2).getJudgmentId()))
            .andExpect(jsonPath("$.items.[2].tagType").value("KEYWORDS"))
            .andExpect(jsonPath("$.items.[2].value.keywords.[0]").value("val11"))
            .andExpect(jsonPath("$.items.[2].value.keywords.[1]").value("val21"))
            .andExpect(jsonPath("$.items.[2].value.keywords.[2]").value("val32"));
        
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
        
        result
            .andExpect(jsonPath("$.items", hasSize(1)))
            
            .andExpect(jsonPath("$.items.[0].id").value(enrichmentTags.get(2).getId()))
            .andExpect(jsonPath("$.items.[0].judgmentId").value(enrichmentTags.get(2).getJudgmentId()))
            .andExpect(jsonPath("$.items.[0].tagType").value("KEYWORDS"))
            .andExpect(jsonPath("$.items.[0].value.keywords.[0]").value("val11"))
            .andExpect(jsonPath("$.items.[0].value.keywords.[1]").value("val21"))
            .andExpect(jsonPath("$.items.[0].value.keywords.[2]").value("val32"))

            .andExpect(jsonPath("$.queryTemplate.pageSize.value").value(pageSize))
            .andExpect(jsonPath("$.queryTemplate.pageNumber.value").value(pageNumber));
    }
    
    @Test
    public void showEnrichmentTags_WRONG_PARAM() throws Exception {
        
        // execute
        
        ResultActions actions = mockMvc.perform(get(DUMP_ENRICHMENT_PATH)
                .param("some_incorrect_parameter_name", "")
                .accept(MediaType.APPLICATION_JSON));

        
        // assert
        
        actions.andExpect(status().isBadRequest());
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private void createEnrichmentTags() {
        EnrichmentTag enrichmentTag;
        
        String jsonValue1 = normalizeJson("{'ref':'AAA1'}");
        String jsonValue2 = normalizeJson("{'caseNumbers':['XYZ1','XYZ2']}");
        String jsonValue3 = normalizeJson("{'keywords':['val11','val21','val32']}");
        
        enrichmentTag = createEnrichmentTag(testObjectContext.getCcCourtId(), "REFERENCED_REGULATIONS", jsonValue1);
        enrichmentTags.add(enrichmentTag);
        enrichmentTag = createEnrichmentTag(testObjectContext.getCcCourtId(), "REFERENCED_CASE_NUMBERS", jsonValue2);
        enrichmentTags.add(enrichmentTag);
        enrichmentTag = createEnrichmentTag(testObjectContext.getCcCourtId(), "KEYWORDS", jsonValue3);
        enrichmentTags.add(enrichmentTag);
        
    }
    
    private EnrichmentTag createEnrichmentTag(int judgmentId, String tagType, String jsonValue) {
        EnrichmentTag enrichmentTag = new EnrichmentTag();
        
        enrichmentTag.setJudgmentId(judgmentId);
        enrichmentTag.setTagType(tagType);
        enrichmentTag.setValue(jsonValue);
        
        enrichmentTagRepository.save(enrichmentTag);
        return enrichmentTag;
    }
}
