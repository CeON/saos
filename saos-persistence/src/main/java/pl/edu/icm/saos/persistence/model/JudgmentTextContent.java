package pl.edu.icm.saos.persistence.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang3.StringUtils;

import pl.edu.icm.saos.persistence.common.DataObject;

/**
 * Model class for judgment content
 * 
 * @author madryk
 */
@Entity
@Table( uniqueConstraints = { @UniqueConstraint(columnNames = {"fk_judgment"})})
@SequenceGenerator(name = "seq_judgment_text_content", allocationSize = 1, sequenceName = "seq_judgment_text_content")
public class JudgmentTextContent extends DataObject {

    private Judgment judgment;
    private String rawTextContent;
    private ContentType type;
    private String filePath;
    
    /**
     * Defines type of content.
     */
    public enum ContentType {
        /** Portable Document Format */
        PDF,
        /** Microsoft Word Binary File Format */
        DOC,
        /** HyperText Markup Language */
        HTML
    }
    
    
    //------------------------ GETTERS --------------------------
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_judgment_text_content")
    @Override
    public long getId() {
        return id;
    }
    
    /**
     * Judgment associated with this text content
     */
    @OneToOne
    public Judgment getJudgment() {
        return judgment;
    }
    
    /**
     * Textual content of judgment represented as
     * {@literal String}.
     * It can contain html tags used for formatting.
     */
    public String getRawTextContent() {
        return rawTextContent;
    }
    
    /**
     * Type of content.
     * i.e. {@link ContentType#DOC} for Microsoft Word format
     */
    @Enumerated(EnumType.STRING)
    public ContentType getType() {
        return type;
    }
    
    /**
     * Location of external content file.
     */
    public String getFilePath() {
        return filePath;
    }
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Returns true if there is an external original file with the judgment content.
     */
    @Transient
    public boolean isContentInFile() {
        return StringUtils.isNotBlank(filePath);
    }
    
    
    //------------------------ SETTERS --------------------------
    
    public void setJudgment(Judgment judgment) {
        this.judgment = judgment;
    }
    
    public void setRawTextContent(String rawTextContent) {
        this.rawTextContent = rawTextContent;
    }
    
    public void setType(ContentType type) {
        this.type = type;
    }
    
    public void setFilePath(String path) {
        this.filePath = path;
    }

}
