package pl.edu.icm.saos.importer.notapi.common.content;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import pl.edu.icm.saos.common.testcommon.PathResolver;
import pl.edu.icm.saos.importer.common.ImportException;

/**
 * @author madryk
 */
public class JudgmentContentFileExtractorTest {

    private JudgmentContentFileExtractor judgmentContentFileExtractor = new JudgmentContentFileExtractor();
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void extractJudgmentContent() throws IOException {
        // given
        String archivePath = PathResolver.resolveToAbsolutePath("import/content/judgmentContent.zip");
        String judgmentContentPath = PathResolver.resolveToAbsolutePath("import/content/3b42a6299303c65d869c4806fdcdbf7a.doc");
        
        // execute
        InputStreamWithFilename judgmentContent =  judgmentContentFileExtractor.extractJudgmentContent(new File(archivePath), "3b42a6299303c65d869c4806fdcdbf7a");
        
        
        // assert
        assertEquals("3b42a6299303c65d869c4806fdcdbf7a.doc", judgmentContent.getFilename());
        
        InputStream expectedFileStream = null;
        try {
            expectedFileStream = new FileInputStream(new File(judgmentContentPath));
            assertTrue(IOUtils.contentEquals(judgmentContent.getInputStream(), expectedFileStream));
        } finally {
            IOUtils.closeQuietly(judgmentContent);
            IOUtils.closeQuietly(expectedFileStream);
        }
        
    }
    
    @Test(expected = ImportException.class)
    public void extractJudgmentContent_NOT_FOUND() throws IOException {
        // given
        String archivePath = PathResolver.resolveToAbsolutePath("import/content/judgmentContent.zip");
        
        // execute
        judgmentContentFileExtractor.extractJudgmentContent(new File(archivePath), "notExistingSourceId");
    }
}
