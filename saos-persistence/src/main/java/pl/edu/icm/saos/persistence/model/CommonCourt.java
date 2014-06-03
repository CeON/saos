package pl.edu.icm.saos.persistence.model;

import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import pl.edu.icm.saos.persistence.common.DataObject;

/**
 * pl. Sąd Powszechny
 * <br/> <br/>
 * Dictionary of common courts
 * 
 * @author Łukasz Dumiszewski
 */
@Entity
@Cacheable(true)
@SequenceGenerator(name = "seq_common_court", allocationSize = 1, sequenceName = "seq_common_court")
public class CommonCourt extends DataObject {

    public enum CommonCourtType {
        /** pl. sąd apelacyjny */
        APPEAL_COURT,  
        
        /** pl. sąd okręgowy */
        REGIONAL_COURT,
        
        /** pl. sąd rejonowy */
        DISTRICT_COURT 
    }
    
    
    private String name;
    
    private CommonCourtType type;
    
    private List<CommonCourtDivision> divisions;
    
    
    
    //------------------------ GETTERS --------------------------
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_common_court")
    @Override
    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    @Enumerated(EnumType.STRING)
    public CommonCourtType getType() {
        return type;
    }
    
    @OneToMany(cascade=CascadeType.ALL, mappedBy="court")
    public List<CommonCourtDivision> getDivisions() {
        return divisions;
    }
    
    
    //------------------------ SETTERS --------------------------
    
    public void setName(String name) {
        this.name = name;
    }
    public void setType(CommonCourtType type) {
        this.type = type;
    }
    public void setDivisions(List<CommonCourtDivision> divisions) {
        this.divisions = divisions;
    }
}
