package pl.edu.icm.saos.importer.commoncourt;

import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Service;

/**
 * @author Łukasz Dumiszewski
 */
@Service
public class CcJudgmentImportProcessor implements ItemProcessor<String, String> {

    @Override
    public String process(String item) throws Exception {
        return StringUtils.reverse(item);
    }

}
