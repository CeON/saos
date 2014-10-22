package pl.edu.icm.saos.persistence.repository;

import java.util.List;

import pl.edu.icm.saos.persistence.model.Judgment;

/**
 * @author Łukasz Dumiszewski
 */

public interface JudgmentRepositoryCustom {

    
    public <T extends Judgment> T findOneAndInitialize(int id);
    
    public void delete(List<Integer> judgmentIds);
}
