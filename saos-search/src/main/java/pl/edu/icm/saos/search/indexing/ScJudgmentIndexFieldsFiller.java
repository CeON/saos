package pl.edu.icm.saos.search.indexing;

import java.util.List;

import org.apache.solr.common.SolrInputDocument;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.SupremeCourtChamber;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamberDivision;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment;
import pl.edu.icm.saos.search.config.model.JudgmentIndexField;
import pl.edu.icm.saos.search.search.model.CourtType;

import com.google.common.collect.Lists;

/**
 * Fills {@link SolrInputDocument} with fields from {@link SupremeCourtJudgment}
 * @author madryk
 */
@Service
public class ScJudgmentIndexFieldsFiller extends JudgmentIndexFieldsFiller<SupremeCourtJudgment> {

    @Override
    public void fillFields(SolrInputDocument doc, SupremeCourtJudgment judgment) {
        super.fillFields(doc, judgment);
        
        fillPersonnelType(doc, judgment);
        fillCourtType(doc);
        fillChambers(doc, judgment);
    }
    
    private void fillPersonnelType(SolrInputDocument doc, SupremeCourtJudgment judgment) {
        if (judgment.getPersonnelType() != null) {
            fieldAdder.addField(doc, JudgmentIndexField.SC_PERSONNEL_TYPE, judgment.getPersonnelType().name());
        }
    }
    
    private void fillCourtType(SolrInputDocument doc) {
        fieldAdder.addField(doc, JudgmentIndexField.COURT_TYPE, CourtType.SUPREME.name());
    }
    
    private void fillChambers(SolrInputDocument doc, SupremeCourtJudgment judgment) {
        SupremeCourtChamberDivision division = judgment.getSupremeCourtChamberDivision();
        if (division != null) {
            fieldAdder.addField(doc, JudgmentIndexField.SC_DIVISION_ID, String.valueOf(division.getId()));
            fieldAdder.addField(doc, JudgmentIndexField.SC_DIVISION_NAME, null); // TODO fill when it will be available
        }
        
        List<SupremeCourtChamber> chambers = judgment.getSupremeCourtChambers();
        if (chambers != null) {
            chambers.forEach(x -> { 
                fieldAdder.addFieldWithAttributes(doc,
                        JudgmentIndexField.SC_CHAMBER, String.valueOf(x.getId()), Lists.newArrayList()); // TODO fill when it will be available
                fieldAdder.addField(doc, JudgmentIndexField.SC_CHAMBER_ID, String.valueOf(x.getId()));
                fieldAdder.addField(doc, JudgmentIndexField.SC_CHAMBER_NAME, null); // TODO fill when it will be available
            });
        }
    }
}
