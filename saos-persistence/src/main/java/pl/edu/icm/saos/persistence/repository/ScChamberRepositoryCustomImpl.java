package pl.edu.icm.saos.persistence.repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.common.InitializingVisitor;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamber;

/**
 * @author pavtel
 */
@Service("ScChamberRepositoryCustom")
public class ScChamberRepositoryCustomImpl implements ScChamberRepositoryCustom {


    @Autowired
    private EntityManager entityManager;


    @Override
    @Transactional
    public SupremeCourtChamber findOneAndInitialize(long id) {
        SupremeCourtChamber chamber = entityManager.find(SupremeCourtChamber.class, id);

        if(chamber != null){
            chamber.accept(new InitializingVisitor());
        }

        return chamber;
    }
}
