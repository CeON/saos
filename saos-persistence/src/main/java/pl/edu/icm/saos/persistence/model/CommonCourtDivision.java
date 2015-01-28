package pl.edu.icm.saos.persistence.model;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import pl.edu.icm.saos.persistence.common.ColumnDefinitionConst;
import pl.edu.icm.saos.persistence.common.DataObject;

/**
 * Court & department codes based roughly* on: <br/>
 * https://github.com/CeON/saos/tree/master/saos-persistence/src/main/doc/commonCourtCodes.pdf <br/><br/>
 * *not all codes are compatible with this document
 * @author Łukasz Dumiszewski
 */
@Table(uniqueConstraints={@UniqueConstraint(name="court_division_code_unique", columnNames={"fk_court", "code"})})
@Entity
@Cacheable(true)
@SequenceGenerator(name = "seq_common_court_division", allocationSize = 1, sequenceName = "seq_common_court_division")
public class CommonCourtDivision extends DataObject {
    
    
    private boolean active = true;
    private CommonCourt court;
    private String code;
    private String name;
    private CommonCourtDivisionType type;
    
    
    //------------------------ GETTERS --------------------------
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_common_court_division")
    @Override
    public long getId() {
        return id;
    }
    
    @ManyToOne
    public CommonCourt getCourt() {
        return court;
    }
    
    public String getName() {
        return name;
    }
    
    @ManyToOne
    public CommonCourtDivisionType getType() {
        return type;
    }

    public String getCode() {
        return code;
    }
    
    /** if a division is deleted in reality, then we mark it as inactive */
    @Column(columnDefinition=ColumnDefinitionConst.BOOLEAN_NOT_NULL_DEFUALT_TRUE)
    public boolean isActive() {
        return active;
    }

    
    //------------------------ LOGIC --------------------------
    
    public void markInactive() {
        setActive(false);
    }
    
    //------------------------ SETTERS --------------------------
    
    public void setCourt(CommonCourt court) {
        this.court = court;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setType(CommonCourtDivisionType type) {
        this.type = type;
    }

    public void setCode(String code) {
        this.code = code;
    }
    
    private void setActive(boolean active) {
        this.active = active;
    }
    
    
    //------------------------ HashCode & Equals --------------------------
    

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((code == null) ? 0 : code.hashCode());
        result = prime * result + ((court == null) ? 0 : court.hashCode());
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
        CommonCourtDivision other = (CommonCourtDivision) obj;
        if (code == null) {
            if (other.code != null)
                return false;
        } else if (!code.equals(other.code))
            return false;
        if (court == null) {
            if (other.court != null)
                return false;
        } else if (!court.equals(other.court))
            return false;
        return true;
    }

    
    
}
