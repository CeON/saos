package pl.edu.icm.saos.importer.notapi.supremecourt.judgment.process;

import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.SupremeCourtChamber;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamberDivision;

import com.google.common.base.Preconditions;

/**
 * @author Łukasz Dumiszewski
 */
@Service("scChamberDivisionNameExtractor")
class ScChamberDivisionNameExtractor {

    
    /**
     * Extracts {@link SupremeCourtChamberDivision#getName()} from fullName <br/>
     * @throws IllegalArgumentException if fullName does not contain '\\S+Wydział.*\\S+'
     * 
     */
    public String extractDivisionName(String fullName) {
        checkName(fullName);
        return "Wydział " + fullName.split("Wydział")[1].trim();
    }


     /**
     * Extracts {@link SupremeCourtChamber#getName()} from the given fullName 
     * ({@link SupremeCourtChamberDivision#getFullName()}) <br/>
     * @throws IllegalArgumentException if fullName does not contain '\\S+Wydział.*\\S+'
     */
    public String extractChamberName(String fullName) {
        checkName(fullName);
        return fullName.split("Wydział")[0].trim();
    }

    
    
    //------------------------ PRIVATE --------------------------
   
    private void checkName(String fullName) {
        Preconditions.checkArgument(fullName.matches("\\S+.*Wydział.*\\S+"));
    }
    

   
}
