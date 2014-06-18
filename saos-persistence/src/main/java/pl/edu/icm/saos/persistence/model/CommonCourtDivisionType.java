package pl.edu.icm.saos.persistence.model;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import pl.edu.icm.saos.persistence.common.DataObject;

/**
 * pl. Typ wydziału sądu powszechnego
 * 
 * Based on:
 * https://github.com/CeON/saos/tree/master/saos-persistence/src/main/doc/commonCourtCodes.pdf
 *
 * 
 * @author Łukasz Dumiszewski
 */
@Entity
@Cacheable(true)
@SequenceGenerator(name = "seq_cc_division_type", allocationSize = 1, sequenceName = "seq_cc_division_type")
public class CommonCourtDivisionType extends DataObject {

    private String code;
    private String name;

    
    //------------------------ GETTERS --------------------------
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_cc_division_type")
    @Override
    public int getId() {
        return id;
    }
    
    /** e.g. (Wydział) Pracy */
    public String getName() {
        return name;
    }
    
    /** e.g. 15 */
    @Column(unique=true, nullable=false)
    public String getCode() {
        return code;
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
        CommonCourtDivisionType other = (CommonCourtDivisionType) obj;
        if (code == null) {
            if (other.code != null)
                return false;
        } else if (!code.equals(other.code))
            return false;
        return true;
    }
    
    //------------------------ SETTERS --------------------------
    
    public void setName(String name) {
        this.name = name;
    }


    public void setCode(String code) {
        this.code = code;
    }





    
}
