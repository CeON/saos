package pl.edu.icm.saos.enrichment.reference;

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
    
    
    @Before
    public void setUp() throws IOException {
        doCallRealMethod().when(tagModifyingJudgmentReferenceRemover).setEnrichmentTagRepository(enrichmentTagRepository);
        doCallRealMethod().when(tagModifyingJudgmentReferenceRemover).setEntityManager(entityManager);
        doCallRealMethod().when(tagModifyingJudgmentReferenceRemover).setJsonStringParser(jsonStringParser);
        doCallRealMethod().when(tagModifyingJudgmentReferenceRemover).setJsonStringWriter(jsonStringWriter);
        
        tagModifyingJudgmentReferenceRemover.setEnrichmentTagRepository(enrichmentTagRepository);
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
        Query query = mock(Query.class);
        when(query.getResultList()).thenReturn(Lists.newArrayList(tag1));
        
        doCallRealMethod().when(tagModifyingJudgmentReferenceRemover).removeReference(2L);
        when(tagModifyingJudgmentReferenceRemover.buildSelectQuery()).thenReturn("query");
        
        when(entityManager.createNativeQuery("query", EnrichmentTag.class)).thenReturn(query);
        when(jsonStringParser.parseAndValidate("et1value")).thenReturn(value1);
        when(jsonStringWriter.write(value1)).thenReturn("et1value_changed");
        
        // execute
        tagModifyingJudgmentReferenceRemover.removeReference(2L);
        
        // assert
        verify(entityManager).createNativeQuery("query", EnrichmentTag.class);
        verify(query).setParameter("tagType", null);
        verify(query).setParameter("judgmentId", "2");
        
        verify(jsonStringParser).parseAndValidate("et1value");
        verify(tagModifyingJudgmentReferenceRemover).removeReference(value1, 2L);
        verify(jsonStringWriter).write(value1);
        
        EnrichmentTag tag1Changed = new EnrichmentTag();
        tag1Changed.setValue("et1value_changed");
        verify(enrichmentTagRepository).save(tag1Changed);
        
    }
    
    
}
