package pl.edu.icm.saos.persistence.search;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.PersistenceTestSupport;
import pl.edu.icm.saos.persistence.common.TestJudgmentFactory;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.search.dto.JudgmentSearchFilter;
import pl.edu.icm.saos.persistence.search.result.SearchResult;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;


//@Category(SlowTest.class)
public class DatabaseSearchServiceImplTest extends PersistenceTestSupport{

    @Autowired
    private TestJudgmentFactory testJudgmentFactory;

    @Autowired
    private DatabaseSearchServiceImpl databaseSearchService;

    @Test
    public void shouldFindJudgmentsWithAllBasicFields(){
        //given
        CommonCourtJudgment ccJudgment = testJudgmentFactory.createFullCcJudgment(true);
        JudgmentSearchFilter searchFilter = new JudgmentSearchFilter();

        //when
        SearchResult<Judgment> searchResult = databaseSearchService.search(searchFilter);

        //then
        List<Judgment> judgments = searchResult.getResultRecords();
        assertThat(judgments, iterableWithSize(1));

        Judgment judgment = judgments.get(0);
        assertThat(judgment, is(instanceOf(CommonCourtJudgment.class)));

//        CommonCourtJudgment actualJudgment = (CommonCourtJudgment) judgment;
//        assertThat(actualJudgment.getCaseNumber(), is(ccJudgment.getCaseNumber()));
//        assertThat(actualJudgment.getCreationDate(), is(ccJudgment.getCreationDate()));
//        assertThat(actualJudgment.getCourtDivision().getId(), is(ccJudgment.getCourtDivision().getId()));
//        assertThat(actualJudgment.getKeywords(), contains(ccJudgment.getKeywords()));
////        assertThat(actualJudgment.getCourtReporters(), contains(ccJudgment.getCourtReporters()));
//        assertThat(actualJudgment.getDecision(), is(ccJudgment.getDecision()));
//        assertThat(actualJudgment.getJudges(), contains(ccJudgment.getJudges()));
//        assertThat(actualJudgment.getJudgmentDate(), is(ccJudgment.getJudgmentDate()));
//        assertThat(actualJudgment.getJudgmentType(), is(ccJudgment.getJudgmentType()));
//        assertThat(actualJudgment.getLegalBases(), contains(ccJudgment.getReasoning()));
//        assertThat(actualJudgment.getReferencedRegulations(), contains(ccJudgment.getReferencedRegulations()));
//
//        assertThat(actualJudgment.getReasoning().getText(), is(ccJudgment.getReasoning().getText()));


    }

}