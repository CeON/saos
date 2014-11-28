package pl.edu.icm.saos.api.dump.supreme.court.chamber;

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
import pl.edu.icm.saos.persistence.common.TestObjectFactory;
import pl.edu.icm.saos.persistence.search.DatabaseSearchService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static pl.edu.icm.saos.api.ApiConstants.PAGE_NUMBER;
import static pl.edu.icm.saos.api.ApiConstants.PAGE_SIZE;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.*;

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
    private TestObjectFactory testObjectsFactory;

    private TestObjectContext testObjectContext;


    private MockMvc mockMvc;

    @Before
    public void setUp(){
        testObjectContext = testObjectsFactory.createTestObjectContext(true);

        DumpSupremeCourtChambersController scChambersController = new DumpSupremeCourtChambersController();
        scChambersController.setDatabaseSearchService(databaseSearchService);
        scChambersController.setDumpScChambersListsSuccessRepresentationBuilder(dumpScChambersRepresentationBuilder);
        scChambersController.setParametersExtractor(parametersExtractor);

        mockMvc = standaloneSetup(scChambersController)
                .build();
    }

    @Test
    public void it_should_show_all_scChambers_fields_with_divisions_fields() throws Exception {
        //when
        ResultActions actions = mockMvc.perform(get(DUMP_SC_CHAMBERS_PATH)
                .accept(MediaType.APPLICATION_JSON));

        //then

        String pathPrefix = "$.items.[0]";

        actions
                .andExpect(jsonPath(pathPrefix + ".id").value(testObjectContext.getScChamberId()))
                .andExpect(jsonPath(pathPrefix + ".name").value(SC_CHAMBER_NAME))

                .andExpect(jsonPath(pathPrefix + ".divisions").isArray())

                .andExpect(jsonPath(pathPrefix + ".divisions.[0].id").value(testObjectContext.getScFirstDivisionId()))
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
        actions
                .andExpect(jsonPath("$.queryTemplate.pageSize").value(pageSize))
                .andExpect(jsonPath("$.queryTemplate.pageNumber").value(pageNumber))
        ;
    }

}