package pl.edu.icm.saos.importer.notapi.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collection;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipInputStream;

import org.apache.commons.compress.compressors.gzip.GzipUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import pl.edu.icm.saos.importer.common.ImportException;

/**
 * Utility methods related to import files 
 * 
 * @author Łukasz Dumiszewski
 */
public class ImportFileUtils {

    
    private String importMainDir;
    
    private String importRelDir;
    
    private String[] eligibleFileExtensions;
    
    private String fileCharset = "UTF-8";
    
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Returns collection of import files. <br/>
     * The files are looked for in the directory specified by {@link #setImportMainDir(String)} and {@link #setImportRelDir(String)}.
     * <br/>
     * The method will only return files with extensions defined by {@link #setEligibleFileExtensions(String[])}
     * <br/>
     * Never returns null.
     */
    public Collection<File> listImportFiles() {
        String importDir = StringUtils.appendIfMissing(importMainDir, "/") + importRelDir;
        return FileUtils.listFiles(new File(importDir), eligibleFileExtensions, true);
    }

    /**
     * Returns a reader for the given file.<br/>
     * In case of files with extensions: gz and zip the reader is
     * built upon {@link GZIPInputStream} and {@link ZipInputStream} respectively. <br/>
     * The reader gets/reads only first zip entry in case of zip files<br/>
     * The reader uses the encoding defined by {@link #setFileCharset(String)}
     * 
     * @throws ImportException in case of any exception
     * 
     */
    public Reader getReader(File file) throws ImportException {
        try {
            InputStream inputStream = new FileInputStream(file);
            if (GzipUtils.isCompressedFilename(file.getName())) {
                inputStream = new GZIPInputStream(inputStream);
            } else if (FilenameUtils.getExtension(file.getName()).equals("zip")) {
                inputStream = new ZipInputStream(inputStream);
                ((ZipInputStream)inputStream).getNextEntry();
                
            }
            
            return new InputStreamReader(inputStream, fileCharset);
            
        } catch (Exception e) {
            throw new ImportException(e);
        }
    }

        
    
    //------------------------ SETTERS --------------------------
    
    public void setImportMainDir(String importMainDir) {
        this.importMainDir = importMainDir;
    }

    public void setImportRelDir(String importRelDir) {
        this.importRelDir = importRelDir;
    }

    /**
     * @param eligibleFileExtensions File extensions that will be taken into consideration by
     * {@link #listImportFiles()}
     */
    public void setEligibleFileExtensions(String[] eligibleFileExtensions) {
        this.eligibleFileExtensions = eligibleFileExtensions;
    }


    /**
     * 
     * @param fileCharset fileCharset that will be used for reading import files
     */
    public void setFileCharset(String fileCharset) {
        this.fileCharset = fileCharset;
    }
    
}
