package pl.edu.icm.saos.search.indexing;

import java.util.List;

import org.apache.solr.common.SolrInputDocument;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.CourtType;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamber;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamberDivision;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment;
import pl.edu.icm.saos.search.config.model.JudgmentIndexField;

import com.google.common.collect.Lists;

/**
 * Fills {@link SolrInputDocument} with fields from {@link SupremeCourtJudgment}
 * @author madryk
 */
@Service
public class ScJudgmentIndexFieldsFiller extends JudgmentIndexFieldsFiller {

    
    //------------------------ LOGIC --------------------------
    
    @Override
    public boolean isApplicable(CourtType courtType) {
        return courtType == CourtType.SUPREME;
    }
    
    @Override
    public void fillFields(SolrInputDocument doc, JudgmentIndexingData judgmentData) {
        super.fillFields(doc, judgmentData);
        
        SupremeCourtJudgment supremeCourtJudgment = (SupremeCourtJudgment) judgmentData.getJudgment();
        fillScJudmentForm(doc, supremeCourtJudgment);
        fillPersonnelType(doc, supremeCourtJudgment);
        fillChambers(doc, supremeCourtJudgment);
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private void fillScJudmentForm(SolrInputDocument doc, SupremeCourtJudgment judgment) {
    	if (judgment.getScJudgmentForm() != null) {
            fieldAdder.addField(doc, JudgmentIndexField.SC_JUDGMENT_FORM, judgment.getScJudgmentForm().getName());
        }
    }
    
    private void fillPersonnelType(SolrInputDocument doc, SupremeCourtJudgment judgment) {
        if (judgment.getPersonnelType() != null) {
            fieldAdder.addField(doc, JudgmentIndexField.SC_PERSONNEL_TYPE, judgment.getPersonnelType().name());
        }
    }
    
   
    private void fillChambers(SolrInputDocument doc, SupremeCourtJudgment judgment) {
        SupremeCourtChamberDivision division = judgment.getScChamberDivision();
        if (division != null) {
            fieldAdder.addField(doc, JudgmentIndexField.SC_COURT_DIVISION_ID, division.getId());
            fieldAdder.addField(doc, JudgmentIndexField.SC_COURT_DIVISION_NAME, division.getName());
            if (division.getScChamber() != null) {
                fieldAdder.addField(doc, JudgmentIndexField.SC_COURT_DIVISIONS_CHAMBER_ID, division.getScChamber().getId());
                fieldAdder.addField(doc, JudgmentIndexField.SC_COURT_DIVISIONS_CHAMBER_NAME, division.getScChamber().getName());
            }
        }
        
        List<SupremeCourtChamber> chambers = judgment.getScChambers();
        if (chambers != null) {
            chambers.forEach(x -> { 
                List<String> chamberCompositeField = Lists.newArrayList(String.valueOf(x.getId()), x.getName());
                fieldAdder.addCompositeField(doc,
                        JudgmentIndexField.SC_COURT_CHAMBER, chamberCompositeField);
                fieldAdder.addField(doc, JudgmentIndexField.SC_COURT_CHAMBER_ID, x.getId());
                fieldAdder.addField(doc, JudgmentIndexField.SC_COURT_CHAMBER_NAME, x.getName());
            });
        }
    }
}
