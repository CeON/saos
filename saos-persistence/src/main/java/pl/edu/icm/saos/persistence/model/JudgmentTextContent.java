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
    private String path;
    
    /**
     * Defines type of content stored under {@link JudgmentTextContent#getPath()} 
     */
    public enum ContentType {
        PDF,
        DOC
    }
    
    
    //------------------------ GETTERS --------------------------
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_judgment_text_content")
    @Override
    public long getId() {
        return id;
    }
    
    @OneToOne
    public Judgment getJudgment() {
        return judgment;
    }
    
    public String getRawTextContent() {
        return rawTextContent;
    }
    
    @Enumerated(EnumType.STRING)
    public ContentType getType() {
        return type;
    }
    
    public String getPath() {
        return path;
    }
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Tells if with content is associated some external file.
     */
    public boolean haveExternalContent() {
        return StringUtils.isNotBlank(path);
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
    
    public void setPath(String path) {
        this.path = path;
    }

}
