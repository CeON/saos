package pl.edu.icm.saos.persistence.common;


import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.joda.time.DateTime;

import pl.edu.icm.saos.common.visitor.Visitor;
import pl.edu.icm.saos.common.visitor.VisitorUtils;

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
     * record creation timestamp 
     */
    public DateTime getCreationDate() {
        return creationDate;
    }
    
    /** every instance needs to provide a proper GeneratedValue annotation */
    @Transient
    public abstract int getId();

    
    //------------------------ LOGIC --------------------------
    
    /** 
     * Accept the given visitor. <br/><br/>
     * 1. Selects and invokes the visitor method depending on the current object class type. <br/>
     * 2. Invokes {@link #passVisitorDown(Visitor)} <br/>
     * 
     * */
    public final void accept(Visitor visitor) {
        VisitorUtils.executeVisitorMethod(visitor, this);
        passVisitorDown(visitor);
    }
    
    /**
     * Passes the visitor to referenced objects (invokes their {@link #accept(Visitor)} method).<br/><br/>
     * <b>NOTE:</b> the visitor should be passed only to sub-objects (down the tree) belonging exclusively
     * to the given tree (not shared by other trees) <br/><br/>
     * The default implementation does nothing.
     */
    protected void passVisitorDown(Visitor visitor) {
        
    }
    
    /**
     * Has this object been persisted at least once?
     * 
     */
    @Transient
    public boolean isPersisted() {
        return getId() != 0;
    }
    
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
