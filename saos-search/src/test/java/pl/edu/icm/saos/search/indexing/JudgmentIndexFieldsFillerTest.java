package pl.edu.icm.saos.search.indexing;

import static pl.edu.icm.saos.search.indexing.SolrDocumentAssert.assertFieldValue;
import static pl.edu.icm.saos.search.indexing.SolrDocumentAssert.assertFieldValues;

import org.apache.solr.common.SolrInputDocument;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.CourtCase;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judge.JudgeRole;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.Judgment.JudgmentType;
import pl.edu.icm.saos.persistence.model.JudgmentReferencedRegulation;
import pl.edu.icm.saos.search.config.model.JudgmentIndexField;

/**
 * @author madryk
 */
public class JudgmentIndexFieldsFillerTest {

    private JudgmentIndexFieldsFiller<Judgment> judgmentIndexFieldsFiller = new JudgmentIndexFieldsFiller<Judgment>();
    
    private SolrFieldAdder<JudgmentIndexField> fieldAdder = new SolrFieldAdder<JudgmentIndexField>();
    
    @Before
    public void setUp() {
        judgmentIndexFieldsFiller.setFieldAdder(fieldAdder);
    }
    
    @Test
    public void fillIds() {
        CommonCourtJudgment judgment = new CommonCourtJudgment();
        ReflectionTestUtils.setField(judgment, "id", 6);
        
        SolrInputDocument doc = new SolrInputDocument();
        judgmentIndexFieldsFiller.fillIds(doc, judgment);
        
        assertFieldValue(doc, "databaseId", "6");
    }
    
    @Test
    public void fillCourtCases() {
        CommonCourtJudgment judgment = new CommonCourtJudgment();
        judgment.addCourtCase(new CourtCase("ABC1A"));
        
        SolrInputDocument doc = new SolrInputDocument();
        judgmentIndexFieldsFiller.fillCourtCases(doc, judgment);
        
        assertFieldValue(doc, "caseNumber", "ABC1A");
    }
    
    @Test
    public void fillJudges() {
        CommonCourtJudgment judgment = new CommonCourtJudgment();
        judgment.addJudge(new Judge("Jan Kowalski", JudgeRole.PRESIDING_JUDGE));
        judgment.addJudge(new Judge("Adam Nowak"));
        
        SolrInputDocument doc = new SolrInputDocument();
        judgmentIndexFieldsFiller.fillJudges(doc, judgment);
        
        assertFieldValue(doc, "judge", "Adam Nowak");
        assertFieldValue(doc, "judgeWithRole_#_PRESIDING_JUDGE", "Jan Kowalski");
    }
    
    @Test
    public void fillLegalBases() {
        CommonCourtJudgment judgment = new CommonCourtJudgment();
        judgment.addLegalBase("art 1203 kc");
        judgment.addLegalBase("art 1204 kc");
        
        SolrInputDocument doc = new SolrInputDocument();
        judgmentIndexFieldsFiller.fillLegalBases(doc, judgment);
        
        assertFieldValues(doc, "legalBases", "art 1203 kc", "art 1204 kc");
    }
    
    @Test
    public void fillReferencedRegulations() {
        CommonCourtJudgment judgment = new CommonCourtJudgment();
        JudgmentReferencedRegulation firstReferencedRegulation = new JudgmentReferencedRegulation();
        firstReferencedRegulation.setRawText("Ustawa 1");
        JudgmentReferencedRegulation secondReferencedRegulation = new JudgmentReferencedRegulation();
        secondReferencedRegulation.setRawText("Ustawa 2");
        judgment.addReferencedRegulation(firstReferencedRegulation);
        judgment.addReferencedRegulation(secondReferencedRegulation);
        
        SolrInputDocument doc = new SolrInputDocument();
        judgmentIndexFieldsFiller.fillReferencedRegulations(doc, judgment);
        
        assertFieldValues(doc, "referencedRegulations", "Ustawa 1", "Ustawa 2");
    }
    
    @Test
    public void fillJudgmentDate() {
        CommonCourtJudgment judgment = new CommonCourtJudgment();
        judgment.setJudgmentDate(new LocalDate(2014, 9, 4));
        
        SolrInputDocument doc = new SolrInputDocument();
        judgmentIndexFieldsFiller.fillJudgmentDate(doc, judgment);
        
        assertFieldValue(doc, "judgmentDate", "2014-09-04T00:00:00Z");
    }
    
    @Test
    public void fillJudgmentType() {
        CommonCourtJudgment judgment = new CommonCourtJudgment();
        judgment.setJudgmentType(JudgmentType.SENTENCE);
        
        SolrInputDocument doc = new SolrInputDocument();
        judgmentIndexFieldsFiller.fillJudgmentType(doc, judgment);
        
        assertFieldValue(doc, "judgmentType", "SENTENCE");
    }
    
    @Test
    public void fillContent() {
        CommonCourtJudgment judgment = new CommonCourtJudgment();
        judgment.setTextContent("some content");
        
        SolrInputDocument doc = new SolrInputDocument();
        judgmentIndexFieldsFiller.fillContent(doc, judgment);
        
        assertFieldValue(doc, "content", "some content");
    }
    
    @Test
    public void fillFields() {
        CommonCourtJudgment judgment = new CommonCourtJudgment();
        
        JudgmentReferencedRegulation firstReferencedRegulation = new JudgmentReferencedRegulation();
        firstReferencedRegulation.setRawText("Ustawa 1");
        
        ReflectionTestUtils.setField(judgment, "id", 6);
        judgment.addCourtCase(new CourtCase("ABC1A"));
        judgment.addJudge(new Judge("Adam Nowak"));
        judgment.addLegalBase("art 1203 kc");
        judgment.addReferencedRegulation(firstReferencedRegulation);
        judgment.setJudgmentDate(new LocalDate(2014, 9, 4));
        judgment.setJudgmentType(JudgmentType.SENTENCE);
        judgment.setTextContent("some content");
        
        
        SolrInputDocument doc = new SolrInputDocument();
        judgmentIndexFieldsFiller.fillFields(doc, judgment);
        
        
        assertFieldValue(doc, "databaseId", "6");
        assertFieldValue(doc, "caseNumber", "ABC1A");
        assertFieldValue(doc, "judge", "Adam Nowak");
        assertFieldValue(doc, "legalBases", "art 1203 kc");
        assertFieldValue(doc, "referencedRegulations", "Ustawa 1");
        assertFieldValue(doc, "judgmentDate", "2014-09-04T00:00:00Z");
        assertFieldValue(doc, "judgmentType", "SENTENCE");
        assertFieldValue(doc, "content", "some content");
    }

}
