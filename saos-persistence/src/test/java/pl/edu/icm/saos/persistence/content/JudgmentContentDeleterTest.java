package pl.edu.icm.saos.persistence.content;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.assertj.core.util.Lists;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.io.Files;

/**
 * @author madryk
 */
public class JudgmentContentDeleterTest {

    private JudgmentContentDeleter judgmentContentDeleter = new JudgmentContentDeleter();
    
    private File contentDir;
    
    
    @Before
    public void setUp() {
        contentDir = Files.createTempDir();
        judgmentContentDeleter.setJudgmentContentPath(contentDir.getPath());
    }
    
    @After
    public void cleanup() throws IOException {
        FileUtils.deleteDirectory(contentDir);
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void deleteContents() throws IOException {
        // given
        File file1 = new File(contentDir, "file1.txt");
        File file2 = new File(contentDir, "file2.txt");
        File file3 = new File(contentDir, "file3.txt");
        
        file1.createNewFile();
        file2.createNewFile();
        file3.createNewFile();
        
        // execute
        judgmentContentDeleter.deleteContents(Lists.newArrayList("file1.txt", "file2.txt"));
        
        // assert
        assertFalse(file1.exists());
        assertFalse(file2.exists());
        assertTrue(file3.exists());
    }
}
