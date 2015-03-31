package pl.edu.icm.saos.importer.notapi.common.content;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.icm.saos.importer.notapi.common.content.transaction.ContentFileTransactionContext;
import pl.edu.icm.saos.importer.notapi.common.content.transaction.ContentFileOperationPerformer;
import pl.edu.icm.saos.persistence.model.Judgment;

import com.google.common.base.Preconditions;

/**
 * Processor of judgment content files
 * 
 * @author madryk
 */
public class JudgmentContentFileProcessor {
    
    private JudgmentContentFileExtractor judgmentContentFileExtractor;
    
    private JudgmentContentFilePathGenerator judgmentContentFilePathGenerator;
    
    private ContentTypeExtractor contentTypeExtractor;
    
    private ContentFileOperationPerformer contentFileOperationPerformer;
    
    
    private String downloadedContentDir;

    
    //------------------------ LOGIC --------------------------
    
    /**
     * Handles adding of judgment content files.
     * It saves all needed operations on files in {@link ContentFileTransactionContext}.
     * 
     * @param context - used to save needed operations on files
     * @param downloadedContentFilename - name of archive file from which judgment content file can be extracted
     * @param judgment - judgment which content is processed. This method will update it's {@link Judgment#getTextContent()}
     * @param oldJudgmentContentPath - path to old version of judgment content file.
     *     It can be null if old judgment doesn't have content file or processing concerns new judgment.
     * @throws IOException
     */
    public void processJudgmentContentFile(ContentFileTransactionContext context, String downloadedContentFilename, Judgment judgment, String oldJudgmentContentPath) throws IOException {
        
        Preconditions.checkNotNull(context);
        Preconditions.checkNotNull(downloadedContentFilename);
        Preconditions.checkNotNull(judgment);
        
        InputStreamWithFilename judgmentContent = null;
        try {
            File downloadedContentFile = new File(downloadedContentDir, downloadedContentFilename);
            String sourceJudgmentId = judgment.getSourceInfo().getSourceJudgmentId();
            
            judgmentContent = judgmentContentFileExtractor.extractJudgmentContent(downloadedContentFile, sourceJudgmentId);

            updateContentFileInfo(judgment, judgmentContent.getFilename());

            handleContentFile(context, judgmentContent, judgment, oldJudgmentContentPath);
            
        } finally {
            if (judgmentContent != null) {
                judgmentContent.close();
            }
        }
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private void updateContentFileInfo(Judgment judgment, String contentFileName) {
        
        String path = judgmentContentFilePathGenerator.generatePath(judgment) + contentFileName;
        judgment.getTextContent().setFilePath(path);
        judgment.getTextContent().setType(contentTypeExtractor.extractContentType(contentFileName));
    }
    
    
    private void handleContentFile(ContentFileTransactionContext context, InputStreamWithFilename judgmentContent, Judgment judgment, String oldJudgmentContentPath) throws IOException {
        String contentPath = judgment.getTextContent().getFilePath();
        
        if (oldJudgmentContentPath != null) {
            contentFileOperationPerformer.overwriteFile(context, judgmentContent.getInputStream(), contentPath, oldJudgmentContentPath);
        } else {
            contentFileOperationPerformer.addFile(context, judgmentContent.getInputStream(), contentPath);
        }
    }

    
    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setJudgmentContentFileExtractor(JudgmentContentFileExtractor judgmentContentFileExtractor) {
        this.judgmentContentFileExtractor = judgmentContentFileExtractor;
    }

    @Autowired
    public void setJudgmentContentFilePathGenerator(JudgmentContentFilePathGenerator judgmentContentFilePathGenerator) {
        this.judgmentContentFilePathGenerator = judgmentContentFilePathGenerator;
    }

    @Autowired
    public void setContentTypeExtractor(ContentTypeExtractor contentTypeExtractor) {
        this.contentTypeExtractor = contentTypeExtractor;
    }

    @Autowired
    public void setContentFileOperationPerformer(ContentFileOperationPerformer contentFileOperationPerformer) {
        this.contentFileOperationPerformer = contentFileOperationPerformer;
    }

    public void setDownloadedContentDir(String downloadedContentDir) {
        this.downloadedContentDir = downloadedContentDir;
    }
}
