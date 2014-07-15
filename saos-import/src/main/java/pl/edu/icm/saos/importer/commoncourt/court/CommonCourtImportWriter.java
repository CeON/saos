package pl.edu.icm.saos.importer.commoncourt.court;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.repository.CommonCourtRepository;

/**
 * @author Łukasz Dumiszewski
 */
@Service("commonCourtImportWriter")
public class CommonCourtImportWriter implements ItemWriter<CommonCourt> {

    private CommonCourtRepository commonCourtRepository;
    
    
    @Override
    public void write(List<? extends CommonCourt> courts) throws Exception {
        commonCourtRepository.save(courts);
        commonCourtRepository.flush();
        
    }

    
    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setCommonCourtRepository(CommonCourtRepository commonCourtRepository) {
        this.commonCourtRepository = commonCourtRepository;
    }

}
