package pl.edu.icm.saos.persistence.model;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.util.ObjectUtils;

import pl.edu.icm.saos.common.visitor.Visitor;
import pl.edu.icm.saos.persistence.common.IndexableObject;
import pl.edu.icm.saos.persistence.model.Judge.JudgeRole;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;


/**
 * 
 * pl. Orzeczenie
 * 
 * @author Łukasz Dumiszewski
 */
@Entity
@Table(indexes={@Index(columnList="indexed")}, uniqueConstraints={@UniqueConstraint(name="source_id_judgment_id_unique", columnNames={"sourceCode", "sourceJudgmentId"})})
@Inheritance(strategy = InheritanceType.JOINED)
@Cacheable(true)
@SequenceGenerator(name = "seq_judgment", allocationSize = 1, sequenceName = "seq_judgment")
public abstract class Judgment extends IndexableObject {

    /** pl. rodzaj wyroku */
    public enum JudgmentType {
        /** pl. postanowienie */
        DECISION,
        /** pl. uchwała */
        RESOLUTION,
        /** pl. wyrok */
        SENTENCE,
        /** pl. zarządzenie */
        REGULATION,
        /** pl. uzasadnienie, 
         * the common court judgment data provider sometimes gives the judgment and reasons as
           two separate entities. */
        REASONS
        
    }
    
    private JudgmentSourceInfo sourceInfo = new JudgmentSourceInfo();
    
    private LocalDate judgmentDate;
    
    private List<CourtCase> courtCases = Lists.newArrayList();
    
    private List<Judge> judges = Lists.newArrayList();
    private List<String> courtReporters = Lists.newArrayList(); 
    private String decision; 
    
    private String summary;
    
    private String textContent;
    
    private List<String> legalBases = Lists.newArrayList();
    private List<JudgmentReferencedRegulation> referencedRegulations = Lists.newArrayList();
    private List<JudgmentKeyword> keywords = Lists.newArrayList();
    
    private JudgmentType judgmentType;

    private DateTime modificationDate = new DateTime();
    
    private List<ReferencedCourtCase> referenceCourtCases = Lists.newArrayList();
    
    
    //------------------------ GETTERS --------------------------
    
    
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_judgment")
    @Override
    public long getId() {
        return id;
    }

    @Embedded
    public JudgmentSourceInfo getSourceInfo() {
        return sourceInfo;
    }
    

    /** pl. data orzeczenia */
    @Type(type="pl.edu.icm.saos.persistence.mapper.SaosPersistentLocalDate")
    public LocalDate getJudgmentDate() {
        return judgmentDate;
    }

    @OneToMany(mappedBy="judgment", orphanRemoval=true, cascade=CascadeType.ALL)
    private List<Judge> getJudges_() {
        return judges;
    }

    @Transient
    public List<Judge> getJudges() {
        return ImmutableList.copyOf(getJudges_());
    }
    
    /** pl. protokolanci */
    @ElementCollection
    @CollectionTable(name="judgment_court_reporter", uniqueConstraints={@UniqueConstraint(name="judgment_court_reporter_unique", columnNames={"fk_judgment", "court_reporter"})})
    @Column(name="court_reporter")
    private List<String> getCourtReporters_() {
        return courtReporters;
    }
    
    @Transient
    public List<String> getCourtReporters() {
        return ImmutableList.copyOf(getCourtReporters_());
    }
    
    /** pl. rozstrzygnięcie */
    public String getDecision() {
        return decision;
    }

    /** ruling summary, pl. teza */
    public String getSummary() {
        return summary;
    }

    /** pl. podstawy prawne */
    @ElementCollection
    @CollectionTable(name="judgment_legal_bases", uniqueConstraints={@UniqueConstraint(name="judgment_legal_base_unique", columnNames={"fk_judgment", "legal_base"})})
    @Column(name="legal_base")
    private List<String> getLegalBases_() {
        return legalBases;
    }
    
    @Transient
    public List<String> getLegalBases() {
        return ImmutableList.copyOf(getLegalBases_());
    }
    
    /**
     * Referenced law journal entries
     * pl. powołane przepisy
     * for hibernate
     */
    @OneToMany(mappedBy="judgment", orphanRemoval=true, cascade=CascadeType.ALL)
    private List<JudgmentReferencedRegulation> getReferencedRegulations_() {
        return referencedRegulations;
    }
    
    @Transient
    public List<JudgmentReferencedRegulation> getReferencedRegulations() {
        return ImmutableList.copyOf(getReferencedRegulations_());
    }

    
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "assigned_judgment_keyword",
            joinColumns = {@JoinColumn(name = "fk_judgment", nullable = false, updatable = false) }, 
            inverseJoinColumns = {@JoinColumn(name = "fk_keyword", nullable = false, updatable = false) })
    private List<JudgmentKeyword> getKeywords_() {
        return keywords;
    }
    
    
    @Transient
    public List<JudgmentKeyword> getKeywords() {
        return ImmutableList.copyOf(getKeywords_());
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

    @OneToMany(mappedBy="judgment", orphanRemoval=true, cascade=CascadeType.ALL)
    private List<CourtCase> getCourtCases_() {
        return courtCases;
    }
    
    @Transient
    public List<String> getCaseNumbers() {
        return ImmutableList.copyOf(getCourtCases_().stream().map(CourtCase::getCaseNumber).collect(Collectors.toList()));    
    }
    
    @Transient
    public List<CourtCase> getCourtCases() {
        return ImmutableList.copyOf(getCourtCases_());
    }

    
    @Transient
    public abstract CourtType getCourtType();

    /**
     * @return Judgment's last modification date
     */
    public DateTime getModificationDate() {
        return modificationDate;
    }

    @Transient
    public List<ReferencedCourtCase> getReferencedCourtCases() {
        return referenceCourtCases;
    }


    
    //------------------------ LOGIC --------------------------
    
    
    //--- court cases ---
    
    @Transient
    public boolean isSingleCourtCase() {
        return getCourtCases_().size() == 1;
    }
    
    public void addCourtCase(CourtCase courtCase) {
        Preconditions.checkNotNull(courtCase);
        Preconditions.checkArgument(!StringUtils.isBlank(courtCase.getCaseNumber()));
        Preconditions.checkArgument(!containsCourtCase(courtCase.getCaseNumber()));
        
        courtCase.setJudgment(this);
        this.courtCases.add(courtCase);
    }
    
    
    public CourtCase getCourtCase(String caseNumber) {
        for (CourtCase courtCase : this.courtCases) {
            if (courtCase.getCaseNumber().equals(caseNumber)) {
                return courtCase;
            }
        }
        return null;
    }
    
    
    public boolean containsCourtCase(String caseNumber) {
        return getCourtCase(caseNumber) != null;
    }
    
    
    public void removeCourtCase(CourtCase courtCase) {
        courtCases.remove(courtCase);
    }
    
    
    public void removeAllCourtCases() {
        courtCases.clear();
    }
    
    
    //--- judges ---
    
    public void addJudge(Judge judge) { 
        Preconditions.checkArgument(!this.containsJudge(judge.getName()));
        
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
    
    
    public boolean containsJudge(String judgeName) {
        return getJudge(judgeName) != null;
    }
    
    public Judge getJudge(String judgeName) {
        for (Judge existingJudge : judges) {
            if (StringUtils.equalsIgnoreCase(existingJudge.getName(), judgeName)) {
                return existingJudge;
            }
        }
        return null;
    }
    
    
    public void removeAllJudges() {
        judges.clear();
    }
    
    public void removeJudge(Judge judge) {
        judges.remove(judge);
    }
    
    
    //--- legal bases ---
    
    public void addLegalBase(String legalBase) {
        Preconditions.checkArgument(!containsLegalBase(legalBase));
        
        this.legalBases.add(legalBase);
    }

    public boolean containsLegalBase(String legalBase) {
        return this.legalBases.contains(legalBase);
    }
    
    public void removeLegalBase(String legalBase) {
        this.legalBases.remove(legalBase);
    }
    
    
    //--- court reporters ---
    
    public void addCourtReporter(String courtReporter) {
        Preconditions.checkArgument(!StringUtils.isBlank(courtReporter));
        Preconditions.checkArgument(!containsCourtReporter(courtReporter));
        
        this.courtReporters.add(courtReporter);
    }

    public boolean containsCourtReporter(String courtReporter) {
        return this.courtReporters.contains(courtReporter);
    }

    public void removeCourtReporter(String courtReporter) {
        this.courtReporters.remove(courtReporter);
    }
    
  
    //--- referenced regulations ---
    
    public void addReferencedRegulation(JudgmentReferencedRegulation regulation) {
        Preconditions.checkArgument(!this.containsReferencedRegulation(regulation));
        
        this.referencedRegulations.add(regulation);
        regulation.setJudgment(this);
    }

    public void removeAllReferencedRegulations() {
        referencedRegulations.clear();
    }
    
    public void removeReferencedRegulation(JudgmentReferencedRegulation regulation) {
        referencedRegulations.remove(regulation);
    }
    
    public boolean containsReferencedRegulation(JudgmentReferencedRegulation regulation) {
        Preconditions.checkNotNull(regulation);
        
        for (JudgmentReferencedRegulation referencedRegulation : getReferencedRegulations_()) {
            if (regulation.getRawText().equals(referencedRegulation.getRawText()) &&
                ObjectUtils.nullSafeEquals(regulation.getLawJournalEntry(), referencedRegulation.getLawJournalEntry())) {
                return true;
            }
        }
        return false;
    }
    

    //--- keywords ---
    
    public void addKeyword(JudgmentKeyword keyword) {
        Preconditions.checkArgument(!containsKeyword(keyword));
        Preconditions.checkArgument(keyword.getCourtType().equals(getCourtType()));
        
        this.keywords.add(keyword);
    }
    
    public void removeAllKeywords() {
        this.keywords.clear();
    }
    
    public void removeKeyword(JudgmentKeyword keyword) {
        this.keywords.remove(keyword);
    }
    
    @Transient
    public JudgmentKeyword getKeyword(String phrase) {
        for (JudgmentKeyword keyword : this.keywords) {
            if (keyword.getPhrase().equalsIgnoreCase(phrase)) {
                return keyword;
            }
        }
        return null;
    }
    
    public boolean containsKeyword(String phrase) {
        return getKeyword(phrase) != null;
    }
    
    public boolean containsKeyword(JudgmentKeyword keyword) {
        return keywords.contains(keyword);
    }
    
    
    //--- referenced court cases ---
    
    public boolean containsReferencedCourtCase(ReferencedCourtCase referencedCourtCase) {
        return this.referenceCourtCases.contains(referencedCourtCase);
    }
    
    
    public boolean addReferencedCourtCase(ReferencedCourtCase referencedCourtCase) {
        
        Preconditions.checkNotNull(referencedCourtCase);
        Preconditions.checkArgument(!containsReferencedCourtCase(referencedCourtCase));
        
        return this.referenceCourtCases.add(referencedCourtCase);
    }
    
    //--- other ---
    
    @Override
    public void passVisitorDown(Visitor visitor) {
        for (Judge judge : getJudges()) {
            judge.accept(visitor);
        }
        
        for (CourtCase courtCase : getCourtCases_()) {
            courtCase.accept(visitor);
        }
        
        for (JudgmentReferencedRegulation regulation : getReferencedRegulations()) {
            regulation.accept(visitor);
        }
    }

    @PreUpdate
    protected void onUpdate(){
        this.modificationDate = new DateTime();
    }
    
    //------------------------ SETTERS --------------------------
    
    

    public void setJudgmentDate(LocalDate judgmentDate) {
        this.judgmentDate = judgmentDate;
    }

   
    @SuppressWarnings("unused") /** for hibernate only */
    private void setJudges_(List<Judge> judges) {
        this.judges = judges;
    }

    @SuppressWarnings("unused") /** for hibernate only */
    private void setCourtReporters_(List<String> courtReporters) {
        this.courtReporters = courtReporters;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @SuppressWarnings("unused") /** for hibernate */
    private void setLegalBases_(List<String> legalBases) {
        this.legalBases = legalBases;
    }
    
    @SuppressWarnings("unused") /** for hibernate */
    private void setKeywords_(List<JudgmentKeyword> keywords) {
        this.keywords = keywords;
    }
    
    public void setJudgmentType(JudgmentType judgmentType) {
        this.judgmentType = judgmentType;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public void setSourceInfo(JudgmentSourceInfo sourceInfo) {
        this.sourceInfo = sourceInfo;
    }
    
    @SuppressWarnings("unused") /** for hibernate only */
    private void setCourtCases_(List<CourtCase> courtCases) {
        this.courtCases = courtCases;
    }

    @SuppressWarnings("unused") /** for hibernate only */
    private void setReferencedRegulations_(List<JudgmentReferencedRegulation> referencedRegulations) {
        this.referencedRegulations = referencedRegulations;
    }


    @SuppressWarnings("unused") /** for hibernate only */
    private void setModificationDate(DateTime modificationDate) {
        this.modificationDate = modificationDate;
    }
    

    


    
    //------------------------ HashCode & Equals --------------------------
    

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((sourceInfo == null) ? 0 : sourceInfo.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Judgment other = (Judgment) obj;
        if (sourceInfo == null) {
            if (other.sourceInfo != null)
                return false;
        } else if (!sourceInfo.equals(other.sourceInfo))
            return false;
        return true;
    }

    
    //------------------------ toString --------------------------
    
    @Override
    public String toString() {
        return "Judgment [sourceInfo=" + sourceInfo + ", judgmentDate="
                + judgmentDate + ", courtCases=" + courtCases + ", judges="
                + judges + ", courtReporters=" + courtReporters
                + ", legalBases=" + legalBases + ", referencedRegulations="
                + referencedRegulations + ", judgmentType=" + judgmentType
                + "]";
    }

    
   
    
}
