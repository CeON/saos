package pl.edu.icm.saos.webapp.court;

import static org.hamcrest.Matchers.endsWith;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static pl.edu.icm.saos.api.services.Constants.SINGLE_JUDGMENTS_PATH;
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
public class CcListControllerTest {

	@Autowired
    private WebApplicationContext webApplicationCtx;
	
	private MockMvc mockMvc;
	
    @Autowired
    @InjectMocks
    private CcListController ccListController;
	
    @Mock
    private CcListService ccListService;
    
    
    
    
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		
		when(ccListService.findCcDivisions(1)).thenReturn(getTestDivisions());
		
		mockMvc = webAppContextSetup(webApplicationCtx)
					.build();
	}
	
	
	//------------------------ TESTS --------------------------
	
	@Test
	public void listCourtDivisions() throws Exception {
		//when
        ResultActions actions = mockMvc.perform(get("/cc/courts/1/courtDivisions/list")
                .accept(MediaType.APPLICATION_JSON));
                
        
        //then
        actions
		        .andExpect(status().isOk())
		        .andExpect(jsonPath("$.[0].id").value("1"))
				.andExpect(jsonPath("$.[0].name").value("Wydzial 1"))
		        .andExpect(jsonPath("$.[1].id").value("23"))
				.andExpect(jsonPath("$.[1].name").value("Wydział 23 Karny"));
                
	}
	
	
	//------------------------ PRIVATE --------------------------

	private List<SimpleDivision> getTestDivisions() {
		SimpleDivision divisionOne = new SimpleDivision();
		SimpleDivision divisionTwo = new SimpleDivision();
		divisionOne.setId("1");
		divisionOne.setName("Wydzial 1");
		divisionTwo.setId("23");
		divisionTwo.setName("Wydział 23 Karny");
		
		return Lists.newArrayList(divisionOne, divisionTwo);
	}
}
