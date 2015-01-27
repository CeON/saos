package pl.edu.icm.saos.persistence.repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.common.InitializingVisitor;
import pl.edu.icm.saos.persistence.model.CommonCourt;

/**
 * @author pavtel
 */
@Service("commonCourtRepositoryCustom")
public class CommonCourtRepositoryCustomImpl implements CommonCourtRepositoryCustom {

    @Autowired
    private EntityManager entityManager;

    @Override
    @Transactional
    public CommonCourt findOneAndInitialize(long id){

        CommonCourt commonCourt = entityManager.find(CommonCourt.class, id);

        if (commonCourt != null) {
            commonCourt.accept(new InitializingVisitor());
        }

        return commonCourt;
    }
}
