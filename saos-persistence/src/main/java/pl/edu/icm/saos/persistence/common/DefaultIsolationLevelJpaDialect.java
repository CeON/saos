package pl.edu.icm.saos.persistence.common;

import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * @author Łukasz Dumiszewski
 */

public class DefaultIsolationLevelJpaDialect extends HibernateJpaDialect {

    private static Logger log = LoggerFactory.getLogger(DefaultIsolationLevelJpaDialect.class);
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    
    @Override
    public Object beginTransaction(EntityManager entityManager, TransactionDefinition definition)
            throws PersistenceException, SQLException, TransactionException {
        if (definition.getIsolationLevel()!=TransactionDefinition.ISOLATION_DEFAULT) {
            log.warn("changing isolation level to ISOLATION_DEFAULT, jpa does not handle custom isolation levels");
        }
        DefaultTransactionDefinition def = new DefaultTransactionDefinition(definition);
        def.setIsolationLevelName("ISOLATION_DEFAULT");
        
        return super.beginTransaction(entityManager, def);
    }
    
    
    @Override
    public void cleanupTransaction(Object transactionData) {
        if (transactionData != null) {
            super.cleanupTransaction(transactionData);
        }
    }

}
