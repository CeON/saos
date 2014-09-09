package pl.edu.icm.saos.api.builders;

import pl.edu.icm.saos.persistence.model.LawJournalEntry;

/**
 * @author pavtel
 */
public class LawJournalEntryBuilder extends LawJournalEntry {

    LawJournalEntryBuilder() {
    }

    public LawJournalEntryBuilder title(String title){
        setTitle(title);
        return this;
    }

    public LawJournalEntryBuilder year(int year){
        setYear(year);
        return this;
    }

    public LawJournalEntryBuilder entry(int entry){
        setEntry(entry);
        return this;
    }

    public LawJournalEntryBuilder journalNo(int journalNo){
        setJournalNo(journalNo);
        return this;
    }

}