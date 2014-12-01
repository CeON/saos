package pl.edu.icm.saos.importer.common.correction;

import java.util.List;

import pl.edu.icm.saos.persistence.common.DataObject;

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
    
    
    public void addCorrection(ImportCorrection importCorrection) {
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
    
    public int getNumberOfCorrections() {
        return importCorrections.size();
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
