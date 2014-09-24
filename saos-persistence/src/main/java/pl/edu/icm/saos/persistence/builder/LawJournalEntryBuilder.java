package pl.edu.icm.saos.persistence.builder;

import pl.edu.icm.saos.persistence.model.LawJournalEntry;

/**
 * @author pavtel
 * Simplified {@link pl.edu.icm.saos.persistence.model.LawJournalEntry LawJournalEntry} creation.
 * Do not use it in conjugation with persistence's repositories.
 */
public class LawJournalEntryBuilder{

    private LawJournalEntry element;

    LawJournalEntryBuilder() {
        element = new LawJournalEntry();
    }

    public LawJournalEntryBuilder title(String title){
        element.setTitle(title);
        return this;
    }

    public LawJournalEntryBuilder year(int year){
        element.setYear(year);
        return this;
    }

    public LawJournalEntryBuilder entry(int entry){
        element.setEntry(entry);
        return this;
    }

    public LawJournalEntryBuilder journalNo(int journalNo){
        element.setJournalNo(journalNo);
        return this;
    }

    public LawJournalEntry build(){
        return element;
    }

}