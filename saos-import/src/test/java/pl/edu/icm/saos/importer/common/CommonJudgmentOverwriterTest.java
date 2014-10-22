package pl.edu.icm.saos.importer.common;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.hamcrest.Matchers;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.powermock.reflect.Whitebox;

import pl.edu.icm.saos.common.testcommon.category.FastTest;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.CourtCase;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judge.JudgeRole;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.Judgment.JudgmentType;
import pl.edu.icm.saos.persistence.model.JudgmentSourceInfo;
import pl.edu.icm.saos.persistence.model.SourceCode;

import com.google.common.collect.Lists;

/**
 * @author Łukasz Dumiszewski
 */
@Category(FastTest.class)
public class CommonJudgmentOverwriterTest {

    private CommonJudgmentOverwriter judgmentOverwriter = new CommonJudgmentOverwriter();
 
    
    @Test
    public void overwriteJudgment_DONT_TOUCH_DB_FIELDS() {
        Judgment oldJudgment = createJudgment();
        int oldId = 132;
        int oldVer = 12;
        DateTime oldCreationDate = new DateTime(2012, 12, 12, 12, 12);
        Whitebox.setInternalState(oldJudgment, "id", oldId);
        Whitebox.setInternalState(oldJudgment, "ver", oldVer);
        Whitebox.setInternalState(oldJudgment, "creationDate", oldCreationDate);
        
        Judgment newJudgment = createJudgment();
        Whitebox.setInternalState(newJudgment, "id", oldId + 10);
        Whitebox.setInternalState(newJudgment, "ver", oldVer + 10);
        Whitebox.setInternalState(newJudgment, "creationDate", oldCreationDate.plusDays(12));
        
        judgmentOverwriter.overwriteJudgment(oldJudgment, newJudgment);
        
        assertEquals(oldId, oldJudgment.getId());
        assertEquals(oldVer, oldJudgment.getVer());
        assertEquals(oldCreationDate, oldJudgment.getCreationDate());
    }
    
    
    @Test
    public void overwriteJudgment_CourtCases() {
        Judgment oldJudgment = createJudgment();
        oldJudgment.addCourtCase(new CourtCase("123XD"));
        oldJudgment.addCourtCase(new CourtCase("123"));
        
        Judgment newJudgment = createJudgment();
        String newCaseNumber1 = "cxcxcxcxcxcxc";
        newJudgment.addCourtCase(new CourtCase(newCaseNumber1));
        String newCaseNumber2 = "123";
        newJudgment.addCourtCase(new CourtCase(newCaseNumber2));
        
        judgmentOverwriter.overwriteJudgment(oldJudgment, newJudgment);
        
        assertThat(newJudgment.getCaseNumbers(), Matchers.containsInAnyOrder(newCaseNumber1, newCaseNumber2));
        assertThat(oldJudgment.getCaseNumbers(), Matchers.containsInAnyOrder(newCaseNumber1, newCaseNumber2));
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
    public void overwriteJudgment_Judges() {
        Judgment oldJudgment = createJudgment();
        oldJudgment.addJudge(new Judge("Anna Nowak", JudgeRole.PRESIDING_JUDGE));
        oldJudgment.addJudge(new Judge("Katarzyna Oleksiak"));
        oldJudgment.addJudge(new Judge("Szymon Woźniak"));
        oldJudgment.addJudge(new Judge("Szymon Z"));
        
        Judgment newJudgment = createJudgment();
        newJudgment.addJudge(new Judge("Katarzyna Oleksiak", JudgeRole.PRESIDING_JUDGE));
        newJudgment.addJudge(new Judge("Anna Nowak"));
        newJudgment.addJudge(new Judge("Szymon Woźniak"));
        newJudgment.addJudge(new Judge("Szymon W"));
        
        judgmentOverwriter.overwriteJudgment(oldJudgment, newJudgment);
        
        assertEquals(1, newJudgment.getJudges(JudgeRole.PRESIDING_JUDGE).size());
        assertEquals("Katarzyna Oleksiak", newJudgment.getJudges(JudgeRole.PRESIDING_JUDGE).get(0).getName());
        assertTrue(newJudgment.containsJudge("Anna Nowak"));
        assertTrue(newJudgment.containsJudge("Szymon Woźniak"));
        assertTrue(newJudgment.containsJudge("Szymon W"));
        
        assertEquals(1, oldJudgment.getJudges(JudgeRole.PRESIDING_JUDGE).size());
        assertEquals("Katarzyna Oleksiak", oldJudgment.getJudges(JudgeRole.PRESIDING_JUDGE).get(0).getName());
        assertTrue(oldJudgment.containsJudge("Anna Nowak"));
        assertTrue(newJudgment.containsJudge("Szymon Woźniak"));
        assertTrue(newJudgment.containsJudge("Szymon W"));
        
    }
    
    @Test
    public void overwriteJudgment_CourtReporters() {
        Judgment oldJudgment = createJudgment();
        oldJudgment.addCourtReporter("sldslkdlsdsd");
        oldJudgment.addCourtReporter("ddsdsdsds");
        
        Judgment newJudgment = createJudgment();
        List<String> newCourtReporters = Lists.newArrayList("sldslkdlsdssd");
        newJudgment.addCourtReporter(newCourtReporters.get(0));
        
        judgmentOverwriter.overwriteJudgment(oldJudgment, newJudgment);
        
        assertEquals(newCourtReporters, oldJudgment.getCourtReporters());
        assertEquals(newCourtReporters, newJudgment.getCourtReporters());
    }
    
    
    @Test
    public void overwriteJudgment_LegalBases() {
        Judgment oldJudgment = createJudgment();
        oldJudgment.addLegalBase("sldslkdlsdsd");
        oldJudgment.addLegalBase("ddsdsdsds");
        
        Judgment newJudgment = createJudgment();
        List<String> newLegalBases = Lists.newArrayList("sldslkdlsdssd", "dsdsddsdddddd", "lllllsldlsdl");
        newJudgment.addLegalBase(newLegalBases.get(0));
        newJudgment.addLegalBase(newLegalBases.get(1));
        newJudgment.addLegalBase(newLegalBases.get(2));
        
        judgmentOverwriter.overwriteJudgment(oldJudgment, newJudgment);
        
        assertEquals(newLegalBases, oldJudgment.getLegalBases());
        assertEquals(newLegalBases, newJudgment.getLegalBases());
    }
    
    
    @Test
    public void overwriteJudgment_SourceInfo() {
        
        // old judgment
        Judgment oldJudgment = createJudgment();
        JudgmentSourceInfo oldSource = new JudgmentSourceInfo();
        oldSource.setPublicationDate(new DateTime(2003, 12, 23, 23, 23));
        oldSource.setPublisher("123dssds d sd");
        oldSource.setReviser("2323232escxcvxv3");
        oldSource.setSourceJudgmentId("212121212 2121 212 12 12");
        oldSource.setSourceJudgmentUrl("http://sdsdsd/sd/sdsd");
        oldSource.setSourceCode(SourceCode.ADMINISTRATIVE_COURT);
        oldJudgment.setSourceInfo(oldSource);
        
        
        // new judgment
        Judgment newJudgment = createJudgment();
        JudgmentSourceInfo newSource = new JudgmentSourceInfo();
        
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
        
        SourceCode newSourceType = SourceCode.COMMON_COURT;
        newSource.setSourceCode(newSourceType);
        
        newJudgment.setSourceInfo(newSource);
        
        
        // overwrite
        judgmentOverwriter.overwriteJudgment(oldJudgment, newJudgment);

        
        //assert
        
        JudgmentSourceInfoAssertUtils.assertSourceInfo(oldJudgment.getSourceInfo(), newJudgment.getSourceInfo(), newPublicationDate,
                newPublisher, newReviser, newSourceJudgmentId, newSourceUrl,
                newSourceType);
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private Judgment createJudgment() {
        Judgment judgment = new CommonCourtJudgment();
        return judgment;
    }

    
    
    

    
    
}
