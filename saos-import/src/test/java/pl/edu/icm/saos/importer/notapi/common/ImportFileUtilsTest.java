package pl.edu.icm.saos.importer.notapi.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.util.Collection;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pl.edu.icm.saos.importer.common.ImportException;


/**
 * @author Łukasz Dumiszewski
 */

public class ImportFileUtilsTest {

    
    private ImportFileUtils importFileUtils = new ImportFileUtils();
    
    private File importDir;
    
    private File importFileJsonGz;
    private File importFileJson;
    private File importFileZip;
    
    private String importFileJsonContent = "{\"bre\":\"sss\", \"arr\":[\"1112\", \"abc\"]}";
    private String importFileJsonGzContent = "{\"bre\":\"sss\", \"arr\":[\"1112\", \"CCC\"]}";
    private String importFileZipContent = "{\"bre\":\"sss\", \"arr\":[\"1112\", \"CCC\"]\n\n}";
    
    
    
    @Before
    public void before() throws IOException {
       
        importDir = Files.createTempDirectory(null).toFile();
        
        createImportFiles(importDir);
       
        importFileUtils.setImportDir(importDir.getAbsolutePath());
       
    }

    
    @After
    public void after() throws IOException {
        
        FileUtils.deleteDirectory(importDir);
    
    }
    
    
    
    @Test
    public void listImportFiles() {
        importFileUtils.setEligibleFileExtensions(new String[]{"json.gz", "json"});
         
        Collection<File> files = importFileUtils.listImportFiles();
        
        assertEquals(2, files.size());
        assertTrue(files.contains(importFileJsonGz));
        assertTrue(files.contains(importFileJson));
    
    }
    
    
    @Test
    public void getReader_JsonContent() throws ImportException, IOException {
        try(Reader reader = importFileUtils.getReader(importFileJson)) {
            String readContent = IOUtils.toString(reader);
            assertEquals(importFileJsonContent, readContent);
        }
    }
    
    
    @Test
    public void getReader_GzContent() throws ImportException, IOException {
        try(Reader reader = importFileUtils.getReader(importFileJsonGz)) {
            String readContent = IOUtils.toString(reader);
            assertEquals(importFileJsonGzContent, readContent);
        }
    }
    
    @Test
    public void getReader_ZipContent() throws ImportException, IOException {
        try(Reader reader = importFileUtils.getReader(importFileZip)) {
            String readContent = IOUtils.toString(reader);
            assertEquals(importFileZipContent, readContent);
        }
    }
    

    //------------------------ PRIVATE --------------------------
    
    
    private void createImportFiles(File relDir) throws IOException {
        importFileJsonGz = new File(relDir, "importFile1.json.gz");
        importFileJson = new File(relDir, "importFile2.json");
        importFileZip = new File(relDir, "importFile3.zip");
        File strangeFile = new File(relDir, "strangeFile.zyx");
           
        importFileJsonGz.createNewFile();
        importFileJson.createNewFile();
        importFileZip.createNewFile();
        strangeFile.createNewFile();
        
        FileUtils.writeStringToFile(importFileJson, importFileJsonContent);
        
        try(Writer writer = new OutputStreamWriter(new GZIPOutputStream(new FileOutputStream(importFileJsonGz)))) {
            writer.write(importFileJsonGzContent);
        }        
        
        try(ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(importFileZip)); Writer writer = new OutputStreamWriter(zipOutputStream)) {
            ZipEntry entry = new ZipEntry("content");
            zipOutputStream.putNextEntry(entry);
            writer.write(importFileZipContent);
            writer.flush();
            zipOutputStream.closeEntry();
        }        
        
        
        
    }

    
}
