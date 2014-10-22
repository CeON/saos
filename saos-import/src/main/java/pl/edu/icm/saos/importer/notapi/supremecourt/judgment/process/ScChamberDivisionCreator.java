package pl.edu.icm.saos.importer.notapi.supremecourt.judgment.process;

import javax.persistence.EntityNotFoundException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.SupremeCourtChamber;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamberDivision;
import pl.edu.icm.saos.persistence.repository.ScChamberDivisionRepository;
import pl.edu.icm.saos.persistence.repository.ScChamberRepository;

import com.google.common.base.Preconditions;

/**
 * @author Łukasz Dumiszewski
 */
@Service("scChamberDivisionCreator")
public class ScChamberDivisionCreator {
    
    private ScChamberRepository scChamberRepository;
    
    private ScChamberDivisionRepository scChamberDivisionRepository;

    private ScChamberDivisionNameExtractor scChamberDivisionNameExtractor;

    //------------------------ LOGIC --------------------------

    /**
     * Returns {@link SupremeCourtChamberDivision} with {@link SupremeCourtChamberDivision#getFullName()}
     * equal to chamberDivisionFullName.
     * If the division cannot be found, then creates one. The creation process involves finding 
     * a {@link SupremeCourtChamber} with a name extracted from chamberDivisionFullName by
     * {@link ScChamberDivisionNameExtractor#extractChamberName(String)}.
     * 
     * @throws IllegalArgumentException if chamberDivisionFullName is blank
     * @throws EntityNotFoundException if there is no chamber division found during the process of creation
     * of a new {@link SupremeCourtChamberDivision}
     */
    public SupremeCourtChamberDivision getOrCreateScChamberDivision(String chamberDivisionFullName) {

        Preconditions.checkArgument(StringUtils.isNotBlank(chamberDivisionFullName));
        
        SupremeCourtChamberDivision scChamberDivision = scChamberDivisionRepository.findOneByFullName(chamberDivisionFullName);
        
        if (scChamberDivision == null) {
            
            SupremeCourtChamber scChamber = findScChamber(chamberDivisionFullName);
            
            scChamberDivision = createAndSaveScChamberDivision(scChamber, chamberDivisionFullName);
        }
        return scChamberDivision;
    
    }


    
    
    //------------------------ PRIVATE --------------------------
    
    private SupremeCourtChamber findScChamber(String chamberDivisionFullName) {
        String chamberName = scChamberDivisionNameExtractor.extractChamberName(chamberDivisionFullName);
        SupremeCourtChamber scChamber = scChamberRepository.findOneByName(chamberName);
        if (scChamber == null) {
            throw new EntityNotFoundException("division chamber with " + chamberName + " not found");
        }
        return scChamber;
    }
    
    private SupremeCourtChamberDivision createAndSaveScChamberDivision(SupremeCourtChamber scChamber, String chamberDivisionFullName) {
        SupremeCourtChamberDivision scChamberDivision;
        scChamberDivision = new SupremeCourtChamberDivision();
        scChamberDivision.setFullName(chamberDivisionFullName);
        scChamberDivision.setName(scChamberDivisionNameExtractor.extractDivisionName(chamberDivisionFullName));
        scChamberDivision.setScChamber(scChamber);
        scChamberDivisionRepository.saveAndFlush(scChamberDivision);
        return scChamberDivision;
    }

    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setScChamberRepository(ScChamberRepository scChamberRepository) {
        this.scChamberRepository = scChamberRepository;
    }

    @Autowired
    public void setScChamberDivisionRepository(ScChamberDivisionRepository scChamberDivisionRepository) {
        this.scChamberDivisionRepository = scChamberDivisionRepository;
    }

    @Autowired
    public void setScChamberDivisionNameExtractor(ScChamberDivisionNameExtractor scChamberDivisionNameExtractor) {
        this.scChamberDivisionNameExtractor = scChamberDivisionNameExtractor;
    }
    
}
