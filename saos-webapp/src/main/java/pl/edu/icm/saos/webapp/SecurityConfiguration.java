package pl.edu.icm.saos.webapp;


import static pl.edu.icm.saos.enrichment.upload.EnrichmentTagUploadResponseMessages.ERROR_ACCESS_DENIED;

import java.util.Locale;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;

import pl.edu.icm.saos.common.service.ServiceResponse;
import pl.edu.icm.saos.webapp.security.LocaleSettingFilter;
import pl.edu.icm.saos.webapp.security.ServiceBasicAuthenticationEntryPoint;

import com.google.common.collect.Lists;

@Configuration
@EnableWebMvcSecurity
@ComponentScan(basePackages="pl.edu.icm.crpd.webapp.security")
public class SecurityConfiguration {

    
    public static final String CSRF_TOKEN_SESSION_ATTR_NAME = "HTTP_CSRF_TOKEN";
    
    @Autowired
    private LocaleResolver localeResolver;
    
    @Autowired
    private MessageSource messageSource;
    
    
    //------------------------ CONFIGURATIONS --------------------------
    
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
    public static class EnricherSecurityConfiguration extends WebSecurityConfigurerAdapter {
    
        @Autowired
        private HttpMessageConverter<?> mappingJackson2HttpMessageConverter;
        
        @Autowired
        private PasswordEncoder passwordEncoder;
        
        @Autowired
        @Qualifier("localeSettingFilter")
        private Filter localeSettingFilter;
        
        @Autowired
        private MessageSource messageSource;
        
        @Value("${enrichment.enricher.login}")
        private String enricherLogin;
        
        @Value("${enrichment.enricher.passwordHash}")
        private String enricherPasswordHash;
        
        
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                .requestMatchers().antMatchers(HttpMethod.PUT, "/api/enrichment/tags")
                .and()
                .authorizeRequests()
                    .anyRequest().authenticated()
                    .and().csrf().disable()
                .authenticationProvider(enricherAuthenticationProvider())
                .httpBasic().authenticationEntryPoint(serviceBasicAuthenticationEntryPoint())
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf().disable()
                .addFilterBefore(localeSettingFilter, BasicAuthenticationFilter.class)
                    
                ;
        }
        
        @SuppressWarnings("unchecked")
        private ServiceBasicAuthenticationEntryPoint serviceBasicAuthenticationEntryPoint() {
            ServiceBasicAuthenticationEntryPoint authenticationEntryPoint = new ServiceBasicAuthenticationEntryPoint();
            authenticationEntryPoint.setMessageConverter((HttpMessageConverter<ServiceResponse>)mappingJackson2HttpMessageConverter);
            authenticationEntryPoint.setBasicRealm("/api/enrichment/tags");
            authenticationEntryPoint.setMainMessage(ERROR_ACCESS_DENIED);
            return authenticationEntryPoint;
        }
        
        private AuthenticationProvider enricherAuthenticationProvider() {
            DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
            authenticationProvider.setUserDetailsService(enricherUserService());
            authenticationProvider.setPasswordEncoder(passwordEncoder);
            authenticationProvider.setMessageSource(messageSource);
            return authenticationProvider;
        }
        
        private InMemoryUserDetailsManager enricherUserService() {
            UserDetails admin = new User(enricherLogin, enricherPasswordHash, Lists.newArrayList());
            InMemoryUserDetailsManager userService = new InMemoryUserDetailsManager(Lists.newArrayList(admin));
            return userService;
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
    
    
    //------------------------ BEANS --------------------------
    
    
    @Bean 
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder;
        
    }

    @Bean
    public Filter localeSettingFilter() {
        LocaleSettingFilter filter = new LocaleSettingFilter();
        filter.setLocaleResolver(new FixedLocaleResolver(Locale.ENGLISH));
        return filter;
    }
  

}

