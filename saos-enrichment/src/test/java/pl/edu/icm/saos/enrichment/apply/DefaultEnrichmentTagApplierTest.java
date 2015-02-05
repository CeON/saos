package pl.edu.icm.saos.enrichment.apply;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.Mockito;

import pl.edu.icm.saos.common.json.JsonStringParser;
import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTag;
import pl.edu.icm.saos.persistence.model.Judgment;

/**
 * @author Łukasz Dumiszewski
 */

public class DefaultEnrichmentTagApplierTest {

    private static final String HANDLED_TAG_TYPE = "handledTagType";
    
 
    @Mock private JsonStringParser<String> jsonStringParser;
    
    @Mock private EnrichmentTagValueConverter<String, Object> enrichmentTagValueConverter;
    
    @Mock private JudgmentUpdater<Object> judgmentUpdater;
    
    @Mock private Judgment judgment;
    
    @Mock private EnrichmentTag enrichmentTag;
 
    
    private DefaultEnrichmentTagApplier<String, Object> defaultEnrichmentTagApplier = new DefaultEnrichmentTagApplier<>(HANDLED_TAG_TYPE);
    
    @Rule public ExpectedException expectedException = ExpectedException.none();
    
    
    @Before
    public void before() {
        
        initMocks(this);
        
        defaultEnrichmentTagApplier.setJsonStringParser(jsonStringParser);
        defaultEnrichmentTagApplier.setEnrichmentTagValueConverter(enrichmentTagValueConverter);
        defaultEnrichmentTagApplier.setJudgmentUpdater(judgmentUpdater);
        
        when(enrichmentTag.getTagType()).thenReturn(HANDLED_TAG_TYPE);
        
    }
    
    
    //------------------------ TESTS --------------------------
    
    
    @Test
    public void handlesEnrichmentTagType_DoesNotHandle() {
        
        // execute & assert
        assertFalse(defaultEnrichmentTagApplier.handlesEnrichmentTagType("XXX"));
        
        // assert
        verifyZeroInteractions(enrichmentTagValueConverter, jsonStringParser, judgmentUpdater);

    }
    
    @Test
    public void handlesEnrichmentTagType_Handles() {
        
        // execute & assert
        assertTrue(defaultEnrichmentTagApplier.handlesEnrichmentTagType(HANDLED_TAG_TYPE));
        
        // assert
        verifyZeroInteractions(enrichmentTagValueConverter, jsonStringParser, judgmentUpdater);

    }
    
    
    @Test
    public void applyEnrichmentTag_NotHandledTagType() {
        
        // given
        when(enrichmentTag.getTagType()).thenReturn("ABC");
        
        // execute
        catchException(defaultEnrichmentTagApplier).applyEnrichmentTag(judgment, enrichmentTag);
        
        // assert
        assertTrue(caughtException() instanceof IllegalArgumentException);
        verifyZeroInteractions(enrichmentTagValueConverter, jsonStringParser, judgmentUpdater);
    }
    
    
    @Test
    public void applyEnrichmentTag_NullJudgment() {
        
        // execute
        catchException(defaultEnrichmentTagApplier).applyEnrichmentTag(null, enrichmentTag);
        
        // assert
        assertTrue(caughtException() instanceof NullPointerException);
        verifyZeroInteractions(enrichmentTagValueConverter, jsonStringParser, judgmentUpdater);

    }
    
    
    @Test
    public void applyEnrichmentTag_NullEnrichmentTag() {
        
        // execute
        catchException(defaultEnrichmentTagApplier).applyEnrichmentTag(judgment, null);
        
        // assert
        assertTrue(caughtException() instanceof NullPointerException);
        verifyZeroInteractions(enrichmentTagValueConverter, jsonStringParser, judgmentUpdater);
    }
    
    
    @Test
    public void applyEnrichmentTag_JsonParseEx() throws Exception {
        
        // given
        when(jsonStringParser.parseAndValidate(Mockito.anyString())).thenThrow(new IllegalArgumentException());
        
        // execute
        catchException(defaultEnrichmentTagApplier).applyEnrichmentTag(judgment, enrichmentTag);
        
        // assert
        assertTrue(caughtException() instanceof IllegalArgumentException);
        verify(jsonStringParser).parseAndValidate(enrichmentTag.getValue());
        verifyZeroInteractions(enrichmentTagValueConverter, judgmentUpdater);
    }
    
    
    @Test
    public void applyEnrichmentTag() throws Exception {
        
        // given
        String tagValueObject = "YYY";
        Object modelObject = mock(Object.class);
        
        when(enrichmentTag.getValue()).thenReturn(tagValueObject);
        when(jsonStringParser.parseAndValidate(enrichmentTag.getValue())).thenReturn(tagValueObject);
        when(enrichmentTagValueConverter.convert(tagValueObject)).thenReturn(modelObject);
        
        
        // execute
        defaultEnrichmentTagApplier.applyEnrichmentTag(judgment, enrichmentTag);
        
        
        // assert
        verify(jsonStringParser).parseAndValidate(tagValueObject);
        verify(enrichmentTagValueConverter).convert(tagValueObject);
        verify(judgmentUpdater).addToJudgment(judgment, modelObject);
        
        verifyNoMoreInteractions(jsonStringParser, enrichmentTagValueConverter, judgmentUpdater);
    }
}
