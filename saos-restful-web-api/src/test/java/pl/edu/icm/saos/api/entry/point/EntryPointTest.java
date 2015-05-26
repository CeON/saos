package pl.edu.icm.saos.api.entry.point;

import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static pl.edu.icm.saos.api.ApiResponseAssertUtils.assertOk;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import pl.edu.icm.saos.api.ApiTestConfiguration;
import pl.edu.icm.saos.api.dump.DumpEntryPointController;
import pl.edu.icm.saos.api.search.SearchEntryPointController;
import pl.edu.icm.saos.api.services.interceptor.AccessControlHeaderHandlerInterceptor;
import pl.edu.icm.saos.common.testcommon.category.SlowTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes =  {ApiTestConfiguration.class})
@Category(SlowTest.class)
public class EntryPointTest {

    @Autowired
    @Qualifier("apiMessageSource")
    private MessageSource apiMessageService;

    private MockMvc mockMvc;

    @Before
    public void setUp(){

        MainEntryPointController mainEntryPointController = new MainEntryPointController();
        mainEntryPointController.setApiMessageService(apiMessageService);

        DumpEntryPointController dumpEntryPointController = new DumpEntryPointController();
        dumpEntryPointController.setApiMessageService(apiMessageService);

        SearchEntryPointController searchEntryPointController = new SearchEntryPointController();
        searchEntryPointController.setApiMessageService(apiMessageService);


        mockMvc = standaloneSetup(
                    mainEntryPointController,
                    dumpEntryPointController,
                    searchEntryPointController
                )
                .addInterceptors(new AccessControlHeaderHandlerInterceptor())
                .build();

    }

    //------------------------ TESTS --------------------------

    @Test
    public void show__it_should_contain_links_to_the_dump_and_search_services() throws Exception {
        //when
        ResultActions actions = mockMvc.perform(get("/api")
                .accept(MediaType.APPLICATION_JSON));

        //then
        assertOk(actions);
        actions
                .andExpect(jsonPath("$.links[0].rel").value("dump"))
                .andExpect(jsonPath("$.links[0].href").value(endsWith("/api/dump")))
                .andExpect(jsonPath("$.links[0].description").value(notNullValue()))
                ;
    }

    @Test
    public void show__it_should_contain_link_to_the_judgment_search_service() throws Exception {
        //when
        ResultActions actions = mockMvc.perform(get("/api/search")
                .accept(MediaType.APPLICATION_JSON));

        //then
        assertOk(actions);
        actions
                .andExpect(jsonPath("$.links[0].rel").value("judgments"))
                .andExpect(jsonPath("$.links[0].href").value(endsWith("/api/search/judgments")))
                .andExpect(jsonPath("$.links[0].description").value(notNullValue()))
        ;
    }

    @Test
    public void show__it_should_contain_links_to_the_dump_services() throws Exception{
        //when
        ResultActions actions = mockMvc.perform(get("/api/dump")
                .accept(MediaType.APPLICATION_JSON));

        //then
        assertOk(actions);
        actions
                .andExpect(jsonPath("$.links[0].rel").value("commonCourts"))
                .andExpect(jsonPath("$.links[0].href").value(endsWith("/api/dump/commonCourts")))
                .andExpect(jsonPath("$.links[0].description").value(notNullValue()))

                .andExpect(jsonPath("$.links[1].rel").value("judgments"))
                .andExpect(jsonPath("$.links[1].href").value(endsWith("/api/dump/judgments")))
                .andExpect(jsonPath("$.links[1].description").value(notNullValue()))

                .andExpect(jsonPath("$.links[2].rel").value("scChambers"))
                .andExpect(jsonPath("$.links[2].href").value(endsWith("/api/dump/scChambers")))
                .andExpect(jsonPath("$.links[2].description").value(notNullValue()))
                
                .andExpect(jsonPath("$.links[3].rel").value("enrichments"))
                .andExpect(jsonPath("$.links[3].href").value(endsWith("/api/dump/enrichments")))
                .andExpect(jsonPath("$.links[3].description").value(notNullValue()))
        ;
    }

}