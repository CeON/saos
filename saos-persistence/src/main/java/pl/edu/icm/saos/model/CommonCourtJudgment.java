package pl.edu.icm.saos.model;

import java.util.List;

/**
 * pl. Orzeczenie sądu powszechnego
 * 
 * @author Łukasz Dumiszewski
 */

public class CommonCourtJudgment extends Judgment {
    
    private CommonCourtDivision commonCourtDivision;
    private List<CommonCourtJudgmentKeyword> keywords;
    
    
    //------------------------ GETTERS --------------------------
    
    public List<CommonCourtJudgmentKeyword> getKeywords() {
        return keywords;
    }
    
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
