package pl.edu.icm.saos.api.judgments;

import org.joda.time.DateTime;
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
import pl.edu.icm.saos.persistence.model.SourceCode;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;

import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static pl.edu.icm.saos.api.utils.Constansts.*;
import static pl.edu.icm.saos.api.utils.FieldsDefinition.*;
import static pl.edu.icm.saos.api.judgments.JudgmentRepresentationVerifier.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes =  {JudgmentControllerTest.TestConfiguration.class, ApiConfiguration.class})
@Category(SlowTest.class)
public class JudgmentControllerTest {

    private static final String JUDGMENT_PATH = JUDGMENTS_PATH+"/"+JC.JUDGMENT_ID;
    private static final String DIVISION_PATH = DIVISIONS_PATH+"/"+JC.DIVISION_ID;
    private static final String COURT_PATH = COURTS_PATH+"/"+JC.COURT_ID;
    private static final String PARENT_COURT_PATH = COURTS_PATH+"/"+JC.COURT_PARENT_ID;

    @Configuration
    public static class TestConfiguration {

        @Bean(name = "mockJudgmentRepository")
        public JudgmentRepository judgmentRepository(){

            JudgmentRepository judgmentRepository = mock(JudgmentRepository.class);
            when(judgmentRepository.findOne(JC.JUDGMENT_ID)).thenReturn(createCommonJudgment());

            return judgmentRepository;

        }

    }


    @Autowired
    private SingleJudgmentSuccessRepresentationBuilder singleJudgmentSuccessRepresentationBuilder;

    @Autowired
    @Qualifier("mockJudgmentRepository")
    private JudgmentRepository judgmentRepository;


    private MockMvc mockMvc;

    @Before
    public void setUp(){
        JudgmentController judgmentController = new JudgmentController();

        judgmentController.setSingleJudgmentSuccessRepresentationBuilder(singleJudgmentSuccessRepresentationBuilder);
        judgmentController.setJudgmentRepository(judgmentRepository);

        mockMvc = standaloneSetup(judgmentController)
                .build();
    }

    @Test
    public void itShouldShowAllJudgmentsFields() throws Exception {
        //when
        ResultActions actions = mockMvc.perform(get(JUDGMENT_PATH)
                .accept(MediaType.APPLICATION_JSON));

        //then
        verifyBasicFields(actions, "$.data");

        actions
                .andExpect(jsonPath("$.data.source.code").value(SourceCode.COMMON_COURT.name()))
                .andExpect(jsonPath("$.data.source.judgmentUrl").value(JC.SOURCE_JUDGMENT_URL))
                .andExpect(jsonPath("$.data.source.judgmentId").value(JC.SOURCE_JUDGMENT_ID))
                .andExpect(jsonPath("$.data.source.publisher").value(JC.SOURCE_PUBLISHER))
                .andExpect(jsonPath("$.data.source.reviser").value(JC.SOURCE_REVISER))
                .andExpect(jsonPath("$.data.source.publicationDate").value(new DateTime(JC.SOURCE_PUBLICATION_DATE_IN_MILLISECONDS).toString(DATE_FORMAT)))

                .andExpect(jsonPath("$.data.courtReporters").value(iterableWithSize(2)))
                .andExpect(jsonPath("$.data.courtReporters.[0]").value(JC.FIRST_COURT_REPORTER))
                .andExpect(jsonPath("$.data.courtReporters.[1]").value(JC.SECOND_COURT_REPORTER))

                .andExpect(jsonPath("$.data.decision").value(JC.DECISION))
                .andExpect(jsonPath("$.data.summary").value(JC.SUMMARY))
                .andExpect(jsonPath("$.data.textContent").value(JC.TEXT_CONTENT))

                .andExpect(jsonPath("$.data.reasoning.text").value(JC.REASONING_TEXT))
                .andExpect(jsonPath("$.data.reasoning.judgmentUrl").value(JC.REASONING_JUDGMENT_URL))
                .andExpect(jsonPath("$.data.reasoning.judgmentId").value(JC.REASONING_JUDGMENT_ID))
                .andExpect(jsonPath("$.data.reasoning.publicationDate").value(new DateTime(JC.REASONING_PUBLICATION_DATE_IN_MILLISECONDS).toString(DATE_FORMAT)))
                .andExpect(jsonPath("$.data.reasoning.publisher").value(JC.REASONING_PUBLISHER))
                .andExpect(jsonPath("$.data.reasoning.reviser").value(JC.REASONING_REVISER))

                .andExpect(jsonPath("$.data.legalBases").value(iterableWithSize(2)))
                .andExpect(jsonPath("$.data.legalBases.[0]").value(JC.FIRST_LEGAL_BASE))
                .andExpect(jsonPath("$.data.legalBases.[1]").value(JC.SECOND_LEGAL_BASE))

                .andExpect(jsonPath("$.data.referencedRegulations").value(iterableWithSize(3)))
                .andExpect(jsonPath("$.data.referencedRegulations.[0].journalTitle").value(JC.FIRST_REFERENCED_REGULATION_TITLE))
                .andExpect(jsonPath("$.data.referencedRegulations.[0].journalNo").value(JC.FIRST_REFERENCED_REGULATION_JOURNAL_NO))
                .andExpect(jsonPath("$.data.referencedRegulations.[0].journalEntry").value(JC.FIRST_REFERENCED_REGULATION_ENTRY))
                .andExpect(jsonPath("$.data.referencedRegulations.[0].journalYear").value(JC.FIRST_REFERENCED_REGULATION_YEAR))
                .andExpect(jsonPath("$.data.referencedRegulations.[0].text").value(JC.FIRST_REFERENCED_REGULATION_TEXT))

                .andExpect(jsonPath("$.data.referencedRegulations.[1].journalTitle").value(JC.SECOND_REFERENCED_REGULATION_TITLE))
                .andExpect(jsonPath("$.data.referencedRegulations.[1].journalNo").value(JC.SECOND_REFERENCED_REGULATION_JOURNAL_NO))
                .andExpect(jsonPath("$.data.referencedRegulations.[1].journalEntry").value(JC.SECOND_REFERENCED_REGULATION_ENTRY))
                .andExpect(jsonPath("$.data.referencedRegulations.[1].journalYear").value(JC.SECOND_REFERENCED_REGULATION_YEAR))
                .andExpect(jsonPath("$.data.referencedRegulations.[1].text").value(JC.SECOND_REFERENCED_REGULATION_TEXT))

                .andExpect(jsonPath("$.data.referencedRegulations.[2].journalTitle").value(JC.THIRD_REFERENCED_REGULATION_TITLE))
                .andExpect(jsonPath("$.data.referencedRegulations.[2].journalNo").value(JC.THIRD_REFERENCED_REGULATION_JOURNAL_NO))
                .andExpect(jsonPath("$.data.referencedRegulations.[2].journalEntry").value(JC.THIRD_REFERENCED_REGULATION_ENTRY))
                .andExpect(jsonPath("$.data.referencedRegulations.[2].journalYear").value(JC.THIRD_REFERENCED_REGULATION_YEAR))
                .andExpect(jsonPath("$.data.referencedRegulations.[2].text").value(JC.THIRD_REFERENCED_REGULATION_TEXT))

                .andExpect(jsonPath("$.data.keywords.[0]").value(JC.FIRST_KEYWORD))
                .andExpect(jsonPath("$.data.keywords.[1]").value(JC.SECOND_KEYWORD))

                .andExpect(jsonPath("$.data.division.href").value(endsWith(DIVISION_PATH)))
                .andExpect(jsonPath("$.data.division.name").value(JC.DIVISION_NAME))
                .andExpect(jsonPath("$.data.division.code").value(JC.DIVISION_CODE))
                .andExpect(jsonPath("$.data.division.type").value(JC.DIVISION_TYPE_NAME))

                .andExpect(jsonPath("$.data.division.court.href").value(endsWith(COURT_PATH)))
                .andExpect(jsonPath("$.data.division.court.code").value(JC.COURT_CODE))
                .andExpect(jsonPath("$.data.division.court.name").value(JC.COURT_NAME))
                .andExpect(jsonPath("$.data.division.court.type").value(JC.COURT_TYPE.name()))

                .andExpect(jsonPath("$.data.division.court.parentCourt.href").value(endsWith(PARENT_COURT_PATH)))
                .andExpect(jsonPath("$.data.division.court.parentCourt.name").value(endsWith(JC.COURT_PARENT_NAME)))
                ;
    }

    @Test
    public void itShouldShowLinks() throws Exception {
        //when
        ResultActions actions = mockMvc.perform(get(JUDGMENT_PATH)
                .accept(MediaType.APPLICATION_JSON));

        //then
        actions
                .andExpect(jsonPath("$.links").isArray())
                .andExpect(jsonPath("$.links[?(@.rel==self)].href[0]").value(endsWith(JUDGMENT_PATH)))
                ;
    }



}