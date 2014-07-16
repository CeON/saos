package pl.edu.icm.saos.importer.commoncourt.judgment.download;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;
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

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
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
    private int pageNo = 0;
    private DateTime publicationDateFrom;
    private Queue<String> judgmentIds;
    
    private String customPublicationDateFrom = null;
    
    private SourceCcjExternalRepository sourceCcjExternalRepository;
    
    private RawSourceCcJudgmentRepository rawSourceCcJudgmentRepository;
    
    private CcjImportDateFormatter ccjImportDateFormatter;
    
    
    
    @Override
    public SourceCcJudgmentTextData read() throws Exception, UnexpectedInputException, NonTransientResourceException {
        Preconditions.checkNotNull(publicationDateFrom);
        
        if (CollectionUtils.isEmpty(judgmentIds)) {
            log.debug("reading next {} judgments...", pageSize);
            judgmentIds = new LinkedList<String>(sourceCcjExternalRepository.findJudgmentIds(pageNo, pageSize, publicationDateFrom));
            log.debug("{} judgments have been read", pageNo*pageSize + judgmentIds.size());
            if (CollectionUtils.isEmpty(judgmentIds)) {
                return null;
            }
            pageNo++;
            
        }
        
        String judgmentId = judgmentIds.poll();
        
        if (judgmentId == null) {
            return null;
        }
        
        return sourceCcjExternalRepository.findJudgment(judgmentId);
    }
    
    
    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        pageNo = 0;
        judgmentIds = Lists.newLinkedList();
        if (customPublicationDateFrom == null) {
            publicationDateFrom = rawSourceCcJudgmentRepository.findMaxPublicationDate();
        } else {
            publicationDateFrom = ccjImportDateFormatter.parse(customPublicationDateFrom);
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
    
    
    //------------------------ GETTERS --------------------------
    
    int getPageNo() {
        return pageNo;
    }


    DateTime getPublicationDateFrom() {
        return publicationDateFrom;
    }


    List<String> getJudgmentIds() {
        return ImmutableList.copyOf(judgmentIds);
    }

    
    //------------------------ SETTERS --------------------------
    
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    @Value("#{jobParameters['customPublicationDateFrom']}")
    public void setCustomPublicationDateFrom(String customPublicationDateFrom) {
        this.customPublicationDateFrom = customPublicationDateFrom;
    }

    @Autowired
    public void setSourceCcjExternalRepository(SourceCcjExternalRepository sourceCcjExternalRepository) {
        this.sourceCcjExternalRepository = sourceCcjExternalRepository;
    }

    @Autowired
    public void setRawSourceCcJudgmentRepository(RawSourceCcJudgmentRepository rawSourceCcJudgmentRepository) {
        this.rawSourceCcJudgmentRepository = rawSourceCcJudgmentRepository;
    }

    @Autowired
    public void setCcjImportDateFormatter(CcjImportDateFormatter ccjImportDateFormatter) {
        this.ccjImportDateFormatter = ccjImportDateFormatter;
    }


    

   

}
