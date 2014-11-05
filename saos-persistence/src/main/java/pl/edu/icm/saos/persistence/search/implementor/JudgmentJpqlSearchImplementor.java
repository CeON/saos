package pl.edu.icm.saos.persistence.search.implementor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.icm.saos.persistence.model.*;
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

        if(searchFilter.getSinceModificationDate()!=null){
            map.put("sinceModificationDateTime", searchFilter.getSinceModificationDate());
        }

        return map;
    }
    
    @Override
    protected void processResult(SearchResult<Judgment> searchResult, JudgmentSearchFilter searchFilter) {
        List<Integer> judgmentIds = extractJudgmentIds(searchResult);
        if(judgmentIds.isEmpty()){
            return;
        }

        initializeCourtCases(judgmentIds);
        initializeJudgesAndTheirRoles(judgmentIds, searchResult);
        initializeCourtReporters(judgmentIds);
        initializeLegalBases(judgmentIds);
        initializeReferencedRegulationsAndTheirLawJournalEntries(judgmentIds, searchResult);

        initializeCommonCourtJudgmentSpecificFields(judgmentIds);
        initializeSupremeCourtJudgmentSpecificFields(judgmentIds);

    }


    //------------------------ PRIVATE --------------------------

    private void initializeCourtCases(List<Integer> judgmentIds){
        setIdsParameterAndExecuteQuery(" select judgment from " + Judgment.class.getName() + " judgment left join fetch judgment.courtCases_ courtCase where judgment.id in (:ids) ",
                judgmentIds);
    }

    private void initializeJudgesAndTheirRoles(List<Integer> judgmentIds, SearchResult<Judgment> searchResult){
        setIdsParameterAndExecuteQuery(" select judgment from " + Judgment.class.getName() + " judgment join fetch judgment.judges_  judge where judgment.id in (:ids) "
                , judgmentIds); //important initialize judges before their roles
        List<Integer> judgesIds = extractJudgesIds(searchResult);
        if(!judgesIds.isEmpty()){
            setIdsParameterAndExecuteQuery(" select judge from " + Judge.class.getName() + " judge left join fetch judge.specialRoles role where judge.id in (:ids) ",
                    judgesIds);
        }

    }


    private void initializeCourtReporters(List<Integer> judgmentIds){
        setIdsParameterAndExecuteQuery(" select judgment from " + Judgment.class.getName() + " judgment left join fetch judgment.courtReporters_  courtReporters where judgment.id in (:ids) ",
                judgmentIds);
    }

    private void initializeLegalBases(List<Integer> judgmentIds){
        setIdsParameterAndExecuteQuery(" select judgment from " + Judgment.class.getName() + " judgment left join fetch judgment.legalBases_  legalBases where judgment.id in (:ids) ",
                judgmentIds);
    }

    private void initializeReferencedRegulationsAndTheirLawJournalEntries(List<Integer> judgmentIds, SearchResult<Judgment> searchResult){
        setIdsParameterAndExecuteQuery(" select judgment from " + Judgment.class.getName() + " judgment join fetch judgment.referencedRegulations_  referencedRegulation where judgment.id in (:ids) ",
                judgmentIds); //important initialize regulations before their entries
        List<Integer> regulationsIds = extractReferencedRegulationsIds(searchResult);
        if(!regulationsIds.isEmpty()){
            setIdsParameterAndExecuteQuery(" select regulation from " + JudgmentReferencedRegulation.class.getName() + " regulation join fetch regulation.lawJournalEntry  lawJournalEntry where regulation.id in (:ids) ",
                    extractReferencedRegulationsIds(searchResult));
        }
    }

    private void initializeCommonCourtJudgmentSpecificFields(List<Integer> judgmentIds) {
        initializeCommonCourtKeywords(judgmentIds);
    }

    private void initializeCommonCourtKeywords(List<Integer> judgmentIds) {
        setIdsParameterAndExecuteQuery(" select judgment from "+ CommonCourtJudgment.class.getName()+" judgment left join fetch judgment.keywords_ keyword where judgment.id in (:ids) ",
                judgmentIds);
    }

    private void initializeSupremeCourtJudgmentSpecificFields(List<Integer> judgmentIds) {
        initializeSupremeCourtChambers(judgmentIds);
    }

    private void initializeSupremeCourtChambers(List<Integer> judgmentIds){
        setIdsParameterAndExecuteQuery(" select judgment from "+ SupremeCourtJudgment.class.getName()+" judgment left join fetch judgment.scChambers_ scChamber where judgment.id in (:ids) ",
                judgmentIds);
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

        if(searchFilter.getSinceModificationDate() != null){
            jpql.append(" and modificationDate >= :sinceModificationDateTime");
        }

        return jpql.toString();
    }
}
