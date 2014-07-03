package pl.edu.icm.saos.persistence.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

/**
 * pl. Orzeczenie sądu powszechnego
 * 
 * @author Łukasz Dumiszewski
 */
@Entity
public class CommonCourtJudgment extends Judgment {
    
    private CommonCourtData courtData = new CommonCourtData();
    private List<CcJudgmentKeyword> keywords = Lists.newArrayList();
    
    
    //------------------------ GETTERS --------------------------
    
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "assigned_cc_judgment_keyword",
            joinColumns = {@JoinColumn(name = "fk_judgment", nullable = false, updatable = false) }, 
            inverseJoinColumns = {@JoinColumn(name = "fk_keyword", nullable = false, updatable = false) })
    public List<CcJudgmentKeyword> getKeywords() {
        return keywords;
    }

    @Embedded
    public CommonCourtData getCourtData() {
        return courtData;
    }
    
    
    //------------------------ LOGIC --------------------------
    
    public void addKeyword(CcJudgmentKeyword keyword) {
        Preconditions.checkArgument(!containsKeyword(keyword));
        
        this.keywords.add(keyword);
    }
    
    public void removeAllKeywords() {
        this.keywords.clear();
    }
    
    @Transient
    public CcJudgmentKeyword getKeyword(String phrase) {
        for (CcJudgmentKeyword keyword : this.keywords) {
            if (keyword.getPhrase().equalsIgnoreCase(phrase)) {
                return keyword;
            }
        }
        return null;
    }
    
    public boolean containsKeyword(String phrase) {
        return getKeyword(phrase) != null;
    }
    
    public boolean containsKeyword(CcJudgmentKeyword keyword) {
        return keywords.contains(keyword);
    }
        
    
    //------------------------ SETTERS --------------------------
    
    @SuppressWarnings("unused") /* for hibernate */
    private void setKeywords(List<CcJudgmentKeyword> keywords) {
        this.keywords = keywords;
    }

    public void setCourtData(CommonCourtData courtData) {
        this.courtData = courtData;
    }

   
}
