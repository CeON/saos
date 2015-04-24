package pl.edu.icm.saos.webapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import pl.edu.icm.saos.common.cache.MethodSignatureKeyGenerator;

/**
 * @author Łukasz Dumiszewski
 */
@Configuration
@EnableCaching
public class CacheConfiguration implements CachingConfigurer {

    @Autowired
    private ApplicationContext ctx;

    @Value("${ehcache.configurationFilePath}")
    private String ehcacheConfigurationFilePath;
    
    
    @Bean
    public KeyGenerator keyGenerator() {
        return new MethodSignatureKeyGenerator();
    }
    
    
    @Bean
    public EhCacheCacheManager cacheManager() {
        EhCacheCacheManager cacheManager = new EhCacheCacheManager();
        cacheManager.setCacheManager(ehCacheManagerFactory().getObject());
        return cacheManager;
    }

    
    @Bean
    public EhCacheManagerFactoryBean ehCacheManagerFactory() {
        EhCacheManagerFactoryBean ehCacheManagerFactory = new EhCacheManagerFactoryBean();
        ehCacheManagerFactory.setConfigLocation(ctx.getResource(ehcacheConfigurationFilePath));
        return ehCacheManagerFactory;
    }
    
    
    
     
    
    
    
    
}
