package pl.edu.icm.saos.importer.notapi.common.content;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pl.edu.icm.saos.persistence.model.JudgmentTextContent.ContentType;

/**
 * @author madryk
 */
public class ContentTypeMapperTest {

    private ContentTypeExtractor contentTypeMapper = new ContentTypeExtractor();
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void extractContentType() {
        // execute
        ContentType contentType = contentTypeMapper.extractContentType("filename.pdf");
        
        // assert
        assertEquals(ContentType.PDF, contentType);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void extractContentType_NOT_SUPPORTED() {
        // execute
        contentTypeMapper.extractContentType("filename.zip");
    }
}
