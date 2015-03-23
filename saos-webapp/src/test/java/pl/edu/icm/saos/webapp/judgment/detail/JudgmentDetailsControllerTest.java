package pl.edu.icm.saos.webapp.judgment.detail;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.util.List;

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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.enrichment.apply.JudgmentEnrichmentService;
import pl.edu.icm.saos.persistence.correction.model.CorrectedProperty;
import pl.edu.icm.saos.persistence.correction.model.JudgmentCorrection;
import pl.edu.icm.saos.persistence.correction.model.JudgmentCorrectionBuilder;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.webapp.WebappTestConfiguration;
import pl.edu.icm.saos.webapp.judgment.detail.correction.JudgmentCorrectionService;

import com.google.common.collect.Lists;

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
public class JudgmentDetailsControllerTest {

	@Autowired
    private WebApplicationContext webApplicationCtx;
	
	private MockMvc mockMvc;
	
    @Autowired
    @InjectMocks
    private JudgmentDetailsController judgmentDetailController;
	
	@Mock
	private JudgmentEnrichmentService judgmentEnrichmentService;
	
	@Mock
	private JudgmentCorrectionService judgmentCorrectionService;
	
	@Mock
	private JudgmentDetailsSortService judgmentDetailsSortService;
    
	private CommonCourtJudgment judgment = getCcJudgment();
	private List<JudgmentCorrection> judgmentCorrections = getJudgmentCorrections();
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		
		when(judgmentEnrichmentService.findOneAndEnrich(judgment.getId())).thenReturn(judgment);
		when(judgmentDetailsSortService.sortJudges(judgment)).thenReturn(judgment);
		when(judgmentCorrectionService.findAllByJudgmentIdSorted(judgment.getId())).thenReturn(judgmentCorrections);
		
		mockMvc = webAppContextSetup(webApplicationCtx)
					.build();
	}
	
	
	//------------------------ TESTS --------------------------	
	
	@Test
	public void showJudgmentDetails() throws Exception {
	
		//execute
		ResultActions actions = mockMvc.perform(get("/judgments/" + judgment.getId()));
		
		
		//assert
		actions
			.andExpect(status().isOk())
			.andExpect(view().name("judgmentDetails"))
			.andExpect(model().attribute("judgment", judgment))
			.andExpect(model().attribute("corrections", judgmentCorrections));
		
		verify(judgmentEnrichmentService).findOneAndEnrich(judgment.getId());
		verify(judgmentDetailsSortService).sortJudges(judgment);
		verify(judgmentCorrectionService).findAllByJudgmentIdSorted(judgment.getId());
	}

	
	//------------------------ PRIVATE --------------------------
	
	private CommonCourtJudgment getCcJudgment() {
		
		CommonCourtJudgment ccJudgment = new CommonCourtJudgment();
		
		Whitebox.setInternalState(ccJudgment, "id", 28);
		
		return ccJudgment;
	}
	
	private List<JudgmentCorrection> getJudgmentCorrections() {

		JudgmentCorrectionBuilder judgmentCorrectionBuilder = JudgmentCorrectionBuilder.createFor(judgment);
		
		JudgmentCorrection jc = judgmentCorrectionBuilder.update(judgment)
															.property(CorrectedProperty.JUDGMENT_TYPE)
															.newValue("SENTENCE")
															.oldValue("SENTENCE, REASON")
															.build();
		
		return Lists.newArrayList(jc);
	}

}
