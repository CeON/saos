package pl.edu.icm.saos.api.single.court;

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
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_FIRST_DIVISION_NAME;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_SECOND_DIVISION_NAME;

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
import pl.edu.icm.saos.persistence.repository.CommonCourtRepository;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes =  {ApiTestConfiguration.class})
@Category(SlowTest.class)
public class CourtControllerTest extends PersistenceTestSupport {

    @Autowired
    private CommonCourtRepository courtRepository;

    @Autowired
    private TestPersistenceObjectFactory testPersistenceObjectFactory;

    private TestObjectContext testObjectContext;

    private MockMvc mockMvc;


    private String path;
    private String parentPath;


    @Autowired
    private SingleCourtSuccessRepresentationBuilder singleCourtSuccessRepresentationBuilder;


    @Before
    public void setUp(){
        testObjectContext = testPersistenceObjectFactory.createTestObjectContext();
        path = SINGLE_COURTS_PATH+"/"+testObjectContext.getCcCourtId();
        parentPath = SINGLE_COURTS_PATH+"/"+testObjectContext.getCcCourtParentId();

        CourtController courtController = new CourtController();

        courtController.setCourtRepository(courtRepository);
        courtController.setSingleCourtSuccessRepresentationBuilder(singleCourtSuccessRepresentationBuilder);

        mockMvc = standaloneSetup(courtController)
                .build();
    }


    @Test
    public void itShouldShowAllCourtsFields() throws Exception {

        //when
        ResultActions actions = mockMvc.perform(get(path)
                .accept(MediaType.APPLICATION_JSON));

        //then
        String pathPrefix = "$.data";

        actions
                .andExpect(jsonPath(pathPrefix + ".id").value(equalsLong(testObjectContext.getCcCourtId())))
                .andExpect(jsonPath(pathPrefix + ".href").value(endsWith(path)))
                .andExpect(jsonPath(pathPrefix + ".code").value(CC_COURT_CODE))
                .andExpect(jsonPath(pathPrefix + ".name").value(CC_COURT_NAME))
                .andExpect(jsonPath(pathPrefix + ".type").value(CC_COURT_TYPE.name()))

                .andExpect(jsonPath(pathPrefix + ".parentCourt.id").value(equalsLong(testObjectContext.getCcCourtParentId())))
                .andExpect(jsonPath(pathPrefix + ".parentCourt.href").value(endsWith(parentPath)))

                .andExpect(jsonPath(pathPrefix + ".divisions").isArray())
                .andExpect(jsonPath(pathPrefix + ".divisions.[0].id").value(equalsLong(testObjectContext.getCcFirstDivisionId())))
                .andExpect(jsonPath(pathPrefix + ".divisions.[0].href").value(endsWith(SINGLE_DIVISIONS_PATH + "/" + testObjectContext.getCcFirstDivisionId())))
                .andExpect(jsonPath(pathPrefix + ".divisions.[0].name").value(CC_FIRST_DIVISION_NAME))

                .andExpect(jsonPath(pathPrefix + ".divisions.[1].id").value(equalsLong(testObjectContext.getCcSecondDivisionId())))
                .andExpect(jsonPath(pathPrefix + ".divisions.[1].href").value(endsWith(SINGLE_DIVISIONS_PATH + "/" + testObjectContext.getCcSecondDivisionId())))
                .andExpect(jsonPath(pathPrefix + ".divisions.[1].name").value(CC_SECOND_DIVISION_NAME))
        ;

    }


    @Test
    public void itShouldShowLinks() throws Exception {
        //when
        ResultActions actions = mockMvc.perform(get(path)
                .accept(MediaType.APPLICATION_JSON));

        //then
        actions
                .andExpect(jsonPath("$.links").isArray())
                .andExpect(jsonPath("$.links[?(@.rel==self)].href[0]").value(endsWith(path)))
                .andExpect(jsonPath("$.links[?(@.rel==parentCourt)].href[0]").value(endsWith(parentPath)))
        ;
    }

}