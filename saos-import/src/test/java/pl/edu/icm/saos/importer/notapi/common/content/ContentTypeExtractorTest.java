package pl.edu.icm.saos.importer.notapi.common.content;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pl.edu.icm.saos.persistence.model.JudgmentTextContent.ContentType;

/**
 * @author madryk
 */
public class ContentTypeExtractorTest {

    private ContentTypeExtractor contentTypeExtractor = new ContentTypeExtractor();
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void extractContentType() {
        // execute
        ContentType contentType = contentTypeExtractor.extractContentType("filename.pdf");
        
        // assert
        assertEquals(ContentType.PDF, contentType);
    }
    
    @Test
    public void extractContentType_LOWER_CASE() {
        // execute
        ContentType contentType = contentTypeExtractor.extractContentType("filename.DOC");
        
        // assert
        assertEquals(ContentType.DOC, contentType);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void extractContentType_NOT_SUPPORTED() {
        // execute
        contentTypeExtractor.extractContentType("filename.zip");
    }
}
