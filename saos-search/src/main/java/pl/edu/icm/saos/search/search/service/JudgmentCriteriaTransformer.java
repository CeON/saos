package pl.edu.icm.saos.search.search.service;

import java.util.List;
import java.util.stream.Collectors;

import org.jadira.usertype.spi.utils.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.search.config.model.JudgmentIndexField;
import pl.edu.icm.saos.search.search.model.JudgmentCriteria;
import pl.edu.icm.saos.search.search.service.SolrCriterionTransformer.Operator;
import pl.edu.icm.saos.search.util.SolrConstants;

import com.google.common.collect.Lists;

/**
 * Creates part of solr query that depends on {@link JudgmentCriteria}
 * @author madryk
 */
@Service
public class JudgmentCriteriaTransformer implements CriteriaTransformer<JudgmentCriteria> {

    private SolrCriterionTransformer<JudgmentIndexField> criterionTransformer;
    
    
    //------------------------ LOGIC --------------------------
    
    @Override
    public String transformCriteria(JudgmentCriteria criteria) {
        List<String> list = Lists.newLinkedList();
        
        list.add(criterionTransformer.transformToEqualsCriterionWithParsing(JudgmentIndexField.ALL, criteria.getAll()));
        list.addAll(transformCommonCourtCriteria(criteria));
        list.addAll(transformSupremeCourtCriteria(criteria));
        list.addAll(transformConstitutionalTribunalCriteria(criteria));
        list.add(criterionTransformer.transformToEqualsCriterion(JudgmentIndexField.COURT_TYPE, criteria.getCourtType()));
        list.add(criterionTransformer.transformToDateRangeCriterion(
                JudgmentIndexField.JUDGMENT_DATE, criteria.getDateFrom(), criteria.getDateTo()));
        list.add(criterionTransformer.transformToEqualsEnumCriteria(JudgmentIndexField.JUDGMENT_TYPE, criteria.getJudgmentTypes(), Operator.OR));
        list.add(criterionTransformer.transformToEqualsCriterionWithParsing(JudgmentIndexField.JUDGE_NAME, criteria.getJudgeName()));
        list.add(criterionTransformer.transformToEqualsCriteria(JudgmentIndexField.KEYWORD, criteria.getKeywords(), Operator.AND));
        list.add(criterionTransformer.transformToEqualsCriterionWithParsing(JudgmentIndexField.LEGAL_BASE, criteria.getLegalBase()));
        list.add(criterionTransformer.transformToEqualsCriterionWithParsing(JudgmentIndexField.REFERENCED_REGULATION, criteria.getReferencedRegulation()));
        list.add(criterionTransformer.transformToEqualsCriterion(JudgmentIndexField.LAW_JOURNAL_ENTRY_ID, criteria.getLawJournalEntryId()));
        list.add(criterionTransformer.transformToEqualsCriterion(JudgmentIndexField.CASE_NUMBER, criteria.getCaseNumber()));
        
        String criteriaString = list.stream()
                .filter(x -> StringUtils.isNotEmpty(x))
                .collect(Collectors.joining(" "));
        
        return StringUtils.isNotEmpty(criteriaString) ? criteriaString : SolrConstants.DEFAULT_QUERY;
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private List<String> transformCommonCourtCriteria(JudgmentCriteria criteria) {
        List<String> list = Lists.newLinkedList();
        
        list.add(criterionTransformer.transformToEqualsCriterion(JudgmentIndexField.CC_COURT_TYPE, criteria.getCcCourtType()));
        list.add(criterionTransformer.transformToEqualsCriterion(JudgmentIndexField.CC_COURT_ID, criteria.getCcCourtId()));
        list.add(criterionTransformer.transformToEqualsCriterion(JudgmentIndexField.CC_COURT_CODE, criteria.getCcCourtCode()));
        list.add(criterionTransformer.transformToEqualsCriterion(JudgmentIndexField.CC_COURT_NAME, criteria.getCcCourtName()));
        list.add(criterionTransformer.transformToEqualsCriterion(JudgmentIndexField.CC_COURT_DIVISION_ID, criteria.getCcCourtDivisionId()));
        list.add(criterionTransformer.transformToEqualsCriterion(JudgmentIndexField.CC_COURT_DIVISION_CODE, criteria.getCcCourtDivisionCode()));
        list.add(criterionTransformer.transformToEqualsCriterion(JudgmentIndexField.CC_COURT_DIVISION_NAME, criteria.getCcCourtDivisionName()));
        
        return list;
    }
    
    private List<String> transformSupremeCourtCriteria(JudgmentCriteria criteria) {
        List<String> list = Lists.newLinkedList();
        
        list.add(criterionTransformer.transformToEqualsCriterion(JudgmentIndexField.SC_JUDGMENT_FORM, criteria.getScJudgmentForm()));
        list.add(criterionTransformer.transformToEqualsCriterion(JudgmentIndexField.SC_PERSONNEL_TYPE, criteria.getScPersonnelType()));
        list.add(criterionTransformer.transformToEqualsCriterion(JudgmentIndexField.SC_COURT_CHAMBER_ID, criteria.getScCourtChamberId()));
        list.add(criterionTransformer.transformToEqualsCriterion(JudgmentIndexField.SC_COURT_CHAMBER_NAME, criteria.getScCourtChamberName()));
        list.add(criterionTransformer.transformToEqualsCriterion(JudgmentIndexField.SC_COURT_DIVISION_ID, criteria.getScCourtChamberDivisionId()));
        list.add(criterionTransformer.transformToEqualsCriterion(JudgmentIndexField.SC_COURT_DIVISION_NAME, criteria.getScCourtChamberDivisionName()));
        
        return list;
    }
    
    private List<String> transformConstitutionalTribunalCriteria(JudgmentCriteria criteria) {
        List<String> list = Lists.newLinkedList();
        
        list.add(criterionTransformer.transformToEqualsCriterionWithParsing(JudgmentIndexField.CT_DISSENTING_OPINION, criteria.getCtDissentingOpinion()));
        list.add(criterionTransformer.transformToEqualsCriterionWithParsing(JudgmentIndexField.CT_DISSENTING_OPINION_AUTHOR, criteria.getCtDissentingOpinionAuthor()));
        
        return list;
    }
    

    //------------------------ SETTERS --------------------------

    @Autowired
    public void setCriterionTransformer(
            SolrCriterionTransformer<JudgmentIndexField> criterionTransformer) {
        this.criterionTransformer = criterionTransformer;
    }

}
