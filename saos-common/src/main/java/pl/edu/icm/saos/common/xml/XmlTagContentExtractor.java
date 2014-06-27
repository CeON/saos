package pl.edu.icm.saos.common.xml;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.Lists;

/**
 * @author Łukasz Dumiszewski
 */

public class XmlTagContentExtractor {

    
    /**
     * Extracts the contents of all tags specified by tagName  
     * @return list of trimmed tag contents or empty list if no tag with the given tagName has been found
     */
    public List<String> extractTagContents(String message, String tagName) {
        List<String> ids = Lists.newArrayList();
        Matcher m = createMatcher(message, tagName);
        while(m.find()) {
            String tagWithValue = m.group();
            String value = extractValue(tagWithValue, tagName);
            ids.add(value);
        }
        return ids;
    }
    
    /**
     * Extracts the content of the first tag specified by tagName
     * @return trimmed tag content or null if no tag with the given name has been found 
     */
    public String extractFirstTagContent(String message, String tagName) {
        Matcher m = createMatcher(message, tagName);
        String value = null;
        if (m.find()) {
            String tagWithValue = m.group();
            value = extractValue(tagWithValue, tagName);
        }
        return value;
    }


    //------------------------ PRIVATE --------------------------
    
    private String extractValue(String tagWithValue, String tagName) {
        String startTag = getStartTag(tagName);
        String endTag = getEndTag(tagName);
        String value = tagWithValue.substring(tagWithValue.indexOf(startTag) + startTag.length(), tagWithValue.indexOf(endTag)).trim();
        return value;
    }


    private Matcher createMatcher(String message, String tagName) {
        String regStartTag = getStartTag(tagName);
        String regEndTag = getEndTag(tagName).replace("/", "\\/");
        Pattern p = Pattern.compile(regStartTag+"(.*?)"+regEndTag);
        Matcher m = p.matcher(message);
        return m;
    }


    private String getEndTag(String tagName) {
        String endTag = "</"+tagName+">";
        return endTag;
    }


    private String getStartTag(String tagName) {
        String startTag = "<"+tagName+">";
        return startTag;
    }
    
    
    
    
}
