package pl.edu.icm.saos.api.dump.deletedjudgment;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static pl.edu.icm.saos.api.ApiResponseAssertUtils.assertIncorrectParamNameError;
import static pl.edu.icm.saos.api.ApiResponseAssertUtils.assertNotSupportedMediaType;
import static pl.edu.icm.saos.api.ApiResponseAssertUtils.assertNotSupportedMethod;
import static pl.edu.icm.saos.api.ApiResponseAssertUtils.assertOk;
import static pl.edu.icm.saos.common.testcommon.IntToLongMatcher.equalsLong;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import pl.edu.icm.saos.api.ApiTestSupport;
import pl.edu.icm.saos.api.services.interceptor.AccessControlHeaderHandlerInterceptor;
import pl.edu.icm.saos.api.services.interceptor.RestrictParamsHandlerInterceptor;
import pl.edu.icm.saos.common.json.JsonFormatter;
import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.common.TestPersistenceObjectFactory;
import pl.edu.icm.saos.persistence.repository.DeletedJudgmentRepository;

/**
* @author Łukasz Dumiszewski
*/
@Category(SlowTest.class)
public class DumpDeletedJudgmentControllerTest extends ApiTestSupport {
    
    
    @Autowired
    private DumpDeletedJudgmentsSuccessRepresentationBuilder representationBuilder;
    
    @Autowired
    private DeletedJudgmentRepository deletedJudgmentRepository;

    @Autowired
    private JsonFormatter jsonFormatter;

    @Autowired
    private TestPersistenceObjectFactory testPersistenceObjectFactory;

    
    private MockMvc mockMvc;
    
    
    private final static String DUMP_PATH = "/api/dump/judgments/deleted";
    
    
    @Before
    public void setUp(){
        
        DumpDeletedJudgmentController dumpDeletedJudgmentController = new DumpDeletedJudgmentController();
        
        dumpDeletedJudgmentController.setRepresentationBuilder(representationBuilder);
        dumpDeletedJudgmentController.setDeletedJudgmentRepository(deletedJudgmentRepository);
        dumpDeletedJudgmentController.setJsonFormatter(jsonFormatter);
        

        mockMvc = standaloneSetup(dumpDeletedJudgmentController)
                .addInterceptors(new AccessControlHeaderHandlerInterceptor())
                .addInterceptors(new RestrictParamsHandlerInterceptor())
                .build();
    }
    
    
    //------------------------ TESTS --------------------------

    @Test
    public void showDeletedJudgments() throws Exception {
        
        // given
        
        testPersistenceObjectFactory.createDeletedJudgments(123L, 124L, 1240L);
        
        
        // execute
        
        ResultActions result = mockMvc.perform(get(DUMP_PATH).accept(MediaType.APPLICATION_JSON));
        
        
        // assert
        
        assertOk(result);
        result
            .andExpect(jsonPath("$.links", hasSize(1)))
            .andExpect(jsonPath("$.links.[0].rel").value("self"))
            .andExpect(jsonPath("$.links.[0].href").value(Matchers.endsWith(DUMP_PATH)))
            
            .andExpect(jsonPath("$.items", hasSize(3)))
            .andExpect(jsonPath("$.items.[0]").value(equalsLong(123L)))
            .andExpect(jsonPath("$.items.[1]").value(equalsLong(124L)))
            .andExpect(jsonPath("$.items.[2]").value(equalsLong(1240L)))
            .andExpect(jsonPath("$.queryTemplate").value(Matchers.nullValue()))
            .andExpect(jsonPath("$.info").value(Matchers.nullValue()));

    }    
   
    
    @Test
    public void showDeletedJudgments_WRONG_PARAM() throws Exception {
        // execute
        ResultActions actions = mockMvc.perform(get(DUMP_PATH).param("some_incorrect_parameter_name", "")
                                                              .accept(MediaType.APPLICATION_JSON));

        // assert
        assertIncorrectParamNameError(actions, "some_incorrect_parameter_name");
    }

    
    @Test
    public void should_respond_in_iso8859_1_charset() throws Exception {
        // execute
        ResultActions actions = mockMvc.perform(get(DUMP_PATH).accept(MediaType.APPLICATION_JSON+";charset=ISO-8859-1"));
        
        // assert
        assertOk(actions, "ISO-8859-1");
    }
    
    @Test
    public void should_not_allow_not_supported_method() throws Exception {
        // execute
        ResultActions actions = mockMvc.perform(post(DUMP_PATH).accept(MediaType.APPLICATION_JSON));
        
        // assert
        assertNotSupportedMethod(actions, "POST", "GET");
    }
    
    @Test
    public void should_not_allow_not_supported_media_type() throws Exception {
        // execute
        ResultActions actions = mockMvc.perform(get(DUMP_PATH).accept(MediaType.APPLICATION_XML));
        
        // assert
        assertNotSupportedMediaType(actions, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE);
    }
    
}
