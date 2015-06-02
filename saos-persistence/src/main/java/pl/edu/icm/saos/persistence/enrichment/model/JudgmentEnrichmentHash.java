package pl.edu.icm.saos.persistence.enrichment.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang3.StringUtils;

import pl.edu.icm.saos.persistence.common.DataObject;

/**
 * Entity containing hashes generated from enrichment tags related to judgments 
 * (generated for it, see: {@link EnrichmentTag#getJudgmentId()} or 
 * referring to it in their value, see: {@link EnrichmentTag#getValue()}).<br/>
 * Hashes are used to determine if enrichment tags have changed 
 * since the last processing of them.
 * 
 * @author madryk
 */
@Entity
@Table(uniqueConstraints={@UniqueConstraint(name="tag_judgment_id_unique", columnNames={"judgmentId"})})
@SequenceGenerator(name = "seq_judgment_enrichment_hash", allocationSize = 1, sequenceName = "seq_judgment_enrichment_hash")
public class JudgmentEnrichmentHash extends DataObject {

    private long judgmentId;
    
    private String oldHash;
    
    private String hash;
    
    private boolean processed;

    
    //------------------------ LOGIC --------------------------
    
    /**
     * Updates hash value (see {@link #getHash()}). Marks judgment enrichment tags as not processed if
     * hash value changed since last enrichment tags processing.
     */
    public void updateHash(String newHash) {
        if (processed) {
            this.oldHash = this.hash;
            this.hash = newHash;
            if (!StringUtils.equals(this.oldHash, this.hash)) {
                processed = false;
            }
        } else {
            this.hash = newHash;
            if (StringUtils.equals(this.oldHash, this.hash)) {
                processed = true;
            }
        }
    }
    
    
    //------------------------ GETTERS --------------------------
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_judgment_enrichment_hash")
    @Override
    public long getId() {
        return id;
    }


    /**
     * Returns id of judgment ({@link pl.edu.icm.saos.persistence.model.Judgment#getId()})
     */
    public long getJudgmentId() {
        return judgmentId;
    }

    /**
     * Returns a hash for the last but one judgment enrichment tags (not the current ones).
     */
    public String getOldHash() {
        return oldHash;
    }

    /**
     * Returns a hash for the current judgment enrichment tags 
     * (those generated for the judgment and those that refer to it).
     */
    public String getHash() {
        return hash;
    }

    /**
     * Returns true if the current judgment enrichment tags 
     * (for which {@link #getHash()} has been generated) have been processed.
     */
    public boolean isProcessed() {
        return processed;
    }


    //------------------------ SETTERS --------------------------
    
    public void setJudgmentId(long judgmentId) {
        this.judgmentId = judgmentId;
    }

    @SuppressWarnings("unused") /** for hibernate only */
    private void setOldHash(String oldHash) {
        this.oldHash = oldHash;
    }

    @SuppressWarnings("unused") /** for hibernate only */
    private void setHash(String hash) {
        this.hash = hash;
    }

    @SuppressWarnings("unused") /** for hibernate only */
    private void setProcessed(boolean processed) {
        this.processed = processed;
    }
}
