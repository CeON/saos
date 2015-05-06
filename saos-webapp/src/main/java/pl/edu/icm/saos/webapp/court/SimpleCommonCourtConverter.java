package pl.edu.icm.saos.webapp.court;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.CommonCourt;

import com.google.common.collect.Lists;

/**
 * 
 * A converter of {@link CommonCourt} objects into {@link SimpleCommonCourt}s
 * 
 * @author Łukasz Dumiszewski
 */
@Service("simpleCommonCourtConverter")
public class SimpleCommonCourtConverter {

    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * 
     * Converts the passed commonCourts into a list of {@link SimpleCommonCourt}s
     * 
     */
    public List<SimpleCommonCourt> convertCommonCourts(List<CommonCourt> commonCourts) {
        
        if (CollectionUtils.isEmpty(commonCourts)) {
            return Lists.newArrayList();
        }
        
        return commonCourts.stream()
            .map(commonCourt -> convertFromCommonCourt(commonCourt))
            .collect(Collectors.toList());
    }
     
    
    //------------------------ PRIVATE --------------------------
    
    private SimpleCommonCourt convertFromCommonCourt(CommonCourt commonCourt) {
        SimpleCommonCourt simpleCommonCourt = new SimpleCommonCourt();
        simpleCommonCourt.setId(commonCourt.getId());
        simpleCommonCourt.setName(commonCourt.getName());
        simpleCommonCourt.setType(commonCourt.getType());
        return simpleCommonCourt;
    }
}
