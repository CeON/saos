package pl.edu.icm.saos.importer.notapi.common;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.importer.ImportTestSupport;
import pl.edu.icm.saos.persistence.content.JudgmentContentFileDeleter;
import pl.edu.icm.saos.persistence.model.ConstitutionalTribunalJudgment;
import pl.edu.icm.saos.persistence.model.CourtType;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.JudgmentResult;
import pl.edu.icm.saos.persistence.model.MeansOfAppeal;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment;
import pl.edu.icm.saos.persistence.model.importer.notapi.RawSourceScJudgment;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;
import pl.edu.icm.saos.persistence.repository.JudgmentResultRepository;
import pl.edu.icm.saos.persistence.repository.MeansOfAppealRepository;

import com.google.common.collect.Lists;
import com.google.common.io.Files;

/**
 * @author madryk
 */
@Category(SlowTest.class)
public class JudgmentObjectDeleterTest extends ImportTestSupport {

    @Autowired
    private JudgmentObjectDeleter judgmentObjectDeleter;
    
    @Autowired
    private JudgmentRepository judgmentRepository;
    
    @Autowired
    private JudgmentContentFileDeleter judgmentContentFileDeleter;
    
    @Autowired
    private RawJudgmentTestFactory rawJudgmentTestFactory;
    
    @Autowired
    private MeansOfAppealRepository meansOfAppealRepository;
    
    @Autowired
    private JudgmentResultRepository judgmentResultRepository;
    
    
    private File judgmentContentDir;
    
    @Before
    public void setUp() {
        judgmentContentDir = Files.createTempDir();
        
        judgmentContentFileDeleter.setJudgmentContentPath(judgmentContentDir.getPath());
    }
    
    @After
    public void cleanup() throws IOException {
        FileUtils.deleteDirectory(judgmentContentDir);
    }
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void deleteJudgmentsWithoutRawSourceJudgment() throws IOException {
        // given
        SupremeCourtJudgment scJudgment1 = rawJudgmentTestFactory.createScJudgment(true);
        SupremeCourtJudgment scJudgment2 = rawJudgmentTestFactory.createScJudgment(false);
        rawJudgmentTestFactory.createScJudgment(false);
        ConstitutionalTribunalJudgment ctJudgment1 = rawJudgmentTestFactory.createCtJudgment(true);
        ConstitutionalTribunalJudgment ctJudgment2 = rawJudgmentTestFactory.createCtJudgment(false);
        
        scJudgment1.getTextContent().setFilePath("scJudgment1File.txt");
        File scJudgment1File = new File(judgmentContentDir, "scJudgment1File.txt");
        scJudgment2.getTextContent().setFilePath("scJudgment2File.txt");
        File scJudgment2File = new File(judgmentContentDir, "scJudgment2File.txt");
        ctJudgment1.getTextContent().setFilePath("ctJudgment1File.txt");
        File ctJudgment1File = new File(judgmentContentDir, "ctJudgment1File.txt");
        
        scJudgment1File.createNewFile();
        scJudgment2File.createNewFile();
        ctJudgment1File.createNewFile();
        
        judgmentRepository.save(Lists.newArrayList(scJudgment1, scJudgment2, ctJudgment1));
        
        
        // execute
        judgmentObjectDeleter.deleteJudgmentsWithoutRawSourceJudgment(SupremeCourtJudgment.class, RawSourceScJudgment.class);
        
        // assert
        List<Judgment> dbJudgments = judgmentRepository.findAll();
        
        assertThat(dbJudgments.stream().map(x -> x.getId()).collect(Collectors.toList()),
                containsInAnyOrder(scJudgment1.getId(), ctJudgment1.getId(), ctJudgment2.getId()));
        
        assertTrue(scJudgment1File.exists());
        assertFalse(scJudgment2File.exists());
        assertTrue(ctJudgment1File.exists());
    }
    
    @Test
    public void deleteMeansOfAppealWithoutJudgments() {
        
        // given
        Judgment scJudgment = rawJudgmentTestFactory.createCtJudgment(false);
        Judgment ctJudgment = rawJudgmentTestFactory.createCtJudgment(false);
        
        MeansOfAppeal scMeansOfAppeal1 = createMeansOfAppeal(CourtType.SUPREME, "scMeansOfAppeal1");
        createMeansOfAppeal(CourtType.SUPREME, "scMeansOfAppeal2");
        MeansOfAppeal ctMeansOfAppeal1 = createMeansOfAppeal(CourtType.CONSTITUTIONAL_TRIBUNAL, "ctMeansOfAppeal1");
        MeansOfAppeal ctMeansOfAppeal2 = createMeansOfAppeal(CourtType.CONSTITUTIONAL_TRIBUNAL, "ctMeansOfAppeal2");
        
        scJudgment.setMeansOfAppeal(scMeansOfAppeal1);
        ctJudgment.setMeansOfAppeal(ctMeansOfAppeal1);
        judgmentRepository.save(Lists.newArrayList(scJudgment, ctJudgment));
        
        
        // execute
        judgmentObjectDeleter.deleteMeansOfAppealWithoutJudgments(CourtType.SUPREME);
        
        
        // assert
        List<MeansOfAppeal> dbMeansOfAppeal = meansOfAppealRepository.findAll();
        assertThat(dbMeansOfAppeal, containsInAnyOrder(scMeansOfAppeal1, ctMeansOfAppeal1, ctMeansOfAppeal2));
        
    }
    
    @Test
    public void deleteJudgmentResultsWithoutJudgments() {
        
        // given
        Judgment scJudgment = rawJudgmentTestFactory.createCtJudgment(false);
        Judgment ctJudgment = rawJudgmentTestFactory.createCtJudgment(false);
        
        JudgmentResult scJudgmentResult1 = createJudgmentResult(CourtType.SUPREME, "scMeansOfAppeal1");
        createMeansOfAppeal(CourtType.SUPREME, "scMeansOfAppeal2");
        JudgmentResult ctJudgmentResult1 = createJudgmentResult(CourtType.CONSTITUTIONAL_TRIBUNAL, "ctMeansOfAppeal1");
        JudgmentResult ctJudgmentResult2 = createJudgmentResult(CourtType.CONSTITUTIONAL_TRIBUNAL, "ctMeansOfAppeal2");
        
        scJudgment.setJudgmentResult(scJudgmentResult1);
        ctJudgment.setJudgmentResult(ctJudgmentResult1);
        judgmentRepository.save(Lists.newArrayList(scJudgment, ctJudgment));
        
        
        // execute
        judgmentObjectDeleter.deleteJudgmentResultsWithoutJudgments(CourtType.SUPREME);
        
        
        // assert
        List<JudgmentResult> dbJudgmentResults = judgmentResultRepository.findAll();
        assertThat(dbJudgmentResults, containsInAnyOrder(scJudgmentResult1, ctJudgmentResult1, ctJudgmentResult2));
        
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private MeansOfAppeal createMeansOfAppeal(CourtType courtType, String name) {
        MeansOfAppeal meansOfAppeal = new MeansOfAppeal(courtType, name);
        meansOfAppealRepository.save(meansOfAppeal);
        return meansOfAppeal;
    }
    
    private JudgmentResult createJudgmentResult(CourtType courtType, String text) {
        JudgmentResult judgmentResult = new JudgmentResult(courtType, text);
        judgmentResultRepository.save(judgmentResult);
        return judgmentResult;
        
    }

}
