package pl.edu.icm.saos.persistence.enrichment.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * 
 * A production data enrichment tag. <br/> <br/>
 * See {@link UploadEnrichmentTag} for an enrichment tag class used during upload process 
 * 
 * @author Łukasz Dumiszewski
 */
@Entity
@Table(uniqueConstraints={@UniqueConstraint(name="tag_judgment_type_value_unique", columnNames={"judgmentId", "tagType"})})
@SequenceGenerator(name = "seq_enrichment_tag", allocationSize = 1, sequenceName = "seq_enrichment_tag")
public class EnrichmentTag extends AbstractEnrichmentTag {

    
    //------------------------ GETTERS --------------------------
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_enrichment_tag")
    @Override
    public long getId() {
        return id;
    }


    


}
