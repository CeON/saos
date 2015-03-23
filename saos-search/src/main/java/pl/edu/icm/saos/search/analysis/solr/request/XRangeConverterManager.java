package pl.edu.icm.saos.search.analysis.solr.request;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import pl.edu.icm.saos.search.analysis.request.XRange;

/**
 * Manager of converting {@link XRange} to solr facet request
 * 
 * @author madryk
 */
@Service
public class XRangeConverterManager {

    private List<XRangeConverter> xRangeConverters = Lists.newArrayList();
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Returns {@link XRangeConverter} that is applicable to {@link XRange}
     * with class provided as argument 
     * 
     * @throws IllegalArgumentException if no suitable {@link XRangeConverter} was found
     */
    public XRangeConverter getXRangeConverter(Class<? extends XRange> clazz) {
        
        Preconditions.checkNotNull(clazz);
        
        for (XRangeConverter xRangeConverter : xRangeConverters) {
            if (xRangeConverter.isApplicable(clazz)) {
                return xRangeConverter;
            }
        }
        
        throw new IllegalArgumentException("No XRangeConverter applicable for " + clazz + " found.");
    }
    
    
    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setxRangeConverters(List<XRangeConverter> xRangeConverters) {
        this.xRangeConverters = xRangeConverters;
    }
}
