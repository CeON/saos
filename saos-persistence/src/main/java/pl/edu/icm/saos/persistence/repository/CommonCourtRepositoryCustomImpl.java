package pl.edu.icm.saos.persistence.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.icm.saos.persistence.common.InitializingVisitor;
import pl.edu.icm.saos.persistence.model.CommonCourt;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

/**
 * @author pavtel
 */
@Service("CommonCourtRepositoryCustom")
public class CommonCourtRepositoryCustomImpl implements CommonCourtRepositoryCustom {

    @Autowired
    private EntityManager entityManager;

    @Override
    @Transactional
    public CommonCourt findOneAndInitialize(int id){

        CommonCourt element = entityManager.find(CommonCourt.class, id);

        if (element != null) {
            element.accept(new InitializingVisitor());
        }

        return element;
    }
}
