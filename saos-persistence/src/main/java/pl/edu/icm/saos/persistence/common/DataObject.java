package pl.edu.icm.saos.persistence.common;


import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.joda.time.DateTime;

/**
 * 
 * @author Łukasz Dumiszewski
 *
 */
@MappedSuperclass
public abstract class DataObject {

    protected int id;
    private int ver;
    private DateTime creationDate = new DateTime(); 
   
    
    
    //------------------------ GETTERS --------------------------
    
    @Version
    //@Column(columnDefinition="integer DEFAULT 0")
    public int getVer() {
      return ver;
    } 
    
    /**
     * timestamp utworzenia rekordu
     */
    public DateTime getCreationDate() {
        return creationDate;
    }
    
    /** every instance needs to provided it with a proper GeneratedValue annotation */
    @Transient
    public abstract int getId();

    
    //------------------------ SETTERS --------------------------
    /** for hibernate */
    @SuppressWarnings("unused")
    private void setVer(int ver) {
         this.ver = ver;
    }

    /** for hibernate */
    @SuppressWarnings("unused")
    private void setCreationDate(DateTime creationDate) {
        this.creationDate = creationDate;
    }

    protected void setId(int id) {
        this.id = id;
    }
    
}
