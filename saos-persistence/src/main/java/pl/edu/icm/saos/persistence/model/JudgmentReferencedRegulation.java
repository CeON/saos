package pl.edu.icm.saos.persistence.model;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import pl.edu.icm.saos.common.visitor.Visitor;
import pl.edu.icm.saos.persistence.common.GeneratableDataObject;

/**
 * @author Łukasz Dumiszewski
 */
@Entity
@Table(uniqueConstraints={@UniqueConstraint(name="judgment_rawText_journalEntry_unique", columnNames={"fk_judgment", "rawText", "fk_law_journal_entry"})})
@Cacheable(true)
@SequenceGenerator(name = "seq_j_referenced_regulation", allocationSize = 1, sequenceName = "seq_j_referenced_regulation")
public class JudgmentReferencedRegulation extends GeneratableDataObject {

    
    private Judgment judgment;
    private String rawText;
    private LawJournalEntry lawJournalEntry;
    
    
    
    //------------------------ GETTERS --------------------------
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_j_referenced_regulation")
    @Override
    public long getId() {
        return id;
    }

    @ManyToOne(fetch=FetchType.LAZY)
    public Judgment getJudgment() {
        return judgment;
    }

    @ManyToOne(fetch=FetchType.LAZY)
    public LawJournalEntry getLawJournalEntry() {
        return lawJournalEntry;
    }

    /**
     *  The raw legal basis text from the judgment source. <br/>
     * */
    public String getRawText() {
        return rawText;
    }

    //------------------------ LOGIC --------------------------
    @Override
    public void passVisitorDown(Visitor visitor) {
        if (lawJournalEntry != null) {
            lawJournalEntry.accept(visitor);
        }
    }

    //------------------------ SETTERS --------------------------
    
    public void setJudgment(Judgment judgment) {
        this.judgment = judgment;
    }


    public void setLawJournalEntry(LawJournalEntry lawJournalEntry) {
        this.lawJournalEntry = lawJournalEntry;
    }

    public void setRawText(String rawText) {
        this.rawText = rawText;
    }

    
    
    @Override
    public String toString() {
        return "ReferencedRegulation [rawText="
                + rawText + ", lawJournalEntry=" + lawJournalEntry + ", id="
                + id + "]";
    }
    
    
    
    //------------------------ HashCode & Equals --------------------------
    

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((judgment == null) ? 0 : judgment.hashCode());
        result = prime * result
                + ((lawJournalEntry == null) ? 0 : lawJournalEntry.hashCode());
        result = prime * result + ((rawText == null) ? 0 : rawText.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        JudgmentReferencedRegulation other = (JudgmentReferencedRegulation) obj;
        if (judgment == null) {
            if (other.judgment != null)
                return false;
        } else if (!judgment.equals(other.judgment))
            return false;
        if (lawJournalEntry == null) {
            if (other.lawJournalEntry != null)
                return false;
        } else if (!lawJournalEntry.equals(other.lawJournalEntry))
            return false;
        if (rawText == null) {
            if (other.rawText != null)
                return false;
        } else if (!rawText.equals(other.rawText))
            return false;
        return true;
    }
    
    
   
    
}
