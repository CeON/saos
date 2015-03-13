package pl.edu.icm.saos.importer.common.overwriter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static pl.edu.icm.saos.persistence.correction.model.CorrectedProperty.NAME;

import java.util.List;

import org.hamcrest.Matchers;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.powermock.reflect.Whitebox;

import pl.edu.icm.saos.common.testcommon.category.FastTest;
import pl.edu.icm.saos.importer.common.JudgmentSourceInfoAssertUtils;
import pl.edu.icm.saos.importer.common.correction.ImportCorrection;
import pl.edu.icm.saos.importer.common.correction.ImportCorrectionBuilder;
import pl.edu.icm.saos.importer.common.correction.ImportCorrectionList;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.CourtCase;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.JudgmentTextContent;
import pl.edu.icm.saos.persistence.model.Judge.JudgeRole;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.Judgment.JudgmentType;
import pl.edu.icm.saos.persistence.model.JudgmentSourceInfo;
import pl.edu.icm.saos.persistence.model.JudgmentTextContent.ContentType;
import pl.edu.icm.saos.persistence.model.SourceCode;

import com.google.common.collect.Lists;

/**
 * @author Łukasz Dumiszewski
 */
@Category(FastTest.class)
public class CommonJudgmentOverwriterTest {

    private CommonJudgmentOverwriter judgmentOverwriter = new CommonJudgmentOverwriter();
 
    private ImportCorrectionList correctionList = new ImportCorrectionList();
    
    private Judgment oldJudgment = new CommonCourtJudgment();
    
    private Judgment newJudgment = new CommonCourtJudgment();
    
    //------------------------ LOGIC --------------------------
    
    @Test
    public void overwriteJudgment_DONT_TOUCH_DB_FIELDS() {
        
        int oldId = 132;
        int oldVer = 12;
        DateTime oldCreationDate = new DateTime(2012, 12, 12, 12, 12);
        Whitebox.setInternalState(oldJudgment, "id", oldId);
        Whitebox.setInternalState(oldJudgment, "ver", oldVer);
        Whitebox.setInternalState(oldJudgment, "creationDate", oldCreationDate);
        
        Whitebox.setInternalState(newJudgment, "id", oldId + 10);
        Whitebox.setInternalState(newJudgment, "ver", oldVer + 10);
        Whitebox.setInternalState(newJudgment, "creationDate", oldCreationDate.plusDays(12));
        
        judgmentOverwriter.overwriteJudgment(oldJudgment, newJudgment, correctionList);
        
        assertEquals(oldId, oldJudgment.getId());
        assertEquals(oldVer, oldJudgment.getVer());
        assertEquals(oldCreationDate, oldJudgment.getCreationDate());
    }
    
    
    @Test
    public void overwriteJudgment_CourtCases() {
        
        oldJudgment.addCourtCase(new CourtCase("123XD"));
        oldJudgment.addCourtCase(new CourtCase("123"));
        
        String newCaseNumber1 = "cxcxcxcxcxcxc";
        newJudgment.addCourtCase(new CourtCase(newCaseNumber1));
        String newCaseNumber2 = "123";
        newJudgment.addCourtCase(new CourtCase(newCaseNumber2));
        
        judgmentOverwriter.overwriteJudgment(oldJudgment, newJudgment, correctionList);
        
        assertThat(newJudgment.getCaseNumbers(), Matchers.containsInAnyOrder(newCaseNumber1, newCaseNumber2));
        assertThat(oldJudgment.getCaseNumbers(), Matchers.containsInAnyOrder(newCaseNumber1, newCaseNumber2));
    }
    
    
    @Test
    public void overwriteJudgment_Decision() {
        
        oldJudgment.setDecision("sdsdskldfald l3qkn dlkasn d");
        
        String newDecision = "sdsdsdsd s ds ds";
        newJudgment.setDecision(newDecision);
        
        judgmentOverwriter.overwriteJudgment(oldJudgment, newJudgment, correctionList);
        
        assertEquals(newDecision, oldJudgment.getDecision());
        assertEquals(newDecision, newJudgment.getDecision());
        
        
    }
    
    
    @Test
    public void overwriteJudgment_Summary() {
        oldJudgment.setSummary("sdsdskldfald l3qkn dlkasn d");
        
        String newSummary = "dcsdkcnl4k32l4nd ";
        newJudgment.setSummary(newSummary);
        
        judgmentOverwriter.overwriteJudgment(oldJudgment, newJudgment, correctionList);
        
        assertEquals(newSummary, oldJudgment.getSummary());
        assertEquals(newSummary, newJudgment.getSummary());
    }
    
    @Test
    public void overwriteJudgment_JudgmentDate() {
        oldJudgment.setJudgmentDate(new LocalDate(2012, 12, 12));
        
        LocalDate newJudgmentDate = new LocalDate(2012, 12, 14);
        newJudgment.setJudgmentDate(newJudgmentDate);
        
        judgmentOverwriter.overwriteJudgment(oldJudgment, newJudgment, correctionList);
        
        assertEquals(newJudgmentDate, oldJudgment.getJudgmentDate());
        assertEquals(newJudgmentDate, newJudgment.getJudgmentDate());
    }
    
    
    @Test
    public void overwriteJudgment_JudgmentType() {
        oldJudgment.setJudgmentType(JudgmentType.DECISION);
        
        JudgmentType newJudgmentType = JudgmentType.SENTENCE;
        newJudgment.setJudgmentType(newJudgmentType);
        
        judgmentOverwriter.overwriteJudgment(oldJudgment, newJudgment, correctionList);
        
        assertEquals(newJudgmentType, oldJudgment.getJudgmentType());
        assertEquals(newJudgmentType, newJudgment.getJudgmentType());
    }
    
    
    @Test
    public void overwriteJudgment_TextContent() {
        JudgmentTextContent oldTextContent = new JudgmentTextContent();
        oldTextContent.setRawTextContent("salkd;lad a;dlks; d;sadkl ;l3ke ;lk;l344343kk34j3jh4j3h43 ");
        oldTextContent.setType(ContentType.PDF);
        oldTextContent.setFilePath("/old/file/path.pdf");
        
        oldJudgment.setTextContent(oldTextContent);
        
        JudgmentTextContent newTextContent = new JudgmentTextContent();
        newTextContent.setRawTextContent("23232l32323230239 2030230293 029 302 3023\n03923");
        newTextContent.setType(ContentType.DOC);
        newTextContent.setFilePath("/new/file/path.doc");
        
        newJudgment.setTextContent(newTextContent);
        
        judgmentOverwriter.overwriteJudgment(oldJudgment, newJudgment, correctionList);
        
        assertEquals(newTextContent.getRawTextContent(), oldJudgment.getRawTextContent());
        assertEquals(newTextContent.getType(), oldJudgment.getTextContent().getType());
        assertEquals(newTextContent.getFilePath(), oldJudgment.getTextContent().getFilePath());
        
        assertEquals(newTextContent.getRawTextContent(), newJudgment.getRawTextContent());
        assertEquals(newTextContent.getType(), newJudgment.getTextContent().getType());
        assertEquals(newTextContent.getFilePath(), newJudgment.getTextContent().getFilePath());
    }
    
    @Test
    public void overwriteJudgment_Judges() {
        
        // given
        
        Judge oldAnnaNowak = new Judge("Anna Nowak", JudgeRole.PRESIDING_JUDGE);
        oldJudgment.addJudge(oldAnnaNowak);
        oldJudgment.addJudge(new Judge("Katarzyna Oleksiak"));
        oldJudgment.addJudge(new Judge("Szymon Woźniak"));
        oldJudgment.addJudge(new Judge("Szymon Z"));
        
        newJudgment.addJudge(new Judge("Katarzyna Oleksiak", JudgeRole.PRESIDING_JUDGE));
        Judge newAnnaNowak = new Judge("Anna Nowak");
        newJudgment.addJudge(newAnnaNowak);
        newJudgment.addJudge(new Judge("Szymon Woźniak"));
        newJudgment.addJudge(new Judge("Szymon W"));
        
        ImportCorrection judgeNameCorrection = ImportCorrectionBuilder.createUpdate(newAnnaNowak).ofProperty(NAME).oldValue("Sędzia Anna Nowak").newValue(newAnnaNowak.getName()).build(); 
        correctionList.addCorrection(judgeNameCorrection);
        
     
        // execute
        
        judgmentOverwriter.overwriteJudgment(oldJudgment, newJudgment, correctionList);
        
        
        // assert
        
        assertEquals(1, newJudgment.getJudges(JudgeRole.PRESIDING_JUDGE).size());
        assertEquals("Katarzyna Oleksiak", newJudgment.getJudges(JudgeRole.PRESIDING_JUDGE).get(0).getName());
        assertTrue(newJudgment.containsJudge("Anna Nowak"));
        assertTrue(newJudgment.containsJudge("Szymon Woźniak"));
        assertTrue(newJudgment.containsJudge("Szymon W"));
        
        assertEquals(1, oldJudgment.getJudges(JudgeRole.PRESIDING_JUDGE).size());
        assertEquals("Katarzyna Oleksiak", oldJudgment.getJudges(JudgeRole.PRESIDING_JUDGE).get(0).getName());
        assertTrue(oldJudgment.containsJudge("Anna Nowak"));
        assertTrue(oldJudgment.containsJudge("Szymon Woźniak"));
        assertTrue(oldJudgment.containsJudge("Szymon W"));
        
        
        // check if the reference to a corrected object in the import correction has changed
        assertTrue(correctionList.hasImportCorrection(judgeNameCorrection));
        assertTrue(judgeNameCorrection.getCorrectedObject() == oldAnnaNowak);
    }
    
    
    @Test
    public void overwriteJudgment_Judges_NameSameRolesChanged() {
        
        // given
        
        Judge annaNowakOld = new Judge("Anna Nowak", JudgeRole.PRESIDING_JUDGE);
        Judge janNowakOld = new Judge("Jan Nowak", JudgeRole.REPORTING_JUDGE);
        
        oldJudgment.addJudge(janNowakOld);
        oldJudgment.addJudge(annaNowakOld);
        
        
        Judge annaNowakNew = new Judge("Anna Nowak", JudgeRole.REPORTING_JUDGE);
        Judge janNowakNew = new Judge("Jan Nowak", JudgeRole.PRESIDING_JUDGE, JudgeRole.REASONS_FOR_JUDGMENT_AUTHOR);

        newJudgment.addJudge(annaNowakNew);
        newJudgment.addJudge(janNowakNew);
        
        
        // execute
        
        judgmentOverwriter.overwriteJudgment(oldJudgment, newJudgment, correctionList);
        
        
        // assert
        
        assertEquals(2, oldJudgment.getJudges().size());
        assertTrue(annaNowakOld == oldJudgment.getJudge(annaNowakNew.getName()));
        assertTrue(janNowakOld == oldJudgment.getJudge(janNowakNew.getName()));
        assertThat(oldJudgment.getJudge(annaNowakNew.getName()).getSpecialRoles(), Matchers.containsInAnyOrder(JudgeRole.REPORTING_JUDGE));
        assertThat(oldJudgment.getJudge(janNowakNew.getName()).getSpecialRoles(), Matchers.containsInAnyOrder(JudgeRole.PRESIDING_JUDGE, JudgeRole.REASONS_FOR_JUDGMENT_AUTHOR));
        
        assertEquals(2, newJudgment.getJudges().size());
        assertTrue(annaNowakNew == newJudgment.getJudge(annaNowakNew.getName()));
        assertTrue(janNowakNew == newJudgment.getJudge(janNowakNew.getName()));
        assertThat(newJudgment.getJudge(annaNowakNew.getName()).getSpecialRoles(), Matchers.containsInAnyOrder(JudgeRole.REPORTING_JUDGE));
        assertThat(newJudgment.getJudge(janNowakNew.getName()).getSpecialRoles(), Matchers.containsInAnyOrder(JudgeRole.PRESIDING_JUDGE, JudgeRole.REASONS_FOR_JUDGMENT_AUTHOR));
        
        
    }
    
    
    @Test
    public void overwriteJudgment_CourtReporters() {
        
        oldJudgment.addCourtReporter("sldslkdlsdsd");
        oldJudgment.addCourtReporter("ddsdsdsds");
        
        List<String> newCourtReporters = Lists.newArrayList("sldslkdlsdssd");
        newJudgment.addCourtReporter(newCourtReporters.get(0));
        
        judgmentOverwriter.overwriteJudgment(oldJudgment, newJudgment, correctionList);
        
        assertEquals(newCourtReporters, oldJudgment.getCourtReporters());
        assertEquals(newCourtReporters, newJudgment.getCourtReporters());
    }
    
    
    @Test
    public void overwriteJudgment_LegalBases() {
        oldJudgment.addLegalBase("sldslkdlsdsd");
        oldJudgment.addLegalBase("ddsdsdsds");
        
        List<String> newLegalBases = Lists.newArrayList("sldslkdlsdssd", "dsdsddsdddddd", "lllllsldlsdl");
        newJudgment.addLegalBase(newLegalBases.get(0));
        newJudgment.addLegalBase(newLegalBases.get(1));
        newJudgment.addLegalBase(newLegalBases.get(2));
        
        judgmentOverwriter.overwriteJudgment(oldJudgment, newJudgment, correctionList);
        
        assertEquals(newLegalBases, oldJudgment.getLegalBases());
        assertEquals(newLegalBases, newJudgment.getLegalBases());
    }
    
    
    @Test
    public void overwriteJudgment_SourceInfo() {
        
        // old judgment
        JudgmentSourceInfo oldSource = new JudgmentSourceInfo();
        oldSource.setPublicationDate(new DateTime(2003, 12, 23, 23, 23));
        oldSource.setPublisher("123dssds d sd");
        oldSource.setReviser("2323232escxcvxv3");
        oldSource.setSourceJudgmentId("212121212 2121 212 12 12");
        oldSource.setSourceJudgmentUrl("http://sdsdsd/sd/sdsd");
        oldSource.setSourceCode(SourceCode.ADMINISTRATIVE_COURT);
        oldJudgment.setSourceInfo(oldSource);
        
        
        // new judgment
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
        judgmentOverwriter.overwriteJudgment(oldJudgment, newJudgment, correctionList);

        
        //assert
        
        JudgmentSourceInfoAssertUtils.assertSourceInfo(oldJudgment.getSourceInfo(), newJudgment.getSourceInfo(), newPublicationDate,
                newPublisher, newReviser, newSourceJudgmentId, newSourceUrl,
                newSourceType);
    }
    
    
    
    
    
    

    
    
}
