package pl.edu.icm.saos.enrichment.reference;

import java.util.List;

/**
 * Implementation of {@link TagJudgmentReferenceRemover} adding ability
 * to divide removing of judgment references into pages. 
 * 
 * @author madryk
 */
public class PageableDelegatingJudgmentReferenceRemover implements TagJudgmentReferenceRemover {
    
    private TagJudgmentReferenceRemover delegatedJudgmentReferenceRemover;

    private int pageSize = 1000; 
    
    
    //------------------------ CONSTRUCTORS --------------------------
    
    public PageableDelegatingJudgmentReferenceRemover(TagJudgmentReferenceRemover delegatedJudgmentReferenceRemover) {
        this.delegatedJudgmentReferenceRemover = delegatedJudgmentReferenceRemover;
    }

    
    //------------------------ LOGIC --------------------------

    @Override
    public void removeReferences(List<Long> judgmentIds) {

        int pageNr = 0;
        
        while (true) {
            int fromIndex = pageNr * pageSize;
            int toIndex = pageNr * pageSize + pageSize;
            
            if (fromIndex >= judgmentIds.size()) {
                break;
            }
            if (toIndex > judgmentIds.size()) {
                toIndex = judgmentIds.size();
            }
            
            delegatedJudgmentReferenceRemover.removeReferences(judgmentIds.subList(fromIndex, toIndex));
            
            pageNr++;
        }
    }

    
    //------------------------ SETTERS --------------------------
    
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    
}
