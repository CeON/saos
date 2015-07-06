package pl.edu.icm.saos.api.entry.point;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static pl.edu.icm.saos.api.ApiResponseAssertUtils.assertOk;
import static pl.edu.icm.saos.api.ApiResponseAssertUtils.assertPageNotFoundError;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import pl.edu.icm.saos.api.ApiTestSupport;
import pl.edu.icm.saos.api.dump.court.DumpCommonCourtsController;
import pl.edu.icm.saos.api.dump.court.DumpCourtsListSuccessRepresentationBuilder;
import pl.edu.icm.saos.api.search.parameters.ParametersExtractor;
import pl.edu.icm.saos.api.services.interceptor.AccessControlHeaderHandlerInterceptor;
import pl.edu.icm.saos.api.single.court.CommonCourtController;
import pl.edu.icm.saos.api.single.court.SingleCourtSuccessRepresentationBuilder;
import pl.edu.icm.saos.common.json.JsonFormatter;
import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.common.TestObjectContext;
import pl.edu.icm.saos.persistence.common.TestPersistenceObjectFactory;
import pl.edu.icm.saos.persistence.repository.CommonCourtRepository;
import pl.edu.icm.saos.persistence.search.DatabaseSearchService;

/**
 * @author madryk
 */
@Category(SlowTest.class)
public class NotExistingPageTest extends ApiTestSupport {

    @Autowired
    @Qualifier("apiMessageSource")
    private MessageSource apiMessageService;

    @Autowired
    private ParametersExtractor parametersExtractor;

    @Autowired
    private DatabaseSearchService databaseSearchService;

    @Autowired
    private CommonCourtRepository courtRepository;

    @Autowired
    private DumpCourtsListSuccessRepresentationBuilder dumpCourtsListSuccessRepresentationBuilder;

    @Autowired
    private SingleCourtSuccessRepresentationBuilder singleCourtSuccessRepresentationBuilder;

    @Autowired
    private JsonFormatter jsonFormatter;


    @Autowired
    private TestPersistenceObjectFactory testPersistenceObjectFactory;


    private TestObjectContext testObjectContext;


    private MockMvc mockMvc;


    @Before
    public void setUp() {
        testObjectContext = testPersistenceObjectFactory.createTestObjectContext();

        MainEntryPointController mainEntryPointController = new MainEntryPointController();
        mainEntryPointController.setApiMessageService(apiMessageService);
        mainEntryPointController.setJsonFormatter(jsonFormatter);

        CommonCourtController commonCourtController = new CommonCourtController();
        commonCourtController.setCourtRepository(courtRepository);
        commonCourtController.setSingleCourtSuccessRepresentationBuilder(singleCourtSuccessRepresentationBuilder);
        commonCourtController.setJsonFormatter(jsonFormatter);

        DumpCommonCourtsController dumpCommonCourtsController = new DumpCommonCourtsController();
        dumpCommonCourtsController.setParametersExtractor(parametersExtractor);
        dumpCommonCourtsController.setDatabaseSearchService(databaseSearchService);
        dumpCommonCourtsController.setDumpCourtsListSuccessRepresentationBuilder(dumpCourtsListSuccessRepresentationBuilder);
        dumpCommonCourtsController.setJsonFormatter(jsonFormatter);
        
        NotExistingPageController notExisitingPageController = new NotExistingPageController();
        mainEntryPointController.setJsonFormatter(jsonFormatter);


        mockMvc = standaloneSetup(
                    mainEntryPointController,
                    commonCourtController,
                    dumpCommonCourtsController,
                    notExisitingPageController
                )
                .addInterceptors(new AccessControlHeaderHandlerInterceptor())
                .build();

    }

    //------------------------ TESTS --------------------------

    @Test
    public void should_answer_ok() throws Exception {
        // execute & assert
        requestPageAndAssertOk("/api");
        requestPageAndAssertOk("/api/commonCourts/" + testObjectContext.getCcCourtId());
        requestPageAndAssertOk("/api/dump/commonCourts");
    }
    
    @Test
    public void should_answer_with_page_not_found() throws Exception {
        // execute & assert
        requestPageAndAssertPageNotFound("/api/invalidpath");
        requestPageAndAssertPageNotFound("/api/commonCourts/1/invalid");
        requestPageAndAssertPageNotFound("/api/dump");
        requestPageAndAssertPageNotFound("/api/dump/commonCourts/a");
        requestPageAndAssertPageNotFound("/api/dump/common");
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private void requestPageAndAssertOk(String path) throws Exception {
        
        ResultActions actions = mockMvc.perform(get(path)
                .accept(MediaType.APPLICATION_JSON));
        
        assertOk(actions);
    }
    
    private void requestPageAndAssertPageNotFound(String path) throws Exception {
        
        ResultActions actions = mockMvc.perform(get(path)
                .accept(MediaType.APPLICATION_JSON));
        
        assertPageNotFoundError(actions);
    }

}
