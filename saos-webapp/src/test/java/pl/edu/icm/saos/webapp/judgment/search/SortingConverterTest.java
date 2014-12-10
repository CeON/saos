package pl.edu.icm.saos.webapp.judgment.search;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

import pl.edu.icm.saos.search.config.model.JudgmentIndexField;
import pl.edu.icm.saos.search.search.model.Sorting;
import pl.edu.icm.saos.webapp.judgment.search.SortingConverter;

/**
 * @author Łukasz Pawełczak
 *
 */
@RunWith(DataProviderRunner.class)
public class SortingConverterTest {
	
	private SortingConverter sortingConverter = new SortingConverter();
	
	private static final String judgmentDate = "JUDGMENT_DATE";
	private static final String caseNumber = "CASE_NUMBER";
	

	@DataProvider
	public static Object[][] convertData() {
		return new Object[][] {
				{new Sorting(judgmentDate, Sorting.Direction.ASC), new Order(Direction.ASC, judgmentDate)},
				{new Sorting(judgmentDate, Sorting.Direction.DESC), new Order(Direction.DESC, judgmentDate)},
				{new Sorting(caseNumber, Sorting.Direction.ASC), new Order(Direction.ASC, caseNumber)},
				{new Sorting(caseNumber, Sorting.Direction.DESC), new Order(Direction.DESC, caseNumber)},
		};
		
	}
	
	
	//------------------------ TESTS --------------------------
	
	@Test
	@UseDataProvider("convertData")
	public void convert(Sorting expectedSorting, Order givenOrder) {
		
		assertEquals(expectedSorting.getDirection(), sortingConverter.convert(new Sort(givenOrder)).getDirection());
		assertEquals(JudgmentIndexField.valueOf(expectedSorting.getFieldName()).getFieldName(), sortingConverter.convert(new Sort(givenOrder)).getFieldName());
	}
	
	@Test
	public void convert_relevance() {
		//given
		Order order = new Order(Direction.DESC, "RELEVANCE");
		Sort sort = new Sort(order);
		
		//when
		Sorting actual = sortingConverter.convert(sort);
		
		
		//then
		assertEquals(Sorting.relevanceSorting().getFieldName(), actual.getFieldName());
	}


}
