package pl.edu.icm.saos.persistence.search.implementor;

import org.springframework.stereotype.Service;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.search.dto.JudgmentSearchFilter;

import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

/**
 * @author pavtel
 */
@Service
public class JudgmentJpqlSearchImplementor extends AbstractJpqlSearchImplementor<JudgmentSearchFilter, Judgment> {



    @Override
    protected String createQuery(JudgmentSearchFilter searchFilter) {
        StringBuilder jpql = new StringBuilder(" select journal from " + Judgment.class.getName() + " journal ");
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
