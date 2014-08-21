package pl.edu.icm.saos.importer.commoncourt.judgment.process;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * @author Łukasz Dumiszewski
 */

public class CcjReasoningExtractorTest {

    
    private CcjReasoningExtractor ccjReasoningExtractor = new CcjReasoningExtractor();
    
    
    @Test
    public void extractReasoning_NOT_FOUND() {
        String reasoning = ccjReasoningExtractor.extractReasoningText("sdsdsd<div>dsdUzasadnieniedcdc");
        assertNull(reasoning);
    }
    
    
    @Test
    public void extractReasoning_FOUND() {
        String reasoningText = "<p>Treść uzadadnienia</p>";
        String extractedReasoningText = ccjReasoningExtractor.extractReasoningText("sdsdsd<div><h2>UzaSadNienie</h2>\n"+reasoningText);
        assertNotNull(extractedReasoningText);
        assertEquals(reasoningText, extractedReasoningText);
    }
    
    @Test
    public void extractReasoning_FOUND_WITH_DIV() {
        String reasoningText = "<p>Treść uzadadnienia</p>";
        String extractedReasoningText = ccjReasoningExtractor.extractReasoningText("sdsdsd<div><h2>UzaSadNienie</h2>"+reasoningText + "  \n </div>");
        assertNotNull(extractedReasoningText);
        assertEquals(reasoningText, extractedReasoningText);
    }
    
    @Test
    public void removeReasoningText_NOT_FOUND() {
        String judgmentText = "sdsdsd<div>dsdUzasadnieniedcdc";
        String calcJudgmentText = ccjReasoningExtractor.removeReasoningText(judgmentText);
        assertEquals(judgmentText, calcJudgmentText);
    }
    
    @Test
    public void removeReasoningText_FOUND() {
        String reasoningText = "<p>Treść uzadadnienia</p>";
        String judgmentText = "sdsdsd";
        String calcJudgmentText = ccjReasoningExtractor.removeReasoningText(judgmentText+"<div><h2>UzaSadNienie</h2>"+reasoningText);
        assertEquals(judgmentText, calcJudgmentText);
    }
    
    @Test
    public void removeReasoningText_FOUND_WITH_DIV() {
        String reasoningText = "<p>Treść uzadadnienia</p>";
        String judgmentText = "sdsdsd";
        String calcJudgmentText = ccjReasoningExtractor.removeReasoningText(judgmentText+"<div><h2>UzaSadNienie</h2>"+reasoningText+"</div>");
        assertEquals(judgmentText, calcJudgmentText);
    }
}
