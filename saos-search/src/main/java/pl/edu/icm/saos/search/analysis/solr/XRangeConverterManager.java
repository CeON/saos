package pl.edu.icm.saos.search.analysis.solr;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import pl.edu.icm.saos.search.analysis.request.XRange;

@Service
public class XRangeConverterManager {

    private List<XRangeConverter> xRangeConverters = Lists.newArrayList();
    
    
    //------------------------ LOGIC --------------------------
    
    public XRangeConverter getXRangeConverter(Class<? extends XRange> clazz) {
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
