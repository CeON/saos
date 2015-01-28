package pl.edu.icm.saos.persistence.search.implementor;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.SupremeCourtChamber;
import pl.edu.icm.saos.persistence.search.dto.SupremeCourtChamberSearchFilter;
import pl.edu.icm.saos.persistence.search.result.SearchResult;

/**
 * @author pavtel
 */
@Service
public class SupremeCourtChamberJpqlSearchImplementator extends AbstractJpqlSearchImplementor<SupremeCourtChamberSearchFilter, SupremeCourtChamber>  {
    @Autowired
    private EntityManager entityManager;

    //------------------------ LOGIC --------------------------
    @Override
    protected String createQuery(SupremeCourtChamberSearchFilter searchFilter) {
        return " select chamber from "+SupremeCourtChamber.class.getName()+" chamber ";
    }


    @Override
    protected Map<String, Object> createParametersMap(SupremeCourtChamberSearchFilter searchFilter) {
        return Collections.emptyMap();
    }

    @Override
    protected void processResult(SearchResult<SupremeCourtChamber> searchResult, SupremeCourtChamberSearchFilter searchFilter) {
        List<Long> chambersIds = extractChambersIds(searchResult);

        if(chambersIds.isEmpty()){
            return;
        }

        initializeSupremeCourtChamberDivisions(chambersIds);
    }


    //------------------------ PRIVATE --------------------------
    private List<Long> extractChambersIds(SearchResult<SupremeCourtChamber> searchResult) {
        return searchResult.getResultRecords().stream().map(result->result.getId()).collect(Collectors.toList());
    }


    private void initializeSupremeCourtChamberDivisions(List<Long> chambersIds) {
        setIdsParameterAndExecuteQuery(" select chamber from " + SupremeCourtChamber.class.getName() + " chamber left join fetch chamber.divisions_ division where chamber.id in (:ids) ",
                chambersIds);
    }


    private void setIdsParameterAndExecuteQuery(String query, List<Long> ids){
        Query queryObject = entityManager.createQuery(query);
        queryObject.setParameter("ids", ids);

        queryObject.getResultList();
    }
}
