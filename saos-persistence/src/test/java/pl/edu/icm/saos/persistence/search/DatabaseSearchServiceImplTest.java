package pl.edu.icm.saos.persistence.search;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import java.util.List;
import java.util.stream.Collectors;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.PersistenceTestSupport;
import pl.edu.icm.saos.persistence.common.FieldsNames;
import pl.edu.icm.saos.persistence.common.TestJudgmentFactory;
import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.CommonCourtDivision;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.search.dto.CommonCourtSearchFilter;
import pl.edu.icm.saos.persistence.search.dto.JudgmentSearchFilter;
import pl.edu.icm.saos.persistence.search.result.SearchResult;


@Category(SlowTest.class)
public class DatabaseSearchServiceImplTest extends PersistenceTestSupport{

    private static final Logger logger = LoggerFactory.getLogger(DatabaseSearchServiceImplTest.class);

    @Autowired
    private TestJudgmentFactory testJudgmentFactory;

    @Autowired
    private DatabaseSearchService databaseSearchService;

    @Test
    public void it_should_find_judgments_with_all_basic_fields(){
        //given
        CommonCourtJudgment ccJudgment = testJudgmentFactory.createFullCcJudgment(true);
        JudgmentSearchFilter searchFilter = JudgmentSearchFilter.builder()
                .initialize()
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
        assertThat("keywords", actualJudgment.getKeywords(), containsListInAnyOrder(ccJudgment.getKeywords()));
        assertThat("court reporters", actualJudgment.getCourtReporters(), containsListInAnyOrder(ccJudgment.getCourtReporters()));
        assertThat("decision" , actualJudgment.getDecision(), is(ccJudgment.getDecision()));
        assertThat("judges", actualJudgment.getJudges(), containsListInAnyOrder(ccJudgment.getJudges()));
        assertThat("judgment date", actualJudgment.getJudgmentDate(), is(ccJudgment.getJudgmentDate()));
        assertThat("judgment type", actualJudgment.getJudgmentType(), is(ccJudgment.getJudgmentType()));
        assertThat("legal bases", actualJudgment.getLegalBases(), containsListInAnyOrder(ccJudgment.getLegalBases()));
        assertThat("referenced regulation", actualJudgment.getReferencedRegulations(), containsListInAnyOrder(ccJudgment.getReferencedRegulations()));

    }

    @Test
    public void it_should_find_subList_of_judgments_ordered_by_judgmentDate(){
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
    public void it_should_find_judgments_between_startJudgmentDate_and_endJudgmentDate(){
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
    public void it_should_find_all_CommonCourt_Basic_Fields_With_Its_All_Divisions_Fields(){
        //given
        CommonCourt commonCourt = testJudgmentFactory.createFullCommonCourt(true);

        CommonCourtSearchFilter searchFilter = CommonCourtSearchFilter.builder()
                .initialize()
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
    public void it_should_find_subList_of_courts_ordered_by_id(){
        //given
        List<CommonCourt> commonCourts = testJudgmentFactory.createSimpleCommonCourts(true);

        commonCourts.sort((first, second) -> Integer.compare(first.getId(), second.getId()));

        int offset = 1;
        int limit = 2;
        CommonCourtSearchFilter searchFilter = CommonCourtSearchFilter.builder()
                .limit(limit)
                .offset(offset)
                .upBy("id")
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

    private static <T extends Judgment> List<String> toCaseNumbers(List<T> judgments){
        return judgments.stream().flatMap(j -> j.getCaseNumbers().stream()).collect(Collectors.toList());
        
    }

    private static List<String> toCodes(List<CommonCourt> courts){
        return courts.stream().map(CommonCourt::getCode).collect(Collectors.toList());
    }





}