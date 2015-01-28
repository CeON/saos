package pl.edu.icm.saos.persistence.enrichment.model;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * 
 * An enrichment tag class used during an upload process. After the upload process has finished, the data is moved
 * into {@link EnrichmentTag} table which is then used in SAOS typical functions.<br/> <br/>
 * See {@link EnrichmentTag} for an enrichment tag class used normally in system 
 * 
 * @author Łukasz Dumiszewski
 */
@Entity
@Table(uniqueConstraints={@UniqueConstraint(name="upload_tag_judgment_type_value_unique", columnNames={"judgmentId", "tagType"})})
@Cacheable(true)
@SequenceGenerator(name = "seq_upload_enrichment_tag", allocationSize = 1, sequenceName = "seq_upload_enrichment_tag")
public class UploadEnrichmentTag extends AbstractEnrichmentTag {


    
    //------------------------ GETTERS --------------------------
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_upload_enrichment_tag")
    @Override
    public long getId() {
        return id;
    }


    


}
