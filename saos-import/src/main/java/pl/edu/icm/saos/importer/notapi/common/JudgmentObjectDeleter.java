package pl.edu.icm.saos.importer.notapi.common;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.jadira.usertype.spi.utils.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.content.JudgmentContentFileDeleter;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.importer.RawSourceJudgment;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;

/**
 * @author madryk
 */
@Service
public class JudgmentObjectDeleter {

    private static Logger log = LoggerFactory.getLogger(JudgmentObjectDeleter.class);
    
    
    @Autowired
    private EntityManager entityManager;
    
    @Autowired
    private JudgmentRepository judgmentRepository;
    
    @Autowired
    private JudgmentContentFileDeleter judgmentContentFileDeleter;
    
    
    
    /**
     * Deletes judgments that do not have corresponding raw source judgment.
     * 
     * @param judgmentClass - class of judgments to remove
     * @param rawJudgmentClass - class of corresponding raw source judgment
     */
    @Transactional
    public void deleteJudgmentsWithoutRawSourceJudgment(Class<? extends Judgment> judgmentClass, Class<? extends RawSourceJudgment> rawJudgmentClass) {
        
        log.debug("Deleting judgments ({}) without corresponding rawSourceJudgments ({})", judgmentClass.getName(), rawJudgmentClass.getName());
        
        String q = "select judgment.id, content.filePath from " + judgmentClass.getName() + " judgment " +
                " left join judgment.textContent content" +
                " where not exists  (select rJudgment from "+ rawJudgmentClass.getName() + " rJudgment " +
                                        " where rJudgment.sourceId = judgment.sourceInfo.sourceJudgmentId)";
        
        List<Object[]> judgmentIdsWithContentPath = entityManager.createQuery(q, Object[].class).getResultList();

        if (!judgmentIdsWithContentPath.isEmpty()) {
            judgmentRepository.delete(judgmentIdsWithContentPath.stream()
                    .map(x -> (Long)x[0])
                    .collect(Collectors.toList()));
            
            judgmentContentFileDeleter.deleteContents(judgmentIdsWithContentPath.stream()
                    .map(x -> (String)x[1])
                    .filter(path -> StringUtils.isNotEmpty(path))
                    .collect(Collectors.toList()));
        }
    }
    
}
