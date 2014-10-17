package pl.edu.icm.saos.importer.notapi.supremecourt.judgment.download;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.importer.notapi.RawSourceScJudgment;
import pl.edu.icm.saos.persistence.repository.RawSourceScJudgmentRepository;

/**
 *  Simple supreme court judgment import - download - writer
 *  
 * @author Łukasz Dumiszewski
 */
@Service
public class ScjImportDownloadWriter implements ItemWriter<RawSourceScJudgment> {

    private RawSourceScJudgmentRepository simpleRawSourceScJudgmentRepository;
    
    
    @Override
    public void write(List<? extends RawSourceScJudgment> rJudgments) {
        simpleRawSourceScJudgmentRepository.save(rJudgments);
        
    }

    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setSimpleRawSourceScJudgmentRepository(RawSourceScJudgmentRepository simpleRawSourceScJudgmentRepository) {
        this.simpleRawSourceScJudgmentRepository = simpleRawSourceScJudgmentRepository;
    }

}
