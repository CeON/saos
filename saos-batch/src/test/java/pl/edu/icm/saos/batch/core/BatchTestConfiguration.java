package pl.edu.icm.saos.batch.core;

import java.sql.SQLException;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.jdbc.datasource.init.ScriptException;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import pl.edu.icm.saos.batch.core.BatchConfiguration;
import pl.edu.icm.saos.common.TestConfigurationBase;
import pl.edu.icm.saos.importer.ImportTestConfiguration;
import pl.edu.icm.saos.search.SearchTestConfiguration;

/**
 * @author Łukasz Dumiszewski
 */
@Import({BatchConfiguration.class, ImportTestConfiguration.class, SearchTestConfiguration.class})
public class BatchTestConfiguration extends TestConfigurationBase {
    
    private static Logger log = LoggerFactory.getLogger(BatchTestConfiguration.class);
    
    @Autowired
    private DataSource dataSource;
    
     
    @PostConstruct
    public void postConstruct() throws ScriptException, SQLException {
        recreateSpringBatchTables();
        
    }


    //------------------------ PRIVATE --------------------------
    
    private void recreateSpringBatchTables() throws SQLException {
        log.debug("dropping spring batch tables");
        ClassPathResource resource = new ClassPathResource("dropBatchTables.sql");
        ScriptUtils.executeSqlScript(dataSource.getConnection(), new EncodedResource(resource, "UTF-8"));
        
        log.debug("creating spring batch tables");
        resource = new ClassPathResource("createBatchTables.sql");
        ScriptUtils.executeSqlScript(dataSource.getConnection(), new EncodedResource(resource, "UTF-8"));
    }
    
    
   
    
}
