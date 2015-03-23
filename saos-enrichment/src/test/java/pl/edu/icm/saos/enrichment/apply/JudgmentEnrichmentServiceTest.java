package pl.edu.icm.saos.enrichment.apply;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import pl.edu.icm.saos.persistence.common.TestInMemoryObjectFactory;
import pl.edu.icm.saos.persistence.enrichment.EnrichmentTagRepository;
import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTag;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;

import com.google.common.collect.Lists;

/**
 * @author Łukasz Dumiszewski
 */

public class JudgmentEnrichmentServiceTest {

    private JudgmentEnrichmentService judgmentEnrichmentService = new JudgmentEnrichmentService();
    
    @Mock private JudgmentRepository judgmentRepository;

    @Mock private EnrichmentTagRepository enrichmentTagRepository;
    
    @Mock private EnrichmentTagApplierManager enrichmentTagApplierManager;
    
    
    private Judgment judgment = TestInMemoryObjectFactory.createScJudgment();
    
    
    
    @Before
    public void before() {
        
        initMocks(this);

        judgmentEnrichmentService.setJudgmentRepository(judgmentRepository);
        judgmentEnrichmentService.setEnrichmentTagRepository(enrichmentTagRepository);
        judgmentEnrichmentService.setEnrichmentTagApplierManager(enrichmentTagApplierManager);
        
    }
    
    
    
    //------------------------ TESTS --------------------------
    
    
    
    //---------- judgmentEnrichmentService.enrich(Judgment)
    
    
    
    @Test(expected=NullPointerException.class)
    public void enrich_Judgment_Null() {
        
        // execute
        judgmentEnrichmentService.enrich(null);
    }
    
    
    @Test
    public void enrich_Judgment_NoTagsFound() {
        
        // given
        when(enrichmentTagRepository.findAllByJudgmentId(judgment.getId())).thenReturn(Lists.newArrayList());
        
        // execute
        judgmentEnrichmentService.enrich(judgment);
        
        // assert
        verifyZeroInteractions(judgmentRepository);
        verifyZeroInteractions(enrichmentTagApplierManager);
    
    }
    
    
    @Test
    public void enrich_Judgment_TagsFound() {
        
        // given
        
        EnrichmentTag enrichmentTag1 = new EnrichmentTag();
        enrichmentTag1.setTagType("TAG_TYPE_1");
        EnrichmentTag enrichmentTag2 = new EnrichmentTag();
        enrichmentTag1.setTagType("TAG_TYPE_2");
        
        when(enrichmentTagRepository.findAllByJudgmentId(judgment.getId())).thenReturn(Lists.newArrayList(enrichmentTag1, enrichmentTag2));
        
        EnrichmentTagApplier enrichmentTagApplier1 = mock(EnrichmentTagApplier.class);
        when(enrichmentTagApplierManager.getEnrichmentTagApplier(enrichmentTag1.getTagType())).thenReturn(enrichmentTagApplier1);
        
        EnrichmentTagApplier enrichmentTagApplier2 = mock(EnrichmentTagApplier.class);
        when(enrichmentTagApplierManager.getEnrichmentTagApplier(enrichmentTag2.getTagType())).thenReturn(enrichmentTagApplier2);
        
        // execute
        
        judgmentEnrichmentService.enrich(judgment);
        
        
        // assert
        
        verifyZeroInteractions(judgmentRepository);
        verify(enrichmentTagApplierManager).getEnrichmentTagApplier(enrichmentTag1.getTagType());
        verify(enrichmentTagApplierManager).getEnrichmentTagApplier(enrichmentTag2.getTagType());
        verify(enrichmentTagApplier1).applyEnrichmentTag(judgment, enrichmentTag1);
        verify(enrichmentTagApplier2).applyEnrichmentTag(judgment, enrichmentTag2);
        
        
    }
    
    
    
    //---------- judgmentEnrichmentService.enrich(Judgment, List<EnrichmentTag>)
    
    
    @Test
    public void enrich_Judgment_EnrichmentTags() {
        
        // given
        
        EnrichmentTag enrichmentTag1 = new EnrichmentTag();
        enrichmentTag1.setTagType("TAG_TYPE_1");
        EnrichmentTag enrichmentTag2 = new EnrichmentTag();
        enrichmentTag1.setTagType("TAG_TYPE_2");
        
        EnrichmentTagApplier enrichmentTagApplier1 = mock(EnrichmentTagApplier.class);
        when(enrichmentTagApplierManager.getEnrichmentTagApplier(enrichmentTag1.getTagType())).thenReturn(enrichmentTagApplier1);
        
        EnrichmentTagApplier enrichmentTagApplier2 = mock(EnrichmentTagApplier.class);
        when(enrichmentTagApplierManager.getEnrichmentTagApplier(enrichmentTag2.getTagType())).thenReturn(enrichmentTagApplier2);
        
        // execute
        
        judgmentEnrichmentService.enrich(judgment, Lists.newArrayList(enrichmentTag1, enrichmentTag2));
        
        
        // assert
        
        verifyZeroInteractions(judgmentRepository, enrichmentTagRepository);
        verify(enrichmentTagApplierManager).getEnrichmentTagApplier(enrichmentTag1.getTagType());
        verify(enrichmentTagApplierManager).getEnrichmentTagApplier(enrichmentTag2.getTagType());
        verify(enrichmentTagApplier1).applyEnrichmentTag(judgment, enrichmentTag1);
        verify(enrichmentTagApplier2).applyEnrichmentTag(judgment, enrichmentTag2);
        
        
    }
    
    
    @Test
    public void enrich_Judgment_EnrichmentTags_EmptyList() {
        
        // execute
        
        judgmentEnrichmentService.enrich(judgment, Lists.newArrayList());
        
        
        // assert
        
        verifyZeroInteractions(judgmentRepository, enrichmentTagRepository, enrichmentTagApplierManager);
        
        
    }
    
    
    @Test(expected=NullPointerException.class)
    public void enrich_Judgment_EnrichmentTags_NullList() {
        
        // execute
        
        judgmentEnrichmentService.enrich(judgment, (List<EnrichmentTag>)null);
        
    }
    
    
    
    //---------- judgmentEnrichmentService.enrich(Judgment, EnrichmentTag)
    
    
    @Test
    public void enrich_Judgment_EnrichmentTag() {
        
        // given
        
        EnrichmentTag enrichmentTag = new EnrichmentTag();
        enrichmentTag.setTagType("TAG_TYPE_1");
        enrichmentTag.setJudgmentId(judgment.getId());
        
        EnrichmentTagApplier enrichmentTagApplier = mock(EnrichmentTagApplier.class);
        when(enrichmentTagApplierManager.getEnrichmentTagApplier(enrichmentTag.getTagType())).thenReturn(enrichmentTagApplier);
        
        // execute
        
        judgmentEnrichmentService.enrich(judgment, enrichmentTag);
        
        // assert
        
        verifyZeroInteractions(judgmentRepository, enrichmentTagRepository);
        verify(enrichmentTagApplierManager).getEnrichmentTagApplier(enrichmentTag.getTagType());
        verify(enrichmentTagApplier).applyEnrichmentTag(judgment, enrichmentTag);
        
    }
    
    
    
    @Test(expected=IllegalArgumentException.class)
    public void enrich_Judgment_EnrichmentTag_IncorrectEnrichmentTagJudgmentId() {
        
        // given
        
        EnrichmentTag enrichmentTag = new EnrichmentTag();
        enrichmentTag.setTagType("TAG_TYPE_1");
        long otherJudgmentId = judgment.getId() + 1;
        enrichmentTag.setJudgmentId(otherJudgmentId);
        
        
        // execute
        
        judgmentEnrichmentService.enrich(judgment, enrichmentTag);
    }
    
    
    @Test(expected=NullPointerException.class)
    public void enrich_Judgment_EnrichmentTag_JudgmentNull() {
        
        // given
        
        EnrichmentTag enrichmentTag = new EnrichmentTag();
        enrichmentTag.setTagType("TAG_TYPE_1");
        enrichmentTag.setJudgmentId(judgment.getId());
        
        
        // execute
        
        judgmentEnrichmentService.enrich(null, enrichmentTag);
    }
    
    
    @Test(expected=NullPointerException.class)
    public void enrich_Judgment_EnrichmentTag_EnrichmentTagNull() {
        
        // execute
        
        judgmentEnrichmentService.enrich(judgment, (EnrichmentTag)null);
    }
    
    
    
    //---------- judgmentEnrichmentService.findOneAndEnrich(long judgmentId)
    
    @Test
    public void findOneAndEnrich_NotFound() {
        
        // given
        when(judgmentRepository.findOneAndInitialize(Mockito.anyLong())).thenReturn(null);
        
        // execute & assert
        assertNull(judgmentEnrichmentService.findOneAndEnrich(1));
        
    }
    

    @Test
    public void findOneAndEnrich_Found() {
        
        // given
        when(judgmentRepository.findOneAndInitialize(judgment.getId())).thenReturn(judgment);
        
        EnrichmentTag enrichmentTag1 = new EnrichmentTag();
        enrichmentTag1.setTagType("TAG_TYPE_1");
        
        when(enrichmentTagRepository.findAllByJudgmentId(judgment.getId())).thenReturn(Lists.newArrayList(enrichmentTag1));
        
        EnrichmentTagApplier enrichmentTagApplier1 = mock(EnrichmentTagApplier.class);
        when(enrichmentTagApplierManager.getEnrichmentTagApplier(enrichmentTag1.getTagType())).thenReturn(enrichmentTagApplier1);
        
        
        // execute
        Judgment enrichedJudgment = judgmentEnrichmentService.findOneAndEnrich(judgment.getId());
        
        
        // assert
        assertNotNull(enrichedJudgment);
        assertTrue(enrichedJudgment == judgment);
        verify(judgmentRepository).findOneAndInitialize(judgment.getId());
        verify(enrichmentTagApplierManager).getEnrichmentTagApplier(enrichmentTag1.getTagType());
        verify(enrichmentTagApplier1).applyEnrichmentTag(judgment, enrichmentTag1);
        
    }

    
}
