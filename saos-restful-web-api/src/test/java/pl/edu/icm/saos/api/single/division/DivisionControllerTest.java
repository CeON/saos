package pl.edu.icm.saos.api.single.division;

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
import pl.edu.icm.saos.api.config.ApiTestConfiguration;
import pl.edu.icm.saos.api.support.TestPersistenceObjectsContext;
import pl.edu.icm.saos.api.support.TestPersistenceObjectsFactory;
import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.PersistenceTestSupport;
import pl.edu.icm.saos.persistence.repository.CcDivisionRepository;

import static org.hamcrest.Matchers.endsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static pl.edu.icm.saos.api.services.Constansts.DIVISIONS_PATH;
import static pl.edu.icm.saos.api.services.Constansts.SINGLE_COURTS_PATH;
import static pl.edu.icm.saos.api.services.FieldsDefinition.JC;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes =  {ApiTestConfiguration.class})
@Category(SlowTest.class)
public class DivisionControllerTest extends PersistenceTestSupport {

    @Autowired
    private CcDivisionRepository ccDivisionRepository;

    @Autowired
    private DivisionSuccessRepresentationBuilder divisionSuccessRepresentationBuilder;

    @Autowired
    private TestPersistenceObjectsFactory testPersistenceObjectsFactory;

    private MockMvc mockMvc;

    private TestPersistenceObjectsContext objectsContext;

    private String divisionPath;
    private String courtPath;
    private String parentCourtPath;

    @Before
    public void setUp(){
        objectsContext = testPersistenceObjectsFactory.createPersistenceObjectsContext();
        divisionPath = DIVISIONS_PATH + "/" +objectsContext.getFirstDivisionId();
        courtPath = SINGLE_COURTS_PATH + "/" +objectsContext.getCommonCourtId();
        parentCourtPath = SINGLE_COURTS_PATH + "/" +objectsContext.getParentCourtId();

        DivisionController divisionController = new DivisionController();

        divisionController.setCcDivisionRepository(ccDivisionRepository);
        divisionController.setDivisionSuccessRepresentationBuilder(divisionSuccessRepresentationBuilder);

        mockMvc = standaloneSetup(divisionController)
                .build();
    }

    @Test
    public void itShouldShowAllDivisionsFields() throws Exception {
        //when
        ResultActions actions = mockMvc.perform(get(divisionPath)
                .accept(MediaType.APPLICATION_JSON));

        //then
        actions
                .andExpect(jsonPath("$.data.href").value(endsWith(divisionPath)))
                .andExpect(jsonPath("$.data.name").value(JC.DIVISION_NAME))
                .andExpect(jsonPath("$.data.code").value(JC.DIVISION_CODE))
                .andExpect(jsonPath("$.data.type").value(JC.DIVISION_TYPE_NAME))

                .andExpect(jsonPath("$.data.court.href").value(endsWith(courtPath)))
                .andExpect(jsonPath("$.data.court.code").value(JC.COURT_CODE))
                .andExpect(jsonPath("$.data.court.name").value(JC.COURT_NAME))
                .andExpect(jsonPath("$.data.court.type").value(JC.COURT_TYPE.name()))

                .andExpect(jsonPath("$.data.court.parentCourt.href").value(endsWith(parentCourtPath)))

        ;
    }

    @Test
    public void itShouldShowLinks() throws Exception {
        //when
        ResultActions actions = mockMvc.perform(get(divisionPath)
                .accept(MediaType.APPLICATION_JSON));

        //then
        actions
                .andExpect(jsonPath("$.links").isArray())
                .andExpect(jsonPath("$.links[?(@.rel==self)].href[0]").value(endsWith(divisionPath)))
                .andExpect(jsonPath("$.links[?(@.rel==court)].href[0]").value(endsWith(courtPath)))
        ;
    }



}