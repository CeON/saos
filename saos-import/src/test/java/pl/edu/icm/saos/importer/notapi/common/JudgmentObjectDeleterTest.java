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

import com.google.common.collect.Lists;
import com.google.common.io.Files;

import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.importer.ImportTestSupport;
import pl.edu.icm.saos.persistence.content.JudgmentContentDeleter;
import pl.edu.icm.saos.persistence.model.ConstitutionalTribunalJudgment;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment;
import pl.edu.icm.saos.persistence.model.importer.notapi.RawSourceScJudgment;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;

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
    private JudgmentContentDeleter judgmentContentDeleter;
    
    @Autowired
    private RawJudgmentTestFactory rawJudgmentTestFactory;
    
    
    private File judgmentContentDir;
    
    @Before
    public void setUp() {
        judgmentContentDir = Files.createTempDir();
        
        judgmentContentDeleter.setJudgmentContentPath(judgmentContentDir.getPath());
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
    
    

}
