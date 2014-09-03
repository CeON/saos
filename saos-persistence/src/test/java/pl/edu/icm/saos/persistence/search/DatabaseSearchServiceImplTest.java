package pl.edu.icm.saos.persistence.search;

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
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.search.dto.JudgmentSearchFilter;
import pl.edu.icm.saos.persistence.search.result.SearchResult;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

import java.util.List;
import java.util.stream.Collectors;


@Category(SlowTest.class)
public class DatabaseSearchServiceImplTest extends PersistenceTestSupport{

    private static final Logger logger = LoggerFactory.getLogger(DatabaseSearchServiceImplTest.class);

    @Autowired
    private TestJudgmentFactory testJudgmentFactory;

    @Autowired
    private DatabaseSearchService databaseSearchService;

    @Test
    public void itShouldFindJudgmentsWithAllBasicFields(){
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
        assertThat("case number",actualJudgment.getCaseNumber(), is(ccJudgment.getCaseNumber()));
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

        assertThat("reasoning text",actualJudgment.getReasoning().getText(), is(ccJudgment.getReasoning().getText()));
        assertThat("reasoning source", actualJudgment.getReasoning().getSourceInfo(), is(ccJudgment.getReasoning().getSourceInfo()));
    }

    @Test
    public void itShouldFindSubListOfJudgmentsOrderedByJudgmentDate(){
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
        assertThat(toCaseNumbers(actualJudgments), containsListInAnyOrder(toCaseNumbers(judgments.subList(1, 3))));
    }

    @Test
    public void itShouldFindJudgmentsBetweenStartJudgmentDateAndEndJudgmentDate(){
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
        assertThat(actualJudgments.get(0).getCaseNumber(), is(givenJudgment.getCaseNumber()));
    }

    private static  org.hamcrest.Matcher<java.lang.Iterable<?>> containsListInAnyOrder(List<?> items) {
        return containsInAnyOrder(items.toArray());
    }

    private static <T extends Judgment> List<String> toCaseNumbers(List<T> judgments){
        return judgments.stream().map(Judgment::getCaseNumber).collect(Collectors.toList());
    }





}