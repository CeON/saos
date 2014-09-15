package pl.edu.icm.saos.persistence.search.implementor;

import static com.google.common.collect.Maps.newHashMap;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.search.dto.JudgmentSearchFilter;
import pl.edu.icm.saos.persistence.search.result.SearchResult;

import com.google.common.collect.Maps;

/**
 * @author pavtel
 */
@Service
public class JudgmentJpqlSearchImplementor extends AbstractJpqlSearchImplementor<JudgmentSearchFilter, Judgment> {

    @Autowired
    private EntityManager entityManager;


    @Override
    protected String createQuery(JudgmentSearchFilter searchFilter) {
        StringBuilder jpql = new StringBuilder(" select judgment from " + Judgment.class.getName() + " judgment ");
        return appendConditions(searchFilter, jpql);
    }

    @Override
    protected Map<String, Object> createParametersMap(JudgmentSearchFilter searchFilter) {
        Map<String, Object> map = newHashMap();
        if(searchFilter.getStartDate()!=null){
            map.put("startDate", searchFilter.getStartDate());
        }

        if(searchFilter.getEndDate()!=null){
            map.put("endDate", searchFilter.getEndDate());
        }

        return map;
    }
    
    @Override
    protected void processResult(SearchResult<Judgment> searchResult, JudgmentSearchFilter searchFilter) {
        
        initializeCourtCases(searchResult);
        
        
    }

   

    
    //------------------------ PRIVATE --------------------------
   
    private void initializeCourtCases(SearchResult<Judgment> searchResult) {
        StringBuilder jpql = new StringBuilder(" select judgment from " + Judgment.class.getName() + " judgment join fetch judgment.courtCases_ courtCase where judgment.id in (:judgmentIds) ");
        Query queryObject = entityManager.createQuery(jpql.toString());
        
        Map<String, Object> parametersMap = Maps.newHashMap();
        parametersMap.put("judgmentIds", extractJudgmentIds(searchResult));
        parametersMap.forEach(queryObject::setParameter);
        queryObject.getResultList();
    }
    
    private List<Integer> extractJudgmentIds(SearchResult<Judgment> searchResult) {
        return searchResult.getResultRecords().stream().map(result->result.getId()).collect(Collectors.toList());
    }

    private  String appendConditions(JudgmentSearchFilter searchFilter, StringBuilder jpql) {
        jpql.append(" where 1=1 ");

        if(searchFilter.getStartDate() != null){
            jpql.append(" and judgmentDate >= :startDate");
        }

        if(searchFilter.getEndDate() != null){
            jpql.append(" and judgmentDate <= :endDate");
        }

        return jpql.toString();
    }
}
