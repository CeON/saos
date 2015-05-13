package pl.edu.icm.saos.api.single.ccdivision;

import static org.hamcrest.Matchers.endsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static pl.edu.icm.saos.api.ApiResponseAssertUtils.assertNotFoundError;
import static pl.edu.icm.saos.api.ApiResponseAssertUtils.assertOk;
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
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import pl.edu.icm.saos.api.ApiTestConfiguration;
import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.PersistenceTestSupport;
import pl.edu.icm.saos.persistence.common.TestObjectContext;
import pl.edu.icm.saos.persistence.common.TestPersistenceObjectFactory;
import pl.edu.icm.saos.persistence.repository.CcDivisionRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes =  {ApiTestConfiguration.class})
@Category(SlowTest.class)
public class CcDivisionControllerTest extends PersistenceTestSupport {

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
    
    private long notExistingDivisionId;
    private String notExistingDivisionPath;

    @Before
    public void setUp(){
        testObjectContext = testPersistenceObjectFactory.createTestObjectContext();
        divisionPath = SINGLE_DIVISIONS_PATH + "/" +testObjectContext.getCcFirstDivisionId();
        courtPath = SINGLE_COURTS_PATH + "/" +testObjectContext.getCcCourtId();
        parentCourtPath = SINGLE_COURTS_PATH + "/" +testObjectContext.getCcCourtParentId();
        notExistingDivisionId = ccDivisionRepository.findAll(new Sort(Direction.DESC, "id")).get(0).getId() + 1;
        notExistingDivisionPath = SINGLE_DIVISIONS_PATH + "/" + notExistingDivisionId;
        

        CcDivisionController divisionController = new CcDivisionController();

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
        assertOk(actions);
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
        actions.andDo(MockMvcResultHandlers.print());
        //then
        assertOk(actions);
        actions
                .andExpect(jsonPath("$.links").isArray())
                .andExpect(jsonPath("$.links[?(@.rel==self)].href[0]").value(endsWith(divisionPath)))
                .andExpect(jsonPath("$.links[?(@.rel==court)].href[0]").value(endsWith(courtPath)))
        ;
    }
    
    
    @Test
    public void it_should_not_allow_not_existing_division_id() throws Exception {
        // when
        ResultActions actions = mockMvc.perform(get(notExistingDivisionPath));
        
        // then
        assertNotFoundError(actions, notExistingDivisionId);
    }
    
    @Test
    public void should_respond_in_iso8859_1_charset() throws Exception {
        // when
        ResultActions actions = mockMvc.perform(get(divisionPath)
                .accept(MediaType.APPLICATION_JSON+";charset=ISO-8859-1"));
        
        // then
        assertOk(actions, "ISO-8859-1");
    }
    
    



}