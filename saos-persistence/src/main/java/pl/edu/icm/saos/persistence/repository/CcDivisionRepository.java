package pl.edu.icm.saos.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.edu.icm.saos.persistence.model.CommonCourtDivision;

/**
 * @author Ĺ�ukasz Dumiszewski
 */
public interface CcDivisionRepository extends JpaRepository<CommonCourtDivision, Integer> { 

    CommonCourtDivision findOneByCourtIdAndCode(int courtId, String code);
    
    List<CommonCourtDivision> findAllByCourtId(int courtId);
}
