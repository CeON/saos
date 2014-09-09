package pl.edu.icm.saos.api.builders;

import pl.edu.icm.saos.persistence.model.JudgmentReferencedRegulation;
import pl.edu.icm.saos.persistence.model.LawJournalEntry;

/**
 * @author pavtel
 */
public class JudgmentReferencedRegulationBuilder extends JudgmentReferencedRegulation {

    JudgmentReferencedRegulationBuilder() {
    }

    public JudgmentReferencedRegulationBuilder rawText(String rawText){
        setRawText(rawText);
        return this;
    }

    public JudgmentReferencedRegulationBuilder lawJournalEntry(LawJournalEntry lawJournalEntry){
        setLawJournalEntry(lawJournalEntry);
        return this;
    }
}