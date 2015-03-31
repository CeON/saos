package pl.edu.icm.saos.importer.notapi.common.content.transaction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * @author madryk
 */
public class ContentFileTransactionContextFactoryTest {

    private ContentFileTransactionContextFactory contentFileTransactionContextFactory = new ContentFileTransactionContextFactory();
    
    private String contentDirectoryPath = "/content/directory/path";
    
    @Before
    public void setUp() {
        contentFileTransactionContextFactory.setContentDirectoryPath(contentDirectoryPath);
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void createTransactionContext() throws IOException {
        // execute
        ContentFileTransactionContext context = contentFileTransactionContextFactory.createTransactionContext();
        
        // assert
        assertNotNull(context);
        assertEquals(new File(contentDirectoryPath), context.getContentDirectory());
        assertTrue(context.getDeletedTmpDirectory().exists());
        
        FileUtils.deleteDirectory(context.getDeletedTmpDirectory());
    }
    
}
