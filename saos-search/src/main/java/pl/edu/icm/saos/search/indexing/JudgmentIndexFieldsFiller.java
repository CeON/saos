package pl.edu.icm.saos.search.indexing;

import java.util.List;
import java.util.UUID;

import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.icm.saos.persistence.model.CourtCase;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.JudgmentReferencedRegulation;
import pl.edu.icm.saos.persistence.model.Judge.JudgeRole;
import pl.edu.icm.saos.search.config.model.JudgmentIndexField;

public class JudgmentIndexFieldsFiller<J extends Judgment> {

    protected SolrFieldAdder<JudgmentIndexField> fieldAdder;
    
    
    public void fillFields(SolrInputDocument doc, J judgment) {
        fillIds(doc, judgment);
        fillCourtCases(doc, judgment);
        fillJudges(doc, judgment);
        fillLegalBases(doc, judgment);
        fillReferencedRegulations(doc, judgment);
        fillJudgmentDate(doc, judgment);
        fillJudgmentType(doc, judgment);
        fillContent(doc, judgment);
    }
    
    protected void fillIds(SolrInputDocument doc, Judgment item) {
        fieldAdder.addField(doc, JudgmentIndexField.ID, UUID.randomUUID().toString());
        fieldAdder.addField(doc, JudgmentIndexField.DATABASE_ID, String.valueOf(item.getId()));
    }
    
    protected void fillCourtCases(SolrInputDocument doc, Judgment item) {
        for (CourtCase courtCase : item.getCourtCases()) {
            fieldAdder.addField(doc, JudgmentIndexField.CASE_NUMBER, courtCase.getCaseNumber());
        }
        
    }
    
    protected void fillJudges(SolrInputDocument doc, Judgment item) {
        for (Judge judge : item.getJudges()) {
            List<JudgeRole> roles = judge.getSpecialRoles();
            
            if (roles.isEmpty()) {
                fieldAdder.addField(doc, JudgmentIndexField.JUDGE, judge.getName());
            } else {
                for (JudgeRole role : roles) {
                    fieldAdder.addField(doc, JudgmentIndexField.JUDGE_WITH_ROLE, role.name(), judge.getName());
                }
            }
        }
    }
    
    protected void fillLegalBases(SolrInputDocument doc, Judgment item) {
        for (String legalBase : item.getLegalBases()) {
            fieldAdder.addField(doc, JudgmentIndexField.LEGAL_BASE, legalBase);
        }
    }
    
    protected void fillReferencedRegulations(SolrInputDocument doc, Judgment item) {
        for (JudgmentReferencedRegulation referencedRegulation : item.getReferencedRegulations()) {
            fieldAdder.addField(doc, JudgmentIndexField.REFERENCED_REGULATION, referencedRegulation.getRawText());
        }
    }
    
    protected void fillJudgmentDate(SolrInputDocument doc, Judgment item) {
        fieldAdder.addDateField(doc, JudgmentIndexField.JUDGMENT_DATE, item.getJudgmentDate());
    }
    
    protected void fillJudgmentType(SolrInputDocument doc, Judgment item) {
        if (item.getJudgmentType() != null) {
            fieldAdder.addField(doc, JudgmentIndexField.JUDGMENT_TYPE, item.getJudgmentType().name());
        }
    }
    
    protected void fillContent(SolrInputDocument doc, Judgment item) {
        fieldAdder.addField(doc, JudgmentIndexField.CONTENT, item.getTextContent());
    }

    
    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setFieldAdder(SolrFieldAdder<JudgmentIndexField> fieldAdder) {
        this.fieldAdder = fieldAdder;
    }
}
