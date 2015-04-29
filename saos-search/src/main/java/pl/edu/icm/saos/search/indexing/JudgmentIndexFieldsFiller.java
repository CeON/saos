package pl.edu.icm.saos.search.indexing;

import java.util.List;

import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.icm.saos.persistence.model.CourtCase;
import pl.edu.icm.saos.persistence.model.CourtType;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judge.JudgeRole;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.JudgmentReferencedRegulation;
import pl.edu.icm.saos.persistence.model.LawJournalEntry;
import pl.edu.icm.saos.persistence.model.ReferencedCourtCase;
import pl.edu.icm.saos.search.config.model.JudgmentIndexField;

import com.google.common.collect.Lists;

/**
 * Fills {@link SolrInputDocument} with fields from {@link Judgment}.
 * @author madryk
 */
public abstract class JudgmentIndexFieldsFiller {

    protected SolrFieldAdder<JudgmentIndexField> fieldAdder;
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Checks if this {@link JudgmentIndexFieldsFiller} can be used with
     * {@link Judgment judgments} whose {@link CourtType} is provided as argument. 
     * @param judgmentClass
     * @return
     */
    public abstract boolean isApplicable(CourtType courtType);
    
    /**
     * Fills {@link SolrInputDocument} with fields from {@link Judgment}.
     * Use it only for applicable {@link Judgment judgments} ({@link #isApplicable(CourtType)} returns true)
     * @param doc
     * @param judgment
     */
    public void fillFields(SolrInputDocument doc, JudgmentIndexingData judgmentData) {
        Judgment judgment = judgmentData.getJudgment();
        
        fillIds(doc, judgment);
        fillSourceCode(doc, judgment);
        fillCourtType(doc, judgment);
        fillCourtCases(doc, judgment);
        fillJudges(doc, judgment);
        fillLegalBases(doc, judgment);
        fillReferencedRegulations(doc, judgment);
        fillReferencedCourtCases(doc, judgment);
        fillJudgmentDate(doc, judgment);
        fillJudgmentType(doc, judgment);
        fillMeansOfAppeal(doc, judgment);
        fillJudgmentResult(doc, judgment);
        fillContent(doc, judgment);
        fillMaxMoneyAmount(doc, judgment);
        
        fillReferencingCount(doc, judgmentData);
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private void fillIds(SolrInputDocument doc, Judgment judgment) {
        fieldAdder.addField(doc, JudgmentIndexField.DATABASE_ID, judgment.getId());
    }
    
    private void fillSourceCode(SolrInputDocument doc, Judgment judgment) {
        if (judgment.getSourceInfo() != null && judgment.getSourceInfo().getSourceCode() != null) {
            fieldAdder.addField(doc, JudgmentIndexField.SOURCE_CODE, judgment.getSourceInfo().getSourceCode().name());
        }
    }
    
    private void fillCourtType(SolrInputDocument doc, Judgment judgment) {
        fieldAdder.addField(doc, JudgmentIndexField.COURT_TYPE, judgment.getCourtType().name());
    }
    
    private void fillCourtCases(SolrInputDocument doc, Judgment judgment) {
        for (CourtCase courtCase : judgment.getCourtCases()) {
            fieldAdder.addField(doc, JudgmentIndexField.CASE_NUMBER, courtCase.getCaseNumber());
        }
        
    }
    
    private void fillJudges(SolrInputDocument doc, Judgment judgment) {
        for (Judge judge : judgment.getJudges()) {
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
    
    private void fillLegalBases(SolrInputDocument doc, Judgment judgment) {
        for (String legalBase : judgment.getLegalBases()) {
            fieldAdder.addField(doc, JudgmentIndexField.LEGAL_BASE, legalBase);
        }
    }
    
    private void fillReferencedRegulations(SolrInputDocument doc, Judgment judgment) {
        for (JudgmentReferencedRegulation referencedRegulation : judgment.getReferencedRegulations()) {
            fieldAdder.addField(doc, JudgmentIndexField.REFERENCED_REGULATION, referencedRegulation.getRawText());
            
            LawJournalEntry lawJournalEntry = referencedRegulation.getLawJournalEntry();
            if (lawJournalEntry != null) {
                fieldAdder.addField(doc, JudgmentIndexField.LAW_JOURNAL_ENTRY_ID, lawJournalEntry.getId());
            }
        }
    }
    
    private void fillReferencedCourtCases(SolrInputDocument doc, Judgment judgment) {
        for (ReferencedCourtCase referencedCourtCase : judgment.getReferencedCourtCases()) {
            for (long referencedJudgmentId : referencedCourtCase.getJudgmentIds()) {
                fieldAdder.addField(doc, JudgmentIndexField.REFERENCED_COURT_CASE_ID, referencedJudgmentId);
            }
        }
    }
    
    private void fillJudgmentDate(SolrInputDocument doc, Judgment judgment) {
        fieldAdder.addDateField(doc, JudgmentIndexField.JUDGMENT_DATE, judgment.getJudgmentDate());
    }
    
    private void fillJudgmentType(SolrInputDocument doc, Judgment judgment) {
        if (judgment.getJudgmentType() != null) {
            fieldAdder.addField(doc, JudgmentIndexField.JUDGMENT_TYPE, judgment.getJudgmentType().name());
        }
    }
    
    private void fillMeansOfAppeal(SolrInputDocument doc, Judgment judgment) {
        if (judgment.getMeansOfAppeal() != null) {
            fieldAdder.addField(doc, JudgmentIndexField.ALL, judgment.getMeansOfAppeal().getName());
        }
    }
    
    private void fillJudgmentResult(SolrInputDocument doc, Judgment judgment) {
        if (judgment.getJudgmentResult() != null) {
            fieldAdder.addField(doc, JudgmentIndexField.ALL, judgment.getJudgmentResult().getText());
        }
    }
    
    private void fillContent(SolrInputDocument doc, Judgment judgment) {
        fieldAdder.addField(doc, JudgmentIndexField.CONTENT, judgment.getRawTextContent());
    }
    
    private void fillMaxMoneyAmount(SolrInputDocument doc, Judgment judgment) {
        if (judgment.getMaxMoneyAmount() != null) {
            fieldAdder.addCurrencyField(doc, JudgmentIndexField.MAXIMUM_MONEY_AMOUNT, judgment.getMaxMoneyAmount().getAmount());
        }
    }
    
    private void fillReferencingCount(SolrInputDocument doc, JudgmentIndexingData judgmentData) {
        fieldAdder.addField(doc, JudgmentIndexField.REFERENCING_JUDGMENTS_COUNT, judgmentData.getReferencingCount());
    }

    
    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setFieldAdder(SolrFieldAdder<JudgmentIndexField> fieldAdder) {
        this.fieldAdder = fieldAdder;
    }
}
