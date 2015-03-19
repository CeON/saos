package pl.edu.icm.saos.importer.notapi.common;

import java.io.File;

/**
 * Class that ties json judgment with file.
 * 
 * @author madryk
 */
public class JsonJudgmentItem {

    private String json;
    
    private File sourceMetadataFile;

    
    //------------------------ CONSTRUCTORS --------------------------
    
    public JsonJudgmentItem(String json, File fileSource) {
        this.json = json;
        this.sourceMetadataFile = fileSource;
    }
    
    
    //------------------------ GETTERS --------------------------
    
    /**
     * Returns json judgment
     */
    public String getJson() {
        return json;
    }

    /**
     * Returns reference to json file in which judgment was defined
     */
    public File getSourceMetadataFile() {
        return sourceMetadataFile;
    }
    
    
}
