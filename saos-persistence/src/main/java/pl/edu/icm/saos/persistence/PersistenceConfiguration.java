package pl.edu.icm.saos.persistence;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import pl.edu.icm.saos.persistence.common.CustomDbNamingStrategy;

/**
 * @author Łukasz Dumiszewski
 */
@Configuration
@ComponentScan
//@EnableJpaRepositories
@EnableTransactionManagement
public class PersistenceConfiguration {
    
    @Autowired
    private Environment env;

    
    @Bean
    public DataSource dataSource() {
        
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(env.getProperty("datasource.driverClassName"));
        dataSource.setUrl(env.getProperty("datasource.url"));
        dataSource.setUsername(env.getProperty("datasource.username"));
        dataSource.setPassword(env.getProperty("datasource.password"));
        return dataSource;
        
    }
    
    
    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setHibernateProperties(hibernateProperties());
        sessionFactory.setPackagesToScan("pl.edu.icm.saos.persistence.model");
        sessionFactory.setNamingStrategy(new CustomDbNamingStrategy());
        return sessionFactory;
    }
    
    
    public Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
        properties.setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
        properties.setProperty("hibernate.format_sql", "true");
        properties.setProperty("hibernate.generate_statistics", env.getProperty("hibernate.generate_statistics"));
        properties.setProperty("hibernate.cache.use_second_level_cache", "true");        
        properties.setProperty("hibernate.cache.region.factory_class", "org.hibernate.cache.EhCacheRegionFactory");
        properties.setProperty("net.sf.ehcache.configurationResourceName", env.getProperty("net.sf.ehcache.configurationResourceName"));
        return properties;
    }
    
    
    @Bean
    @Autowired
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
       HibernateTransactionManager txManager = new HibernateTransactionManager();
       txManager.setSessionFactory(sessionFactory);
       return txManager;
    }
    
}
