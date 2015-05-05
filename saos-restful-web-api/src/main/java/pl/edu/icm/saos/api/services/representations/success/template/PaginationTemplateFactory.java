package pl.edu.icm.saos.api.services.representations.success.template;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;

import pl.edu.icm.saos.api.search.parameters.Pagination;

/**
 * Service responsible for creating pagination related templates
 * 
 * @author madryk
 */
@Service
public class PaginationTemplateFactory {

    @Value("${restful.api.max.page.size}")
    private int maxPageSize=100;
    
    @Value("${restful.api.min.page.size}")
    private int minPageSize=10;
    
    
    //------------------------ LOGIC --------------------------
    
    public PageNumberTemplate createPageNumberTemplate(Pagination pagination) {
        Preconditions.checkNotNull(pagination);
        
        return new PageNumberTemplate(pagination.getPageNumber());
    }
    
    public PageSizeTemplate createPageSizeTemplate(Pagination pagination) {
        Preconditions.checkNotNull(pagination);
        
        return new PageSizeTemplate(pagination.getPageSize(), minPageSize, maxPageSize);
    }

    
    //------------------------ SETTERS --------------------------
    
    public void setMaxPageSize(int maxPageSize) {
        this.maxPageSize = maxPageSize;
    }

    public void setMinPageSize(int minPageSize) {
        this.minPageSize = minPageSize;
    }
}
