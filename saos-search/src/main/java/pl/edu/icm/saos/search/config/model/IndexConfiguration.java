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
    
    private List<Resource> configurationFiles = new LinkedList<>();


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstanceDir() {
        return instanceDir;
    }

    public void setInstanceDir(String instanceDir) {
        this.instanceDir = instanceDir;
    }
    
    public boolean isCreateIndexPropertyFile() {
        return createIndexPropertyFile;
    }

    public void setCreateIndexPropertyFile(boolean createIndexPropertyFile) {
        this.createIndexPropertyFile = createIndexPropertyFile;
    }

    public void addConfigurationFile(Resource configurationFile) {
        configurationFiles.add(configurationFile);
    }

    public List<Resource> getConfigurationFiles() {
        return configurationFiles;
    }

    public void setConfigurationFiles(List<Resource> configurationFiles) {
        this.configurationFiles = configurationFiles;
    }

}
