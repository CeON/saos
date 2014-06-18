package pl.edu.icm.saos.persistence.repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.icm.saos.common.util.CommonCourtUtils;
import pl.edu.icm.saos.persistence.model.CommonCourt;

import com.google.common.base.Preconditions;

/**
 * @author Łukasz Dumiszewski
 */

public class CommonCourtRepositoryCustomImpl implements CommonCourtRepositoryCustom {

    @Autowired
    private EntityManager entityManager;
    
    @Override
    public CommonCourt getOneByCode(String courtCode) throws NoResultException {
        Preconditions.checkArgument(CommonCourtUtils.isValidCommonCourtCode(courtCode));
        
        StringBuilder qb = new StringBuilder();
        qb.append("select cc from " + CommonCourt.class.getName() + " cc where cc.appealCourtCode = :appealCourtCode ");
        qb.append(" and cc.regionalCourtCode = :regionalCourtCode ");
        qb.append(" and cc.districtCourtCode = :districtCourtCode ");
        
        TypedQuery<CommonCourt> q = entityManager.createQuery(qb.toString(), CommonCourt.class)
                                                    .setParameter("appealCourtCode",   CommonCourtUtils.extractAppealCourtNumber(courtCode))
                                                    .setParameter("regionalCourtCode", CommonCourtUtils.extractRegionalCourtNumber(courtCode))
                                                    .setParameter("districtCourtCode", CommonCourtUtils.extractDistrictCourtNumber(courtCode));
        return q.getSingleResult();
        
    }

}
