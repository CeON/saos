package pl.edu.icm.saos.persistence.builder;

import pl.edu.icm.saos.persistence.model.JudgmentReferencedRegulation;
import pl.edu.icm.saos.persistence.model.LawJournalEntry;

/**
 * @author pavtel
 * Simplified {@link pl.edu.icm.saos.persistence.model.JudgmentReferencedRegulation JuJudgmentReferencedRegulation} creation.
 * Do not use it in conjugation with persistence's repositories.
 */
public class JudgmentReferencedRegulationBuilder{

    private JudgmentReferencedRegulation element;

    JudgmentReferencedRegulationBuilder() {
        element = new JudgmentReferencedRegulation();
    }

    public JudgmentReferencedRegulationBuilder rawText(String rawText){
        element.setRawText(rawText);
        return this;
    }

    public JudgmentReferencedRegulationBuilder lawJournalEntry(LawJournalEntry lawJournalEntry){
        element.setLawJournalEntry(lawJournalEntry);
        return this;
    }

    public JudgmentReferencedRegulation build(){
        return element;
    }
}