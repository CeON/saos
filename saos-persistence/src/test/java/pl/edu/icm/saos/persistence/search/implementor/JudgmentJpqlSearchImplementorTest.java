package pl.edu.icm.saos.persistence.search.implementor;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.PersistenceTestSupport;
import pl.edu.icm.saos.persistence.common.FieldsNames;
import pl.edu.icm.saos.persistence.common.TestPersistenceObjectFactory;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.ConstitutionalTribunalJudgment;
import pl.edu.icm.saos.persistence.model.ConstitutionalTribunalJudgmentDissentingOpinion;
import pl.edu.icm.saos.persistence.model.CourtType;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;
import pl.edu.icm.saos.persistence.search.DatabaseSearchService;
import pl.edu.icm.saos.persistence.search.dto.JudgmentSearchFilter;
import pl.edu.icm.saos.persistence.search.result.SearchResult;

/**
 * Tests integration  between
 * {@link pl.edu.icm.saos.persistence.search.implementor.JudgmentJpqlSearchImplementor JudgmentJpqlSearchImplementor}
 * and {@link pl.edu.icm.saos.persistence.search.DatabaseSearchService DatabaseSearchService}
 */
@Category(SlowTest.class)
public class JudgmentJpqlSearchImplementorTest extends PersistenceTestSupport {
    



    @Autowired
    private DatabaseSearchService databaseSearchService;

    @Autowired
    private JudgmentRepository judgmentRepository;

    @Autowired
    private TestPersistenceObjectFactory testPersistenceObjectFactory;


    @Test
    public void search__it_should_not_throw_lazy_exception_for_empty_dissenting_opinions_list(){
        //given
        ConstitutionalTribunalJudgment ctJudgment = testPersistenceObjectFactory.createSimpleCtJudgment();

        //when
        JudgmentSearchFilter searchFilter = JudgmentSearchFilter.builder()
                .filter();

        SearchResult<Judgment> searchResult = databaseSearchService.search(searchFilter);

        //then
        Judgment judgment = searchResult.getResultRecords().get(0);

        assertThat(judgment, is(instanceOf(ConstitutionalTribunalJudgment.class)));
        ConstitutionalTribunalJudgment constitutionalTribunalJudgment = (ConstitutionalTribunalJudgment) judgment;
        constitutionalTribunalJudgment.getDissentingOpinions();

    }

    @Test
    public void search__it_should_not_throw_lazy_exception_for_empty_scChambers_list(){
        //given
        SupremeCourtJudgment scJudgment = testPersistenceObjectFactory.createSimpleScJudgment();

        //when
        JudgmentSearchFilter searchFilter = JudgmentSearchFilter.builder()
                .filter();

        SearchResult<Judgment> searchResult = databaseSearchService.search(searchFilter);

        //then
        Judgment judgment = searchResult.getResultRecords().get(0);

        assertThat(judgment, is(instanceOf(SupremeCourtJudgment.class)));
        SupremeCourtJudgment supremeCourtJudgment = (SupremeCourtJudgment) judgment;
        supremeCourtJudgment.getScChambers();
    }

    @Test
    public void search_it_should_not_throw_lazy_exception_for_empty_court_cases(){
        CommonCourtJudgment firstJudgment = testPersistenceObjectFactory.createSimpleCcJudgment();

        //when
        JudgmentSearchFilter searchFilter = JudgmentSearchFilter.builder()
                .filter();

        SearchResult<Judgment> searchResult = databaseSearchService.search(searchFilter);

        //then
        Judgment judgment = searchResult.getResultRecords().get(0);
        judgment.getCourtCases();
    }


    @Test
    public void search_it_should_not_throw_lazy_exception_for_empty_judges_list(){
        CommonCourtJudgment firstJudgment = testPersistenceObjectFactory.createSimpleCcJudgment();

        //when
        JudgmentSearchFilter searchFilter = JudgmentSearchFilter.builder()
                .filter();

        SearchResult<Judgment> searchResult = databaseSearchService.search(searchFilter);

        //then
        Judgment judgment = searchResult.getResultRecords().get(0);
        judgment.getJudges();
    }

    @Test
    public void search_it_should_not_throw_lazy_exception_for_empty_courtReporters_list(){
        CommonCourtJudgment firstJudgment = testPersistenceObjectFactory.createSimpleCcJudgment();

        //when
        JudgmentSearchFilter searchFilter = JudgmentSearchFilter.builder()
                .filter();

        SearchResult<Judgment> searchResult = databaseSearchService.search(searchFilter);

        //then
        Judgment judgment = searchResult.getResultRecords().get(0);
        judgment.getCourtReporters();
    }

    @Test
    public void search_it_should_not_throw_lazy_exception_for_empty_legalBases_list(){
        //given
        CommonCourtJudgment firstJudgment = testPersistenceObjectFactory.createSimpleCcJudgment();

        //when
        JudgmentSearchFilter searchFilter = JudgmentSearchFilter.builder()
                .filter();

        SearchResult<Judgment> searchResult = databaseSearchService.search(searchFilter);

        //then
        Judgment judgment = searchResult.getResultRecords().get(0);
        judgment.getLegalBases();
    }

    
    @Test
    public void search_it_should_not_throw_lazy_exception_for_empty_referencedRegulations_list(){
        CommonCourtJudgment firstJudgment = testPersistenceObjectFactory.createSimpleCcJudgment();

        //when
        JudgmentSearchFilter searchFilter = JudgmentSearchFilter.builder()
                .filter();

        SearchResult<Judgment> searchResult = databaseSearchService.search(searchFilter);

        //then
        Judgment judgment = searchResult.getResultRecords().get(0);
        judgment.getReferencedRegulations();
    }

    @Test
    public void search_it_should_not_throw_lazy_exception_for_empty_keywords_list(){
        CommonCourtJudgment firstJudgment = testPersistenceObjectFactory.createSimpleCcJudgment();

        //when
        JudgmentSearchFilter searchFilter = JudgmentSearchFilter.builder()
                .filter();

        SearchResult<Judgment> searchResult = databaseSearchService.search(searchFilter);

        //then
        Judgment judgment = searchResult.getResultRecords().get(0);

        assertEquals(CourtType.COMMON, judgment.getCourtType());
        CommonCourtJudgment ccJudgment = (CommonCourtJudgment) judgment;
        ccJudgment.getKeywords();
    }

    @Test
    public void search__it_should_find_judgments_with_all_basic_fields(){
        //given
        CommonCourtJudgment ccJudgment = testPersistenceObjectFactory.createCcJudgment();
        JudgmentSearchFilter searchFilter = JudgmentSearchFilter.builder()
                .filter();

        //when
        SearchResult<Judgment> searchResult = databaseSearchService.search(searchFilter);

        //then
        List<Judgment> judgments = searchResult.getResultRecords();
        assertThat(judgments, iterableWithSize(1));

        Judgment actualJudgment = judgments.get(0);

        assertThat("judgment source ", actualJudgment.getSourceInfo(), is(ccJudgment.getSourceInfo()));
        assertThat("actual judgment court cases should not be null", actualJudgment.getCourtCases(), not(nullValue()));
        assertThat("case numbers",actualJudgment.getCourtCases(), containsListInAnyOrder(ccJudgment.getCourtCases()));
        assertThat("creation date", actualJudgment.getCreationDate(), is(ccJudgment.getCreationDate()));
        assertThat("judges", actualJudgment.getJudges(), containsListInAnyOrder(ccJudgment.getJudges()));
        assertThat("all roles ", extractRolesNames(actualJudgment.getJudges()), containsInAnyOrder(Judge.JudgeRole.PRESIDING_JUDGE.name()));
        assertThat("judge's role", actualJudgment.getJudges().get(0).getSpecialRoles().get(0), is(Judge.JudgeRole.PRESIDING_JUDGE));

        assertThat("court reporters", actualJudgment.getCourtReporters(), containsListInAnyOrder(ccJudgment.getCourtReporters()));
        assertThat("decision" , actualJudgment.getDecision(), is(ccJudgment.getDecision()));

        assertThat("judgment date", actualJudgment.getJudgmentDate(), is(ccJudgment.getJudgmentDate()));
        assertThat("judgment type", actualJudgment.getJudgmentType(), is(ccJudgment.getJudgmentType()));

        assertThat("legal bases", actualJudgment.getLegalBases(), containsListInAnyOrder(ccJudgment.getLegalBases()));
        assertThat("referenced regulation", actualJudgment.getReferencedRegulations(), containsListInAnyOrder(ccJudgment.getReferencedRegulations()));

        assertThat("last modification date should not be null", actualJudgment.getModificationDate(), notNullValue());
        assertThat("last modification date ", actualJudgment.getModificationDate(), is(ccJudgment.getModificationDate()));
    }

    @Test
    public void search__it_should_find_common_court_judgments_with_all_basic_fields(){
        //given
        CommonCourtJudgment ccJudgment = testPersistenceObjectFactory.createCcJudgment();
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

        assertThat("keywords", actualJudgment.getKeywords(), containsListInAnyOrder(ccJudgment.getKeywords()));
        assertThat("division id should be not null", actualJudgment.getCourtDivision().getId(), notNullValue());
        assertThat("division id ", actualJudgment.getCourtDivision().getId(), is(ccJudgment.getCourtDivision().getId()));
    }

    @Test
    public void search__it_should_find_supreme_court_judgment_with_all_basic_fields(){
        //given
        SupremeCourtJudgment scJudgment = testPersistenceObjectFactory.createScJudgment();
        JudgmentSearchFilter searchFilter = JudgmentSearchFilter.builder()
                .filter();

        //when
        SearchResult<Judgment> searchResult = databaseSearchService.search(searchFilter);

        //then
        List<Judgment> judgments = searchResult.getResultRecords();
        assertThat(judgments, iterableWithSize(1));

        Judgment judgment = judgments.get(0);
        assertThat(judgment, is(instanceOf(SupremeCourtJudgment.class)));

        SupremeCourtJudgment actualJudgment = (SupremeCourtJudgment) judgment;

        assertThat("personnel type ", actualJudgment.getPersonnelType(), is(scJudgment.getPersonnelType()));
        assertThat("scChambers ", actualJudgment.getScChambers(), containsListInAnyOrder(scJudgment.getScChambers()));
        assertThat("scChamber's name should be not null", actualJudgment.getScChambers().get(0).getId(), notNullValue());
        assertThat("division id should be not null", actualJudgment.getScChamberDivision().getId(), notNullValue());
        assertThat("division id", actualJudgment.getScChamberDivision().getId(), is(scJudgment.getScChamberDivision().getId()));
        assertThat("form", actualJudgment.getScJudgmentForm(), is(scJudgment.getScJudgmentForm()));
    }

    @Test
    public void search__it_should_find_constitutional_tribunal_judgment_with_all_basic_fields(){
        //given
        ConstitutionalTribunalJudgment ctJudgment = testPersistenceObjectFactory.createCtJudgment();
        JudgmentSearchFilter searchFilter = JudgmentSearchFilter.builder()
                .filter();

        //when
        SearchResult<Judgment> searchResult = databaseSearchService.search(searchFilter);

        //then
        List<Judgment> judgments = searchResult.getResultRecords();
        assertThat(judgments, iterableWithSize(1));

        Judgment judgment = judgments.get(0);
        assertThat(judgment, is(instanceOf(ConstitutionalTribunalJudgment.class)));

        ConstitutionalTribunalJudgment actualJudgment = (ConstitutionalTribunalJudgment) judgment;
        assertThat("keywords", actualJudgment.getKeywords(), containsListInAnyOrder(ctJudgment.getKeywords()));
        assertThat("opinion authors should be not null", actualJudgment.getDissentingOpinions().get(0).getAuthors().get(0), notNullValue());
        assertOpinions(actualJudgment.getDissentingOpinions(), ctJudgment.getDissentingOpinions());
    }

    @Test
    public void search__it_should_find_subList_of_judgments_ordered_by_judgmentDate(){
        //given
        List<CommonCourtJudgment> judgments = testPersistenceObjectFactory.createCcJudgmentListWithRandomData(4);
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
        List<CommonCourtJudgment> judgments = testPersistenceObjectFactory.createCcJudgmentListWithRandomData(4);
        judgments.get(0).setJudgmentDate(judgments.get(3).getJudgmentDate().minusMonths(1));
        judgments.get(1).setJudgmentDate(judgments.get(3).getJudgmentDate().minusMonths(1));
        judgments.get(2).setJudgmentDate(judgments.get(3).getJudgmentDate().minusMonths(1));
        judgmentRepository.save(judgments);
        
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
        List<CommonCourtJudgment> judgments = testPersistenceObjectFactory.createCcJudgmentListWithRandomData(10);
        sortByCaseNumberDown(judgments);

        List<Long> sortedJudgmentsIds = sort(extractIds(judgments));


        JudgmentSearchFilter searchFilter = JudgmentSearchFilter.builder()
                .filter();

        //when
        SearchResult<Judgment> searchResult = databaseSearchService.search(searchFilter);

        //then
        List<Judgment> actualJudgments = searchResult.getResultRecords();

        List<Long> judgmentsIds = actualJudgments.stream()
                .map(Judgment::getId)
                .collect(Collectors.toList());

        assertThat(judgmentsIds, is(sortedJudgmentsIds));
    }

    @Test
    public void search__it_should_find_judgments_that_are_modified_after_first_search(){
        //given
        List<CommonCourtJudgment> judgments = testPersistenceObjectFactory.createCcJudgmentListWithRandomData(10);

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

    @Test
    public void search_it_should_return_empty_list_if_there_is_no_judgments(){
        JudgmentSearchFilter simpleSearchFilter = JudgmentSearchFilter.builder()
                .filter();

        //when
        SearchResult<Judgment> searchResult = databaseSearchService.search(simpleSearchFilter);

        //then
        List<Judgment> judgments = searchResult.getResultRecords();

        assertThat("should be empty", judgments, iterableWithSize(0));

    }

    
    //------------------------ PRIVATE --------------------------
    private static void assertOpinions(List<ConstitutionalTribunalJudgmentDissentingOpinion> actual, List<ConstitutionalTribunalJudgmentDissentingOpinion> expected){
        assertThat("list size should match ", expected.size() == actual.size());

        Map<Long, ConstitutionalTribunalJudgmentDissentingOpinion> actualOpinions = actual.stream().collect(Collectors.toMap(o -> o.getId(),o -> o));

        for(ConstitutionalTribunalJudgmentDissentingOpinion expectedOpinion: expected){
            ConstitutionalTribunalJudgmentDissentingOpinion actualOpinion = actualOpinions.get(expectedOpinion.getId());
            assertThat("opinion's text ", actualOpinion.getTextContent(), is(expectedOpinion.getTextContent()));
            assertThat("opinion's authors ", actualOpinion.getAuthors(), is(expectedOpinion.getAuthors()));
        }
    }
    
    private static List<String> extractRolesNames(List<Judge> judges){
        List<String> rolesNames = judges.stream()
                .flatMap(judge -> judge.getSpecialRoles().stream())
                .map(role -> role.name())
                .collect(Collectors.toList());

        return rolesNames;
    }

    private static List<Long> extractIds(List<? extends Judgment> judgments){
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