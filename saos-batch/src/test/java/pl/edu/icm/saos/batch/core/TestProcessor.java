package pl.edu.icm.saos.batch.core;

import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Service;

/**
 * @author Łukasz Dumiszewski
 */
@Service
public class TestProcessor implements ItemProcessor<String, String> {

    @Override
    public String process(String item) throws Exception {
        return StringUtils.reverse(item);
    }

}
