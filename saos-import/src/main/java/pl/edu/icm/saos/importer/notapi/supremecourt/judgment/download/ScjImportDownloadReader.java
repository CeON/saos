package pl.edu.icm.saos.importer.notapi.supremecourt.judgment.download;


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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.importer.common.ImportException;
import pl.edu.icm.saos.importer.notapi.common.ImportFileUtils;
import pl.edu.icm.saos.importer.notapi.common.JsonUtils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

/**
 * Simple supreme court judgment import - download - reader
 *  
 * @author Łukasz Dumiszewski
 */
@Service
public class ScjImportDownloadReader implements ItemStreamReader<String> {

    
    private ImportFileUtils scjImportFileUtils;
    
    private JsonUtils jsonUtils;
    
    private JsonFactory jsonFactory;
    
    
    
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
            fileReader = scjImportFileUtils.getReader(currentFile);
        }
        
        if (jsonParser == null || jsonParser.isClosed()) {
            jsonParser = jsonFactory.createParser(fileReader);
        }
        
        String judgment = jsonUtils.nextNode(jsonParser);
        
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
        importFiles = Lists.newLinkedList(scjImportFileUtils.listImportFiles());
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
    
    @Autowired
    public void setScjImportFileUtils(ImportFileUtils scjImportFileUtils) {
        this.scjImportFileUtils = scjImportFileUtils;
    }

    @Autowired
    public void setJsonUtils(JsonUtils jsonUtils) {
        this.jsonUtils = jsonUtils;
    }

    @Autowired
    public void setJsonFactory(JsonFactory jsonFactory) {
        this.jsonFactory = jsonFactory;
    }
}
