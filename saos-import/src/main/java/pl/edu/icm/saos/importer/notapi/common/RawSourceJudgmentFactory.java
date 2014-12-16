package pl.edu.icm.saos.importer.notapi.common;

import pl.edu.icm.saos.persistence.model.importer.RawSourceJudgment;

/**
 * Creates {@link RawSourceJudgment} from json
 * 
 * @author madryk
 * @param <T> - type of created {@link RawSourceJudgment}
 */
public interface RawSourceJudgmentFactory<T extends RawSourceJudgment> {

    /**
     * Creates {@link RawSourceJudgment} from json {@literal String}
     * 
     * @param jsonContent
     * @return rawSourceJudgment
     */
    T createRawSourceJudgment(String jsonContent);
}
