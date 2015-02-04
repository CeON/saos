package pl.edu.icm.saos.persistence.common;

import javax.persistence.Transient;

import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTag;

/**
 * An extension of {@link DataObject} that implements {@link Generatable}.
 * <br/>
 * Objects of this class are data objects that can be part of the model imported from an external source or
 * ones generated from {@link EnrichmentTag}s. In the last case they are not persisted.
 * 
 * @author Łukasz Dumiszewski
 */

public abstract class GeneratableDataObject extends DataObject implements Generatable {

    
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
