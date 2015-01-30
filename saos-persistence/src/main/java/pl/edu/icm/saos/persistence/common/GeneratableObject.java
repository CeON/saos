package pl.edu.icm.saos.persistence.common;

import javax.persistence.Transient;

/**
 * An abstract implementation of {@link Generatable} 
 * 
 * @author Łukasz Dumiszewski
 */

public abstract class GeneratableObject implements Generatable {

    
    private boolean generated = false;
    
    
    
    //------------------------ LOGIC --------------------------
    
    @Override
    @Transient
    public boolean isGenerated() {
        return this.generated;
        
    }

    @Override
    public void markGenerated() {
        this.generated = true;
        
    }

}
