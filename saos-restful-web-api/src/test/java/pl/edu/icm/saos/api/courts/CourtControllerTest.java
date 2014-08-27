package pl.edu.icm.saos.api.courts;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import pl.edu.icm.saos.api.ApiConfiguration;

import static org.hamcrest.Matchers.endsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static pl.edu.icm.saos.api.utils.FieldsDefinition.*;
import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.repository.CommonCourtRepository;
import static org.mockito.Mockito.*;
import static pl.edu.icm.saos.api.utils.Constansts.*;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes =  {CourtControllerTest.TestConfiguration.class, ApiConfiguration.class})
@Category(SlowTest.class)
public class CourtControllerTest {

    @Configuration
    public static class TestConfiguration {

        @Bean(name = "mockCourtRepository")
        public CommonCourtRepository commonCourtRepository(){
            CommonCourtRepository commonCourtRepository = mock(CommonCourtRepository.class);
            when(commonCourtRepository.findOne(JC.COURT_ID)).thenReturn(createCommonCourt());

            return commonCourtRepository;
        }
    }

    private MockMvc mockMvc;

    @Autowired
    @Qualifier("mockCourtRepository")
    private CommonCourtRepository courtRepository;

    @Autowired
    private SingleCourtSuccessRepresentationBuilder singleCourtSuccessRepresentationBuilder;


    @Before
    public void setUp(){
        CourtController courtController = new CourtController();

        courtController.setCourtRepository(courtRepository);
        courtController.setSingleCourtSuccessRepresentationBuilder(singleCourtSuccessRepresentationBuilder);

        mockMvc = standaloneSetup(courtController)
                .build();
    }


    @Test
    public void itShouldShowAllCourtsFields() throws Exception {
        //when
        ResultActions actions = mockMvc.perform(get(COURT_PATH)
                .accept(MediaType.APPLICATION_JSON));

        //then
        actions
                .andExpect(jsonPath("$.data.href").value(endsWith(COURT_PATH)))
                .andExpect(jsonPath("$.data.code").value(JC.COURT_CODE))
                .andExpect(jsonPath("$.data.name").value(JC.COURT_NAME))
                .andExpect(jsonPath("$.data.type").value(JC.COURT_TYPE.name()))

                .andExpect(jsonPath("$.data.parentCourt.href").value(endsWith(PARENT_COURT_PATH)))
                .andExpect(jsonPath("$.data.parentCourt.name").value(endsWith(JC.COURT_PARENT_NAME)))

                .andExpect(jsonPath("$.data.divisions").isArray())
                .andExpect(jsonPath("$.data.divisions.[0].href").value(endsWith(DIVISIONS_PATH + "/" + JC.DIVISION_ID)))
                .andExpect(jsonPath("$..data.divisions.[0].name").value(JC.DIVISION_NAME))

                .andExpect(jsonPath("$.data.divisions.[1].href").value(endsWith(DIVISIONS_PATH + "/" + JC.SECOND_DIVISION_ID)))
                .andExpect(jsonPath("$..data.divisions.[1].name").value(JC.SECOND_DIVISION_NAME))

                ;
    }


    @Test
    public void itShouldShowLinks() throws Exception {
        //when
        ResultActions actions = mockMvc.perform(get(COURT_PATH)
                .accept(MediaType.APPLICATION_JSON));

        //then
        actions
                .andExpect(jsonPath("$.links").isArray())
                .andExpect(jsonPath("$.links[?(@.rel==self)].href[0]").value(endsWith(COURT_PATH)))
                .andExpect(jsonPath("$.links[?(@.rel==parentCourt)].href[0]").value(endsWith(PARENT_COURT_PATH)))
        ;
    }

}