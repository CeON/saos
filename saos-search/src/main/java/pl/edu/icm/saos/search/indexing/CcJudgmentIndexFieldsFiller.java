package pl.edu.icm.saos.search.indexing;

import org.apache.solr.common.SolrInputDocument;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.CcJudgmentKeyword;
import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.CommonCourtDivision;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.search.config.model.JudgmentIndexField;
import pl.edu.icm.saos.search.search.model.CourtType;

/**
 * Fills {@link SolrInputDocument} with fields from {@link CommonCourtJudgment} 
 * @author madryk
 */
@Service
public class CcJudgmentIndexFieldsFiller extends JudgmentIndexFieldsFiller<CommonCourtJudgment> {
    
    @Override
    public void fillFields(SolrInputDocument doc, CommonCourtJudgment judgment) {
        super.fillFields(doc, judgment);
        
        fillKeywords(doc, judgment);
        fillCourt(doc, judgment);
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private void fillKeywords(SolrInputDocument doc, CommonCourtJudgment item) {
        for (CcJudgmentKeyword keyword : item.getKeywords()) {
            fieldAdder.addField(doc, JudgmentIndexField.KEYWORD, keyword.getPhrase());
        }
    }

    private void fillCourt(SolrInputDocument doc, CommonCourtJudgment item) {
        if (item.getCourtDivision() == null) {
            return;
        }
        CommonCourtDivision division = item.getCourtDivision();
        CommonCourt court = division.getCourt();


        fieldAdder.addField(doc, JudgmentIndexField.COURT_TYPE, CourtType.COMMON.name());
        fieldAdder.addField(doc, JudgmentIndexField.COURT_TYPE, court.getType().name());

        fieldAdder.addField(doc, JudgmentIndexField.COURT_ID, String.valueOf(court.getId()));
        fieldAdder.addField(doc, JudgmentIndexField.COURT_CODE, court.getCode());
        fieldAdder.addField(doc, JudgmentIndexField.COURT_NAME, court.getName());

        fieldAdder.addField(doc, JudgmentIndexField.COURT_DIVISION_ID, String.valueOf(division.getId()));
        fieldAdder.addField(doc, JudgmentIndexField.COURT_DIVISION_CODE, division.getCode());
        fieldAdder.addField(doc, JudgmentIndexField.COURT_DIVISION_NAME, division.getName());
    }
}
