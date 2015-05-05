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
        paginationTemplateFactory.setMinPageSize(10);
        paginationTemplateFactory.setMaxPageSize(100);
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
    public void createPageSizeTemplate() {
        // given
        Pagination pagination = new Pagination(20, 2);
        
        // execute
        PageSizeTemplate pageSizeTemplate = paginationTemplateFactory.createPageSizeTemplate(pagination);
        
        // assert
        assertEquals(new PageSizeTemplate(20, 10, 100), pageSizeTemplate);
    }
    
    @Test(expected = NullPointerException.class)
    public void createPageSizeTemplate_NULL_CHECK() {
        // execute
        paginationTemplateFactory.createPageSizeTemplate(null);
    }
    
}
