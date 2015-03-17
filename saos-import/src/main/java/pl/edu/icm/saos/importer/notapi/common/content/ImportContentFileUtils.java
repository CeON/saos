package pl.edu.icm.saos.importer.notapi.common.content;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.importer.common.ImportException;
import pl.edu.icm.saos.persistence.model.importer.RawSourceJudgment;

/**
 * Utilities for operations on content files used in download process.
 * 
 * @author madryk
 */
@Service
public class ImportContentFileUtils {

    private String contentFilesBaseDir;
    
    private ContentSourceFileFinder contentSourceFileFinder;
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Deletes all content files that are associated with {@link RawSourceJudgment} class.
     */
    public void deleteAllContentFiles(Class<? extends RawSourceJudgment> rawSourceJudgmentClass) {
        File contentDir = getContentFilesDirForClass(rawSourceJudgmentClass);
        
        try {
            FileUtils.deleteDirectory(contentDir);
        } catch (IOException e) {
            throw new ImportException(e);
        }
    }
    
    /**
     * Returns file that contains content for passed metadata file.
     */
    public File locateContentFile(File correspondingMetadataFile, Class<? extends RawSourceJudgment> rawSourceJudgmentClass) {
        
        File contentDir = getContentFilesDirForClass(rawSourceJudgmentClass);
        
        return contentSourceFileFinder.findContentFile(contentDir, correspondingMetadataFile);
    }
    
    /**
     * Copies content file into {@link #setContentFilesBaseDir(String)}
     */
    public void copyContentFile(File sourceFile, Class<? extends RawSourceJudgment> rawSourceJudgmentClass) {
        File contentDir = getContentFilesDirForClass(rawSourceJudgmentClass);
        
        try {
            FileUtils.copyFileToDirectory(sourceFile, contentDir);
        } catch (IOException e) {
            throw new ImportException(e);
        }
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private File getContentFilesDirForClass(Class<?> rawSourceJugmentClass) {
        return new File(contentFilesBaseDir, rawSourceJugmentClass.getSimpleName());
    }

    
    //------------------------ SETTERS --------------------------

    @Value("${import.judgments.download.dir}")
    public void setContentFilesBaseDir(String contentFilesBaseDir) {
        this.contentFilesBaseDir = contentFilesBaseDir;
    }

    @Autowired
    public void setContentSourceFileFinder(ContentSourceFileFinder importContentFileFinder) {
        this.contentSourceFileFinder = importContentFileFinder;
    }
    
    
}
