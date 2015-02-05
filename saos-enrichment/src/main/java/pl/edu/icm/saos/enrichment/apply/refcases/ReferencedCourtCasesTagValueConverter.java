package pl.edu.icm.saos.enrichment.apply.refcases;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.enrichment.apply.EnrichmentTagValueConverter;
import pl.edu.icm.saos.persistence.model.ReferencedCourtCase;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

/**
 * @author Łukasz Dumiszewski
 */
@Service("referencedCourtCasesTagValueConverter")
public class ReferencedCourtCasesTagValueConverter implements EnrichmentTagValueConverter<ReferencedCourtCasesTagValueItem[], List<ReferencedCourtCase>>{

    
    
    
    //------------------------ LOGIC --------------------------
    
    
    @Override
    public List<ReferencedCourtCase> convert(ReferencedCourtCasesTagValueItem[] referencedCourtCasesTagValueItems) {
        
        Preconditions.checkNotNull(referencedCourtCasesTagValueItems);
        
        
        List<ReferencedCourtCase> referencedCourtCases = Lists.newArrayList();
        
        for (ReferencedCourtCasesTagValueItem referencedCourtCaseTagValueItem: referencedCourtCasesTagValueItems) {
            
            referencedCourtCases.add(convert(referencedCourtCaseTagValueItem));
            
        }
        
        return referencedCourtCases;
        
    }
    
    
    
    //------------------------ PRIVATE --------------------------
    
    private ReferencedCourtCase convert(ReferencedCourtCasesTagValueItem referencedCasesTagValueItem) {
        
        ReferencedCourtCase referencedCourtCase = new ReferencedCourtCase();
        
        referencedCourtCase.setCaseNumber(StringUtils.trim(referencedCasesTagValueItem.getCaseNumber()));
        
        referencedCourtCase.setJudgmentIds(referencedCasesTagValueItem.getJudgmentIds());
        
        referencedCourtCase.markGenerated();
        
        return referencedCourtCase;
        
    }

    
}
