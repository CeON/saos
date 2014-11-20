package pl.edu.icm.saos.persistence.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.icm.saos.persistence.common.InitializingVisitor;
import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamber;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

/**
 * @author pavtel
 */
@Service("ScChamberRepositoryCustom")
public class ScChamberRepositoryCustomImpl implements ScChamberRepositoryCustom {


    @Autowired
    private EntityManager entityManager;


    @Override
    @Transactional
    public SupremeCourtChamber findOneAndInitialize(int id) {
        SupremeCourtChamber chamber = entityManager.find(SupremeCourtChamber.class, id);

        if(chamber != null){
            chamber.accept(new InitializingVisitor());
        }

        return chamber;
    }
}
