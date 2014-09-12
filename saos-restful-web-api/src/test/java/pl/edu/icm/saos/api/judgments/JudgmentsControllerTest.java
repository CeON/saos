package pl.edu.icm.saos.api.judgments;


import static org.hamcrest.Matchers.endsWith;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static pl.edu.icm.saos.api.ApiConstants.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import pl.edu.icm.saos.api.config.TestsConfig;
import pl.edu.icm.saos.api.judgments.extractors.JudgmentsParametersExtractor;
import pl.edu.icm.saos.api.judgments.parameters.JudgmentsParameters;
import pl.edu.icm.saos.api.search.ApiSearchService;
import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.api.parameters.ParametersExtractor;
import pl.edu.icm.saos.api.utils.FieldsDefinition.JC;
import pl.edu.icm.saos.api.utils.TrivialApiSearchService;
import pl.edu.icm.saos.persistence.model.Judgment;

import static pl.edu.icm.saos.api.utils.Constansts.*;
import static pl.edu.icm.saos.api.judgments.JudgmentRepresentationVerifier.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes =  JudgmentsControllerTest.TestConfiguration.class)
@Category(SlowTest.class)
public class JudgmentsControllerTest {

    @Configuration
    @Import(TestsConfig.class)
    static class TestConfiguration {

        @Bean(name = "mockJudgmentApiSearchService")
        public ApiSearchService<Judgment, JudgmentsParameters> judgmentApiSearchService(){
            return new TrivialApiSearchService();
        }

    }

    private MockMvc mockMvc;

    //*** CONFIGURATION ***

    @Autowired
    private JudgmentsListSuccessRepresentationBuilder listSuccessRepresentationBuilder;


    @Autowired
    @Qualifier("mockJudgmentApiSearchService")
    private ApiSearchService<Judgment, JudgmentsParameters> apiSearchService;

    @Autowired
    private JudgmentsParametersExtractor parametersExtractor;



    @Before
    public void setUp() {
        JudgmentsController judgmentsController = new JudgmentsController();
        judgmentsController.setApiSearchService(apiSearchService);
        judgmentsController.setListSuccessRepresentationBuilder(listSuccessRepresentationBuilder);
        judgmentsController.setParametersExtractor(parametersExtractor);

        mockMvc = standaloneSetup(judgmentsController)
                .build();
    }


    //*** END CONFIGURATION ***

    @Test
    public void showJudgments__it_should_show_all_basics_judgments_fields() throws Exception {
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
    public void showJudgments__it_should_show_request_parameters() throws Exception {
        //given
        int limit = 11;
        int offset = 5;
        String allValue = "someAllValue";
        String judgmentDateFrom = "2010-01-21";
        String judgmentDateTo = "2020-10-13";

        //when
        ResultActions actions = mockMvc.perform(get(JUDGMENTS_PATH)
                .param(LIMIT, String.valueOf(limit))
                .param(OFFSET, String.valueOf(offset))
                .param(ALL, allValue)
                .param(COURT_NAME, JC.COURT_NAME)
                .param(LEGAL_BASE, JC.FIRST_LEGAL_BASE)
                .param(REFERENCED_REGULATION, JC.FIRST_REFERENCED_REGULATION_TEXT)
                .param(JUDGE_NAME, JC.SECOND_JUDGE_NAME)
                .param(KEYWORD, JC.SECOND_KEYWORD)
                .param(JUDGMENT_DATE_FROM, judgmentDateFrom)
                .param(JUDGMENT_DATE_TO, judgmentDateTo)
                .accept(MediaType.APPLICATION_JSON));

        //then
        String prefix = "$.queryTemplate";

        actions
                .andExpect(jsonPath(prefix+".all").value(allValue))
                .andExpect(jsonPath(prefix+".courtName").value(JC.COURT_NAME))
                .andExpect(jsonPath(prefix+".legalBase").value(JC.FIRST_LEGAL_BASE))
                .andExpect(jsonPath(prefix+".referencedRegulation").value(JC.FIRST_REFERENCED_REGULATION_TEXT))
                .andExpect(jsonPath(prefix+".judgeName").value(JC.SECOND_JUDGE_NAME))
                .andExpect(jsonPath(prefix+".keyword").value(JC.SECOND_KEYWORD))
                .andExpect(jsonPath(prefix+".judgmentDateFrom").value(judgmentDateFrom))
                .andExpect(jsonPath(prefix+".judgmentDateTo").value(judgmentDateTo))
                .andExpect(jsonPath(prefix+".limit").value(limit))
                .andExpect(jsonPath(prefix+".offset").value(offset))

        ;
    }
}
