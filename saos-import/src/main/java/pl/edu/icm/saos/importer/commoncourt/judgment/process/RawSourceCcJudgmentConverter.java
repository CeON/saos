package pl.edu.icm.saos.importer.commoncourt.judgment.process;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.importer.commoncourt.judgment.xml.SourceCcJudgment;
import pl.edu.icm.saos.importer.commoncourt.judgment.xml.SourceCcJudgmentMarshaller;
import pl.edu.icm.saos.persistence.model.importer.RawSourceCcJudgment;

/**
 * @author Łukasz Dumiszewski
 */
@Service("rawSourceCcjConverter")
class RawSourceCcJudgmentConverter {

    private SourceCcJudgmentMarshaller ccJudgmentMarshaller;
    
    
    
    public SourceCcJudgment convertSourceCcJudgment(final RawSourceCcJudgment rawJudgment) {
        SourceCcJudgment sourceCcJudgment = ccJudgmentMarshaller.unmarshal(rawJudgment.getTextMetadata());
        sourceCcJudgment.setTextContent(rawJudgment.getTextContent());
        sourceCcJudgment.setSourceUrl(rawJudgment.getSourceUrl());
        return sourceCcJudgment;
        
    }
    
    

    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setCcJudgmentMarshaller(SourceCcJudgmentMarshaller ccJudgmentMarshaller) {
        this.ccJudgmentMarshaller = ccJudgmentMarshaller;
    }
    
    
}
