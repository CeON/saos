package pl.edu.icm.saos.api.judgments;


import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static pl.edu.icm.saos.api.ApiConstants.*;

import org.joda.time.DateTime;
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
import pl.edu.icm.saos.api.judgments.services.ApiSearchService;
import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.api.parameters.ParametersExtractor;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.SourceCode;
import pl.edu.icm.saos.api.utils.FieldsDefinition.JC;
import pl.edu.icm.saos.api.utils.TrivialApiSearchService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes =  ApiConfiguration.class)
//@Category(SlowTest.class)
public class JudgmentsControllerTest {

    private static final String JUDGMENTS_PATH = "/api/judgments";

    private static final String DATE_FORMAT = "YYYY-MM-dd";

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
    public void itShouldShowAllJudgmentsFields() throws Exception {
        //when
        ResultActions actions = mockMvc.perform(get(JUDGMENTS_PATH)
                .param(LIMIT, "2")
                .param(OFFSET, "1")
                .param(EXPAND, ALL)
                .accept(MediaType.APPLICATION_JSON));
        //then
        checkBasicFields(actions);
        checkAdditionalFields(actions);
    }

    @Test
    public void itShouldShowOnlyBasicJudgmentsFields() throws Exception {
        //when
        ResultActions actions = mockMvc.perform(get(JUDGMENTS_PATH)
                .param(LIMIT, "2")
                .param(OFFSET, "1")
                .accept(MediaType.APPLICATION_JSON));
        //then
        checkBasicFields(actions);

        actions
                .andExpect(jsonPath("$.items.[0].division").doesNotExist())
                .andExpect(jsonPath("$.items.[0].keywords").doesNotExist())
        ;


    }


    private void checkBasicFields(ResultActions actions) throws Exception {

        actions
                .andExpect(jsonPath("$.items.[0].caseNumber").value(JC.CASE_NUMBER))
                .andExpect(jsonPath("$.items.[0].judgmentType").value(Judgment.JudgmentType.SENTENCE.name()))

                .andExpect(jsonPath("$.items.[0].source.code").value(SourceCode.COMMON_COURT.name()))
                .andExpect(jsonPath("$.items.[0].source.judgmentUrl").value(JC.SOURCE_JUDGMENT_URL))
                .andExpect(jsonPath("$.items.[0].source.judgmentId").value(JC.SOURCE_JUDGMENT_ID))
                .andExpect(jsonPath("$.items.[0].source.publisher").value(JC.SOURCE_PUBLISHER))
                .andExpect(jsonPath("$.items.[0].source.reviser").value(JC.SOURCE_REVISER))
                .andExpect(jsonPath("$.items.[0].source.publicationDate").value(new DateTime(JC.SOURCE_PUBLICATION_DATE_IN_MILLISECONDS).toString(DATE_FORMAT)))

                .andExpect(jsonPath("$.items.[0].judgmentDate").value(JC.DATE_YEAR + "-" + JC.DATE_MONTH + "-" + JC.DATE_DAY))

                .andExpect(jsonPath("$.items.[0].judges").isArray())
                .andExpect(jsonPath("$.items.[0].judges").value(iterableWithSize(3)))
                .andExpect(jsonPath("$.items.[0].judges.[0].name").value(JC.PRESIDING_JUDGE_NAME))
                .andExpect(jsonPath("$.items.[0].judges.[0].specialRoles").value(iterableWithSize(1)))
                .andExpect(jsonPath("$.items.[0].judges.[0].specialRoles.[0]").value(Judge.JudgeRole.PRESIDING_JUDGE.name()))
                .andExpect(jsonPath("$.items.[0].judges.[1].name").value(JC.SECOND_JUDGE_NAME))
                .andExpect(jsonPath("$.items.[0].judges.[1].specialRoles").value(emptyIterable()))
                .andExpect(jsonPath("$.items.[0].judges.[2].name").value(JC.THIRD_JUDGE_NAME))
                .andExpect(jsonPath("$.items.[0].judges.[2].specialRoles").value(emptyIterable()))

                .andExpect(jsonPath("$.items.[0].courtReporters").value(iterableWithSize(2)))
                .andExpect(jsonPath("$.items.[0].courtReporters.[0]").value(JC.FIRST_COURT_REPORTER))
                .andExpect(jsonPath("$.items.[0].courtReporters.[1]").value(JC.SECOND_COURT_REPORTER))

                .andExpect(jsonPath("$.items.[0].decision").value(JC.DECISION))
                .andExpect(jsonPath("$.items.[0].summary").value(JC.SUMMARY))
                .andExpect(jsonPath("$.items.[0].textContent").value(JC.TEXT_CONTENT))

                .andExpect(jsonPath("$.items.[0].reasoning.text").value(JC.REASONING_TEXT))
                .andExpect(jsonPath("$.items.[0].reasoning.judgmentUrl").value(JC.REASONING_JUDGMENT_URL))
                .andExpect(jsonPath("$.items.[0].reasoning.judgmentId").value(JC.REASONING_JUDGMENT_ID))
                .andExpect(jsonPath("$.items.[0].reasoning.publicationDate").value(new DateTime(JC.REASONING_PUBLICATION_DATE_IN_MILLISECONDS).toString(DATE_FORMAT)))
                .andExpect(jsonPath("$.items.[0].reasoning.publisher").value(JC.REASONING_PUBLISHER))
                .andExpect(jsonPath("$.items.[0].reasoning.reviser").value(JC.REASONING_REVISER))

                .andExpect(jsonPath("$.items.[0].legalBases").value(iterableWithSize(2)))
                .andExpect(jsonPath("$.items.[0].legalBases.[0]").value(JC.FIRST_LEGAL_BASE))
                .andExpect(jsonPath("$.items.[0].legalBases.[1]").value(JC.SECOND_LEGAL_BASE))

                .andExpect(jsonPath("$.items.[0].referencedRegulations").value(iterableWithSize(3)))
                .andExpect(jsonPath("$.items.[0].referencedRegulations.[0].journalTitle").value(JC.FIRST_REFERENCED_REGULATION_TITLE))
                .andExpect(jsonPath("$.items.[0].referencedRegulations.[0].journalNo").value(JC.FIRST_REFERENCED_REGULATION_JOURNAL_NO))
                .andExpect(jsonPath("$.items.[0].referencedRegulations.[0].journalEntry").value(JC.FIRST_REFERENCED_REGULATION_ENTRY))
                .andExpect(jsonPath("$.items.[0].referencedRegulations.[0].journalYear").value(JC.FIRST_REFERENCED_REGULATION_YEAR))
                .andExpect(jsonPath("$.items.[0].referencedRegulations.[0].text").value(JC.FIRST_REFERENCED_REGULATION_TEXT))

                .andExpect(jsonPath("$.items.[0].referencedRegulations.[1].journalTitle").value(JC.SECOND_REFERENCED_REGULATION_TITLE))
                .andExpect(jsonPath("$.items.[0].referencedRegulations.[1].journalNo").value(JC.SECOND_REFERENCED_REGULATION_JOURNAL_NO))
                .andExpect(jsonPath("$.items.[0].referencedRegulations.[1].journalEntry").value(JC.SECOND_REFERENCED_REGULATION_ENTRY))
                .andExpect(jsonPath("$.items.[0].referencedRegulations.[1].journalYear").value(JC.SECOND_REFERENCED_REGULATION_YEAR))
                .andExpect(jsonPath("$.items.[0].referencedRegulations.[1].text").value(JC.SECOND_REFERENCED_REGULATION_TEXT))

                .andExpect(jsonPath("$.items.[0].referencedRegulations.[2].journalTitle").value(JC.THIRD_REFERENCED_REGULATION_TITLE))
                .andExpect(jsonPath("$.items.[0].referencedRegulations.[2].journalNo").value(JC.THIRD_REFERENCED_REGULATION_JOURNAL_NO))
                .andExpect(jsonPath("$.items.[0].referencedRegulations.[2].journalEntry").value(JC.THIRD_REFERENCED_REGULATION_ENTRY))
                .andExpect(jsonPath("$.items.[0].referencedRegulations.[2].journalYear").value(JC.THIRD_REFERENCED_REGULATION_YEAR))
                .andExpect(jsonPath("$.items.[0].referencedRegulations.[2].text").value(JC.THIRD_REFERENCED_REGULATION_TEXT))
        ;
    }

    public void checkAdditionalFields(ResultActions actions) throws Exception {
        actions
                .andExpect(jsonPath("$.items.[0].division.court.code").value(JC.COURT_CODE))
                .andExpect(jsonPath("$.items.[0].division.court.name").value(JC.COURT_NAME))
                .andExpect(jsonPath("$.items.[0].division.court.type").value(JC.COURT_TYPE.name()))
                .andExpect(jsonPath("$.items.[0].division.name").value(JC.DEVISION_NAME))
                .andExpect(jsonPath("$.items.[0].division.code").value(JC.DEVISION_CODE))
                .andExpect(jsonPath("$.items.[0].division.type").value(JC.DEVISION_TYPE_NAME))

                .andExpect(jsonPath("$.items.[0].keywords.[0]").value(JC.FIRST_KEYWORD))
                .andExpect(jsonPath("$.items.[0].keywords.[1]").value(JC.SECOND_KEYWORD))
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
