package pl.edu.icm.saos.api.dump.court;


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
import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.PersistenceTestSupport;
import pl.edu.icm.saos.persistence.common.TestObjectContext;
import pl.edu.icm.saos.persistence.common.TestObjectsFactory;
import pl.edu.icm.saos.persistence.search.DatabaseSearchService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static pl.edu.icm.saos.api.ApiConstants.PAGE_NUMBER;
import static pl.edu.icm.saos.api.ApiConstants.PAGE_SIZE;
import static pl.edu.icm.saos.api.services.Constansts.DUMP_COURTS_PATH;
import static pl.edu.icm.saos.persistence.common.TestObjectsDefaultData.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes =  {ApiTestConfiguration.class})
@Category(SlowTest.class)
public class DumpCourtsControllerTest extends PersistenceTestSupport {


    @Autowired
    private ParametersExtractor parametersExtractor;

    @Autowired
    private DatabaseSearchService databaseSearchService;

    @Autowired
    private DumpCourtsListSuccessRepresentationBuilder dumpCourtsListSuccessRepresentationBuilder;

    @Autowired
    private TestObjectsFactory testObjectsFactory;

    private TestObjectContext testObjectContext;

    private MockMvc mockMvc;

    @Before
    public void setUp(){
        testObjectContext = testObjectsFactory.createTestObjectContext(true);

        DumpCourtsController dumpCourtsController = new DumpCourtsController();

        dumpCourtsController.setParametersExtractor(parametersExtractor);
        dumpCourtsController.setDatabaseSearchService(databaseSearchService);
        dumpCourtsController.setDumpCourtsListSuccessRepresentationBuilder(dumpCourtsListSuccessRepresentationBuilder);


        mockMvc = standaloneSetup(dumpCourtsController)
                .build();
    }

    @Test
    public void it_should_show_all_courts_fields_with_divisions_fields() throws Exception {
        //when
        ResultActions actions = mockMvc.perform(get(DUMP_COURTS_PATH)
                .accept(MediaType.APPLICATION_JSON));

        //then

        String pathPrefix = "$.items.[0]";

        actions
                .andExpect(jsonPath(pathPrefix+".id").value(testObjectContext.getCcCourtId()))
                .andExpect(jsonPath(pathPrefix + ".code").value(CC_COURT_CODE))
                .andExpect(jsonPath(pathPrefix + ".name").value(CC_COURT_NAME))
                .andExpect(jsonPath(pathPrefix + ".type").value(CC_COURT_TYPE.name()))

                .andExpect(jsonPath(pathPrefix + ".parentCourt.id").value(testObjectContext.getCcCourtParentId()))

                .andExpect(jsonPath(pathPrefix + ".divisions").isArray())

                .andExpect(jsonPath(pathPrefix + ".divisions.[0].id").value(testObjectContext.getCcFirstDivisionId()))
                .andExpect(jsonPath(pathPrefix + ".divisions.[0].name").value(CC_FIRST_DIVISION_NAME))
                .andExpect(jsonPath(pathPrefix + ".divisions.[0].code").value(CC_FIRST_DIVISION_CODE))
                .andExpect(jsonPath(pathPrefix + ".divisions.[0].type").value(CC_FIRST_DIVISION_TYPE_NAME))

                .andExpect(jsonPath(pathPrefix + ".divisions.[1].id").value(testObjectContext.getCcSecondDivisionId()))
                .andExpect(jsonPath(pathPrefix + ".divisions.[1].name").value(CC_SECOND_DIVISION_NAME))
                .andExpect(jsonPath(pathPrefix + ".divisions.[1].code").value(CC_SECOND_DIVISION_CODE))
                .andExpect(jsonPath(pathPrefix + ".divisions.[1].type").value(CC_SECOND_DIVISION_TYPE_NAME))
                ;

    }

    @Test
    public void it_should_show_request_parameters() throws Exception {
        //given
        int pageSize = 11;
        int pageNumber = 5;

        //when
        ResultActions actions = mockMvc.perform(get(DUMP_COURTS_PATH)
                .param(PAGE_SIZE, String.valueOf(pageSize))
                .param(PAGE_NUMBER, String.valueOf(pageNumber))
                .accept(MediaType.APPLICATION_JSON));

        //then
        actions
                .andExpect(jsonPath("$.queryTemplate.pageSize").value(pageSize))
                .andExpect(jsonPath("$.queryTemplate.pageNumber").value(pageNumber))
        ;
    }

}