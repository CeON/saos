package pl.edu.icm.saos.importer.commoncourt.process;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.joda.time.DateTime;
import org.junit.Test;

import pl.edu.icm.saos.importer.commoncourt.common.JudgmentSourceInfoAssertUtils;
import pl.edu.icm.saos.persistence.model.CcJudgmentKeyword;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.JudgmentReasoning;
import pl.edu.icm.saos.persistence.model.LawJournalEntry;
import pl.edu.icm.saos.persistence.model.JudgmentReferencedRegulation;
import pl.edu.icm.saos.persistence.model.SourceCode;

/**
 * @author Łukasz Dumiszewski
 */

public class CcjReasoningMergerTest {

    
    private CcjReasoningMerger ccjReasoningMerger = new CcjReasoningMerger();
    
    
    
    @Test
    public void mergeReasoning_Reasoning() {
        
        CommonCourtJudgment judgment = new CommonCourtJudgment();
        JudgmentReasoning oldReasoning = new JudgmentReasoning();
        oldReasoning.getSourceInfo().setPublicationDate(new DateTime());
        oldReasoning.getSourceInfo().setPublisher("233223232323");
        oldReasoning.getSourceInfo().setReviser("2323dssfdsf");
        oldReasoning.getSourceInfo().setSourceJudgmentId("212121212 2121 212 12 12");
        oldReasoning.getSourceInfo().setSourceJudgmentUrl("http://sdsdsd/sd/sdsd");
        oldReasoning.getSourceInfo().setSourceCode(SourceCode.ADMINISTRATIVE_COURT);
        oldReasoning.setText("2112121212wk,sjkdsdjckdjck xkj kdxc j");
        
        judgment.setReasoning(oldReasoning);
        
        
        CommonCourtJudgment reasoningJudgment = new CommonCourtJudgment();
        JudgmentReasoning newReasoning = new JudgmentReasoning();
        
        DateTime newPublicationDate = oldReasoning.getSourceInfo().getPublicationDate().minusDays(12);
        newReasoning.getSourceInfo().setPublicationDate(newPublicationDate);
        
        String newPublisher = oldReasoning.getSourceInfo().getPublisher() + "new";
        newReasoning.getSourceInfo().setPublisher(newPublisher);
        
        String newReviser = oldReasoning.getSourceInfo().getReviser() + "new";
        newReasoning.getSourceInfo().setReviser(newReviser);
        
        String newText = oldReasoning.getText() + "new";
        newReasoning.setText(newText);
        
        String newSourceJudgmentId = "212121212sd2121 212 12 12";
        newReasoning.getSourceInfo().setSourceJudgmentId(newSourceJudgmentId);
        
        String newSourceUrl = "http://sdsdssssssd/sd/sdsd";
        newReasoning.getSourceInfo().setSourceJudgmentUrl(newSourceUrl);
        
        SourceCode newSourceType = SourceCode.COMMON_COURT;
        newReasoning.getSourceInfo().setSourceCode(newSourceType);
        
        reasoningJudgment.setReasoning(newReasoning);
        
        
        
        ccjReasoningMerger.mergeReasoning(judgment, reasoningJudgment);
        
        assertEquals(newText, oldReasoning.getText());
        
        assertEquals(newReasoning, reasoningJudgment.getReasoning());
        assertEquals(newText, newReasoning.getText());
        
        JudgmentSourceInfoAssertUtils.assertSourceInfo(oldReasoning.getSourceInfo(), newReasoning.getSourceInfo(), newPublicationDate, newPublisher, newReviser, newSourceJudgmentId, newSourceUrl, newSourceType);
        
    }
    
    
    
    @Test
    public void mergeReasoning_LegalBases() {
        CommonCourtJudgment judgment = new CommonCourtJudgment();
        String legalBase1 = "legalBase1";
        String legalBase2 = "legalBase2";
        String legalBase3 = "legalBase3";
        String legalBase4 = "legalBase4";
        judgment.addLegalBase(legalBase1);
        judgment.addLegalBase(legalBase4);
        
        CommonCourtJudgment reasoningJudgment = new CommonCourtJudgment();
        reasoningJudgment.addLegalBase(legalBase1);
        reasoningJudgment.addLegalBase(legalBase2);
        reasoningJudgment.addLegalBase(legalBase3);
        
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
        
        assertTrue(reasoningJudgment.getKeyword("keyword1")==judgment.getKeyword("keyword1"));
        
    }
    
    
    @Test
    public void mergeReasoning_ReferencedRegulations() {
        CommonCourtJudgment judgment = new CommonCourtJudgment();
        
        JudgmentReferencedRegulation regulation1 = createRegulation("rawText1", "entryTitle1", 2011, 12, 2);
        JudgmentReferencedRegulation regulation2 = createRegulation("rawText2", "entryTitle2", 2012, 13, 2);
        JudgmentReferencedRegulation regulation3 = createRegulation("rawText3", "entryTitle3", 2013, 14, 2);
        
        judgment.addReferencedRegulation(regulation1);
        judgment.addReferencedRegulation(regulation2);
        judgment.addReferencedRegulation(regulation3);
        
        
        CommonCourtJudgment reasoningJudgment = new CommonCourtJudgment();
        JudgmentReferencedRegulation regulation4 = createRegulation("rawText4", "entryTitle4", 2011, 12, 2);
        JudgmentReferencedRegulation regulation5 = createRegulation("rawText2", "entryTitle2", 2012, 13, 2);
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



    private JudgmentReferencedRegulation createRegulation(String rawText, String title,
            int year, int journalNo, int entry) {
        LawJournalEntry lawJournalEntry = new LawJournalEntry();
        lawJournalEntry.setTitle(title);
        lawJournalEntry.setYear(year);
        lawJournalEntry.setJournalNo(journalNo);
        lawJournalEntry.setEntry(entry);
        JudgmentReferencedRegulation regulation1 = new JudgmentReferencedRegulation();
        regulation1.setRawText(rawText);
        regulation1.setLawJournalEntry(lawJournalEntry);
        return regulation1;
    }
    
    
}
