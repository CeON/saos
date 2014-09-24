package pl.edu.icm.saos.webapp.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.search.search.model.Paging;
import pl.edu.icm.saos.search.search.model.Sorting;

/**
 * @author Łukasz Pawełczak
 *
 */
@Service
public class PagingConverter {

	@Autowired
	private SortingConverter sortingConverter;
	
	public Paging convert(Pageable pageable) {
		
		Sorting sorting = sortingConverter.convert(pageable.getSort());
		
		Paging paging = new Paging(pageable.getPageNumber(), pageable.getPageSize(), sorting);
		
		return paging;
	}
	
	
}
