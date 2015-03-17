package pl.edu.icm.saos.importer.notapi.common.content;

import java.io.File;
import java.util.Collection;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.icm.saos.importer.notapi.common.ImportFileUtils;
import pl.edu.icm.saos.persistence.model.importer.RawSourceJudgment;

import com.google.common.collect.Lists;

/**
 * Listener responsible for copying content files.
 * 
 * @author madryk
 */
public class ContentDownloadStepExecutionListener implements StepExecutionListener {
    
    private ImportFileUtils importFileUtils;
    
    private ImportContentFileUtils importContentFileUtils;
    
    private ContentSourceFileFinder importContentFileFinder;
    
    private String importDir;
    
    private String importContentDir;
    
    private Class<? extends RawSourceJudgment> rawJudgmentClass;
    
    
    //------------------------ LOGIC --------------------------
    
    @Override
    public void beforeStep(StepExecution stepExecution) {
        
        importContentFileUtils.deleteAllContentFiles(rawJudgmentClass);
        
        File contentSourceDir = new File(importContentDir);
        
        Collection<File> importFiles = importFileUtils.listImportFiles(importDir);
        Collection<File> contentFiles = Lists.newArrayList();
        
        
        for (File importFile : importFiles) {
            File contentFile = importContentFileFinder.findContentFile(contentSourceDir, importFile);
            contentFiles.add(contentFile);
        }
        
        
        for (File contentFile : contentFiles) {
            importContentFileUtils.copyContentFile(contentFile, rawJudgmentClass);
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
    public void setImportContentFileUtils(ImportContentFileUtils importContentFileUtils) {
        this.importContentFileUtils = importContentFileUtils;
    }

    @Autowired
    public void setImportContentFileFinder(ContentSourceFileFinder importContentFileFinder) {
        this.importContentFileFinder = importContentFileFinder;
    }

    public void setImportDir(String importDir) {
        this.importDir = importDir;
    }

    public void setImportContentDir(String importContentDir) {
        this.importContentDir = importContentDir;
    }

    public void setRawJudgmentClass(Class<? extends RawSourceJudgment> rawJudgmentClass) {
        this.rawJudgmentClass = rawJudgmentClass;
    }

    
}
