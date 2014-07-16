package pl.edu.icm.saos.importer.commoncourt.judgment.xml;

import java.io.StringReader;

import javax.xml.transform.stream.StreamSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;

/**
 * @author Łukasz Dumiszewski
 */
@Service("ccJudgmentMarshaller")
public class SourceCcJudgmentMarshaller {
  
    private Jaxb2Marshaller ccJudgmentJaxb2Marshaller;
    
    public SourceCcJudgment unmarshal(String xmlCcJudgmentMetadata) {
        return (SourceCcJudgment)ccJudgmentJaxb2Marshaller.unmarshal(new StreamSource(new StringReader(xmlCcJudgmentMetadata)));
    }

    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setCcJudgmentJaxb2Marshaller(Jaxb2Marshaller ccJudgmentJaxb2Marshaller) {
        this.ccJudgmentJaxb2Marshaller = ccJudgmentJaxb2Marshaller;
    }
}
