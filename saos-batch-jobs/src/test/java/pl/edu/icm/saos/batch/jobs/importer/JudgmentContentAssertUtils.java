package pl.edu.icm.saos.batch.jobs.importer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import pl.edu.icm.saos.persistence.model.JudgmentTextContent;
import pl.edu.icm.saos.persistence.model.JudgmentTextContent.ContentType;

/**
 * Utils for assertion of judgment content
 * 
 * @author madryk
 */
class JudgmentContentAssertUtils {

    public static void assertJudgmentContentsExist(File judgmentContentDir, String ... paths) {
        for (String path : paths) {
            File file = new File(judgmentContentDir, path);
            
            assertTrue("Judgment content doesn't exists in " + path, file.exists());
        }
    }
    
    public static void assertJudgmentContentNotExists(File judgmentContentDir, String path) {
        File file = new File(judgmentContentDir, path);
        assertFalse("Judgment content exists in " + path, file.exists());
    }
    
    public static void assertJudgmentContent(File expected, File actual) throws IOException {
        assertTrue(FileUtils.contentEquals(expected, actual));
    }
    
    public static void assertTextContent(JudgmentTextContent textContent, String rawText, String filePath, ContentType contentType) {
        assertThat(textContent.getRawTextContent(), is(rawText));
        assertThat(textContent.getFilePath(), is(filePath));
        assertThat(textContent.getType(), is(contentType));
    }
}
