package pl.edu.icm.saos.search.indexing;

import org.apache.solr.common.SolrInputDocument;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.CommonCourtDivision;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.CourtType;
import pl.edu.icm.saos.persistence.model.JudgmentKeyword;
import pl.edu.icm.saos.search.config.model.JudgmentIndexField;

/**
 * Fills {@link SolrInputDocument} with fields from {@link CommonCourtJudgment} 
 * @author madryk
 */
@Service
public class CcJudgmentIndexFieldsFiller extends JudgmentIndexFieldsFiller {
    
    
    //------------------------ LOGIC --------------------------
    
    @Override
    public boolean isApplicable(CourtType courtType) {
        return courtType == CourtType.COMMON;
    }
    
    @Override
    public void fillFields(SolrInputDocument doc, JudgmentIndexingData judgmentData) {
        super.fillFields(doc, judgmentData);
        
        CommonCourtJudgment commonCourtJudgment = (CommonCourtJudgment) judgmentData.getJudgment();
        fillKeywords(doc, commonCourtJudgment);
        fillCourt(doc, commonCourtJudgment);
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private void fillKeywords(SolrInputDocument doc, CommonCourtJudgment judgment) {
        for (JudgmentKeyword keyword : judgment.getKeywords()) {
            fieldAdder.addField(doc, JudgmentIndexField.KEYWORD, keyword.getPhrase());
        }
    }

    private void fillCourt(SolrInputDocument doc, CommonCourtJudgment item) {
        if (item.getCourtDivision() == null) {
            return;
        }
        CommonCourtDivision division = item.getCourtDivision();
        CommonCourt court = division.getCourt();


        fieldAdder.addField(doc, JudgmentIndexField.CC_COURT_TYPE, court.getType().name());

        fieldAdder.addField(doc, JudgmentIndexField.CC_COURT_ID, court.getId());
        fieldAdder.addField(doc, JudgmentIndexField.CC_COURT_CODE, court.getCode());
        fieldAdder.addField(doc, JudgmentIndexField.CC_COURT_NAME, court.getName());

        fieldAdder.addField(doc, JudgmentIndexField.CC_COURT_DIVISION_ID, division.getId());
        fieldAdder.addField(doc, JudgmentIndexField.CC_COURT_DIVISION_CODE, division.getCode());
        fieldAdder.addField(doc, JudgmentIndexField.CC_COURT_DIVISION_NAME, division.getName());

        fieldAdder.addField(doc, JudgmentIndexField.CC_APPEAL_COURT_ID, court.getAppealCourt().getId());
        fieldAdder.addField(doc, JudgmentIndexField.CC_APPEAL_COURT_NAME, court.getAppealCourt().getName());
        
        if (court.isRegionalCourt() || court.isDistrictCourt()) {
            fieldAdder.addField(doc, JudgmentIndexField.CC_REGIONAL_COURT_ID, court.getRegionalCourt().getId());
            fieldAdder.addField(doc, JudgmentIndexField.CC_REGIONAL_COURT_NAME, court.getRegionalCourt().getName());
        }
        
        if (court.isDistrictCourt()) {
            fieldAdder.addField(doc, JudgmentIndexField.CC_DISTRICT_COURT_ID, court.getId());
            fieldAdder.addField(doc, JudgmentIndexField.CC_DISTRICT_COURT_NAME, court.getName());
        }

    }

}
