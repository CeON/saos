package pl.edu.icm.saos.api.services.representations.success.template;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import pl.edu.icm.saos.api.search.parameters.Pagination;

/**
 * @author madryk
 */
public class PaginationTemplateFactoryTest {

    private PaginationTemplateFactory paginationTemplateFactory = new PaginationTemplateFactory();
    
    
    @Before
    public void setUp() {
        paginationTemplateFactory.setDefaultMinPageSize(10);
        paginationTemplateFactory.setDefaultMaxPageSize(100);
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void createPageNumberTemplate() {
        // given
        Pagination pagination = new Pagination(20, 2);
        
        // execute
        PageNumberTemplate pageNumberTemplate = paginationTemplateFactory.createPageNumberTemplate(pagination);
        
        // assert
        assertEquals(new PageNumberTemplate(2), pageNumberTemplate);
    }
    
    @Test(expected = NullPointerException.class)
    public void createPageNumberTemplate_NULL_CHECK() {
        // execute
        paginationTemplateFactory.createPageNumberTemplate(null);
    }
    
    
    @Test
    public void createPageSizeTemplate_DEFAULT_MIN_MAX() {
        // given
        Pagination pagination = new Pagination(20, 2);
        
        // execute
        PageSizeTemplate pageSizeTemplate = paginationTemplateFactory.createPageSizeTemplate(pagination);
        
        // assert
        assertEquals(new PageSizeTemplate(20, 10, 100), pageSizeTemplate);
    }
    
    @Test
    public void createPageSizeTemplate_OVERRIDDEN_MIN_MAX() {
        // given
        Pagination pagination = new Pagination(20, 2);
        
        // execute
        PageSizeTemplate pageSizeTemplate = paginationTemplateFactory.createPageSizeTemplate(pagination, 5, 200);
        
        // assert
        assertEquals(new PageSizeTemplate(20, 5, 200), pageSizeTemplate);
    }
    
    
    @Test(expected = NullPointerException.class)
    public void createPageSizeTemplate_NULL_CHECK() {
        // execute
        paginationTemplateFactory.createPageSizeTemplate(null);
    }
    
    @Test(expected = NullPointerException.class)
    public void createPageSizeTemplate_OVERRIDDEN_MIN_MAX_NULL_CHECK() {
        // execute
        paginationTemplateFactory.createPageSizeTemplate(null, 1, 10);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void createPageSizeTemplate_MIN_GREATER_THAN_MAX() {
        // given
        Pagination pagination = new Pagination(20, 2);
        
        // execute
        paginationTemplateFactory.createPageSizeTemplate(pagination, 100, 50);
    }
}
