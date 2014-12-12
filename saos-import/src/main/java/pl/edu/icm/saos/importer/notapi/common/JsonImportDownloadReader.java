package pl.edu.icm.saos.importer.notapi.common;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.Queue;

import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import pl.edu.icm.saos.importer.common.ImportException;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

/**
 * Json files based import - download - reader
 *  
 * @author Łukasz Dumiszewski
 */
public class JsonImportDownloadReader implements ItemStreamReader<String> {
    
    private ImportFileUtils importFileUtils;
    
    private JsonUtilService jsonUtilService;
    
    private JsonFactory jsonFactory;
    
    private String importDir;
    
    
    
    private JsonParser jsonParser;
    
    private File currentFile;
    
    private Queue<File> importFiles;
    
    private Reader fileReader;
    
    
    
    @Override
    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        
        if (currentFile == null) {
            return null;
        }
        
        if (fileReader == null) {
            Preconditions.checkState(jsonParser == null || jsonParser.isClosed()); 
            fileReader = importFileUtils.getReader(currentFile);
        }
        
        if (jsonParser == null || jsonParser.isClosed()) {
            jsonParser = jsonFactory.createParser(fileReader);
        }
        
        String judgment = jsonUtilService.nextNode(jsonParser);
        
        if (judgment != null) {
            return StringUtils.trim(judgment);
        }
        
        closeJsonParser();
        closeFileReader();
        currentFile = importFiles.poll();
        
        return read();
    }


    
    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        importFiles = Lists.newLinkedList(importFileUtils.listImportFiles(importDir));
        currentFile = importFiles.poll();
        fileReader = null;
        jsonParser = null;
    }

    
    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        
    }

    
    @Override
    public void close() throws ItemStreamException {
        try {
            closeJsonParser();
            closeFileReader();
        } catch (IOException e) {
            throw new ImportException(e);
        }
        
    }

    

    
    //------------------------ PRIVATE --------------------------
    
    private void closeFileReader() throws IOException {
        if (fileReader != null) {
            fileReader.close();
            fileReader = null;
        }
    }



    private void closeJsonParser() throws IOException {
        if (jsonParser != null) {
            jsonParser.close();
            jsonParser = null;
        }
    }
    
    

    //------------------------ SETTERS --------------------------
    
    public void setImportDir(String importDir) {
        this.importDir = importDir;
    }
    
    public void setImportFileUtils(ImportFileUtils importFileUtils) {
        this.importFileUtils = importFileUtils;
    }

    public void setJsonUtilService(JsonUtilService jsonUtilService) {
        this.jsonUtilService = jsonUtilService;
    }

    public void setJsonFactory(JsonFactory jsonFactory) {
        this.jsonFactory = jsonFactory;
    }

}
