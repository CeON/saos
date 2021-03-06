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

import pl.edu.icm.saos.enrichment.delete.JudgmentWithEnrichmentDeleter;
import pl.edu.icm.saos.persistence.content.JudgmentContentFileDeleter;
import pl.edu.icm.saos.persistence.model.CourtType;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.JudgmentResult;
import pl.edu.icm.saos.persistence.model.MeansOfAppeal;
import pl.edu.icm.saos.persistence.model.importer.RawSourceJudgment;
import pl.edu.icm.saos.persistence.repository.JudgmentResultRepository;
import pl.edu.icm.saos.persistence.repository.MeansOfAppealRepository;

/**
 * @author madryk
 */
@Service
public class JudgmentObjectDeleter {

    private static Logger log = LoggerFactory.getLogger(JudgmentObjectDeleter.class);
    
    
    @Autowired
    private EntityManager entityManager;
    
    @Autowired
    private JudgmentWithEnrichmentDeleter judgmentWithEnrichmentDeleter;
    
    @Autowired
    private JudgmentContentFileDeleter judgmentContentFileDeleter;
    
    @Autowired
    private MeansOfAppealRepository meansOfAppealRepository;
    
    @Autowired
    private JudgmentResultRepository judgmentResultRepository;
    
    
    
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
            judgmentWithEnrichmentDeleter.delete(judgmentIdsWithContentPath.stream()
                    .map(x -> (Long)x[0])
                    .collect(Collectors.toList()));
            
            judgmentContentFileDeleter.deleteContents(judgmentIdsWithContentPath.stream()
                    .map(x -> (String)x[1])
                    .filter(path -> StringUtils.isNotEmpty(path))
                    .collect(Collectors.toList()));
        }
    }
    
    
    /**
     * Deletes {@link MeansOfAppeal} which are not referenced from any judgment.
     * 
     * @param courtType of means of appeal ({@link MeansOfAppeal#getCourtType()})
     *      that will be checked for reference existence
     */
    @Transactional
    public void deleteMeansOfAppealWithoutJudgments(CourtType courtType) {
        
        log.debug("Deleting meansOfAppeal (with type {}) without referring judgments ", courtType.name());
        
        
        String q = "select meansOfAppeal.id from " + MeansOfAppeal.class.getName() + " meansOfAppeal " +
                    " where meansOfAppeal.courtType = :courtType " +
                    " and not exists (select judgment from "+ Judgment.class.getName() + " judgment " +
                                            " where judgment.meansOfAppeal.id = meansOfAppeal.id)";
        @SuppressWarnings("unchecked")
        List<Long> meansOfAppealIds = entityManager.createQuery(q)
                .setParameter("courtType", courtType)
                .getResultList();
        
        meansOfAppealIds.stream().forEach(meansOfAppealRepository::delete);
        
        meansOfAppealRepository.flush();
        
    }
    
    /**
     * Deletes {@link JudgmentResult} which are not referenced from any judgment.
     * 
     * @param courtType of means of appeal ({@link JudgmentResult#getCourtType()})
     *      that will be checked for reference existence
     */
    @Transactional
    public void deleteJudgmentResultsWithoutJudgments(CourtType courtType) {
        
        log.debug("Deleting judgmentResults (with type {}) without referring judgments ", courtType.name());
        
        
        String q = "select judgmentResult.id from " + JudgmentResult.class.getName() + " judgmentResult " +
                    " where judgmentResult.courtType = :courtType " +
                    " and not exists  (select judgment from "+ Judgment.class.getName() + " judgment " +
                                            " where judgment.judgmentResult.id = judgmentResult.id)";
        @SuppressWarnings("unchecked")
        List<Long> judgmentResultIds = entityManager.createQuery(q)
                .setParameter("courtType", courtType)
                .getResultList();
        
        judgmentResultIds.stream().forEach(judgmentResultRepository::delete);
        
        judgmentResultRepository.flush();
        
    }
    
}
