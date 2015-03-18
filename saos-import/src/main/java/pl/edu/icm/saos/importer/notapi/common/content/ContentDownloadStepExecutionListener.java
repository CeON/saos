package pl.edu.icm.saos.importer.notapi.common.content;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.icm.saos.importer.common.ImportException;
import pl.edu.icm.saos.importer.notapi.common.ImportFileUtils;

/**
 * Listener responsible for copying content files.
 * 
 * @author madryk
 */
public class ContentDownloadStepExecutionListener implements StepExecutionListener {
    
    private ImportFileUtils importFileUtils;
    
    private ContentSourceFileFinder importContentFileFinder;
    
    private String importMetadataDir;
    
    private String importContentDir;
    
    private String downloadedContentDir;
    
    
    //------------------------ LOGIC --------------------------
    
    @Override
    public void beforeStep(StepExecution stepExecution) {
        
        try {
            FileUtils.deleteDirectory(new File(downloadedContentDir));
        } catch (IOException e) {
            throw new ImportException(e);
        }
        
        
        Collection<File> importMetadataFiles = importFileUtils.listImportFiles(importMetadataDir);
        
        
        for (File importMetadataFile : importMetadataFiles) {
            File importContentFile = importContentFileFinder.findContentFile(new File(importContentDir), importMetadataFile);
            
            try {
                FileUtils.copyFileToDirectory(importContentFile, new File(downloadedContentDir));
            } catch (IOException e) {
                throw new ImportException(e);
            }
        }
        
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return ExitStatus.COMPLETED;
    }

    
    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setImportFileUtils(ImportFileUtils importFileUtils) {
        this.importFileUtils = importFileUtils;
    }

    @Autowired
    public void setImportContentFileFinder(ContentSourceFileFinder importContentFileFinder) {
        this.importContentFileFinder = importContentFileFinder;
    }

    public void setImportMetadataDir(String importMetadataDir) {
        this.importMetadataDir = importMetadataDir;
    }

    public void setImportContentDir(String importContentDir) {
        this.importContentDir = importContentDir;
    }

    public void setDownloadedContentDir(String downloadedContentDir) {
        this.downloadedContentDir = downloadedContentDir;
    }

    
}
