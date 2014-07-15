package pl.edu.icm.saos.importer.commoncourt.court;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.repository.CommonCourtRepository;

/**
 * @author Łukasz Dumiszewski
 */
@Service("commonCourtImportProcessor")
public class CommonCourtImportProcessor implements ItemProcessor<XmlCommonCourt, CommonCourt> {

    private static Logger log = LoggerFactory.getLogger(CommonCourtImportProcessor.class);
    
    private CommonCourtRepository commonCourtRepository;
    
    private SourceCommonCourtConverter sourceCcConverter;
    
    private CommonCourtOverwriter ccOverwriter;
    
    
    @Override
    public CommonCourt process(XmlCommonCourt sourceCommonCourt) throws Exception {
        log.debug("processing: \n {}", sourceCommonCourt);
        CommonCourt newCommonCourt = sourceCcConverter.convert(sourceCommonCourt);
        
        CommonCourt existingCourt = commonCourtRepository.findOneByCode(newCommonCourt.getCode());
        
        if (existingCourt != null) {
            ccOverwriter.overwrite(existingCourt, newCommonCourt);
            newCommonCourt = existingCourt;
        }
        
        return newCommonCourt;
        
    }

    
    //------------------------ SETTERS --------------------------
    
    
    @Autowired
    public void setCommonCourtRepository(CommonCourtRepository commonCourtRepository) {
        this.commonCourtRepository = commonCourtRepository;
    }


    @Autowired
    public void setSourceCcConverter(SourceCommonCourtConverter sourceCcConverter) {
        this.sourceCcConverter = sourceCcConverter;
    }


    @Autowired
    public void setCcOverwriter(CommonCourtOverwriter ccOverwriter) {
        this.ccOverwriter = ccOverwriter;
    }

}
