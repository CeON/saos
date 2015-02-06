package pl.edu.icm.saos.enrichment.apply;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.enrichment.EnrichmentTagRepository;
import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTag;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.search.DatabaseSearchService;
import pl.edu.icm.saos.persistence.search.dto.JudgmentSearchFilter;
import pl.edu.icm.saos.persistence.search.result.SearchResult;

import com.google.common.base.Preconditions;

/**
 * Service for searching judgments in database and enriching them with {@EnrichmentTag}s.
 * @author Łukasz Dumiszewski
 */
@Service("judgmentEnrichmentDbSearchService")
public class JudgmentEnrichmentDbSearchService {

    private DatabaseSearchService databaseSearchService;
    
    private EnrichmentTagRepository enrichmentTagRepository;
    
    private JudgmentEnrichmentService judgmentEnrichmentService;
    
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Finds judgments that met the criteria specified by searchFilter (uses {@link DatabaseSearchService#search(pl.edu.icm.saos.persistence.search.dto.SearchFilter)}).
     * <br/>
     * If {@link {@link JudgmentSearchFilter#isWithGenerated()} is true then enriches each found judgment with {@link EnrichmentTag}s assigned to it. 
     * 
     */
    public SearchResult<Judgment> search(JudgmentSearchFilter searchFilter) {
        
        Preconditions.checkNotNull(searchFilter);
        
        SearchResult<Judgment> searchResult = databaseSearchService.search(searchFilter);
        
        if (searchFilter.isWithGenerated() != null && !searchFilter.isWithGenerated()) {
            return searchResult;
        }
        
        if (CollectionUtils.isEmpty(searchResult.getResultRecords())) {
            return searchResult;
        }
        
        List<Judgment> judgments = searchResult.getResultRecords();
        
        List<EnrichmentTag> enrichmentTags = enrichmentTagRepository.findAllByJudgmentIds(extractJudgmentIds(judgments));
        
        
        for (Judgment judgment : judgments) {
            
            List<EnrichmentTag> judgmentEnrichmentTags = extractEnrichmentTags(enrichmentTags, judgment);
        
            judgmentEnrichmentService.enrich(judgment, judgmentEnrichmentTags);
        
        }
        
        
        
        return searchResult;
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private List<Long> extractJudgmentIds(List<Judgment> judgments) {
        return judgments.stream().map(j->j.getId()).collect(Collectors.toList());
    }
    
    private List<EnrichmentTag> extractEnrichmentTags(List<EnrichmentTag> allEnrichmentTags, Judgment judgment) {
        return allEnrichmentTags.stream().filter(tag->tag.getJudgmentId()==judgment.getId()).collect(Collectors.toList());
    }

    
    //------------------------ SETTERS --------------------------

    @Autowired
    public void setDatabaseSearchService(DatabaseSearchService databaseSearchService) {
        this.databaseSearchService = databaseSearchService;
    }

    @Autowired
    public void setEnrichmentTagRepository(EnrichmentTagRepository enrichmentTagRepository) {
        this.enrichmentTagRepository = enrichmentTagRepository;
    }

    @Autowired
    public void setJudgmentEnrichmentService(JudgmentEnrichmentService judgmentEnrichmentService) {
        this.judgmentEnrichmentService = judgmentEnrichmentService;
    }
    
    
}
