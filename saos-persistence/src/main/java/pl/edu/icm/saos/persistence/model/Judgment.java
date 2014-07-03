package pl.edu.icm.saos.persistence.model;

import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;
import org.joda.time.LocalDate;
import org.springframework.util.ObjectUtils;

import pl.edu.icm.saos.persistence.common.DataObject;
import pl.edu.icm.saos.persistence.model.Judge.JudgeRole;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;


/**
 * 
 * pl. Orzeczenie
 * 
 * @author Łukasz Dumiszewski
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Cacheable(true)
@SequenceGenerator(name = "seq_judgment", allocationSize = 1, sequenceName = "seq_judgment")
public abstract class Judgment extends DataObject {

    /** pl. rodzaj wyroku */
    public enum JudgmentType {
        /** pl. postanowienie */
        DECISION,
        /** pl. uchwała */
        RESOLUTION,
        /** pl. wyrok */
        SENTENCE
        
    }
    
    private JudgmentSource judgmentSource = new JudgmentSource();
    
    // sentence
    private String caseNumber;
    private LocalDate judgmentDate;
    private List<Judge> judges = Lists.newArrayList();;
    private List<String> courtReporters = Lists.newArrayList(); 
    private String decision; 
    
    private String summary;
    
    private JudgmentReasoning reasoning; 

    private String textContent;
    
    private List<String> legalBases = Lists.newArrayList();
    private List<ReferencedRegulation> referencedRegulations = Lists.newArrayList();
    
    private JudgmentType judgmentType;
    
    //------------------------ GETTERS --------------------------
    
    
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_judgment")
    @Override
    public int getId() {
        return id;
    }

    @Embedded
    public JudgmentSource getJudgmentSource() {
        return judgmentSource;
    }
    
    /** pl. sygnatura sprawy */
    public String getCaseNumber() {
        return caseNumber;
    }

    /** pl. data orzeczenia */
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDate")
    public LocalDate getJudgmentDate() {
        return judgmentDate;
    }

    @OneToMany(mappedBy="judgment", orphanRemoval=true, cascade=CascadeType.ALL)
    public List<Judge> getJudges() {
        return judges;
    }

    /** pl. protokolanci */
    @ElementCollection
    @CollectionTable(name="judgment_court_reporter")
    @Column(name="court_reporter")
    public List<String> getCourtReporters() {
        return courtReporters;
    }
    
    /** pl. rozstrzygnięcie */
    public String getDecision() {
        return decision;
    }

    /** ruling summary, pl. teza */
    public String getSummary() {
        return summary;
    }

    /** reasons for judgment, pl. uzasadnienie */
    @OneToOne(mappedBy="judgment", orphanRemoval=true, cascade=CascadeType.ALL)
    public JudgmentReasoning getReasoning() {
        return reasoning;
    }

    /** pl. podstawy prawne */
    @ElementCollection
    @CollectionTable(name="judgment_legal_bases")
    @Column(name="legal_base")
    public List<String> getLegalBases() {
        return legalBases;
    }
    
    /**
     * Referenced law journal entries
     * pl. powołane przepisy
     */
    @OneToMany(mappedBy="judgment", orphanRemoval=true, cascade=CascadeType.ALL)
    public List<ReferencedRegulation> getReferencedRegulations() {
        return referencedRegulations;
    }

    
    
    @Enumerated(EnumType.STRING)
    public JudgmentType getJudgmentType() {
        return judgmentType;
    }

    /** 
     * A full text content of the judgment  
     */
    public String getTextContent() {
        return textContent;
    }

    
    //------------------------ LOGIC --------------------------
    
    
    public void addJudge(Judge judge) { // how to treat the situation when there is more than one judge with the same name?
        this.judges.add(judge);
        judge.setJudgment(this);
    }
    
    
    /**
     * Returns {@link Judge}s that have the given role. If the role == null then returns judges that
     * have no special role assigned. See {@link Judge#getSpecialRoles()}. 
     */
    @Transient
    public List<Judge> getJudges(JudgeRole role) {
        List<Judge> roleJudges = Lists.newArrayList();
        for (Judge judge : getJudges()) {
            if (role != null && judge.hasAnySpecialRole()) {
                if (judge.getSpecialRoles().contains(role)) {
                    roleJudges.add(judge);
                }
            }
            else if (role == null && !judge.hasAnySpecialRole()) {
                roleJudges.add(judge);
            }
        }
        return roleJudges;
    }
    
    
    public void removeAllJudges() {
        judges.clear();
    }
    
    
    
    public void addLegalBase(String legalBase) {
        Preconditions.checkArgument(!containsLegalBase(legalBase));
        
        this.legalBases.add(legalBase);
    }

    public boolean containsLegalBase(String legalBase) {
        return this.legalBases.contains(legalBase);
    }
    
    
    
    public void addReferencedRegulation(ReferencedRegulation regulation) {
        Preconditions.checkArgument(!this.containsReferencedRegulation(regulation));
        
        this.referencedRegulations.add(regulation);
        regulation.setJudgment(this);
    }

    public void removeAllReferencedRegulations() {
        referencedRegulations.clear();
    }
    
    public boolean containsReferencedRegulation(ReferencedRegulation regulation) {
        Preconditions.checkNotNull(regulation);
        
        for (ReferencedRegulation referencedRegulation : getReferencedRegulations()) {
            if (regulation.getRawText().equals(referencedRegulation.getRawText()) &&
                ObjectUtils.nullSafeEquals(regulation.getLawJournalEntry(), referencedRegulation.getLawJournalEntry())) {
                return true;
            }
        }
        return false;
    }
    
    //------------------------ SETTERS --------------------------
    
    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }

    public void setJudgmentDate(LocalDate judgmentDate) {
        this.judgmentDate = judgmentDate;
    }

   
    @SuppressWarnings("unused") // for hibernate only
    private void setJudges(List<Judge> judges) {
        this.judges = judges;
    }

    public void setCourtReporters(List<String> courtReporters) {
        this.courtReporters = courtReporters;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setReasoning(JudgmentReasoning reasoning) {
        if (reasoning != null) {
            reasoning.setJudgment(this);
        }
        this.reasoning = reasoning;
    }

    public void setLegalBases(List<String> legalBases) {
        this.legalBases = legalBases;
    }

    public void setJudgmentType(JudgmentType judgmentType) {
        this.judgmentType = judgmentType;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public void setJudgmentSource(JudgmentSource judgmentSource) {
        this.judgmentSource = judgmentSource;
    }

    @SuppressWarnings("unused") // for hibernate only
    private void setReferencedRegulations(List<ReferencedRegulation> referencedRegulations) {
        this.referencedRegulations = referencedRegulations;
    }


    
}
