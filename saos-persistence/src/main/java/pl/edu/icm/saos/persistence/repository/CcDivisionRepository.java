package pl.edu.icm.saos.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.edu.icm.saos.persistence.model.CommonCourtDivision;

/**
 * @author Łukasz Dumiszewski
 */
public interface CcDivisionRepository extends JpaRepository<CommonCourtDivision, Long> { 

    CommonCourtDivision findOneByCourtIdAndCode(long courtId, String code);
    
    List<CommonCourtDivision> findAllByCourtId(long courtId);
}
