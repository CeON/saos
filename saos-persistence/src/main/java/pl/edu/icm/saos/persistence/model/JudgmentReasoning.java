package pl.edu.icm.saos.persistence.model;

import javax.persistence.Cacheable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

import pl.edu.icm.saos.persistence.common.DataObject;

/**
 * Reasons for judgment, pl. uzasadnienie orzeczenia
 * 
 * @author Łukasz Dumiszewski
 */
@Entity
@Cacheable(true)
@SequenceGenerator(name = "seq_judgment_reasoning", allocationSize = 1, sequenceName = "seq_judgment_reasoning")
public class JudgmentReasoning extends DataObject {

    private Judgment judgment;
    private String text;
    private JudgmentSourceInfo sourceInfo = new JudgmentSourceInfo();
    
    
    
    //------------------------ GETTERS --------------------------
    
    
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_judgment_reasoning")
    @Override
    public int getId() {
        return id;
    }


    @OneToOne(optional=false)
    public Judgment getJudgment() {
        return judgment;
    }



    public String getText() {
        return text;
    }

    @Embedded
    public JudgmentSourceInfo getSourceInfo() {
        return sourceInfo;
    }

    


    
    //------------------------ SETTERS --------------------------

    public void setJudgment(Judgment judgment) {
        this.judgment = judgment;
    }


    public void setText(String text) {
        this.text = text;
    }

    public void setSourceInfo(JudgmentSourceInfo sourceInfo) {
        this.sourceInfo = sourceInfo;
    }


   
}
