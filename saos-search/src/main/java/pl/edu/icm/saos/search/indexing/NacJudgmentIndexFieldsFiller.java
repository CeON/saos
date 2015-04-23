package pl.edu.icm.saos.search.indexing;

import org.apache.solr.common.SolrInputDocument;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.CourtType;

/**
 * Fills {@link SolrInputDocument} with fields from
 * {@link pl.edu.icm.saos.persistence.model.NationalAppealChamberJudgment NationalAppealChamberJudgment} 
 * 
 * @author Łukasz Dumiszewski
 */
@Service
public class NacJudgmentIndexFieldsFiller extends JudgmentIndexFieldsFiller {

    
    //------------------------ LOGIC --------------------------
    
    @Override
    public boolean isApplicable(CourtType courtType) {
        return courtType == CourtType.NATIONAL_APPEAL_CHAMBER;
    }

    @Override
    public void fillFields(SolrInputDocument doc, JudgmentIndexingData judgmentData) {
        super.fillFields(doc, judgmentData);
    }
    
}
