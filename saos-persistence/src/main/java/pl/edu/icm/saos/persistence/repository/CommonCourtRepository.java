package pl.edu.icm.saos.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.edu.icm.saos.persistence.model.CommonCourt;

/**
 * @author Łukasz Dumiszewski
 */

public interface CommonCourtRepository extends JpaRepository<CommonCourt, Integer>, CommonCourtRepositoryCustom{

    
    public CommonCourt findOneByCode(String code);
    
}
