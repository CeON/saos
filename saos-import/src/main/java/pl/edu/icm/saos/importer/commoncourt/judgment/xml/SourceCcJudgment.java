package pl.edu.icm.saos.importer.commoncourt.judgment.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import pl.edu.icm.saos.common.xml.JaxbStringToListAdapter;

/**
 * DTO containing judgment data imported from an external source
 * 
 * @author Łukasz Dumiszewski
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "judgement")
public class SourceCcJudgment {
    
    @XmlAttribute(required = true)
    private String id;

    @XmlElement(required = true)
    private String signature;
    
    @XmlElement(required = true, name="date")
    @XmlJavaTypeAdapter(CcJaxbJodaLocalDateAdapter.class)
    private LocalDate judgmentDate;
    
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CcJaxbJodaDateTimeAdapter.class)
    private DateTime publicationDate;
                 
    @XmlElement(required = true)
    private String courtId;
    
    @XmlElement(required = true)
    private String departmentId;
    
    @XmlElement(name="type")
    @XmlJavaTypeAdapter(JaxbStringToListAdapter.class)
    private List<String> types;

    @XmlElement
    private String chairman;
    
    @XmlElementWrapper(name="judges")
    @XmlElement(name="judge")
    private List<String> judges;
    
    @XmlElementWrapper(name="themePhrases")
    @XmlElement(name="themePhrase")
    private List<String> themePhrases;
    
    @XmlElementWrapper(name="references")
    @XmlElement(name="reference")
    private List<String> references;
    
    @XmlElementWrapper(name="legalBases")
    @XmlElement(name="legalBasis")
    private List<String> legalBases;
    
    @XmlElement
    private String recorder;
    
    @XmlElement
    private String decision;
    
    @XmlElement
    private String reviser;
    
    @XmlElement
    private String publisher;
    
    @XmlElement
    private String thesis;
    
    
    private String textContent;
    
    private String sourceUrl;
    
    
    //------------------------ GETTERS --------------------------
    
    public String getSignature() {
        return signature;
    }

    public LocalDate getJudgmentDate() {
        return judgmentDate;
    }

    public DateTime getPublicationDate() {
        return publicationDate;
    }

    public String getCourtId() {
        return courtId;
    }

    public String getDepartmentId() {
        return departmentId;
    }
    
    public List<String> getTypes() {
        return types;
    }

    public String getChairman() {
        return chairman;
    }

    public List<String> getJudges() {
        return judges;
    }

    public List<String> getThemePhrases() {
        return themePhrases;
    }

    public List<String> getReferences() {
        return references;
    }

    public List<String> getLegalBases() {
        return legalBases;
    }
    
    public String getRecorder() {
        return recorder;
    }

    public String getDecision() {
        return decision;
    }

    public String getReviser() {
        return reviser;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getId() {
        return id;
    }
    
    public String getThesis() {
        return thesis;
    }

    public String getTextContent() {
        return textContent;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    
    
    
    
    //------------------------ LOGIC --------------------------
    
    @Override
    public String toString() {
        return "SourceCcJudgment [id=" + id + ", signature=" + signature
                + ", judgmentDate=" + judgmentDate + ", publicationDate="
                + publicationDate + ", courtId=" + courtId + ", departmentId="
                + departmentId + ", types=" + types + ", chairman=" + chairman
                + ", judges=" + judges + ", themePhrases=" + themePhrases
                + ", references=" + references + ", legalBases=" + legalBases
                + ", recorder=" + recorder + ", decision=" + decision
                + ", reviser=" + reviser + ", publisher=" + publisher
                + ", thesis=" + thesis + ", textContent=(...)" 
                + ", sourceUrl=" + sourceUrl + "]";
    }
    
    
    //------------------------ SETTERS --------------------------
    
    public void setSignature(String signature) {
        this.signature = signature;
    }
    
    public void setJudgmentDate(LocalDate judgmentDate) {
        this.judgmentDate = judgmentDate;
    }

    public void setPublicationDate(DateTime publicationDate) {
        this.publicationDate = publicationDate;
    }

    public void setCourtId(String courtId) {
        this.courtId = courtId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public void setChairman(String chairman) {
        this.chairman = chairman;
    }

    public void setJudges(List<String> judges) {
        this.judges = judges;
    }

    public void setThemePhrases(List<String> themePhrases) {
        this.themePhrases = themePhrases;
    }

    public void setReferences(List<String> references) {
        this.references = references;
    }

    public void setLegalBases(List<String> legalBases) {
        this.legalBases = legalBases;
    }

    public void setRecorder(String recorder) {
        this.recorder = recorder;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public void setReviser(String reviser) {
        this.reviser = reviser;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setThesis(String thesis) {
        this.thesis = thesis;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }


}
