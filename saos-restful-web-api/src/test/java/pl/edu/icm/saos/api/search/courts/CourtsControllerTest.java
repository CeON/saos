package pl.edu.icm.saos.api.search.courts;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import pl.edu.icm.saos.api.config.ApiTestConfiguration;
import pl.edu.icm.saos.api.search.parameters.ParametersExtractor;
import pl.edu.icm.saos.api.search.parameters.RequestParameters;
import pl.edu.icm.saos.api.search.services.ApiSearchService;
import pl.edu.icm.saos.api.search.services.ElementsSearchResults;
import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.model.CommonCourt;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static pl.edu.icm.saos.api.ApiConstants.*;
import static pl.edu.icm.saos.api.services.Constansts.COURTS_PATH;
import static pl.edu.icm.saos.api.services.FieldsDefinition.createCommonCourt;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes =  {CourtsControllerTest.TestConfiguration.class})
@Category(SlowTest.class)
public class CourtsControllerTest {

    @Configuration
    @Import(ApiTestConfiguration.class)
    static class TestConfiguration {

        @Bean(name = "mockCourtsSearchService")
        public ApiSearchService<CommonCourt,RequestParameters> courtApiSearchService(){
            return requestParameters -> new ElementsSearchResults<>( Arrays.asList(createCommonCourt()), requestParameters);
        }
    }


    private MockMvc mockMvc;


    @Autowired
    private ParametersExtractor parametersExtractor;

    @Autowired
    @Qualifier("mockCourtsSearchService")
    private ApiSearchService<CommonCourt,RequestParameters> searchService;

    @Autowired
    private CourtsListSuccessRepresentationBuilder successRepresentationBuilder;

    @Before
    public void setUp(){
        CourtsController courtsController = new CourtsController();

        courtsController.setParametersExtractor(parametersExtractor);
        courtsController.setSearchService(searchService);
        courtsController.setSuccessRepresentationBuilder(successRepresentationBuilder);

        mockMvc = standaloneSetup(courtsController)
                .build();
    }


    @Test
    public void showCourts__it_should_show_all_basics_courts_fields() throws Exception {
        //when
        ResultActions actions = mockMvc.perform(get(COURTS_PATH)
                .param(PAGE_SIZE, "2")
                .param(PAGE_NUMBER, "1")
                .accept(MediaType.APPLICATION_JSON));
        //then

        CourtsRepresentationVerifier.verifyBasicFields(actions, "$.items.[0]");

    }

    @Test
    public void showCourts__it_should_show_request_parameters() throws Exception {
        //given
        int pageSize = 11;
        int pageNumber = 5;

        //when
        ResultActions actions = mockMvc.perform(get(COURTS_PATH)
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