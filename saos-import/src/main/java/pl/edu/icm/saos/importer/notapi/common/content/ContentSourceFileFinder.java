package pl.edu.icm.saos.importer.notapi.common.content;

import java.io.File;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.NameFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

import pl.edu.icm.saos.importer.common.ImportException;

import com.google.common.collect.Lists;

/**
 * Finder of content files.
 * 
 * @author madryk
 */
public class ContentSourceFileFinder {
    
    private String[] eligibleFileExtensions;

    
    //------------------------ LOGIC --------------------------
    
    /**
     * Finds content file in directory (or subdirectory) that 'corresponds' to
     * passed metadata file.
     * 
     * In our case 'corresponding' files means that files have the same name base
     * but extensions may differ.
     * Extension of returned file must be one of the {@link #setEligibleFileExtensions(String[])}
     */
    public File findContentFile(File contentDir, File correspondingMetadataFile) {
        String filenameBase = extractFilenameBase(correspondingMetadataFile.getName());
        
        List<String> possibleFileNames = buildPossibleFilenames(filenameBase);
        
        Collection<File> foundContentFiles = FileUtils.listFiles(contentDir, new NameFileFilter(possibleFileNames), TrueFileFilter.INSTANCE);
        
        if (foundContentFiles.size() == 0) {
            throw new ImportException("No content file was found for " + correspondingMetadataFile + " in " + contentDir.getPath());
        }
        if (foundContentFiles.size() > 1) {
            throw new ImportException("More than one file found as content file candidate of " + correspondingMetadataFile);
        }
        
        return foundContentFiles.iterator().next();
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private List<String> buildPossibleFilenames(String filenameBase) {
        List<String> possibleFileNames = Lists.newArrayList();
        for (String extension : eligibleFileExtensions) {
            possibleFileNames.add(filenameBase + "." + extension);
        }
        
        return possibleFileNames;
    }
    
    private String extractFilenameBase(String fileName) {
        return fileName.substring(0, fileName.indexOf("."));
    }


    //------------------------ SETTERS --------------------------
    
    public void setEligibleFileExtensions(String[] eligibleFileExtensions) {
        this.eligibleFileExtensions = eligibleFileExtensions;
    }
}
