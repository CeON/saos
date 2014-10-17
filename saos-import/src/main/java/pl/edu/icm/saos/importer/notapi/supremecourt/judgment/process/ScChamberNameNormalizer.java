package pl.edu.icm.saos.importer.notapi.supremecourt.judgment.process;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.SupremeCourtChamber;

import com.google.common.collect.Maps;

/**
 * @author Łukasz Dumiszewski
 */
@Service("scChamberNameNormalizer")
class ScChamberNameNormalizer {

    private Map<String, String> oldToNewNameMap = Maps.newHashMap();
    
    {
        oldToNewNameMap.put("Izba Administracyjna, Pracy i Ubezpieczeń Społecznych", "Izba Pracy, Ubezpieczeń Społecznych i Spraw Publicznych");
    }
    
    
    /**
     * Normalizes the passed supreme court chamber name (see: {@link SupremeCourtChamber#getName()}) </br>
     * Looks for the given name in keys of {@link #setOldToNewNameMap(Map)} and changes it to a corresponding
     * map value. Returns the unchanged scChamberName if there is no key for it in this map.
     * 
     * @param scChamberName supreme court chamber name to be normalized
     * @return normalized supreme court chamber name
     */
    public String normalize(String scChamberName) {
        
        scChamberName = StringUtils.trim(scChamberName); 
        
        String newName = oldToNewNameMap.get(scChamberName);
        
        return newName != null? newName: scChamberName;
    }


    //------------------------ SETTERS --------------------------
    
    public void setOldToNewNameMap(Map<String, String> oldToNewNameMap) {
        this.oldToNewNameMap = oldToNewNameMap;
    }
    
    
}
