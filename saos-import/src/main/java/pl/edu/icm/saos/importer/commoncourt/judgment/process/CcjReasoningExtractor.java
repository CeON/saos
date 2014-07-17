package pl.edu.icm.saos.importer.commoncourt.judgment.process;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

/**
 * @author Łukasz Dumiszewski
 */
@Service("ccjReasoningExtractor")
class CcjReasoningExtractor {

    
    private String reasoningStartPhrase = "<h2>UZASADNIENIE</h2>";
    
    
    /** 
     * Extracts reasoning from the judgment text. If the fragment with reasoning cannot be found then
     * null is returned.
     * 
     * */
    public String extractReasoningText(String judgmentText) {
        Pattern p = Pattern.compile("(?i)"+reasoningStartPhrase+".*");
        Matcher m = p.matcher(judgmentText);
        if (m.find()) {
            String reasoning = m.group();
            
            reasoning = reasoning.replaceAll("(?i)"+reasoningStartPhrase, "");
            if (!reasoning.startsWith("<div>") && reasoning.matches("(</div>\\s*)$")) {
                reasoning = reasoning.replaceAll("(</div>\\s*)$", "");
            }
            
            return reasoning.trim();
        }
        return null;
    }
    
    /**
     * Removes reasoning from the given judgmentText
     */
    public String removeReasoningText(String judgmentText) {
        return judgmentText.replaceAll("(?i)(<div>)*\\s*"+reasoningStartPhrase+".*" , "");
    }
    
}
