package pl.edu.icm.saos.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.edu.icm.saos.persistence.model.CommonCourtDivision;

/**
 * @author Łukasz Dumiszewski
 */
public interface CcDivisionRepository extends JpaRepository<CommonCourtDivision, Integer> { 

    CommonCourtDivision findOneByCourtIdAndCode(int courtId, String code);
    
}
