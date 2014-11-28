package pl.edu.icm.saos.api.dump.judgment;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import pl.edu.icm.saos.api.ApiConstants;
import pl.edu.icm.saos.api.ApiTestConfiguration;
import pl.edu.icm.saos.api.formatter.DateTimeWithZoneFormatterFactory;
import pl.edu.icm.saos.api.search.parameters.ParametersExtractor;
import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.PersistenceTestSupport;
import pl.edu.icm.saos.persistence.common.TestObjectContext;
import pl.edu.icm.saos.persistence.common.TestObjectsFactory;
import pl.edu.icm.saos.persistence.model.CourtType;
import pl.edu.icm.saos.persistence.search.DatabaseSearchService;

import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static pl.edu.icm.saos.api.services.Constansts.DATE_FORMAT;
import static pl.edu.icm.saos.api.services.Constansts.DUMP_JUDGMENTS_PATH;
import static pl.edu.icm.saos.persistence.common.TestObjectsDefaultData.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes =  {ApiTestConfiguration.class})
@Category(SlowTest.class)
public class DumpJudgmentsControllerTest extends PersistenceTestSupport{


    @Autowired
    private ParametersExtractor parametersExtractor;

    @Autowired
    private DatabaseSearchService databaseSearchService;

    @Autowired
    private DumpJudgmentsListSuccessRepresentationBuilder dumpJudgmentsListSuccessRepresentationBuilder;

    @Autowired
    private TestObjectsFactory testObjectsFactory;

    private TestObjectContext testObjectContext;


    private MockMvc mockMvc;

    @Before
    public void setUp(){
        testObjectContext = testObjectsFactory.createTestObjectContext(true);

        DumpJudgmentsController dumpJudgmentsController = new DumpJudgmentsController();

        dumpJudgmentsController.setDatabaseSearchService(databaseSearchService);
        dumpJudgmentsController.setDumpJudgmentsListSuccessRepresentationBuilder(dumpJudgmentsListSuccessRepresentationBuilder);
        dumpJudgmentsController.setParametersExtractor(parametersExtractor);

        FormattingConversionService conversionService = new DefaultFormattingConversionService();
        conversionService.addFormatterForFieldAnnotation(new DateTimeWithZoneFormatterFactory());

        mockMvc = standaloneSetup(dumpJudgmentsController)
                .setConversionService(conversionService)
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
                .andExpect(jsonPath(pathPrefix + ".id").value(testObjectContext.getCcJudgmentId()))
                .andExpect(jsonPath(pathPrefix + ".courtType").value(CourtType.COMMON.name()))
                .andExpect(jsonPath(pathPrefix + ".courtCases").value(iterableWithSize(1)))
                .andExpect(jsonPath(pathPrefix+".courtCases.[0].caseNumber").value(CASE_NUMBER))
                .andExpect(jsonPath(pathPrefix + ".judgmentType").value(JUDGMENT_TYPE.name()))


                .andExpect(jsonPath(pathPrefix+".judgmentDate").value(DATE_YEAR + "-" + DATE_MONTH + "-" + DATE_DAY))

                .andExpect(jsonPath(pathPrefix+".judges").isArray())
                .andExpect(jsonPath(pathPrefix+".judges").value(iterableWithSize(3)))
                .andExpect(jsonPath(pathPrefix+".judges.[0].name").value(FIRST_JUDGE_NAME))
                .andExpect(jsonPath(pathPrefix+".judges.[0].specialRoles").value(iterableWithSize(1)))
                .andExpect(jsonPath(pathPrefix+".judges.[0].specialRoles.[0]").value(FIRST_JUDGE_ROLE.name()))
                .andExpect(jsonPath(pathPrefix+".judges.[1].name").value(SECOND_JUDGE_NAME))
                .andExpect(jsonPath(pathPrefix+".judges.[1].specialRoles").value(emptyIterable()))
                .andExpect(jsonPath(pathPrefix+".judges.[2].name").value(THIRD_JUDGE_NAME))
                .andExpect(jsonPath(pathPrefix+".judges.[2].specialRoles").value(emptyIterable()))


                .andExpect(jsonPath(pathPrefix+".source.code").value(SOURCE_CODE.name()))
                .andExpect(jsonPath(pathPrefix+".source.judgmentUrl").value(SOURCE_JUDGMENT_URL))
                .andExpect(jsonPath(pathPrefix+".source.judgmentId").value(CC_SOURCE_JUDGMENT_ID))
                .andExpect(jsonPath(pathPrefix+".source.publisher").value(SOURCE_PUBLISHER))
                .andExpect(jsonPath(pathPrefix+".source.reviser").value(SOURCE_REVISER))
                .andExpect(jsonPath(pathPrefix+".source.publicationDate").value(new DateTime(SOURCE_PUBLICATION_DATE_IN_MILLISECONDS).toString(DATE_FORMAT)))

                .andExpect(jsonPath(pathPrefix+".courtReporters").value(iterableWithSize(2)))
                .andExpect(jsonPath(pathPrefix+".courtReporters.[0]").value(FIRST_COURT_REPORTER))
                .andExpect(jsonPath(pathPrefix+".courtReporters.[1]").value(SECOND_COURT_REPORTER))

                .andExpect(jsonPath(pathPrefix+".decision").value(DECISION))
                .andExpect(jsonPath(pathPrefix+".summary").value(SUMMARY))
                .andExpect(jsonPath(pathPrefix+".textContent").value(TEXT_CONTENT))

                .andExpect(jsonPath(pathPrefix+".legalBases").value(iterableWithSize(2)))
                .andExpect(jsonPath(pathPrefix+".legalBases.[0]").value(FIRST_LEGAL_BASE))
                .andExpect(jsonPath(pathPrefix+".legalBases.[1]").value(SECOND_LEGAL_BASE))

                .andExpect(jsonPath(pathPrefix+".referencedRegulations").value(iterableWithSize(3)))
                .andExpect(jsonPath(pathPrefix+".referencedRegulations.[0].journalTitle").value(FIRST_REFERENCED_REGULATION_TITLE))
                .andExpect(jsonPath(pathPrefix+".referencedRegulations.[0].journalNo").value(FIRST_REFERENCED_REGULATION_JOURNAL_NO))
                .andExpect(jsonPath(pathPrefix+".referencedRegulations.[0].journalEntry").value(FIRST_REFERENCED_REGULATION_ENTRY))
                .andExpect(jsonPath(pathPrefix+".referencedRegulations.[0].journalYear").value(FIRST_REFERENCED_REGULATION_YEAR))
                .andExpect(jsonPath(pathPrefix+".referencedRegulations.[0].text").value(FIRST_REFERENCED_REGULATION_TEXT))

                .andExpect(jsonPath(pathPrefix+".referencedRegulations.[1].journalTitle").value(SECOND_REFERENCED_REGULATION_TITLE))
                .andExpect(jsonPath(pathPrefix+".referencedRegulations.[1].journalNo").value(SECOND_REFERENCED_REGULATION_JOURNAL_NO))
                .andExpect(jsonPath(pathPrefix+".referencedRegulations.[1].journalEntry").value(SECOND_REFERENCED_REGULATION_ENTRY))
                .andExpect(jsonPath(pathPrefix+".referencedRegulations.[1].journalYear").value(SECOND_REFERENCED_REGULATION_YEAR))
                .andExpect(jsonPath(pathPrefix+".referencedRegulations.[1].text").value(SECOND_REFERENCED_REGULATION_TEXT))

                .andExpect(jsonPath(pathPrefix+".referencedRegulations.[2].journalTitle").value(THIRD_REFERENCED_REGULATION_TITLE))
                .andExpect(jsonPath(pathPrefix+".referencedRegulations.[2].journalNo").value(THIRD_REFERENCED_REGULATION_JOURNAL_NO))
                .andExpect(jsonPath(pathPrefix+".referencedRegulations.[2].journalEntry").value(THIRD_REFERENCED_REGULATION_ENTRY))
                .andExpect(jsonPath(pathPrefix+".referencedRegulations.[2].journalYear").value(THIRD_REFERENCED_REGULATION_YEAR))
                .andExpect(jsonPath(pathPrefix+".referencedRegulations.[2].text").value(THIRD_REFERENCED_REGULATION_TEXT))

                .andExpect(jsonPath(pathPrefix+".keywords.[0]").value(FIRST_KEYWORD))
                .andExpect(jsonPath(pathPrefix + ".keywords.[1]").value(SECOND_KEYWORD))

                .andExpect(jsonPath(pathPrefix + ".division.id").value(testObjectContext.getCcFirstDivisionId()))
        ;


        //supreme court specific fields
        pathPrefix = "$.items.[1]";
        actions
                .andExpect(jsonPath(pathPrefix + ".id").value(testObjectContext.getScJudgmentId()))
                .andExpect(jsonPath(pathPrefix + ".courtType").value(CourtType.SUPREME.name()))
                .andExpect(jsonPath(pathPrefix + ".courtCases.[0].caseNumber").value(CASE_NUMBER))
                .andExpect(jsonPath(pathPrefix + ".personnelType").value(SC_PERSONNEL_TYPE.name()))
                .andExpect(jsonPath(pathPrefix + ".form.name").value(SC_JUDGMENT_FORM_NAME))
                .andExpect(jsonPath(pathPrefix + ".division.id").value(testObjectContext.getScFirstDivisionId()))
                .andExpect(jsonPath(pathPrefix + ".chambers.[0].id").value(testObjectContext.getScFirstChamberId()))

        ;

    }

    @Test
    public void it_should_show_request_parameters() throws Exception {
        //given
        int pageSize = 11;
        int pageNumber = 5;
        String judgmentStartDate = "2011-11-10";
        String judgmentEndDate = "2014-10-25";

        String sinceModificationDate = "2015-10-25T13:55:18.769";
        //when
        ResultActions actions = mockMvc.perform(get(DUMP_JUDGMENTS_PATH)
                .param(ApiConstants.PAGE_SIZE, String.valueOf(pageSize))
                .param(ApiConstants.PAGE_NUMBER, String.valueOf(pageNumber))
                .param(ApiConstants.JUDGMENT_START_DATE, judgmentStartDate)
                .param(ApiConstants.JUDGMENT_END_DATE, judgmentEndDate)
                .param(ApiConstants.SINCE_MODIFICATION_DATE, sinceModificationDate)
                .accept(MediaType.APPLICATION_JSON));

        //then
        actions
                .andExpect(jsonPath("$.queryTemplate.pageSize").value(pageSize))
                .andExpect(jsonPath("$.queryTemplate.pageNumber").value(pageNumber))
                .andExpect(jsonPath("$.queryTemplate.judgmentStartDate").value(judgmentStartDate))
                .andExpect(jsonPath("$.queryTemplate.judgmentEndDate").value(judgmentEndDate))
                .andExpect(jsonPath("$.queryTemplate.sinceModificationDate").value(sinceModificationDate))
        ;
    }




}