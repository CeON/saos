package pl.edu.icm.saos.search.indexing;

import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;

import com.google.common.collect.Lists;

@Service
public class JudgmentIndexingReader implements ItemStreamReader<Judgment> {

    private static Logger log = LoggerFactory.getLogger(JudgmentIndexingReader.class);

    @Autowired
    private JudgmentRepository judgmentRepository;
    
    private int pageSize = 20;
    private int pageNo = 0;
    private Queue<Judgment> judgments = Lists.newLinkedList();
    
    @Override
    public void open(ExecutionContext executionContext)
            throws ItemStreamException {
        judgments = Lists.newLinkedList();
    }

    @Override
    public Judgment read() throws Exception,
            UnexpectedInputException, ParseException,
            NonTransientResourceException {
        if (judgments.isEmpty()) {
            judgments = Lists.newLinkedList(judgmentRepository.findAllToIndex(new PageRequest(pageNo, pageSize)).getContent());
            if (judgments.isEmpty()) {
                return null;
            }
            log.debug("Read {} judgments for indexing", judgments.size());
            pageNo++;
        }
        
        Judgment commonCourtJudgment = judgments.poll();
        
        return commonCourtJudgment;
    }
    
    @Override
    public void update(ExecutionContext executionContext)
            throws ItemStreamException {
    }

    @Override
    public void close() throws ItemStreamException {
    }

    public void setJudgmentRepository(JudgmentRepository judgmentRepository) {
        this.judgmentRepository = judgmentRepository;
    }

}
