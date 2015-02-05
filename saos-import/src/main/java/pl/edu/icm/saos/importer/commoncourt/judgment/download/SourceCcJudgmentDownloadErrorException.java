package pl.edu.icm.saos.importer.commoncourt.judgment.download;

/**
 * @author madryk
 */
public class SourceCcJudgmentDownloadErrorException extends RuntimeException {

    private static final long serialVersionUID = 1221256051720093982L;

    
    //------------------------ CONSTRUCTORS --------------------------
    
    public SourceCcJudgmentDownloadErrorException(String message) {
        super(message);
    }
}
