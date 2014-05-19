package pl.edu.icm.saos.model;

/**
 * pl. Hasła tematyczne/ słowa kluczowe
 * 
 * @author Łukasz Dumiszewski
 */

public class CommonCourtJudgmentKeyword {

    private String name;
    private CommonCourtJudgmentKeyword parent;

    
    //------------------------ GETTERS --------------------------
    
    public String getName() {
        return name;
    }

    public CommonCourtJudgmentKeyword getParent() {
        return parent;
    }


    
    //------------------------ SETTERS --------------------------
    
    public void setName(String name) {
        this.name = name;
    }

    public void setParent(CommonCourtJudgmentKeyword parent) {
        this.parent = parent;
    }
}
