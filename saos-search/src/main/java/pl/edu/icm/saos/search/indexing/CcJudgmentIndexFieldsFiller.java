package pl.edu.icm.saos.search.indexing;

import org.apache.solr.common.SolrInputDocument;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.CcJudgmentKeyword;
import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.CommonCourtDivision;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.search.config.model.JudgmentIndexField;

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


        fieldAdder.addField(doc, JudgmentIndexField.COURT_TYPE, court.getType().name());

        fieldAdder.addField(doc, JudgmentIndexField.COURT_ID, court.getCode());
        fieldAdder.addField(doc, JudgmentIndexField.COURT_NAME, court.getName());

        fieldAdder.addField(doc, JudgmentIndexField.COURT_DIVISION_ID, division.getCode());
        fieldAdder.addField(doc, JudgmentIndexField.COURT_DIVISION_NAME, division.getName());
    }
}
