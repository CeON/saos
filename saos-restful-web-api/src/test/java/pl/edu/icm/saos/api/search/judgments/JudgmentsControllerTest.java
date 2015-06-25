package pl.edu.icm.saos.api.search.judgments;


import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static pl.edu.icm.saos.api.ApiResponseAssertUtils.assertIncorrectParamNameError;
import static pl.edu.icm.saos.api.ApiResponseAssertUtils.assertIncorrectValueError;
import static pl.edu.icm.saos.api.ApiResponseAssertUtils.assertInvalidPageNumberError;
import static pl.edu.icm.saos.api.ApiResponseAssertUtils.assertInvalidPageSizeError;
import static pl.edu.icm.saos.api.ApiResponseAssertUtils.assertNegativePageNumberError;
import static pl.edu.icm.saos.api.ApiResponseAssertUtils.assertOk;
import static pl.edu.icm.saos.api.ApiResponseAssertUtils.assertTooBigPageSizeError;
import static pl.edu.icm.saos.api.ApiResponseAssertUtils.assertTooSmallPageSizeError;
import static pl.edu.icm.saos.api.services.Constants.JUDGMENTS_PATH;
import static pl.edu.icm.saos.api.services.Constants.SINGLE_COURTS_PATH;
import static pl.edu.icm.saos.api.services.Constants.SINGLE_DIVISIONS_PATH;
import static pl.edu.icm.saos.api.services.Constants.SINGLE_JUDGMENTS_PATH;
import static pl.edu.icm.saos.common.testcommon.IntToLongMatcher.equalsLong;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CASE_NUMBER;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_COURT_CODE;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_COURT_NAME;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_DATE_DAY;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_DATE_MONTH;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_DATE_YEAR;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_FIRST_DIVISION_CODE;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_FIRST_DIVISION_NAME;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_FIRST_JUDGE_NAME;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_FIRST_JUDGE_ROLE;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_JUDGMENT_TYPE;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_SECOND_JUDGE_NAME;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_TEXT_CONTENT;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_THIRD_JUDGE_NAME;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.SC_FIRST_CHAMBER_NAME;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.SC_FIRST_DIVISION_NAME;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.SC_JUDGMENT_FORM_NAME;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.SC_PERSONNEL_TYPE;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.NAC_CASE_NUMBER;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.SC_JUDGMENT_TYPE;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_FIRST_KEYWORD;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_SECOND_KEYWORD;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.SC_CHAMBER_NAME;
import static pl.edu.icm.saos.persistence.common.TextObjectDefaultData.CC_COURT_TYPE;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import pl.edu.icm.saos.api.ApiConstants;
import pl.edu.icm.saos.api.ApiTestSupport;
import pl.edu.icm.saos.api.formatter.LawJournalEntryCodeFormatterFactory;
import pl.edu.icm.saos.api.search.judgments.services.JudgmentsApiSearchService;
import pl.edu.icm.saos.api.search.parameters.ParametersExtractor;
import pl.edu.icm.saos.api.services.interceptor.AccessControlHeaderHandlerInterceptor;
import pl.edu.icm.saos.api.services.interceptor.RestrictParamsHandlerInterceptor;
import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.common.TestObjectContext;
import pl.edu.icm.saos.persistence.common.TestPersistenceObjectFactory;
import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.CourtType;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.Judgment.JudgmentType;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment.PersonnelType;
import pl.edu.icm.saos.persistence.service.LawJournalEntryCodeExtractor;
import pl.edu.icm.saos.search.config.model.JudgmentIndexField;
import pl.edu.icm.saos.search.indexing.JudgmentIndexingData;
import pl.edu.icm.saos.search.indexing.JudgmentIndexingProcessor;
import pl.edu.icm.saos.search.search.model.Sorting;

import com.google.common.collect.Lists;

@Category(SlowTest.class)
public class JudgmentsControllerTest extends ApiTestSupport {

    private static final int NR_OF_JUDGMENTS_STORED_IN_SOLR_INDEX = 4;


    private MockMvc mockMvc;



    @Autowired
    private JudgmentsListSuccessRepresentationBuilder listSuccessRepresentationBuilder;

    @Autowired
    private ParametersExtractor parametersExtractor;

    @Autowired
    private JudgmentsApiSearchService apiSearchService;

    @Autowired
    private LawJournalEntryCodeExtractor lawJournalEntryCodeExtractor;

    @Autowired
    private TestPersistenceObjectFactory testPersistenceObjectFactory;

    private TestObjectContext testObjectContext;

    @Before
    public void setUp() throws Exception{
        testObjectContext = testPersistenceObjectFactory.createTestObjectContext();

        clearAndIndexJudgmentsInSolr(testObjectContext);

        JudgmentsController judgmentsController = new JudgmentsController();
        judgmentsController.setApiSearchService(apiSearchService);
        judgmentsController.setListSuccessRepresentationBuilder(listSuccessRepresentationBuilder);
        judgmentsController.setParametersExtractor(parametersExtractor);
        
        FormattingConversionService conversionService = new DefaultFormattingConversionService();
        conversionService.addFormatterForFieldAnnotation(new LawJournalEntryCodeFormatterFactory(lawJournalEntryCodeExtractor));

        mockMvc = standaloneSetup(judgmentsController)
                .addInterceptors(new RestrictParamsHandlerInterceptor())
                .addInterceptors(new AccessControlHeaderHandlerInterceptor())
                .setConversionService(conversionService)
                .build();
    }


    @Autowired
    @Qualifier("solrJudgmentsServer")
    private SolrServer judgmentsServer;

    @Autowired
    private JudgmentIndexingProcessor judgmentIndexingProcessor;

    private void clearAndIndexJudgmentsInSolr(TestObjectContext testObjectContext) throws Exception{
        judgmentsServer.deleteByQuery("*:*");
        judgmentsServer.commit();

        JudgmentIndexingData ccJudgmentIndexingData = new JudgmentIndexingData();
        ccJudgmentIndexingData.setJudgment(testObjectContext.getCcJudgment());
        
        JudgmentIndexingData scJudgmentIndexingData = new JudgmentIndexingData();
        scJudgmentIndexingData.setJudgment(testObjectContext.getScJudgment());
        
        JudgmentIndexingData ctJudgmentIndexingData = new JudgmentIndexingData();
        ctJudgmentIndexingData.setJudgment(testObjectContext.getCtJudgment());
        
        JudgmentIndexingData nacJudgmentIndexingData = new JudgmentIndexingData();
        nacJudgmentIndexingData.setJudgment(testObjectContext.getNacJudgment());
        
        SolrInputDocument ccJudgmentDoc = judgmentIndexingProcessor.process(ccJudgmentIndexingData);
        SolrInputDocument scJudgmentDoc = judgmentIndexingProcessor.process(scJudgmentIndexingData);
        SolrInputDocument ctJudgmentDoc = judgmentIndexingProcessor.process(ctJudgmentIndexingData);
        SolrInputDocument nacJudgmentDoc = judgmentIndexingProcessor.process(nacJudgmentIndexingData);
        judgmentsServer.add(
                Arrays.asList(ccJudgmentDoc, scJudgmentDoc, ctJudgmentDoc, nacJudgmentDoc)
        );

        judgmentsServer.commit();
    }


    //------------------------ TESTS --------------------------

    @Test
    public void showJudgments__it_should_show_all_basic_judgment_fields() throws Exception {
        //when
        ResultActions actions = mockMvc.perform(get(JUDGMENTS_PATH)
                .param(ApiConstants.PAGE_SIZE, "2")
                .param(ApiConstants.PAGE_NUMBER, "0")
                .accept(MediaType.APPLICATION_JSON));
        //then


        assertOk(actions);


        actions
                .andExpect(jsonPath("$.items.[0].id").value(equalsLong(testObjectContext.getCcJudgmentId())))
                .andExpect(jsonPath("$.items.[0].href").value(endsWith(SINGLE_JUDGMENTS_PATH + "/" + testObjectContext.getCcJudgmentId())))
                .andExpect(jsonPath("$.items.[0].courtType").value(CourtType.COMMON.name()))
                .andExpect(jsonPath("$.items.[0].courtCases").value(iterableWithSize(1)))
                .andExpect(jsonPath("$.items.[0].courtCases.[0].caseNumber").value(CASE_NUMBER))
                .andExpect(jsonPath("$.items.[0].judgmentType").value(CC_JUDGMENT_TYPE.name()))

                .andExpect(jsonPath("$.items.[0].judgmentDate").value(CC_DATE_YEAR + "-" + CC_DATE_MONTH + "-" + CC_DATE_DAY))

                .andExpect(jsonPath("$.items.[0].judges").isArray())
                .andExpect(jsonPath("$.items.[0].judges").value(iterableWithSize(3)))
                .andExpect(jsonPath("$.items.[0].judges.[0].name").value(CC_FIRST_JUDGE_NAME))
                .andExpect(jsonPath("$.items.[0].judges.[0].specialRoles").value(iterableWithSize(1)))
                .andExpect(jsonPath("$.items.[0].judges.[0].specialRoles.[0]").value(CC_FIRST_JUDGE_ROLE.name()))
                .andExpect(jsonPath("$.items.[0].judges.[1].name").value(CC_SECOND_JUDGE_NAME))
                .andExpect(jsonPath("$.items.[0].judges.[1].specialRoles").value(emptyIterable()))
                .andExpect(jsonPath("$.items.[0].judges.[2].name").value(CC_THIRD_JUDGE_NAME))
                .andExpect(jsonPath("$.items.[0].judges.[2].specialRoles").value(emptyIterable()))


                .andExpect(jsonPath("$.items.[0].source").doesNotExist())
                .andExpect(jsonPath("$.items.[0].courtReporters").doesNotExist())
                .andExpect(jsonPath("$.items.[0].decision").doesNotExist())
                .andExpect(jsonPath("$.items.[0].summary").doesNotExist())

                .andExpect(jsonPath("$.items.[0].reasoning").doesNotExist())
                .andExpect(jsonPath("$.items.[0].legalBases").doesNotExist())
                .andExpect(jsonPath("$.items.[0].referencedRegulations").doesNotExist())


                .andExpect(jsonPath("$.items.[0].division.type").doesNotExist())

                .andExpect(jsonPath("$.items.[0].division.court.type").doesNotExist())

                .andExpect(jsonPath("$.items.[0].textContent").value(CC_TEXT_CONTENT))

                .andExpect(jsonPath("$.items.[0].division.href").value(endsWith(SINGLE_DIVISIONS_PATH + "/" + testObjectContext.getCcFirstDivisionId())))
                .andExpect(jsonPath("$.items.[0].division.id").value(equalsLong(testObjectContext.getCcFirstDivisionId())))
                .andExpect(jsonPath("$.items.[0].division.name").value(CC_FIRST_DIVISION_NAME))
                .andExpect(jsonPath("$.items.[0].division.code").value(CC_FIRST_DIVISION_CODE))

                .andExpect(jsonPath("$.items.[0].division.court.href").value(endsWith(SINGLE_COURTS_PATH + "/" + testObjectContext.getCcCourtId())))
                .andExpect(jsonPath("$.items.[0].division.court.id").value(equalsLong(testObjectContext.getCcCourtId())))
                .andExpect(jsonPath("$.items.[0].division.court.name").value(CC_COURT_NAME))
                .andExpect(jsonPath("$.items.[0].division.court.code").value(CC_COURT_CODE))


                .andExpect(jsonPath("$.items.[1].id").value(equalsLong(testObjectContext.getScJudgmentId())))
                .andExpect(jsonPath("$.items.[1].href").value(endsWith("/api/judgments/" + testObjectContext.getScJudgmentId())))
                .andExpect(jsonPath("$.items.[1].courtType").value(CourtType.SUPREME.name()))
                .andExpect(jsonPath("$.items.[1].personnelType").value(SC_PERSONNEL_TYPE.name()))
                .andExpect(jsonPath("$.items.[1].judgmentForm").value(SC_JUDGMENT_FORM_NAME))

                .andExpect(jsonPath("$.items.[1].division.href").value(endsWith("/api/scDivisions/" + testObjectContext.getScFirstDivisionId())))
                .andExpect(jsonPath("$.items.[1].division.id").value(equalsLong(testObjectContext.getScFirstDivisionId())))
                .andExpect(jsonPath("$.items.[1].division.name").value(SC_FIRST_DIVISION_NAME))

                .andExpect(jsonPath("$.items.[1].division.chambers.[0].href").value(endsWith("/api/scChambers/" + testObjectContext.getScChamberId())))
                .andExpect(jsonPath("$.items.[1].division.chambers.[0].id").value(equalsLong(testObjectContext.getScChamberId())))
                .andExpect(jsonPath("$.items.[1].division.chambers.[0].name").value(SC_FIRST_CHAMBER_NAME))

        ;

    }


    @Test
    public void showJudgments__it_should_sort_by_judgment_date_descending() throws Exception{
        //when
        ResultActions actions = mockMvc.perform(get(JUDGMENTS_PATH)
                .param(ApiConstants.PAGE_NUMBER, "0")
                .param("sortingField", JudgmentIndexField.JUDGMENT_DATE.name())
                .param("sortingDirection", Sorting.Direction.DESC.name())
                .accept(MediaType.APPLICATION_JSON));

        //then
        List<Judgment> judgments = Lists.newArrayList(testObjectContext.getCcJudgment(), testObjectContext.getScJudgment(),
                testObjectContext.getNacJudgment(), testObjectContext.getCtJudgment());
        Collections.sort(judgments, (j1, j2) -> j2.getJudgmentDate().compareTo(j1.getJudgmentDate()));
        
        assertOk(actions);
        actions
            .andExpect(jsonPath("$.items.[0].id").value(equalsLong(judgments.get(0).getId())))
            .andExpect(jsonPath("$.items.[1].id").value(equalsLong(judgments.get(1).getId())))
            .andExpect(jsonPath("$.items.[2].id").value(equalsLong(judgments.get(2).getId())))
            .andExpect(jsonPath("$.items.[3].id").value(equalsLong(judgments.get(3).getId())));

    }

    @Test
    public void showJudgments__it_should_sort_by_judgment_date_ascending() throws Exception{
        //when
        ResultActions actions = mockMvc.perform(get(JUDGMENTS_PATH)
                .param(ApiConstants.PAGE_NUMBER, "0")
                .param("sortingField", JudgmentIndexField.JUDGMENT_DATE.name())
                .param("sortingDirection", Sorting.Direction.ASC.name())
                .accept(MediaType.APPLICATION_JSON));

        //then
        List<Judgment> judgments = Lists.newArrayList(testObjectContext.getCcJudgment(), testObjectContext.getScJudgment(),
                testObjectContext.getNacJudgment(), testObjectContext.getCtJudgment());
        Collections.sort(judgments, (j1, j2) -> j1.getJudgmentDate().compareTo(j2.getJudgmentDate()));
        
        assertOk(actions);
        actions
            .andExpect(jsonPath("$.items.[0].id").value(equalsLong(judgments.get(0).getId())))
            .andExpect(jsonPath("$.items.[1].id").value(equalsLong(judgments.get(1).getId())))
            .andExpect(jsonPath("$.items.[2].id").value(equalsLong(judgments.get(2).getId())))
            .andExpect(jsonPath("$.items.[3].id").value(equalsLong(judgments.get(3).getId())));

    }

    @Test
    public void showJudgments__it_should_search_by_all_field() throws Exception {
        // when
        ResultActions actions = mockMvc.perform(get(JUDGMENTS_PATH)
                .param(ApiConstants.PAGE_NUMBER, "0")
                .param(ApiConstants.ALL, "nac content")
                .accept(MediaType.APPLICATION_JSON));
        
        // then
        assertOk(actions);
        actions
            .andExpect(jsonPath("$.items").value(iterableWithSize(1)))
            .andExpect(jsonPath("$.items.[0].id").value(equalsLong(testObjectContext.getNacJudgmentId())));
    }
    
    @Test
    public void showJudgments__it_should_search_by_legal_base() throws Exception {
        // when
        ResultActions actions = mockMvc.perform(get(JUDGMENTS_PATH)
                .param(ApiConstants.PAGE_NUMBER, "0")
                .param(ApiConstants.LEGAL_BASE, "ustawa z dnia 22 listopada 2013")
                .accept(MediaType.APPLICATION_JSON));
        
        // then
        assertOk(actions);
        actions
            .andExpect(jsonPath("$.items").value(iterableWithSize(1)))
            .andExpect(jsonPath("$.items.[0].id").value(equalsLong(testObjectContext.getScJudgmentId())));
    }
    
    @Test
    public void showJudgments__it_should_search_by_referenced_regulation() throws Exception {
        // when
        ResultActions actions = mockMvc.perform(get(JUDGMENTS_PATH)
                .param(ApiConstants.PAGE_NUMBER, "0")
                .param(ApiConstants.REFERENCED_REGULATION, "first ct")
                .accept(MediaType.APPLICATION_JSON));
        
        // then
        assertOk(actions);
        actions
            .andExpect(jsonPath("$.items").value(iterableWithSize(1)))
            .andExpect(jsonPath("$.items.[0].id").value(equalsLong(testObjectContext.getCtJudgmentId())));
    }
    
    @Test
    public void showJudgments__it_should_search_by_journal_entry_code() throws Exception {
        // when
        ResultActions actions = mockMvc.perform(get(JUDGMENTS_PATH)
                .param(ApiConstants.PAGE_NUMBER, "0")
                .param(ApiConstants.LAW_JOURNAL_ENTRY_CODE, "1960/30/168")
                .accept(MediaType.APPLICATION_JSON));
        
        // then
        assertOk(actions);
        actions
            .andExpect(jsonPath("$.items").value(iterableWithSize(1)))
            .andExpect(jsonPath("$.items.[0].id").value(equalsLong(testObjectContext.getCcJudgmentId())));
    }
    
    @Test
    public void showJudgments__it_should_search_by_judge_name() throws Exception {
        // when
        ResultActions actions = mockMvc.perform(get(JUDGMENTS_PATH)
                .param(ApiConstants.PAGE_NUMBER, "0")
                .param(ApiConstants.JUDGE_NAME, CC_FIRST_JUDGE_NAME)
                .accept(MediaType.APPLICATION_JSON));
        
        // then
        assertOk(actions);
        actions
            .andExpect(jsonPath("$.items").value(iterableWithSize(1)))
            .andExpect(jsonPath("$.items.[0].id").value(equalsLong(testObjectContext.getCcJudgmentId())));
    }
    
    @Test
    public void showJudgments__it_should_search_by_case_number() throws Exception {
        ResultActions actions = mockMvc.perform(get(JUDGMENTS_PATH)
                .param(ApiConstants.PAGE_NUMBER, "0")
                .param(ApiConstants.CASE_NUMBER, NAC_CASE_NUMBER)
                .accept(MediaType.APPLICATION_JSON));
        
        // then
        assertOk(actions);
        actions
            .andExpect(jsonPath("$.items").value(iterableWithSize(1)))
            .andExpect(jsonPath("$.items.[0].id").value(equalsLong(testObjectContext.getNacJudgmentId())));
    }
    
    @Test
    public void showJudgments__it_should_search_by_court_type() throws Exception {
        // given
        ResultActions actions = mockMvc.perform(get(JUDGMENTS_PATH)
                .param(ApiConstants.PAGE_NUMBER, "0")
                .param(ApiConstants.COURT_TYPE, "CONSTITUTIONAL_TRIBUNAL")
                .accept(MediaType.APPLICATION_JSON));
        
        // then
        assertOk(actions);
        actions
            .andExpect(jsonPath("$.items").value(iterableWithSize(1)))
            .andExpect(jsonPath("$.items.[0].id").value(equalsLong(testObjectContext.getCtJudgmentId())));
    }
    
    @Test
    public void showJudgments__it_should_search_by_judgment_type() throws Exception {
        // given
        String firstType = CC_JUDGMENT_TYPE.name();
        String secondType = SC_JUDGMENT_TYPE.name();
        List<String> judgmentTypes = Arrays.asList(firstType, secondType);
        
        // when
        ResultActions actions = mockMvc.perform(get(JUDGMENTS_PATH)
                .param(ApiConstants.PAGE_NUMBER, "0")
                .param(ApiConstants.JUDGMENT_TYPES, judgmentTypes.toArray(new String[0]))
                .accept(MediaType.APPLICATION_JSON));
        
        // then
        List<Judgment> judgments = Lists.newArrayList(testObjectContext.getCcJudgment(), testObjectContext.getScJudgment());
        Collections.sort(judgments, (j1, j2) -> Long.compare(j1.getId(), j2.getId()));
        
        assertOk(actions);
        actions
            .andExpect(jsonPath("$.items").value(iterableWithSize(2)))
            .andExpect(jsonPath("$.items.[0].id").value(equalsLong(judgments.get(0).getId())))
            .andExpect(jsonPath("$.items.[1].id").value(equalsLong(judgments.get(1).getId())));
    }
    
    @Test
    public void showJudgments__it_should_search_by_keywords() throws Exception {
        // given
        String firstKeyword = CC_FIRST_KEYWORD;
        String secondKeyword = CC_SECOND_KEYWORD;
        List<String> keywords = Arrays.asList(firstKeyword, secondKeyword);
        
        // when
        ResultActions actions = mockMvc.perform(get(JUDGMENTS_PATH)
                .param(ApiConstants.PAGE_NUMBER, "0")
                .param(ApiConstants.KEYWORDS, keywords.toArray(new String[0]))
                .accept(MediaType.APPLICATION_JSON));
        
        // then
        assertOk(actions);
        actions
            .andExpect(jsonPath("$.items").value(iterableWithSize(1)))
            .andExpect(jsonPath("$.items.[0].id").value(equalsLong(testObjectContext.getCcJudgmentId())));
    }
    
    @Test
    public void showJudgments__it_should_search_by_judgment_date_from() throws Exception {
        // given
        ResultActions actions = mockMvc.perform(get(JUDGMENTS_PATH)
                .param(ApiConstants.PAGE_NUMBER, "0")
                .param(ApiConstants.JUDGMENT_DATE_FROM, "2013-01-01")
                .accept(MediaType.APPLICATION_JSON));
        
        // then
        List<Judgment> judgments = Lists.newArrayList(testObjectContext.getCtJudgment(), testObjectContext.getNacJudgment());
        Collections.sort(judgments, (j1, j2) -> Long.compare(j1.getId(), j2.getId()));
        
        assertOk(actions);
        actions
            .andExpect(jsonPath("$.items").value(iterableWithSize(2)))
            .andExpect(jsonPath("$.items.[0].id").value(equalsLong(judgments.get(0).getId())))
            .andExpect(jsonPath("$.items.[1].id").value(equalsLong(judgments.get(1).getId())));
    }
    
    @Test
    public void showJudgments__it_should_search_by_judgment_date_to() throws Exception {
        // given
        ResultActions actions = mockMvc.perform(get(JUDGMENTS_PATH)
                .param(ApiConstants.PAGE_NUMBER, "0")
                .param(ApiConstants.JUDGMENT_DATE_TO, "2013-01-01")
                .accept(MediaType.APPLICATION_JSON));
        
        // then
        List<Judgment> judgments = Lists.newArrayList(testObjectContext.getCcJudgment(), testObjectContext.getScJudgment());
        Collections.sort(judgments, (j1, j2) -> Long.compare(j1.getId(), j2.getId()));
        
        assertOk(actions);
        actions
            .andExpect(jsonPath("$.items").value(iterableWithSize(2)))
            .andExpect(jsonPath("$.items.[0].id").value(equalsLong(judgments.get(0).getId())))
            .andExpect(jsonPath("$.items.[1].id").value(equalsLong(judgments.get(1).getId())));
    }
    
    @Test
    public void showJudgments__it_should_search_by_sc_personnel_type() throws Exception {
        // given
        ResultActions actions = mockMvc.perform(get(JUDGMENTS_PATH)
                .param(ApiConstants.PAGE_NUMBER, "0")
                .param(ApiConstants.SC_PERSONNEL_TYPE, SC_PERSONNEL_TYPE.name())
                .accept(MediaType.APPLICATION_JSON));
        
        // then
        assertOk(actions);
        actions
            .andExpect(jsonPath("$.items").value(iterableWithSize(1)))
            .andExpect(jsonPath("$.items.[0].id").value(equalsLong(testObjectContext.getScJudgmentId())));
    }
    
    @Test
    public void showJudgments__it_should_search_by_sc_chamber_id() throws Exception {
        // given
        ResultActions actions = mockMvc.perform(get(JUDGMENTS_PATH)
                .param(ApiConstants.PAGE_NUMBER, "0")
                .param(ApiConstants.SC_CHAMBER_ID, String.valueOf(testObjectContext.getScChamberId()))
                .accept(MediaType.APPLICATION_JSON));
        
        // then
        assertOk(actions);
        actions
            .andExpect(jsonPath("$.items").value(iterableWithSize(1)))
            .andExpect(jsonPath("$.items.[0].id").value(equalsLong(testObjectContext.getScJudgmentId())));
    }
    
    @Test
    public void showJudgments__it_should_search_by_sc_chamber_name() throws Exception {
        // given
        ResultActions actions = mockMvc.perform(get(JUDGMENTS_PATH)
                .param(ApiConstants.PAGE_NUMBER, "0")
                .param(ApiConstants.SC_CHAMBER_NAME, SC_CHAMBER_NAME)
                .accept(MediaType.APPLICATION_JSON));
        
        // then
        assertOk(actions);
        actions
            .andExpect(jsonPath("$.items").value(iterableWithSize(1)))
            .andExpect(jsonPath("$.items.[0].id").value(equalsLong(testObjectContext.getScJudgmentId())));
    }
    
    @Test
    public void showJudgments__it_should_search_by_sc_division_id() throws Exception {
        // given
        ResultActions actions = mockMvc.perform(get(JUDGMENTS_PATH)
                .param(ApiConstants.PAGE_NUMBER, "0")
                .param(ApiConstants.SC_DIVISION_ID, String.valueOf(testObjectContext.getScFirstDivisionId()))
                .accept(MediaType.APPLICATION_JSON));
        
        // then
        assertOk(actions);
        actions
            .andExpect(jsonPath("$.items").value(iterableWithSize(1)))
            .andExpect(jsonPath("$.items.[0].id").value(equalsLong(testObjectContext.getScJudgmentId())));
    }
    
    @Test
    public void showJudgments__it_should_search_by_sc_division_name() throws Exception {
        // given
        ResultActions actions = mockMvc.perform(get(JUDGMENTS_PATH)
                .param(ApiConstants.PAGE_NUMBER, "0")
                .param(ApiConstants.SC_DIVISION_NAME, SC_FIRST_DIVISION_NAME)
                .accept(MediaType.APPLICATION_JSON));
        
        // then
        assertOk(actions);
        actions
            .andExpect(jsonPath("$.items").value(iterableWithSize(1)))
            .andExpect(jsonPath("$.items.[0].id").value(equalsLong(testObjectContext.getScJudgmentId())));
    }
    
    @Test
    public void showJudgments__it_should_search_by_cc_court_type() throws Exception {
        // given
        ResultActions actions = mockMvc.perform(get(JUDGMENTS_PATH)
                .param(ApiConstants.PAGE_NUMBER, "0")
                .param(ApiConstants.CC_COURT_TYPE, CC_COURT_TYPE.name())
                .accept(MediaType.APPLICATION_JSON));
        
        // then
        assertOk(actions);
        actions
            .andExpect(jsonPath("$.items").value(iterableWithSize(1)))
            .andExpect(jsonPath("$.items.[0].id").value(equalsLong(testObjectContext.getCcJudgmentId())));
    }
    
    @Test
    public void showJudgments__it_should_search_by_cc_court_id() throws Exception {
        // given
        ResultActions actions = mockMvc.perform(get(JUDGMENTS_PATH)
                .param(ApiConstants.PAGE_NUMBER, "0")
                .param(ApiConstants.CC_COURT_ID, String.valueOf(testObjectContext.getCcCourtId()))
                .accept(MediaType.APPLICATION_JSON));
        
        // then
        assertOk(actions);
        actions
            .andExpect(jsonPath("$.items").value(iterableWithSize(1)))
            .andExpect(jsonPath("$.items.[0].id").value(equalsLong(testObjectContext.getCcJudgmentId())));
    }
    
    @Test
    public void showJudgments__it_should_search_by_cc_court_code() throws Exception {
        // given
        ResultActions actions = mockMvc.perform(get(JUDGMENTS_PATH)
                .param(ApiConstants.PAGE_NUMBER, "0")
                .param(ApiConstants.CC_COURT_CODE, CC_COURT_CODE)
                .accept(MediaType.APPLICATION_JSON));
        
        // then
        assertOk(actions);
        actions
            .andExpect(jsonPath("$.items").value(iterableWithSize(1)))
            .andExpect(jsonPath("$.items.[0].id").value(equalsLong(testObjectContext.getCcJudgmentId())));
    }
    
    @Test
    public void showJudgments__it_should_search_by_cc_court_name() throws Exception {
        // given
        ResultActions actions = mockMvc.perform(get(JUDGMENTS_PATH)
                .param(ApiConstants.PAGE_NUMBER, "0")
                .param(ApiConstants.CC_COURT_NAME, CC_COURT_NAME)
                .accept(MediaType.APPLICATION_JSON));
        
        // then
        assertOk(actions);
        actions
            .andExpect(jsonPath("$.items").value(iterableWithSize(1)))
            .andExpect(jsonPath("$.items.[0].id").value(equalsLong(testObjectContext.getCcJudgmentId())));
    }
    
    @Test
    public void showJudgments__it_should_search_by_cc_division_id() throws Exception {
        // given
        ResultActions actions = mockMvc.perform(get(JUDGMENTS_PATH)
                .param(ApiConstants.PAGE_NUMBER, "0")
                .param(ApiConstants.CC_DIVISION_ID, String.valueOf(testObjectContext.getCcFirstDivisionId()))
                .accept(MediaType.APPLICATION_JSON));
        
        // then
        assertOk(actions);
        actions
            .andExpect(jsonPath("$.items").value(iterableWithSize(1)))
            .andExpect(jsonPath("$.items.[0].id").value(equalsLong(testObjectContext.getCcJudgmentId())));
    }
    
    @Test
    public void showJudgments__it_should_search_by_cc_division_code() throws Exception {
        // given
        ResultActions actions = mockMvc.perform(get(JUDGMENTS_PATH)
                .param(ApiConstants.PAGE_NUMBER, "0")
                .param(ApiConstants.CC_DIVISION_CODE, CC_FIRST_DIVISION_CODE)
                .accept(MediaType.APPLICATION_JSON));
        
        // then
        assertOk(actions);
        actions
            .andExpect(jsonPath("$.items").value(iterableWithSize(1)))
            .andExpect(jsonPath("$.items.[0].id").value(equalsLong(testObjectContext.getCcJudgmentId())));
    }
    
    @Test
    public void showJudgments__it_should_search_by_cc_division_name() throws Exception {
        // given
        ResultActions actions = mockMvc.perform(get(JUDGMENTS_PATH)
                .param(ApiConstants.PAGE_NUMBER, "0")
                .param(ApiConstants.CC_DIVISION_NAME, CC_FIRST_DIVISION_NAME)
                .accept(MediaType.APPLICATION_JSON));
        
        // then
        assertOk(actions);
        actions
            .andExpect(jsonPath("$.items").value(iterableWithSize(1)))
            .andExpect(jsonPath("$.items.[0].id").value(equalsLong(testObjectContext.getCcJudgmentId())));
    }
    
    @Test
    public void showJudgments__it_should_search_with_dependent_courts() throws Exception {
        // given
        ResultActions actions = mockMvc.perform(get(JUDGMENTS_PATH)
                .param(ApiConstants.PAGE_NUMBER, "0")
                .param(ApiConstants.CC_COURT_ID, String.valueOf(testObjectContext.getCcCourtParentId()))
                .param(ApiConstants.CC_INCLUDE_DEPENDENT_COURT_JUDGMENTS, "true")
                .accept(MediaType.APPLICATION_JSON));
        
        // then
        assertOk(actions);
        actions
            .andExpect(jsonPath("$.items").value(iterableWithSize(1)))
            .andExpect(jsonPath("$.items.[0].id").value(equalsLong(testObjectContext.getCcJudgmentId())));
    }


    @Test
    public void showJudgments__it_should_show_request_parameters() throws Exception {
        //given
        int pageSize = 11;
        int pageNumber = 5;

        String sortingFieldValue = JudgmentIndexField.JUDGMENT_DATE.name();
        String sortingDirectionValue = Sorting.Direction.ASC.name();

        String allValue = "someAllValue";
        String judgmentDateFrom = "2010-01-21";
        String judgmentDateTo = "2020-10-13";
        String personnelTypeValue = PersonnelType.FIVE_PERSON.name();
        String judgmentFormValue = "someJudgmentForm";

        String legalBaseValue = "someLegalBase";
        String referencedRegulationValue = "someReferencedRegulation";
        String lawJournalEntryCodeValue = "2000/10/123";
        String judgeNameValue = "someJudgeName";
        String caseNumberValue = "someCaseNumber";
        String courtTypeValue = CourtType.ADMINISTRATIVE.name();
        String ccCourtTypeValue = CommonCourt.CommonCourtType.APPEAL.name();
        Boolean ccIncludeDependentCourtJudgments = true;

        Integer ccCourtIdValue = 33;
        String ccCourtCodeValue = "someCcCode";
        String ccCourtNameValue = "someCcName";

        Integer ccDivisionIdValue = 44;
        String ccDivisionCodeValue = "someCcDivisionCode";
        String ccDivisionNameValue = "someCcDivisionName";

        Integer scChamberIdValue = 55;
        String scChamberNameValue = "someScChamberName";
        Integer scDivisionIdValue = 66;
        String scDivisionNameValue = "someScDivisionName";

        String firstType = JudgmentType.DECISION.name();
        String secondType = JudgmentType.REGULATION.name();
        List<String> judgmentTypes = Arrays.asList(firstType, secondType);

        String firstKeyword="someFirstKeyword";
        String secondKeyword="someSecondKeyword";
        List<String> keywords = Arrays.asList(firstKeyword, secondKeyword);

        //when
        ResultActions actions = mockMvc.perform(get(JUDGMENTS_PATH)
                .param(ApiConstants.PAGE_SIZE, String.valueOf(pageSize))
                .param(ApiConstants.PAGE_NUMBER, String.valueOf(pageNumber))
                .param(ApiConstants.SORTING_FIELD, sortingFieldValue)
                .param(ApiConstants.SORTING_DIRECTION, sortingDirectionValue)
                .param(ApiConstants.ALL, allValue)
                .param(ApiConstants.SC_PERSONNEL_TYPE, personnelTypeValue)
                .param(ApiConstants.SC_JUDGMENT_FORM, judgmentFormValue)
                .param(ApiConstants.LEGAL_BASE, legalBaseValue)
                .param(ApiConstants.REFERENCED_REGULATION, referencedRegulationValue)
                .param(ApiConstants.LAW_JOURNAL_ENTRY_CODE, lawJournalEntryCodeValue)
                .param(ApiConstants.JUDGE_NAME, judgeNameValue)
                .param(ApiConstants.CASE_NUMBER, caseNumberValue)
                .param(ApiConstants.COURT_TYPE, courtTypeValue)
                .param(ApiConstants.CC_COURT_TYPE, ccCourtTypeValue)
                .param(ApiConstants.CC_COURT_ID, ccCourtIdValue.toString())
                .param(ApiConstants.CC_COURT_CODE, ccCourtCodeValue)
                .param(ApiConstants.CC_COURT_NAME, ccCourtNameValue)
                .param(ApiConstants.CC_DIVISION_ID, ccDivisionIdValue.toString())
                .param(ApiConstants.CC_DIVISION_CODE, ccDivisionCodeValue)
                .param(ApiConstants.CC_DIVISION_NAME, ccDivisionNameValue)
                .param(ApiConstants.CC_INCLUDE_DEPENDENT_COURT_JUDGMENTS, ccIncludeDependentCourtJudgments.toString())
                .param(ApiConstants.SC_CHAMBER_ID, scChamberIdValue.toString())
                .param(ApiConstants.SC_CHAMBER_NAME, scChamberNameValue)
                .param(ApiConstants.SC_DIVISION_ID, scDivisionIdValue.toString())
                .param(ApiConstants.SC_DIVISION_NAME, scDivisionNameValue)
                .param(ApiConstants.JUDGMENT_TYPES, judgmentTypes.toArray(new String[0]))
                .param(ApiConstants.KEYWORDS, keywords.toArray(new String[0]))
                .param(ApiConstants.JUDGMENT_DATE_FROM, judgmentDateFrom)
                .param(ApiConstants.JUDGMENT_DATE_TO, judgmentDateTo)
                .accept(MediaType.APPLICATION_JSON));

        //then
        assertOk(actions);

        String prefix = "$.queryTemplate";

        actions
                .andExpect(jsonPath(prefix + ".all").value(allValue))
                .andExpect(jsonPath(prefix + ".legalBase").value(legalBaseValue))
                .andExpect(jsonPath(prefix + ".referencedRegulation").value(referencedRegulationValue))
                .andExpect(jsonPath(prefix + ".lawJournalEntryCode.value").value(lawJournalEntryCodeValue))
                .andExpect(jsonPath(prefix + ".judgeName").value(judgeNameValue))
                .andExpect(jsonPath(prefix + ".caseNumber").value(caseNumberValue))
                .andExpect(jsonPath(prefix + ".courtType.value").value(courtTypeValue))

                .andExpect(jsonPath(prefix + ".ccCourtType.value").value(ccCourtTypeValue))
                .andExpect(jsonPath(prefix + ".ccCourtId").value(ccCourtIdValue))
                .andExpect(jsonPath(prefix + ".ccCourtCode").value(ccCourtCodeValue))
                .andExpect(jsonPath(prefix + ".ccCourtName").value(ccCourtNameValue))

                .andExpect(jsonPath(prefix + ".ccDivisionId").value(ccDivisionIdValue))
                .andExpect(jsonPath(prefix + ".ccDivisionCode").value(ccDivisionCodeValue))
                .andExpect(jsonPath(prefix + ".ccDivisionName").value(ccDivisionNameValue))
                
                .andExpect(jsonPath(prefix + ".ccIncludeDependentCourtJudgments").value(ccIncludeDependentCourtJudgments))

                .andExpect(jsonPath(prefix + ".scPersonnelType.value").value(personnelTypeValue))
                .andExpect(jsonPath(prefix + ".scJudgmentForm").value(judgmentFormValue))

                .andExpect(jsonPath(prefix + ".scChamberId").value(scChamberIdValue))
                .andExpect(jsonPath(prefix + ".scChamberName").value(scChamberNameValue))

                .andExpect(jsonPath(prefix + ".scDivisionId").value(scDivisionIdValue))
                .andExpect(jsonPath(prefix + ".scDivisionName").value(scDivisionNameValue))

                .andExpect(jsonPath(prefix + ".judgmentTypes.value.[0]").value(firstType))
                .andExpect(jsonPath(prefix + ".judgmentTypes.value.[1]").value(secondType))

                .andExpect(jsonPath(prefix + ".keywords.[0]").value(firstKeyword))
                .andExpect(jsonPath(prefix + ".keywords.[1]").value(secondKeyword))
                .andExpect(jsonPath(prefix + ".judgmentDateFrom.value").value(judgmentDateFrom))
                .andExpect(jsonPath(prefix + ".judgmentDateTo.value").value(judgmentDateTo))

                .andExpect(jsonPath(prefix + ".pageSize.value").value(pageSize))
                .andExpect(jsonPath(prefix + ".pageNumber.value").value(pageNumber))

                .andExpect(jsonPath(prefix + ".sortingField.value").value(sortingFieldValue))
                .andExpect(jsonPath(prefix + ".sortingDirection.value").value(sortingDirectionValue))
        ;
    }

    @Test
    public void showJudgments__it_should_show_only_pagination_and_sort_parameters_if_none_is_specified() throws Exception{
        //given
        int pageSize = 11;
        int pageNumber = 5;

        //when
        ResultActions actions = mockMvc.perform(get(JUDGMENTS_PATH)
                .param(ApiConstants.PAGE_SIZE, String.valueOf(pageSize))
                .param(ApiConstants.PAGE_NUMBER, String.valueOf(pageNumber))
                .accept(MediaType.APPLICATION_JSON));

        //then
        assertOk(actions);

        String prefix = "$.queryTemplate";

        actions

                .andExpect(jsonPath(prefix + ".pageSize.value").value(pageSize))
                .andExpect(jsonPath(prefix + ".pageNumber.value").value(pageNumber))

                .andExpect(jsonPath(prefix + ".all").value(isEmptyOrNullString()))
                .andExpect(jsonPath(prefix + ".legalBase").value(isEmptyOrNullString()))
                .andExpect(jsonPath(prefix + ".referencedRegulation").value(isEmptyOrNullString()))
                .andExpect(jsonPath(prefix + ".lawJournalEntryCode.value").value(isEmptyOrNullString()))
                .andExpect(jsonPath(prefix + ".judgeName").value(isEmptyOrNullString()))
                .andExpect(jsonPath(prefix + ".caseNumber").value(isEmptyOrNullString()))
                .andExpect(jsonPath(prefix + ".courtType.value").value(isEmptyOrNullString()))

                .andExpect(jsonPath(prefix + ".ccCourtType.value").value(isEmptyOrNullString()))
                .andExpect(jsonPath(prefix + ".ccCourtId").value(nullValue()))
                .andExpect(jsonPath(prefix + ".ccCourtCode").value(isEmptyOrNullString()))
                .andExpect(jsonPath(prefix + ".ccCourtName").value(isEmptyOrNullString()))

                .andExpect(jsonPath(prefix + ".ccDivisionId").value(nullValue()))
                .andExpect(jsonPath(prefix + ".ccDivisionCode").value(isEmptyOrNullString()))
                .andExpect(jsonPath(prefix + ".ccDivisionName").value(isEmptyOrNullString()))
                
                .andExpect(jsonPath(prefix + ".ccIncludeDependentCourtJudgments").value(nullValue()))

                .andExpect(jsonPath(prefix + ".scPersonnelType.value").value(isEmptyOrNullString()))
                .andExpect(jsonPath(prefix + ".scJudgmentForm").value(isEmptyOrNullString()))

                .andExpect(jsonPath(prefix + ".scChamberId").value(nullValue()))
                .andExpect(jsonPath(prefix + ".scChamberName").value(isEmptyOrNullString()))

                .andExpect(jsonPath(prefix + ".scDivisionId").value(nullValue()))
                .andExpect(jsonPath(prefix + ".scDivisionName").value(isEmptyOrNullString()))

                .andExpect(jsonPath(prefix + ".judgmentTypes.value").value(iterableWithSize(0)))

                .andExpect(jsonPath(prefix + ".keywords").value(iterableWithSize(0)))
                .andExpect(jsonPath(prefix + ".judgmentDateFrom.value").value(isEmptyOrNullString()))
                .andExpect(jsonPath(prefix + ".judgmentDateTo.value").value(isEmptyOrNullString()))

                .andExpect(jsonPath(prefix + ".sortingField.value").value(JudgmentIndexField.DATABASE_ID.name()))
                .andExpect(jsonPath(prefix + ".sortingDirection.value").value(Sorting.Direction.ASC.name()))
        ;

    }
    
    
    @Test
    public void it_should_not_allow_incorrect_date_from_format() throws Exception {
        //when
        ResultActions actions = mockMvc.perform(get(JUDGMENTS_PATH)
                .param(ApiConstants.JUDGMENT_DATE_FROM, "2011-11-10 20:23"));

        //then
        assertIncorrectValueError(actions, ApiConstants.JUDGMENT_DATE_FROM, "2011-11-10 20:23");
    }
    
    @Test
    public void it_should_not_allow_incorrect_date_to_format() throws Exception {
        //when
        ResultActions actions = mockMvc.perform(get(JUDGMENTS_PATH)
                .param(ApiConstants.JUDGMENT_DATE_TO, "2011-11-10 20:23"));

        //then
        assertIncorrectValueError(actions, ApiConstants.JUDGMENT_DATE_TO, "2011-11-10 20:23");
    }
    
    @Test
    public void it_should_not_allow_incorrect_sorting_field() throws Exception {
        //when
        ResultActions actions = mockMvc.perform(get(JUDGMENTS_PATH)
                .param(ApiConstants.SORTING_FIELD, "NOT_EXISTING_FIELD"));

        //then
        assertIncorrectValueError(actions, ApiConstants.SORTING_FIELD, "NOT_EXISTING_FIELD");
    }
    
    @Test
    public void it_should_not_allow_incorrect_sorting_direction() throws Exception {
        //when
        ResultActions actions = mockMvc.perform(get(JUDGMENTS_PATH)
                .param(ApiConstants.SORTING_FIELD, "AASC"));

        //then
        assertIncorrectValueError(actions, ApiConstants.SORTING_FIELD, "AASC");
    }
    
    @Test
    public void it_should_not_allow_incorrect_include_dependent_court_judgments() throws Exception {
        //when
        ResultActions actions = mockMvc.perform(get(JUDGMENTS_PATH)
                .param(ApiConstants.CC_INCLUDE_DEPENDENT_COURT_JUDGMENTS, "not boolean"));
        actions.andDo(MockMvcResultHandlers.print());
        //then
        assertIncorrectValueError(actions, ApiConstants.CC_INCLUDE_DEPENDENT_COURT_JUDGMENTS, "not boolean");
    }
    
    @Test
    public void it_should_not_allow_incorrect_personnel_type() throws Exception {
        //when
        ResultActions actions = mockMvc.perform(get(JUDGMENTS_PATH)
                .param(ApiConstants.SC_PERSONNEL_TYPE, "INCORRECT_PERSONNEL_TYPE"));

        //then
        assertIncorrectValueError(actions, ApiConstants.SC_PERSONNEL_TYPE, "INCORRECT_PERSONNEL_TYPE");
    }
    
    @Test
    public void it_should_not_allow_incorrect_cc_court_type() throws Exception {
        //when
        ResultActions actions = mockMvc.perform(get(JUDGMENTS_PATH)
                .param(ApiConstants.CC_COURT_TYPE, "INCORRECT_COURT_TYPE"));

        //then
        assertIncorrectValueError(actions, ApiConstants.CC_COURT_TYPE, "INCORRECT_COURT_TYPE");
    }
    
    @Test
    public void it_should_not_allow_incorrect_court_type() throws Exception {
        //when
        ResultActions actions = mockMvc.perform(get(JUDGMENTS_PATH)
                .param(ApiConstants.COURT_TYPE, "INCORRECT_COURT_TYPE"));

        //then
        assertIncorrectValueError(actions, ApiConstants.COURT_TYPE, "INCORRECT_COURT_TYPE");
    }
    
    @Test
    public void it_should_not_allow_incorrect_judgment_types() throws Exception {
        //when
        ResultActions actions = mockMvc.perform(get(JUDGMENTS_PATH)
                .param(ApiConstants.JUDGMENT_TYPES, new String[] {"DECISION", "RESOLUTION", "INCORRECT"}));

        //then
        assertIncorrectValueError(actions, ApiConstants.JUDGMENT_TYPES, "INCORRECT");
    }
    
    @Test
    public void it_should_not_allow_incorrect_law_journal_entry_code() throws Exception {
        //when
        ResultActions actions = mockMvc.perform(get(JUDGMENTS_PATH)
                .param(ApiConstants.LAW_JOURNAL_ENTRY_CODE, "2000/abc/123"));

        //then
        assertIncorrectValueError(actions, ApiConstants.LAW_JOURNAL_ENTRY_CODE, "2000/abc/123");
    }

    @Test
    public void showJudgments__it_should_show_info() throws Exception {

        //when
        ResultActions actions = mockMvc.perform(get(JUDGMENTS_PATH)
                .accept(MediaType.APPLICATION_JSON)
        );

        //then
        assertOk(actions);

        String prefix = "$.info";

        actions
                .andExpect(jsonPath(prefix+".totalResults").value(is(NR_OF_JUDGMENTS_STORED_IN_SOLR_INDEX)))
                ;
    }

    @Test
    public void showJudgments__it_should_not_show_next_link() throws Exception {
        //given
        int pageSize = 2;
        int pageNumber = NR_OF_JUDGMENTS_STORED_IN_SOLR_INDEX /pageSize;

        //when
        ResultActions actions = mockMvc.perform(get(JUDGMENTS_PATH)
                .param(ApiConstants.PAGE_SIZE, String.valueOf(pageSize))
                .param(ApiConstants.PAGE_NUMBER, String.valueOf(pageNumber))
                .accept(MediaType.APPLICATION_JSON)
        );

        //then
        assertOk(actions);

        String prefix = "$.links";

        actions
                .andExpect(jsonPath(prefix + "[?(@.rel==next)].href[0]").doesNotExist())
                ;
    }

    @Test
    public void showJudgments__it_should_show_next_link() throws Exception {
        //given
        int pageSize = 3;
        int nrOfElementsOnTheNextPage = 1;
        int pageNumber = (NR_OF_JUDGMENTS_STORED_IN_SOLR_INDEX - nrOfElementsOnTheNextPage) / pageSize - 1; // minus one as page numbers starts from 0


        //when
        ResultActions actions = mockMvc.perform(get(JUDGMENTS_PATH)
                .param(ApiConstants.PAGE_SIZE, String.valueOf(pageSize))
                .param(ApiConstants.PAGE_NUMBER, String.valueOf(pageNumber))
        );

        //then
        actions.andDo(MockMvcResultHandlers.print());
        assertOk(actions);

        String prefix = "$.links";

        actions
                .andExpect((jsonPath(prefix + "[?(@.rel==next)].href[0]").exists()))
        ;

    }

    
    @Test
    public void it_should_not_allow_incorrect_request_parameter_name() throws Exception {
        //when
        ResultActions actions = mockMvc.perform(get(JUDGMENTS_PATH)
                .param("some_incorrect_parameter_name", ""));

        //then
        assertIncorrectParamNameError(actions, "some_incorrect_parameter_name");
    }
    
    
    @Test
    public void it_should_not_allow_too_small_page_size() throws Exception {
        // when
        ResultActions actions = mockMvc.perform(get(JUDGMENTS_PATH)
                .param(ApiConstants.PAGE_SIZE, String.valueOf(1)));
        
        // then
        assertTooSmallPageSizeError(actions, 2);
    }
    
    @Test
    public void it_should_not_allow_too_big_page_size() throws Exception {
        // execute
        ResultActions actions = mockMvc.perform(get(JUDGMENTS_PATH)
                .param(ApiConstants.PAGE_SIZE, String.valueOf(101)));
        
        // assert
        assertTooBigPageSizeError(actions, 100);
    }
    
    @Test
    public void it_should_not_allow_invalid_page_size() throws Exception {
        // execute
        ResultActions actions = mockMvc.perform(get(JUDGMENTS_PATH)
                .param(ApiConstants.PAGE_SIZE, "abc"));
        
        // assert
        assertInvalidPageSizeError(actions, "abc");
    }
    
    
    @Test
    public void it_should_not_allow_invalid_page_number() throws Exception {
        // execute
        ResultActions actions = mockMvc.perform(get(JUDGMENTS_PATH)
                .param(ApiConstants.PAGE_NUMBER, "abc"));
        
        // assert
        assertInvalidPageNumberError(actions, "abc");
    }
    
    @Test
    public void it_should_not_allow_negative_page_number() throws Exception {
        // execute
        ResultActions actions = mockMvc.perform(get(JUDGMENTS_PATH)
                .param(ApiConstants.PAGE_NUMBER, "-1"));
        
        // assert
        assertNegativePageNumberError(actions);
    }
    
    @Test
    public void should_respond_in_iso8859_1_charset() throws Exception {
        // execute
        ResultActions actions = mockMvc.perform(get(JUDGMENTS_PATH)
                .accept(MediaType.APPLICATION_JSON+";charset=ISO-8859-1"));
        
        // assert
        assertOk(actions, "ISO-8859-1");
    }
    
    
    
    
}
