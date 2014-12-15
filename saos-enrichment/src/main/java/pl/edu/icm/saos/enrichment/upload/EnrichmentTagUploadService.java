package pl.edu.icm.saos.enrichment.upload;

import static pl.edu.icm.saos.enrichment.upload.EnrichmentTagUploadResponseMessages.ERROR_MAX_UPLOAD_SIZE_EXCEEDED;

import java.io.IOException;
import java.io.InputStream;

import javax.validation.ValidationException;

import org.apache.commons.fileupload.util.LimitedInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.common.json.JsonItemParseException;
import pl.edu.icm.saos.common.service.ServiceException;
import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTag;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;

@Service("enrichmentTagUploadService")
public class EnrichmentTagUploadService {

    
    
    private JsonFactory jsonFactory;
    
    private EnrichmentTagItemReader enrichmentTagItemReader;
    
    private EnrichmentTagItemUploadProcessor enrichmentTagItemUploadProcessor;
    
    private long enrichmentTagMaxUploadSize;
    
    
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Reads {@link EnrichmentTagItem}s from the passed inputStream, converts them into {@link EnrichmentTag}s
     * and saves into the SAOS datasource.
     */
    public void uploadEnrichmentTags(InputStream inputStream) throws IOException, JsonParseException, JsonItemParseException, ValidationException, ServiceException {
        
            
        try (LimitedInputStream limitedInputStream = limitInputStream(inputStream, enrichmentTagMaxUploadSize)) {
            
            JsonParser jsonParser = jsonFactory.createParser(limitedInputStream);

            
            EnrichmentTagItem enrichmentTagItem = null;
            
            do {
            
                enrichmentTagItem = enrichmentTagItemReader.nextEnrichmentTagItem(jsonParser);
                
                if (enrichmentTagItem == null) {
                    
                    return;
                    
                }
            
                enrichmentTagItemUploadProcessor.processEnrichmentTagItem(enrichmentTagItem);
                
                
            } while (enrichmentTagItem != null);
            
            
        }
    
    }

   
        
    

    
    //------------------------ PRIVATE --------------------------
    
    private LimitedInputStream limitInputStream(InputStream thesisContentInputStream, long maxSize) {
    
        LimitedInputStream inputStream = new LimitedInputStream(thesisContentInputStream, maxSize) {
        
            @Override
            protected void raiseError(long pSizeMax, long pCount) throws IOException {
                throw new ServiceException(ERROR_MAX_UPLOAD_SIZE_EXCEEDED, ""+pSizeMax);
            }
                  
        };
    
        return inputStream;
    }
    
    
    //------------------------ SETTERS --------------------------

    
    @Value("${enrichment.enrichmentTagMaxUploadSizeInBytes}")
    public void setEnrichmentTagMaxUploadSize(long enrichmentTagMaxUploadSize) {
    
        this.enrichmentTagMaxUploadSize = enrichmentTagMaxUploadSize;
    
    }

    @Autowired
    public void setJsonFactory(JsonFactory jsonFactory) {
        this.jsonFactory = jsonFactory;
    }

    @Autowired
    public void setEnrichmentTagItemReader(EnrichmentTagItemReader enrichmentTagItemReader) {
        this.enrichmentTagItemReader = enrichmentTagItemReader;
    }

    @Autowired
    public void setEnrichmentTagItemUploadProcessor(EnrichmentTagItemUploadProcessor enrichmentTagItemUploadProcessor) {
        this.enrichmentTagItemUploadProcessor = enrichmentTagItemUploadProcessor;
    }

    
    
    
}
