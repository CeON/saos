package pl.edu.icm.saos.api.single.scchamber;

import static org.hamcrest.Matchers.endsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static pl.edu.icm.saos.common.testcommon.IntToLongMatcher.equalsLong;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.SC_CHAMBER_NAME;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.SC_FIRST_DIVISION_NAME;

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
import pl.edu.icm.saos.persistence.repository.ScChamberRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes =  {ApiTestConfiguration.class})
@Category(SlowTest.class)
public class ScChamberControllerTest extends PersistenceTestSupport {

    @Autowired
    private ScChamberSuccessRepresentationBuilder scChamberRepresentationBuilder;

    @Autowired
    private ScChamberRepository scChamberRepository;

    @Autowired
    private TestPersistenceObjectFactory testPersistenceObjectFactory;

    private TestObjectContext testObjectContext;

    private MockMvc mockMvc;

    private String chambersPath;


    @Before
    public void setUp(){
        testObjectContext = testPersistenceObjectFactory.createTestObjectContext();
        chambersPath = "/api/scChambers/"+testObjectContext.getScChamberId();

        ScChamberController scChamberController = new ScChamberController();
        scChamberController.setScChamberRepository(scChamberRepository);
        scChamberController.setScChamberRepresentationBuilder(scChamberRepresentationBuilder);

        mockMvc =  standaloneSetup(scChamberController)
                .build();
    }

    @Test
    public void showChamber__it_should_show_all_scDivisions_fields() throws Exception {
        //when
        ResultActions actions = mockMvc.perform(get(chambersPath)
                .accept(MediaType.APPLICATION_JSON));


        //then
        String divisionsPath = "/api/scDivisions/"+testObjectContext.getScFirstDivisionId();
        actions
                .andExpect(jsonPath("$.data.id").value(equalsLong(testObjectContext.getScChamberId())))
                .andExpect(jsonPath("$.data.href").value(endsWith(chambersPath)))
                .andExpect(jsonPath("$.data.name").value(SC_CHAMBER_NAME))

                .andExpect(jsonPath("$.data.divisions.[0].id").value(equalsLong(testObjectContext.getScFirstDivisionId())))
                .andExpect(jsonPath("$.data.divisions.[0].href").value(endsWith(divisionsPath)))
                .andExpect(jsonPath("$.data.divisions.[0].name").value(SC_FIRST_DIVISION_NAME))
        ;

    }

    @Test
    public void showChamber__it_should_show_links() throws Exception {
        //when
        ResultActions actions = mockMvc.perform(get(chambersPath)
                .accept(MediaType.APPLICATION_JSON));

        //then
        actions
                .andExpect(jsonPath("$.links").isArray())
                .andExpect(jsonPath("$.links[?(@.rel==self)].href[0]").value(endsWith(chambersPath)))
        ;
    }

}