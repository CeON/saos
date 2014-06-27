package pl.edu.icm.saos.importer.commoncourt.xml;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

/**
 * @author Łukasz Dumiszewski
 */

public class SourceCcJudgmentMarshallerTest {

    private SourceCcJudgmentMarshaller sourceCcJudgmentMarshaller = new SourceCcJudgmentMarshaller();
    private Jaxb2Marshaller jaxb2Marshaller = mock(Jaxb2Marshaller.class);
    
    
    
    @Test
    public void unmarshal() throws IOException {
        SourceCcJudgment sourceCcJudgment = new SourceCcJudgment();
        String judgmentXml = "<xxx>sdsdsd</xxx>xcxcxc xcdx cx c";
        when(jaxb2Marshaller.unmarshal(Mockito.any(Source.class))).thenReturn(sourceCcJudgment);
        
        sourceCcJudgmentMarshaller.setCcJudgmentJaxb2Marshaller(jaxb2Marshaller);
        
        SourceCcJudgment uSourceCcJudgment = sourceCcJudgmentMarshaller.unmarshal(judgmentXml);
        assertNotNull(uSourceCcJudgment);
        
        ArgumentCaptor<StreamSource> argument = ArgumentCaptor.forClass(StreamSource.class);
        verify(jaxb2Marshaller).unmarshal(argument.capture());
        
        char[] buff = new char[judgmentXml.toCharArray().length];
        argument.getValue().getReader().read(buff);
        assertTrue(Arrays.equals(judgmentXml.toCharArray(), buff));
        
    }
    
}
