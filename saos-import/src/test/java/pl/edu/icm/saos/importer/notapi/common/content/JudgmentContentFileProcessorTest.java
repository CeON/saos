package pl.edu.icm.saos.importer.notapi.common.content;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import pl.edu.icm.saos.importer.notapi.common.content.transaction.ContentFileTransactionContext;
import pl.edu.icm.saos.importer.notapi.common.content.transaction.ContentFileOperationPerformer;
import pl.edu.icm.saos.persistence.model.ConstitutionalTribunalJudgment;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.JudgmentTextContent.ContentType;

/**
 * @author madryk
 */
@RunWith(MockitoJUnitRunner.class)
public class JudgmentContentFileProcessorTest {

    @InjectMocks
    private JudgmentContentFileProcessor judgmentContentFileProcessor = new JudgmentContentFileProcessor();
    
    @Mock
    private JudgmentContentFileExtractor judgmentContentFileExtractor;
    
    @Mock
    private JudgmentContentFilePathGenerator judgmentContentFilePathGenerator;
    
    @Mock
    private ContentTypeExtractor contentTypeExtractor;
    
    @Mock
    private ContentFileOperationPerformer contentFileOperationPerformer;
    
    
    
    private String downloadedContentDir = "/downloaded/content/dir";
    
    private String downloadedContentFilename = "contentFilename.zip";
    
    private String judgmentContentFilename = "judgmentContent.pdf";
    
    private String sourceJudgmentId = "AAAXXX";
    
    private InputStreamWithFilename contentStreamWithFilename;
    
    private InputStream contentStream;
    
    
    
    @Before
    public void setUp() throws IOException {
        judgmentContentFileProcessor.setDownloadedContentDir(downloadedContentDir);
        
        contentStreamWithFilename = mock(InputStreamWithFilename.class);
        contentStream = mock(InputStream.class);
        
        when(contentStreamWithFilename.getFilename()).thenReturn(judgmentContentFilename);
        when(contentStreamWithFilename.getInputStream()).thenReturn(contentStream);
        
        when(judgmentContentFileExtractor.extractJudgmentContent(any(), any())).thenReturn(contentStreamWithFilename);
        when(judgmentContentFilePathGenerator.generatePath(any())).thenReturn("/judgment/content/path/");
        when(contentTypeExtractor.extractContentType(any())).thenReturn(ContentType.PDF);
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void processJudgmentContentFile_NULL_OLD_CONTENT_PATH() throws IOException {
        // given
        ContentFileTransactionContext transactionContext = mock(ContentFileTransactionContext.class);
        
        Judgment judgment = new ConstitutionalTribunalJudgment();
        judgment.getSourceInfo().setSourceJudgmentId(sourceJudgmentId);
        
        
        // execute
        judgmentContentFileProcessor.processJudgmentContentFile(transactionContext, downloadedContentFilename, judgment, null);
        
        
        // assert
        verify(judgmentContentFileExtractor).extractJudgmentContent(new File(downloadedContentDir, downloadedContentFilename), sourceJudgmentId);
        verify(judgmentContentFilePathGenerator).generatePath(judgment);
        verify(contentTypeExtractor).extractContentType(judgmentContentFilename);
        verify(contentFileOperationPerformer).addFile(transactionContext, contentStream, "/judgment/content/path/judgmentContent.pdf");

        verify(contentStreamWithFilename).close();
        
        verifyNoMoreInteractions(judgmentContentFileExtractor, judgmentContentFilePathGenerator, contentTypeExtractor, contentFileOperationPerformer);
    }
    
    @Test
    public void processJudgmentContentFile_WITH_OLD_CONTENT_PATH() throws IOException {
        // given
        ContentFileTransactionContext transactionContext = mock(ContentFileTransactionContext.class);
        
        Judgment judgment = new ConstitutionalTribunalJudgment();
        judgment.getSourceInfo().setSourceJudgmentId(sourceJudgmentId);
        
        
        // execute
        judgmentContentFileProcessor.processJudgmentContentFile(transactionContext, downloadedContentFilename, judgment, "/judgment/content/path/oldJudgmentContent.pdf");
        
        
        // assert
        verify(judgmentContentFileExtractor).extractJudgmentContent(new File(downloadedContentDir, downloadedContentFilename), sourceJudgmentId);
        verify(judgmentContentFilePathGenerator).generatePath(judgment);
        verify(contentTypeExtractor).extractContentType(judgmentContentFilename);
        verify(contentFileOperationPerformer).overwriteFile(transactionContext, contentStream, "/judgment/content/path/judgmentContent.pdf", "/judgment/content/path/oldJudgmentContent.pdf");

        verify(contentStreamWithFilename).close();
        
        verifyNoMoreInteractions(judgmentContentFileExtractor, judgmentContentFilePathGenerator, contentTypeExtractor, contentFileOperationPerformer);
    }
    
}
