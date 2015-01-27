package pl.edu.icm.saos.persistence.model;

import java.util.Objects;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang3.StringUtils;

import pl.edu.icm.saos.common.util.StringTools;
import pl.edu.icm.saos.persistence.common.DataObject;

import com.google.common.base.Preconditions;

/**
 * pl. Hasła tematyczne/ słowa kluczowe
 * 
 * @author Łukasz Dumiszewski
 */
@Entity
@Table(uniqueConstraints={@UniqueConstraint(name="court_type_phrase_unique", columnNames={"courtType", "phrase"})})
@Cacheable(true)
@SequenceGenerator(name = "seq_judgment_keyword", allocationSize = 1, sequenceName = "seq_judgment_keyword")
public class JudgmentKeyword extends DataObject {

   
    private CourtType courtType;
    private String phrase;
   
    
    //------------------------ CONSTRUCTORS --------------------------
    
    // for hibernate
    @SuppressWarnings("unused")
    private JudgmentKeyword() {
        
    }

    /**
     * @param courtType may not be null
     * @param phrase may not be blank, will be lowercased with {@link StringTools#toRootLowerCase(String)}
     * @throws NullPointerException if courtType is null
     * @throws IllegalArgumentException if phrase is blank
     */
    public JudgmentKeyword(CourtType courtType, String phrase) {
        
        Preconditions.checkNotNull(courtType);
        Preconditions.checkArgument(StringUtils.isNotBlank(phrase));
        
        setCourtType(courtType);
        setPhrase(StringTools.toRootLowerCase(phrase));
    }
    
    
    //------------------------ GETTERS --------------------------
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_judgment_keyword")
    @Override
    public long getId() {
        return id;
    }
    
    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    public CourtType getCourtType() {
        return courtType;
    }

    @Column(nullable=false)
    public String getPhrase() {
        return phrase;
    }

    
    
    
    //------------------------ SETTERS --------------------------
    
    private void setCourtType(CourtType courtType) {
        this.courtType = courtType;
    }

    private void setPhrase(String phrase) {
        this.phrase = phrase;
    }

    
    
    //------------------------ HashCode & Equals --------------------------
    
    
    @Override
    public int hashCode() {
        return Objects.hash(this.phrase, this.courtType);
    }
    
    
    @Override
    public boolean equals(Object obj) {
        
        if (obj == null) {
           return false;
        }
        
        if (getClass() != obj.getClass()) {
           return false;
        }
        
        final JudgmentKeyword other = (JudgmentKeyword) obj;
        
        return Objects.equals(this.phrase, other.phrase) &&
               Objects.equals(this.courtType, other.courtType);

    }


    //------------------------ toString --------------------------
    
    @Override
    public String toString() {
        return "JudgmentKeyword [courtType="+courtType + ", phrase=" + phrase + "]";
    }

    
    
}
