package pl.edu.icm.saos.importer.common.converter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static pl.edu.icm.saos.common.util.StringTools.toRootLowerCase;
import static pl.edu.icm.saos.importer.common.correction.ImportCorrectionBuilder.createDelete;
import static pl.edu.icm.saos.importer.common.correction.ImportCorrectionBuilder.createUpdate;
import static pl.edu.icm.saos.persistence.correction.model.CorrectedProperty.NAME;

import java.util.List;

import org.assertj.core.util.Lists;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import pl.edu.icm.saos.common.util.PersonNameNormalizer;
import pl.edu.icm.saos.importer.common.correction.ImportCorrectionList;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judge.JudgeRole;

/**
 * @author Łukasz Dumiszewski
 */

public class JudgeConverterTest {

    
    private JudgeConverter judgeConverter = new JudgeConverter();
    
    @Mock private JudgeNameNormalizer judgeNameNormalizer;
    
    private ImportCorrectionList correctionList = new ImportCorrectionList();
    
    
    @Before
    public void before() {
        
        initMocks(this);
        
        judgeConverter.setJudgeNameNormalizer(judgeNameNormalizer);
    }
    
    
    
    //------------------------ TESTS --------------------------
    
    
    
    @Test(expected=NullPointerException.class)
    public void convert_correctionListNull() {
    
        judgeConverter.convertJudge("ssd ", null);
    }
    
    
    @Test
    public void convert_normalizedNameBlank() {
        
        // given
        
        String judgeName = " !! ";
        when(judgeNameNormalizer.normalize(judgeName)).thenReturn(" ");
        
        
        // execute
        
        Judge judge = judgeConverter.convertJudge(judgeName, correctionList);
        
        
        // assert
        
        assertNull(judge);
        
        assertEquals(1, correctionList.getNumberOfCorrections());
        correctionList.hasImportCorrection(createDelete(Judge.class).oldValue(judgeName).newValue(null).build());
    }
    
    
    
    @Test
    public void convert_nameCorrection() {
        
        // given
        
        String judgeName = " Jan Nowak!! ";
        String normalizedJudgeName = "Jan Nowak";
        
        when(judgeNameNormalizer.normalize(judgeName)).thenReturn(normalizedJudgeName);
        
        
        // execute
        
        Judge judge = judgeConverter.convertJudge(judgeName, correctionList);
        
        
        // assert
        
        assertNotNull(judge);
        assertEquals(normalizedJudgeName, judge.getName());
        assertEquals(0, judge.getSpecialRoles().size());
        
        assertEquals(1, correctionList.getNumberOfCorrections());
        correctionList.hasImportCorrection(createUpdate(judge).ofProperty(NAME).oldValue(judgeName).newValue(normalizedJudgeName).build());
    }
    
    
    @Test
    public void convert_withRoles_noCorrection() {
        
        // given
        
        String judgeName = " Jan Nowak ";
        String normalizedJudgeName = "Jan Nowak";
        List<JudgeRole> judgeRoles = Lists.newArrayList(JudgeRole.PRESIDING_JUDGE, JudgeRole.REPORTING_JUDGE);
        
        when(judgeNameNormalizer.normalize(judgeName)).thenReturn(normalizedJudgeName);
        
        
        // execute
        
        Judge judge = judgeConverter.convertJudge(judgeName, judgeRoles, correctionList);
        
        
        // assert
        
        assertNotNull(judge);
        assertEquals(normalizedJudgeName, judge.getName());
        assertThat(judge.getSpecialRoles(), Matchers.containsInAnyOrder(JudgeRole.PRESIDING_JUDGE, JudgeRole.REPORTING_JUDGE));
        
        assertEquals(0, correctionList.getNumberOfCorrections());
        
    }
    
    
    @Test
    public void convert_LongDashIsNoCorrection() {
 
        // given
        
        String judgeName = "Jan Nowak– Kos"; // long dash [-] here 
        String normalizedJudgeName = "Jan Nowak-Kos";
        
        assertNotEquals(toRootLowerCase(judgeName), PersonNameNormalizer.unify(judgeName));
        assertEquals(toRootLowerCase(normalizedJudgeName), PersonNameNormalizer.unify(normalizedJudgeName));
        
        when(judgeNameNormalizer.normalize(judgeName)).thenReturn(normalizedJudgeName);
        
        
        // execute
        
        Judge judge = judgeConverter.convertJudge(judgeName, correctionList);
        
        
        // assert
        
        assertNotNull(judge);
        assertEquals(normalizedJudgeName, judge.getName());
        assertEquals(0, judge.getSpecialRoles().size());
        
        assertEquals(0, correctionList.getNumberOfCorrections());

    }
    
}
