package pl.edu.icm.saos.persistence.model.importer.notapi;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author madryk
 */
@Table(schema="importer", name="notapi_raw_source_ct_judgment")
@Entity
@Cacheable(true)
public class RawSourceCtJudgment extends JsonRawSourceJudgment {

}
