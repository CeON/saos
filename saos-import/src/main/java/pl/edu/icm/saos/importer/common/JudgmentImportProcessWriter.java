package pl.edu.icm.saos.importer.common;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.icm.saos.importer.common.correction.ImportCorrectionConverter;
import pl.edu.icm.saos.persistence.correction.JudgmentCorrectionRepository;
import pl.edu.icm.saos.persistence.correction.model.JudgmentCorrection;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;

/**
 *
 * Common writer for all import processes
 * 
 * @author Łukasz Dumiszewski
 */

public class JudgmentImportProcessWriter<T extends Judgment> implements ItemWriter<JudgmentWithCorrectionList<T>> {

    
    private JudgmentRepository judgmentRepository;
    
    private ImportCorrectionConverter importCorrectionConverter;
    
    private JudgmentCorrectionRepository judgmentCorrectionRepository;
    
    
    
    //------------------------ LOGIC --------------------------
    
    @Override
    public void write(List<? extends JudgmentWithCorrectionList<T>> judgmentsWithCorrectionList) {
        
        List<Judgment> judgments = judgmentsWithCorrectionList.stream().map(j->j.getJudgment()).collect(Collectors.toList()); 
        judgmentRepository.save(judgments);
        
        judgmentRepository.flush();
        
        judgmentCorrectionRepository.deleteByJudgmentIds(judgments.stream().map(j->j.getId()).collect(Collectors.toList()));
        
        judgmentCorrectionRepository.flush();
        
        for (JudgmentWithCorrectionList<T> jwc : judgmentsWithCorrectionList) {
            
            List<JudgmentCorrection> judgmentCorrections = importCorrectionConverter.convertToJudgmentCorrections(jwc.getJudgment(), jwc.getCorrectionList().getImportCorrections());
                       
            judgmentCorrectionRepository.save(judgmentCorrections);
        }
        
        judgmentCorrectionRepository.flush();
        
    }

    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setJudgmentRepository(JudgmentRepository judgmentRepository) {
        this.judgmentRepository = judgmentRepository;
    }

    @Autowired
    public void setImportCorrectionConverter(ImportCorrectionConverter importCorrectionConverter) {
        this.importCorrectionConverter = importCorrectionConverter;
    }

    @Autowired
    public void setJudgmentCorrectionRepository(JudgmentCorrectionRepository judgmentCorrectionRepository) {
        this.judgmentCorrectionRepository = judgmentCorrectionRepository;
    }

}