package pl.edu.icm.saos.importer.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.CourtType;
import pl.edu.icm.saos.persistence.model.MeansOfAppeal;
import pl.edu.icm.saos.persistence.repository.MeansOfAppealRepository;

/**
 * @author madryk
 */
@Service
public class JudgmentMeansOfAppealCreator {

    private MeansOfAppealRepository meansOfAppealRepository;
    
    
    //------------------------ LOGIC --------------------------
    
    public MeansOfAppeal fetchOrCreateMeansOfAppeal(CourtType courtType, String name) {
        MeansOfAppeal meansOfAppeal = meansOfAppealRepository.findOneByCourtTypeAndNameIgnoreCase(courtType, name);
        if (meansOfAppeal == null) {
            meansOfAppeal = new MeansOfAppeal(courtType, name);
            meansOfAppealRepository.save(meansOfAppeal);
            meansOfAppealRepository.flush();
        }
        return meansOfAppeal;
    }

    
    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setMeansOfAppealRepository(MeansOfAppealRepository meansOfAppealRepository) {
        this.meansOfAppealRepository = meansOfAppealRepository;
    }
    
    
}
