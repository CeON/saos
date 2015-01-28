package pl.edu.icm.saos.importer.common;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static pl.edu.icm.saos.persistence.correction.model.JudgmentCorrectionBuilder.createFor;

import java.util.List;

import org.apache.commons.lang3.RandomUtils;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.reflect.Whitebox;

import pl.edu.icm.saos.importer.common.correction.ImportCorrectionBuilder;
import pl.edu.icm.saos.importer.common.correction.ImportCorrectionConverter;
import pl.edu.icm.saos.importer.common.correction.ImportCorrectionList;
import pl.edu.icm.saos.persistence.correction.JudgmentCorrectionRepository;
import pl.edu.icm.saos.persistence.correction.model.CorrectedProperty;
import pl.edu.icm.saos.persistence.correction.model.JudgmentCorrection;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.SourceCode;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;

import com.google.common.collect.Lists;

/**
 * @author Łukasz Dumiszewski
 */

public class JudgmentImportProcessWriterTest {

    private JudgmentImportProcessWriter<Judgment> judgmentImportProcessWriter = new JudgmentImportProcessWriter<>();
    
    @Mock private JudgmentRepository judgmentRepository;
    
    @Mock private ImportCorrectionConverter importCorrectionConverter;
    
    @Mock private JudgmentCorrectionRepository judgmentCorrectionRepository;
    
    @Captor
    private ArgumentCaptor<List<Judgment>> argJudgments;

    @Captor
    private ArgumentCaptor<List<Long>> argJudgmentIds;
    
    @Captor
    private ArgumentCaptor<List<JudgmentCorrection>> argJudgmentCorrections;
    
    
    @Before
    public void before() {
        
        MockitoAnnotations.initMocks(this);
        
        judgmentImportProcessWriter.setJudgmentRepository(judgmentRepository);
        judgmentImportProcessWriter.setImportCorrectionConverter(importCorrectionConverter);
        judgmentImportProcessWriter.setJudgmentCorrectionRepository(judgmentCorrectionRepository);
        
    }
    
    
    
    //------------------------ LOGIC --------------------------
    
    @Test
    public void write() {
    
        // given 
        
        JudgmentWithCorrectionList<Judgment> jwc1 = new JudgmentWithCorrectionList<>(createScJudgment(), createImportCorrectionList());
        JudgmentWithCorrectionList<Judgment> jwc2 = new JudgmentWithCorrectionList<>(createScJudgment(), createImportCorrectionList());
        JudgmentWithCorrectionList<Judgment> jwc3 = new JudgmentWithCorrectionList<>(createScJudgment(), createImportCorrectionList());
        
        @SuppressWarnings("unchecked")
        List<JudgmentWithCorrectionList<Judgment>> judgmentsWithCorrectionList = Lists.newArrayList(jwc1, jwc2, jwc3);
        
        
        List<JudgmentCorrection> j1Corrections = createJudgmentCorrections(jwc1.getJudgment(), 1);
        
        when(importCorrectionConverter.convertToJudgmentCorrections(jwc1.getJudgment(), jwc1.getCorrectionList().getImportCorrections())).thenReturn(j1Corrections);
        
        
        List<JudgmentCorrection> j2Corrections = createJudgmentCorrections(jwc2.getJudgment(), 2);
        
        when(importCorrectionConverter.convertToJudgmentCorrections(jwc2.getJudgment(), jwc2.getCorrectionList().getImportCorrections())).thenReturn(j2Corrections);
        
        
        List<JudgmentCorrection> j3Corrections = createJudgmentCorrections(jwc3.getJudgment(), 3);
        when(importCorrectionConverter.convertToJudgmentCorrections(jwc3.getJudgment(), jwc3.getCorrectionList().getImportCorrections())).thenReturn(j3Corrections);
        
        
        // execute
        
        judgmentImportProcessWriter.write(judgmentsWithCorrectionList);
        
        
        // assert
        
        verify(judgmentRepository).save(argJudgments.capture());
        assertThat(argJudgments.getValue(), Matchers.containsInAnyOrder(jwc1.getJudgment(), jwc2.getJudgment(), jwc3.getJudgment()));
        verify(judgmentRepository).flush();
        
        verify(judgmentCorrectionRepository).deleteByJudgmentIds(argJudgmentIds.capture());
        assertThat(argJudgmentIds.getValue(), Matchers.containsInAnyOrder(jwc1.getJudgment().getId(), jwc2.getJudgment().getId(), jwc3.getJudgment().getId()));
        
        verify(judgmentCorrectionRepository, times(judgmentsWithCorrectionList.size())).save(argJudgmentCorrections.capture());
        Matcher<Iterable<? extends Object>> containsInAnyOrder = Matchers.containsInAnyOrder(j1Corrections, j2Corrections, j3Corrections);
        assertThat(argJudgmentCorrections.getAllValues(), containsInAnyOrder);
        verify(judgmentCorrectionRepository, times(2)).flush();
        
    }



    
    
    
    //------------------------ PRIVATE --------------------------
    
    
    private SupremeCourtJudgment createScJudgment() {
        SupremeCourtJudgment scJudgment1 = new SupremeCourtJudgment();
        Whitebox.setInternalState(scJudgment1, "id", RandomUtils.nextInt(1, 100));
        scJudgment1.getSourceInfo().setSourceJudgmentId(randomAlphabetic(15));
        scJudgment1.getSourceInfo().setSourceCode(SourceCode.SUPREME_COURT);
        return scJudgment1;
    }
    
    
    private ImportCorrectionList createImportCorrectionList() {
        ImportCorrectionList correctionList = new ImportCorrectionList();
        correctionList.addCorrection(ImportCorrectionBuilder.createUpdate(new Judge(randomAlphabetic(12)))
                                        .ofProperty(CorrectedProperty.NAME)
                                        .oldValue(randomAlphabetic(23))
                                        .newValue(randomAlphabetic(12))
                                        .build());
                             
        return correctionList;
    }
    
    
    private List<JudgmentCorrection> createJudgmentCorrections(Judgment judgment, int judgeId) {
        
        Judge judge = new Judge("AAAA");
        Whitebox.setInternalState(judge, "id", judgeId);
        
        List<JudgmentCorrection> j1Corrections = Lists.newArrayList(createFor(judgment)
                                                        .update(judge)
                                                        .property(CorrectedProperty.NAME)
                                                        .oldValue("Sędzia Jan").newValue("Jan")
                                                        .build());
        return j1Corrections;
    }

    
}
