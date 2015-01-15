package pl.edu.icm.saos.importer.notapi.common;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import pl.edu.icm.saos.importer.common.converter.JudgeConverter;
import pl.edu.icm.saos.importer.common.correction.ImportCorrectionList;
import pl.edu.icm.saos.importer.notapi.common.SourceJudgment.SourceJudge;
import pl.edu.icm.saos.importer.notapi.supremecourt.judgment.json.SourceScJudgment;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judge.JudgeRole;

import com.google.common.collect.Lists;

/**
 * @author madryk
 */
@RunWith(MockitoJUnitRunner.class)
public class SourceJudgeExtractorHelperTest {

    private SourceJudgeExtractorHelper sourceJudgeExtractorHelper = new SourceJudgeExtractorHelper();
    
    @Mock
    private JudgeConverter judgeConverter;
    
    
    private SourceJudgment sJudgment = new SourceScJudgment();
    
    private ImportCorrectionList correctionList = new ImportCorrectionList();
    
    
    @Before
    public void setUp() {
        sourceJudgeExtractorHelper.setJudgeConverter(judgeConverter);
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void extractJudges() {
        
        // given
        
        String janNowak = "Jan Nowak";
        String adamKowalski = "Adam Kowalski";
        String wrongName = "!! 11";
        
        SourceJudge sourceJudge1 = new SourceJudge();
        sourceJudge1.setName(janNowak);
        sourceJudge1.setFunction("SSN");
        sourceJudge1.setSpecialRoles(Lists.newArrayList(JudgeRole.PRESIDING_JUDGE.name(), JudgeRole.REPORTING_JUDGE.name()));
        
        SourceJudge sourceJudge2 = new SourceJudge();
        sourceJudge2.setName(adamKowalski);
        sourceJudge2.setFunction("SSA");
        
        SourceJudge sourceJudge3 = new SourceJudge();
        sourceJudge3.setName(wrongName);
        
        
        SourceJudge sourceJudgeBlank = new SourceJudge(); // shouldn't be taken into account because it's name is blank
        
        
        sJudgment.setJudges(Lists.newArrayList(sourceJudge1, sourceJudge2, sourceJudge3, sourceJudgeBlank));
        
        
        Judge judgeJanNowak = new Judge(janNowak, JudgeRole.PRESIDING_JUDGE, JudgeRole.REPORTING_JUDGE);
        Judge judgeAdamKowalski = new Judge(adamKowalski);
        when(judgeConverter.convertJudge(janNowak, Lists.newArrayList(JudgeRole.PRESIDING_JUDGE, JudgeRole.REPORTING_JUDGE), correctionList)).thenReturn(judgeJanNowak);
        when(judgeConverter.convertJudge(adamKowalski, Lists.newArrayList(), correctionList)).thenReturn(judgeAdamKowalski);
        when(judgeConverter.convertJudge(wrongName, Lists.newArrayList(), correctionList)).thenReturn(null);
        
        
        
        // execute
        
        List<Judge> judges = sourceJudgeExtractorHelper.extractJudges(sJudgment, correctionList);
        
        
        
        // assert
        
        assertEquals(2, judges.size());
        
        for (Judge judge : judges) {
            sJudgment.getJudges().contains(judge.getName());
            if (judge.getName().equals(sourceJudge1.getName())) {
                assertThat(judge.getSpecialRoles(), Matchers.containsInAnyOrder(JudgeRole.PRESIDING_JUDGE, JudgeRole.REPORTING_JUDGE));
                assertEquals(sourceJudge1.getFunction(), judge.getFunction());
            } else {
                assertThat(judge.getSpecialRoles(), Matchers.containsInAnyOrder(sourceJudge2.getSpecialRoles().toArray()));
                assertEquals(sourceJudge2.getFunction(), judge.getFunction());
            }
        }
        
    }
}
