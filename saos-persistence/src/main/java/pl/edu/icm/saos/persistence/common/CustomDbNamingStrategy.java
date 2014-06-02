package pl.edu.icm.saos.persistence.common;

import org.hibernate.cfg.ImprovedNamingStrategy;

/**
 * Prepends 'fk_' to foreign key column names
 *  
 * @author Łukasz Dumiszewski
 */

public class CustomDbNamingStrategy extends ImprovedNamingStrategy {

    private static final long serialVersionUID = 1L;

    @Override
    public String foreignKeyColumnName(String propertyName,
            String propertyEntityName, String propertyTableName,
            String referencedColumnName) {
        return "fk_" + super.foreignKeyColumnName(propertyName, propertyEntityName, propertyTableName, referencedColumnName);
    }
}
