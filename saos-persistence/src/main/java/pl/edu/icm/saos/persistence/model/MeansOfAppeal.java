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
 * Dictionary of means of appeal divided by {@link #CourtType}
 * <br/><br/>
 * pl. środek odwoławczy
 * 
 * @author Łukasz Dumiszewski
 */
@Entity
@Table(uniqueConstraints={@UniqueConstraint(name="court_type_appeal_name_unique", columnNames={"courtType", "name"})})
@Cacheable(true)
@SequenceGenerator(name = "seq_means_of_appeal", allocationSize = 1, sequenceName = "seq_means_of_appeal")
public class MeansOfAppeal extends DataObject {

   
    private CourtType courtType;
    private String name;
   
    
    //------------------------ CONSTRUCTORS --------------------------
    
    // for hibernate
    @SuppressWarnings("unused")
    private MeansOfAppeal() {
        
    }

    /**
     * @param courtType may not be null
     * @param name may not be blank, will be lowercased with {@link StringTools#toRootLowerCase(String)}
     * @throws NullPointerException if courtType is null
     * @throws IllegalArgumentException if name is blank
     */
    public MeansOfAppeal(CourtType courtType, String name) {
        
        Preconditions.checkNotNull(courtType);
        Preconditions.checkArgument(StringUtils.isNotBlank(name));
        
        setCourtType(courtType);
        setName(toRootLowerCase(name));
    }
    
    
    //------------------------ GETTERS --------------------------
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_means_of_appeal")
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
     * Name of a means of appeal, for example complaint
     * @return
     */
    @Column(nullable=false)
    public String getName() {
        return name;
    }

    
    
    
    //------------------------ SETTERS --------------------------
    
    private void setCourtType(CourtType courtType) {
        this.courtType = courtType;
    }

    private void setName(String name) {
        this.name = name;
    }

    
    
    //------------------------ HashCode & Equals --------------------------
    
    
    @Override
    public int hashCode() {
        return Objects.hash(toRootLowerCase(this.name), this.courtType);
    }
    
    
    @Override
    public boolean equals(Object obj) {
        
        if (obj == null) {
           return false;
        }
        
        if (getClass() != obj.getClass()) {
           return false;
        }
        
        final MeansOfAppeal other = (MeansOfAppeal) obj;
        
        return Objects.equals(toRootLowerCase(this.name), toRootLowerCase(other.name)) &&
               Objects.equals(this.courtType, other.courtType);

    }


    //------------------------ toString --------------------------
    
    @Override
    public String toString() {
        return "MeansOfAppeal [courtType="+courtType + ", name=" + name + "]";
    }

    
    
}
