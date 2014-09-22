package pl.edu.icm.saos.search.config.model;

import java.util.LinkedList;
import java.util.List;

import org.springframework.core.io.Resource;


/**
 * @author madryk
 */
public class IndexConfiguration {

    private String name;

    private String instanceDir;

    private boolean createIndexPropertyFile;

    private boolean persistent = true;

    private List<Resource> configurationFiles = new LinkedList<>();


    //------------------------ LOGIC --------------------------
    
    public void addConfigurationFile(Resource configurationFile) {
        configurationFiles.add(configurationFile);
    }
    
    
    //------------------------ GETTERS --------------------------
    
    public String getName() {
        return name;
    }

    public String getInstanceDir() {
        return instanceDir;
    }

    public boolean isCreateIndexPropertyFile() {
        return createIndexPropertyFile;
    }

    public boolean isPersistent() {
        return persistent;
    }

    public List<Resource> getConfigurationFiles() {
        return configurationFiles;
    }


    //------------------------ SETTERS --------------------------
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setInstanceDir(String instanceDir) {
        this.instanceDir = instanceDir;
    }
    
    public void setPersistent(boolean persistent) {
        this.persistent = persistent;
    }

    public void setCreateIndexPropertyFile(boolean createIndexPropertyFile) {
        this.createIndexPropertyFile = createIndexPropertyFile;
    }
    
    public void setConfigurationFiles(List<Resource> configurationFiles) {
        this.configurationFiles = configurationFiles;
    }
}
