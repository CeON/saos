package pl.edu.icm.saos.webapp.keyword;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.edu.icm.saos.webapp.services.KeywordsWebService;

/**
 * @author Łukasz Pawełczak
 *
 */
@Controller
public class KeywordController {

	
	@Autowired
	private KeywordsWebService keywordsWebService;
	
	@Autowired
    private SimpleKeywordConverter simpleKeywordConverter;
	
	
    //------------------------ LOGIC --------------------------	
	
	@RequestMapping("/ccKeywords/{phrase}")
	@ResponseBody
	public List<SimpleKeyword> ccKeywords(@PathVariable("phrase") String phrase) {
		return simpleKeywordConverter.convertCcJudgmentKeywords(keywordsWebService.getCcJudgmentKeywords(phrase));
	}
	
	
}

