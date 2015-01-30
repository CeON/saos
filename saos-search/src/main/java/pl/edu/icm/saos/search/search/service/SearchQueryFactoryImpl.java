package pl.edu.icm.saos.search.search.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.common.params.HighlightParams;

import pl.edu.icm.saos.search.search.model.Criteria;
import pl.edu.icm.saos.search.search.model.HighlightingFieldParams;
import pl.edu.icm.saos.search.search.model.HighlightingParams;
import pl.edu.icm.saos.search.search.model.Paging;
import pl.edu.icm.saos.search.search.model.Sorting;
import pl.edu.icm.saos.search.search.model.Sorting.Direction;

/**
 * @author madryk
 * @see SearchQueryFactory
 */
public class SearchQueryFactoryImpl<C extends Criteria> implements SearchQueryFactory<C> {

    private CriteriaTransformer<C> criteriaTransformer;
    
    private HighlightingParams highlightParams;
    
    
    @Override
    public SolrQuery createQuery(C criteria, Paging paging) {
        SolrQuery query = new SolrQuery();
        
        String queryString = criteriaTransformer.transformCriteria(criteria);
        query.setQuery(queryString);
        
        applyPaging(query, paging);
        applySorting(query, paging);
        applyHighlighting(query);
        
        return query;
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private void applyPaging(SolrQuery query, Paging paging) {
        if (paging == null) {
            return;
        }
        query.setStart(paging.getPageNumber() * paging.getPageSize());
        query.setRows(paging.getPageSize());
    }
    
    private void applySorting(SolrQuery query, Paging paging) {
        if (paging == null) {
            return;
        }
        
        Sorting sorting = paging.getSort();
        if (sorting == null || StringUtils.isBlank(sorting.getFieldName())) {
            return;
        }
        
        query.addSort(sorting.getFieldName(), (sorting.getDirection() == Direction.ASC) ? ORDER.asc : ORDER.desc);
    }
    
    private void applyHighlighting(SolrQuery query) {
        if (highlightParams == null) {
            return;
        }
        
        applyGlobalHighlightParams(query);
        
        for (HighlightingFieldParams highlightFieldParams : highlightParams.getFieldsParams()) {

            applyPerFieldHighlightParams(query, highlightFieldParams);
            
        }

    }
    
    private void applyGlobalHighlightParams(SolrQuery query) {
        query.setHighlight(true);
        
        highlightParams.getParams().forEach((paramName, value) -> query.set(paramName, value));
    }
    
    private void applyPerFieldHighlightParams(SolrQuery query, HighlightingFieldParams highlightFieldParams) {
        String fieldToHighlight = highlightFieldParams.getFieldName();
        
        query.addHighlightField(fieldToHighlight);
        
        if (!highlightFieldParams.getHighlightFromFields().isEmpty()) {
            applyAlternateQueryForHighlighting(query, highlightFieldParams);
        }

        highlightFieldParams.getParams().forEach(
                (paramName, value) -> query.set("f." + fieldToHighlight + "." + paramName, value)
        );
    }
    
    private void applyAlternateQueryForHighlighting(SolrQuery query, HighlightingFieldParams highlightFieldParams) {
        String fieldToHighlight = highlightFieldParams.getFieldName();
        String queryForHighlightingString = query.getQuery();

        for (String highlightFromField : highlightFieldParams.getHighlightFromFields()) {
            queryForHighlightingString = queryForHighlightingString.replace(highlightFromField + ":", fieldToHighlight + ":");
        }

        query.set(HighlightParams.Q, queryForHighlightingString);
    }
    
    


    //------------------------ SETTERS --------------------------

    public void setCriteriaTransformer(CriteriaTransformer<C> criteriaTransformer) {
        this.criteriaTransformer = criteriaTransformer;
    }
    
    public void setHighlightParams(HighlightingParams highlightParams) {
        this.highlightParams = highlightParams;
    }

}
