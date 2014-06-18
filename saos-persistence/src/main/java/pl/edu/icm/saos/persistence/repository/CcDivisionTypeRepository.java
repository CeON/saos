package pl.edu.icm.saos.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.edu.icm.saos.persistence.model.CommonCourtDivisionType;

/**
 * @author Łukasz Dumiszewski
 */

public interface CcDivisionTypeRepository extends JpaRepository<CommonCourtDivisionType, Integer> {

    CommonCourtDivisionType findByCode(String code);
    
}
