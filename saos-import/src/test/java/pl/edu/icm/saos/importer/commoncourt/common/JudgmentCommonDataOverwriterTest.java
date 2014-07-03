package pl.edu.icm.saos.importer.commoncourt.common;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Test;

import pl.edu.icm.saos.common.testUtils.ReflectionFieldSetter;
import pl.edu.icm.saos.importer.common.JudgmentCommonDataOverwriter;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.Judgment.JudgmentType;
import pl.edu.icm.saos.persistence.model.JudgmentReasoning;
import pl.edu.icm.saos.persistence.model.JudgmentSource;
import pl.edu.icm.saos.persistence.model.JudgmentSourceType;

import com.google.common.collect.Lists;

/**
 * @author Łukasz Dumiszewski
 */

public class JudgmentCommonDataOverwriterTest {

    private JudgmentCommonDataOverwriter judgmentOverwriter = new JudgmentCommonDataOverwriter();
 
    
    @Test
    public void overwriteJudgment_DONT_TOUCH_DB_FIELDS() {
        Judgment oldJudgment = createJudgment();
        int oldId = 132;
        int oldVer = 12;
        DateTime oldCreationDate = new DateTime(2012, 12, 12, 12, 12);
        ReflectionFieldSetter.setField(oldJudgment, "id", oldId);
        ReflectionFieldSetter.setField(oldJudgment, "ver", oldVer);
        ReflectionFieldSetter.setField(oldJudgment, "creationDate", oldCreationDate);
        
        Judgment newJudgment = createJudgment();
        ReflectionFieldSetter.setField(newJudgment, "id", oldId + 10);
        ReflectionFieldSetter.setField(newJudgment, "ver", oldVer + 10);
        ReflectionFieldSetter.setField(newJudgment, "creationDate", oldCreationDate.plusDays(12));
        
        judgmentOverwriter.overwriteJudgment(oldJudgment, newJudgment);
        
        assertEquals(oldId, oldJudgment.getId());
        assertEquals(oldVer, oldJudgment.getVer());
        assertEquals(oldCreationDate, oldJudgment.getCreationDate());
    }
    
    
    @Test
    public void overwriteJudgment_CaseNumber() {
        Judgment oldJudgment = createJudgment();
        oldJudgment.setCaseNumber("123XD");
        
        Judgment newJudgment = createJudgment();
        String newCaseNumber = "cxcxcxcxcxcxc";
        newJudgment.setCaseNumber(newCaseNumber);
        
        judgmentOverwriter.overwriteJudgment(oldJudgment, newJudgment);
        
        assertEquals(newCaseNumber, oldJudgment.getCaseNumber());
        assertEquals(newCaseNumber, newJudgment.getCaseNumber());
        
    }
    
    
    @Test
    public void overwriteJudgment_Decision() {
        Judgment oldJudgment = createJudgment();
        oldJudgment.setDecision("sdsdskldfald l3qkn dlkasn d");
        
        Judgment newJudgment = createJudgment();
        String newDecision = "sdsdsdsd s ds ds";
        newJudgment.setDecision(newDecision);
        
        judgmentOverwriter.overwriteJudgment(oldJudgment, newJudgment);
        
        assertEquals(newDecision, oldJudgment.getDecision());
        assertEquals(newDecision, newJudgment.getDecision());
        
        
    }
    
    
    @Test
    public void overwriteJudgment_Summary() {
        Judgment oldJudgment = createJudgment();
        oldJudgment.setSummary("sdsdskldfald l3qkn dlkasn d");
        
        Judgment newJudgment = createJudgment();
        String newSummary = "dcsdkcnl4k32l4nd ";
        newJudgment.setSummary(newSummary);
        
        judgmentOverwriter.overwriteJudgment(oldJudgment, newJudgment);
        
        assertEquals(newSummary, oldJudgment.getSummary());
        assertEquals(newSummary, newJudgment.getSummary());
    }
    
    @Test
    public void overwriteJudgment_JudgmentDate() {
        Judgment oldJudgment = createJudgment();
        oldJudgment.setJudgmentDate(new LocalDate(2012, 12, 12));
        
        Judgment newJudgment = createJudgment();
        LocalDate newJudgmentDate = new LocalDate(2012, 12, 14);
        newJudgment.setJudgmentDate(newJudgmentDate);
        
        judgmentOverwriter.overwriteJudgment(oldJudgment, newJudgment);
        
        assertEquals(newJudgmentDate, oldJudgment.getJudgmentDate());
        assertEquals(newJudgmentDate, newJudgment.getJudgmentDate());
    }
    
    
    @Test
    public void overwriteJudgment_JudgmentType() {
        Judgment oldJudgment = createJudgment();
        oldJudgment.setJudgmentType(JudgmentType.DECISION);
        
        Judgment newJudgment = createJudgment();
        JudgmentType newJudgmentType = JudgmentType.SENTENCE;
        newJudgment.setJudgmentType(newJudgmentType);
        
        judgmentOverwriter.overwriteJudgment(oldJudgment, newJudgment);
        
        assertEquals(newJudgmentType, oldJudgment.getJudgmentType());
        assertEquals(newJudgmentType, newJudgment.getJudgmentType());
    }
    
    
    @Test
    public void overwriteJudgment_TextContent() {
        Judgment oldJudgment = createJudgment();
        oldJudgment.setTextContent("salkd;lad a;dlks; d;sadkl ;l3ke ;lk;l344343kk34j3jh4j3h43 ");
        
        Judgment newJudgment = createJudgment();
        String newTextContent = "23232l32323230239 2030230293 029 302 3023\n03923";
        newJudgment.setTextContent(newTextContent);
        
        judgmentOverwriter.overwriteJudgment(oldJudgment, newJudgment);
        
        assertEquals(newTextContent, oldJudgment.getTextContent());
        assertEquals(newTextContent, newJudgment.getTextContent());
    }
    
    
    
    @Test
    public void overwriteJudgment_CourtReporters() {
        Judgment oldJudgment = createJudgment();
        oldJudgment.setCourtReporters(Lists.newArrayList("sldslkdlsdsd", "ddsdsdsds"));
        
        Judgment newJudgment = createJudgment();
        List<String> newCourtReporters = Lists.newArrayList("sldslkdlsdssd");
        newJudgment.setCourtReporters(newCourtReporters);
        
        judgmentOverwriter.overwriteJudgment(oldJudgment, newJudgment);
        
        assertEquals(newCourtReporters, oldJudgment.getCourtReporters());
        assertEquals(newCourtReporters, newJudgment.getCourtReporters());
    }
    
    
    @Test
    public void overwriteJudgment_LegalBases() {
        Judgment oldJudgment = createJudgment();
        oldJudgment.setLegalBases(Lists.newArrayList("sldslkdlsdsd", "ddsdsdsds"));
        
        Judgment newJudgment = createJudgment();
        List<String> newLegalBases = Lists.newArrayList("sldslkdlsdssd", "dsdsddsdddddd", "lllllsldlsdl");
        newJudgment.setLegalBases(newLegalBases);
        
        judgmentOverwriter.overwriteJudgment(oldJudgment, newJudgment);
        
        assertEquals(newLegalBases, oldJudgment.getLegalBases());
        assertEquals(newLegalBases, newJudgment.getLegalBases());
    }
    
    
    @Test
    public void overwriteJudgment_Reasoning() {
        Judgment oldJudgment = createJudgment();
        JudgmentReasoning oldReasoning = new JudgmentReasoning();
        oldReasoning.setPublicationDate(new DateTime());
        oldReasoning.setPublisher("233223232323");
        oldReasoning.setReviser("2323dssfdsf");
        oldReasoning.setText("2112121212wk,sjkdsdjckdjck xkj kdxc j");
        oldJudgment.setReasoning(oldReasoning);
        
        
        Judgment newJudgment = createJudgment();
        JudgmentReasoning newReasoning = new JudgmentReasoning();
        
        DateTime newPublicationDate = oldReasoning.getPublicationDate().minusDays(12);
        newReasoning.setPublicationDate(newPublicationDate);
        
        String newPublisher = oldReasoning.getPublisher() + "new";
        newReasoning.setPublisher(newPublisher);
        
        String newReviser = oldReasoning.getReviser() + "new";
        newReasoning.setReviser(newReviser);
        
        String newText = oldReasoning.getText() + "new";
        newReasoning.setText(newText);
        
        newJudgment.setReasoning(newReasoning);
        
        
        
        judgmentOverwriter.overwriteJudgment(oldJudgment, newJudgment);
        
        assertEquals(newPublicationDate, oldReasoning.getPublicationDate());
        assertEquals(newPublisher, oldReasoning.getPublisher());
        assertEquals(newReviser, oldReasoning.getReviser());
        assertEquals(newText, oldReasoning.getText());
        
        assertEquals(newReasoning, newJudgment.getReasoning());
        assertEquals(newPublicationDate, newReasoning.getPublicationDate());
        assertEquals(newPublisher, newReasoning.getPublisher());
        assertEquals(newReviser, newReasoning.getReviser());
        assertEquals(newText, newReasoning.getText());
        
    }
    
    
    @Test
    public void overwriteJudgment_JudgmentSource() {
        
        // old judgment
        Judgment oldJudgment = createJudgment();
        JudgmentSource oldSource = new JudgmentSource();
        oldSource.setPublicationDate(new DateTime(2003, 12, 23, 23, 23));
        oldSource.setPublisher("123dssds d sd");
        oldSource.setReviser("2323232escxcvxv3");
        oldSource.setSourceJudgmentId("212121212 2121 212 12 12");
        oldSource.setSourceJudgmentUrl("http://sdsdsd/sd/sdsd");
        oldSource.setSourceType(JudgmentSourceType.ADMINISTRATIVE_COURT);
        oldJudgment.setJudgmentSource(oldSource);
        
        
        // new judgment
        Judgment newJudgment = createJudgment();
        JudgmentSource newSource = new JudgmentSource();
        
        DateTime newPublicationDate = new DateTime(2003, 12, 23, 23, 23);
        newSource.setPublicationDate(newPublicationDate);
        
        String newPublisher = "12232w sds df sd s";
        newSource.setPublisher(newPublisher);
        
        String newReviser = "12232w sds df sd s";
        newSource.setReviser(newReviser);
        
        String newSourceJudgmentId = "212121212sd2121 212 12 12";
        newSource.setSourceJudgmentId(newSourceJudgmentId);
        
        String newSourceUrl = "http://sdsdssssssd/sd/sdsd";
        newSource.setSourceJudgmentUrl(newSourceUrl);
        
        JudgmentSourceType newSourceType = JudgmentSourceType.COMMON_COURT;
        newSource.setSourceType(newSourceType);
        
        newJudgment.setJudgmentSource(newSource);
        
        
        // overwrite
        judgmentOverwriter.overwriteJudgment(oldJudgment, newJudgment);

        
        //assert
        
        assertEquals(newPublicationDate, oldJudgment.getJudgmentSource().getPublicationDate());
        assertEquals(newPublisher, oldJudgment.getJudgmentSource().getPublisher());
        assertEquals(newReviser, oldJudgment.getJudgmentSource().getReviser());
        assertEquals(newSourceJudgmentId, oldJudgment.getJudgmentSource().getSourceJudgmentId());
        assertEquals(newSourceUrl, oldJudgment.getJudgmentSource().getSourceJudgmentUrl());
        assertEquals(newSourceType, oldJudgment.getJudgmentSource().getSourceType());
        
        assertEquals(newPublicationDate, newJudgment.getJudgmentSource().getPublicationDate());
        assertEquals(newPublisher, newJudgment.getJudgmentSource().getPublisher());
        assertEquals(newReviser, newJudgment.getJudgmentSource().getReviser());
        assertEquals(newSourceJudgmentId, newJudgment.getJudgmentSource().getSourceJudgmentId());
        assertEquals(newSourceUrl, newJudgment.getJudgmentSource().getSourceJudgmentUrl());
        assertEquals(newSourceType, newJudgment.getJudgmentSource().getSourceType());
    }
    
    
    
    
    private Judgment createJudgment() {
        Judgment judgment = new CommonCourtJudgment();
        return judgment;
    }

    
    
}
