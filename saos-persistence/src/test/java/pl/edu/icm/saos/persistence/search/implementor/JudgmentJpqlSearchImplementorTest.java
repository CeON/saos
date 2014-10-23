package pl.edu.icm.saos.persistence.search.implementor;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.PersistenceTestSupport;
import pl.edu.icm.saos.persistence.common.FieldsNames;
import pl.edu.icm.saos.persistence.common.TestJudgmentFactory;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;
import pl.edu.icm.saos.persistence.search.DatabaseSearchService;
import pl.edu.icm.saos.persistence.search.dto.JudgmentSearchFilter;
import pl.edu.icm.saos.persistence.search.result.SearchResult;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Tests integration  between
 * {@link pl.edu.icm.saos.persistence.search.implementor.JudgmentJpqlSearchImplementor JudgmentJpqlSearchImplementor}
 * and {@link pl.edu.icm.saos.persistence.search.DatabaseSearchService DatabaseSearchService}
 */
@Category(SlowTest.class)
public class JudgmentJpqlSearchImplementorTest extends PersistenceTestSupport {
    

    @Autowired
    private TestJudgmentFactory testJudgmentFactory;

    @Autowired
    private DatabaseSearchService databaseSearchService;

    @Autowired
    private JudgmentRepository judgmentRepository;

    @Test
    public void search__it_should_find_judgments_with_all_basic_fields(){
        //given
        CommonCourtJudgment ccJudgment = testJudgmentFactory.createFullCcJudgment(true);
        JudgmentSearchFilter searchFilter = JudgmentSearchFilter.builder()
                .filter();

        //when
        SearchResult<Judgment> searchResult = databaseSearchService.search(searchFilter);

        //then
        List<Judgment> judgments = searchResult.getResultRecords();
        assertThat(judgments, iterableWithSize(1));

        Judgment judgment = judgments.get(0);
        assertThat(judgment, is(instanceOf(CommonCourtJudgment.class)));

        CommonCourtJudgment actualJudgment = (CommonCourtJudgment) judgment;

        assertThat("judgment source ", actualJudgment.getSourceInfo(), is(ccJudgment.getSourceInfo()));
        assertThat("actual judgment court cases should not be null", actualJudgment.getCourtCases(), not(nullValue()));
        assertThat("case numbers",actualJudgment.getCourtCases(), containsListInAnyOrder(ccJudgment.getCourtCases()));
        assertThat("creation date", actualJudgment.getCreationDate(), is(ccJudgment.getCreationDate()));
        assertThat("division id should be not null", actualJudgment.getCourtDivision().getId(), notNullValue());
        assertThat("division id ", actualJudgment.getCourtDivision().getId(), is(ccJudgment.getCourtDivision().getId()));
        assertThat("judges", actualJudgment.getJudges(), containsListInAnyOrder(ccJudgment.getJudges()));
        assertThat("judge role", actualJudgment.getJudges().get(0).getSpecialRoles().get(0), is(Judge.JudgeRole.PRESIDING_JUDGE));

        assertThat("keywords", actualJudgment.getKeywords(), containsListInAnyOrder(ccJudgment.getKeywords()));


        assertThat("court reporters", actualJudgment.getCourtReporters(), containsListInAnyOrder(ccJudgment.getCourtReporters()));
        assertThat("decision" , actualJudgment.getDecision(), is(ccJudgment.getDecision()));


        assertThat("judgment date", actualJudgment.getJudgmentDate(), is(ccJudgment.getJudgmentDate()));

        assertThat("judgment type", actualJudgment.getJudgmentType(), is(ccJudgment.getJudgmentType()));
        assertThat("legal bases", actualJudgment.getLegalBases(), containsListInAnyOrder(ccJudgment.getLegalBases()));
        assertThat("referenced regulation", actualJudgment.getReferencedRegulations(), containsListInAnyOrder(ccJudgment.getReferencedRegulations()));


        assertThat("last modification date should not null", actualJudgment.getModificationDate(), notNullValue());
        assertThat("last modification date ", actualJudgment.getModificationDate(), is(ccJudgment.getModificationDate()));
    }

    @Test
    public void search__it_should_find_subList_of_judgments_ordered_by_judgmentDate(){
        //given
        List<CommonCourtJudgment> judgments = testJudgmentFactory.createSimpleCcJudgments(true);
        judgments.sort((first, second) -> first.getJudgmentDate().compareTo(second.getJudgmentDate()));



        int offset = 1;
        int limit = 2;
        JudgmentSearchFilter searchFilter = JudgmentSearchFilter.builder()
                .limit(limit)
                .offset(offset)
                .upBy(FieldsNames.JUDGMENT_DATE)
                .filter();

        //when
        SearchResult<Judgment> searchResult = databaseSearchService.search(searchFilter);

        //then
        List<Judgment> actualJudgments = searchResult.getResultRecords();

        assertThat(actualJudgments, iterableWithSize(2));
        assertThat(toCaseNumbers(actualJudgments), is(toCaseNumbers(judgments.subList(1, 3))));
    }

    @Test
    public void search__it_should_find_judgments_between_startJudgmentDate_and_endJudgmentDate(){
        //given
        List<CommonCourtJudgment> judgments = testJudgmentFactory.createSimpleCcJudgments(true);
        CommonCourtJudgment givenJudgment = judgments.get(3);
        LocalDate localDate = givenJudgment.getJudgmentDate();
        LocalDate startJudgmentDate = localDate.minusDays(1);
        LocalDate endJudgmentDate = localDate.plusDays(1);

        JudgmentSearchFilter searchFilter = JudgmentSearchFilter.builder()
                .startDate(startJudgmentDate)
                .endDate(endJudgmentDate)
                .filter();

        //when
        SearchResult<Judgment> searchResult = databaseSearchService.search(searchFilter);

        //then
        List<Judgment> actualJudgments = searchResult.getResultRecords();

        assertThat(actualJudgments, iterableWithSize(1));

        assertThat(actualJudgments.get(0).getCaseNumbers(), containsListInAnyOrder(givenJudgment.getCaseNumbers()));
    }


    @Test
    public void search__it_should_find_judgments_sorted_by_id_up(){
        //given
        List<CommonCourtJudgment> judgments = testJudgmentFactory.createSimpleCcJudgments(true);
        sortByCaseNumberDown(judgments);

        List<Integer> sortedJudgmentsIds = sort(extractIds(judgments));


        JudgmentSearchFilter searchFilter = JudgmentSearchFilter.builder()
                .filter();

        //when
        SearchResult<Judgment> searchResult = databaseSearchService.search(searchFilter);

        //then
        List<Judgment> actualJudgments = searchResult.getResultRecords();

        List<Integer> judgmentsIds = actualJudgments.stream()
                .map(Judgment::getId)
                .collect(Collectors.toList());

        assertThat(judgmentsIds, is(sortedJudgmentsIds));
    }

    @Test
    public void search__it_should_find_judgments_that_are_modified_after_first_search(){
        //given
        List<CommonCourtJudgment> judgments = testJudgmentFactory.createSimpleCcJudgments(true);

        JudgmentSearchFilter simpleSearchFilter = JudgmentSearchFilter.builder()
                .filter();



        //when & then
        SearchResult<Judgment> firstSearchResult = databaseSearchService.search(simpleSearchFilter);

        assertThat("should find all elements", firstSearchResult.getResultRecords().size(), is(judgments.size()));

        JudgmentSearchFilter filterWithModificationDate = JudgmentSearchFilter.builder()
                .sinceModificationDateTime(new DateTime())
                .filter();

        Judgment judgment = judgments.get(0);
        judgment.setDecision("some updated decision");
        judgmentRepository.save(judgment);

        SearchResult<Judgment> secondSearchResult = databaseSearchService.search(filterWithModificationDate);

        assertThat("should find only one recently modified element", secondSearchResult.getResultRecords().size(), is(1));
        assertThat("should id match", secondSearchResult.getResultRecords().get(0).getId(), is(judgment.getId()));

    }

    private static List<Integer> extractIds(List<? extends Judgment> judgments){
        return judgments.stream()
                .map(Judgment::getId)
                .collect(Collectors.toList());
    }

    private static <T extends Comparable<? super T>> List<T> sort(List<T> list){
        Collections.sort(list);
        return list;
    }

    private static void sortByCaseNumberDown(List<? extends Judgment> judgments){
        judgments.sort( (first, second) ->
                -first.getCourtCases().get(0).getCaseNumber().compareTo(second.getCourtCases().get(0).getCaseNumber())
        );
    }


    private static <T extends Judgment> List<String> toCaseNumbers(List<T> judgments){
        return judgments.stream().flatMap(j -> j.getCaseNumbers().stream()).collect(Collectors.toList());

    }

    private static  org.hamcrest.Matcher<java.lang.Iterable<?>> containsListInAnyOrder(List<?> items) {
        return containsInAnyOrder(items.toArray());
    }

}