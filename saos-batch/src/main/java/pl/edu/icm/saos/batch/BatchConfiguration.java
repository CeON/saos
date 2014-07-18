package pl.edu.icm.saos.batch;

import javax.sql.DataSource;

import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * @author Łukasz Dumiszewski
 */
@Configuration
@EnableBatchProcessing
@Import(JobConfiguration.class)
public class BatchConfiguration implements BatchConfigurer {
    
    @Autowired
    private PlatformTransactionManager transactionManager;
    
    @Autowired
    private DataSource dataSource;
    
    
    @Bean
    public JobRepository jobRepository() throws Exception {
        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
        factory.setDataSource(dataSource);
        factory.setTransactionManager(transactionManager);
        factory.afterPropertiesSet();
        factory.setIsolationLevelForCreate("ISOLATION_DEFAULT");
        
        return factory.getObject();        
    }
    
    @Bean 
    public JobLauncher jobLauncher() throws Exception {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(jobRepository());
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }
   
    
    //------------------------ BatchConfigurer Implementation --------------------------
    
    @Override
    public JobRepository getJobRepository() throws Exception {
        return jobRepository();
    }

    @Override
    public PlatformTransactionManager getTransactionManager() throws Exception {
        return transactionManager;
    }

    @Override
    public JobLauncher getJobLauncher() throws Exception {
        return jobLauncher();
    }
    
    
}
