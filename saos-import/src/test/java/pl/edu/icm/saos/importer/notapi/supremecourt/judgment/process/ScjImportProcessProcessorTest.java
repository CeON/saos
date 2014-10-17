package pl.edu.icm.saos.importer.notapi.supremecourt.judgment.process;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import pl.edu.icm.saos.importer.common.JudgmentConverter;
import pl.edu.icm.saos.importer.notapi.supremecourt.judgment.json.SourceScJudgment;
import pl.edu.icm.saos.importer.notapi.supremecourt.judgment.json.SourceScJudgmentParser;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment;
import pl.edu.icm.saos.persistence.model.importer.notapi.RawSourceScJudgment;

/**
 * @author Łukasz Dumiszewski
 */

public class ScjImportProcessProcessorTest {

    private ScjImportProcessProcessor scjImportProcessProcessor = new ScjImportProcessProcessor();
    
    @Mock private SourceScJudgmentParser sourceScJudgmentParser;
    
    @Mock private JudgmentConverter<SupremeCourtJudgment, SourceScJudgment> sourceScJudgmentConverter;
    
    
    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        
        scjImportProcessProcessor.setSourceScJudgmentConverter(sourceScJudgmentConverter);
        scjImportProcessProcessor.setSourceScJudgmentParser(sourceScJudgmentParser);
        
        
        
    }
    
    @Test
    public void process() {
        
        // given
        
        RawSourceScJudgment rJudgment = new RawSourceScJudgment();
        rJudgment.setJsonContent("12121212esfcsfc");
        
        SourceScJudgment sourceScJudgment = new SourceScJudgment();
        when(sourceScJudgmentParser.parse(rJudgment.getJsonContent())).thenReturn(sourceScJudgment);
        
        SupremeCourtJudgment supremeCourtJudgment = new SupremeCourtJudgment();
        when(sourceScJudgmentConverter.convertJudgment(sourceScJudgment)).thenReturn(supremeCourtJudgment);
        
        // execute
        
        SupremeCourtJudgment retScJudgment = scjImportProcessProcessor.process(rJudgment);
        
        // assert
        
        assertTrue(supremeCourtJudgment == retScJudgment);
        
    }
    
    
}
