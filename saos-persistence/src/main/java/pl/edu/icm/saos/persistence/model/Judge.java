package pl.edu.icm.saos.persistence.model;

import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import pl.edu.icm.saos.persistence.common.DataObject;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

/**
 * pl. Sędzia
 * 
 * @author Łukasz Dumiszewski
 */
@Entity
@Table(uniqueConstraints={@UniqueConstraint(name="judgment_name_unique", columnNames={"fk_judgment", "name"})})
@Cacheable(true)
@SequenceGenerator(name = "seq_judge", allocationSize = 1, sequenceName = "seq_judge")
public class Judge extends DataObject {
    
    
    public enum JudgeRole {
        
        /** pl. przewodniczacy składu sędziowskiego */
        PRESIDING_JUDGE, 
        
        /** pl. sędzia sprawozdawca */
        REPORTING_JUDGE,
        
        /** pl. autor uzasadnienia */
        REASONS_FOR_JUDGMENT_AUTHOR;
        
        
        
    }
    
    private Judgment judgment;
    
    private String name;
    
    private String function;
    
    private List<JudgeRole> specialRoles;

    
    
    
    //------------------------ CONSTRUCTORS --------------------------
    
    
    public Judge(String name, JudgeRole... specialRoles) {
        this(name, Lists.newArrayList(specialRoles));
    }
    
    public Judge(String name, List<JudgeRole> specialRoles) {
        super();
        Preconditions.checkArgument(!StringUtils.isBlank(name));
        this.name = name;
        this.specialRoles = specialRoles;
    }
    

    @SuppressWarnings("unused") // for hibernate
    private Judge() {
        super();
    }

    
    
    //------------------------ GETTERS --------------------------
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_judge")
    @Override
    public int getId() {
        return id;
    }
    
    @Enumerated(EnumType.STRING)
    @ElementCollection
    @CollectionTable(name="judge_role", joinColumns = @JoinColumn(name = "fk_judge"))
    @Column(name="role")
    public List<JudgeRole> getSpecialRoles() {
        return specialRoles;
    }

    @ManyToOne
    public Judgment getJudgment() {
        return judgment;
    }

    /** judge's name (in most cases full name) */
    public String getName() {
        return name;
    }

    /** Additional function of the judge, for example SSN in Supreme Court*/
    public String getFunction() {
        return function;
    }


    //------------------------ LOGIC --------------------------
    
    public boolean hasAnySpecialRole() {
        return !CollectionUtils.isEmpty(getSpecialRoles());
    }
    
    
    //------------------------ SETTERS --------------------------
    
    public void setSpecialRoles(List<JudgeRole> specialRoles) {
        this.specialRoles = specialRoles;
    }

    public void setJudgment(Judgment judgment) {
        this.judgment = judgment;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFunction(String function) {
        this.function = function;
    }


    //------------------------ HashCode & Equals --------------------------
    

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((judgment == null) ? 0 : judgment.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        Judge other = (Judge) obj;
        if (judgment == null) {
            if (other.judgment != null)
                return false;
        } else if (!judgment.equals(other.judgment))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(Judge.class)
                .add("name", name)
                .add("roles", specialRoles)
                .toString();
    }

   
}
