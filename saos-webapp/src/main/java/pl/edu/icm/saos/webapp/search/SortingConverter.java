package pl.edu.icm.saos.webapp.search;

import java.util.Iterator;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.search.config.model.JudgmentIndexField;
import pl.edu.icm.saos.search.search.model.Sorting;

/**
 * @author Łukasz Pawełczak
 *
 */
@Service
public class SortingConverter {

	private final String RELEVANCE = "RELEVANCE";
	
	private final Sorting.Direction defaultDirection = Sorting.Direction.ASC;
	
	public Sorting convert(Sort sort) {
		Iterator<Order> order = sort.iterator();
		
		//get one sorting field, if empty set sorting to by relevance
		if (order.hasNext()) {
			Order next = order.next(); 
			String property = next.getProperty();
			String fieldName = "";
			
			try {
				fieldName = JudgmentIndexField.valueOf(property).getFieldName();
			} catch (IllegalArgumentException e) {
				return Sorting.relevanceSorting();
			}
			
			if (!property.equals(RELEVANCE)) {
				return new Sorting(fieldName, convertDirection(next.getDirection()));
			}
		} 
		
		return Sorting.relevanceSorting();
	}

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
