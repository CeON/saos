package pl.edu.icm.saos.api.single.division;

import static org.hamcrest.Matchers.endsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static pl.edu.icm.saos.api.services.Constants.SINGLE_COURTS_PATH;
import static pl.edu.icm.saos.api.services.Constants.SINGLE_DIVISIONS_PATH;
import static pl.edu.icm.saos.common.testcommon.IntToLongMatcher.equalsLong;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_COURT_CODE;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_COURT_NAME;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_COURT_TYPE;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_FIRST_DIVISION_CODE;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_FIRST_DIVISION_NAME;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_FIRST_DIVISION_TYPE_NAME;

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
import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.PersistenceTestSupport;
import pl.edu.icm.saos.persistence.common.TestObjectContext;
import pl.edu.icm.saos.persistence.common.TestPersistenceObjectFactory;
import pl.edu.icm.saos.persistence.repository.CcDivisionRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes =  {ApiTestConfiguration.class})
@Category(SlowTest.class)
public class DivisionControllerTest extends PersistenceTestSupport {

    @Autowired
    private CcDivisionRepository ccDivisionRepository;

    @Autowired
    private DivisionSuccessRepresentationBuilder divisionSuccessRepresentationBuilder;

    @Autowired
    private TestPersistenceObjectFactory testPersistenceObjectFactory;

    private TestObjectContext testObjectContext;


    private MockMvc mockMvc;


    private String divisionPath;
    private String courtPath;
    private String parentCourtPath;

    @Before
    public void setUp(){
        testObjectContext = testPersistenceObjectFactory.createTestObjectContext();
        divisionPath = SINGLE_DIVISIONS_PATH + "/" +testObjectContext.getCcFirstDivisionId();
        courtPath = SINGLE_COURTS_PATH + "/" +testObjectContext.getCcCourtId();
        parentCourtPath = SINGLE_COURTS_PATH + "/" +testObjectContext.getCcCourtParentId();

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
                .andExpect(jsonPath("$.data.id").value(equalsLong(testObjectContext.getCcFirstDivisionId())))
                .andExpect(jsonPath("$.data.href").value(endsWith(divisionPath)))
                .andExpect(jsonPath("$.data.name").value(CC_FIRST_DIVISION_NAME))
                .andExpect(jsonPath("$.data.code").value(CC_FIRST_DIVISION_CODE))
                .andExpect(jsonPath("$.data.type").value(CC_FIRST_DIVISION_TYPE_NAME))

                .andExpect(jsonPath("$.data.court.id").value(equalsLong(testObjectContext.getCcCourtId())))
                .andExpect(jsonPath("$.data.court.href").value(endsWith(courtPath)))
                .andExpect(jsonPath("$.data.court.code").value(CC_COURT_CODE))
                .andExpect(jsonPath("$.data.court.name").value(CC_COURT_NAME))
                .andExpect(jsonPath("$.data.court.type").value(CC_COURT_TYPE.name()))

                .andExpect(jsonPath("$.data.court.parentCourt.id").value(equalsLong(testObjectContext.getCcCourtParentId())))
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