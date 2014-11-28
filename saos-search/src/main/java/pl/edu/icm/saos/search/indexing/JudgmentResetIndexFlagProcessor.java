package pl.edu.icm.saos.search.indexing;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.Judgment;

/**
 * Spring Batch processor reseting indexed flag in {@link Judgment judgments}.
 * 
 * @author madryk
 */
@Service
public class JudgmentResetIndexFlagProcessor implements ItemProcessor<Judgment, Judgment> {

    @Override
    public Judgment process(Judgment judgment) throws Exception {
        judgment.resetIndexedFlag();
        return judgment;
    }

}
