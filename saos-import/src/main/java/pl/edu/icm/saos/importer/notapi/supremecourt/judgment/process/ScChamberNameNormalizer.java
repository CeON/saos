package pl.edu.icm.saos.importer.notapi.supremecourt.judgment.process;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;

/**
 * @author Łukasz Dumiszewski
 */
@Service("scChamberNameNormalizer")
class ScChamberNameNormalizer {

    private static Map<String, String> oldToNewNameMap = Maps.newHashMap();
    
    {
        oldToNewNameMap.put("Izba Administracyjna, Pracy i Ubezpieczeń Społecznych", "Izba Pracy, Ubezpieczeń Społecznych i Spraw Publicznych");
    }
    
    
    public String normalize(String scChamberName) {
        
        scChamberName = StringUtils.trim(scChamberName); 
        
        String newName = oldToNewNameMap.get(scChamberName);
        
        return newName != null? newName: scChamberName;
    }
    
    
}
