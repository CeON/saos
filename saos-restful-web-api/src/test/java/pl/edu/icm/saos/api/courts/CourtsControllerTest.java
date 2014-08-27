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
import pl.edu.icm.saos.api.parameters.ParametersExtractor;
import pl.edu.icm.saos.api.search.ApiSearchService;
import pl.edu.icm.saos.api.search.ElementsSearchResults;
import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.model.CommonCourt;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static pl.edu.icm.saos.api.ApiConstants.LIMIT;
import static pl.edu.icm.saos.api.ApiConstants.OFFSET;
import static pl.edu.icm.saos.api.utils.Constansts.COURTS_PATH;
import static pl.edu.icm.saos.api.utils.FieldsDefinition.createCommonCourt;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes =  {CourtsControllerTest.TestConfiguration.class, ApiConfiguration.class})
@Category(SlowTest.class)
public class CourtsControllerTest {


    @Configuration
    public static class TestConfiguration {

        @Bean(name = "mockCourtsSearchService")
        public ApiSearchService<CommonCourt> courtApiSearchService(){
            return requestParameters -> new ElementsSearchResults<>(requestParameters, Arrays.asList(createCommonCourt()));
        }
    }


    private MockMvc mockMvc;


    @Autowired
    private ParametersExtractor parametersExtractor;

    @Autowired
    @Qualifier("mockCourtsSearchService")
    private ApiSearchService<CommonCourt> searchService;

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
    public void itShouldShowAllBasicsJudgmentsFields() throws Exception {
        //when
        ResultActions actions = mockMvc.perform(get(COURTS_PATH)
                .param(LIMIT, "2")
                .param(OFFSET, "1")
                .accept(MediaType.APPLICATION_JSON));
        //then

        CourtsRepresentationVerifier.verifyBasicFields(actions, "$.items.[0]");

    }

    @Test
    public void itShouldShowRequestParameters() throws Exception {
        //given
        int limit = 11;
        int offset = 5;

        //when
        ResultActions actions = mockMvc.perform(get(COURTS_PATH)
                .param(LIMIT, String.valueOf(limit))
                .param(OFFSET, String.valueOf(offset))
                .accept(MediaType.APPLICATION_JSON));

        //then
        actions
                .andExpect(jsonPath("$.queryTemplate.limit").value(limit))
                .andExpect(jsonPath("$.queryTemplate.offset").value(offset))
        ;
    }

}