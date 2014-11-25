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
import pl.edu.icm.saos.api.services.FieldsDefinition.JC;
import pl.edu.icm.saos.api.support.TestPersistenceObjectsContext;
import pl.edu.icm.saos.api.support.TestPersistenceObjectsFactory;
import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.PersistenceTestSupport;
import pl.edu.icm.saos.persistence.model.SourceCode;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static pl.edu.icm.saos.api.search.judgments.JudgmentJsonRepresentationVerifier.verifyBasicFields;
import static pl.edu.icm.saos.api.services.Constansts.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes =  {ApiTestConfiguration.class})
@Category(SlowTest.class)
public class JudgmentControllerTest extends PersistenceTestSupport {

    @Autowired
    private SingleJudgmentSuccessRepresentationBuilder singleJudgmentSuccessRepresentationBuilder;

    @Autowired
    private JudgmentRepository judgmentRepository;

    @Autowired
    private TestPersistenceObjectsFactory testPersistenceObjectsFactory;


    private MockMvc mockMvc;

    private TestPersistenceObjectsContext objectsContext;
    private String ccJudgmentPath;
    private String scJudgmentPath;

    @Before
    public void setUp(){
        objectsContext = testPersistenceObjectsFactory.createPersistenceObjectsContext();
        ccJudgmentPath = SINGLE_JUDGMENTS_PATH + "/" + objectsContext.getJudgmentId();
        scJudgmentPath = SINGLE_JUDGMENTS_PATH + "/" + objectsContext.getScJudgmentId();

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
        verifyBasicFields(actions, "$.data", objectsContext);
    }

    @Test
    public void it_should_show_all_ccJudgments_fields() throws Exception {
        //when
        ResultActions actions = mockMvc.perform(get(ccJudgmentPath)
                .accept(MediaType.APPLICATION_JSON));

        //then

        actions
                .andExpect(jsonPath("$.data.href").value(endsWith(ccJudgmentPath)))
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

                .andExpect(jsonPath("$.data.division.href").value(endsWith(SINGLE_DIVISIONS_PATH +"/"+objectsContext.getFirstDivisionId())))
                .andExpect(jsonPath("$.data.division.name").value(JC.DIVISION_NAME))
                .andExpect(jsonPath("$.data.division.code").value(JC.DIVISION_CODE))
                .andExpect(jsonPath("$.data.division.type").value(JC.DIVISION_TYPE_NAME))

                .andExpect(jsonPath("$.data.division.court.href").value(endsWith(SINGLE_COURTS_PATH+"/"+objectsContext.getCommonCourtId())))
                .andExpect(jsonPath("$.data.division.court.code").value(JC.COURT_CODE))
                .andExpect(jsonPath("$.data.division.court.name").value(JC.COURT_NAME))
                .andExpect(jsonPath("$.data.division.court.type").value(JC.COURT_TYPE.name()))

                .andExpect(jsonPath("$.data.division.court.parentCourt.href").value(endsWith(SINGLE_COURTS_PATH+"/"+objectsContext.getParentCourtId())))
                ;
    }

    @Test
    public void it_should_show_all_scJudgments_fields() throws Exception {
        //when
        ResultActions actions = mockMvc.perform(get(scJudgmentPath)
                .accept(MediaType.APPLICATION_JSON));

        //then
        actions
                .andExpect(jsonPath("$.data.href").value(endsWith(scJudgmentPath)))
                .andExpect(jsonPath("$.data.personnelType").value(SupremeCourtJudgment.PersonnelType.FIVE_PERSON.name()))

                .andExpect(jsonPath("$.data.form.name").value(JC.SC_JUDGMENT_FORM_NAME))

                .andExpect(jsonPath("$.data.division.href").value(endsWith("/api/scDivisions/" + objectsContext.getScDivisionId())))
                .andExpect(jsonPath("$.data.division.name").value(JC.SC_CHAMBER_DIVISION_NAME))
                .andExpect(jsonPath("$.data.division.chamber.href").value(endsWith("/api/scChambers/" + objectsContext.getScChamberId())))
                .andExpect(jsonPath("$.data.division.chamber.name").value(JC.SC_CHAMBER_NAME))

                .andExpect(jsonPath("$.data.chambers.[0].href").value(endsWith("/api/scChambers/" + objectsContext.getScChamberId())))
                .andExpect(jsonPath("$.data.chambers.[0].name").value(JC.SC_CHAMBER_NAME))

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