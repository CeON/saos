package pl.edu.icm.saos.importer.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.CourtType;
import pl.edu.icm.saos.persistence.model.JudgmentKeyword;
import pl.edu.icm.saos.persistence.repository.JudgmentKeywordRepository;

/**
 * @author Łukasz Dumiszewski
 */
@Service("judgmentKeywordCreator")
public class JudgmentKeywordCreator {

    
    private JudgmentKeywordRepository judgmentKeywordRepository;
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Returns {@link JudgmentKeyword} with {@link JudgmentKeyword#getCourtType()}
     * equal to courtType and {@link JudgmentKeyword#getPhrase()} equal to phrase (case is ignored).
     * If the appropriate judgmentKeyword cannot be found, then one is created, saved in a datasource and returned<br/>
     * 
     * @see JudgmentKeyword#JudgmentKeyword(CourtType, String) 
     */
    public JudgmentKeyword getOrCreateJudgmentKeyword(CourtType courtType, String phrase) {
        JudgmentKeyword keyword = judgmentKeywordRepository.findOneByCourtTypeAndPhraseIgnoreCase(courtType, phrase);
        if (keyword == null) {
            keyword = new JudgmentKeyword(courtType, phrase);
            judgmentKeywordRepository.save(keyword);
            judgmentKeywordRepository.flush();
        }
        return keyword;
    }

    
    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setJudgmentKeywordRepository(JudgmentKeywordRepository judgmentKeywordRepository) {
        this.judgmentKeywordRepository = judgmentKeywordRepository;
    }

    
    
    
    
}
