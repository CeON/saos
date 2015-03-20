package pl.edu.icm.saos.webapp.analysis.request.converter;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.search.analysis.request.XRange;
import pl.edu.icm.saos.webapp.analysis.request.UixRange;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

/**
 * 
 * A manager of {@link UixRangeConverter}s.
 * 
 * @author Łukasz Dumiszewski
 */
@Service("uixRangeConverterManager")
public class UixRangeConverterManager {

    
    private List<UixRangeConverter> uixRangeConverters = Lists.newArrayList();
    
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Converts {@link UixRange} into {@link XRange} by using a proper {@link UixRangeConverter}
     * from converters specified by {@link #setUixRangeConverters(List)}.
     * @throws IllegalArgumentException if no {@link UixRangeConverter} handling the given uixRange can be found 
     */
    public XRange convertUixRange(UixRange uixRange) {
        
        Preconditions.checkNotNull(uixRange);
        
        List<UixRangeConverter> handlingConverters = uixRangeConverters.stream().filter(c->c.handles(uixRange.getClass())).collect(Collectors.toList());
        
        if (CollectionUtils.isEmpty(handlingConverters)) {
            throw new IllegalArgumentException("no uixRangeConverter for uixRange class = " + uixRange.getClass().getName() + " can be found");
        }
        
        return handlingConverters.get(0).convert(uixRange);
    }


    
    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setUixRangeConverters(List<UixRangeConverter> uixRangeConverters) {
        this.uixRangeConverters = uixRangeConverters;
    }
    
    
    
}
