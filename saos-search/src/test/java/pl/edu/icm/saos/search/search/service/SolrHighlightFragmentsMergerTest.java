package pl.edu.icm.saos.search.search.service;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import pl.edu.icm.saos.search.StringListMap;
import pl.edu.icm.saos.search.config.model.JudgmentIndexField;

import com.google.common.collect.Maps;

/**
 * @author madryk
 */
public class SolrHighlightFragmentsMergerTest {

    private SolrHighlightFragmentsMerger<JudgmentIndexField> highlightFragmentsMerger = new SolrHighlightFragmentsMerger<JudgmentIndexField>();
    
    @Test
    public void merge() {
        Map<String, List<String>> highlightFragments = StringListMap.of(new String[][] {
                { "content", "some text some text", "other text content", "yet another text" },
        });
        
        String actualMerged = highlightFragmentsMerger.merge(highlightFragments, JudgmentIndexField.CONTENT);
        
        assertEquals("some text some text ... other text content ... yet another text", actualMerged);
    }
    
    @Test
    public void merge_EMPTY() {
        Map<String, List<String>> highlightFragments = Maps.newHashMap();
        
        String actualMerged = highlightFragmentsMerger.merge(highlightFragments, JudgmentIndexField.CONTENT);
        
        assertEquals(StringUtils.EMPTY, actualMerged);
    }
}
