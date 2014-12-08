package pl.edu.icm.saos.webapp.judgment.search;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import pl.edu.icm.saos.search.config.model.JudgmentIndexField;
import pl.edu.icm.saos.search.search.model.Sorting;
import pl.edu.icm.saos.webapp.judgment.search.SortingConverter;

/**
 * @author Łukasz Pawełczak
 *
 */
public class SortingConverterTest {
	
	private SortingConverter sortingConverter = new SortingConverter();
	
	private final String fieldName = "JUDGMENT_DATE"; 
	

	//------------------------ TESTS --------------------------
	
	@Test
	public void convert() {
		//given
		Order orderAsc = new Order(Direction.ASC, fieldName);
		Order orderDesc = new Order(Direction.DESC, fieldName);
		Sort sortAsc = new Sort(orderAsc);
		Sort sortDesc = new Sort(orderDesc);

		//when
		Sorting actualASC = sortingConverter.convert(sortAsc);
		Sorting actualDESC = sortingConverter.convert(sortDesc);
		
		//then
		assertEquals(JudgmentIndexField.valueOf(fieldName).getFieldName(), actualASC.getFieldName());
		assertEquals(Sorting.Direction.ASC, actualASC.getDirection());
		assertEquals(Sorting.Direction.DESC, actualDESC.getDirection());
	}
	
	@Test
	public void convert_relevance() {
		Order order = new Order(Direction.DESC, "RELEVANCE");
		Sort sort = new Sort(order);
		
		assertEquals(Sorting.relevanceSorting().getFieldName(), sortingConverter.convert(sort).getFieldName());
	}
	
	@Test
	public void convert_noDirection() {
		Order order = new Order(null, fieldName);
		Sort sort = new Sort(order);

		assertEquals(Sorting.Direction.DESC, sortingConverter.convert(sort).getDirection());
	}

}
