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
 * Entity containing hashes enrichment tags related to judgment.
 * Hashes are used to determine if enrichment tags has changed
 * since last processing of it after their upload.
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
     * Updates hash value. Marks judgment enrichment tags as not processed if
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
     * Returns id of judgment
     */
    public long getJudgmentId() {
        return judgmentId;
    }

    /**
     * Returns old hash of judgment enrichment tags.
     * If {@link #isProcessed()} is false then it contains hash
     * for last processed enrichment tags (new enrichment tags are
     * in database, but was not processed yet)
     * If {@link #isProcessed()} is true then it contains hash
     * that is already outdated (new enrichment tags are in database
     * and their processing have ended).
     */
    public String getOldHash() {
        return oldHash;
    }

    /**
     * Returns hash of judgment enrichment tags.
     * If {@link #isProcessed()} is false then it contains
     * newly calculated hash for current enrichment tags that are in database,
     * but was not processed yet.
     * If {@link #isProcessed()} is true then it contains
     * hash for current enrichment tags and their processing have ended.
     */
    public String getHash() {
        return hash;
    }

    /**
     * Flag telling if judgment was processed with
     * last known enrichment tags.
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
