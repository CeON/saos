package pl.edu.icm.saos.importer.commoncourt.judgment.process;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import pl.edu.icm.saos.importer.commoncourt.judgment.xml.SourceCcJudgment;
import pl.edu.icm.saos.importer.commoncourt.judgment.xml.SourceCcJudgmentMarshaller;
import pl.edu.icm.saos.persistence.model.importer.RawSourceCcJudgment;

/**
 * @author Łukasz Dumiszewski
 */

public class RawSourceCcJudgmentConverterTest {

    private RawSourceCcJudgmentConverter rawSourceCcJudgmentConverter = new RawSourceCcJudgmentConverter();
    
    
    private SourceCcJudgmentMarshaller sourceCcJudgmentMarshaller = mock(SourceCcJudgmentMarshaller.class);
    
    @Before
    public void before() {
        
        rawSourceCcJudgmentConverter.setCcJudgmentMarshaller(sourceCcJudgmentMarshaller);
        
        
    }
    
    @Test
    public void convertSourceCcJudgment() {
        RawSourceCcJudgment rawJudgment = Mockito.mock(RawSourceCcJudgment.class);
        
        String textContent = "TEXT CONTENT OF THE JUDGMENT";
        String textMetadata = "METADATA AS TEXT";
        String sourceUrl = "http://ssss/ssss/ssss";
        
        when(rawJudgment.getTextContent()).thenReturn(textContent);
        when(rawJudgment.getTextMetadata()).thenReturn(textMetadata);
        when(rawJudgment.getSourceUrl()).thenReturn(sourceUrl);
        
        SourceCcJudgment ccJudgment = new SourceCcJudgment();
        when(sourceCcJudgmentMarshaller.unmarshal(Mockito.eq(textMetadata))).thenReturn(ccJudgment);
        
        SourceCcJudgment ccJudgmentReturned = rawSourceCcJudgmentConverter.convertSourceCcJudgment(rawJudgment);
        
        assertTrue(ccJudgment == ccJudgmentReturned);
        
        assertEquals(textContent, ccJudgment.getTextContent());
        assertEquals(sourceUrl, ccJudgment.getSourceUrl());
        
        verify(rawJudgment).getTextContent();
        verify(rawJudgment).getTextMetadata();
        verify(rawJudgment).getSourceUrl();
        verifyNoMoreInteractions(rawJudgment);
        
    }
    
}
