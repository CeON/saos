package pl.edu.icm.saos.search.analysis.solr.result;

import java.util.ArrayList;
import java.util.List;

import pl.edu.icm.saos.search.analysis.request.XSettings;

import com.google.common.base.Preconditions;

/**
 * A manager of {@link FacetValueConverter}s.
 * 
 * @author Łukasz Dumiszewski
 */
public class FacetValueConverterManager {

    
    private List<? extends FacetValueConverter> converters = new ArrayList<>();
    
    
    
    //------------------------ LOGIC --------------------------
    
    /** 
     * Returns a {@link FacetValueConverter} handling the given xsettings (from converters set by {@link #setConverters(List)}).
     * 
     * @throws IllegalArgumentException if no converter handling the given xsettings can be found
     */
    public FacetValueConverter getConverter(XSettings xsettings) {
        
        Preconditions.checkNotNull(xsettings);
        
        for (FacetValueConverter converter :converters) {
            
           if (converter.handles(xsettings)) {
               
               return converter;
               
           }
            
        }
        
        
        
        throw new IllegalArgumentException("no FacetValueConverter can be found for " + xsettings);
        
        
    }



    //------------------------ SETTERS --------------------------
    
    /**
     * The converters that will be used to convert facet values. The order of the list matters, the first matching
     * converter will be used (see: {@link FacetValueConverter#handles(XSettings)})
     */
    public void setConverters(List<? extends FacetValueConverter> converters) {
        this.converters = converters;
    }
    

}
