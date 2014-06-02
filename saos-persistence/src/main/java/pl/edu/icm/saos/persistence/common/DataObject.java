package pl.edu.icm.saos.persistence.common;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.persistence.Version;

/**
 * 
 * @author Łukasz Dumiszewski
 *
 */
@MappedSuperclass
public abstract class DataObject {

    protected int id;
    private int ver;
    private Date creationDate = new Date(); 
   
    
    
    //------------------------ GETTERS --------------------------
    
    @Version
    //@Column(columnDefinition="integer DEFAULT 0")
    public int getVer() {
      return ver;
    } 
    
    /**
     * timestamp utworzenia rekordu
     */
    @Column(columnDefinition="timestamp")
    public Date getCreationDate() {
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
    private void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void setId(int id) {
        this.id = id;
    }
    
}
