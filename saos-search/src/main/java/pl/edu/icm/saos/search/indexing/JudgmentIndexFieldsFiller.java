package pl.edu.icm.saos.search.indexing;

import java.util.List;
import java.util.UUID;

import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.icm.saos.persistence.model.CourtCase;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judge.JudgeRole;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.JudgmentReferencedRegulation;
import pl.edu.icm.saos.search.config.model.JudgmentIndexField;

import com.google.common.collect.Lists;

public abstract class JudgmentIndexFieldsFiller {

    protected SolrFieldAdder<JudgmentIndexField> fieldAdder;
    
    
    //------------------------ LOGIC --------------------------
    
    public abstract boolean isApplicable(Class<? extends Judgment> judgmentClass);
    
    
    public void fillFields(SolrInputDocument doc, Judgment judgment) {
        fillIds(doc, judgment);
        fillCourtCases(doc, judgment);
        fillJudges(doc, judgment);
        fillLegalBases(doc, judgment);
        fillReferencedRegulations(doc, judgment);
        fillJudgmentDate(doc, judgment);
        fillJudgmentType(doc, judgment);
        fillContent(doc, judgment);
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private void fillIds(SolrInputDocument doc, Judgment item) {
        fieldAdder.addField(doc, JudgmentIndexField.ID, UUID.randomUUID().toString());
        fieldAdder.addField(doc, JudgmentIndexField.DATABASE_ID, item.getId());
    }
    
    private void fillCourtCases(SolrInputDocument doc, Judgment item) {
        for (CourtCase courtCase : item.getCourtCases()) {
            fieldAdder.addField(doc, JudgmentIndexField.CASE_NUMBER, courtCase.getCaseNumber());
        }
        
    }
    
    private void fillJudges(SolrInputDocument doc, Judgment item) {
        for (Judge judge : item.getJudges()) {
            List<JudgeRole> roles = judge.getSpecialRoles();
            
            List<String> judgeCompositeField = Lists.newArrayList(judge.getName());
            roles.stream().map(JudgeRole::name).forEach(judgeCompositeField::add);
            fieldAdder.addCompositeField(doc, JudgmentIndexField.JUDGE, judgeCompositeField);
            fieldAdder.addField(doc, JudgmentIndexField.JUDGE_NAME, judge.getName());
            if (roles.isEmpty()) {
                fieldAdder.addField(doc, JudgmentIndexField.JUDGE_WITH_ROLE, "NO_ROLE", judge.getName());
            } else {
                roles.forEach(role -> fieldAdder.addField(doc, JudgmentIndexField.JUDGE_WITH_ROLE, role.name(), judge.getName()));
            }
        }
    }
    
    private void fillLegalBases(SolrInputDocument doc, Judgment item) {
        for (String legalBase : item.getLegalBases()) {
            fieldAdder.addField(doc, JudgmentIndexField.LEGAL_BASE, legalBase);
        }
    }
    
    private void fillReferencedRegulations(SolrInputDocument doc, Judgment item) {
        for (JudgmentReferencedRegulation referencedRegulation : item.getReferencedRegulations()) {
            fieldAdder.addField(doc, JudgmentIndexField.REFERENCED_REGULATION, referencedRegulation.getRawText());
        }
    }
    
    private void fillJudgmentDate(SolrInputDocument doc, Judgment item) {
        fieldAdder.addDateField(doc, JudgmentIndexField.JUDGMENT_DATE, item.getJudgmentDate());
    }
    
    private void fillJudgmentType(SolrInputDocument doc, Judgment item) {
        if (item.getJudgmentType() != null) {
            fieldAdder.addField(doc, JudgmentIndexField.JUDGMENT_TYPE, item.getJudgmentType().name());
        }
    }
    
    private void fillContent(SolrInputDocument doc, Judgment item) {
        fieldAdder.addField(doc, JudgmentIndexField.CONTENT, item.getTextContent());
    }

    
    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setFieldAdder(SolrFieldAdder<JudgmentIndexField> fieldAdder) {
        this.fieldAdder = fieldAdder;
    }
}
