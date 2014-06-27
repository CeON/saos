package pl.edu.icm.saos.importer.commoncourt.download;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.importer.RawSourceCcJudgment;
import pl.edu.icm.saos.persistence.repository.RawSourceCcJudgmentRepository;

/**
 * Common court judgment import - download - processor
 * 
 * 
 * @author Łukasz Dumiszewski
 */
@Service
public class CcjImportDownloadProcessor implements ItemProcessor<SourceCcJudgmentTextData, RawSourceCcJudgment> {

    private static Logger log = LoggerFactory.getLogger(CcjImportDownloadProcessor.class);
    
    private SourceCcjTextDataConverter sourceCcjTextDataConverter;
    
    private RawSourceCcJudgmentRepository rawSourceCcJudgmentRepository;  
    
    
    @Override
    public RawSourceCcJudgment process(SourceCcJudgmentTextData ccjTextData) throws Exception {
        
        RawSourceCcJudgment rawSourceCcJudgment = sourceCcjTextDataConverter.convert(ccjTextData);
        
        if (rawSourceCcJudgmentRepository.findOneBySourceIdAndDataMd5(rawSourceCcJudgment.getSourceId(), rawSourceCcJudgment.getDataMd5()) != null) {
            log.debug("omitting, same judgment has been found in raw_source_cc_judgment: sourceId: {}, dataMd5: {}", rawSourceCcJudgment.getSourceId(), rawSourceCcJudgment.getDataMd5());
            return null;
        }
        
        return rawSourceCcJudgment;
        
    }

    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setSourceCcjTextDataConverter(SourceCcjTextDataConverter sourceCcjTextDataConverter) {
        this.sourceCcjTextDataConverter = sourceCcjTextDataConverter;
    }
    
    @Autowired
    public void setRawSourceCcJudgmentRepository(RawSourceCcJudgmentRepository rawSourceCcJudgmentRepository) {
        this.rawSourceCcJudgmentRepository = rawSourceCcJudgmentRepository;
    }

}
