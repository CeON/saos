package pl.edu.icm.saos.batch.jobs.importer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.stream.Collectors;

import pl.edu.icm.saos.persistence.model.CourtType;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judge.JudgeRole;
import pl.edu.icm.saos.persistence.model.Judgment;

/**
 * @author madryk
 */
class JudgmentAssertUtils {

    //------------------------ CONSTRUCTORS --------------------------
    
    private JudgmentAssertUtils() { }
    

    
    //------------------------ LOGIC --------------------------
    
    public static void assertJudge(Judgment judgment, String name, String expectedFunction, JudgeRole... expectedRoles) {
        Judge judge = judgment.getJudge(name);
        assertNotNull(judge);
        assertThat(judge.getSpecialRoles(), containsInAnyOrder(expectedRoles));
        assertEquals(expectedFunction, judge.getFunction());
    }
    
    public static void assertSourceJudgmentIds(List<Judgment> judgments, CourtType courtType, String... sourceJudgmentIds) {
        List<String> ids = judgments.stream()
                .filter(j -> j.getCourtType() == courtType)
                .map(j -> j.getSourceInfo().getSourceJudgmentId())
                .collect(Collectors.toList());
        assertThat(ids, containsInAnyOrder(sourceJudgmentIds));
    }
    
}
