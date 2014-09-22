package pl.edu.icm.saos.webapp.search;

import java.util.Iterator;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.search.config.model.JudgmentIndexField;
import pl.edu.icm.saos.search.model.Sorting;

/**
 * @author Łukasz Pawełczak
 *
 */
@Service
public class SortingConverter {

	private final String RELEVANCE = "RELEVANCE";
	
	public Sorting convert(Sort sort) {
		Iterator<Order> order = sort.iterator();
		
		//get one sorting field, if empty set sorting to by relevance
		if (order.hasNext()) {
			Order next = order.next(); 
			String property = next.getProperty();

			if (!property.equals(RELEVANCE)) {
				return new Sorting(JudgmentIndexField.valueOf(property).getFieldName(),
									convertDirection(next.getDirection()));
			}
		} 
		
		return Sorting.relevanceSorting();
	}

	private Sorting.Direction convertDirection(Sort.Direction direction) {		
		if (direction.toString().equalsIgnoreCase("desc")) {
			return Sorting.Direction.DESC;
		} else {
			return Sorting.Direction.ASC;
		}
	}
}
