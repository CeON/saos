package pl.edu.icm.saos.importer.common.correction;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;

import pl.edu.icm.saos.persistence.common.DataObject;
import pl.edu.icm.saos.persistence.correction.model.CorrectedProperty;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

/**
 * Contains corrections of a single judgment made during the import process 
 * 
 * @author Łukasz Dumiszewski
 */

public class ImportCorrectionList {

    
    public List<ImportCorrection> importCorrections = Lists.newArrayList();

    
    
    
    //------------------------ LOGIC --------------------------
    
    
    public void addCorrection(DataObject correctedObject, CorrectedProperty correctedProperty, String oldValue, String newValue) {
        
        addCorrection(new ImportCorrection(correctedObject, correctedProperty, oldValue, newValue));
    
    }
    
    public void addCorrection(ImportCorrection importCorrection) {
        Preconditions.checkArgument(! hasImportCorrection(importCorrection.getCorrectedObject(), importCorrection.getCorrectedProperty()));
        
        importCorrections.add(importCorrection);
     }
    
    /**
     * Finds all corrected objects in importCorrections ({@link ImportCorrection#getCorrectedObject()})
     * that are oldObject. Changes them to newObject. 
     */
    public void changeCorrectedObject(DataObject oldObject, DataObject newObject) {
        Preconditions.checkNotNull(oldObject);
        Preconditions.checkNotNull(newObject);
        Preconditions.checkArgument(oldObject.getClass().equals(newObject.getClass()));
        
        for (ImportCorrection importCorrection : importCorrections) {
            if (importCorrection.getCorrectedObject() == oldObject) {
                importCorrection.setCorrectedObject(newObject);
            }
        }
    }
    
    /**
     * Returns {@link ImportCorrection} that with the given correctedObject and correctedProperty. Returns null
     * if no correction that meets the specified criteria can be found in {@link #getImportCorrections()}<br/>
     * The passed correctedObject can be null - in such a case the method tries to find import corrections with null
     * correctedObject and correctedProperty that is equal to the passed one.
     * 
     */
    public ImportCorrection getImportCorrection(DataObject correctedObject, CorrectedProperty correctedProperty) {
        List<ImportCorrection> corrections = getImportCorrections().stream().filter(c->(c.getCorrectedObject()==correctedObject || (c.getCorrectedObject() == null && correctedObject==null)) && c.getCorrectedProperty()==correctedProperty).collect(Collectors.toList());
        
        if (CollectionUtils.isEmpty(corrections)) {
            return null;
        }
    
        return corrections.get(0);

    }
    
    public int getNumberOfCorrections() {
        return importCorrections.size();
    }
    
    
    /**
     * Uses {@link #getImportCorrection(DataObject, CorrectedProperty)}
     */
    public boolean hasImportCorrection(DataObject correctedObject, CorrectedProperty correctedProperty) {
        return getImportCorrection(correctedObject, correctedProperty) != null;
    }
    
    public boolean hasImportCorrection(ImportCorrection importCorrection) {
        return getImportCorrections().contains(importCorrection);
    }
    
    
    
    //------------------------ GETTERS --------------------------
    
    public List<ImportCorrection> getImportCorrections() {
        return ImmutableList.copyOf(importCorrections);
    }

    
    
    //------------------------ HashCode & Equals --------------------------
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime
                * result
                + ((importCorrections == null) ? 0 : importCorrections
                        .hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ImportCorrectionList other = (ImportCorrectionList) obj;
        if (importCorrections == null) {
            if (other.importCorrections != null)
                return false;
        } else if (!importCorrections.equals(other.importCorrections))
            return false;
        return true;
    }

    
    //------------------------ toString --------------------------
    
    @Override
    public String toString() {
        return "ImportCorrectionList [importCorrections=" + importCorrections
                + "]";
    }
    
    
    
    
}
