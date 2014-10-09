package pl.edu.icm.saos.search.search.service;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.solr.client.solrj.SolrQuery;
import org.jadira.usertype.spi.utils.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.search.config.model.JudgmentIndexField;
import pl.edu.icm.saos.search.search.model.HighlightingParams;
import pl.edu.icm.saos.search.search.model.JudgmentCriteria;
import pl.edu.icm.saos.search.util.SolrConstants;

import com.google.common.collect.Lists;

/**
 * Creates {@link SolrQuery} based on {@link JudgmentCriteria}
 * @author madryk
 */
@Service
public class JudgmentSearchQueryFactory extends AbstractSearchQueryFactory<JudgmentCriteria> {

    private SolrCriterionTransformer<JudgmentIndexField> criterionTransformer;
    
    @Override
    protected String transformCriteria(JudgmentCriteria criteria) {
        List<String> list = Lists.newLinkedList();
        
        list.add(criterionTransformer.transformCriterion(JudgmentIndexField.CONTENT, criteria.getAll()));
        list.add(criterionTransformer.transformCriterion(JudgmentIndexField.COURT_TYPE, criteria.getCourtType()));
        list.add(criterionTransformer.transformCriterion(JudgmentIndexField.COURT_ID, criteria.getCourtId()));
        list.add(criterionTransformer.transformCriterion(JudgmentIndexField.COURT_CODE, criteria.getCourtCode()));
        list.add(criterionTransformer.transformCriterion(JudgmentIndexField.COURT_NAME, criteria.getCourtName()));
        list.add(criterionTransformer.transformCriterion(JudgmentIndexField.COURT_DIVISION_ID, criteria.getCourtDivisionId()));
        list.add(criterionTransformer.transformCriterion(JudgmentIndexField.COURT_DIVISION_CODE, criteria.getCourtDivisionCode()));
        list.add(criterionTransformer.transformCriterion(JudgmentIndexField.COURT_DIVISION_NAME, criteria.getCourtDivisionName()));
        list.add(criterionTransformer.transformDateRangeCriterion(
                JudgmentIndexField.JUDGMENT_DATE, criteria.getDateFrom(), criteria.getDateTo()));
        list.add(criterionTransformer.transformCriterion(JudgmentIndexField.JUDGMENT_TYPE, criteria.getJudgmentType()));
        list.add(criterionTransformer.transformCriterion(JudgmentIndexField.JUDGE_NAME, criteria.getJudgeName()));
        list.add(criterionTransformer.transformCriterion(JudgmentIndexField.KEYWORD, criteria.getKeyword()));
        list.add(criterionTransformer.transformCriterion(JudgmentIndexField.LEGAL_BASE, criteria.getLegalBase()));
        list.add(criterionTransformer.transformCriterion(JudgmentIndexField.REFERENCED_REGULATION, criteria.getReferencedRegulation()));
        list.add(criterionTransformer.transformCriterion(JudgmentIndexField.CASE_NUMBER, criteria.getCaseNumber()));
        
        String criteriaString = list.stream()
                .filter(x -> StringUtils.isNotEmpty(x))
                .collect(Collectors.joining(" "));
        
        return StringUtils.isNotEmpty(criteriaString) ? criteriaString : SolrConstants.DEFAULT_QUERY;
    }
    

    //------------------------ SETTERS --------------------------

    @Override
    @Autowired
    @Qualifier("judgmentsHighlightParams")
    public void setHighlightParams(HighlightingParams highlightParams) {
        super.setHighlightParams(highlightParams);
    }

    @Autowired
    public void setCriterionTransformer(
            SolrCriterionTransformer<JudgmentIndexField> criterionTransformer) {
        this.criterionTransformer = criterionTransformer;
    }

}
