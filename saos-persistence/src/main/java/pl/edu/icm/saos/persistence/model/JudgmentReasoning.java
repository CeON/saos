package pl.edu.icm.saos.persistence.model;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

import org.joda.time.DateTime;

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
    private DateTime publicationDate;
    private String reviser;
    private String publisher;
    
    
    
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



    public DateTime getPublicationDate() {
        return publicationDate;
    }



    public String getReviser() {
        return reviser;
    }



    public String getPublisher() {
        return publisher;
    }


    
    //------------------------ SETTERS --------------------------

    public void setJudgment(Judgment judgment) {
        this.judgment = judgment;
    }



    public void setText(String text) {
        this.text = text;
    }



    public void setPublicationDate(DateTime publicationDate) {
        this.publicationDate = publicationDate;
    }



    public void setReviser(String reviser) {
        this.reviser = reviser;
    }



    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

}
