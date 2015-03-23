package pl.edu.icm.saos.webapp.analysis.request;

import pl.edu.icm.saos.search.analysis.request.XField;

/**
 * Settings of the x axis
 * 
 * @author Łukasz Dumiszewski
 */

public class UixSettings {
    
    private XField field;
    
    private UixRange range;
    
    
    //------------------------ GETTERS --------------------------
    
    
    
    /**
     * The selected field of the x axis 
     */
    public XField getField() {
        return field;
    }

    /**
     * Range of the x field
     */
    public UixRange getRange() {
        return range;
    }


    
   
    //------------------------ SETTERS --------------------------
    
 

    public void setField(XField field) {
        this.field = field;
    }


   public void setRange(UixRange range) {
        this.range = range;
    }
    
    
 
    
   
    
    
   
}
