package pl.edu.icm.saos.importer.commoncourt;

import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Łukasz Dumiszewski
 */

public class CcJudgmentImportUtilsTest {

    
    private CcJudgmentImportUtils ccJudgmentImportUtils = new CcJudgmentImportUtils();
    
    
    @Test
    public void extractIds_OneId() {
        String idValue = "sds232ewdxsc";
        String message = "<id>" + idValue + "</id>";
        assertExtractedIds(ccJudgmentImportUtils.extractIds(message), idValue);
    }
    
    @Test
    public void extractIds_ManyIds() {
        String idValue1 = "sds232ewdxsc";
        String idValue2 = "sds232ewdxsc";
        String idValue3 = "sds232ewdxsc2";
        String message = "<id>" + idValue1 + "</id>\n";
        message += "<id>" + idValue2 + "</id>";
        message += "<id>" + idValue3 + "</id>";
        
        assertExtractedIds(ccJudgmentImportUtils.extractIds(message), idValue1, idValue3);
    }
    
    @Test
    public void extractIds_ManyIds_Trim() {
        String idValue1 = "sds232ewdxsc";
        String idValue2 = "sds232ewdxsc";
        String idValue3 = "sds232ewdxsc2";
        String message = "fdfdffdgvgdrvv<id>" + idValue1 + "</id>\n";
        message += "<id>" + idValue2 + "   </id>";
        message += "<id>" + idValue3 + "   </id>d sd sd s d sd s ds d";
        
        assertExtractedIds(ccJudgmentImportUtils.extractIds(message), idValue1, idValue3);
    }
    
    
    private void assertExtractedIds(Set<String> extractedIds, String... expectedIds) {
        Assert.assertEquals(expectedIds.length, extractedIds.size());
        for (String expectedId : expectedIds) {
            Assert.assertTrue(extractedIds.contains(expectedId));
        }
    }
    
    
}
