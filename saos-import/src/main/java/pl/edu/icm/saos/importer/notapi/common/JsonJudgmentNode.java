package pl.edu.icm.saos.importer.notapi.common;

import java.io.File;

/**
 * Class that ties json judgment with file.
 * 
 * @author madryk
 */
public class JsonJudgmentNode {

    private String json;
    
    private File fileSource;

    
    //------------------------ CONSTRUCTORS --------------------------
    
    public JsonJudgmentNode(String json, File fileSource) {
        this.json = json;
        this.fileSource = fileSource;
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
    public File getFileSource() {
        return fileSource;
    }
    
    
}
