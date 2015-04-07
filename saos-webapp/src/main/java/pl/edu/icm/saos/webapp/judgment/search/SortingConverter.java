package pl.edu.icm.saos.webapp.judgment.search;

import java.util.Iterator;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.search.config.model.JudgmentIndexField;
import pl.edu.icm.saos.search.search.model.Sorting;

/**
 * Provides functionality of converting {@link org.springframework.data.domain.Sort} into
 * {@link pl.edu.icm.saos.search.search.model.Sorting}
 * @author Łukasz Pawełczak
 *
 */
@Service
public class SortingConverter {

	
	private final Sorting.Direction defaultDirection = Sorting.Direction.DESC;
	
	
	//------------------------ LOGIC --------------------------
	
	/**
	 * Method converts object {@link org.springframework.data.domain.Sort} into {@link pl.edu.icm.saos.search.search.model.Sorting}.
	 * If Sort object contains incorrect value of sort field, method returns sorting by relevance. 
	 * 
	 * @param sort {@link org.springframework.data.domain.Sort} 
	 * @return {@link pl.edu.icm.saos.search.search.model.Sorting}
	 */
	public Sorting convert(Sort sort) {
		Iterator<Order> order = sort.iterator();
		
		// Get one sorting property, if it is not valid use sorting by relevance.
		if (order.hasNext()) {
			Order next = order.next(); 
			String property = next.getProperty();
			
			if (JudgmentIndexField.hasFieldName(property)) {
				return new Sorting(JudgmentIndexField.valueOf(property).getFieldName(), convertDirection(next.getDirection()));
			}
			
            return Sorting.relevanceSorting(convertDirection(next.getDirection()));
		} 
		
		return Sorting.relevanceSorting();
	}

	
	//------------------------ PRIVATE --------------------------
	
	private Sorting.Direction convertDirection(Sort.Direction direction) {	
		if (direction != null) {
			if (direction == Sort.Direction.DESC) {
				return Sorting.Direction.DESC;
			} else {
				return Sorting.Direction.ASC;
			}
		} else {
			return defaultDirection;
		}
	}
}
