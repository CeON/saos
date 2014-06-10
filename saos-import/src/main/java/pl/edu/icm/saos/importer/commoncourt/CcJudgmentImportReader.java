package pl.edu.icm.saos.importer.commoncourt;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Service;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

/**
 * @author Łukasz Dumiszewski
 */
@Service
public class CcJudgmentImportReader implements ItemReader<String> {

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
