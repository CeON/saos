package pl.edu.icm.saos.enrichment.upload;

import static pl.edu.icm.saos.enrichment.upload.EnrichmentTagUploadResponseMessages.ERROR_INVALID_DATA;
import static pl.edu.icm.saos.enrichment.upload.EnrichmentTagUploadResponseMessages.ERROR_INVALID_JSON_FORMAT;
import static pl.edu.icm.saos.enrichment.upload.EnrichmentTagUploadResponseMessages.ERROR_IO;
import static pl.edu.icm.saos.enrichment.upload.EnrichmentTagUploadResponseMessages.ERROR_MAX_UPLOAD_SIZE_EXCEEDED;
import static pl.edu.icm.saos.enrichment.upload.EnrichmentTagUploadResponseMessages.ERROR_SAME_TAG_ALREADY_UPLOADED;

import java.io.IOException;
import java.io.InputStream;

import javax.transaction.Transactional;
import javax.validation.ValidationException;

import org.apache.commons.fileupload.util.LimitedInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.common.json.JsonObjectIterator;
import pl.edu.icm.saos.common.service.ServiceException;
import pl.edu.icm.saos.persistence.enrichment.UploadEnrichmentTagRepository;
import pl.edu.icm.saos.persistence.enrichment.model.UploadEnrichmentTag;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

@Service("enrichmentTagUploadService")
public class EnrichmentTagUploadService {

    
    
    private JsonFactory jsonFactory;
    
    private JsonObjectIterator jsonObjectIterator;
    
    private EnrichmentTagItemUploadProcessor enrichmentTagItemUploadProcessor;
    
    private UploadEnrichmentTagRepository uploadEnrichmentTagRepository;
    
    
    private long enrichmentTagMaxUploadSize;
    
    
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * <b>Truncates</b> the upload tag table ({@link UploadEnrichmentTag}), then
     * reads {@link EnrichmentTagItem}s from the passed inputStream, converts them into {@link UploadEnrichmentTag}s
     * and saves into the SAOS datasource ({@link UploadEnrichmentTag}).
     * 
     * @throws ServiceException in case of any service exception like parse error, i/o error etc. 
     */
    @Transactional
    public void uploadEnrichmentTags(InputStream inputStream) {
        
    	uploadEnrichmentTagRepository.truncate();
    	
        try {    
            try (LimitedInputStream limitedInputStream = limitInputStream(inputStream, enrichmentTagMaxUploadSize)) {
                
                JsonParser jsonParser = jsonFactory.createParser(limitedInputStream);
    
                
                EnrichmentTagItem enrichmentTagItem = null;
                
                if (!JsonToken.START_ARRAY.equals(jsonParser.nextToken())) {
                    throw new ServiceException(ERROR_INVALID_JSON_FORMAT, "the content is not a json array");
                }
                
                do {
                
                    enrichmentTagItem = jsonObjectIterator.nextJsonObject(jsonParser, EnrichmentTagItem.class);
                    
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
    public void setEnrichmentTagItemUploadProcessor(EnrichmentTagItemUploadProcessor enrichmentTagItemUploadProcessor) {
        this.enrichmentTagItemUploadProcessor = enrichmentTagItemUploadProcessor;
    }

    @Autowired
    public void setJsonObjectIterator(JsonObjectIterator jsonObjectIterator) {
        this.jsonObjectIterator = jsonObjectIterator;
    }

    @Autowired
    public void setUploadEnrichmentTagRepository(UploadEnrichmentTagRepository uploadEnrichmentTagRepository) {
		this.uploadEnrichmentTagRepository = uploadEnrichmentTagRepository;
	}


    
    
}
