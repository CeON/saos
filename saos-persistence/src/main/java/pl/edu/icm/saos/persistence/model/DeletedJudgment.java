package pl.edu.icm.saos.persistence.model;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import pl.edu.icm.saos.persistence.common.DataObject;

/**
 * Entity representing judgment that have been
 * deleted from database
 * 
 * @author madryk
 */
@Entity
@SequenceGenerator(name = "seq_removed_judgment", allocationSize = 1, sequenceName = "seq_removed_judgment")
public class DeletedJudgment extends DataObject {

    private long removedJudgmentId;
    
    private JudgmentSourceInfo sourceInfo = new JudgmentSourceInfo();


    //------------------------ GETTERS --------------------------
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_removed_judgment")
    @Override
    public long getId() {
        return id;
    }
    
    public long getRemovedJudgmentId() {
        return removedJudgmentId;
    }
    
    @Embedded
    public JudgmentSourceInfo getSourceInfo() {
        return sourceInfo;
    }


    //------------------------ SETTERS --------------------------

    public void setRemovedJudgmentId(long removedJudgmentId) {
        this.removedJudgmentId = removedJudgmentId;
    }

    public void setSourceInfo(JudgmentSourceInfo sourceInfo) {
        this.sourceInfo = sourceInfo;
    }

}
