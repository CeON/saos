package pl.edu.icm.saos.importer.notapi.common;

import pl.edu.icm.saos.persistence.model.importer.RawSourceJudgment;

/**
 * Creates {@link RawSourceJudgment} from {@literal String}
 * 
 * @author madryk
 * @param <T> - type of created {@link RawSourceJudgment}
 */
public interface RawSourceJudgmentParser<T extends RawSourceJudgment> {

    /**
     * Creates {@link RawSourceJudgment} from {@literal String}
     * 
     * @param item
     * @return rawSourceJudgment
     */
    T createRawSourceJudgment(String item);
}
