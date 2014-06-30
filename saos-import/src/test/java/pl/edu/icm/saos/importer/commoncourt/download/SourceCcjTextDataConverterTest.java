package pl.edu.icm.saos.importer.commoncourt.download;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import pl.edu.icm.saos.importer.commoncourt.xml.SourceCcJudgment;
import pl.edu.icm.saos.importer.commoncourt.xml.SourceCcJudgmentMarshaller;
import pl.edu.icm.saos.persistence.model.importer.RawSourceCcJudgment;

import com.google.common.collect.Lists;

/**
 * @author Łukasz Dumiszewski
 */

public class SourceCcjTextDataConverterTest {

    
    private SourceCcjTextDataConverter sourceCcjTextDataConverter = new SourceCcjTextDataConverter();
    private SourceCcJudgmentMarshaller marshaller = mock(SourceCcJudgmentMarshaller.class);
    
    private SourceCcJudgmentTextData ccjTextData = new SourceCcJudgmentTextData();
    private SourceCcJudgment sourceCcJudgment = new SourceCcJudgment();
    private String sourceId = "sourceId123";
    private String caseNumber = "caseNumber11223";
    private DateTime publicationDate = new DateTime(2014,6,24,18,15,06,0);
    
    
    @Before
    public void before() {
        ccjTextData.setMetadataSourceUrl("http:/ssdsdsdsd/dsdsd/sd?dssds=3ewdd");
        ccjTextData.setContentSourceUrl("http:/ssdsdsdsd/dsdsd/sd?dssds=3ewdd");
        ccjTextData.setContent("<html><body><h1>W imieniu Najjaśniejszej...</h1><p>Bla bla bla</p></body></html>");
        ccjTextData.setMetadata("<xml><id>sdsds</id><signature>dsdsdsd</signature></xml>");
        
        sourceCcJudgment.setSignature(caseNumber);
        sourceCcJudgment.setId(sourceId);
        sourceCcJudgment.setPublicationDate(publicationDate);
        
        sourceCcjTextDataConverter.setCcJudgmentMarshaller(marshaller);
        when(marshaller.unmarshal(Mockito.eq(ccjTextData.getMetadata()))).thenReturn(sourceCcJudgment);
        
    }
    
    
    @Test
    public void convert_TypeReason() {
        
        sourceCcJudgment.setTypes(Lists.newArrayList("Reason"));
        
        RawSourceCcJudgment rJudgment = sourceCcjTextDataConverter.convert(ccjTextData);
        
        assertRawSourceCcJudgment(rJudgment, true);
    }

    

    @Test
    public void convert_TypeSentence() {
        
        sourceCcJudgment.setTypes(Lists.newArrayList("Reason", "Sentence"));
        
        RawSourceCcJudgment rJudgment = sourceCcjTextDataConverter.convert(ccjTextData);
        
        assertRawSourceCcJudgment(rJudgment, false);
    }

    

    private void assertRawSourceCcJudgment(RawSourceCcJudgment rJudgment, boolean justReasons) {
        assertEquals(ccjTextData.getContent(), rJudgment.getTextContent());
        assertEquals(ccjTextData.getMetadata(), rJudgment.getTextMetadata());
        assertEquals(ccjTextData.getMetadataSourceUrl(), rJudgment.getSourceUrl());
        assertEquals(ccjTextData.getContentSourceUrl(), rJudgment.getContentSourceUrl());
        
        assertEquals(sourceId, rJudgment.getSourceId());
        assertEquals(caseNumber, rJudgment.getCaseNumber());
        assertEquals(publicationDate, rJudgment.getPublicationDate());
        assertFalse(StringUtils.isEmpty(rJudgment.getDataMd5()));
        assertEquals(justReasons, rJudgment.isJustReasons());
    }
    
}
