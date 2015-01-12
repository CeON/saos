package pl.edu.icm.saos.persistence.enrichment;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTagTemp;


/**
 * @author Łukasz Dumiszewski
 */

public interface EnrichmentTagTempRepository extends JpaRepository<EnrichmentTagTemp, Integer> {

}
