package pl.edu.icm.saos.importer.notapi.supremecourt.judgment.process;

import java.util.Locale;
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
        oldToNewNameMap.put("izba administracyjna, pracy i ubezpieczeń społecznych", "Izba Pracy, Ubezpieczeń Społecznych i Spraw Publicznych");
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
        
        scChamberName = adjust(scChamberName); 
        
        String newName = oldToNewNameMap.get(scChamberName.toLowerCase(Locale.ROOT));
        
        return newName != null? newName: scChamberName;
    }

    
    /**
     * Says whether scChamberName is subject to change by normalization, see: {@link ScChamberNameNormalizer#normalize(String)}
     */
    public boolean isChangedByNormalization(String scChamberName) {
        return !adjust(scChamberName).equals(normalize(scChamberName));
    }

    
    //------------------------ PRIVATE --------------------------
    
    private String adjust(String scChamberName) {
        return StringUtils.trim(scChamberName);
    }
    
    
    //------------------------ SETTERS --------------------------
    
    public void setOldToNewNameMap(Map<String, String> oldToNewNameMap) {
        this.oldToNewNameMap = oldToNewNameMap;
    }
    
    
}
