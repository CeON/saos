package pl.edu.icm.saos.batch.core;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Service;

/**
 * @author Łukasz Dumiszewski
 */
@Service
public class TestReader implements ItemReader<String> {

    private int counter = 0;
    
    @Override
    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        while (counter<100) {
            counter++;
            return RandomStringUtils.randomAlphanumeric(10);
        }
        return null;
    }

   

}
