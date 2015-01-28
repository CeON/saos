package pl.edu.icm.saos.persistence.repository;

import pl.edu.icm.saos.persistence.model.CommonCourt;

/**
 * @author pavtel
 */
public interface CommonCourtRepositoryCustom  {

    /**
     * Finds {@link pl.edu.icm.saos.persistence.model.CommonCourt CommonCourt} with the given id ({pl.edu.icm.saos.persistence.model.CommonCourt#getId()}), initializes it (the whole tree)
     * and returns it.
     */
    public CommonCourt findOneAndInitialize(long id);

}
