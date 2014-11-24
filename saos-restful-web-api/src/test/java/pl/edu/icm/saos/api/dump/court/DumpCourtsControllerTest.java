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
import pl.edu.icm.saos.api.config.ApiWithMockSearchTestConfiguration;
import pl.edu.icm.saos.api.search.parameters.ParametersExtractor;
import pl.edu.icm.saos.api.support.TestPersistenceObjectsContext;
import pl.edu.icm.saos.api.support.TestPersistenceObjectsFactory;
import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.PersistenceTestSupport;
import pl.edu.icm.saos.persistence.search.DatabaseSearchService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static pl.edu.icm.saos.api.ApiConstants.PAGE_NUMBER;
import static pl.edu.icm.saos.api.ApiConstants.PAGE_SIZE;
import static pl.edu.icm.saos.api.services.Constansts.DUMP_COURTS_PATH;
import static pl.edu.icm.saos.api.services.FieldsDefinition.JC;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes =  {ApiWithMockSearchTestConfiguration.class})
@Category(SlowTest.class)
public class DumpCourtsControllerTest extends PersistenceTestSupport {


    @Autowired
    private ParametersExtractor parametersExtractor;

    @Autowired
    private DatabaseSearchService databaseSearchService;

    @Autowired
    private DumpCourtsListSuccessRepresentationBuilder dumpCourtsListSuccessRepresentationBuilder;

    @Autowired
    private TestPersistenceObjectsFactory testPersistenceObjectsFactory;


    private MockMvc mockMvc;
    private TestPersistenceObjectsContext objectsContext;

    @Before
    public void setUp(){
        objectsContext = testPersistenceObjectsFactory.createPersistenceObjectsContext();

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
                .andExpect(jsonPath(pathPrefix+".id").value(objectsContext.getCommonCourtId()))
                .andExpect(jsonPath(pathPrefix + ".code").value(JC.COURT_CODE))
                .andExpect(jsonPath(pathPrefix + ".name").value(JC.COURT_NAME))
                .andExpect(jsonPath(pathPrefix+".type").value(JC.COURT_TYPE.name()))

                .andExpect(jsonPath(pathPrefix + ".parentCourt.id").value(objectsContext.getParentCourtId()))

                .andExpect(jsonPath(pathPrefix + ".divisions").isArray())

                .andExpect(jsonPath(pathPrefix + ".divisions.[0].id").value(objectsContext.getFirstDivisionId()))
                .andExpect(jsonPath(pathPrefix + ".divisions.[0].name").value(JC.DIVISION_NAME))
                .andExpect(jsonPath(pathPrefix + ".divisions.[0].code").value(JC.DIVISION_CODE))
                .andExpect(jsonPath(pathPrefix +".divisions.[0].type").value(JC.DIVISION_TYPE_NAME))

                .andExpect(jsonPath(pathPrefix + ".divisions.[1].id").value(objectsContext.getSecondDivisionId()))
                .andExpect(jsonPath(pathPrefix + ".divisions.[1].name").value(JC.SECOND_DIVISION_NAME))
                .andExpect(jsonPath(pathPrefix + ".divisions.[1].code").value(JC.SECOND_DIVISION_CODE))
                .andExpect(jsonPath(pathPrefix +".divisions.[1].type").value(JC.SECOND_DIVISION_TYPE_NAME))
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