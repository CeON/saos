package pl.edu.icm.saos.importer.commoncourt.judgment.process;

import pl.edu.icm.saos.persistence.model.importer.ImportProcessingSkipReason;

/**
 * @author Łukasz Dumiszewski
 */

public class CcjImportProcessSkippableException extends RuntimeException {

   private static final long serialVersionUID = 1L;

   private ImportProcessingSkipReason skipReason;
   
   public CcjImportProcessSkippableException(String message, ImportProcessingSkipReason skipReason) {
       super(message);
       this.skipReason = skipReason;
   }

   public ImportProcessingSkipReason getSkipReason() {
       return skipReason;
   }
   
   
}
