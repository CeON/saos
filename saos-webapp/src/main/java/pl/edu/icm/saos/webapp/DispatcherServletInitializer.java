package pl.edu.icm.saos.webapp;

import javax.servlet.Filter;

import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import pl.edu.icm.saos.api.ApiConfiguration;
import pl.edu.icm.saos.batch.core.BatchConfiguration;
import pl.edu.icm.saos.common.CommonConfiguration;
import pl.edu.icm.saos.enrichment.EnrichmentConfiguration;
import pl.edu.icm.saos.importer.ImportConfiguration;
import pl.edu.icm.saos.persistence.PersistenceConfiguration;
import pl.edu.icm.saos.search.SearchConfiguration;
import pl.edu.icm.saos.webapp.analysis.UiAnalysisConfiguration;


   
public class DispatcherServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
   
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[] {  GeneralConfiguration.class, CommonConfiguration.class, EnrichmentConfiguration.class, SecurityConfiguration.class, BatchConfiguration.class, PersistenceConfiguration.class, ImportConfiguration.class,  SearchConfiguration.class, ApiConfiguration.class, UiAnalysisConfiguration.class};
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

