package pl.edu.icm.saos.importer.commoncourt.judgment.download;

/**
 * Exception thrown when application could not download common court
 * judgments id list from external repository.
 * 
 * @author madryk
 */
public class SourceCcJudgmentIdsDownloadException extends RuntimeException {

    private static final long serialVersionUID = 2939777306136925756L;


    //------------------------ CONSTRUCTORS --------------------------
    
    public SourceCcJudgmentIdsDownloadException(String message) {
        super(message);
    }
}
