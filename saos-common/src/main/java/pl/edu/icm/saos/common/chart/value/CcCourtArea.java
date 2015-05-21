package pl.edu.icm.saos.common.chart.value;

import java.util.Objects;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Common court simple data, for use as json
 * 
 * @author Łukasz Dumiszewski
 */

public class CcCourtArea implements Comparable<CcCourtArea> {

    private long courtId;
    private String name;
    
    
    
    //------------------------ GETTERS --------------------------
    
    public long getCourtId() {
        return courtId;
    }
    
    public String getName() {
        return name;
    }
    
    
    //------------------------ LOGIC --------------------------

    @Override
    public int compareTo(CcCourtArea o) {
        return this.getName().compareTo(o.getName());
    }
    
    
    
    //------------------------ SETTERS --------------------------
    
    public void setCourtId(long courtId) {
        this.courtId = courtId;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    
    //------------------------ HashCode & Equals --------------------------
    
    
    @Override
    public int hashCode() {
        return Objects.hash(this.courtId, this.name);
    }
    
    
    @Override
    public boolean equals(Object obj) {
        
        if (obj == null) {
           return false;
        }
        
        if (getClass() != obj.getClass()) {
           return false;
        }
        
        final CcCourtArea other = (CcCourtArea) obj;
        
        return Objects.equals(this.courtId, other.courtId)
                && Objects.equals(this.name, other.name);

    }
    
    
    //------------------------ toString --------------------------
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    
    

    
}
