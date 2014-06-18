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

import org.gradle.jarjar.com.google.common.collect.Lists;

import pl.edu.icm.saos.persistence.common.DataObject;

/**
 * pl. Sędzia
 * 
 * @author Łukasz Dumiszewski
 */
@Entity
@Cacheable(true)
@SequenceGenerator(name = "seq_judge", allocationSize = 1, sequenceName = "seq_judge")
public class Judge extends DataObject {
    
    
    public enum JudgeRole {
        
        /** pl. przewodniczacy składu sędziowskiego */
        PRESIDING_JUDGE, 
        
        /** pl. sędzia sprawozdawca */
        REPORTING_JUDGE,
        
        /** pl. autor uzasadnienia */
        REASONS_FOR_JUDGMENT_AUTHOR
    }
    
    private Judgment judgment;
    
    private String name;
    
    private List<JudgeRole> specialRoles;

    
    
    
    //------------------------ CONSTRUCTORS --------------------------
    
    
    public Judge(String name, JudgeRole... specialRoles) {
        super();
        this.name = name;
        this.specialRoles = Lists.newArrayList(specialRoles);
    }
    

    public Judge() {
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
    
    
}
