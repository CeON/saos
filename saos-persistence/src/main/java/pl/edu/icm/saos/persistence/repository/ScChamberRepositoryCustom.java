package pl.edu.icm.saos.persistence.repository;

import pl.edu.icm.saos.persistence.model.SupremeCourtChamber;

/**
 * @author pavtel
 */
public interface ScChamberRepositoryCustom {

    /**
     * Finds {@link pl.edu.icm.saos.persistence.model.SupremeCourtChamber SupremeCourtChamber} with the given id ({pl.edu.icm.saos.persistence.model.SupremeCourtChamber#getId()}), initializes it (the whole tree)
     * and returns it.
     */
    public SupremeCourtChamber findOneAndInitialize(int id);
}
