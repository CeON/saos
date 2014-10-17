package pl.edu.icm.saos.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.edu.icm.saos.persistence.model.SupremeCourtChamberDivision;

/**
 * @author Łukasz Dumiszewski
 */

public interface ScChamberDivisionRepository extends JpaRepository<SupremeCourtChamberDivision, Integer> {

    
    SupremeCourtChamberDivision findOneByFullName(String fullName);

}
