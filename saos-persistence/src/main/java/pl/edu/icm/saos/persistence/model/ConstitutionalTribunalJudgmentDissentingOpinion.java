package pl.edu.icm.saos.persistence.model;

import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import pl.edu.icm.saos.persistence.common.DataObject;

/**
 * pl. odrębna opinia
 * 
 * @author madryk
 */
@Entity(name = "ct_judgment_opinion")
@Cacheable(true)
@SequenceGenerator(name = "seq_ctj_opinion", allocationSize = 1, sequenceName = "seq_ctj_opinion")
public class ConstitutionalTribunalJudgmentDissentingOpinion extends DataObject {

    private Judgment judgment;
    
    private String textContent;
    
    private List<String> authors;
    
    
    //------------------------ GETTERS --------------------------
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_ctj_opinion")
    @Override
    public int getId() {
        return id;
    }
    
    @ManyToOne(fetch=FetchType.LAZY)
    public Judgment getJudgment() {
        return judgment;
    }
    
    public String getTextContent() {
        return textContent;
    }
    
    @ElementCollection
    @CollectionTable(name="ct_judgment_opinion_author", uniqueConstraints={@UniqueConstraint(name="ct_judgment_opinion_author_unique", columnNames={"fk_constitutional_tribunal_judgment_dissenting_opinion", "author"})})
    @Column(name="author")
    private List<String> getAuthors_() {
        return authors;
    }
    
    @Transient
    public List<String> getAuthors() {
        return ImmutableList.copyOf(getAuthors_());
    }
    
    
    //------------------------ LOGIC --------------------------
    
    public void addAuthor(String author) {
        Preconditions.checkArgument(!containsAuthor(author));
        
        this.authors.add(author);
    }

    public boolean containsAuthor(String author) {
        return this.authors.contains(author);
    }
    
    public void removeAuthor(String author) {
        this.authors.remove(author);
    }
    
    
    //------------------------ SETTERS --------------------------
    
    public void setJudgment(Judgment judgment) {
        this.judgment = judgment;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    @SuppressWarnings("unused") // for hibernate
    private void setAuthors_(List<String> authors) {
        this.authors = authors;
    }

    
    //------------------------ HashCode & Equals --------------------------
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((authors == null) ? 0 : authors.hashCode());
        result = prime * result
                + ((judgment == null) ? 0 : judgment.hashCode());
        result = prime * result
                + ((textContent == null) ? 0 : textContent.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof ConstitutionalTribunalJudgmentDissentingOpinion)) {
            return false;
        }
        ConstitutionalTribunalJudgmentDissentingOpinion other = (ConstitutionalTribunalJudgmentDissentingOpinion) obj;
        if (authors == null) {
            if (other.authors != null) {
                return false;
            }
        } else if (!authors.equals(other.authors)) {
            return false;
        }
        if (judgment == null) {
            if (other.judgment != null) {
                return false;
            }
        } else if (!judgment.equals(other.judgment)) {
            return false;
        }
        if (textContent == null) {
            if (other.textContent != null) {
                return false;
            }
        } else if (!textContent.equals(other.textContent)) {
            return false;
        }
        return true;
    }

    
    //------------------------ toString --------------------------
    
    @Override
    public String toString() {
        return "ConstitutionalTribunalJudgmentDissentingOpinion [id="
                + id + ", textContent=" + textContent + ", authors="
                + authors + "]";
    }

    
    

}
