package pl.edu.icm.saos.search.indexing;

import org.apache.solr.common.SolrInputDocument;
import org.springframework.stereotype.Component;

import pl.edu.icm.saos.persistence.model.CcJudgmentKeyword;
import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.CommonCourtDivision;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.search.config.model.JudgmentIndexField;

@Component
public class CcJudgmentIndexingProcessor extends JudgmentIndexingProcessorBase implements SpecificJudgmentIndexingProcessor<CommonCourtJudgment> {

    @Override
    public void process(SolrInputDocument doc, CommonCourtJudgment judgment) {
        processKeywords(doc, judgment);
        processCourt(doc, judgment);
    }
    
    protected void processKeywords(SolrInputDocument doc, CommonCourtJudgment item) {
        for (CcJudgmentKeyword keyword : item.getKeywords()) {
            addField(doc, JudgmentIndexField.KEYWORD, keyword.getPhrase());
        }
    }

    protected void processCourt(SolrInputDocument doc, CommonCourtJudgment item) {
        if (item.getCourtDivision() == null) {
            return;
        }
        CommonCourtDivision division = item.getCourtDivision();
        CommonCourt court = division.getCourt();


        addField(doc, JudgmentIndexField.COURT_TYPE, court.getType().name());

        addField(doc, JudgmentIndexField.COURT_ID, court.getCode());
        addField(doc, JudgmentIndexField.COURT_NAME, court.getName());

        addField(doc, JudgmentIndexField.COURT_DIVISION_ID, division.getCode());
        addField(doc, JudgmentIndexField.COURT_DIVISION_NAME, division.getName());
    }
}
