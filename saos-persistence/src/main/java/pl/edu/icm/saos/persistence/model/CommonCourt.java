package pl.edu.icm.saos.persistence.model;

import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import pl.edu.icm.saos.persistence.common.DataObject;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

/**
 * pl. Sąd Powszechny
 * <br/> <br/>
 * Dictionary of common courts
 * <br/><br/>
 * Court codes based on: 
 * https://github.com/CeON/saos/tree/master/saos-persistence/src/main/doc/commonCourtCodes.pdf
 * 
 * 
 * @author Łukasz Dumiszewski
 */
@Table
@Entity
@Cacheable(true)
@SequenceGenerator(name = "seq_common_court", allocationSize = 1, sequenceName = "seq_common_court")
public class CommonCourt extends DataObject {

    public enum CommonCourtType {
        /** pl. sąd apelacyjny */
        APPEAL,  
        
        /** pl. sąd okręgowy */
        REGIONAL,
        
        /** pl. sąd rejonowy */
        DISTRICT 
    }
    
    private String code;
    private String name;
    private CommonCourtType type;
    private CommonCourt parentCourt;
    private List<CommonCourtDivision> divisions = Lists.newArrayList();
    
    
    
    //------------------------ GETTERS --------------------------
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_common_court")
    @Override
    public int getId() {
        return id;
    }
    
    /** See class description */
    @Column(unique=true, nullable=false)
    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
    
    @Enumerated(EnumType.STRING)
    public CommonCourtType getType() {
        return type;
    }
    
    @OneToMany(mappedBy="court", cascade=CascadeType.ALL)
    private List<CommonCourtDivision> getDivisions_() {
        return divisions;
    }
    
    @Transient
    public List<CommonCourtDivision> getDivisions() {
        return ImmutableList.copyOf(divisions);
    }


    @ManyToOne(fetch=FetchType.LAZY)
    public CommonCourt getParentCourt() {
        return parentCourt;
    }

    
    //------------------------ LOGIC --------------------------
    
    /**
     * Returns {@link CommonCourtDivision} with the given divisionCode ({@link CommonCourtDivision#getCode()})
     * and assigned to this court, or null if no division with the passed divisionCode can be found.
     *  
     */
    @Transient
    public CommonCourtDivision getDivision(String divisionCode) {
        for (CommonCourtDivision division : getDivisions_()) {
            if (divisionCode.equals(division.getCode())) {
                return division;
            }
        }
        return null;
    }
    
    public boolean hasDivision(String divisionCode) {
        return getDivision(divisionCode) != null;
    }
    
    @Transient
    public void addDivision(CommonCourtDivision division) {
        division.setCourt(this);
        divisions.add(division);
    }
    
    
    
    //------------------------ PRIVATE --------------------------
    
    
    
    
    //------------------------ HashCode & Equals --------------------------

   

    
    //------------------------ SETTERS --------------------------
    
    public void setName(String name) {
        this.name = name;
    }
    
    @SuppressWarnings("unused") /** for jpa */
    private void setDivisions_(List<CommonCourtDivision> divisions) {
        this.divisions = divisions;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setType(CommonCourtType type) {
        this.type = type;
    }

    public void setParentCourt(CommonCourt parentCourt) {
        this.parentCourt = parentCourt;
    }

    //------------------------ HashCode & Equals --------------------------
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((code == null) ? 0 : code.hashCode());
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
        CommonCourt other = (CommonCourt) obj;
        if (code == null) {
            if (other.code != null)
                return false;
        } else if (!code.equals(other.code))
            return false;
        return true;
    }

    
   
}
