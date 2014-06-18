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

import org.apache.commons.lang3.StringUtils;

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
    
    private String appealCourtCode;
    private String regionalCourtCode;
    private String districtCourtCode;
    
    private String shortName;
    private String name;
    
    
    
    //------------------------ GETTERS --------------------------
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_common_court")
    @Override
    public int getId() {
        return id;
    }
    
    /** See class description */
    public String getAppealCourtCode() {
        return appealCourtCode;
    }

    /** See class description */
    public String getRegionalCourtCode() {
        return regionalCourtCode;
    }

    /** See class description */
    public String getDistrictCourtCode() {
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
        if (!StringUtils.equals(districtCourtCode, "00")) {
            return CommonCourtType.DISTRICT_COURT;
        }
        
        if (!StringUtils.equals(regionalCourtCode, "00")) {
            return CommonCourtType.REGIONAL_COURT;
        }
        
        if (!StringUtils.equals(appealCourtCode, "00")) {
            return CommonCourtType.APPEAL_COURT;
        }
        
        throw new IllegalStateException("can't identify common court type, incorrect court codes");
    }
    
    @Transient
    public String getCourtCode() {
        return "15" + this.appealCourtCode + this.regionalCourtCode + this.districtCourtCode;
    }

    
    //------------------------ PRIVATE --------------------------
    
    
    
    
    //------------------------ HashCode & Equals --------------------------

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((appealCourtCode == null) ? 0 : appealCourtCode.hashCode());
        result = prime
                * result
                + ((districtCourtCode == null) ? 0 : districtCourtCode
                        .hashCode());
        result = prime
                * result
                + ((regionalCourtCode == null) ? 0 : regionalCourtCode
                        .hashCode());
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
        if (appealCourtCode == null) {
            if (other.appealCourtCode != null)
                return false;
        } else if (!appealCourtCode.equals(other.appealCourtCode))
            return false;
        if (districtCourtCode == null) {
            if (other.districtCourtCode != null)
                return false;
        } else if (!districtCourtCode.equals(other.districtCourtCode))
            return false;
        if (regionalCourtCode == null) {
            if (other.regionalCourtCode != null)
                return false;
        } else if (!regionalCourtCode.equals(other.regionalCourtCode))
            return false;
        return true;
    }


    
    //------------------------ SETTERS --------------------------
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public void setAppealCourtCode(String appealCourtCode) {
        this.appealCourtCode = appealCourtCode;
    }

    public void setRegionalCourtCode(String regionalCourtCode) {
        this.regionalCourtCode = regionalCourtCode;
    }

    public void setDistrictCourtCode(String districtCourtCode) {
        this.districtCourtCode = districtCourtCode;
    }

   
}
