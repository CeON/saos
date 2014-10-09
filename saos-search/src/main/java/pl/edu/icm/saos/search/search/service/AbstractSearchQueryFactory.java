package pl.edu.icm.saos.search.search.service;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;

import pl.edu.icm.saos.search.search.model.Criteria;
import pl.edu.icm.saos.search.search.model.HighlightingFieldParams;
import pl.edu.icm.saos.search.search.model.HighlightingParams;
import pl.edu.icm.saos.search.search.model.Paging;
import pl.edu.icm.saos.search.search.model.Sorting;
import pl.edu.icm.saos.search.search.model.Sorting.Direction;
import pl.edu.icm.saos.search.util.SolrConstants;

/**
 * Base class for creating {@link SolrQuery}.
 * It handles paging, sorting and highlighting
 * @author madryk
 * @param <C> type of criteria that is supported
 */
public abstract class AbstractSearchQueryFactory<C extends Criteria> implements SearchQueryFactory<C> {

    private HighlightingParams highlightParams;
    
    
    @Override
    public SolrQuery createQuery(C criteria, Paging paging) {
        SolrQuery query = new SolrQuery();
        
        String queryString = transformCriteria(criteria);
        if (StringUtils.isEmpty(queryString)) {
            query.setQuery(SolrConstants.DEFAULT_QUERY);
        } else {
            query.setQuery(queryString);
        }
        
        applyPaging(query, paging);
        if (paging != null) {
            applySorting(query, paging.getSort());
        }
        applyHighlighting(query);
        
        return query;
    }
    
    /**
     * Creates part of solr query that depends on criteria  
     * @param criteria
     * @return
     */
    protected abstract String transformCriteria(C criteria);
    
    
    //------------------------ PRIVATE --------------------------
    
    private void applyPaging(SolrQuery query, Paging paging) {
        if (paging == null) {
            return;
        }
        query.setStart(paging.getPageNumber() * paging.getPageSize());
        query.setRows(paging.getPageSize());
    }
    
    private void applySorting(SolrQuery query, Sorting sorting) {
        if (sorting == null || StringUtils.isBlank(sorting.getFieldName())) {
            return;
        }
        
        query.addSort(sorting.getFieldName(), (sorting.getDirection() == Direction.ASC) ? ORDER.asc : ORDER.desc);
    }
    
    private void applyHighlighting(SolrQuery query) {
        if (highlightParams == null) {
            return;
        }
        
        query.setHighlight(true);
        for (Map.Entry<String, String> highlightParam : highlightParams.getParams().entrySet()) {
            query.set(highlightParam.getKey(), highlightParam.getValue());
        }
        
        for (HighlightingFieldParams highlightFieldParams : highlightParams.getFieldsParams()) {
            query.addHighlightField(highlightFieldParams.getFieldName());
            
            for (Map.Entry<String, String> highlightFieldParam : highlightFieldParams.getParams().entrySet()) {
                query.set("f." + highlightFieldParams.getFieldName() + "." + highlightFieldParam.getKey(), highlightFieldParam.getValue());
            }
        }

    }


    //------------------------ SETTERS --------------------------
    
    public void setHighlightParams(HighlightingParams highlightParams) {
        this.highlightParams = highlightParams;
    }
}
