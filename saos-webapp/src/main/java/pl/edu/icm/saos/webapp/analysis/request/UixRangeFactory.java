package pl.edu.icm.saos.webapp.analysis.request;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.search.analysis.request.XField;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

/**
 * 
 * Factory of {@link UixRange} objects
 * 
 * @author Łukasz Dumiszewski
 */
@Service("uixRangeFactory")
public class UixRangeFactory {

    private List<UixRangeCreator> uixRangeCreators = Lists.newArrayList();
    
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Creates a default {@link UixRange} object for the given xfield 
     */
    public UixRange createUixRange(XField xfield) {
        
        Preconditions.checkNotNull(xfield);
        
        List<UixRangeCreator> handlingCreators = uixRangeCreators.stream().filter(c->c.handles(xfield)).collect(Collectors.toList());
        
        if (CollectionUtils.isEmpty(handlingCreators)) {
            throw new IllegalArgumentException("no uixRangeCreator for xfield = " + xfield + " can be found");
        }
        
        return handlingCreators.get(0).createRange();
    }

    
    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setUixRangeCreators(List<UixRangeCreator> uixRangeCreators) {
        this.uixRangeCreators = uixRangeCreators;
    }
}
