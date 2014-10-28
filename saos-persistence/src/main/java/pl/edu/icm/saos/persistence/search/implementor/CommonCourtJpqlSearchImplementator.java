package pl.edu.icm.saos.persistence.search.implementor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.search.dto.CommonCourtSearchFilter;
import pl.edu.icm.saos.persistence.search.result.SearchResult;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author pavtel
 */
@Service
public class CommonCourtJpqlSearchImplementator extends AbstractJpqlSearchImplementor<CommonCourtSearchFilter, CommonCourt> {

    //**** fields ******

    @Autowired
    private EntityManager entityManager;


    //****** END fields ***********


    //********* AbstractStringQuerySearchImplementor implementation *****************

    @Override
    protected String createQuery(CommonCourtSearchFilter searchFilter) {
        return " select court from "+CommonCourt.class.getName()+" court ";
    }

    @Override
    protected Map<String, Object> createParametersMap(CommonCourtSearchFilter searchFilter) {
        return Collections.emptyMap();
    }

    @Override
    protected void processResult(SearchResult<CommonCourt> searchResult, CommonCourtSearchFilter searchFilter) {
        List<Integer> courtsIds = extractCommonCourtIds(searchResult);
        if(courtsIds.isEmpty()){
            return;
        }

        initializeCommonCourtDivisions(courtsIds);
    }

    //***** END AbstractStringQuerySearchImplementor implementation *********************


    //******** PRIVATE *************

    private List<Integer> extractCommonCourtIds(SearchResult<CommonCourt> searchResult) {
        return searchResult.getResultRecords().stream().map(result->result.getId()).collect(Collectors.toList());
    }

    private void initializeCommonCourtDivisions(List<Integer> courtsIds){
        setIdsParameterAndExecuteQuery(" select commonCourt from " + CommonCourt.class.getName() + " commonCourt left join fetch commonCourt.divisions_ division where commonCourt.id in (:ids) ",
                courtsIds);
    }


    private void setIdsParameterAndExecuteQuery(String query, List<Integer> ids){
        Query queryObject = entityManager.createQuery(query);
        queryObject.setParameter("ids", ids);

        queryObject.getResultList();
    }

    //********* END PRIVATE ************
}
