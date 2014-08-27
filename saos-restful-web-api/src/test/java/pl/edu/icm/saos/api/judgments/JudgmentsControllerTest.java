package pl.edu.icm.saos.api.judgments;


import static org.hamcrest.Matchers.endsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static pl.edu.icm.saos.api.ApiConstants.*;

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

import pl.edu.icm.saos.api.ApiConfiguration;
import pl.edu.icm.saos.api.search.ApiSearchService;
import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.api.parameters.ParametersExtractor;
import pl.edu.icm.saos.api.utils.FieldsDefinition.JC;
import pl.edu.icm.saos.api.utils.TrivialApiSearchService;
import static pl.edu.icm.saos.api.utils.Constansts.*;
import static pl.edu.icm.saos.api.judgments.JudgmentRepresentationVerifier.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes =  ApiConfiguration.class)
@Category(SlowTest.class)
public class JudgmentsControllerTest {

    private MockMvc mockMvc;

    //*** CONFIGURATION ***

    @Autowired
    private JudgmentsListSuccessRepresentationBuilder listSuccessRepresentationBuilder;

    @Autowired
    private ParametersExtractor parametersExtractor;

    @Before
    public void setUp() {
        ApiSearchService apiSearchService = new TrivialApiSearchService();

        JudgmentsController judgmentsController = new JudgmentsController();
        judgmentsController.setApiSearchService(apiSearchService);
        judgmentsController.setListSuccessRepresentationBuilder(listSuccessRepresentationBuilder);
        judgmentsController.setParametersExtractor(parametersExtractor);

        mockMvc = standaloneSetup(judgmentsController)
                .build();
    }


    //*** END CONFIGURATION ***

    @Test
    public void itShouldShowAllBasicsJudgmentsFields() throws Exception {
        //when
        ResultActions actions = mockMvc.perform(get(JUDGMENTS_PATH)
                .param(LIMIT, "2")
                .param(OFFSET, "1")
                .accept(MediaType.APPLICATION_JSON));
        //then
        verifyBasicFields(actions, "$.items.[0]");

        actions
                .andExpect(jsonPath("$.items.[0].source").doesNotExist())
                .andExpect(jsonPath("$.items.[0].courtReporters").doesNotExist())
                .andExpect(jsonPath("$.items.[0].decision").doesNotExist())
                .andExpect(jsonPath("$.items.[0].summary").doesNotExist())
                .andExpect(jsonPath("$.items.[0].textContent").doesNotExist())
                .andExpect(jsonPath("$.items.[0].reasoning").doesNotExist())
                .andExpect(jsonPath("$.items.[0].legalBases").doesNotExist())
                .andExpect(jsonPath("$.items.[0].referencedRegulations").doesNotExist())

                .andExpect(jsonPath("$.items.[0].division.code").doesNotExist())
                .andExpect(jsonPath("$.items.[0].division.type").doesNotExist())

                .andExpect(jsonPath("$.items.[0].division.court.code").doesNotExist())
                .andExpect(jsonPath("$.items.[0].division.court.type").doesNotExist())

                .andExpect(jsonPath("$.items.[0].division.href").value(endsWith(DIVISIONS_PATH+"/"+JC.DIVISION_ID)))
                .andExpect(jsonPath("$.items.[0].division.name").value(JC.DIVISION_NAME))

                .andExpect(jsonPath("$.items.[0].division.court.href").value(endsWith(COURTS_PATH+"/"+JC.COURT_ID)))
                .andExpect(jsonPath("$.items.[0].division.court.name").value(JC.COURT_NAME))
        ;
    }



    @Test
    public void itShouldShowRequestParameters() throws Exception {
        //given
        int limit = 11;
        int offset = 5;
        String expand = ALL;

        //when
        ResultActions actions = mockMvc.perform(get(JUDGMENTS_PATH)
                .param(LIMIT, String.valueOf(limit))
                .param(OFFSET, String.valueOf(offset))
                .param(EXPAND, expand)
                .accept(MediaType.APPLICATION_JSON));

        //then
        actions
                .andExpect(jsonPath("$.queryTemplate.limit").value(limit))
                .andExpect(jsonPath("$.queryTemplate.offset").value(offset))
                .andExpect(jsonPath("$.queryTemplate.expand").value(expand))
        ;
    }
}
