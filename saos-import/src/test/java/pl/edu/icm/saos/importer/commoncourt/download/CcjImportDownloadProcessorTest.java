package pl.edu.icm.saos.importer.commoncourt.download;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import pl.edu.icm.saos.persistence.model.importer.RawSourceCcJudgment;
import pl.edu.icm.saos.persistence.repository.RawSourceCcJudgmentRepository;

/**
 * @author Łukasz Dumiszewski
 */

public class CcjImportDownloadProcessorTest {

    
    private CcjImportDownloadProcessor ccjImportDownloadProcessor = new CcjImportDownloadProcessor();
    
    private SourceCcjTextDataConverter sourceCcjTextDataConverter = mock(SourceCcjTextDataConverter.class);
    
    private RawSourceCcJudgmentRepository rawSourceCcJudgmentRepository = mock(RawSourceCcJudgmentRepository.class);
    
    private SourceCcJudgmentTextData ccjTextData = new SourceCcJudgmentTextData();
    
    private RawSourceCcJudgment rJudgment = new RawSourceCcJudgment();
    
    
    @Before
    public void before() {
        ccjImportDownloadProcessor.setRawSourceCcJudgmentRepository(rawSourceCcJudgmentRepository);
        ccjImportDownloadProcessor.setSourceCcjTextDataConverter(sourceCcjTextDataConverter);
        
        rJudgment.setSourceId("121wdvc");
        rJudgment.setDataMd5("2323dafcdcvsdrzr4 rf34");
        
    }
    
    
    @Test
    public void process_FoundSame() throws Exception {
        
        when(sourceCcjTextDataConverter.convert(Mockito.eq(ccjTextData))).thenReturn(rJudgment);
        
        Mockito.when(rawSourceCcJudgmentRepository.findOneBySourceIdAndDataMd5(rJudgment.getSourceId(), rJudgment.getDataMd5())).thenReturn(mock(RawSourceCcJudgment.class));
        
        RawSourceCcJudgment rJudgment = ccjImportDownloadProcessor.process(ccjTextData);
        
        assertNull(rJudgment);
    }
    
    
    @Test
    public void process_NotFoundSame() throws Exception {
        
        when(sourceCcjTextDataConverter.convert(Mockito.eq(ccjTextData))).thenReturn(rJudgment);
        
        Mockito.when(rawSourceCcJudgmentRepository.findOneBySourceIdAndDataMd5(rJudgment.getSourceId(), rJudgment.getDataMd5())).thenReturn(null);
        
        RawSourceCcJudgment returnedJudgment = ccjImportDownloadProcessor.process(ccjTextData);
        
        assertEquals(returnedJudgment, rJudgment);
    }
    
    
}
