package pl.edu.icm.saos.persistence.search.implementor;

import static com.google.common.collect.Maps.newHashMap;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.ConstitutionalTribunalJudgment;
import pl.edu.icm.saos.persistence.model.ConstitutionalTribunalJudgmentDissentingOpinion;
import pl.edu.icm.saos.persistence.model.CourtType;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.JudgmentReferencedRegulation;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment;
import pl.edu.icm.saos.persistence.search.dto.JudgmentSearchFilter;
import pl.edu.icm.saos.persistence.search.result.SearchResult;

import com.google.common.collect.Lists;

/**
 * @author pavtel
 */
@Service
public class JudgmentJpqlSearchImplementor extends AbstractJpqlSearchImplementor<JudgmentSearchFilter, Judgment> {

    
    @Autowired
    private EntityManager entityManager;
    
    

    //------------------------ LOGIC --------------------------
    
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
        List<Long> judgmentIds = extractJudgmentIds(searchResult);
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
        initializeConstitutionalTribunalSpecificFields(judgmentIds, searchResult);
        
    }


    //------------------------ PRIVATE --------------------------

    private void initializeCourtCases(List<Long> judgmentIds){
        setIdsParameterAndExecuteQuery(" select judgment from " + Judgment.class.getName() + " judgment left join fetch judgment.courtCases_ courtCase where judgment.id in (:ids) ",
                judgmentIds);
    }

    private void initializeJudgesAndTheirRoles(List<Long> judgmentIds, SearchResult<Judgment> searchResult){
        setIdsParameterAndExecuteQuery(" select judgment from " + Judgment.class.getName() + " judgment join fetch judgment.judges_  judge where judgment.id in (:ids) "
                , judgmentIds); //important initialize judges before their roles
        List<Long> judgesIds = extractJudgesIds(searchResult);
        if(!judgesIds.isEmpty()){
            setIdsParameterAndExecuteQuery(" select judge from " + Judge.class.getName() + " judge left join fetch judge.specialRoles role where judge.id in (:ids) ",
                    judgesIds);
        }

    }


    private void initializeCourtReporters(List<Long> judgmentIds){
        setIdsParameterAndExecuteQuery(" select judgment from " + Judgment.class.getName() + " judgment left join fetch judgment.courtReporters_  courtReporters where judgment.id in (:ids) ",
                judgmentIds);
    }

    private void initializeLegalBases(List<Long> judgmentIds){
        setIdsParameterAndExecuteQuery(" select judgment from " + Judgment.class.getName() + " judgment left join fetch judgment.legalBases_  legalBases where judgment.id in (:ids) ",
                judgmentIds);
    }

    private void initializeReferencedRegulationsAndTheirLawJournalEntries(List<Long> judgmentIds, SearchResult<Judgment> searchResult){
        setIdsParameterAndExecuteQuery(" select judgment from " + Judgment.class.getName() + " judgment join fetch judgment.referencedRegulations_  referencedRegulation where judgment.id in (:ids) ",
                judgmentIds); //important initialize regulations before their entries
        List<Long> regulationsIds = extractReferencedRegulationsIds(searchResult);
        if(!regulationsIds.isEmpty()){
            setIdsParameterAndExecuteQuery(" select regulation from " + JudgmentReferencedRegulation.class.getName() + " regulation join fetch regulation.lawJournalEntry  lawJournalEntry where regulation.id in (:ids) ",
                    extractReferencedRegulationsIds(searchResult));
        }
    }

    private void initializeCommonCourtJudgmentSpecificFields(List<Long> judgmentIds) {
        initializeKeywords(judgmentIds);
    }

    private void initializeKeywords(List<Long> judgmentIds) {
        setIdsParameterAndExecuteQuery(" select judgment from "+ Judgment.class.getName()+" judgment left join fetch judgment.keywords_ keyword where judgment.id in (:ids) ",
                judgmentIds);
    }

    private void initializeSupremeCourtJudgmentSpecificFields(List<Long> judgmentIds) {
        initializeSupremeCourtChambers(judgmentIds);
    }

    private void initializeSupremeCourtChambers(List<Long> judgmentIds){
        setIdsParameterAndExecuteQuery(" select judgment from "+ SupremeCourtJudgment.class.getName()+" judgment left join fetch judgment.scChambers_ scChamber where judgment.id in (:ids) ",
                judgmentIds);
    }


    private void initializeConstitutionalTribunalSpecificFields(List<Long> judgmentIds, SearchResult<Judgment> searchResult) {
        initializeDissentingOpinionAndTheirAuthors(judgmentIds, searchResult);
    }

    private void initializeDissentingOpinionAndTheirAuthors(List<Long> judgmentIds, SearchResult<Judgment> searchResult) {
        setIdsParameterAndExecuteQuery(" select judgment from "+ ConstitutionalTribunalJudgment.class.getName()+" judgment left join fetch judgment.dissentingOpinions_ opinion where judgment.id in (:ids) ",
                judgmentIds);
        List<Long> opinionsIds = extractOpinionsIds(searchResult);
        if(!opinionsIds.isEmpty()){
            setIdsParameterAndExecuteQuery(" select opinion from "+ ConstitutionalTribunalJudgmentDissentingOpinion.class.getName()+" opinion left join fetch opinion.authors_ author where opinion.id in (:ids) ",
                    opinionsIds);
        }

    }

  
    private void setIdsParameterAndExecuteQuery(String query, List<Long> ids){
        Query queryObject = entityManager.createQuery(query);
        queryObject.setParameter("ids", ids);

        queryObject.getResultList();
    }
    
    private List<Long> extractJudgmentIds(SearchResult<Judgment> searchResult) {
        return searchResult.getResultRecords().stream().map(result->result.getId()).collect(Collectors.toList());
    }
    
    private List<Long> extractReferencedRegulationsIds(SearchResult<Judgment> searchResult){
        return searchResult.getResultRecords().stream()
                .flatMap(j -> j.getReferencedRegulations().stream())
                .map(r -> r.getId())
                .collect(Collectors.toList());
    }

    private List<Long> extractJudgesIds(SearchResult<Judgment> searchResult) {
        return searchResult.getResultRecords().stream()
                .flatMap(j -> j.getJudges().stream())
                .map( jud -> jud.getId())
                .collect(Collectors.toList());
    }

    private List<Long> extractOpinionsIds(SearchResult<Judgment> searchResult) {
        List<Long> opinionsIds = Lists.newArrayList();

        searchResult.getResultRecords().stream()
                .filter(judgment -> judgment.getCourtType() == CourtType.CONSTITUTIONAL_TRIBUNAL)
                .forEach(judgment ->
                        {
                            ConstitutionalTribunalJudgment ctJudgment = (ConstitutionalTribunalJudgment) judgment;
                            opinionsIds.addAll(
                                    ctJudgment.getDissentingOpinions().stream()
                                            .map(o -> o.getId())
                                            .collect(Collectors.toList())
                            );
                        }
                );

        return opinionsIds;
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
