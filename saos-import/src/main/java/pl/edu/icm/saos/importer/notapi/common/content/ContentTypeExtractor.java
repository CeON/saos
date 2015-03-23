package pl.edu.icm.saos.importer.notapi.common.content;

import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;

import pl.edu.icm.saos.persistence.model.JudgmentTextContent.ContentType;

/**
 * Extractor of {@link ContentType}
 * 
 * @author madryk
 */
@Service
public class ContentTypeExtractor {

    private Map<String, ContentType> contentTypeMappings = Maps.newHashMap();
    
    {
        contentTypeMappings.put("pdf", ContentType.PDF);
        contentTypeMappings.put("doc", ContentType.DOC);
    }
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Extracts {@link ContentType} from filename
     */
    public ContentType extractContentType(String filename) {
        String extension = FilenameUtils.getExtension(filename);
        
        if (!contentTypeMappings.containsKey(extension)) {
            throw new IllegalArgumentException("Files with extension " + extension + " are not supported");
        }
        
        return contentTypeMappings.get(extension);
    }
}
