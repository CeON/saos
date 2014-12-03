package pl.edu.icm.saos.persistence.search.implementor;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.PersistenceTestSupport;
import pl.edu.icm.saos.persistence.common.TestPersistenceObjectFactory;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamber;
import pl.edu.icm.saos.persistence.repository.ScChamberRepository;
import pl.edu.icm.saos.persistence.search.DatabaseSearchService;
import pl.edu.icm.saos.persistence.search.dto.SupremeCourtChamberSearchFilter;
import pl.edu.icm.saos.persistence.search.result.SearchResult;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Tests integration between
 * {@link pl.edu.icm.saos.persistence.search.implementor.SupremeCourtChamberJpqlSearchImplementator SupremeCourtChamberJpqlSearchImplementator}
 * and {@link pl.edu.icm.saos.persistence.search.DatabaseSearchService DatabaseSearchService}
 */
@Category(SlowTest.class)
public class SupremeCourtChamberJpqlSearchImplementatorTest extends PersistenceTestSupport {


    @Autowired
    private TestPersistenceObjectFactory testPersistenceObjectFactory;

    @Autowired
    private DatabaseSearchService databaseSearchService;

    @Autowired
    private ScChamberRepository scChamberRepository;



    @Test
    public void search__it_should_not_throw_lazy_exception_for_empty_division_list(){
        //given
        SupremeCourtChamber scChamber = new SupremeCourtChamber();
        scChamber.setName("someName");

        scChamberRepository.save(scChamber);

        SupremeCourtChamberSearchFilter searchFilter = SupremeCourtChamberSearchFilter.builder()
                .filter();


        //when
        SearchResult<SupremeCourtChamber> searchResult = databaseSearchService.search(searchFilter);


        //then
        SupremeCourtChamber court = searchResult.getResultRecords().get(0);
        court.getDivisions();
    }

    @Test
    public void search__it_should_find_all_scChambers_Basic_Fields_With_Its_All_Divisions_Fields(){
        //given
        SupremeCourtChamber scChamber = testPersistenceObjectFactory.createScChamber();

        SupremeCourtChamberSearchFilter searchFilter = SupremeCourtChamberSearchFilter.builder()
                .filter();

        //when
        SearchResult<SupremeCourtChamber> searchResult = databaseSearchService.search(searchFilter);

        //then
        List<SupremeCourtChamber> chambers = searchResult.getResultRecords();

        assertThat(chambers, iterableWithSize(1));
        SupremeCourtChamber actualChamber = chambers.get(0);

        assertThat("chamber name ", actualChamber.getName(), is(scChamber.getName()));

        assertThat(actualChamber.getDivisions(), iterableWithSize(scChamber.getDivisions().size()));
        assertThat("divisions ", actualChamber.getDivisions(), containsListInAnyOrder(scChamber.getDivisions()));

    }


    private static  org.hamcrest.Matcher<java.lang.Iterable<?>> containsListInAnyOrder(List<?> items) {
        return containsInAnyOrder(items.toArray());
    }
}