package pl.edu.icm.saos.persistence.model;

import static pl.edu.icm.saos.common.util.StringTools.toRootLowerCase;

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
 * Dictionary of shortened results of judgments, e.g. rejection
 * 
 * pl. skrócony wynik sprawy
 * @author Łukasz Dumiszewski
 */
@Entity
@Table(uniqueConstraints={@UniqueConstraint(name="court_type_judgment_result_text_unique", columnNames={"courtType", "text"})})
@Cacheable(true)
@SequenceGenerator(name = "seq_judgment_result", allocationSize = 1, sequenceName = "seq_judgment_result")
public class JudgmentResult extends DataObject {

   
    private CourtType courtType;
    private String text;
   
    
    //------------------------ CONSTRUCTORS --------------------------
    
    // for hibernate
    @SuppressWarnings("unused")
    private JudgmentResult() {
        
    }

    /**
     * @param courtType must not be null
     * @param text may not be blank, will be lowercased with {@link StringTools#toRootLowerCase(String)}
     * @throws NullPointerException if courtType is null
     * @throws IllegalArgumentException if text is blank
     */
    public JudgmentResult(CourtType courtType, String text) {
        
        Preconditions.checkNotNull(courtType);
        Preconditions.checkArgument(StringUtils.isNotBlank(text));
        
        setCourtType(courtType);
        setText(toRootLowerCase(text));
    }
    
    
    //------------------------ GETTERS --------------------------
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_judgment_result")
    @Override
    public long getId() {
        return id;
    }
    
    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    public CourtType getCourtType() {
        return courtType;
    }
    
    /**
     * The text value of the result, e.g. rejected /pl. odrzucono/
     */
    @Column(nullable=false)
    public String getText() {
        return text;
    }
    
    
    
    //------------------------ SETTERS --------------------------
    
    private void setText(String text) {
        this.text = text;
    }
    
    public void setCourtType(CourtType courtType) {
        this.courtType = courtType;
    }

    
    
    //------------------------ HashCode & Equals --------------------------
    
    
    @Override
    public int hashCode() {
        return Objects.hash(courtType, toRootLowerCase(this.text));
    }
    
    
    @Override
    public boolean equals(Object obj) {
        
        if (obj == null) {
           return false;
        }
        
        if (getClass() != obj.getClass()) {
           return false;
        }
        
        final JudgmentResult other = (JudgmentResult) obj;
        
        return Objects.equals(this.courtType, other.courtType) 
            && Objects.equals(toRootLowerCase(this.text), toRootLowerCase(other.text));

    }


    //------------------------ toString --------------------------
    
    @Override
    public String toString() {
        return "JudgmentResult [courtType = " + courtType + ", text=" + text + "]";
    }

    
    
    
}

