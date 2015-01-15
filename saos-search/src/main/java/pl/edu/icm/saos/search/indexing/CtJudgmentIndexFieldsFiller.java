package pl.edu.icm.saos.search.indexing;

import org.apache.solr.common.SolrInputDocument;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.CourtType;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.search.config.model.JudgmentIndexField;

/**
 * Fills {@link SolrInputDocument} with fields from
 * {@link pl.edu.icm.saos.persistence.model.ConstitutionalTribunalJudgment ConstitutionalTribunalJudgment} 
 * 
 * @author madryk
 */
@Service
public class CtJudgmentIndexFieldsFiller extends JudgmentIndexFieldsFiller {

    
    //------------------------ LOGIC --------------------------
    
    @Override
    public boolean isApplicable(CourtType courtType) {
        return courtType == CourtType.CONSTITUTIONAL_TRIBUNAL;
    }

    @Override
    public void fillFields(SolrInputDocument doc, Judgment judgment) {
        super.fillFields(doc, judgment);
        
        fillCourt(doc);
    }
    
    //------------------------ PRIVATE --------------------------
    
    private void fillCourt(SolrInputDocument doc) {
        fieldAdder.addField(doc, JudgmentIndexField.COURT_TYPE, CourtType.CONSTITUTIONAL_TRIBUNAL.name());
    }
}
