package pl.edu.icm.saos.importer.commoncourt.download;

import java.util.LinkedList;
import java.util.Queue;

import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import pl.edu.icm.saos.persistence.repository.RawSourceCcJudgmentRepository;

import com.google.common.collect.Lists;

/**
 * Common court judgment import - download - reader
 * 
 * 
 * @author Łukasz Dumiszewski
 */
public class CcjImportDownloadReader implements ItemStreamReader<SourceCcJudgmentTextData> {

    private Logger log = LoggerFactory.getLogger(CcjImportDownloadReader.class);
    
    private int pageSize = 100;
    private int pageNo = 1;
    private DateTime publicationDateFrom;
    private Queue<String> judgmentIds;
    
    private String customPublicationDateFrom = null;
    
    @Autowired
    private SourceCcjExternalRepository sourceCcjExternalRepository;
    
    @Autowired
    private RawSourceCcJudgmentRepository rawSourceCcJudgmentRepository;
    
    
    @Override
    public SourceCcJudgmentTextData read() throws Exception, UnexpectedInputException, NonTransientResourceException {
        if (CollectionUtils.isEmpty(judgmentIds)) {
            judgmentIds = new LinkedList<String>(sourceCcjExternalRepository.findJudgmentIds(pageNo, pageSize, publicationDateFrom));
            if (CollectionUtils.isEmpty(judgmentIds)) {
                return null;
            }
            pageNo++;
            log.debug("{} judgments have been read", judgmentIds.size());
        }
        
        String judgmentId = judgmentIds.poll();
        
        if (judgmentId == null) {
            return null;
        }
        
        return sourceCcjExternalRepository.findJudgment(judgmentId);
    }
    
    
    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        pageNo = 1;
        judgmentIds = Lists.newLinkedList();
        if (customPublicationDateFrom == null) {
            publicationDateFrom = new DateTime(2014, 06, 22, 23, 59, DateTimeZone.forID("CET"));
        } else {
            publicationDateFrom = new CcjImportDateFormatter().parse(customPublicationDateFrom);
        }
        log.info("publication date from: {}", new CcjImportDateFormatter().format(publicationDateFrom));
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void close() throws ItemStreamException {
        
    }
    
    
    
    //------------------------ SETTERS --------------------------
    
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    @Value("#{jobParameters['customPublicationDateFrom']}")
    public void setCustomPublicationDateFrom(String customPublicationDateFrom) {
        this.customPublicationDateFrom = customPublicationDateFrom;
    }


   

}
