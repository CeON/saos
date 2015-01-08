package pl.edu.icm.saos.webapp.judgment.search;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.common.TestObjectContext;
import pl.edu.icm.saos.persistence.common.TestPersistenceObjectFactory;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;
import pl.edu.icm.saos.persistence.repository.ScJudgmentFormRepository;
import pl.edu.icm.saos.webapp.WebappTestConfiguration;
import pl.edu.icm.saos.webapp.court.CcListService;
import pl.edu.icm.saos.webapp.court.ScListService;

/**
 * 
 * @author Łukasz Pawełczak
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextHierarchy({ 
	@ContextConfiguration(classes = WebappTestConfiguration.class) })
@Category(SlowTest.class)
public class JudgmentSearchControllerTest {

	
	@Autowired
	private WebApplicationContext webApplicationCtx;
	
	private MockMvc mockMvc;
	
	@Autowired
	@InjectMocks
	private JudgmentSearchController judgmentSearchController;
	
	@Mock
	private JudgmentRepository judgmentRepository;
	
	@Mock
	private ScJudgmentFormRepository scJudgmentFormRepository;
	
	@Mock
	private CcListService ccListService;
	
	@Mock
	private ScListService scListService;
	
	@Mock
	private JudgmentWebSearchService judgmentsWebSearchService;
	
	@Autowired
	private TestPersistenceObjectFactory testPersistenceObjectFactory;
	
	private TestObjectContext testObjectContext;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		
		testObjectContext = testPersistenceObjectFactory.createTestObjectContext();
		
		
		when(ccListService.findCommonCourts()).thenReturn();
		
		when(judgmentRepository.findOneAndInitialize(testObjectContext.getCcJudgmentId())).thenReturn(testObjectContext.getCcJudgment());
		
		mockMvc = webAppContextSetup(webApplicationCtx)
					.build();
	}
	
	
	//------------------------ TESTS --------------------------
	
	
	@Test
	public void judgmentSearchResults() throws Exception {
	
		mockMvc.perform(get("/judgments/" + testObjectContext.getCcJudgmentId()))
			.andExpect(status().isOk())
			.andExpect(view().name("judgmentDetails"))
			.andExpect(model().attribute("judgment", testObjectContext.getCcJudgment()));
		
	}

}
