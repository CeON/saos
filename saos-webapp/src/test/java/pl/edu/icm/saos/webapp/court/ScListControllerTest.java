package pl.edu.icm.saos.webapp.court;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static pl.edu.icm.saos.common.testcommon.IntToLongMatcher.equalsLong;

import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgmentForm;
import pl.edu.icm.saos.persistence.repository.ScJudgmentFormRepository;
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
public class ScListControllerTest {

    @Autowired
    private WebApplicationContext webApplicationCtx;
	
    private MockMvc mockMvc;
	
    @Autowired
    @InjectMocks
    private ScListController scListController;
	
    @Mock
    private ScListService scListService;
    
    @Mock
    private ScJudgmentFormRepository scJudgmentFormRepository;
    
    @Mock
    private SimpleEntityConverter simpleEntityConverter;
    
    private long chamberId = 1;
    
    private TestCourtsFactory testCourtsFactory = new TestCourtsFactory();
    
    private List<SimpleEntity> simpleEntities = testCourtsFactory.getSimpleEntities();
    
    private List<SupremeCourtJudgmentForm> scJudgmentForms = Lists.newArrayList();
    
    
    @Before
    public void setUp() {
	MockitoAnnotations.initMocks(this);
		
	when(scListService.findScChambers()).thenReturn(simpleEntities);
	when(scListService.findScChamberDivisions(chamberId)).thenReturn(simpleEntities);
	when(scJudgmentFormRepository.findAll()).thenReturn(scJudgmentForms);
	when(simpleEntityConverter.convertScJudgmentForms(scJudgmentForms)).thenReturn(simpleEntities);
		
		
	mockMvc = webAppContextSetup(webApplicationCtx)
			.build();
    }
	
	
    //------------------------ TESTS --------------------------
	
    @Test
    public void listScChambers() throws Exception {
	//when
	ResultActions actions = mockMvc.perform(get("/sc/chambers/list")
		.accept(MediaType.APPLICATION_JSON));
                
        
	//then
	actions
		.andExpect(status().isOk())
            	.andExpect(jsonPath("$.[0].id").value(equalsLong(simpleEntities.get(0).getId())))
        	.andExpect(jsonPath("$.[0].name").value(simpleEntities.get(0).getName()))
        	.andExpect(jsonPath("$.[1].id").value(equalsLong(simpleEntities.get(1).getId())))
        	.andExpect(jsonPath("$.[1].name").value(simpleEntities.get(1).getName()));
     
        
	verify(scListService, times(1)).findScChambers();
    }
    
    
    @Test
    public void listScChamberDivisions() throws Exception {
	//when
	ResultActions actions = mockMvc.perform(get("/sc/chambers/" + chamberId + "/chamberDivisions/list")
			.accept(MediaType.APPLICATION_JSON));
                    
            
	//then
	actions
		.andExpect(status().isOk())
    		.andExpect(jsonPath("$.[0].id").value(equalsLong(simpleEntities.get(0).getId())))
    		.andExpect(jsonPath("$.[0].name").value(simpleEntities.get(0).getName()))
    		.andExpect(jsonPath("$.[1].id").value(equalsLong(simpleEntities.get(1).getId())))
    		.andExpect(jsonPath("$.[1].name").value(simpleEntities.get(1).getName()));
     
        
	verify(scListService, times(1)).findScChamberDivisions(chamberId);
    }
	
    
    @Test
    public void listScJudgmentForms() throws Exception {
	//when
	ResultActions actions = mockMvc.perform(get("/sc/judgmentForms/list")
		.accept(MediaType.APPLICATION_JSON));
            
    
	//then
	actions
        	.andExpect(status().isOk())
        	.andExpect(jsonPath("$.[0].id").value(equalsLong(simpleEntities.get(0).getId())))
        	.andExpect(jsonPath("$.[0].name").value(simpleEntities.get(0).getName()))
        	.andExpect(jsonPath("$.[1].id").value(equalsLong(simpleEntities.get(1).getId())))
        	.andExpect(jsonPath("$.[1].name").value(simpleEntities.get(1).getName()));
 
    
	verify(scJudgmentFormRepository, times(1)).findAll();
	verify(simpleEntityConverter, times(1)).convertScJudgmentForms(scJudgmentForms);
    }

}
