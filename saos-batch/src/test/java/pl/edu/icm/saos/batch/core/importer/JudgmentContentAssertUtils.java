package pl.edu.icm.saos.batch.core.importer;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

/**
 * Utils for assertion of judgment content
 * 
 * @author madryk
 */
class JudgmentContentAssertUtils {

    public static void assertJudgmentContentsExists(File judgmentContentDir, String ... paths) {
        for (String path : paths) {
            File file = new File(judgmentContentDir, path);
            
            assertTrue("Judgment content doesn't exists in " + path, file.exists());
        }
    }
    
    public static void assertJudgmentContent(File expected, File actual) throws IOException {
        FileUtils.contentEquals(expected, actual);
    }
}
