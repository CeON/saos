package pl.edu.icm.saos.persistence.service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Łukasz Dumiszewski
 */
@Service("commonCourtHierarchyUpdater")
public class CommonCourtHierarchyUpdater {

    @Autowired
    private EntityManager entityManager;
    
    @Transactional
    public void updateCommonCourtHierarchy() {
        
        String updateDistrictParentSql = " update common_court child set fk_parent_court = parent.id " +
                         " from common_court parent " +
                         " where  left(child.code, 6) = left(parent.code, 6) " +
                         " and child.type = 'DISTRICT' "+
                         " and parent.type = 'REGIONAL' "; 
              
        String updateRegionalParentSql = "update common_court child set fk_parent_court = parent.id " +
                         " from common_court parent " +
                         " where  left(child.code, 4) = left(parent.code, 4) " +
                         " and child.type = 'REGIONAL' " +
                         " and parent.type = 'APPEAL'"; 
              
        
        entityManager.createNativeQuery(updateDistrictParentSql).executeUpdate();
        entityManager.createNativeQuery(updateRegionalParentSql).executeUpdate();
    }
    
}
