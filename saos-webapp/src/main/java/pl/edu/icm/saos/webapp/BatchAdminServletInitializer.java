package pl.edu.icm.saos.webapp;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import pl.edu.icm.saos.batch.admin.BatchAdminConfiguration;



/**
 * @author Łukasz Dumiszewski
 */

public class BatchAdminServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
   
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[] {  };
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[] {BatchAdminConfiguration.class };
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{ "/batch/*" };
    }
    
    @Override
    protected String getServletName() {
        return "batchAdminDispatcher";
    }
   
    
    
}

