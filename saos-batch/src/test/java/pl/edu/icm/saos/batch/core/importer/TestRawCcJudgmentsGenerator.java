package pl.edu.icm.saos.batch.core.importer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.jdbc.datasource.init.ScriptException;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.importer.RawSourceCcJudgment;
import pl.edu.icm.saos.persistence.repository.RawSourceCcJudgmentRepository;

/**
 * @author Łukasz Dumiszewski
 */
@Service("testRawCcJudgmentsGenerator")
public class TestRawCcJudgmentsGenerator {

    private static final String RAW_JUDGMENT_SQL_PATH = "testRawSourceCcJudgments.sql";
    
    @Autowired
    private DataSource dataSource;
    
    @Autowired
    private EntityManager entityManager;
    
    @Autowired
    private RawSourceCcJudgmentRepository rawSourceCcJudgmentRepository;
    
    
    @Transactional
    public void generateTestRawCcJudgments() throws ScriptException, SQLException {
        assertEquals(0, rawSourceCcJudgmentRepository.count());
        ClassPathResource resource = new ClassPathResource(RAW_JUDGMENT_SQL_PATH);
        
        ScriptUtils.executeSqlScript(dataSource.getConnection(), new EncodedResource(resource, "UTF-8"));
        assertTrue(rawSourceCcJudgmentRepository.count()>10);
        entityManager.createQuery("update " + RawSourceCcJudgment.class.getName() + " set processingDate = null, processed = false, processingSkipReason = null").executeUpdate();
        entityManager.flush();
        
    }
 
}
