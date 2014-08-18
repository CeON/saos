package pl.edu.icm.saos.persistence.repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.common.InitializingVisitor;
import pl.edu.icm.saos.persistence.model.Judgment;

/**
 * @author Łukasz Dumiszewski
 */
@Service("judgmentRepositoryCustom")
public class JudgmentRepositoryCustomImpl implements JudgmentRepositoryCustom {

    @Autowired
    private EntityManager entityManager;
    
    @Override
    @Transactional
    public <T extends Judgment> T findOneAndInitialize(int id) {
        Judgment judgment = entityManager.find(Judgment.class, id);
        
        if (judgment != null) {
            judgment.accept(new InitializingVisitor());
        }
        
        @SuppressWarnings("unchecked")
        T retJudgment = (T)judgment;
        return retJudgment;
        
    }

}
