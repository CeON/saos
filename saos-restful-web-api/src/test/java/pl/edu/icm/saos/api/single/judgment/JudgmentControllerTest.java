package pl.edu.icm.saos.api.single.judgment;

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
import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.PersistenceTestSupport;
import pl.edu.icm.saos.persistence.common.TestObjectContext;
import pl.edu.icm.saos.persistence.common.TestPersistenceObjectFactory;
import pl.edu.icm.saos.persistence.model.CourtType;
import pl.edu.icm.saos.persistence.model.SourceCode;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static pl.edu.icm.saos.api.services.Constants.*;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes =  {ApiTestConfiguration.class})
@Category(SlowTest.class)
public class JudgmentControllerTest extends PersistenceTestSupport {

    @Autowired
    private SingleJudgmentSuccessRepresentationBuilder singleJudgmentSuccessRepresentationBuilder;

    @Autowired
    private JudgmentRepository judgmentRepository;

    @Autowired
    private TestPersistenceObjectFactory testPersistenceObjectFactory;

    private TestObjectContext testObjectContext;


    private MockMvc mockMvc;

    private String ccJudgmentPath;
    private String scJudgmentPath;
    private String ctJudgmentPath;

    @Before
    public void setUp(){
        testObjectContext = testPersistenceObjectFactory.createTestObjectContext();
        ccJudgmentPath = SINGLE_JUDGMENTS_PATH + "/" + testObjectContext.getCcJudgmentId();
        scJudgmentPath = SINGLE_JUDGMENTS_PATH + "/" + testObjectContext.getScJudgmentId();
        ctJudgmentPath = SINGLE_JUDGMENTS_PATH + "/" + testObjectContext.getCtJudgmentId();

        JudgmentController judgmentController = new JudgmentController();

        judgmentController.setSingleJudgmentSuccessRepresentationBuilder(singleJudgmentSuccessRepresentationBuilder);
        judgmentController.setJudgmentRepository(judgmentRepository);

        mockMvc = standaloneSetup(judgmentController)
                .build();
    }

    @Test
    public void it_should_show_all_judgment_basic_fields() throws Exception {
        //when
        ResultActions actions = mockMvc.perform(get(ccJudgmentPath)
                .accept(MediaType.APPLICATION_JSON));

        //then
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

        ;

    }

    @Test
    public void it_should_show_all_ccJudgments_fields() throws Exception {
        //when
        ResultActions actions = mockMvc.perform(get(ccJudgmentPath)
                .accept(MediaType.APPLICATION_JSON));

        //then

        actions
                .andExpect(jsonPath("$.data.id").value(testObjectContext.getCcJudgmentId()))
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
                .andExpect(jsonPath("$.data.division.id").value(testObjectContext.getCcFirstDivisionId()))
                .andExpect(jsonPath("$.data.division.name").value(CC_FIRST_DIVISION_NAME))
                .andExpect(jsonPath("$.data.division.code").value(CC_FIRST_DIVISION_CODE))
                .andExpect(jsonPath("$.data.division.type").value(CC_FIRST_DIVISION_TYPE_NAME))

                .andExpect(jsonPath("$.data.division.court.href").value(endsWith(SINGLE_COURTS_PATH+"/"+testObjectContext.getCcCourtId())))
                .andExpect(jsonPath("$.data.division.court.id").value(testObjectContext.getCcCourtId()))
                .andExpect(jsonPath("$.data.division.court.code").value(CC_COURT_CODE))
                .andExpect(jsonPath("$.data.division.court.name").value(CC_COURT_NAME))
                .andExpect(jsonPath("$.data.division.court.type").value(CC_COURT_TYPE.name()))

                ;
    }

    @Test
    public void it_should_show_all_scJudgments_fields() throws Exception {
        //when
        ResultActions actions = mockMvc.perform(get(scJudgmentPath)
                .accept(MediaType.APPLICATION_JSON));

        //then
        actions
                .andExpect(jsonPath("$.data.id").value(testObjectContext.getScJudgmentId()))
                .andExpect(jsonPath("$.data.courtType").value(CourtType.SUPREME.name()))
                .andExpect(jsonPath("$.data.href").value(endsWith(scJudgmentPath)))
                .andExpect(jsonPath("$.data.personnelType").value(SC_PERSONNEL_TYPE.name()))

                .andExpect(jsonPath("$.data.judgmentForm.name").value(SC_JUDGMENT_FORM_NAME))

                .andExpect(jsonPath("$.data.division.href").value(endsWith("/api/scDivisions/" + testObjectContext.getScFirstDivisionId())))
                .andExpect(jsonPath("$.data.division.id").value(testObjectContext.getScFirstDivisionId()))
                .andExpect(jsonPath("$.data.division.name").value(SC_FIRST_DIVISION_NAME))
                .andExpect(jsonPath("$.data.division.chamber.href").value(endsWith("/api/scChambers/" + testObjectContext.getScChamberId())))
                .andExpect(jsonPath("$.data.division.chamber.id").value(testObjectContext.getScChamberId()))
                .andExpect(jsonPath("$.data.division.chamber.name").value(SC_CHAMBER_NAME))

                .andExpect(jsonPath("$.data.chambers.[0].href").value(endsWith("/api/scChambers/" + testObjectContext.getScFirstChamberId())))
                .andExpect(jsonPath("$.data.chambers.[0].id").value(testObjectContext.getScFirstChamberId()))
                .andExpect(jsonPath("$.data.chambers.[0].name").value(SC_FIRST_CHAMBER_NAME))

                ;
    }

    @Test
    public void it_should_show_all_ctJudgments_fields() throws Exception {
        //when
        ResultActions actions = mockMvc.perform(get(ctJudgmentPath)
                .accept(MediaType.APPLICATION_JSON));

        //then
        actions
                .andExpect(jsonPath("$.data.id").value(testObjectContext.getCtJudgmentId()))
                .andExpect(jsonPath("$.data.courtType").value(CourtType.CONSTITUTIONAL_TRIBUNAL.name()))
                .andExpect(jsonPath("$.data.href").value(endsWith(ctJudgmentPath)))

                .andExpect(jsonPath("$.data.dissentingOpinions").value(iterableWithSize(2)))

                .andExpect(jsonPath("$.data.dissentingOpinions.[0].textContent").value(CT_FIRST_DISSENTING_OPINION_TEXT))
                .andExpect(jsonPath("$.data.dissentingOpinions.[0].authors.[0]").value(CT_FIRST_DISSENTING_OPINION_FIRST_AUTHOR))
                .andExpect(jsonPath("$.data.dissentingOpinions.[0].authors.[1]").value(CT_FIRST_DISSENTING_OPINION_SECOND_AUTHOR))

                .andExpect(jsonPath("$.data.dissentingOpinions.[1].textContent").value(CT_SECOND_DISSENTING_OPINION_TEXT))
                .andExpect(jsonPath("$.data.dissentingOpinions.[1].authors.[0]").value(CT_SECOND_DISSENTING_OPINION_FIRST_AUTHOR))

        ;
    }


    @Test
    public void it_should_show_links() throws Exception {
        //when
        ResultActions actions = mockMvc.perform(get(ccJudgmentPath)
                .accept(MediaType.APPLICATION_JSON));

        //then
        actions
                .andExpect(jsonPath("$.links").isArray())
                .andExpect(jsonPath("$.links[?(@.rel==self)].href[0]").value(endsWith(ccJudgmentPath)))
                ;
    }



}