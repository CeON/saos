package pl.edu.icm.saos.search.search.model;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judge.JudgeRole;

/**
 * Judge search result
 * @author madryk
 */
public class JudgeResult {

    private String name;
    private List<JudgeRole> specialRoles;
    
    
    public JudgeResult(String name, JudgeRole ... specialRoles) {
        this.name = name;
        this.specialRoles = Lists.newArrayList(specialRoles);
    }

    
    //------------------------ GETTERS --------------------------

    public String getName() {
        return name;
    }

    public List<JudgeRole> getSpecialRoles() {
        return specialRoles;
    }

    
    //------------------------ LOGIC --------------------------
    
    public boolean isPresidingJudge() {
    	return specialRoles.stream()
				.filter(p -> p.equals(Judge.JudgeRole.PRESIDING_JUDGE))
				.collect(Collectors.toList())
				.size() > 0 ? true: false; 
    }
    
    //------------------------ SETTERS --------------------------

    public void setName(String name) {
        this.name = name;
    }

    public void setSpecialRoles(List<JudgeRole> specialRoles) {
        this.specialRoles = specialRoles;
    }
    
    
    //------------------------ equals & hashCode --------------------------
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result
                + ((specialRoles == null) ? 0 : specialRoles.hashCode());
        return result;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof JudgeResult)) {
            return false;
        }
        JudgeResult other = (JudgeResult) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (specialRoles == null) {
            if (other.specialRoles != null) {
                return false;
            }
        } else if (!specialRoles.equals(other.specialRoles)) {
            return false;
        }
        return true;
    }
}
