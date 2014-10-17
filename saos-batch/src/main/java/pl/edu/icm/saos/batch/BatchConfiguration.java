package pl.edu.icm.saos.batch;

import javax.sql.DataSource;

import org.springframework.batch.admin.service.SimpleJobService;
import org.springframework.batch.admin.service.SimpleJobServiceFactoryBean;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.ListableJobLocator;
import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.configuration.support.MapJobRegistry;
import org.springframework.batch.core.explore.support.JobExplorerFactoryBean;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import pl.edu.icm.saos.batch.admin.SaosJobService;
import pl.edu.icm.saos.batch.admin.SaosJobServiceAdapter;

/**
 * @author Łukasz Dumiszewski
 */
@Configuration
@EnableBatchProcessing
@Import({ CcjImportJobConfiguration.class, ScjImportJobConfiguration.class, IndexingJobConfiguration.class })
public class BatchConfiguration implements BatchConfigurer {
    
    @Autowired
    private PlatformTransactionManager transactionManager;
    
    @Autowired
    private DataSource dataSource;
    
    @Autowired
    private ListableJobLocator jobLocator;
    
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
   
    @Bean
    public JobExplorerFactoryBean jobExplorer() {
        JobExplorerFactoryBean jobExplorer = new JobExplorerFactoryBean();
        jobExplorer.setDataSource(dataSource);
        return jobExplorer;
    }
    
    /** job launcher used for executing jobs on http requests, here: for batch admin */
    @Bean 
    public JobLauncher asyncJobLauncher() throws Exception {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(jobRepository());
        jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }
    
    
    @Bean
    public JobRegistry jobRegistry() {
        return new MapJobRegistry();
    }

    
    @Bean
    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor() {
        JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor = new JobRegistryBeanPostProcessor();
        jobRegistryBeanPostProcessor.setJobRegistry(jobRegistry());
        return jobRegistryBeanPostProcessor;
    }
    
    @Bean
    public SaosJobService jobService() throws Exception {
        SimpleJobServiceFactoryBean jobServiceFactory = new SimpleJobServiceFactoryBean();
        jobServiceFactory.setJobRepository(jobRepository());
        jobServiceFactory.setDataSource(dataSource);
        jobServiceFactory.setJobLauncher(jobLauncher());
        jobServiceFactory.setJobLocator(jobLocator);
        jobServiceFactory.afterPropertiesSet();
        return new SaosJobServiceAdapter((SimpleJobService)jobServiceFactory.getObject());
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
