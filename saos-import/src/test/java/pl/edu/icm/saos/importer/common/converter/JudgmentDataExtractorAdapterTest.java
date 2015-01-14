package pl.edu.icm.saos.importer.common.converter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import pl.edu.icm.saos.importer.common.correction.ImportCorrectionList;
import pl.edu.icm.saos.importer.notapi.common.SourceJudgment;
import pl.edu.icm.saos.persistence.model.Judgment;

/**
 * @author madryk
 */
@RunWith(MockitoJUnitRunner.class)
public class JudgmentDataExtractorAdapterTest {

    private JudgmentDataExtractorAdapter<Judgment, SourceJudgment> dataExtractorAdapter = new JudgmentDataExtractorAdapter<Judgment, SourceJudgment>();
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void extractJudgmentData() {
        SourceJudgment sourceJudgment = mock(SourceJudgment.class);
        ImportCorrectionList correctionList = mock(ImportCorrectionList.class);
        
        assertThat(dataExtractorAdapter.extractCourtCases(sourceJudgment, correctionList), is(empty()));
        assertThat(dataExtractorAdapter.extractTextContent(sourceJudgment, correctionList), is(nullValue()));
        assertThat(dataExtractorAdapter.extractPublicationDate(sourceJudgment, correctionList), is(nullValue()));
        assertThat(dataExtractorAdapter.extractPublisher(sourceJudgment, correctionList), is(nullValue()));
        assertThat(dataExtractorAdapter.extractReviser(sourceJudgment, correctionList), is(nullValue()));
        assertThat(dataExtractorAdapter.extractJudges(sourceJudgment, correctionList), is(empty()));
        assertThat(dataExtractorAdapter.extractReferencedRegulations(sourceJudgment, correctionList), is(empty()));
        assertThat(dataExtractorAdapter.extractJudgmentType(sourceJudgment, correctionList), is(nullValue()));
        assertThat(dataExtractorAdapter.extractLegalBases(sourceJudgment, correctionList), is(empty()));
        assertThat(dataExtractorAdapter.extractSummary(sourceJudgment, correctionList), is(nullValue()));
        assertThat(dataExtractorAdapter.extractDecision(sourceJudgment, correctionList), is(nullValue()));
        assertThat(dataExtractorAdapter.extractCourtReporters(sourceJudgment, correctionList), is(empty()));
        assertThat(dataExtractorAdapter.extractJudgmentDate(sourceJudgment, correctionList), is(nullValue()));
        assertThat(dataExtractorAdapter.extractSourceJudgmentId(sourceJudgment, correctionList), is(nullValue()));
        assertThat(dataExtractorAdapter.extractSourceJudgmentUrl(sourceJudgment, correctionList), is(nullValue()));
        
        verifyZeroInteractions(sourceJudgment);
        verifyZeroInteractions(correctionList);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void createNewJudgment() {
        dataExtractorAdapter.createNewJudgment();
    }
    
    @Test
    public void getSourceCode() {
        assertThat(dataExtractorAdapter.getSourceCode(), is(nullValue()));
    }
    
    @Test
    public void convertSpecific() {
        Judgment judgment = mock(Judgment.class);
        SourceJudgment sourceJudgment = mock(SourceJudgment.class);
        ImportCorrectionList correctionList = mock(ImportCorrectionList.class);
        
        dataExtractorAdapter.convertSpecific(judgment, sourceJudgment, correctionList);
        
        verifyZeroInteractions(judgment);
        verifyZeroInteractions(sourceJudgment);
        verifyZeroInteractions(correctionList);
    }
}
