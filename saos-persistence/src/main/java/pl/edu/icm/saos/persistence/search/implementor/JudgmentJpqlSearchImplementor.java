package pl.edu.icm.saos.persistence.search.implementor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.JudgmentReferencedRegulation;
import pl.edu.icm.saos.persistence.search.dto.JudgmentSearchFilter;
import pl.edu.icm.saos.persistence.search.result.SearchResult;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.google.common.collect.Maps.newHashMap;

/**
 * @author pavtel
 */
@Service
public class JudgmentJpqlSearchImplementor extends AbstractJpqlSearchImplementor<JudgmentSearchFilter, Judgment> {

    //***** fields *************

    @Autowired
    private EntityManager entityManager;

    //******* END fields **************


    //******** AbstractStringQuerySearchImplementor implementation ******************
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
        List<Integer> judgmentIds = extractJudgmentIds(searchResult);

        initializeCourtCases(judgmentIds);
        initializeJudgesAndTheirRoles(judgmentIds, searchResult);
        initializeCourtReporters(judgmentIds);
        initializeLegalBases(judgmentIds);
        initializeReferencedRegulationsAndTheirLawJournalEntries(judgmentIds, searchResult);

        initializeCommonCourtKeywords(judgmentIds);
    }

    //************ END AbstractStringQuerySearchImplementor implementation **************

    //------------------------ PRIVATE --------------------------

    private void initializeCourtCases(List<Integer> judgmentIds){
        setIdsParameterAndExecuteQuery(QUERY.COURT_CASES, judgmentIds);
    }

    private void initializeJudgesAndTheirRoles(List<Integer> judgmentIds, SearchResult<Judgment> searchResult){
        setIdsParameterAndExecuteQuery(QUERY.JUDGES, judgmentIds); //important initialize judges before their roles
        List<Integer> judgesIds = extractJudgesIds(searchResult);
        if(!judgesIds.isEmpty()){
            setIdsParameterAndExecuteQuery(QUERY.JUDGES_ROLES, judgesIds);
        }

    }


    private void initializeCourtReporters(List<Integer> judgmentIds){
        setIdsParameterAndExecuteQuery(QUERY.COURT_REPORTERS, judgmentIds);
    }

    private void initializeLegalBases(List<Integer> judgmentIds){
        setIdsParameterAndExecuteQuery(QUERY.LEGAL_BASES, judgmentIds);
    }

    private void initializeReferencedRegulationsAndTheirLawJournalEntries(List<Integer> judgmentIds, SearchResult<Judgment> searchResult){
        setIdsParameterAndExecuteQuery(QUERY.REFERENCED_REGULATION, judgmentIds); //important initialize regulations before their entries
        List<Integer> regulationsIds = extractReferencedRegulationsIds(searchResult);
        if(!regulationsIds.isEmpty()){
            setIdsParameterAndExecuteQuery(QUERY.LAW_JOURNAL_ENTRIES, extractReferencedRegulationsIds(searchResult));
        }
    }

    private void initializeCommonCourtKeywords(List<Integer> judgmentIds) {
        setIdsParameterAndExecuteQuery(QUERY.COMMON_COURTS_KEYWORDS, judgmentIds);
    }



    private void setIdsParameterAndExecuteQuery(String query, List<Integer> ids){
        Query queryObject = entityManager.createQuery(query);
        queryObject.setParameter("ids", ids);

        queryObject.getResultList();
    }



    
    private List<Integer> extractJudgmentIds(SearchResult<Judgment> searchResult) {
        return searchResult.getResultRecords().stream().map(result->result.getId()).collect(Collectors.toList());
    }

    private List<Integer> extractReferencedRegulationsIds(SearchResult<Judgment> searchResult){
        return searchResult.getResultRecords().stream()
                .flatMap(j -> j.getReferencedRegulations().stream())
                .map(r -> r.getId())
                .collect(Collectors.toList());
    }

    private List<Integer> extractJudgesIds(SearchResult<Judgment> searchResult) {
        return searchResult.getResultRecords().stream()
                .flatMap(j -> j.getJudges().stream())
                .map( jud -> jud.getId())
                .collect(Collectors.toList());
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

    private static class QUERY {
        public static final String COURT_CASES = " select judgment from " + Judgment.class.getName() + " judgment join fetch judgment.courtCases_ courtCase where judgment.id in (:ids) ";
        public static final String JUDGES = " select judgment from " + Judgment.class.getName() + " judgment join fetch judgment.judges_  judge where judgment.id in (:ids) ";
        public static final String JUDGES_ROLES = " select judge from " + Judge.class.getName() + " judge join fetch judge.specialRoles role where judge.id in (:ids)";
        public static final String COURT_REPORTERS = " select judgment from " + Judgment.class.getName() + " judgment join fetch judgment.courtReporters_  courtReporters where judgment.id in (:ids) ";
        public static final String LEGAL_BASES = " select judgment from " + Judgment.class.getName() + " judgment join fetch judgment.legalBases_  legalBases where judgment.id in (:ids) ";
        public static final String REFERENCED_REGULATION = " select judgment from " + Judgment.class.getName() + " judgment join fetch judgment.referencedRegulations_  referencedRegulation where judgment.id in (:ids) ";
        public static final String LAW_JOURNAL_ENTRIES = " select regulation from " + JudgmentReferencedRegulation.class.getName() + " regulation join fetch regulation.lawJournalEntry  lawJournalEntry where regulation.id in (:ids) ";
        public static final String COMMON_COURTS_KEYWORDS =  " select judgment from "+ CommonCourtJudgment.class.getName()+" judgment join fetch judgment.keywords_ keyword where judgment.id in (:ids)";
    }
}
