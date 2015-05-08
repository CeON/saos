package pl.edu.icm.saos.enrichment.reference;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import pl.edu.icm.saos.common.json.JsonStringParser;
import pl.edu.icm.saos.common.json.JsonStringWriter;
import pl.edu.icm.saos.persistence.enrichment.EnrichmentTagRepository;
import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTag;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;

import com.google.common.collect.Lists;

/**
 * @author madryk
 */
@RunWith(MockitoJUnitRunner.class)
public class TagModifyingJudgmentReferenceRemoverTest {

    @Mock
    private TagModifyingJudgmentReferenceRemover<Object> tagModifyingJudgmentReferenceRemover;
    
    @Mock
    private EnrichmentTagRepository enrichmentTagRepository;
    
    @Mock
    private JsonStringParser<Object> jsonStringParser;
    
    @Mock
    private JsonStringWriter<Object> jsonStringWriter;
    
    @Mock
    private EntityManager entityManager;
    
    @Mock
    private JudgmentRepository judgmentRepository;
    
    
    @Before
    public void setUp() throws IOException {
        doCallRealMethod().when(tagModifyingJudgmentReferenceRemover).setEnrichmentTagRepository(any());
        doCallRealMethod().when(tagModifyingJudgmentReferenceRemover).setJudgmentRepository(any());
        doCallRealMethod().when(tagModifyingJudgmentReferenceRemover).setEntityManager(any());
        doCallRealMethod().when(tagModifyingJudgmentReferenceRemover).setJsonStringParser(any());
        doCallRealMethod().when(tagModifyingJudgmentReferenceRemover).setJsonStringWriter(any());
        doCallRealMethod().when(tagModifyingJudgmentReferenceRemover).removeReference(any());
        
        tagModifyingJudgmentReferenceRemover.setEnrichmentTagRepository(enrichmentTagRepository);
        tagModifyingJudgmentReferenceRemover.setJudgmentRepository(judgmentRepository);
        tagModifyingJudgmentReferenceRemover.setEntityManager(entityManager);
        tagModifyingJudgmentReferenceRemover.setJsonStringParser(jsonStringParser);
        tagModifyingJudgmentReferenceRemover.setJsonStringWriter(jsonStringWriter);
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void removeReference() throws IOException {
        
        // given
        Object value1 = new Object();
        EnrichmentTag tag1 = new EnrichmentTag();
        tag1.setValue("et1value");
        tag1.setJudgmentId(7);
        
        Object value2 = new Object();
        EnrichmentTag tag2 = new EnrichmentTag();
        tag2.setValue("et2value");
        tag2.setJudgmentId(8);
        
        Query query = mock(Query.class);
        when(query.getResultList()).thenReturn(Lists.newArrayList(tag1, tag2));
        
        when(tagModifyingJudgmentReferenceRemover.buildSelectQuery()).thenReturn("query");
        
        when(entityManager.createNativeQuery("query", EnrichmentTag.class)).thenReturn(query);
        when(jsonStringParser.parseAndValidate("et1value")).thenReturn(value1);
        when(jsonStringParser.parseAndValidate("et2value")).thenReturn(value2);
        when(jsonStringWriter.write(value1)).thenReturn("et1value_changed");
        when(jsonStringWriter.write(value2)).thenReturn("et2value_changed");
        
        // execute
        tagModifyingJudgmentReferenceRemover.removeReference(Lists.newArrayList(2L, 3L));
        
        // assert
        verify(entityManager).createNativeQuery("query", EnrichmentTag.class);
        verify(query).setParameter("judgmentIds", Lists.newArrayList("2", "3"));
        
        verify(tagModifyingJudgmentReferenceRemover).removeReference(value1, Lists.newArrayList(2L, 3L));
        verify(tagModifyingJudgmentReferenceRemover).removeReference(value2, Lists.newArrayList(2L, 3L));
        
        verify(judgmentRepository).markAsNotindexed(Lists.newArrayList(7L, 8L));
        
        assertEquals("et1value_changed", tag1.getValue());
        assertEquals("et2value_changed", tag2.getValue());
        
    }
    
    
}
