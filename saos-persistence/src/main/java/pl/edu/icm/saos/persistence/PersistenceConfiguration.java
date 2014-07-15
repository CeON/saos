package pl.edu.icm.saos.persistence;

import java.sql.SQLException;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.persistence.SharedCacheMode;
import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate4.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author Łukasz Dumiszewski
 */
@Configuration
@ComponentScan
@EnableJpaRepositories(repositoryImplementationPostfix="CustomImpl")
@EnableTransactionManagement
public class PersistenceConfiguration {
    
    private static Logger log = LoggerFactory.getLogger(PersistenceConfiguration.class);
    
    @Autowired
    private Environment env;

    
    @Bean
    public DataSource dataSource() {
        log.info("== DATASOURCE URL: " + env.getProperty("datasource.url") + "  == ");
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(env.getProperty("datasource.driverClassName"));
        dataSource.setUrl(env.getProperty("datasource.url"));
        dataSource.setUsername(env.getProperty("datasource.username"));
        dataSource.setPassword(env.getProperty("datasource.password"));
        return dataSource;
        
    }
    
    
    /*@Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setHibernateProperties(hibernateProperties());
        sessionFactory.setPackagesToScan("pl.edu.icm.saos.persistence.model");
        sessionFactory.setNamingStrategy(new CustomDbNamingStrategy());
        return sessionFactory;
    }*/
    
    @Bean
    public EntityManagerFactory entityManagerFactory() {
       LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
       emf.setDataSource(dataSource());
       emf.setPackagesToScan(new String[] {"pl.edu.icm.saos.persistence.model"});
       emf.setSharedCacheMode(SharedCacheMode.ENABLE_SELECTIVE);
       HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
       emf.setJpaVendorAdapter(vendorAdapter);
       emf.setJpaProperties(hibernateProperties());
       emf.afterPropertiesSet();
       return emf.getObject();
    }
    
    
    public Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
        properties.setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
        properties.setProperty("hibernate.format_sql", "true");
        properties.setProperty("hibernate.generate_statistics", env.getProperty("hibernate.generate_statistics"));
        properties.setProperty("hibernate.cache.use_second_level_cache", "true");        
        properties.setProperty("hibernate.cache.region.factory_class", "org.hibernate.cache.SingletonEhCacheRegionFactory");
        properties.setProperty("net.sf.ehcache.configurationResourceName", env.getProperty("net.sf.ehcache.configurationResourceName"));
        properties.setProperty("hibernate.ejb.naming_strategy", "pl.edu.icm.saos.persistence.common.CustomDbNamingStrategy");
        properties.setProperty("jadira.usertype.autoRegisterUserTypes", env.getProperty("jadira.usertype.autoRegisterUserTypes"));
        properties.setProperty("jadira.usertype.databaseZone", env.getProperty("jadira.usertype.databaseZone"));
        properties.setProperty("jadira.usertype.javaZone", env.getProperty("jadira.usertype.javaZone"));
        return properties;
    }
    
    @Bean
    public PlatformTransactionManager transactionManager() throws SQLException {

      JpaTransactionManager txManager = new JpaTransactionManager(entityManagerFactory());
      return txManager;
    }

    @Bean
    public HibernateExceptionTranslator hibernateExceptionTranslator() {
      return new HibernateExceptionTranslator();
    }
    
    /*@Bean
    @Autowired
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
       HibernateTransactionManager txManager = new HibernateTransactionManager();
       txManager.setSessionFactory(sessionFactory);
       return txManager;
    }*/
    
}
