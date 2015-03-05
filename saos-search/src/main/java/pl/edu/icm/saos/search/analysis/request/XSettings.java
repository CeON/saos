package pl.edu.icm.saos.search.analysis.request;

/**
 * @author Łukasz Dumiszewski
 */

public class XSettings {

    private XField field;
    private XRange range;
    
    
    
    //------------------------ GETTERS --------------------------
    
    public XField getField() {
        return field;
    }
    
    public XRange getRange() {
        return range;
    }
    
    
    //------------------------ SETTERS --------------------------
    
    public void setField(XField field) {
        this.field = field;
    }
    
    public void setRange(XRange range) {
        this.range = range;
    }
    
}
