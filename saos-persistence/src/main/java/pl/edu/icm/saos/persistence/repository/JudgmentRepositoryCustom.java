package pl.edu.icm.saos.persistence.repository;

import java.util.List;

import pl.edu.icm.saos.persistence.model.Judgment;

/**
 * 
 * Defines contract for repository methods that can not be specified by annotations or specific
 * method names and have to be written 'by hand'.
 * 
 * 
 * @author Łukasz Dumiszewski
 */

public interface JudgmentRepositoryCustom {

    /**
     * Finds {@link Judgment} with the given id ({@link Judgment#getId()}), initializes it (the whole tree)
     * and returns it.
     */
    public <T extends Judgment> T findOneAndInitialize(int id);

    /**
     * Deletes {@link Judgment}s with the given judgmentIds. 
     */
    public void delete(List<Integer> judgmentIds);
}
