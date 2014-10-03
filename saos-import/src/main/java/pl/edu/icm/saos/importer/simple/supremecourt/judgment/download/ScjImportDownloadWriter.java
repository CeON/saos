package pl.edu.icm.saos.importer.simple.supremecourt.judgment.download;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.importer.SimpleRawSourceScJudgment;
import pl.edu.icm.saos.persistence.repository.SimpleRawSourceScJudgmentRepository;

/**
 *  Simple supreme court judgment import - download - writer
 *  
 * @author Łukasz Dumiszewski
 */
@Service
public class ScjImportDownloadWriter implements ItemWriter<SimpleRawSourceScJudgment> {

    private SimpleRawSourceScJudgmentRepository simpleRawSourceScJudgmentRepository;
    
    
    @Override
    public void write(List<? extends SimpleRawSourceScJudgment> rJudgments) {
        simpleRawSourceScJudgmentRepository.save(rJudgments);
        
    }

    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setSimpleRawSourceScJudgmentRepository(SimpleRawSourceScJudgmentRepository simpleRawSourceScJudgmentRepository) {
        this.simpleRawSourceScJudgmentRepository = simpleRawSourceScJudgmentRepository;
    }

}
