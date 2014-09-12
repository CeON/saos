package pl.edu.icm.saos.api.dump.judgment;

import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static pl.edu.icm.saos.api.ApiConstants.JUDGMENT_END_DATE;
import static pl.edu.icm.saos.api.ApiConstants.JUDGMENT_START_DATE;
import static pl.edu.icm.saos.api.ApiConstants.LIMIT;
import static pl.edu.icm.saos.api.ApiConstants.OFFSET;
import static pl.edu.icm.saos.api.utils.Constansts.DATE_FORMAT;
import static pl.edu.icm.saos.api.utils.Constansts.DUMP_JUDGMENTS_PATH;
import static pl.edu.icm.saos.api.utils.FieldsDefinition.createCommonJudgment;

import java.util.Arrays;

import org.joda.time.DateTime;
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
import pl.edu.icm.saos.api.parameters.ParametersExtractor;
import pl.edu.icm.saos.api.utils.FieldsDefinition;
import pl.edu.icm.saos.api.utils.FieldsDefinition.JC;
import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.SourceCode;
import pl.edu.icm.saos.persistence.search.DatabaseSearchService;
import pl.edu.icm.saos.persistence.search.dto.JudgmentSearchFilter;
import pl.edu.icm.saos.persistence.search.result.SearchResult;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes =  {DumpJudgmentsControllerTest.TestConfiguration.class})
@Category(SlowTest.class)
public class DumpJudgmentsControllerTest {


    @Configuration
    @Import(TestsConfig.class)
    public static class TestConfiguration {

        @Bean(name = "mockDatabaseSearchService")
        public DatabaseSearchService databaseSearchService(){
            SearchResult searchResult = new SearchResult<Judgment>
                    (Arrays.asList(createCommonJudgment()), 1, 0,1);

            DatabaseSearchService databaseSearchService = mock(DatabaseSearchService.class);
            when(databaseSearchService.search(any(JudgmentSearchFilter.class))).thenReturn(searchResult);

            return databaseSearchService;
        }

    }

    @Autowired
    private ParametersExtractor parametersExtractor;

    @Autowired
    @Qualifier("mockDatabaseSearchService")
    private DatabaseSearchService databaseSearchService;

    @Autowired
    private DumpJudgmentsListSuccessRepresentationBuilder dumpJudgmentsListSuccessRepresentationBuilder;


    private MockMvc mockMvc;

    @Before
    public void setUp(){
        DumpJudgmentsController dumpJudgmentsController = new DumpJudgmentsController();

        dumpJudgmentsController.setDatabaseSearchService(databaseSearchService);
        dumpJudgmentsController.setDumpJudgmentsListSuccessRepresentationBuilder(dumpJudgmentsListSuccessRepresentationBuilder);
        dumpJudgmentsController.setParametersExtractor(parametersExtractor);


        mockMvc = standaloneSetup(dumpJudgmentsController)
                .build();
    }

    @Test
    public void it_should_show_all_judgments_fields() throws Exception {
        //when
        ResultActions actions = mockMvc.perform(get(DUMP_JUDGMENTS_PATH)
                .accept(MediaType.APPLICATION_JSON));

        //then

        String pathPrefix = "$.items.[0]";
        actions
                .andExpect(jsonPath(pathPrefix + ".id").value(FieldsDefinition.JC.JUDGMENT_ID))
                .andExpect(jsonPath(pathPrefix+".courtCases").value(iterableWithSize(1)))
                .andExpect(jsonPath(pathPrefix+".courtCases.[0].caseNumber").value(FieldsDefinition.JC.CASE_NUMBER))
                .andExpect(jsonPath(pathPrefix+".judgmentType").value(Judgment.JudgmentType.SENTENCE.name()))


                .andExpect(jsonPath(pathPrefix + ".judgmentDate").value(JC.DATE_YEAR + "-" + JC.DATE_MONTH + "-" + JC.DATE_DAY))

                .andExpect(jsonPath(pathPrefix + ".judges").isArray())
                .andExpect(jsonPath(pathPrefix+".judges").value(iterableWithSize(3)))
                .andExpect(jsonPath(pathPrefix+".judges.[0].name").value(JC.PRESIDING_JUDGE_NAME))
                .andExpect(jsonPath(pathPrefix+".judges.[0].specialRoles").value(iterableWithSize(1)))
                .andExpect(jsonPath(pathPrefix + ".judges.[0].specialRoles.[0]").value(Judge.JudgeRole.PRESIDING_JUDGE.name()))
                .andExpect(jsonPath(pathPrefix+".judges.[1].name").value(JC.SECOND_JUDGE_NAME))
                .andExpect(jsonPath(pathPrefix+".judges.[1].specialRoles").value(emptyIterable()))
                .andExpect(jsonPath(pathPrefix+".judges.[2].name").value(JC.THIRD_JUDGE_NAME))
                .andExpect(jsonPath(pathPrefix+".judges.[2].specialRoles").value(emptyIterable()))


                .andExpect(jsonPath(pathPrefix + ".source.code").value(SourceCode.COMMON_COURT.name()))
                .andExpect(jsonPath(pathPrefix + ".source.judgmentUrl").value(JC.SOURCE_JUDGMENT_URL))
                .andExpect(jsonPath(pathPrefix+".source.judgmentId").value(JC.SOURCE_JUDGMENT_ID))
                .andExpect(jsonPath(pathPrefix+".source.publisher").value(JC.SOURCE_PUBLISHER))
                .andExpect(jsonPath(pathPrefix + ".source.reviser").value(JC.SOURCE_REVISER))
                .andExpect(jsonPath(pathPrefix+".source.publicationDate").value(new DateTime(JC.SOURCE_PUBLICATION_DATE_IN_MILLISECONDS).toString(DATE_FORMAT)))

                .andExpect(jsonPath(pathPrefix + ".courtReporters").value(iterableWithSize(2)))
                .andExpect(jsonPath(pathPrefix + ".courtReporters.[0]").value(JC.FIRST_COURT_REPORTER))
                .andExpect(jsonPath(pathPrefix+".courtReporters.[1]").value(JC.SECOND_COURT_REPORTER))

                .andExpect(jsonPath(pathPrefix + ".decision").value(JC.DECISION))
                .andExpect(jsonPath(pathPrefix+".summary").value(JC.SUMMARY))
                .andExpect(jsonPath(pathPrefix+".textContent").value(JC.TEXT_CONTENT))

                .andExpect(jsonPath(pathPrefix + ".reasoning.text").value(JC.REASONING_TEXT))
                .andExpect(jsonPath(pathPrefix + ".reasoning.judgmentUrl").value(JC.REASONING_JUDGMENT_URL))
                .andExpect(jsonPath(pathPrefix+".reasoning.judgmentId").value(JC.REASONING_JUDGMENT_ID))
                .andExpect(jsonPath(pathPrefix+".reasoning.publicationDate").value(new DateTime(JC.REASONING_PUBLICATION_DATE_IN_MILLISECONDS).toString(DATE_FORMAT)))
                .andExpect(jsonPath(pathPrefix + ".reasoning.publisher").value(JC.REASONING_PUBLISHER))
                .andExpect(jsonPath(pathPrefix+".reasoning.reviser").value(JC.REASONING_REVISER))

                .andExpect(jsonPath(pathPrefix + ".legalBases").value(iterableWithSize(2)))
                .andExpect(jsonPath(pathPrefix+".legalBases.[0]").value(JC.FIRST_LEGAL_BASE))
                .andExpect(jsonPath(pathPrefix+".legalBases.[1]").value(JC.SECOND_LEGAL_BASE))

                .andExpect(jsonPath(pathPrefix + ".referencedRegulations").value(iterableWithSize(3)))
                .andExpect(jsonPath(pathPrefix + ".referencedRegulations.[0].journalTitle").value(JC.FIRST_REFERENCED_REGULATION_TITLE))
                .andExpect(jsonPath(pathPrefix+".referencedRegulations.[0].journalNo").value(JC.FIRST_REFERENCED_REGULATION_JOURNAL_NO))
                .andExpect(jsonPath(pathPrefix+".referencedRegulations.[0].journalEntry").value(JC.FIRST_REFERENCED_REGULATION_ENTRY))
                .andExpect(jsonPath(pathPrefix+".referencedRegulations.[0].journalYear").value(JC.FIRST_REFERENCED_REGULATION_YEAR))
                .andExpect(jsonPath(pathPrefix+".referencedRegulations.[0].text").value(JC.FIRST_REFERENCED_REGULATION_TEXT))

                .andExpect(jsonPath(pathPrefix+".referencedRegulations.[1].journalTitle").value(JC.SECOND_REFERENCED_REGULATION_TITLE))
                .andExpect(jsonPath(pathPrefix+".referencedRegulations.[1].journalNo").value(JC.SECOND_REFERENCED_REGULATION_JOURNAL_NO))
                .andExpect(jsonPath(pathPrefix+".referencedRegulations.[1].journalEntry").value(JC.SECOND_REFERENCED_REGULATION_ENTRY))
                .andExpect(jsonPath(pathPrefix+".referencedRegulations.[1].journalYear").value(JC.SECOND_REFERENCED_REGULATION_YEAR))
                .andExpect(jsonPath(pathPrefix+".referencedRegulations.[1].text").value(JC.SECOND_REFERENCED_REGULATION_TEXT))

                .andExpect(jsonPath(pathPrefix+".referencedRegulations.[2].journalTitle").value(JC.THIRD_REFERENCED_REGULATION_TITLE))
                .andExpect(jsonPath(pathPrefix+".referencedRegulations.[2].journalNo").value(JC.THIRD_REFERENCED_REGULATION_JOURNAL_NO))
                .andExpect(jsonPath(pathPrefix + ".referencedRegulations.[2].journalEntry").value(JC.THIRD_REFERENCED_REGULATION_ENTRY))
                .andExpect(jsonPath(pathPrefix + ".referencedRegulations.[2].journalYear").value(JC.THIRD_REFERENCED_REGULATION_YEAR))
                .andExpect(jsonPath(pathPrefix+".referencedRegulations.[2].text").value(JC.THIRD_REFERENCED_REGULATION_TEXT))

                .andExpect(jsonPath(pathPrefix + ".keywords.[0]").value(JC.FIRST_KEYWORD))
                .andExpect(jsonPath(pathPrefix+".keywords.[1]").value(JC.SECOND_KEYWORD))

                .andExpect(jsonPath(pathPrefix + ".division.id").value(JC.DIVISION_ID))
        ;
    }

    @Test
    public void it_should_show_request_parameters() throws Exception {
        //given
        int limit = 11;
        int offset = 5;
        String judgmentStartDate = "2011-11-10";
        String judgmentEndDate = "2014-10-25";

        //when
        ResultActions actions = mockMvc.perform(get(DUMP_JUDGMENTS_PATH)
                .param(LIMIT, String.valueOf(limit))
                .param(OFFSET, String.valueOf(offset))
                .param(JUDGMENT_START_DATE, judgmentStartDate)
                .param(JUDGMENT_END_DATE, judgmentEndDate)
                .accept(MediaType.APPLICATION_JSON));

        //then
        actions
                .andExpect(jsonPath("$.queryTemplate.limit").value(limit))
                .andExpect(jsonPath("$.queryTemplate.offset").value(offset))
                .andExpect(jsonPath("$.queryTemplate.judgmentStartDate").value(judgmentStartDate))
                .andExpect(jsonPath("$.queryTemplate.judgmentEndDate").value(judgmentEndDate))
        ;
    }




}