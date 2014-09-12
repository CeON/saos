package pl.edu.icm.saos.search.service;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.springframework.stereotype.Component;

import pl.edu.icm.saos.search.config.model.JudgmentIndexField;
import pl.edu.icm.saos.search.model.JudgmentCriteria;
import pl.edu.icm.saos.search.model.Paging;
import pl.edu.icm.saos.search.model.Sorting;
import pl.edu.icm.saos.search.model.Sorting.Direction;
import pl.edu.icm.saos.search.util.SearchDateTimeUtils;
import pl.edu.icm.saos.search.util.SolrConstants;

@Component
public class JudgmentSearchQueryFactory implements SearchQueryFactory<JudgmentCriteria> {

    @Override
    public SolrQuery createQuery(JudgmentCriteria criteria, Paging paging) {
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
        
        return query;
    }
    
    protected String transformCriteria(JudgmentCriteria criteria) {
        StringBuilder sb = new StringBuilder();
        sb.append(transformSingleCriterion(JudgmentIndexField.CONTENT, criteria.getAll()));
        sb.append(transformSingleCriterion(JudgmentIndexField.COURT_ID, criteria.getCourtId()));
        sb.append(transformSingleCriterion(JudgmentIndexField.COURT_NAME, criteria.getCourtName()));
        sb.append(transformDateRangeCriterion(JudgmentIndexField.JUDGMENT_DATE, criteria.getDateFrom(), criteria.getDateTo()));
        sb.append(transformSingleCriterion(JudgmentIndexField.JUDGE, criteria.getJudgeName()));
        sb.append(transformSingleCriterion(JudgmentIndexField.KEYWORD, criteria.getKeyword()));
        sb.append(transformSingleCriterion(JudgmentIndexField.LEGAL_BASE, criteria.getLegalBase()));
        sb.append(transformSingleCriterion(JudgmentIndexField.REFERENCED_REGULATION, criteria.getReferencedRegulation()));
        sb.append(transformSingleCriterion(JudgmentIndexField.CASE_NUMBER, criteria.getCaseNumber()));
        return sb.toString().trim();
    }
    
    private String transformSingleCriterion(JudgmentIndexField field, String value) {
        if (StringUtils.isBlank(value)) {
            return StringUtils.EMPTY;
        }
        return " +" + field.getFieldName() + ":" + ClientUtils.escapeQueryChars(value);
    }
    
    private String transformDateRangeCriterion(JudgmentIndexField field, Date from, Date to) {
        if (from == null && to == null) {
            return StringUtils.EMPTY;
        }
        String fromString = null;
        String toString = null;
        
        if (from != null) {
            fromString = SearchDateTimeUtils.convertDateToISOString(from);
        }
        
        if (to != null) {
            toString = SearchDateTimeUtils.convertDateToISOStringAtEndOfDay(to);
        }
        
        return transformRangeCriterion(field, fromString, toString);
    }
    
    private String transformRangeCriterion(JudgmentIndexField field, String from, String to) {
        if (StringUtils.isBlank(from) && StringUtils.isBlank(to)) {
            return StringUtils.EMPTY;
        }
        String parsedFrom = (StringUtils.isBlank(from)) ? "*" : StringUtils.trim(from);
        String parsedTo = (StringUtils.isBlank(to)) ? "*" : StringUtils.trim(to);
        return " +" + field.getFieldName() + ":[" + parsedFrom + " TO " + parsedTo + "]";
    }
    
    
    
    protected void applyPaging(SolrQuery query, Paging paging) {
        if (paging == null) {
            return;
        }
        query.setStart(paging.getPageNumber() * paging.getPageSize());
        query.setRows(paging.getPageSize());
    }
    
    protected void applySorting(SolrQuery query, Sorting sorting) {
        if (sorting == null || StringUtils.isBlank(sorting.getFieldName())) {
            return;
        }
        
        query.addSort(sorting.getFieldName(), (sorting.getDirection() == Direction.ASC) ? ORDER.asc : ORDER.desc);
    }

}
