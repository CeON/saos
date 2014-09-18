package pl.edu.icm.saos.search.indexing;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static pl.edu.icm.saos.search.indexing.SolrDocumentAssert.assertFieldValue;
import static pl.edu.icm.saos.search.indexing.SolrDocumentAssert.assertFieldValues;

import org.apache.solr.common.SolrInputDocument;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;

import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.CourtCase;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judge.JudgeRole;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.Judgment.JudgmentType;
import pl.edu.icm.saos.persistence.model.JudgmentReferencedRegulation;
import pl.edu.icm.saos.persistence.repository.JudgmentRepository;

/**
 * @author madryk
 */
public class JudgmentIndexingProcessorTest {

    JudgmentIndexingProcessor judgmentIndexingProcessor = new JudgmentIndexingProcessor();
    
    JudgmentRepository judgmentRepository = mock(JudgmentRepository.class);
    
    @Before
    public void setUp() {
        judgmentIndexingProcessor.setCcJudgmentRepository(judgmentRepository);
    }
    
    @Test
    public void process() throws Exception {
        CommonCourtJudgment judgment = new CommonCourtJudgment();
        
        JudgmentReferencedRegulation firstReferencedRegulation = new JudgmentReferencedRegulation();
        firstReferencedRegulation.setRawText("Ustawa 1");
        JudgmentReferencedRegulation secondReferencedRegulation = new JudgmentReferencedRegulation();
        secondReferencedRegulation.setRawText("Ustawa 2");
        
        ReflectionTestUtils.setField(judgment, "id", 6);
        judgment.addCourtCase(new CourtCase("ABC1A"));
        judgment.addJudge(new Judge("Jan Kowalski", JudgeRole.PRESIDING_JUDGE));
        judgment.addJudge(new Judge("Adam Nowak"));
        judgment.addLegalBase("art 1203 kc");
        judgment.addReferencedRegulation(firstReferencedRegulation);
        judgment.addReferencedRegulation(secondReferencedRegulation);
        judgment.setJudgmentDate(new LocalDate(2014, 9, 4));
        judgment.setJudgmentType(JudgmentType.SENTENCE);
        judgment.setTextContent("some content");
        
        
        SolrInputDocument doc = judgmentIndexingProcessor.process(judgment);
        
        
        assertFieldValue(doc, "databaseId", "6");
        assertFieldValue(doc, "caseNumber", "ABC1A");
        assertFieldValue(doc, "judge", "Adam Nowak");
        assertFieldValue(doc, "judgeWithRole_#_PRESIDING_JUDGE", "Jan Kowalski");
        assertFieldValue(doc, "legalBases", "art 1203 kc");
        assertFieldValues(doc, "referencedRegulations", "Ustawa 1", "Ustawa 2");
        assertFieldValue(doc, "judgmentDate", "2014-09-04T00:00:00Z");
        assertFieldValue(doc, "judgmentType", "SENTENCE");
        assertFieldValue(doc, "content", "some content");
        
        ArgumentCaptor<Judgment> argCapture = ArgumentCaptor.forClass(Judgment.class);
        verify(judgmentRepository, times(1)).save(argCapture.capture());
        assertEquals(6, argCapture.getValue().getId());
        assertTrue(argCapture.getValue().isIndexed());
    }
    
}
