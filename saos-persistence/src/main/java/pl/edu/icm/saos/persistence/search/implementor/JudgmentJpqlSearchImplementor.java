package pl.edu.icm.saos.persistence.search.implementor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    @Autowired
    private EntityManager entityManager;


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
        initializeJudges(judgmentIds);
        initializeCourtReporters(judgmentIds);
        initializeLegalBases(judgmentIds);
        initializeReferencedRegulationsAndTheirLawJournalEntries(judgmentIds, searchResult);
    }




    //------------------------ PRIVATE --------------------------

    private void initializeCourtCases(List<Integer> judgmentIds){
        setIdsParameterAndExecuteQuery(courtCasesQuery(), judgmentIds);
    }

    private void initializeJudges(List<Integer> judgmentIds){
        setIdsParameterAndExecuteQuery(judgesQuery(), judgmentIds);
    }

    private void initializeCourtReporters(List<Integer> judgmentIds){
        setIdsParameterAndExecuteQuery(courtReportersQuery(), judgmentIds);
    }

    private void initializeLegalBases(List<Integer> judgmentIds){
        setIdsParameterAndExecuteQuery(legalBasesQuery(), judgmentIds);
    }

    private void initializeReferencedRegulationsAndTheirLawJournalEntries(List<Integer> judgmentIds, SearchResult<Judgment> searchResult){
        setIdsParameterAndExecuteQuery(referencedRegulationsQuery(), judgmentIds); //important initialize regulations before their entries
        List<Integer> regulationsIds = extractReferencedRegulationsIds(searchResult);
        if(!regulationsIds.isEmpty()){
            setIdsParameterAndExecuteQuery(lawJournalEntriesQuery(), extractReferencedRegulationsIds(searchResult));
        }
    }



    private String courtCasesQuery(){
        StringBuilder jpql = new StringBuilder(" select judgment from " + Judgment.class.getName() + " judgment join fetch judgment.courtCases_ courtCase where judgment.id in (:ids) ");
        return jpql.toString();
    }

    private String judgesQuery(){
        StringBuilder jpql = new StringBuilder(" select judgment from " + Judgment.class.getName() + " judgment join fetch judgment.judges_  judge where judgment.id in (:ids) ");
        return jpql.toString();
    }


    private String courtReportersQuery(){
        StringBuilder jpql = new StringBuilder(" select judgment from " + Judgment.class.getName() + " judgment join fetch judgment.courtReporters_  courtReporters where judgment.id in (:ids) ");
        return jpql.toString();
    }


    private String legalBasesQuery(){
        StringBuilder jpql = new StringBuilder(" select judgment from " + Judgment.class.getName() + " judgment join fetch judgment.legalBases_  legalBases where judgment.id in (:ids) ");
        return jpql.toString();
    }

    private String referencedRegulationsQuery(){
        StringBuilder jpql = new StringBuilder(" select judgment from " + Judgment.class.getName() + " judgment join fetch judgment.referencedRegulations_  referencedRegulation where judgment.id in (:ids) ");
        return jpql.toString();
    }


    private String lawJournalEntriesQuery(){
        StringBuilder jpql = new StringBuilder(" select regulation from " + JudgmentReferencedRegulation.class.getName() + " regulation join fetch regulation.lawJournalEntry  lawJournalEntry where regulation.id in (:ids) ");
        return jpql.toString();
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
}
