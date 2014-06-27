package pl.edu.icm.saos.common.xml;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Łukasz Dumiszewski
 */

public class XmlTagContentExtractorTest {

    
    private XmlTagContentExtractor extractor = new XmlTagContentExtractor();
    
    
    @Test
    public void extractTagContents_OneTag() {
        String idValue = "sds232ewdxsc";
        String message = "<id>" + idValue + "</id>";
        assertExtractedIds(extractor.extractTagContents(message, "id"), idValue);
    }
    
    @Test
    public void extractTagContents_ManyTags() {
        String idValue1 = "sds232ewdxsc";
        String idValue2 = "sds232ewdxsc";
        String idValue3 = "sds232ewdxsc2";
        String message = "<id>" + idValue1 + "</id>\n";
        message += "<id>" + idValue2 + "</id>";
        message += "<id>" + idValue3 + "</id>";
        
        assertExtractedIds(extractor.extractTagContents(message, "id"), idValue1, idValue2, idValue3);
    }
    
    @Test
    public void extractTagContents_ManyTags_Trim() {
        String idValue1 = "sds232ewdxsc";
        String idValue2 = "sds232ewdxsc";
        String idValue3 = "sds232ewdxsc2";
        String message = "fdfdffdgvgdrvv<id>" + idValue1 + "</id>\n";
        message += "<id>" + idValue2 + "   </id>";
        message += "<id>" + idValue3 + "   </id>d sd sd s d sd s ds d";
        
        assertExtractedIds(extractor.extractTagContents(message, "id"), idValue1, idValue2, idValue3);
    }
    
    @Test
    public void extractFirstTagContent_ManyTags_Trim() {
        String idValue1 = "sds232ewdx22sc";
        String idValue2 = "sds232ewdxsc";
        String idValue3 = "sds232ewdxsc2";
        String message = "fdfdffdgvgdrvv<ids>" + idValue1 + "</ids>\n";
        message += "<ids>" + idValue2 + "   </ids>";
        message += "<ids>" + idValue3 + "   </ids>d sd sd s d sd s ds d";
        
        assertEquals(idValue1, extractor.extractFirstTagContent(message, "ids"));
    }
    
    
    @Test
    public void extractFirstTagContent_NoTagFound() {
        String idValue1 = "sds232ewdx22sc";
        String idValue2 = "sds232ewdxsc";
        String idValue3 = "sds232ewdxsc2";
        String message = "fdfdffdgvgdrvv<id>" + idValue1 + "</id>\n";
        message += "<id>" + idValue2 + "   </id>";
        message += "<id>" + idValue3 + "   </id>d sd sd s d sd s ds d";
        
        assertNull(extractor.extractFirstTagContent(message, "nonexistenttag"));
    }
    
    
    private void assertExtractedIds(List<String> extractedIds, String... expectedIds) {
        Assert.assertEquals(expectedIds.length, extractedIds.size());
        for (String expectedId : expectedIds) {
            Assert.assertTrue(extractedIds.contains(expectedId));
        }
    }
    
    
}
