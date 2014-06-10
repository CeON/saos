package pl.edu.icm.saos.persistence.model;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import pl.edu.icm.saos.persistence.common.DataObject;

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
@Table(uniqueConstraints={@UniqueConstraint(name="code_unique", columnNames={"appealCourtCode", "regionalCourtCode", "districtCourtCode"})})
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
    
    private int appealCourtCode;
    private int regionalCourtCode;
    private int districtCourtCode;
    
    private String shortName;
    private String name;
    
    
    
    //------------------------ GETTERS --------------------------
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_common_court")
    @Override
    public int getId() {
        return id;
    }
    
    public int getAppealCourtCode() {
        return appealCourtCode;
    }

    public int getRegionalCourtCode() {
        return regionalCourtCode;
    }

    public int getDistrictCourtCode() {
        return districtCourtCode;
    }
    
    public String getName() {
        return name;
    }
    
    public String getShortName() {
        return shortName;
    }

    @Transient
    public CommonCourtType getType() {
        if (districtCourtCode != 0) {
            return CommonCourtType.DISTRICT_COURT;
        }
        
        if (regionalCourtCode != 0) {
            return CommonCourtType.REGIONAL_COURT;
        }
        
        if (appealCourtCode != 0) {
            return CommonCourtType.APPEAL_COURT;
        }
        
        throw new IllegalStateException("can't identify common court type, incorrect court codes");
    }
    
    
    //------------------------ SETTERS --------------------------
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public void setAppealCourtCode(int appealCourtCode) {
        this.appealCourtCode = appealCourtCode;
    }

    public void setRegionalCourtCode(int regionalCourtCode) {
        this.regionalCourtCode = regionalCourtCode;
    }

    public void setDistrictCourtCode(int districtCourtCode) {
        this.districtCourtCode = districtCourtCode;
    }
}
