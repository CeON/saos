package pl.edu.icm.saos.api.judgments.transformers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import pl.edu.icm.saos.api.transformers.SearchResultApiTransformer;
import pl.edu.icm.saos.api.utils.FieldsDefinition.JC;
import pl.edu.icm.saos.persistence.model.CcJudgmentKeyword;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.search.search.model.JudgmentSearchResult;

import com.google.common.collect.Lists;

public class JudgmentSearchResultApiTransformerTest {

    private SearchResultApiTransformer<JudgmentSearchResult, Judgment> transformer;


    @Before
    public void setUp(){
        transformer = new JudgmentSearchResultApiTransformer();
    }


    @Test
    public void it_should_transform_judgment_search_result_into_judgment_fields(){
        //given
        LocalDate judgmentDate = new LocalDate(JC.DATE_YEAR, JC.DATE_MONTH, JC.DATE_DAY);

        JudgmentSearchResult searchResult = new JudgmentSearchResult();
        searchResult.setCaseNumbers(Lists.newArrayList(JC.CASE_NUMBER));
        searchResult.setCourtDivisionName(JC.DIVISION_NAME);
        searchResult.setCourtName(JC.COURT_NAME);
        searchResult.setId(String.valueOf(JC.JUDGMENT_ID));
        searchResult.setKeywords(Arrays.asList(JC.FIRST_KEYWORD, JC.SECOND_KEYWORD));
        searchResult.setJudgmentDate(judgmentDate.toDate());
        searchResult.setJudgmentType(Judgment.JudgmentType.SENTENCE.name());
        searchResult.setJudges(Arrays.asList(JC.PRESIDING_JUDGE_NAME, JC.SECOND_JUDGE_NAME));

        //when
        Judgment judgment = transformer.transform(searchResult);

        //then
        assertThat(judgment, is(instanceOf(CommonCourtJudgment.class)));

        CommonCourtJudgment ccJudgment = (CommonCourtJudgment) judgment;

        assertThat(judgment.getId(), is(JC.JUDGMENT_ID));
        assertThat(judgment.getCaseNumbers(), containsInAnyOrder(JC.CASE_NUMBER));
        assertThat(judgment.getJudgmentDate(), is(judgmentDate));
        assertThat(judgment.getJudgmentType(), is(Judgment.JudgmentType.SENTENCE));

        assertThat(ccJudgment.getKeywords().stream()
                        .map(CcJudgmentKeyword::getPhrase)
                        .collect(Collectors.toList()),
                containsListInAnyOrder(Arrays.asList(JC.FIRST_KEYWORD, JC.SECOND_KEYWORD)));

        assertThat(ccJudgment.getJudges().stream()
                        .map(Judge::getName)
                        .collect(Collectors.toList()),
                containsListInAnyOrder(Arrays.asList(JC.PRESIDING_JUDGE_NAME, JC.SECOND_JUDGE_NAME))
        );

        assertThat(ccJudgment.getCourtDivision().getName(), is(JC.DIVISION_NAME));
        assertThat(ccJudgment.getCourtDivision().getCourt().getName(), is(JC.COURT_NAME));
    }


    private static  org.hamcrest.Matcher<java.lang.Iterable<?>> containsListInAnyOrder(List<?> items) {
        return containsInAnyOrder(items.toArray());
    }

}