package pl.edu.icm.saos.webapp.keyword;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
import pl.edu.icm.saos.persistence.model.CourtType;
import pl.edu.icm.saos.persistence.model.JudgmentKeyword;
import pl.edu.icm.saos.persistence.repository.JudgmentKeywordRepository;
import pl.edu.icm.saos.webapp.WebappTestConfiguration;
import pl.edu.icm.saos.webapp.court.CcListController;
import pl.edu.icm.saos.webapp.court.CcListService;
import pl.edu.icm.saos.webapp.court.SimpleDivision;

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
public class KeywordControllerTest {

	@Autowired
    private WebApplicationContext webApplicationCtx;
	
	private MockMvc mockMvc;
	
    @Autowired
    @InjectMocks
    private KeywordController keywordController;
	
    @Mock
	private JudgmentKeywordRepository judgmentKeywordRepository;
	
    @Mock
	private SimpleKeywordConverter simpleKeywordConverter;
    
    private List<JudgmentKeyword> testKeywords;
    private CourtType courtType = CourtType.COMMON;
    private String phrase = "opinia";
    
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		
		testKeywords = Lists.newArrayList();
		
		when(judgmentKeywordRepository.findAllByCourtTypeAndPhrasePart(courtType, phrase)).thenReturn(testKeywords);
		when(simpleKeywordConverter.convertJudgmentKeywords(testKeywords)).thenReturn(getTestKeywords());
		
		mockMvc = webAppContextSetup(webApplicationCtx)
					.build();
	}
	
	
	//------------------------ TESTS --------------------------
	
	@Test
	public void listJudgmentKeywords() throws Exception {
		//when
        ResultActions actions = mockMvc.perform(get("/keywords/" + courtType.toString() + "/" + phrase)
                .accept(MediaType.APPLICATION_JSON));
                
        
        //then
        actions
		        .andExpect(status().isOk())
		        .andExpect(jsonPath("$.[0].id").value(1))
				.andExpect(jsonPath("$.[0].phrase").value("opinia prawna"))
		        .andExpect(jsonPath("$.[1].id").value(2))
				.andExpect(jsonPath("$.[1].phrase").value("opinia o wykroczeniu"));
                
        verify(judgmentKeywordRepository, times(1)).findAllByCourtTypeAndPhrasePart(courtType, phrase);
        verify(simpleKeywordConverter, times(1)).convertJudgmentKeywords(testKeywords);
	}
	
	
	//------------------------ PRIVATE --------------------------

	private List<SimpleKeyword> getTestKeywords() {
		SimpleKeyword keywordOne = new SimpleKeyword();
		SimpleKeyword keywordTwo = new SimpleKeyword();
		keywordOne.setId(1);
		keywordOne.setPhrase("opinia prawna");
		keywordTwo.setId(2);
		keywordTwo.setPhrase("opinia o wykroczeniu");
		
		return Lists.newArrayList(keywordOne, keywordTwo);
	}
}
