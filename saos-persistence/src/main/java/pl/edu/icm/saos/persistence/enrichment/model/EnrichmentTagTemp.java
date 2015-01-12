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
 * An data enrichment tag used during an upload process. After the upload process has finished, the data is moved
 * into {@link EnrichmentTag} table which is then used in SAOS typical functions.<br/> <br/>
 * See {@link EnrichmentTag} for an enrichment tag class used normally in system 
 * 
 * @author Łukasz Dumiszewski
 */
@Entity
@Table(uniqueConstraints={@UniqueConstraint(name="tag_temp_judgment_type_value_unique", columnNames={"judgmentId", "tagType"})})
@Cacheable(true)
@SequenceGenerator(schema="importer", name = "seq_enrichment_tag_temp", allocationSize = 1, sequenceName = "seq_enrichment_tag_temp")
public class EnrichmentTagTemp extends AbstractEnrichmentTag {


    
    //------------------------ GETTERS --------------------------
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_enrichment_tag_temp")
    @Override
    public int getId() {
        return id;
    }


    


}
