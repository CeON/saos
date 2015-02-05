package pl.edu.icm.saos.importer.commoncourt.judgment.download;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.batch.item.ExecutionContext;

import pl.edu.icm.saos.importer.common.ImportDateTimeFormatter;
import pl.edu.icm.saos.persistence.repository.RawSourceCcJudgmentRepository;

import com.google.common.collect.Lists;

/**
 * @author Łukasz Dumiszewski
 */

public class CcjImportDownloadReaderTest {

    
    private CcjImportDownloadReader ccjImportDownloadReader = new CcjImportDownloadReader();
    
    private SourceCcjExternalRepository sourceCcjExternalRepository = mock(SourceCcjExternalRepository.class);
    
    private RawSourceCcJudgmentRepository rawSourceCcJudgmentRepository = mock(RawSourceCcJudgmentRepository.class);
    
    private ImportDateTimeFormatter ccjImportDateTimeFormatter = mock(ImportDateTimeFormatter.class);
    
    private int pageSize = 100;
    
    @Before
    public void before() {
        ccjImportDownloadReader.setRawSourceCcJudgmentRepository(rawSourceCcJudgmentRepository);
        ccjImportDownloadReader.setSourceCcjExternalRepository(sourceCcjExternalRepository);
        ccjImportDownloadReader.setCcjImportDateTimeFormatter(ccjImportDateTimeFormatter);
        ccjImportDownloadReader.setPageSize(pageSize);
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void open_NoCustomPublicationDateFrom() {
        // given
        ccjImportDownloadReader.setCustomPublicationDateFrom(null);
        
        DateTime maxPublicationDate = new DateTime(2012, 12, 23, 23, 45);
        when(rawSourceCcJudgmentRepository.findMaxPublicationDate()).thenReturn(maxPublicationDate);
        
        
        // execute
        ccjImportDownloadReader.open(mock(ExecutionContext.class));
        
        
        // assert
        verifyZeroInteractions(sourceCcjExternalRepository, ccjImportDateTimeFormatter);
        verify(rawSourceCcJudgmentRepository).findMaxPublicationDate();
        
        assertEquals(maxPublicationDate, ccjImportDownloadReader.getPublicationDateFrom());
        assertEquals(0, ccjImportDownloadReader.getPageNo());
        assertEquals(0, ccjImportDownloadReader.getJudgmentIds().size());
        
    }

    
    
    @Test
    public void open_CustomPublicationDateFrom() {
        // given
        String customPublicationDateFrom = "2013-03-03 12:30";
        ccjImportDownloadReader.setCustomPublicationDateFrom(customPublicationDateFrom);
        
        DateTime customPublicationDate = new DateTime(2013, 03, 03, 12, 30);
        when(ccjImportDateTimeFormatter.parse(customPublicationDateFrom)).thenReturn(customPublicationDate);
        
        
        // execute
        ccjImportDownloadReader.open(mock(ExecutionContext.class));
        
        
        // assert
        verifyZeroInteractions(sourceCcjExternalRepository, rawSourceCcJudgmentRepository);
        verify(ccjImportDateTimeFormatter).parse(customPublicationDateFrom);
        
        assertEquals(customPublicationDate, ccjImportDownloadReader.getPublicationDateFrom());
        assertEquals(0, ccjImportDownloadReader.getPageNo());
        assertEquals(0, ccjImportDownloadReader.getJudgmentIds().size());
        
    }
    
    
    
    @Test
    public void read_findJudgmentIds_FOUND() throws Exception {
        // given
        DateTime publicationDate = new DateTime(2013, 03, 03, 12, 30);
        
        readerOpen(publicationDate);
        
        List<String> judgmentIds = Lists.newArrayList("123", "234");
        when(sourceCcjExternalRepository.findJudgmentIds(0, pageSize , publicationDate)).thenReturn(judgmentIds);
        
        SourceCcJudgmentTextData ccjTextData = createCcjTextData("1111x");
        when(sourceCcjExternalRepository.findJudgment(judgmentIds.get(0))).thenReturn(ccjTextData);
        
        
        // execute
        SourceCcJudgmentTextData readTextData = ccjImportDownloadReader.read();
        
        
        // assert
        verify(sourceCcjExternalRepository).findJudgmentIds(0, pageSize , publicationDate);
        verify(sourceCcjExternalRepository).findJudgment(judgmentIds.get(0));
        
        assertCcjTextData(ccjTextData, readTextData);
        judgmentIds.remove(0);
        assertReader(1, publicationDate, judgmentIds);
        
        
        
        
        // read the 2. time
        Mockito.reset(sourceCcjExternalRepository);
        
        
        // given
        ccjTextData = createCcjTextData("1112x");
        when(sourceCcjExternalRepository.findJudgment(judgmentIds.get(0))).thenReturn(ccjTextData);
        
        
        // execute
        readTextData = ccjImportDownloadReader.read();
        
        
        // assert
        verify(sourceCcjExternalRepository).findJudgment(judgmentIds.get(0));
        verify(sourceCcjExternalRepository, never()).findJudgmentIds(anyInt(), anyInt(), any(DateTime.class));
        
        assertCcjTextData(ccjTextData, readTextData);
        judgmentIds.remove(0);
        assertReader(1, publicationDate, judgmentIds);
    }

    
    
    @Test
    public void read_findJudgmentIds_NOT_FOUND() throws Exception {
        // given
        DateTime publicationDate = new DateTime(2013, 03, 03, 12, 30);
        
        readerOpen(publicationDate);
        
        List<String> judgmentIds = Lists.newArrayList();
        when(sourceCcjExternalRepository.findJudgmentIds(1, pageSize , publicationDate)).thenReturn(judgmentIds);
        
        
        // execute
        SourceCcJudgmentTextData readTextData = ccjImportDownloadReader.read();
        
        
        // assert
        verify(sourceCcjExternalRepository).findJudgmentIds(0, pageSize , publicationDate);
        
        verify(sourceCcjExternalRepository, never()).findJudgment(anyString());
        
        assertNull(readTextData);
        assertReader(0, publicationDate, judgmentIds);
    }
    
    @Test
    public void read_findJudgment_OmmitingDownloadErrorJudgment() throws Exception {
        // given
        DateTime publicationDate = new DateTime(2013, 03, 03, 12, 30);
        readerOpen(publicationDate);
        
        SourceCcJudgmentTextData textData1 = createCcjTextData("abc1");
        SourceCcJudgmentTextData textData2 = createCcjTextData("abc1a");
        
        List<String> judgmentIds = Lists.newArrayList("35", "38", "40");
        when(sourceCcjExternalRepository.findJudgmentIds(0, pageSize, publicationDate)).thenReturn(judgmentIds);
        
        when(sourceCcjExternalRepository.findJudgment("35")).thenReturn(textData1);
        when(sourceCcjExternalRepository.findJudgment("38")).thenThrow(new SourceCcJudgmentDownloadErrorException("<error>something is wrong</error>"));
        when(sourceCcjExternalRepository.findJudgment("40")).thenReturn(textData2);
        
        
        // execute
        SourceCcJudgmentTextData readTextData1 = ccjImportDownloadReader.read();
        SourceCcJudgmentTextData readTextData2 = ccjImportDownloadReader.read();
        SourceCcJudgmentTextData readTextData3 = ccjImportDownloadReader.read();
        
        
        // assert
        assertCcjTextData(textData1, readTextData1);
        assertCcjTextData(textData2, readTextData2);
        assertNull(readTextData3);
        
    }
        
    
    
    //------------------------ PRIVATE --------------------------


    private SourceCcJudgmentTextData createCcjTextData(String metadata) {
        SourceCcJudgmentTextData ccjTextData = new SourceCcJudgmentTextData();
        ccjTextData.setMetadata(metadata);
        return ccjTextData;
    }


    
    private void assertCcjTextData(SourceCcJudgmentTextData expectedTextData, SourceCcJudgmentTextData actualTextData) {
        assertEquals(expectedTextData, actualTextData);
        assertEquals(expectedTextData.getMetadata(), actualTextData.getMetadata());
    }

    
    private void assertReader(int expectedPageNo, DateTime expectedPublicationDate, List<String> expectedJudgmentIds) {
        assertEquals(expectedJudgmentIds, ccjImportDownloadReader.getJudgmentIds());
        assertEquals(expectedPageNo, ccjImportDownloadReader.getPageNo());
        assertEquals(expectedPublicationDate, ccjImportDownloadReader.getPublicationDateFrom());
    }


    private void readerOpen(DateTime publicationDate) {
        ccjImportDownloadReader.setCustomPublicationDateFrom("22323");
        when(ccjImportDateTimeFormatter.parse(anyString())).thenReturn(publicationDate);
        
        ccjImportDownloadReader.open(mock(ExecutionContext.class));
    }
    
    
}

