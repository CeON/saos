package pl.edu.icm.saos.importer.commoncourt;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.google.common.collect.Sets;

/**
 * @author Łukasz Dumiszewski
 */
@Service("ccJudgmentImportUtils")
public class CcJudgmentImportUtils {

    /**
     * Extracts all strings that are between &lt;id&gt; and &lt;/id&gt; tags and then returns
     * list of them.  
     */
    public Set<String> extractIds(String message) {
        Set<String> ids = Sets.newHashSet();
        Pattern p = Pattern.compile("<id>(.*?)<\\/id>");
        Matcher m = p.matcher(message);
        while(m.find()) {
            String mid = m.group();
            System.out.println(mid);
            ids.add(mid.substring(mid.indexOf("<id>") + 4, mid.indexOf("</id>")).trim());
        }
        return ids;
    }
    
    
}
