package pl.edu.icm.saos.webapp.court;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

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
    
    
    
    
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		
		when(scListService.findScChamberDivisions(1)).thenReturn(getTestDivisions());
		
		mockMvc = webAppContextSetup(webApplicationCtx)
					.build();
	}
	
	
	//------------------------ TESTS --------------------------
	
	@Test
	public void listChamberDivisions() throws Exception {
		//when
        ResultActions actions = mockMvc.perform(get("/sc/chambers/1/chamberDivisions/list")
                .accept(MediaType.APPLICATION_JSON));
                
        
        //then
        actions
		        .andExpect(status().isOk())
		        .andExpect(jsonPath("$.[0].id").value("3"))
				.andExpect(jsonPath("$.[0].name").value("Izba 3"))
		        .andExpect(jsonPath("$.[1].id").value("4"))
				.andExpect(jsonPath("$.[1].name").value("Izba Najwyższa"));
                
	}
	
	
	//------------------------ PRIVATE --------------------------

	private List<SimpleDivision> getTestDivisions() {
		SimpleDivision divisionOne = new SimpleDivision();
		SimpleDivision divisionTwo = new SimpleDivision();
		divisionOne.setId("3");
		divisionOne.setName("Izba 3");
		divisionTwo.setId("4");
		divisionTwo.setName("Izba Najwyższa");
		
		return Lists.newArrayList(divisionOne, divisionTwo);
	}
}
