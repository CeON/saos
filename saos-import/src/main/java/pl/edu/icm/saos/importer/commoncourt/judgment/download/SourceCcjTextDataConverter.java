package pl.edu.icm.saos.importer.commoncourt.judgment.download;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.importer.commoncourt.judgment.xml.SourceCcJudgment;
import pl.edu.icm.saos.importer.commoncourt.judgment.xml.SourceCcJudgmentMarshaller;
import pl.edu.icm.saos.persistence.model.importer.RawSourceCcJudgment;

/**
 * @author Łukasz Dumiszewski
 */

@Service("sourceCcjTextDataConverter")
public class SourceCcjTextDataConverter {

    
    private SourceCcJudgmentMarshaller ccJudgmentMarshaller;
    
    
    public RawSourceCcJudgment convert(SourceCcJudgmentTextData ccjTextData) {
        
        RawSourceCcJudgment rJudgment = new RawSourceCcJudgment();
        
        rJudgment.setTextMetadata(ccjTextData.getMetadata());
        rJudgment.setTextContent(ccjTextData.getContent());
        rJudgment.setSourceUrl(ccjTextData.getMetadataSourceUrl());
        rJudgment.setContentSourceUrl(ccjTextData.getContentSourceUrl());
        
        SourceCcJudgment sourceCcJudgment = ccJudgmentMarshaller.unmarshal(ccjTextData.getMetadata());
        
        rJudgment.setCaseNumber(sourceCcJudgment.getSignature());
        rJudgment.setSourceId(sourceCcJudgment.getId());
        rJudgment.setJustReasons(sourceCcJudgment.getTypes().size()==1 && sourceCcJudgment.getTypes().get(0).equalsIgnoreCase("REASON"));
        rJudgment.setPublicationDate(sourceCcJudgment.getPublicationDate());
        rJudgment.setDataMd5(DigestUtils.md5Hex(rJudgment.getTextMetadata() + rJudgment.getTextContent()));
        
        return rJudgment;
    }
    
    
    
    //------------------------ SETTERS --------------------------
    
    
    @Autowired
    public void setCcJudgmentMarshaller(SourceCcJudgmentMarshaller ccJudgmentMarshaller) {
        this.ccJudgmentMarshaller = ccJudgmentMarshaller;
    }
    
}
