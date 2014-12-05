package pl.edu.icm.saos.webapp.keyword;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.edu.icm.saos.persistence.model.CourtType;
import pl.edu.icm.saos.persistence.repository.JudgmentKeywordRepository;

/**
 * @author Łukasz Pawełczak
 *
 */
@Controller
public class KeywordController {

	
	@Autowired
	private JudgmentKeywordRepository judgmentKeywordRepository;
	
	@Autowired
	private SimpleKeywordConverter simpleKeywordConverter;
	
	
    //------------------------ LOGIC --------------------------	
	
	@RequestMapping("/keywords/{courtType}/{phrase}")
	@ResponseBody
	public List<SimpleKeyword> searchKeywords(@PathVariable("courtType") CourtType courtType, @PathVariable("phrase") String phrase) {
		return simpleKeywordConverter.convertJudgmentKeywords(judgmentKeywordRepository.findAllByCourtTypeAndPhrasePart(courtType, phrase));
	}
	
	
}

