package pl.edu.icm.saos.importer.notapi.constitutionaltribunal.judgment.download;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.importer.notapi.RawSourceCtJudgment;
import pl.edu.icm.saos.persistence.repository.RawSourceCtJudgmentRepository;

/**
 * @author madryk
 */
@Service
public class CtjImportDownloadWriter implements ItemWriter<RawSourceCtJudgment> {

    private RawSourceCtJudgmentRepository rawSourceCtJudgmentRepository;
    
    
    //------------------------ LOGIC --------------------------
    
    @Override
    public void write(List<? extends RawSourceCtJudgment> rawJudgments)
            throws Exception {
        rawSourceCtJudgmentRepository.save(rawJudgments);
    }

    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setRawSourceCtJudgmentRepository(
            RawSourceCtJudgmentRepository rawSourceCtJudgmentRepository) {
        this.rawSourceCtJudgmentRepository = rawSourceCtJudgmentRepository;
    }

}
