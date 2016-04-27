package pl.edu.icm.saos.persistence.model;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import org.joda.time.DateTime;

import pl.edu.icm.saos.persistence.common.DataObject;

@Entity
@SequenceGenerator(name = "seq_removed_judgment", allocationSize = 1, sequenceName = "seq_removed_judgment")
public class RemovedJudgment extends DataObject {

    private long removedJudgmentId;
    
    private JudgmentSourceInfo sourceInfo = new JudgmentSourceInfo();
    
    private DateTime removedDate = new DateTime();


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
    
    public DateTime getRemovedDate() {
        return removedDate;
    }


    //------------------------ SETTERS --------------------------

    public void setRemovedJudgmentId(long removedJudgmentId) {
        this.removedJudgmentId = removedJudgmentId;
    }

    public void setSourceInfo(JudgmentSourceInfo sourceInfo) {
        this.sourceInfo = sourceInfo;
    }

    @SuppressWarnings("unused") /** for hibernate only */
    private void setRemovedDate(DateTime removedDate) {
        this.removedDate = removedDate;
    }

}
