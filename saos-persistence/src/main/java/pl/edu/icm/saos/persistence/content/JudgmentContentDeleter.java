package pl.edu.icm.saos.persistence.content;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author madryk
 */
@Service
public class JudgmentContentDeleter {

    private String judgmentContentPath;
    
    
    //------------------------ LOGIC --------------------------
    
    public void deleteContents(List<String> contentPaths) {
        File judgmentContentDir = new File(judgmentContentPath);
        
        for (String contentPath : contentPaths) {
            new File(judgmentContentDir, contentPath).delete();
        }
    }

    
    //------------------------ SETTERS --------------------------
    
    @Value("${judgments.content.dir}")
    public void setJudgmentContentPath(String judgmentContentPath) {
        this.judgmentContentPath = judgmentContentPath;
    }
    
}
