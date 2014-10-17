package pl.edu.icm.saos.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.edu.icm.saos.persistence.model.SupremeCourtChamber;

/**
 * @author Łukasz Dumiszewski
 */

public interface ScChamberRepository extends JpaRepository<SupremeCourtChamber, Integer> {

    
    SupremeCourtChamber findOneByName(String name);

}