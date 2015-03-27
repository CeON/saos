package pl.edu.icm.saos.webapp.judgment.detail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import pl.edu.icm.saos.enrichment.apply.JudgmentEnrichmentService;
import pl.edu.icm.saos.persistence.model.Judgment;

/**
 * @author madryk
 */
@Controller
public class JudgmentContentController {

    @Autowired
    private JudgmentEnrichmentService judgmentEnrichmentService;
    
    
    //------------------------ LOGIC --------------------------
    
    @RequestMapping("/judgments/content/{judgmentId}.html")
    public String showJudgmentContent(ModelMap model, @PathVariable("judgmentId") long judgmentId) {
        
        Judgment judgment = judgmentEnrichmentService.findOneAndEnrich(judgmentId);
        
        model.addAttribute("judgmentContent", judgment.getRawTextContent());
        
        return "judgmentContent";
    }
    
}
