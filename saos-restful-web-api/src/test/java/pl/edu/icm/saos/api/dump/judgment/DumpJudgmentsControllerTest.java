package pl.edu.icm.saos.api.dump.judgment;

import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static pl.edu.icm.saos.api.ApiResponseAssertUtils.assertIncorrectParamNameError;
import static pl.edu.icm.saos.api.ApiResponseAssertUtils.assertIncorrectValueError;
import static pl.edu.icm.saos.api.ApiResponseAssertUtils.assertInvalidPageNumberError;
import static pl.edu.icm.saos.api.ApiResponseAssertUtils.assertInvalidPageSizeError;
import static pl.edu.icm.saos.api.ApiResponseAssertUtils.assertNegativePageNumberError;
import static pl.edu.icm.saos.api.ApiResponseAssertUtils.assertNotSupportedMediaType;
import static pl.edu.icm.saos.api.ApiResponseAssertUtils.assertNotSupportedMethod;
import static pl.edu.icm.saos.api.ApiResponseAssertUtils.assertOk;
import static pl.edu.icm.saos.api.ApiResponseAssertUtils.assertTooBigPageSizeError;
import static pl.edu.icm.saos.api.ApiResponseAssertUtils.assertTooSmallPageSizeError;
import static pl.edu.icm.saos.api.services.Constants.DATE_FORMAT;
import static pl.edu.icm.saos.api.services.Constants.DUMP_JUDGMENTS_PATH;
import static pl.edu.icm.saos.common.testcommon.IntToLongMatcher.equalsLong;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.*;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import pl.edu.icm.saos.api.ApiConstants;
import pl.edu.icm.saos.api.ApiTestSupport;
import pl.edu.icm.saos.api.formatter.DateTimeWithZoneFormatterFactory;
import pl.edu.icm.saos.api.search.parameters.ParametersExtractor;
import pl.edu.icm.saos.api.services.interceptor.AccessControlHeaderHandlerInterceptor;
import pl.edu.icm.saos.api.services.interceptor.RestrictParamsHandlerInterceptor;
import pl.edu.icm.saos.common.json.JsonFormatter;
import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.enrichment.apply.JudgmentEnrichmentDbSearchService;
import pl.edu.icm.saos.persistence.common.TestObjectContext;
import pl.edu.icm.saos.persistence.common.TestPersistenceObjectFactory;
import pl.edu.icm.saos.persistence.model.CourtType;


@Category(SlowTest.class)
public class DumpJudgmentsControllerTest extends ApiTestSupport {


    @Autowired
    private ParametersExtractor parametersExtractor;

    @Autowired
    private JudgmentEnrichmentDbSearchService judgmentEnrichmentDbSearchService;

    @Autowired
    private DumpJudgmentsListSuccessRepresentationBuilder dumpJudgmentsListSuccessRepresentationBuilder;
    
    @Autowired
    private JsonFormatter jsonFormatter;


    @Autowired
    private TestPersistenceObjectFactory testPersistenceObjectFactory;


    private TestObjectContext testObjectContext;

    private MockMvc mockMvc;


    @Before
    public void setUp(){
        testObjectContext = testPersistenceObjectFactory.createTestObjectContext();

        DumpJudgmentsController dumpJudgmentsController = new DumpJudgmentsController();

        dumpJudgmentsController.setJudgmentEnrichmentDbSearchService(judgmentEnrichmentDbSearchService);
        dumpJudgmentsController.setDumpJudgmentsListSuccessRepresentationBuilder(dumpJudgmentsListSuccessRepresentationBuilder);
        dumpJudgmentsController.setParametersExtractor(parametersExtractor);
        dumpJudgmentsController.setJsonFormatter(jsonFormatter);

        FormattingConversionService conversionService = new DefaultFormattingConversionService();
        conversionService.addFormatterForFieldAnnotation(new DateTimeWithZoneFormatterFactory());

        mockMvc = standaloneSetup(dumpJudgmentsController)
                .setConversionService(conversionService)
                .addInterceptors(new AccessControlHeaderHandlerInterceptor())
                .addInterceptors(new RestrictParamsHandlerInterceptor())
                .build();

    }

    //------------------------ TESTS --------------------------

    @Test
    public void it_should_show_all_judgments_fields_withGenerated() throws Exception {
        
        // given
        testPersistenceObjectFactory.createEnrichmentTagsForJudgment(testObjectContext.getCcJudgmentId());
        
        // execute
        ResultActions actions = mockMvc.perform(get(DUMP_JUDGMENTS_PATH).accept(MediaType.APPLICATION_JSON));

        // assert
        assertOk(actions);
        assertAllJudgmentFields(actions, true);
    }
    
    
    @Test
    public void it_should_show_all_judgments_fields_withoutGenerated() throws Exception {
        
        // given
        testPersistenceObjectFactory.createEnrichmentTagsForJudgment(testObjectContext.getCcJudgmentId());
        
        // execute
        ResultActions actions = mockMvc.perform(get(DUMP_JUDGMENTS_PATH).param(ApiConstants.WITH_GENERATED, ""+false).accept(MediaType.APPLICATION_JSON));

        // assert
        assertOk(actions);
        assertAllJudgmentFields(actions, false);
    }
    
    

    @Test
    public void it_should_show_request_parameters() throws Exception {
        //given
        int pageSize = 11;
        int pageNumber = 5;
        String judgmentStartDate = "2011-11-10";
        String judgmentEndDate = "2014-10-25";

        String sinceModificationDate = "2015-10-25T13:55:18.769";
        boolean withGenerated = true;
        
        //when
        ResultActions actions = mockMvc.perform(get(DUMP_JUDGMENTS_PATH)
                .param(ApiConstants.PAGE_SIZE, String.valueOf(pageSize))
                .param(ApiConstants.PAGE_NUMBER, String.valueOf(pageNumber))
                .param(ApiConstants.JUDGMENT_START_DATE, judgmentStartDate)
                .param(ApiConstants.JUDGMENT_END_DATE, judgmentEndDate)
                .param(ApiConstants.SINCE_MODIFICATION_DATE, sinceModificationDate)
                .param(ApiConstants.WITH_GENERATED, ""+withGenerated)
                .accept(MediaType.APPLICATION_JSON));

        //then
        assertOk(actions);
        actions
                .andExpect(jsonPath("$.queryTemplate.pageSize.value").value(pageSize))
                .andExpect(jsonPath("$.queryTemplate.pageNumber.value").value(pageNumber))
                .andExpect(jsonPath("$.queryTemplate.judgmentStartDate.value").value(judgmentStartDate))
                .andExpect(jsonPath("$.queryTemplate.judgmentEndDate.value").value(judgmentEndDate))
                .andExpect(jsonPath("$.queryTemplate.sinceModificationDate.value").value(sinceModificationDate))
                .andExpect(jsonPath("$.queryTemplate.withGenerated.value").value(withGenerated))
        ;
    }

    @Test
    public void it_should_not_allow_incorrect_start_date_format() throws Exception {
        //when
        ResultActions actions = mockMvc.perform(get(DUMP_JUDGMENTS_PATH)
                .param(ApiConstants.JUDGMENT_START_DATE, "2011-11-10 20:23")
                .accept(MediaType.APPLICATION_JSON));

        //then
        assertIncorrectValueError(actions, ApiConstants.JUDGMENT_START_DATE, "2011-11-10 20:23");
    }
    
    @Test
    public void it_should_not_allow_incorrect_end_date_format() throws Exception {
        //when
        ResultActions actions = mockMvc.perform(get(DUMP_JUDGMENTS_PATH)
                .param(ApiConstants.JUDGMENT_END_DATE, "2011-11-10 20:23")
                .accept(MediaType.APPLICATION_JSON));

        //then
        assertIncorrectValueError(actions, ApiConstants.JUDGMENT_END_DATE, "2011-11-10 20:23");
    }
    
    @Test
    public void it_should_not_allow_incorrect_since_modification_date_format() throws Exception {
        //when
        ResultActions actions = mockMvc.perform(get(DUMP_JUDGMENTS_PATH)
                .param(ApiConstants.SINCE_MODIFICATION_DATE, "2015-10-25T13:55:18") // no miliseconds
                .accept(MediaType.APPLICATION_JSON));

        //then
        assertIncorrectValueError(actions, ApiConstants.SINCE_MODIFICATION_DATE, "2015-10-25T13:55:18");
    }
    
    @Test
    public void it_should_not_allow_incorrect_with_generated_parameter() throws Exception {
        //when
        ResultActions actions = mockMvc.perform(get(DUMP_JUDGMENTS_PATH)
                .param(ApiConstants.WITH_GENERATED, "no true")
                .accept(MediaType.APPLICATION_JSON));

        //then
        assertIncorrectValueError(actions, ApiConstants.WITH_GENERATED, "no true");
    }
    
    
    @Test
    public void it_should_not_allow_incorrect_request_parameter_name() throws Exception {
        //when
        ResultActions actions = mockMvc.perform(get(DUMP_JUDGMENTS_PATH)
                .param("some_incorrect_parameter_name", "")
                .accept(MediaType.APPLICATION_JSON));

        //then
        assertIncorrectParamNameError(actions, "some_incorrect_parameter_name");
    }
    
    
    @Test
    public void it_should_not_allow_too_small_page_size() throws Exception {
        // when
        ResultActions actions = mockMvc.perform(get(DUMP_JUDGMENTS_PATH)
                .param(ApiConstants.PAGE_SIZE, String.valueOf(1))
                .accept(MediaType.APPLICATION_JSON));
        
        // then
        assertTooSmallPageSizeError(actions, 2);
    }
    
    @Test
    public void it_should_not_allow_too_big_page_size() throws Exception {
        // execute
        ResultActions actions = mockMvc.perform(get(DUMP_JUDGMENTS_PATH)
                .param(ApiConstants.PAGE_SIZE, String.valueOf(101))
                .accept(MediaType.APPLICATION_JSON));
        
        // assert
        assertTooBigPageSizeError(actions, 100);
    }
    
    @Test
    public void it_should_not_allow_invalid_page_size() throws Exception {
        // execute
        ResultActions actions = mockMvc.perform(get(DUMP_JUDGMENTS_PATH)
                .param(ApiConstants.PAGE_SIZE, "abc")
                .accept(MediaType.APPLICATION_JSON));
        
        // assert
        assertInvalidPageSizeError(actions, "abc");
    }
    
    
    @Test
    public void it_should_not_allow_invalid_page_number() throws Exception {
        // execute
        ResultActions actions = mockMvc.perform(get(DUMP_JUDGMENTS_PATH)
                .param(ApiConstants.PAGE_NUMBER, "abc")
                .accept(MediaType.APPLICATION_JSON));
        
        // assert
        assertInvalidPageNumberError(actions, "abc");
    }
    
    @Test
    public void it_should_not_allow_negative_page_number() throws Exception {
        // execute
        ResultActions actions = mockMvc.perform(get(DUMP_JUDGMENTS_PATH)
                .param(ApiConstants.PAGE_NUMBER, "-1")
                .accept(MediaType.APPLICATION_JSON));
        
        // assert
        assertNegativePageNumberError(actions);
    }
    
    @Test
    public void should_respond_in_iso8859_1_charset() throws Exception {
        // when
        ResultActions actions = mockMvc.perform(get(DUMP_JUDGMENTS_PATH)
                .accept(MediaType.APPLICATION_JSON+";charset=ISO-8859-1"));
        
        // then
        assertOk(actions, "ISO-8859-1");
    }
    
    @Test
    public void should_not_allow_not_supported_method() throws Exception {
        // execute
        ResultActions actions = mockMvc.perform(post(DUMP_JUDGMENTS_PATH)
                .accept(MediaType.APPLICATION_JSON));
        
        // assert
        assertNotSupportedMethod(actions, "POST", "GET");
    }
    
    @Test
    public void should_not_allow_not_supported_media_type() throws Exception {
        // execute
        ResultActions actions = mockMvc.perform(get(DUMP_JUDGMENTS_PATH)
                .accept(MediaType.APPLICATION_XML));
        
        // assert
        assertNotSupportedMediaType(actions, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE);
    }

    
    //------------------------ PRIVATE --------------------------
    
    
    public void assertAllJudgmentFields(ResultActions actions, boolean withGenerated) throws Exception {
        
        String pathPrefix = "$.items.[0]";
        actions
                .andExpect(jsonPath(pathPrefix + ".id").value(equalsLong(testObjectContext.getCcJudgmentId())))
                .andExpect(jsonPath(pathPrefix + ".courtType").value(CourtType.COMMON.name()))
                .andExpect(jsonPath(pathPrefix + ".courtCases").value(iterableWithSize(1)))
                .andExpect(jsonPath(pathPrefix+".courtCases.[0].caseNumber").value(CC_CASE_NUMBER))
                .andExpect(jsonPath(pathPrefix + ".judgmentType").value(CC_JUDGMENT_TYPE.name()))


                .andExpect(jsonPath(pathPrefix+".judgmentDate").value(CC_DATE_YEAR + "-" + CC_DATE_MONTH + "-" + CC_DATE_DAY))

                .andExpect(jsonPath(pathPrefix+".judges").isArray())
                .andExpect(jsonPath(pathPrefix+".judges").value(iterableWithSize(3)))
                .andExpect(jsonPath(pathPrefix+".judges.[0].name").value(CC_FIRST_JUDGE_NAME))
                .andExpect(jsonPath(pathPrefix+".judges.[0].specialRoles").value(iterableWithSize(1)))
                .andExpect(jsonPath(pathPrefix+".judges.[0].specialRoles.[0]").value(CC_FIRST_JUDGE_ROLE.name()))
                .andExpect(jsonPath(pathPrefix+".judges.[1].name").value(CC_SECOND_JUDGE_NAME))
                .andExpect(jsonPath(pathPrefix+".judges.[1].specialRoles").value(emptyIterable()))
                .andExpect(jsonPath(pathPrefix+".judges.[2].name").value(CC_THIRD_JUDGE_NAME))
                .andExpect(jsonPath(pathPrefix+".judges.[2].specialRoles").value(emptyIterable()))


                .andExpect(jsonPath(pathPrefix+".source.code").value(CC_SOURCE_CODE.name()))
                .andExpect(jsonPath(pathPrefix+".source.judgmentUrl").value(CC_SOURCE_JUDGMENT_URL))
                .andExpect(jsonPath(pathPrefix+".source.judgmentId").value(CC_SOURCE_JUDGMENT_ID))
                .andExpect(jsonPath(pathPrefix+".source.publisher").value(CC_SOURCE_PUBLISHER))
                .andExpect(jsonPath(pathPrefix+".source.reviser").value(CC_SOURCE_REVISER))
                .andExpect(jsonPath(pathPrefix+".source.publicationDate").value(new DateTime(CC_SOURCE_PUBLICATION_DATE_IN_MILLISECONDS).toString(DATE_FORMAT)))

                .andExpect(jsonPath(pathPrefix+".courtReporters").value(iterableWithSize(2)))
                .andExpect(jsonPath(pathPrefix+".courtReporters.[0]").value(CC_FIRST_COURT_REPORTER))
                .andExpect(jsonPath(pathPrefix+".courtReporters.[1]").value(CC_SECOND_COURT_REPORTER))

                .andExpect(jsonPath(pathPrefix+".decision").value(CC_DECISION))
                .andExpect(jsonPath(pathPrefix+".summary").value(CC_SUMMARY))
                .andExpect(jsonPath(pathPrefix+".textContent").value(CC_TEXT_CONTENT))

                .andExpect(jsonPath(pathPrefix+".legalBases").value(iterableWithSize(2)))
                .andExpect(jsonPath(pathPrefix+".legalBases.[0]").value(CC_FIRST_LEGAL_BASE))
                .andExpect(jsonPath(pathPrefix+".legalBases.[1]").value(CC_SECOND_LEGAL_BASE))

                .andExpect(jsonPath(pathPrefix+".keywords.[0]").value(CC_FIRST_KEYWORD))
                .andExpect(jsonPath(pathPrefix + ".keywords.[1]").value(CC_SECOND_KEYWORD))
                
                .andExpect(jsonPath(pathPrefix + ".receiptDate").value(CC_RECEIPT_DATE_YEAR + "-" + CC_RECEIPT_DATE_MONTH + "-" + CC_RECEIPT_DATE_DAY))
                .andExpect(jsonPath(pathPrefix + ".meansOfAppeal").value(CC_MEANS_OF_APPEAL))
                .andExpect(jsonPath(pathPrefix + ".judgmentResult").value(CC_JUDGMENT_RESULT))
                
                .andExpect(jsonPath(pathPrefix + ".lowerCourtJudgments", hasSize(2)))
                .andExpect(jsonPath(pathPrefix + ".lowerCourtJudgments.[0]").value(CC_FIRST_LOWER_COURT_JUDGMENT))
                .andExpect(jsonPath(pathPrefix + ".lowerCourtJudgments.[1]").value(CC_SECOND_LOWER_COURT_JUDGMENT))

                .andExpect(jsonPath(pathPrefix + ".division.id").value(equalsLong(testObjectContext.getCcFirstDivisionId())));
        
        if (withGenerated) {
                
            actions
                .andExpect(jsonPath(pathPrefix+".referencedCourtCases", hasSize(2)))
                .andExpect(jsonPath(pathPrefix+".referencedCourtCases.[0].caseNumber").value(REFERENCED_COURT_CASES_TAG_FIRST_CASE_NUMBER))
                .andExpect(jsonPath(pathPrefix+".referencedCourtCases.[0].judgmentIds", hasSize(2)))
                .andExpect(jsonPath(pathPrefix+".referencedCourtCases.[0].judgmentIds.[0]").value(equalsLong(REFERENCED_COURT_CASES_TAG_FIRST_JUDGMENT_IDS[0])))
                .andExpect(jsonPath(pathPrefix+".referencedCourtCases.[0].judgmentIds.[1]").value(equalsLong(REFERENCED_COURT_CASES_TAG_FIRST_JUDGMENT_IDS[1])))
                .andExpect(jsonPath(pathPrefix+".referencedCourtCases.[0].generated").value(true))
                
                .andExpect(jsonPath(pathPrefix+".referencedCourtCases.[1].caseNumber").value(REFERENCED_COURT_CASES_TAG_SECOND_CASE_NUMBER))
                .andExpect(jsonPath(pathPrefix+".referencedCourtCases.[1].judgmentIds", hasSize(0)))
                .andExpect(jsonPath(pathPrefix+".referencedCourtCases.[1].generated").value(true))
                
                .andExpect(jsonPath(pathPrefix+".referencedRegulations").value(iterableWithSize(4)))
                .andExpect(jsonPath(pathPrefix+".referencedRegulations.[0].journalTitle").value(CC_FIRST_REFERENCED_REGULATION_TITLE))
                .andExpect(jsonPath(pathPrefix+".referencedRegulations.[0].journalNo").value(CC_FIRST_REFERENCED_REGULATION_JOURNAL_NO))
                .andExpect(jsonPath(pathPrefix+".referencedRegulations.[0].journalEntry").value(CC_FIRST_REFERENCED_REGULATION_ENTRY))
                .andExpect(jsonPath(pathPrefix+".referencedRegulations.[0].journalYear").value(CC_FIRST_REFERENCED_REGULATION_YEAR))
                .andExpect(jsonPath(pathPrefix+".referencedRegulations.[0].text").value(CC_FIRST_REFERENCED_REGULATION_TEXT))

                .andExpect(jsonPath(pathPrefix+".referencedRegulations.[1].journalTitle").value(CC_SECOND_REFERENCED_REGULATION_TITLE))
                .andExpect(jsonPath(pathPrefix+".referencedRegulations.[1].journalNo").value(CC_SECOND_REFERENCED_REGULATION_JOURNAL_NO))
                .andExpect(jsonPath(pathPrefix+".referencedRegulations.[1].journalEntry").value(CC_SECOND_REFERENCED_REGULATION_ENTRY))
                .andExpect(jsonPath(pathPrefix+".referencedRegulations.[1].journalYear").value(CC_SECOND_REFERENCED_REGULATION_YEAR))
                .andExpect(jsonPath(pathPrefix+".referencedRegulations.[1].text").value(CC_SECOND_REFERENCED_REGULATION_TEXT))

                .andExpect(jsonPath(pathPrefix+".referencedRegulations.[2].journalTitle").value(CC_THIRD_REFERENCED_REGULATION_TITLE))
                .andExpect(jsonPath(pathPrefix+".referencedRegulations.[2].journalNo").value(CC_THIRD_REFERENCED_REGULATION_JOURNAL_NO))
                .andExpect(jsonPath(pathPrefix+".referencedRegulations.[2].journalEntry").value(CC_THIRD_REFERENCED_REGULATION_ENTRY))
                .andExpect(jsonPath(pathPrefix+".referencedRegulations.[2].journalYear").value(CC_THIRD_REFERENCED_REGULATION_YEAR))
                .andExpect(jsonPath(pathPrefix+".referencedRegulations.[2].text").value(CC_THIRD_REFERENCED_REGULATION_TEXT))
                
                .andExpect(jsonPath(pathPrefix+".referencedRegulations.[3].journalTitle").value(REFERENCED_REGULATION_TAG_JOURNAL_TITLE))
                .andExpect(jsonPath(pathPrefix+".referencedRegulations.[3].journalNo").value(REFERENCED_REGULATION_TAG_JOURNAL_NO))
                .andExpect(jsonPath(pathPrefix+".referencedRegulations.[3].journalEntry").value(REFERENCED_REGULATION_TAG_JOURNAL_ENTRY))
                .andExpect(jsonPath(pathPrefix+".referencedRegulations.[3].journalYear").value(REFERENCED_REGULATION_TAG_JOURNAL_YEAR))
                .andExpect(jsonPath(pathPrefix+".referencedRegulations.[3].text").value(REFERENCED_REGULATION_TAG_TEXT))
            ;

        } else {
            
            actions
                .andExpect(jsonPath(pathPrefix+".referencedCourtCases").doesNotExist())
                
                .andExpect(jsonPath(pathPrefix+".referencedRegulations").value(iterableWithSize(3)))
                .andExpect(jsonPath(pathPrefix+".referencedRegulations.[0].journalTitle").value(CC_FIRST_REFERENCED_REGULATION_TITLE))
                .andExpect(jsonPath(pathPrefix+".referencedRegulations.[0].journalNo").value(CC_FIRST_REFERENCED_REGULATION_JOURNAL_NO))
                .andExpect(jsonPath(pathPrefix+".referencedRegulations.[0].journalEntry").value(CC_FIRST_REFERENCED_REGULATION_ENTRY))
                .andExpect(jsonPath(pathPrefix+".referencedRegulations.[0].journalYear").value(CC_FIRST_REFERENCED_REGULATION_YEAR))
                .andExpect(jsonPath(pathPrefix+".referencedRegulations.[0].text").value(CC_FIRST_REFERENCED_REGULATION_TEXT))

                .andExpect(jsonPath(pathPrefix+".referencedRegulations.[1].journalTitle").value(CC_SECOND_REFERENCED_REGULATION_TITLE))
                .andExpect(jsonPath(pathPrefix+".referencedRegulations.[1].journalNo").value(CC_SECOND_REFERENCED_REGULATION_JOURNAL_NO))
                .andExpect(jsonPath(pathPrefix+".referencedRegulations.[1].journalEntry").value(CC_SECOND_REFERENCED_REGULATION_ENTRY))
                .andExpect(jsonPath(pathPrefix+".referencedRegulations.[1].journalYear").value(CC_SECOND_REFERENCED_REGULATION_YEAR))
                .andExpect(jsonPath(pathPrefix+".referencedRegulations.[1].text").value(CC_SECOND_REFERENCED_REGULATION_TEXT))

                .andExpect(jsonPath(pathPrefix+".referencedRegulations.[2].journalTitle").value(CC_THIRD_REFERENCED_REGULATION_TITLE))
                .andExpect(jsonPath(pathPrefix+".referencedRegulations.[2].journalNo").value(CC_THIRD_REFERENCED_REGULATION_JOURNAL_NO))
                .andExpect(jsonPath(pathPrefix+".referencedRegulations.[2].journalEntry").value(CC_THIRD_REFERENCED_REGULATION_ENTRY))
                .andExpect(jsonPath(pathPrefix+".referencedRegulations.[2].journalYear").value(CC_THIRD_REFERENCED_REGULATION_YEAR))
                .andExpect(jsonPath(pathPrefix+".referencedRegulations.[2].text").value(CC_THIRD_REFERENCED_REGULATION_TEXT))
            ;
        }
        
        
        
        //supreme court  fields
        pathPrefix = "$.items.[1]";
        actions
                .andExpect(jsonPath(pathPrefix + ".id").value(equalsLong(testObjectContext.getScJudgmentId())))
                .andExpect(jsonPath(pathPrefix + ".courtType").value(CourtType.SUPREME.name()))
                .andExpect(jsonPath(pathPrefix + ".courtCases.[0].caseNumber").value(SC_CASE_NUMBER))
                .andExpect(jsonPath(pathPrefix + ".personnelType").value(SC_PERSONNEL_TYPE.name()))
                .andExpect(jsonPath(pathPrefix + ".judgmentForm.name").value(SC_JUDGMENT_FORM_NAME))
                .andExpect(jsonPath(pathPrefix + ".division.id").value(equalsLong(testObjectContext.getScFirstDivisionId())))
                .andExpect(jsonPath(pathPrefix + ".chambers.[0].id").value(equalsLong(testObjectContext.getScFirstChamberId())))

                .andExpect(jsonPath(pathPrefix + ".judgmentDate").value(SC_DATE_YEAR + "-" + SC_DATE_MONTH + "-" + SC_DATE_DAY))

                .andExpect(jsonPath(pathPrefix+".judges").isArray())
                .andExpect(jsonPath(pathPrefix + ".judges").value(iterableWithSize(2)))
                .andExpect(jsonPath(pathPrefix+".judges.[0].name").value(SC_FIRST_JUDGE_NAME))
                .andExpect(jsonPath(pathPrefix+".judges.[0].specialRoles").value(iterableWithSize(1)))
                .andExpect(jsonPath(pathPrefix+".judges.[0].specialRoles.[0]").value(SC_FIRST_JUDGE_ROLE.name()))
                .andExpect(jsonPath(pathPrefix+".judges.[1].name").value(SC_SECOND_JUDGE_NAME))
                .andExpect(jsonPath(pathPrefix+".judges.[1].specialRoles").value(SC_SECOND_JUDGE_ROLE.name()))


                .andExpect(jsonPath(pathPrefix + ".source.code").value(SC_SOURCE_CODE.name()))
                .andExpect(jsonPath(pathPrefix + ".source.judgmentUrl").value(SC_SOURCE_JUDGMENT_URL))
                .andExpect(jsonPath(pathPrefix+".source.judgmentId").value(SC_SOURCE_JUDGMENT_ID))
                .andExpect(jsonPath(pathPrefix+".source.publisher").value(SC_SOURCE_PUBLISHER))
                .andExpect(jsonPath(pathPrefix+".source.reviser").value(SC_SOURCE_REVISER))
                .andExpect(jsonPath(pathPrefix+".source.publicationDate").value(new DateTime(SC_SOURCE_PUBLICATION_DATE_IN_MILLISECONDS).toString(DATE_FORMAT)))

                .andExpect(jsonPath(pathPrefix + ".courtReporters").value(iterableWithSize(2)))
                .andExpect(jsonPath(pathPrefix + ".courtReporters.[0]").value(SC_FIRST_COURT_REPORTER))
                .andExpect(jsonPath(pathPrefix+".courtReporters.[1]").value(SC_SECOND_COURT_REPORTER))

                .andExpect(jsonPath(pathPrefix + ".decision").value(SC_DECISION))
                .andExpect(jsonPath(pathPrefix + ".summary").value(SC_SUMMARY))
                .andExpect(jsonPath(pathPrefix+".textContent").value(SC_TEXT_CONTENT))

                .andExpect(jsonPath(pathPrefix + ".legalBases").value(iterableWithSize(2)))
                .andExpect(jsonPath(pathPrefix + ".legalBases.[0]").value(SC_FIRST_LEGAL_BASE))
                .andExpect(jsonPath(pathPrefix+".legalBases.[1]").value(SC_SECOND_LEGAL_BASE))

                .andExpect(jsonPath(pathPrefix + ".referencedRegulations").value(iterableWithSize(3)))
                .andExpect(jsonPath(pathPrefix + ".referencedRegulations.[0].journalTitle").value(SC_FIRST_REFERENCED_REGULATION_TITLE))
                .andExpect(jsonPath(pathPrefix+".referencedRegulations.[0].journalNo").value(SC_FIRST_REFERENCED_REGULATION_JOURNAL_NO))
                .andExpect(jsonPath(pathPrefix + ".referencedRegulations.[0].journalEntry").value(SC_FIRST_REFERENCED_REGULATION_ENTRY))
                .andExpect(jsonPath(pathPrefix+".referencedRegulations.[0].journalYear").value(SC_FIRST_REFERENCED_REGULATION_YEAR))
                .andExpect(jsonPath(pathPrefix+".referencedRegulations.[0].text").value(SC_FIRST_REFERENCED_REGULATION_TEXT))

                .andExpect(jsonPath(pathPrefix + ".referencedRegulations.[1].journalTitle").value(SC_SECOND_REFERENCED_REGULATION_TITLE))
                .andExpect(jsonPath(pathPrefix + ".referencedRegulations.[1].journalNo").value(SC_SECOND_REFERENCED_REGULATION_JOURNAL_NO))
                .andExpect(jsonPath(pathPrefix+".referencedRegulations.[1].journalEntry").value(SC_SECOND_REFERENCED_REGULATION_ENTRY))
                .andExpect(jsonPath(pathPrefix+".referencedRegulations.[1].journalYear").value(SC_SECOND_REFERENCED_REGULATION_YEAR))
                .andExpect(jsonPath(pathPrefix + ".referencedRegulations.[1].text").value(SC_SECOND_REFERENCED_REGULATION_TEXT))

                .andExpect(jsonPath(pathPrefix + ".referencedRegulations.[2].journalTitle").value(SC_THIRD_REFERENCED_REGULATION_TITLE))
                .andExpect(jsonPath(pathPrefix + ".referencedRegulations.[2].journalNo").value(SC_THIRD_REFERENCED_REGULATION_JOURNAL_NO))
                .andExpect(jsonPath(pathPrefix+".referencedRegulations.[2].journalEntry").value(SC_THIRD_REFERENCED_REGULATION_ENTRY))
                .andExpect(jsonPath(pathPrefix+".referencedRegulations.[2].journalYear").value(SC_THIRD_REFERENCED_REGULATION_YEAR))
                .andExpect(jsonPath(pathPrefix+".referencedRegulations.[2].text").value(SC_THIRD_REFERENCED_REGULATION_TEXT))
                
                .andExpect(jsonPath(pathPrefix + ".receiptDate").value(SC_RECEIPT_DATE_YEAR + "-" + SC_RECEIPT_DATE_MONTH + "-" + SC_RECEIPT_DATE_DAY))
                .andExpect(jsonPath(pathPrefix + ".meansOfAppeal").value(SC_MEANS_OF_APPEAL))
                .andExpect(jsonPath(pathPrefix + ".judgmentResult").value(SC_JUDGMENT_RESULT))
                
                .andExpect(jsonPath(pathPrefix + ".lowerCourtJudgments", hasSize(2)))
                .andExpect(jsonPath(pathPrefix + ".lowerCourtJudgments.[0]").value(SC_FIRST_LOWER_COURT_JUDGMENT))
                .andExpect(jsonPath(pathPrefix + ".lowerCourtJudgments.[1]").value(SC_SECOND_LOWER_COURT_JUDGMENT))
                
                .andExpect(jsonPath(pathPrefix+".referencedCourtCases").doesNotExist())
                
        ;


        //constitutional tribunal judgment fields
        pathPrefix = "$.items.[2]";
        actions
                .andExpect(jsonPath(pathPrefix + ".id").value(equalsLong(testObjectContext.getCtJudgmentId())))
                .andExpect(jsonPath(pathPrefix + ".courtType").value(CourtType.CONSTITUTIONAL_TRIBUNAL.name()))
                .andExpect(jsonPath(pathPrefix + ".courtCases.[0].caseNumber").value(CT_CASE_NUMBER))

                .andExpect(jsonPath(pathPrefix + ".judgmentDate").value(CT_DATE_YEAR + "-" + CT_DATE_MONTH + "-" + CT_DATE_DAY))

                .andExpect(jsonPath(pathPrefix + ".judges").isArray())
                .andExpect(jsonPath(pathPrefix + ".judges").value(iterableWithSize(2)))
                .andExpect(jsonPath(pathPrefix + ".judges.[0].name").value(CT_FIRST_JUDGE_NAME))
                .andExpect(jsonPath(pathPrefix + ".judges.[0].specialRoles").value(iterableWithSize(1)))
                .andExpect(jsonPath(pathPrefix + ".judges.[0].specialRoles.[0]").value(CT_FIRST_JUDGE_ROLE.name()))
                .andExpect(jsonPath(pathPrefix + ".judges.[0].function").value(CT_FIRST_JUDGE_FUNCTION))
                .andExpect(jsonPath(pathPrefix + ".judges.[1].name").value(CT_SECOND_JUDGE_NAME))


                .andExpect(jsonPath(pathPrefix + ".source.code").value(CT_SOURCE_CODE.name()))
                .andExpect(jsonPath(pathPrefix + ".source.judgmentUrl").value(CT_SOURCE_JUDGMENT_URL))
                .andExpect(jsonPath(pathPrefix + ".source.judgmentId").value(CT_SOURCE_JUDGMENT_ID))
                .andExpect(jsonPath(pathPrefix+".source.publisher").value(CT_SOURCE_PUBLISHER))
                .andExpect(jsonPath(pathPrefix + ".source.reviser").value(CT_SOURCE_REVISER))
                .andExpect(jsonPath(pathPrefix+".source.publicationDate").value(new DateTime(CT_SOURCE_PUBLICATION_DATE_IN_MILLISECONDS).toString(DATE_FORMAT)))

                .andExpect(jsonPath(pathPrefix + ".courtReporters").value(iterableWithSize(2)))
                .andExpect(jsonPath(pathPrefix + ".courtReporters.[0]").value(CT_FIRST_COURT_REPORTER))
                .andExpect(jsonPath(pathPrefix + ".courtReporters.[1]").value(CT_SECOND_COURT_REPORTER))

                .andExpect(jsonPath(pathPrefix + ".decision").value(CT_DECISION))
                .andExpect(jsonPath(pathPrefix + ".summary").value(CT_SUMMARY))
                .andExpect(jsonPath(pathPrefix + ".textContent").value(CT_TEXT_CONTENT))

                .andExpect(jsonPath(pathPrefix + ".legalBases").value(iterableWithSize(2)))
                .andExpect(jsonPath(pathPrefix + ".legalBases.[0]").value(CT_FIRST_LEGAL_BASE))
                .andExpect(jsonPath(pathPrefix + ".legalBases.[1]").value(CT_SECOND_LEGAL_BASE))

                .andExpect(jsonPath(pathPrefix + ".referencedRegulations").value(iterableWithSize(2)))
                .andExpect(jsonPath(pathPrefix + ".referencedRegulations.[0].journalTitle").value(CT_FIRST_REFERENCED_REGULATION_TITLE))
                .andExpect(jsonPath(pathPrefix + ".referencedRegulations.[0].journalNo").value(CT_FIRST_REFERENCED_REGULATION_JOURNAL_NO))
                .andExpect(jsonPath(pathPrefix + ".referencedRegulations.[0].journalEntry").value(CT_FIRST_REFERENCED_REGULATION_ENTRY))
                .andExpect(jsonPath(pathPrefix + ".referencedRegulations.[0].journalYear").value(CT_FIRST_REFERENCED_REGULATION_YEAR))
                .andExpect(jsonPath(pathPrefix+".referencedRegulations.[0].text").value(CT_FIRST_REFERENCED_REGULATION_TEXT))

                .andExpect(jsonPath(pathPrefix + ".referencedRegulations.[1].journalTitle").value(CT_SECOND_REFERENCED_REGULATION_TITLE))
                .andExpect(jsonPath(pathPrefix + ".referencedRegulations.[1].journalNo").value(CT_SECOND_REFERENCED_REGULATION_JOURNAL_NO))
                .andExpect(jsonPath(pathPrefix + ".referencedRegulations.[1].journalEntry").value(CT_SECOND_REFERENCED_REGULATION_ENTRY))
                .andExpect(jsonPath(pathPrefix+".referencedRegulations.[1].journalYear").value(CT_SECOND_REFERENCED_REGULATION_YEAR))
                .andExpect(jsonPath(pathPrefix + ".referencedRegulations.[1].text").value(CT_SECOND_REFERENCED_REGULATION_TEXT))

                .andExpect(jsonPath(pathPrefix + ".dissentingOpinions").value(iterableWithSize(2)))

                .andExpect(jsonPath(pathPrefix + ".dissentingOpinions.[0].textContent").value(CT_FIRST_DISSENTING_OPINION_TEXT))
                .andExpect(jsonPath(pathPrefix + ".dissentingOpinions.[0].authors.[0]").value(CT_FIRST_DISSENTING_OPINION_FIRST_AUTHOR))
                .andExpect(jsonPath(pathPrefix + ".dissentingOpinions.[0].authors.[1]").value(CT_FIRST_DISSENTING_OPINION_SECOND_AUTHOR))

                .andExpect(jsonPath(pathPrefix + ".dissentingOpinions.[1].textContent").value(CT_SECOND_DISSENTING_OPINION_TEXT))
                .andExpect(jsonPath(pathPrefix + ".dissentingOpinions.[1].authors.[0]").value(CT_SECOND_DISSENTING_OPINION_FIRST_AUTHOR))

                .andExpect(jsonPath(pathPrefix + ".keywords.[0]").value(CT_FIRST_KEYWORD))
                .andExpect(jsonPath(pathPrefix + ".keywords.[1]").value(CT_SECOND_KEYWORD))
                
                .andExpect(jsonPath(pathPrefix + ".receiptDate").value(CT_RECEIPT_DATE_YEAR + "-" + CT_RECEIPT_DATE_MONTH + "-" + CT_RECEIPT_DATE_DAY))
                .andExpect(jsonPath(pathPrefix + ".meansOfAppeal").value(CT_MEANS_OF_APPEAL))
                .andExpect(jsonPath(pathPrefix + ".judgmentResult").value(CT_JUDGMENT_RESULT))
                
                .andExpect(jsonPath(pathPrefix + ".lowerCourtJudgments", hasSize(2)))
                .andExpect(jsonPath(pathPrefix + ".lowerCourtJudgments.[0]").value(CT_FIRST_LOWER_COURT_JUDGMENT))
                .andExpect(jsonPath(pathPrefix + ".lowerCourtJudgments.[1]").value(CT_SECOND_LOWER_COURT_JUDGMENT))
                
                .andExpect(jsonPath(pathPrefix+".referencedCourtCases").doesNotExist())
        ;
    }



}