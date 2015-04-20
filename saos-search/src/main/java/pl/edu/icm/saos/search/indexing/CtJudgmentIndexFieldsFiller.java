package pl.edu.icm.saos.search.indexing;

import org.apache.solr.common.SolrInputDocument;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.ConstitutionalTribunalJudgment;
import pl.edu.icm.saos.persistence.model.ConstitutionalTribunalJudgmentDissentingOpinion;
import pl.edu.icm.saos.persistence.model.CourtType;
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
    public void fillFields(SolrInputDocument doc, JudgmentIndexingData judgmentData) {
        super.fillFields(doc, judgmentData);
        
        ConstitutionalTribunalJudgment ctJudgment = (ConstitutionalTribunalJudgment) judgmentData.getJudgment();
        fillDissentingOpinions(doc, ctJudgment);
    }
    
    //------------------------ PRIVATE --------------------------
    
    private void fillDissentingOpinions(SolrInputDocument doc, ConstitutionalTribunalJudgment judgment) {
        
        for (ConstitutionalTribunalJudgmentDissentingOpinion dissentingOpinion : judgment.getDissentingOpinions()) {
            fieldAdder.addField(doc, JudgmentIndexField.CT_DISSENTING_OPINION, dissentingOpinion.getTextContent());
            
            for (String dissentingOpinionAuthor : dissentingOpinion.getAuthors()) {
                fieldAdder.addField(doc, JudgmentIndexField.CT_DISSENTING_OPINION, dissentingOpinionAuthor);
                fieldAdder.addField(doc, JudgmentIndexField.CT_DISSENTING_OPINION_AUTHOR, dissentingOpinionAuthor);
            }
        }
        
    }
   
}
