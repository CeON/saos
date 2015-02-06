package pl.edu.icm.saos.enrichment.apply;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static pl.edu.icm.saos.persistence.common.TestInMemoryEnrichmentTagFactory.createEnrichmentTag;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

import pl.edu.icm.saos.persistence.common.TestInMemoryObjectFactory;
import pl.edu.icm.saos.persistence.enrichment.EnrichmentTagRepository;
import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTag;
import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTagTypes;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.search.DatabaseSearchService;
import pl.edu.icm.saos.persistence.search.dto.JudgmentSearchFilter;
import pl.edu.icm.saos.persistence.search.result.SearchResult;

import com.google.common.collect.Lists;

/**
 * @author Łukasz Dumiszewski
 */

public class JudgmentEnrichmentDbSearchServiceTest {

    
    private JudgmentEnrichmentDbSearchService judgmentEnrichmentDbSearchService = new JudgmentEnrichmentDbSearchService();
    
    @Mock private DatabaseSearchService databaseSearchService;
    
    @Mock private EnrichmentTagRepository enrichmentTagRepository;
    
    @Mock private JudgmentEnrichmentService judgmentEnrichmentService;
    
    private JudgmentSearchFilter searchFilter = new JudgmentSearchFilter();
    
    private SearchResult<Judgment> searchResult;
    
    
    @Before
    public void before() {
        
        initMocks(this);
        
        judgmentEnrichmentDbSearchService.setDatabaseSearchService(databaseSearchService);
        judgmentEnrichmentDbSearchService.setEnrichmentTagRepository(enrichmentTagRepository);
        judgmentEnrichmentDbSearchService.setJudgmentEnrichmentService(judgmentEnrichmentService);
        
        
    }
    
    
    //------------------------ TESTS --------------------------
    
    
    @Test(expected=NullPointerException.class)
    public void search_searchFilterNull() {
    
        // execute
        judgmentEnrichmentDbSearchService.search(null);
        
    }
    
    @Test
    public void search_JudgmentsNotFound() {
    
        // given
        List<Judgment> judgments = Lists.newArrayList();
        searchResult = new SearchResult<Judgment>(judgments, 0l, 0, 10);
        Mockito.<SearchResult<Judgment>>when(databaseSearchService.search(searchFilter)).thenReturn(searchResult);
        
        
        // execute
        judgmentEnrichmentDbSearchService.search(searchFilter);
        
        // assert
        verify(databaseSearchService).search(searchFilter);
        verifyNoMoreInteractions(databaseSearchService);
        verifyZeroInteractions(enrichmentTagRepository, judgmentEnrichmentService);
        
        
    }
    
    
    @Test
    public void search_JudgmentsFound() {
    
        // given
        
        Judgment ccJudgment = TestInMemoryObjectFactory.createCcJudgment();
        Whitebox.setInternalState(ccJudgment, "id", 19l);
        Judgment nacJudgment = TestInMemoryObjectFactory.createNacJudgment();
        Whitebox.setInternalState(nacJudgment, "id", 29l);
        
        List<Judgment> judgments = Lists.newArrayList(ccJudgment, nacJudgment);
        
        searchResult = new SearchResult<Judgment>(judgments, 2l, 0, 10);
        Mockito.<SearchResult<Judgment>>when(databaseSearchService.search(searchFilter)).thenReturn(searchResult);
        
        
        EnrichmentTag enrichmentTag = createEnrichmentTag(ccJudgment.getId(), EnrichmentTagTypes.REFERENCED_COURT_CASES, "{'xxx':'yyy'}");
        List<EnrichmentTag> enrichmentTags = Lists.newArrayList(enrichmentTag);
        List<Long> judgmentIds = Lists.newArrayList(ccJudgment.getId(), nacJudgment.getId());
        
        
        when(enrichmentTagRepository.findAllByJudgmentIds(judgmentIds)).thenReturn(enrichmentTags);
        
        
        
        // execute
        
        judgmentEnrichmentDbSearchService.search(searchFilter);
        
        
        // assert
        
        verify(databaseSearchService).search(searchFilter);
        verify(enrichmentTagRepository).findAllByJudgmentIds(judgmentIds);
        verify(judgmentEnrichmentService).enrich(ccJudgment, enrichmentTags);
        verify(judgmentEnrichmentService).enrich(nacJudgment, Lists.newArrayList());
        verifyNoMoreInteractions(databaseSearchService, enrichmentTagRepository, judgmentEnrichmentService);
    }
    
    
    @Test
    public void search_JudgmentsFound_WithGeneratedFalse() {
    
        // given
        
        Judgment ccJudgment = TestInMemoryObjectFactory.createCcJudgment();
        Whitebox.setInternalState(ccJudgment, "id", 19l);
        
        List<Judgment> judgments = Lists.newArrayList(ccJudgment);
        
        searchResult = new SearchResult<Judgment>(judgments, 1l, 0, 10);
        searchFilter.setWithGenerated(false);
        
        Mockito.<SearchResult<Judgment>>when(databaseSearchService.search(searchFilter)).thenReturn(searchResult);
        
        
        // execute
        
        judgmentEnrichmentDbSearchService.search(searchFilter);
        
        
        // assert
        
        verify(databaseSearchService).search(searchFilter);
        verifyZeroInteractions(enrichmentTagRepository, judgmentEnrichmentService);
    }

}
