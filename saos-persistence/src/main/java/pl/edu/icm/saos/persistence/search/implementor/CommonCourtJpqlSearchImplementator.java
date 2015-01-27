package pl.edu.icm.saos.persistence.search.implementor;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.search.dto.CommonCourtSearchFilter;
import pl.edu.icm.saos.persistence.search.result.SearchResult;

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
        List<Long> courtsIds = extractCommonCourtIds(searchResult);
        if(courtsIds.isEmpty()){
            return;
        }

        initializeCommonCourtDivisions(courtsIds);
    }

    //***** END AbstractStringQuerySearchImplementor implementation *********************


    //******** PRIVATE *************

    private List<Long> extractCommonCourtIds(SearchResult<CommonCourt> searchResult) {
        return searchResult.getResultRecords().stream().map(result->result.getId()).collect(Collectors.toList());
    }

    private void initializeCommonCourtDivisions(List<Long> courtsIds){
        setIdsParameterAndExecuteQuery(" select commonCourt from " + CommonCourt.class.getName() + " commonCourt left join fetch commonCourt.divisions_ division where commonCourt.id in (:ids) ",
                courtsIds);
    }


    private void setIdsParameterAndExecuteQuery(String query, List<Long> ids){
        Query queryObject = entityManager.createQuery(query);
        queryObject.setParameter("ids", ids);

        queryObject.getResultList();
    }

    //********* END PRIVATE ************
}
