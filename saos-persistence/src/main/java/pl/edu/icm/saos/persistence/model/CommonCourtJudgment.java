package pl.edu.icm.saos.persistence.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

/**
 * pl. Orzeczenie sądu powszechnego
 * 
 * @author Łukasz Dumiszewski
 */
@Entity
public class CommonCourtJudgment extends Judgment {
    
    private CommonCourtDivision commonCourtDivision;
    private List<CommonCourtJudgmentKeyword> keywords;
    
    
    //------------------------ GETTERS --------------------------
    
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "assigned_cc_judgment_keyword",
            joinColumns = {@JoinColumn(name = "fk_judgment", nullable = false, updatable = false) }, 
            inverseJoinColumns = {@JoinColumn(name = "fk_keyword", nullable = false, updatable = false) })
    public List<CommonCourtJudgmentKeyword> getKeywords() {
        return keywords;
    }
    
    @ManyToOne
    public CommonCourtDivision getCommonCourtDivision() {
        return commonCourtDivision;
    }

    
    
    //------------------------ SETTERS --------------------------
    
    public void setKeywords(List<CommonCourtJudgmentKeyword> keywords) {
        this.keywords = keywords;
    }

    public void setCommonCourtDivision(CommonCourtDivision commonCourtDivision) {
        this.commonCourtDivision = commonCourtDivision;
    }
}
