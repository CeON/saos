package pl.edu.icm.saos.enrichment.hash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static pl.edu.icm.saos.persistence.common.TestInMemoryEnrichmentTagFactory.createEnrichmentTag;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;

/**
 * @author madryk
 */
public class JudgmentEnrichmentTagsHashCalculatorTest {

    private JudgmentEnrichmentTagsHashCalculator judgmentEnrichmentTagsHashCalculator = new JudgmentEnrichmentTagsHashCalculator();
    
    
    //------------------------ TESTS --------------------------
    
    @Test(expected = NullPointerException.class)
    public void calculateHash_NULL() {
        // execute
        judgmentEnrichmentTagsHashCalculator.calculateHash(null);
    }
    
    @Test
    public void calculateHash_EMPTY_TAGS_LIST() {
        // given
        JudgmentEnrichmentTags judgmentWithTags = new JudgmentEnrichmentTags(3L);
        
        // execute & assert
        assertNull(judgmentEnrichmentTagsHashCalculator.calculateHash(judgmentWithTags));
        
    }
    
    @Test
    public void calculateHash() {
        // given
        JudgmentEnrichmentTags judgmentWithTags = new JudgmentEnrichmentTags(3L);
        
        judgmentWithTags.addEnrichmentTag(createEnrichmentTag(3L, "B_SOME_TAG_VALUE", "{key:'value1'}"));
        judgmentWithTags.addEnrichmentTag(createEnrichmentTag(3L, "A_SOME_TAG_VALUE", "{key:'value2'}"));
        judgmentWithTags.addEnrichmentTag(createEnrichmentTag(4L, "TAG_WITH_REFERENCE", "{judgmentId:3}"));
        judgmentWithTags.addEnrichmentTag(createEnrichmentTag(2L, "TAG_WITH_REFERENCE", "{judgmentId:3}"));
        
        // execute
        String hash = judgmentEnrichmentTagsHashCalculator.calculateHash(judgmentWithTags);
        
        // assert
        String expectedHash = DigestUtils.md5Hex(
                ("2:TAG_WITH_REFERENCE:{'judgmentId':3}"
                + "::3:A_SOME_TAG_VALUE:{'key':'value2'}"
                + "::3:B_SOME_TAG_VALUE:{'key':'value1'}"
                + "::4:TAG_WITH_REFERENCE:{'judgmentId':3}").replace('\'', '"'));
        assertEquals(expectedHash, hash);
    }
    
}
