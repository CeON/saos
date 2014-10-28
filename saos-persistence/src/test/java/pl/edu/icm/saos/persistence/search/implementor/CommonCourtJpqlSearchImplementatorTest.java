package pl.edu.icm.saos.persistence.search.implementor;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.PersistenceTestSupport;
import pl.edu.icm.saos.persistence.common.TestJudgmentFactory;
import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.CommonCourtDivision;
import pl.edu.icm.saos.persistence.search.DatabaseSearchService;
import pl.edu.icm.saos.persistence.search.dto.CommonCourtSearchFilter;
import pl.edu.icm.saos.persistence.search.result.SearchResult;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Tests integration between
 * {@link pl.edu.icm.saos.persistence.search.implementor.CommonCourtJpqlSearchImplementator CommonCourtJpqlSearchImplementator}
 * and {@link pl.edu.icm.saos.persistence.search.DatabaseSearchService DatabaseSearchService}
 */
@Category(SlowTest.class)
public class CommonCourtJpqlSearchImplementatorTest extends PersistenceTestSupport {

    @Autowired
    private TestJudgmentFactory testJudgmentFactory;

    @Autowired
    private DatabaseSearchService databaseSearchService;



    @Test
    public void search__it_should_return_empty_list_if_there_is_no_common_courts(){
        CommonCourtSearchFilter searchFilter = CommonCourtSearchFilter.builder()
                .filter();

        //when
        SearchResult<CommonCourt> searchResult = databaseSearchService.search(searchFilter);

        //then
        List<CommonCourt> courts = searchResult.getResultRecords();

        assertThat("should be empty", courts, iterableWithSize(0));
    }

    @Test
    public void search__it_should_find_all_CommonCourt_Basic_Fields_With_Its_All_Divisions_Fields(){
        //given
        CommonCourt commonCourt = testJudgmentFactory.createFullCommonCourt(true);

        CommonCourtSearchFilter searchFilter = CommonCourtSearchFilter.builder()
                .filter();

        //when
        SearchResult<CommonCourt> searchResult = databaseSearchService.search(searchFilter);

        //then
        List<CommonCourt> courts = searchResult.getResultRecords();

        assertThat(courts, iterableWithSize(1));
        CommonCourt actualCourt = courts.get(0);

        assertThat("court code", actualCourt.getCode(), is(commonCourt.getCode()));
        assertThat("court name", actualCourt.getName(), is(commonCourt.getName()));
        assertThat("court type", actualCourt.getType(), is(commonCourt.getType()));

        List<CommonCourtDivision> divisions = actualCourt.getDivisions();

        assertThat(divisions, iterableWithSize(2));
        assertThat(" divisions ", actualCourt.getDivisions(), containsListInAnyOrder(commonCourt.getDivisions()));

    }

    @Test
    public void search__it_should_find_subList_of_courts_ordered_by_id(){
        //given
        List<CommonCourt> commonCourts = testJudgmentFactory.createSimpleCommonCourts(true);

        commonCourts.sort((first, second) -> Integer.compare(first.getId(), second.getId()));

        int offset = 1;
        int limit = 2;
        CommonCourtSearchFilter searchFilter = CommonCourtSearchFilter.builder()
                .limit(limit)
                .offset(offset)
                .filter();


        //when
        SearchResult<CommonCourt> searchResult = databaseSearchService.search(searchFilter);

        //then
        List<CommonCourt> courts = searchResult.getResultRecords();

        assertThat("courts code", toCodes(courts), is(toCodes(commonCourts.subList(1, 3))));
    }


    private static  org.hamcrest.Matcher<java.lang.Iterable<?>> containsListInAnyOrder(List<?> items) {
        return containsInAnyOrder(items.toArray());
    }

    private static List<String> toCodes(List<CommonCourt> courts){
        return courts.stream().map(CommonCourt::getCode).collect(Collectors.toList());
    }


}