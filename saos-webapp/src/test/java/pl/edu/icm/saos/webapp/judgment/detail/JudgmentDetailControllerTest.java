package pl.edu.icm.saos.webapp.judgment.detail;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.reflect.Whitebox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;
import pl.edu.icm.saos.webapp.WebappTestConfiguration;

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
public class JudgmentDetailControllerTest {

	@Autowired
    private WebApplicationContext webApplicationCtx;
	
	private MockMvc mockMvc;
	
    @Autowired
    @InjectMocks
    private JudgmentDetailController judgmentDetailController;
	
	@Mock
	private JudgmentRepository judgmentRepository;
    
	private CommonCourtJudgment judgment = getCcJudgment();
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		
		when(judgmentRepository.findOneAndInitialize(judgment.getId())).thenReturn(judgment);
		
		mockMvc = webAppContextSetup(webApplicationCtx)
					.build();
	}
	
	
	//------------------------ TESTS --------------------------	
	
	@Test
	public void showJudgmentDetail() throws Exception {
	
		mockMvc.perform(get("/judgments/" + judgment.getId()))
			.andExpect(status().isOk())
			.andExpect(view().name("judgmentDetails"))
			.andExpect(model().attribute("judgment", judgment));
		
		verify(judgmentRepository, times(1)).findOneAndInitialize(judgment.getId());
	}

	
	//------------------------ PRIVATE --------------------------
	
	private CommonCourtJudgment getCcJudgment() {
		
		CommonCourtJudgment ccJudgment = new CommonCourtJudgment();
		
		Whitebox.setInternalState(ccJudgment, "id", 28);
		
		return ccJudgment;
	}

}
