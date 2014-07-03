package pl.edu.icm.saos.importer.commoncourt.process;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.joda.time.DateTime;
import org.junit.Test;

import pl.edu.icm.saos.persistence.model.CcJudgmentKeyword;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.JudgmentReasoning;
import pl.edu.icm.saos.persistence.model.LawJournalEntry;
import pl.edu.icm.saos.persistence.model.ReferencedRegulation;

import com.google.common.collect.Lists;

/**
 * @author Łukasz Dumiszewski
 */

public class CcjReasoningMergerTest {

    
    private CcjReasoningMerger ccjReasoningMerger = new CcjReasoningMerger();
    
    
    
    @Test
    public void mergeReasoning_Reasoning() {
        
        CommonCourtJudgment judgment = new CommonCourtJudgment();
        JudgmentReasoning oldReasoning = new JudgmentReasoning();
        oldReasoning.setPublicationDate(new DateTime());
        oldReasoning.setPublisher("233223232323");
        oldReasoning.setReviser("2323dssfdsf");
        oldReasoning.setText("2112121212wk,sjkdsdjckdjck xkj kdxc j");
        judgment.setReasoning(oldReasoning);
        
        
        CommonCourtJudgment reasoningJudgment = new CommonCourtJudgment();
        JudgmentReasoning newReasoning = new JudgmentReasoning();
        
        DateTime newPublicationDate = oldReasoning.getPublicationDate().minusDays(12);
        newReasoning.setPublicationDate(newPublicationDate);
        
        String newPublisher = oldReasoning.getPublisher() + "new";
        newReasoning.setPublisher(newPublisher);
        
        String newReviser = oldReasoning.getReviser() + "new";
        newReasoning.setReviser(newReviser);
        
        String newText = oldReasoning.getText() + "new";
        newReasoning.setText(newText);
        
        reasoningJudgment.setReasoning(newReasoning);
        
        
        
        ccjReasoningMerger.mergeReasoning(judgment, reasoningJudgment);
        
        assertEquals(newPublicationDate, oldReasoning.getPublicationDate());
        assertEquals(newPublisher, oldReasoning.getPublisher());
        assertEquals(newReviser, oldReasoning.getReviser());
        assertEquals(newText, oldReasoning.getText());
        
        assertEquals(newReasoning, reasoningJudgment.getReasoning());
        assertEquals(newPublicationDate, newReasoning.getPublicationDate());
        assertEquals(newPublisher, newReasoning.getPublisher());
        assertEquals(newReviser, newReasoning.getReviser());
        assertEquals(newText, newReasoning.getText());
        
        
    }
    
    
    
    @Test
    public void mergeReasoning_LegalBases() {
        CommonCourtJudgment judgment = new CommonCourtJudgment();
        String legalBase1 = "legalBase1";
        String legalBase2 = "legalBase2";
        String legalBase3 = "legalBase3";
        String legalBase4 = "legalBase4";
        judgment.setLegalBases(Lists.newArrayList(legalBase1, legalBase4));
        
        CommonCourtJudgment reasoningJudgment = new CommonCourtJudgment();
        List<String> newLegalBases = Lists.newArrayList(legalBase1, legalBase2, legalBase3);
        reasoningJudgment.setLegalBases(newLegalBases);
        
        
        ccjReasoningMerger.mergeReasoning(judgment, reasoningJudgment);
        
        assertEquals(4, judgment.getLegalBases().size());
        assertTrue(judgment.containsLegalBase(legalBase1));
        assertTrue(judgment.containsLegalBase(legalBase2));
        assertTrue(judgment.containsLegalBase(legalBase3));
        assertTrue(judgment.containsLegalBase(legalBase4));
        
        assertEquals(3, reasoningJudgment.getLegalBases().size());
        assertTrue(reasoningJudgment.containsLegalBase(legalBase1));
        assertTrue(reasoningJudgment.containsLegalBase(legalBase2));
        assertTrue(reasoningJudgment.containsLegalBase(legalBase3));
        
    }
    
    
    @Test
    public void mergeReasoning_Keywords() {
        CommonCourtJudgment judgment = new CommonCourtJudgment();
        CcJudgmentKeyword keyword1 = new CcJudgmentKeyword("keyword1");
        CcJudgmentKeyword keyword2 = new CcJudgmentKeyword("keyword2");
        CcJudgmentKeyword keyword3 = new CcJudgmentKeyword("keyword3");
        CcJudgmentKeyword keyword4 = new CcJudgmentKeyword("keyword4");
        judgment.addKeyword(keyword1);
        judgment.addKeyword(keyword2);
        judgment.addKeyword(keyword3);
        judgment.addKeyword(keyword4);
        
        CommonCourtJudgment reasoningJudgment = new CommonCourtJudgment();
        reasoningJudgment.addKeyword(keyword1);
        reasoningJudgment.addKeyword(keyword2);
        reasoningJudgment.addKeyword(keyword3);
        
        
        ccjReasoningMerger.mergeReasoning(judgment, reasoningJudgment);
        
        assertEquals(4, judgment.getKeywords().size());
        assertTrue(judgment.containsKeyword(keyword1));
        assertTrue(judgment.containsKeyword(keyword2));
        assertTrue(judgment.containsKeyword(keyword3));
        assertTrue(judgment.containsKeyword(keyword4));
        
        assertEquals(3, reasoningJudgment.getKeywords().size());
        assertTrue(reasoningJudgment.containsKeyword(keyword1));
        assertTrue(reasoningJudgment.containsKeyword(keyword2));
        assertTrue(reasoningJudgment.containsKeyword(keyword3));
        
    }
    
    
    @Test
    public void mergeReasoning_ReferencedRegulations() {
        CommonCourtJudgment judgment = new CommonCourtJudgment();
        
        ReferencedRegulation regulation1 = createRegulation("rawText1", "entryTitle1", 2011, 12, 2);
        ReferencedRegulation regulation2 = createRegulation("rawText2", "entryTitle2", 2012, 13, 2);
        ReferencedRegulation regulation3 = createRegulation("rawText3", "entryTitle3", 2013, 14, 2);
        
        judgment.addReferencedRegulation(regulation1);
        judgment.addReferencedRegulation(regulation2);
        judgment.addReferencedRegulation(regulation3);
        
        
        CommonCourtJudgment reasoningJudgment = new CommonCourtJudgment();
        ReferencedRegulation regulation4 = createRegulation("rawText4", "entryTitle4", 2011, 12, 2);
        ReferencedRegulation regulation5 = createRegulation("rawText2", "entryTitle2", 2012, 13, 2);
        reasoningJudgment.addReferencedRegulation(regulation1);
        reasoningJudgment.addReferencedRegulation(regulation4);
        reasoningJudgment.addReferencedRegulation(regulation5);
        
        ccjReasoningMerger.mergeReasoning(judgment, reasoningJudgment);
        
        assertEquals(4, judgment.getReferencedRegulations().size());
        assertTrue(judgment.containsReferencedRegulation(regulation1));
        assertTrue(judgment.containsReferencedRegulation(regulation2));
        assertTrue(judgment.containsReferencedRegulation(regulation3));
        assertTrue(judgment.containsReferencedRegulation(regulation5));
        
        assertEquals(3, reasoningJudgment.getReferencedRegulations().size());
        assertTrue(reasoningJudgment.containsReferencedRegulation(regulation1));
        assertTrue(reasoningJudgment.containsReferencedRegulation(regulation4));
        assertTrue(reasoningJudgment.containsReferencedRegulation(regulation5));
        
    }



    private ReferencedRegulation createRegulation(String rawText, String title,
            int year, int journalNo, int entry) {
        LawJournalEntry lawJournalEntry = new LawJournalEntry();
        lawJournalEntry.setTitle(title);
        lawJournalEntry.setYear(year);
        lawJournalEntry.setJournalNo(journalNo);
        lawJournalEntry.setEntry(entry);
        ReferencedRegulation regulation1 = new ReferencedRegulation();
        regulation1.setRawText(rawText);
        regulation1.setLawJournalEntry(lawJournalEntry);
        return regulation1;
    }
    
    
}
