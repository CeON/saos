package pl.edu.icm.saos.api.single.judgment;

import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static pl.edu.icm.saos.api.ApiResponseAssertUtils.assertNotFoundError;
import static pl.edu.icm.saos.api.ApiResponseAssertUtils.assertOk;
import static pl.edu.icm.saos.api.services.Constants.DATE_FORMAT;
import static pl.edu.icm.saos.api.services.Constants.SINGLE_COURTS_PATH;
import static pl.edu.icm.saos.api.services.Constants.SINGLE_DIVISIONS_PATH;
import static pl.edu.icm.saos.api.services.Constants.SINGLE_JUDGMENTS_PATH;
import static pl.edu.icm.saos.common.testcommon.IntToLongMatcher.equalsLong;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CASE_NUMBER;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_COURT_CODE;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_COURT_NAME;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_COURT_TYPE;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_DATE_DAY;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_DATE_MONTH;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_DATE_YEAR;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_DECISION;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_FIRST_COURT_REPORTER;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_FIRST_DIVISION_CODE;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_FIRST_DIVISION_NAME;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_FIRST_DIVISION_TYPE_NAME;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_FIRST_JUDGE_FUNCTION;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_FIRST_JUDGE_NAME;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_FIRST_JUDGE_ROLE;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_FIRST_KEYWORD;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_FIRST_LEGAL_BASE;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_FIRST_LOWER_COURT_JUDGMENT;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_FIRST_REFERENCED_REGULATION_ENTRY;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_FIRST_REFERENCED_REGULATION_JOURNAL_NO;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_FIRST_REFERENCED_REGULATION_TEXT;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_FIRST_REFERENCED_REGULATION_TITLE;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_FIRST_REFERENCED_REGULATION_YEAR;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_JUDGMENT_RESULT;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_JUDGMENT_TYPE;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_MEANS_OF_APPEAL;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_RECEIPT_DATE_DAY;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_RECEIPT_DATE_MONTH;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_RECEIPT_DATE_YEAR;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_SECOND_COURT_REPORTER;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_SECOND_JUDGE_NAME;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_SECOND_KEYWORD;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_SECOND_LEGAL_BASE;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_SECOND_LOWER_COURT_JUDGMENT;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_SECOND_REFERENCED_REGULATION_ENTRY;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_SECOND_REFERENCED_REGULATION_JOURNAL_NO;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_SECOND_REFERENCED_REGULATION_TEXT;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_SECOND_REFERENCED_REGULATION_TITLE;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_SECOND_REFERENCED_REGULATION_YEAR;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_SOURCE_JUDGMENT_ID;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_SOURCE_JUDGMENT_URL;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_SOURCE_PUBLICATION_DATE_IN_MILLISECONDS;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_SOURCE_PUBLISHER;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_SOURCE_REVISER;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_SUMMARY;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_TEXT_CONTENT;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_THIRD_JUDGE_NAME;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_THIRD_REFERENCED_REGULATION_ENTRY;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_THIRD_REFERENCED_REGULATION_JOURNAL_NO;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_THIRD_REFERENCED_REGULATION_TEXT;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_THIRD_REFERENCED_REGULATION_TITLE;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_THIRD_REFERENCED_REGULATION_YEAR;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CT_FIRST_DISSENTING_OPINION_FIRST_AUTHOR;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CT_FIRST_DISSENTING_OPINION_SECOND_AUTHOR;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CT_FIRST_DISSENTING_OPINION_TEXT;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CT_SECOND_DISSENTING_OPINION_FIRST_AUTHOR;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CT_SECOND_DISSENTING_OPINION_TEXT;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.REFERENCED_COURT_CASES_TAG_FIRST_CASE_NUMBER;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.REFERENCED_COURT_CASES_TAG_FIRST_JUDGMENT_IDS;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.REFERENCED_COURT_CASES_TAG_SECOND_CASE_NUMBER;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.SC_CHAMBER_NAME;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.SC_FIRST_CHAMBER_NAME;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.SC_FIRST_DIVISION_NAME;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.SC_JUDGMENT_FORM_NAME;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.SC_PERSONNEL_TYPE;

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

import pl.edu.icm.saos.api.ApiTestConfiguration;
import pl.edu.icm.saos.api.services.interceptor.AccessControlHeaderHandlerInterceptor;
import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.enrichment.apply.JudgmentEnrichmentService;
import pl.edu.icm.saos.persistence.PersistenceTestSupport;
import pl.edu.icm.saos.persistence.common.TestObjectContext;
import pl.edu.icm.saos.persistence.common.TestPersistenceObjectFactory;
import pl.edu.icm.saos.persistence.model.CourtType;
import pl.edu.icm.saos.persistence.model.SourceCode;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes =  {ApiTestConfiguration.class})
@Category(SlowTest.class)
public class JudgmentControllerTest extends PersistenceTestSupport {

    @Autowired
    private SingleJudgmentSuccessRepresentationBuilder singleJudgmentSuccessRepresentationBuilder;

    @Autowired
    private JudgmentEnrichmentService judgmentEnrichmentService;

    @Autowired
    private TestPersistenceObjectFactory testPersistenceObjectFactory;

    private TestObjectContext testObjectContext;


    private MockMvc mockMvc;

    private String ccJudgmentPath;
    private String scJudgmentPath;
    private String ctJudgmentPath;
    private String nacJudgmentPath;
    
    private long notExistingJudgmentId;
    private String notExistingJudgmentPath;

    @Before
    public void setUp(){
        testObjectContext = testPersistenceObjectFactory.createTestObjectContext();
        ccJudgmentPath = SINGLE_JUDGMENTS_PATH + "/" + testObjectContext.getCcJudgmentId();
        scJudgmentPath = SINGLE_JUDGMENTS_PATH + "/" + testObjectContext.getScJudgmentId();
        ctJudgmentPath = SINGLE_JUDGMENTS_PATH + "/" + testObjectContext.getCtJudgmentId();
        nacJudgmentPath = SINGLE_JUDGMENTS_PATH + "/" + testObjectContext.getNacJudgmentId();
        
        notExistingJudgmentId = testObjectContext.getCcJudgmentId() + testObjectContext.getScJudgmentId()
                + testObjectContext.getCtJudgmentId() + testObjectContext.getNacJudgmentId();
        notExistingJudgmentPath = SINGLE_JUDGMENTS_PATH + "/" + notExistingJudgmentId;

        JudgmentController judgmentController = new JudgmentController();

        judgmentController.setSingleJudgmentSuccessRepresentationBuilder(singleJudgmentSuccessRepresentationBuilder);
        judgmentController.setJudgmentEnrichmentService(judgmentEnrichmentService);

        mockMvc = standaloneSetup(judgmentController)
                .addInterceptors(new AccessControlHeaderHandlerInterceptor())
                .build();
    }


    //------------------------ TESTS --------------------------

    @Test
    public void it_should_show_all_judgment_basic_fields() throws Exception {
        
        // given
        testPersistenceObjectFactory.createEnrichmentTagsForJudgment(testObjectContext.getCcJudgmentId());
        
        // when
        ResultActions actions = mockMvc.perform(get(ccJudgmentPath)
                .accept(MediaType.APPLICATION_JSON));

        // then
        assertOk(actions);
        actions
                .andExpect(jsonPath("$.data.href").value(endsWith(SINGLE_JUDGMENTS_PATH + "/" + testObjectContext.getCcJudgmentId())))
                .andExpect(jsonPath("$.data.courtCases").value(iterableWithSize(1)))
                .andExpect(jsonPath("$.data.courtCases.[0].caseNumber").value(CASE_NUMBER))
                .andExpect(jsonPath("$.data.judgmentType").value(CC_JUDGMENT_TYPE.name()))

                .andExpect(jsonPath("$.data.judgmentDate").value(CC_DATE_YEAR + "-" + CC_DATE_MONTH + "-" + CC_DATE_DAY))

                .andExpect(jsonPath("$.data.judges").isArray())
                .andExpect(jsonPath("$.data.judges").value(iterableWithSize(3)))
                .andExpect(jsonPath("$.data.judges.[0].name").value(CC_FIRST_JUDGE_NAME))
                .andExpect(jsonPath("$.data.judges.[0].function").value(CC_FIRST_JUDGE_FUNCTION))
                .andExpect(jsonPath("$.data.judges.[0].specialRoles").value(iterableWithSize(1)))
                .andExpect(jsonPath("$.data.judges.[0].specialRoles.[0]").value(CC_FIRST_JUDGE_ROLE.name()))
                .andExpect(jsonPath("$.data.judges.[1].name").value(CC_SECOND_JUDGE_NAME))
                .andExpect(jsonPath("$.data.judges.[1].specialRoles").value(emptyIterable()))
                .andExpect(jsonPath("$.data.judges.[2].name").value(CC_THIRD_JUDGE_NAME))
                .andExpect(jsonPath("$.data.judges.[2].specialRoles").value(emptyIterable()))
                
                .andExpect(jsonPath("$.data.receiptDate").value(CC_RECEIPT_DATE_YEAR + "-" + CC_RECEIPT_DATE_MONTH + "-" + CC_RECEIPT_DATE_DAY))
                .andExpect(jsonPath("$.data.meansOfAppeal").value(CC_MEANS_OF_APPEAL))
                .andExpect(jsonPath("$.data.judgmentResult").value(CC_JUDGMENT_RESULT))
                
                .andExpect(jsonPath("$.data.lowerCourtJudgments", hasSize(2)))
                .andExpect(jsonPath("$.data.lowerCourtJudgments.[0]").value(CC_FIRST_LOWER_COURT_JUDGMENT))
                .andExpect(jsonPath("$.data.lowerCourtJudgments.[1]").value(CC_SECOND_LOWER_COURT_JUDGMENT))
                
                .andExpect(jsonPath("$.data.referencedCourtCases", hasSize(2)))
                .andExpect(jsonPath("$.data.referencedCourtCases.[0].caseNumber").value(REFERENCED_COURT_CASES_TAG_FIRST_CASE_NUMBER))
                .andExpect(jsonPath("$.data.referencedCourtCases.[0].judgmentIds", hasSize(2)))
                .andExpect(jsonPath("$.data.referencedCourtCases.[0].judgmentIds.[0]").value(equalsLong(REFERENCED_COURT_CASES_TAG_FIRST_JUDGMENT_IDS[0])))
                .andExpect(jsonPath("$.data.referencedCourtCases.[0].judgmentIds.[1]").value(equalsLong(REFERENCED_COURT_CASES_TAG_FIRST_JUDGMENT_IDS[1])))
                .andExpect(jsonPath("$.data.referencedCourtCases.[0].generated").value(true))
                
                .andExpect(jsonPath("$.data.referencedCourtCases.[1].caseNumber").value(REFERENCED_COURT_CASES_TAG_SECOND_CASE_NUMBER))
                .andExpect(jsonPath("$.data.referencedCourtCases.[1].judgmentIds", hasSize(0)))
                .andExpect(jsonPath("$.data.referencedCourtCases.[1].generated").value(true))
        ;

    }

    @Test
    public void it_should_show_all_ccJudgments_fields() throws Exception {
        //when
        ResultActions actions = mockMvc.perform(get(ccJudgmentPath)
                .accept(MediaType.APPLICATION_JSON + ";charset=UTF-8"));

        //then
        assertOk(actions);
        actions
                .andExpect(jsonPath("$.data.id").value(equalsLong(testObjectContext.getCcJudgmentId())))
                .andExpect(jsonPath("$.data.courtType").value(CourtType.COMMON.name()))
                .andExpect(jsonPath("$.data.href").value(endsWith(ccJudgmentPath)))
                .andExpect(jsonPath("$.data.source.code").value(SourceCode.COMMON_COURT.name()))
                .andExpect(jsonPath("$.data.source.judgmentUrl").value(CC_SOURCE_JUDGMENT_URL))
                .andExpect(jsonPath("$.data.source.judgmentId").value(CC_SOURCE_JUDGMENT_ID))
                .andExpect(jsonPath("$.data.source.publisher").value(CC_SOURCE_PUBLISHER))
                .andExpect(jsonPath("$.data.source.reviser").value(CC_SOURCE_REVISER))
                .andExpect(jsonPath("$.data.source.publicationDate").value(new DateTime(CC_SOURCE_PUBLICATION_DATE_IN_MILLISECONDS).toString(DATE_FORMAT)))

                .andExpect(jsonPath("$.data.courtReporters").value(iterableWithSize(2)))
                .andExpect(jsonPath("$.data.courtReporters.[0]").value(CC_FIRST_COURT_REPORTER))
                .andExpect(jsonPath("$.data.courtReporters.[1]").value(CC_SECOND_COURT_REPORTER))

                .andExpect(jsonPath("$.data.decision").value(CC_DECISION))
                .andExpect(jsonPath("$.data.summary").value(CC_SUMMARY))
                .andExpect(jsonPath("$.data.textContent").value(CC_TEXT_CONTENT))

                .andExpect(jsonPath("$.data.legalBases").value(iterableWithSize(2)))
                .andExpect(jsonPath("$.data.legalBases.[0]").value(CC_FIRST_LEGAL_BASE))
                .andExpect(jsonPath("$.data.legalBases.[1]").value(CC_SECOND_LEGAL_BASE))

                .andExpect(jsonPath("$.data.referencedRegulations").value(iterableWithSize(3)))
                .andExpect(jsonPath("$.data.referencedRegulations.[0].journalTitle").value(CC_FIRST_REFERENCED_REGULATION_TITLE))
                .andExpect(jsonPath("$.data.referencedRegulations.[0].journalNo").value(CC_FIRST_REFERENCED_REGULATION_JOURNAL_NO))
                .andExpect(jsonPath("$.data.referencedRegulations.[0].journalEntry").value(CC_FIRST_REFERENCED_REGULATION_ENTRY))
                .andExpect(jsonPath("$.data.referencedRegulations.[0].journalYear").value(CC_FIRST_REFERENCED_REGULATION_YEAR))
                .andExpect(jsonPath("$.data.referencedRegulations.[0].text").value(CC_FIRST_REFERENCED_REGULATION_TEXT))

                .andExpect(jsonPath("$.data.referencedRegulations.[1].journalTitle").value(CC_SECOND_REFERENCED_REGULATION_TITLE))
                .andExpect(jsonPath("$.data.referencedRegulations.[1].journalNo").value(CC_SECOND_REFERENCED_REGULATION_JOURNAL_NO))
                .andExpect(jsonPath("$.data.referencedRegulations.[1].journalEntry").value(CC_SECOND_REFERENCED_REGULATION_ENTRY))
                .andExpect(jsonPath("$.data.referencedRegulations.[1].journalYear").value(CC_SECOND_REFERENCED_REGULATION_YEAR))
                .andExpect(jsonPath("$.data.referencedRegulations.[1].text").value(CC_SECOND_REFERENCED_REGULATION_TEXT))

                .andExpect(jsonPath("$.data.referencedRegulations.[2].journalTitle").value(CC_THIRD_REFERENCED_REGULATION_TITLE))
                .andExpect(jsonPath("$.data.referencedRegulations.[2].journalNo").value(CC_THIRD_REFERENCED_REGULATION_JOURNAL_NO))
                .andExpect(jsonPath("$.data.referencedRegulations.[2].journalEntry").value(CC_THIRD_REFERENCED_REGULATION_ENTRY))
                .andExpect(jsonPath("$.data.referencedRegulations.[2].journalYear").value(CC_THIRD_REFERENCED_REGULATION_YEAR))
                .andExpect(jsonPath("$.data.referencedRegulations.[2].text").value(CC_THIRD_REFERENCED_REGULATION_TEXT))

                .andExpect(jsonPath("$.data.keywords.[0]").value(CC_FIRST_KEYWORD))
                .andExpect(jsonPath("$.data.keywords.[1]").value(CC_SECOND_KEYWORD))

                .andExpect(jsonPath("$.data.division.href").value(endsWith(SINGLE_DIVISIONS_PATH +"/"+testObjectContext.getCcFirstDivisionId())))
                .andExpect(jsonPath("$.data.division.id").value(equalsLong(testObjectContext.getCcFirstDivisionId())))
                .andExpect(jsonPath("$.data.division.name").value(CC_FIRST_DIVISION_NAME))
                .andExpect(jsonPath("$.data.division.code").value(CC_FIRST_DIVISION_CODE))
                .andExpect(jsonPath("$.data.division.type").value(CC_FIRST_DIVISION_TYPE_NAME))

                .andExpect(jsonPath("$.data.division.court.href").value(endsWith(SINGLE_COURTS_PATH+"/"+testObjectContext.getCcCourtId())))
                .andExpect(jsonPath("$.data.division.court.id").value(equalsLong(testObjectContext.getCcCourtId())))
                .andExpect(jsonPath("$.data.division.court.code").value(CC_COURT_CODE))
                .andExpect(jsonPath("$.data.division.court.name").value(CC_COURT_NAME))
                .andExpect(jsonPath("$.data.division.court.type").value(CC_COURT_TYPE.name()))

                .andExpect(jsonPath("$.data.chambers").doesNotExist())
                .andExpect(jsonPath("$.data.dissentingOpinions").doesNotExist())
                ;
    }

    @Test
    public void it_should_show_all_scJudgments_fields() throws Exception {
        //when
        ResultActions actions = mockMvc.perform(get(scJudgmentPath)
                .accept(MediaType.APPLICATION_JSON));

        //then
        assertOk(actions);
        actions
                .andExpect(jsonPath("$.data.id").value(equalsLong(testObjectContext.getScJudgmentId())))
                .andExpect(jsonPath("$.data.courtType").value(CourtType.SUPREME.name()))
                .andExpect(jsonPath("$.data.href").value(endsWith(scJudgmentPath)))
                .andExpect(jsonPath("$.data.personnelType").value(SC_PERSONNEL_TYPE.name()))

                .andExpect(jsonPath("$.data.judgmentForm.name").value(SC_JUDGMENT_FORM_NAME))

                .andExpect(jsonPath("$.data.division.href").value(endsWith("/api/scDivisions/" + testObjectContext.getScFirstDivisionId())))
                .andExpect(jsonPath("$.data.division.id").value(equalsLong(testObjectContext.getScFirstDivisionId())))
                .andExpect(jsonPath("$.data.division.name").value(SC_FIRST_DIVISION_NAME))
                .andExpect(jsonPath("$.data.division.chamber.href").value(endsWith("/api/scChambers/" + testObjectContext.getScChamberId())))
                .andExpect(jsonPath("$.data.division.chamber.id").value(equalsLong(testObjectContext.getScChamberId())))
                .andExpect(jsonPath("$.data.division.chamber.name").value(SC_CHAMBER_NAME))

                .andExpect(jsonPath("$.data.chambers.[0].href").value(endsWith("/api/scChambers/" + testObjectContext.getScFirstChamberId())))
                .andExpect(jsonPath("$.data.chambers.[0].id").value(equalsLong(testObjectContext.getScFirstChamberId())))
                .andExpect(jsonPath("$.data.chambers.[0].name").value(SC_FIRST_CHAMBER_NAME))

                .andExpect(jsonPath("$.data.dissentingOpinions").doesNotExist())
                ;
    }

    @Test
    public void it_should_show_all_ctJudgments_fields() throws Exception {
        //when
        ResultActions actions = mockMvc.perform(get(ctJudgmentPath)
                .accept(MediaType.APPLICATION_JSON));

        //then
        assertOk(actions);
        actions
                .andExpect(jsonPath("$.data.id").value(equalsLong(testObjectContext.getCtJudgmentId())))
                .andExpect(jsonPath("$.data.courtType").value(CourtType.CONSTITUTIONAL_TRIBUNAL.name()))
                .andExpect(jsonPath("$.data.href").value(endsWith(ctJudgmentPath)))

                .andExpect(jsonPath("$.data.dissentingOpinions").value(iterableWithSize(2)))

                .andExpect(jsonPath("$.data.dissentingOpinions.[0].textContent").value(CT_FIRST_DISSENTING_OPINION_TEXT))
                .andExpect(jsonPath("$.data.dissentingOpinions.[0].authors.[0]").value(CT_FIRST_DISSENTING_OPINION_FIRST_AUTHOR))
                .andExpect(jsonPath("$.data.dissentingOpinions.[0].authors.[1]").value(CT_FIRST_DISSENTING_OPINION_SECOND_AUTHOR))

                .andExpect(jsonPath("$.data.dissentingOpinions.[1].textContent").value(CT_SECOND_DISSENTING_OPINION_TEXT))
                .andExpect(jsonPath("$.data.dissentingOpinions.[1].authors.[0]").value(CT_SECOND_DISSENTING_OPINION_FIRST_AUTHOR))
                
                .andExpect(jsonPath("$.data.division").doesNotExist())
                .andExpect(jsonPath("$.data.chambers").doesNotExist())

        ;
    }
    
    @Test
    public void it_should_show_all_nacJudgments_fields() throws Exception {
        //when
        ResultActions actions = mockMvc.perform(get(nacJudgmentPath)
                .accept(MediaType.APPLICATION_JSON));
        
        //then
        assertOk(actions);
        actions
                .andExpect(jsonPath("$.data.id").value(equalsLong(testObjectContext.getNacJudgmentId())))
                .andExpect(jsonPath("$.data.courtType").value(CourtType.NATIONAL_APPEAL_CHAMBER.name()))
                .andExpect(jsonPath("$.data.href").value(endsWith(nacJudgmentPath)))

                .andExpect(jsonPath("$.data.division").doesNotExist())
                .andExpect(jsonPath("$.data.chambers").doesNotExist())
                .andExpect(jsonPath("$.data.dissentingOpinions").doesNotExist())
        ;
    }


    @Test
    public void it_should_show_links() throws Exception {
        //when
        ResultActions actions = mockMvc.perform(get(ccJudgmentPath)
                .accept(MediaType.APPLICATION_JSON));

        //then
        assertOk(actions);
        actions
                .andExpect(jsonPath("$.links").isArray())
                .andExpect(jsonPath("$.links[?(@.rel==self)].href[0]").value(endsWith(ccJudgmentPath)))
                ;
    }
    
    
    @Test
    public void it_should_not_allow_not_existing_judgment_id() throws Exception {
        // when
        ResultActions actions = mockMvc.perform(get(notExistingJudgmentPath));
        
        // then
        assertNotFoundError(actions, notExistingJudgmentId);
    }
    
    @Test
    public void should_respond_in_iso8859_1_charset() throws Exception {
        // when
        ResultActions actions = mockMvc.perform(get(ccJudgmentPath)
                .accept(MediaType.APPLICATION_JSON+";charset=ISO-8859-1"));
        
        // then
        assertOk(actions, "ISO-8859-1");
    }



}