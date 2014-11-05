package pl.edu.icm.saos.api.search.judgments.transformers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import pl.edu.icm.saos.api.search.transformers.SearchResultApiTransformer;
import pl.edu.icm.saos.api.services.FieldsDefinition.JC;
import pl.edu.icm.saos.persistence.model.CcJudgmentKeyword;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judge.JudgeRole;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.search.search.model.JudgeResult;
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
        JudgeResult presidingJudge = new JudgeResult(JC.PRESIDING_JUDGE_NAME, JudgeRole.PRESIDING_JUDGE);
        JudgeResult secondJudge = new JudgeResult(JC.SECOND_JUDGE_NAME);

        JudgmentSearchResult searchResult = new JudgmentSearchResult();
        searchResult.setCaseNumbers(Lists.newArrayList(JC.CASE_NUMBER));
        searchResult.setCourtDivisionId(JC.DIVISION_ID);
        searchResult.setCourtDivisionCode(JC.DIVISION_CODE);
        searchResult.setCourtDivisionName(JC.DIVISION_NAME);
        searchResult.setCourtId(JC.COURT_ID);
        searchResult.setCourtCode(JC.COURT_CODE);
        searchResult.setCourtName(JC.COURT_NAME);
        searchResult.setId(JC.JUDGMENT_ID);
        searchResult.setKeywords(Arrays.asList(JC.FIRST_KEYWORD, JC.SECOND_KEYWORD));
        searchResult.setJudgmentDate(judgmentDate);
        searchResult.setJudgmentType(Judgment.JudgmentType.SENTENCE.name());
        searchResult.setJudges(Arrays.asList(presidingJudge, secondJudge));

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
        Judge actualPresidingJudge = ccJudgment.getJudges().stream()
                .filter(judge -> StringUtils.equals(judge.getName(), JC.PRESIDING_JUDGE_NAME))
                .findFirst().get();
        Judge actualSecondJudge = ccJudgment.getJudges().stream()
                .filter(judge -> StringUtils.equals(judge.getName(), JC.SECOND_JUDGE_NAME))
                .findFirst().get();
        assertThat(actualSecondJudge.getSpecialRoles(), empty());
        assertThat(actualPresidingJudge.getSpecialRoles(), hasItem(JudgeRole.PRESIDING_JUDGE));

        assertThat(ccJudgment.getCourtDivision().getId(), is(JC.DIVISION_ID));
        assertThat(ccJudgment.getCourtDivision().getCode(), is(JC.DIVISION_CODE));
        assertThat(ccJudgment.getCourtDivision().getName(), is(JC.DIVISION_NAME));
        assertThat(ccJudgment.getCourtDivision().getCourt().getId(), is(JC.COURT_ID));
        assertThat(ccJudgment.getCourtDivision().getCourt().getCode(), is(JC.COURT_CODE));
        assertThat(ccJudgment.getCourtDivision().getCourt().getName(), is(JC.COURT_NAME));
    }


    private static  org.hamcrest.Matcher<java.lang.Iterable<?>> containsListInAnyOrder(List<?> items) {
        return containsInAnyOrder(items.toArray());
    }

}