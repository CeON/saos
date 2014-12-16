package pl.edu.icm.saos.enrichment.upload;

import static pl.edu.icm.saos.enrichment.upload.EnrichmentTagUploadResponseMessages.ERROR_INVALID_DATA;
import static pl.edu.icm.saos.enrichment.upload.EnrichmentTagUploadResponseMessages.ERROR_INVALID_JSON_FORMAT;
import static pl.edu.icm.saos.enrichment.upload.EnrichmentTagUploadResponseMessages.ERROR_IO;
import static pl.edu.icm.saos.enrichment.upload.EnrichmentTagUploadResponseMessages.ERROR_MAX_UPLOAD_SIZE_EXCEEDED;
import static pl.edu.icm.saos.enrichment.upload.EnrichmentTagUploadResponseMessages.ERROR_SAME_TAG_ALREADY_UPLOADED;

import java.io.IOException;
import java.io.InputStream;

import javax.validation.ValidationException;

import org.apache.commons.fileupload.util.LimitedInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

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
     * 
     * @throws ServiceException in case of any service exception like parse error, i/o error etc. 
     */
    public void uploadEnrichmentTags(InputStream inputStream) {
        
        try {    
            try (LimitedInputStream limitedInputStream = limitInputStream(inputStream, enrichmentTagMaxUploadSize)) {
                
                JsonParser jsonParser = jsonFactory.createParser(limitedInputStream);
    
                
                EnrichmentTagItem enrichmentTagItem = null;
                
                do {
                
                    enrichmentTagItem = enrichmentTagItemReader.nextEnrichmentTagItem(jsonParser);
                    
                    if (enrichmentTagItem == null) {
                        
                        return;
                        
                    }
                
                    enrichmentTagItemUploadProcessor.processEnrichmentTagItem(enrichmentTagItem);
                    
                    
                } while (true);
            }
                  
        }
        
        catch (JsonParseException e) {
            throw new ServiceException(ERROR_INVALID_JSON_FORMAT, e.getMessage(), e);
        } 
        
        catch (ValidationException e) {
            throw new ServiceException(ERROR_INVALID_DATA, e.getMessage(), e);
        }
        
        catch (IOException e) {
            throw new ServiceException(ERROR_IO, e.getMessage(), e);
        }
         
        catch (DataIntegrityViolationException e) {
            throw new ServiceException(ERROR_SAME_TAG_ALREADY_UPLOADED, e.getMessage(), e);
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
