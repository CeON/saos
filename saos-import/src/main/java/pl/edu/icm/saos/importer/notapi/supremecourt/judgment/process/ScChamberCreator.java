package pl.edu.icm.saos.importer.notapi.supremecourt.judgment.process;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.SupremeCourtChamber;
import pl.edu.icm.saos.persistence.repository.ScChamberRepository;

import com.google.common.base.Preconditions;

/**
 * @author Łukasz Dumiszewski
 */
@Service("scChamberCreator")
public class ScChamberCreator {
    
    private ScChamberRepository scChamberRepository;
    
    
    
    //------------------------ LOGIC --------------------------
    
    
    public SupremeCourtChamber getOrCreateScChamber(String chamberName) {
        Preconditions.checkArgument(!StringUtils.isBlank(chamberName));
        
        SupremeCourtChamber scChamber = scChamberRepository.findOneByName(chamberName);
        if (scChamber == null) {
            scChamber = new SupremeCourtChamber();
            scChamber.setName(chamberName);
            scChamberRepository.saveAndFlush(scChamber);
        }
        return scChamber;
        
    }
    
    //------------------------ SETTERS --------------------------

    @Autowired
    public void setScChamberRepository(ScChamberRepository scChamberRepository) {
        this.scChamberRepository = scChamberRepository;
    }
}
