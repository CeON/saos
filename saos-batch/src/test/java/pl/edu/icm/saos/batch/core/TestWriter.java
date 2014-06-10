package pl.edu.icm.saos.batch.core;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Service;

/**
 * @author Łukasz Dumiszewski
 */
@Service
public class TestWriter implements ItemWriter<String> {

    private Logger log = LoggerFactory.getLogger(TestWriter.class);
    
    @Override
    public void write(List<? extends String> items) throws Exception {
        log.info("writing: {}", items);
        
    }

}
