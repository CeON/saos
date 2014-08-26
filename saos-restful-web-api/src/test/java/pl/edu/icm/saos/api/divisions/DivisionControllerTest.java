package pl.edu.icm.saos.api.divisions;

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
import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.repository.CcDivisionRepository;

import static org.hamcrest.Matchers.endsWith;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static pl.edu.icm.saos.api.utils.Constansts.*;
import static pl.edu.icm.saos.api.utils.FieldsDefinition.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes =  {DivisionControllerTest.TestConfiguration.class, ApiConfiguration.class})
@Category(SlowTest.class)
public class DivisionControllerTest {

    @Configuration
    public static class TestConfiguration {

        @Bean(name = "mockCcDivisionRepository")
        public CcDivisionRepository ccDivisionRepository(){
            CcDivisionRepository divisionRepository = mock(CcDivisionRepository.class);
            when(divisionRepository.findOne(JC.DIVISION_ID)).thenReturn(createCommonDivision());

            return divisionRepository;
        }

    }


    @Autowired
    @Qualifier("mockCcDivisionRepository")
    private CcDivisionRepository ccDivisionRepository;

    @Autowired
    private DivisionSuccessRepresentationBuilder divisionSuccessRepresentationBuilder;

    private MockMvc mockMvc;

    @Before
    public void setUp(){
        DivisionController divisionController = new DivisionController();

        divisionController.setCcDivisionRepository(ccDivisionRepository);
        divisionController.setDivisionSuccessRepresentationBuilder(divisionSuccessRepresentationBuilder);

        mockMvc = standaloneSetup(divisionController)
                .build();
    }

    @Test
    public void itShouldShowAllDivisionsFields() throws Exception {
        //when
        ResultActions actions = mockMvc.perform(get(DIVISION_PATH)
                .accept(MediaType.APPLICATION_JSON));

        //then
        actions
                .andExpect(jsonPath("$.data.href").value(endsWith(DIVISION_PATH)))
                .andExpect(jsonPath("$.data.name").value(JC.DIVISION_NAME))
                .andExpect(jsonPath("$.data.code").value(JC.DIVISION_CODE))
                .andExpect(jsonPath("$.data.type").value(JC.DIVISION_TYPE_NAME))

                .andExpect(jsonPath("$.data.court.href").value(endsWith(COURT_PATH)))
                .andExpect(jsonPath("$.data.court.code").value(JC.COURT_CODE))
                .andExpect(jsonPath("$.data.court.name").value(JC.COURT_NAME))
                .andExpect(jsonPath("$.data.court.type").value(JC.COURT_TYPE.name()))

                .andExpect(jsonPath("$.data.court.parentCourt.href").value(endsWith(PARENT_COURT_PATH)))
                .andExpect(jsonPath("$.data.court.parentCourt.name").value(endsWith(JC.COURT_PARENT_NAME)))

        ;
    }

    @Test
    public void itShouldShowLinks() throws Exception {
        //when
        ResultActions actions = mockMvc.perform(get(DIVISION_PATH)
                .accept(MediaType.APPLICATION_JSON));

        //then
        actions
                .andExpect(jsonPath("$.links").isArray())
                .andExpect(jsonPath("$.links[?(@.rel==self)].href[0]").value(endsWith(DIVISION_PATH)))
                .andExpect(jsonPath("$.links[?(@.rel==court)].href[0]").value(endsWith(COURT_PATH)))
        ;
    }



}