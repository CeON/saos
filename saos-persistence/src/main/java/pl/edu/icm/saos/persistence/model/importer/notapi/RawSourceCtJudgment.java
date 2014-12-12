package pl.edu.icm.saos.persistence.model.importer.notapi;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import pl.edu.icm.saos.persistence.model.importer.RawSourceJudgment;

@Table(schema="importer", name="notapi_raw_source_ct_judgment")
@Entity
@Cacheable(true)
@SequenceGenerator(name = "seq_notapi_raw_source_ct_judgment", allocationSize = 1, sequenceName = "seq_notapi_raw_source_ct_judgment")
public class RawSourceCtJudgment extends RawSourceJudgment {

    private String jsonContent;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_notapi_raw_source_ct_judgment")
    @Override
    public int getId() {
        return id;
    }

    public String getJsonContent() {
        return jsonContent;
    }

    public void setJsonContent(String jsonContent) {
        this.jsonContent = jsonContent;
    }

}
