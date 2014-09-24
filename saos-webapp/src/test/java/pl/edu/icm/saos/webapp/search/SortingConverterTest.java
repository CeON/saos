package pl.edu.icm.saos.webapp.search;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import pl.edu.icm.saos.search.config.model.JudgmentIndexField;
import pl.edu.icm.saos.search.search.model.Sorting;

/**
 * @author Łukasz Pawełczak
 *
 */
public class SortingConverterTest {
	
	private SortingConverter sortingConverter = new SortingConverter();
	
	private final String fieldName = "JUDGMENT_DATE"; 
	
	@Before
	public void setUp() {
		
	}

	@Test
	public void convert() {
		Sorting sorting = new Sorting(fieldName, Sorting.Direction.ASC);
		Order orderAsc = new Order(Direction.ASC, fieldName);
		Order orderDesc = new Order(Direction.DESC, fieldName);
		Sort sortAsc = new Sort(orderAsc);
		Sort sortDesc = new Sort(orderDesc);

		assertEquals(JudgmentIndexField.valueOf(fieldName).getFieldName(), sortingConverter.convert(sortAsc).getFieldName());
		assertEquals(Sorting.Direction.ASC, sortingConverter.convert(sortAsc).getDirection());
		assertEquals(Sorting.Direction.DESC, sortingConverter.convert(sortDesc).getDirection());
		assertEquals(sorting.getDirection(), sortingConverter.convert(sortAsc).getDirection());
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

		assertEquals(Sorting.Direction.ASC, sortingConverter.convert(sort).getDirection());
	}

}
