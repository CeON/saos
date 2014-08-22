package pl.edu.icm.saos.webapp;

import javax.servlet.Filter;

import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import pl.edu.icm.saos.api.ApiConfiguration;
import pl.edu.icm.saos.batch.BatchConfiguration;
import pl.edu.icm.saos.importer.ImportConfiguration;
import pl.edu.icm.saos.persistence.PersistenceConfiguration;
import pl.edu.icm.saos.search.SearchConfiguration;


   
public class DispatcherServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
   
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[] { GeneralConfiguration.class, SecurityConfiguration.class, BatchConfiguration.class, PersistenceConfiguration.class, ImportConfiguration.class, ApiConfiguration.class, SearchConfiguration.class };
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[] { WebappConfiguration.class };
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{ "/" };
    }
   
    @Override
    protected Filter[] getServletFilters() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        return new Filter[] { characterEncodingFilter};
    }
    
}