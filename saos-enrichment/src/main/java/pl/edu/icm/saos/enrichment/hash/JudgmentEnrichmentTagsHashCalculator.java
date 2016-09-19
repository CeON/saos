package pl.edu.icm.saos.enrichment.hash;

import java.util.Comparator;
import java.util.stream.Collectors;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;

import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTag;

/**
 * Calculator of judgment enrichment tags hashes
 *  
 * @author madryk
 */
@Service
public class JudgmentEnrichmentTagsHashCalculator {

    private final static String TAG_VALUES_SEPARATOR = ":";
    
    private final static String TAG_SEPARATOR = "::";
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Returns hash of enrichment tags for given judgment based on md5 algorithm.
     * If judgment has no enrichment tags then {@literal null} is returned.
     */
    public String calculateHash(JudgmentEnrichmentTags judgmentEnrichmentTags) {
        
        Preconditions.checkNotNull(judgmentEnrichmentTags);
        
        if (judgmentEnrichmentTags.getEnrichmentTags().size() == 0) {
            return null;
        }
        
        String value = judgmentEnrichmentTags.getEnrichmentTags().stream()
                .sorted(Comparator.comparing(EnrichmentTag::getJudgmentId).thenComparing(EnrichmentTag::getTagType))
                .map(tag -> tag.getJudgmentId() + TAG_VALUES_SEPARATOR + tag.getTagType() + TAG_VALUES_SEPARATOR + tag.getValue())
                .collect(Collectors.joining(TAG_SEPARATOR));
        
        String hash = DigestUtils.md5Hex(value);
        
        return hash;
        
    }
}
