package pl.edu.icm.saos.webapp;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;

@Configuration
@EnableWebMvcSecurity
@ComponentScan(basePackages="pl.edu.icm.crpd.webapp.security")
public class SecurityConfiguration {

    public static final String CSRF_TOKEN_SESSION_ATTR_NAME = "HTTP_CSRF_TOKEN";
    
    
    @Configuration
    @Order(1)
    public static class BatchAdminSecurityConfiguration extends WebSecurityConfigurerAdapter {
    
        
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                .requestMatchers().antMatchers("/batch/**/*")
                .and()
                .authorizeRequests()
                    .anyRequest().permitAll()
                    .and().csrf().disable()
                ;
        }
        
       
        
    }

    @Configuration
    @Order(2)
    public static class EnrichmentUploadSecurityConfiguration extends WebSecurityConfigurerAdapter {
    
        
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                .requestMatchers().antMatchers(HttpMethod.PUT, "/api/enrichment/tags")
                .and()
                .authorizeRequests()
                    .anyRequest().permitAll()
                    .and().csrf().disable()
                    
                    
                ;
        }
        
    }
    
    
    @Configuration
    @Order(3)
    public static class MainSecurityConfiguration extends WebSecurityConfigurerAdapter {
    
        
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                .requestMatchers().anyRequest()
                .and()
                .authorizeRequests()
                    .anyRequest().permitAll()
                    
                ;
        }
        
       
        
    }

  

}

