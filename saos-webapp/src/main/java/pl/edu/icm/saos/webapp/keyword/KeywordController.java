package pl.edu.icm.saos.webapp.keyword;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.edu.icm.saos.persistence.repository.CcJudgmentKeywordRepository;

/**
 * @author Łukasz Pawełczak
 *
 */
@Controller
public class KeywordController {

	
	@Autowired
	private CcJudgmentKeywordRepository ccJudgmentKeywordRepository;
	
	@Autowired
    private SimpleKeywordConverter simpleKeywordConverter;
	
	
    //------------------------ LOGIC --------------------------	
	
	@RequestMapping("/keywords")
	@ResponseBody
	public List<SimpleKeyword> keywords() {
		return simpleKeywordConverter.convertCcJudgmentKeywords(ccJudgmentKeywordRepository.findAll());
	}
	
	
}

