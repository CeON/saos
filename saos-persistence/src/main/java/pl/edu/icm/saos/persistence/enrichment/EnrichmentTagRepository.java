package pl.edu.icm.saos.persistence.enrichment;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTag;

/**
 * @author Łukasz Dumiszewski
 */

public interface EnrichmentTagRepository extends JpaRepository<EnrichmentTag, Integer> {

}
