package pl.edu.icm.saos.enrichment.apply.refcases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import pl.edu.icm.saos.persistence.model.ReferencedCourtCase;

import com.google.common.collect.Lists;

/**
 * @author Łukasz Dumiszewski
 */

public class ReferencedCourtCasesTagValueConverterTest {

    
    private ReferencedCourtCasesTagValueConverter referencedCourtCasesTagValueConverter = new ReferencedCourtCasesTagValueConverter();
    
    
    
    
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void convert() {
        
        // given
        ReferencedCourtCasesTagValueItem valueItem1 = new ReferencedCourtCasesTagValueItem();
        valueItem1.setCaseNumber("YYY ");
        valueItem1.setJudgmentIds(Lists.newArrayList(1234l, 123l));
        
        ReferencedCourtCasesTagValueItem valueItem2 = new ReferencedCourtCasesTagValueItem();
        valueItem2.setCaseNumber("SSS");
        valueItem2.setJudgmentIds(null);
        
        ReferencedCourtCasesTagValueItem[] referencedCourtCasesTagValueItems = new ReferencedCourtCasesTagValueItem[] {valueItem1, valueItem2};
        
        
        // execute
        List<ReferencedCourtCase> referencedCourtCases = referencedCourtCasesTagValueConverter.convert(referencedCourtCasesTagValueItems);
        
        
        // assert
        assertEquals(2, referencedCourtCases.size());
        
        assertEquals(valueItem1.getCaseNumber().trim(), referencedCourtCases.get(0).getCaseNumber());
        assertEquals(valueItem1.getJudgmentIds(), referencedCourtCases.get(0).getJudgmentIds());
        assertTrue(referencedCourtCases.get(0).isGenerated());
        
        assertEquals(valueItem2.getCaseNumber(), referencedCourtCases.get(1).getCaseNumber());
        assertEquals(valueItem2.getJudgmentIds(), referencedCourtCases.get(1).getJudgmentIds());
        assertTrue(referencedCourtCases.get(1).isGenerated());
    }
    
    
}
