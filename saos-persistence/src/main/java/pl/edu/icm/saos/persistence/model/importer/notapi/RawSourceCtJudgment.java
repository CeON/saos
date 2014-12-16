package pl.edu.icm.saos.persistence.model.importer.notapi;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Table;

import pl.edu.icm.saos.persistence.model.importer.RawSourceJudgment;

@Table(schema="importer", name="notapi_raw_source_ct_judgment")
@Entity
@Cacheable(true)
public class RawSourceCtJudgment extends RawSourceJudgment {

    private String jsonContent;

    
    //------------------------ GETTERS --------------------------

    public String getJsonContent() {
        return jsonContent;
    }

    public void setJsonContent(String jsonContent) {
        this.jsonContent = jsonContent;
    }

}
