package pl.edu.icm.saos.api.dump.supreme.court.chamber;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
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
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.SC_CHAMBER_NAME;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.SC_FIRST_DIVISION_FULL_NAME;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.SC_FIRST_DIVISION_NAME;

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





//import pl.edu.icm.saos.api.ApiConstants;
import pl.edu.icm.saos.api.ApiTestConfiguration;
import pl.edu.icm.saos.api.search.parameters.ParametersExtractor;
import pl.edu.icm.saos.api.services.exceptions.status.ErrorReason;
import pl.edu.icm.saos.api.services.interceptor.RestrictParamsHandlerInterceptor;
import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.PersistenceTestSupport;
import pl.edu.icm.saos.persistence.common.TestObjectContext;
import pl.edu.icm.saos.persistence.common.TestPersistenceObjectFactory;
import pl.edu.icm.saos.persistence.search.DatabaseSearchService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes =  {ApiTestConfiguration.class})
@Category(SlowTest.class)
public class DumpSupremeCourtChambersControllerTest extends PersistenceTestSupport {

    private static final String DUMP_SC_CHAMBERS_PATH = "/api/dump/scChambers";

    @Autowired
    private ParametersExtractor parametersExtractor;

    @Autowired
    private DatabaseSearchService databaseSearchService;

    @Autowired
    private DumpScChambersListsSuccessRepresentationBuilder dumpScChambersRepresentationBuilder;

    @Autowired
    private TestPersistenceObjectFactory testPersistenceObjectFactory;

    private TestObjectContext testObjectContext;


    private MockMvc mockMvc;

    @Before
    public void setUp(){
        testObjectContext = testPersistenceObjectFactory.createTestObjectContext();

        DumpSupremeCourtChambersController scChambersController = new DumpSupremeCourtChambersController();
        scChambersController.setDatabaseSearchService(databaseSearchService);
        scChambersController.setDumpScChambersListsSuccessRepresentationBuilder(dumpScChambersRepresentationBuilder);
        scChambersController.setParametersExtractor(parametersExtractor);

        mockMvc = standaloneSetup(scChambersController)
                .addInterceptors(new RestrictParamsHandlerInterceptor())
                .build();
    }

    //------------------------ TESTS --------------------------

    @Test
    public void it_should_show_all_scChambers_fields_with_divisions_fields() throws Exception {
        //when
        ResultActions actions = mockMvc.perform(get(DUMP_SC_CHAMBERS_PATH)
                .accept(MediaType.APPLICATION_JSON));

        //then

        String pathPrefix = "$.items.[0]";

        assertOk(actions);
        actions
                .andExpect(jsonPath(pathPrefix + ".id").value(equalsLong(testObjectContext.getScChamberId())))
                .andExpect(jsonPath(pathPrefix + ".name").value(SC_CHAMBER_NAME))

                .andExpect(jsonPath(pathPrefix + ".divisions").isArray())

                .andExpect(jsonPath(pathPrefix + ".divisions.[0].id").value(equalsLong(testObjectContext.getScFirstDivisionId())))
                .andExpect(jsonPath(pathPrefix + ".divisions.[0].name").value(SC_FIRST_DIVISION_NAME))
                .andExpect(jsonPath(pathPrefix + ".divisions.[0].fullName").value(SC_FIRST_DIVISION_FULL_NAME))
        ;
    }

    @Test
    public void it_should_show_request_parameters() throws Exception {
        //given
        int pageSize = 11;
        int pageNumber = 5;

        //when
        ResultActions actions = mockMvc.perform(get(DUMP_SC_CHAMBERS_PATH)
                .param(PAGE_SIZE, String.valueOf(pageSize))
                .param(PAGE_NUMBER, String.valueOf(pageNumber))
                .accept(MediaType.APPLICATION_JSON));

        //then
        assertOk(actions);
        actions
                .andExpect(jsonPath("$.queryTemplate.pageSize.value").value(pageSize))
                .andExpect(jsonPath("$.queryTemplate.pageNumber.value").value(pageNumber))
        ;
    }

    @Test
    public void it_should_not_allow_incorrect_request_parameter_name() throws Exception {
        //when
        ResultActions actions = mockMvc.perform(get(DUMP_SC_CHAMBERS_PATH)
                .param("some_incorrect_parameter_name", ""));

        //then
        assertIncorrectParamNameError(actions, "some_incorrect_parameter_name");
    }
    
    
    @Test
    public void it_should_not_allow_too_small_page_size() throws Exception {
        // when
        ResultActions actions = mockMvc.perform(get(DUMP_SC_CHAMBERS_PATH)
                .param(PAGE_SIZE, String.valueOf(1)));
        
        // then
        assertTooSmallPageSizeError(actions, 2);
    }
    
    @Test
    public void it_should_not_allow_too_big_page_size() throws Exception {
        // execute
        ResultActions actions = mockMvc.perform(get(DUMP_SC_CHAMBERS_PATH)
                .param(PAGE_SIZE, String.valueOf(101)));
        
        // assert
        assertTooBigPageSizeError(actions, 100);
    }
    
    @Test
    public void it_should_not_allow_invalid_page_size() throws Exception {
        // execute
        ResultActions actions = mockMvc.perform(get(DUMP_SC_CHAMBERS_PATH)
                .param(PAGE_SIZE, "invalid"));
        
        // assert
        assertError(actions, HttpStatus.INTERNAL_SERVER_ERROR, ErrorReason.GENERAL_INTERNAL_ERROR .errorReason(),
                null, allOf(containsString("invalid"), containsString("Failed to convert value")));
    }
    
    
    @Test
    public void it_should_not_allow_invalid_page_number() throws Exception {
        // execute
        ResultActions actions = mockMvc.perform(get(DUMP_SC_CHAMBERS_PATH)
                .param(PAGE_NUMBER, "invalid"));
        
        // assert
        assertError(actions, HttpStatus.INTERNAL_SERVER_ERROR, ErrorReason.GENERAL_INTERNAL_ERROR .errorReason(),
                null, allOf(containsString("invalid"), containsString("Failed to convert value")));
    }
    
    @Test
    public void it_should_not_allow_negative_page_number() throws Exception {
        // execute
        ResultActions actions = mockMvc.perform(get(DUMP_SC_CHAMBERS_PATH)
                .param(PAGE_NUMBER, "-1"));
        
        // assert
        assertNegativePageNumberError(actions);
    }

}