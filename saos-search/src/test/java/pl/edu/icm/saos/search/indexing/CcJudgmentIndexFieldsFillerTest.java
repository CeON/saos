package pl.edu.icm.saos.search.indexing;

import static pl.edu.icm.saos.search.indexing.SolrDocumentAssert.assertFieldValue;
import static pl.edu.icm.saos.search.indexing.SolrDocumentAssert.assertFieldValues;

import java.util.List;

import org.apache.solr.common.SolrInputDocument;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import pl.edu.icm.saos.persistence.model.CcJudgmentKeyword;
import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.CommonCourt.CommonCourtType;
import pl.edu.icm.saos.persistence.model.CommonCourtDivision;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.search.config.model.JudgmentIndexField;

import com.google.common.collect.Lists;

/**
 * @author madryk
 */
public class CcJudgmentIndexFieldsFillerTest {

    private CcJudgmentIndexFieldsFiller ccJudgmentIndexingProcessor = new CcJudgmentIndexFieldsFiller();
    
    private SolrFieldAdder<JudgmentIndexField> fieldAdder = new SolrFieldAdder<JudgmentIndexField>();
    
    @Before
    public void setUp() {
        ccJudgmentIndexingProcessor.setFieldAdder(fieldAdder);
    }
    
    @Test
    public void fillKeywords() {
        CcJudgmentKeyword firstKeyword = new CcJudgmentKeyword("some keyword");
        CcJudgmentKeyword secondKeyword = new CcJudgmentKeyword("some other keyword");
        CommonCourtJudgment ccJudgment = createCommonCourtJudgment(1, null, Lists.newArrayList(firstKeyword, secondKeyword));
        
        SolrInputDocument doc = new SolrInputDocument();
        ccJudgmentIndexingProcessor.fillKeywords(doc, ccJudgment);
        
        assertFieldValues(doc, "keyword", "some keyword", "some other keyword");
    }
    
    @Test
    public void fillCourt() {
        CommonCourt commonCourt = createCommonCourt(1, "15200000", "Sąd Apelacyjny w Krakowie", CommonCourtType.APPEAL);
        CommonCourtDivision ccDivision = createCommonCourtDivision(1, "0000503", "I Wydział Cywilny", commonCourt);
        CommonCourtJudgment ccJudgment = createCommonCourtJudgment(1, ccDivision, Lists.newArrayList());
        
        SolrInputDocument doc = new SolrInputDocument();
        ccJudgmentIndexingProcessor.fillCourt(doc, ccJudgment);
        
        
        assertFieldValue(doc, "courtType", "APPEAL");

        assertFieldValue(doc, "courtId", "15200000");
        assertFieldValue(doc, "courtName", "Sąd Apelacyjny w Krakowie");

        assertFieldValue(doc, "courtDivisionId", "0000503");
        assertFieldValue(doc, "courtDivisionName", "I Wydział Cywilny");
    }
    
    @Test
    public void fillFields() {
        CommonCourt commonCourt = createCommonCourt(1, "15200000", "Sąd Apelacyjny w Krakowie", CommonCourtType.APPEAL);
        CommonCourtDivision ccDivision = createCommonCourtDivision(1, "0000503", "I Wydział Cywilny", commonCourt);
        CcJudgmentKeyword firstKeyword = new CcJudgmentKeyword("some keyword");
        
        CommonCourtJudgment ccJudgment = createCommonCourtJudgment(1, ccDivision, Lists.newArrayList(firstKeyword));
        
        
        SolrInputDocument doc = new SolrInputDocument();
        ccJudgmentIndexingProcessor.fillFields(doc, ccJudgment);
        
        
        assertFieldValues(doc, "keyword", "some keyword");
        
        assertFieldValue(doc, "courtType", "APPEAL");

        assertFieldValue(doc, "courtId", "15200000");
        assertFieldValue(doc, "courtName", "Sąd Apelacyjny w Krakowie");

        assertFieldValue(doc, "courtDivisionId", "0000503");
        assertFieldValue(doc, "courtDivisionName", "I Wydział Cywilny");
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private CommonCourt createCommonCourt(int id, String code, String name, CommonCourtType type) {
        CommonCourt commonCourt = new CommonCourt();
        
        ReflectionTestUtils.setField(commonCourt, "id", id);
        commonCourt.setCode(code);
        commonCourt.setName(name);
        commonCourt.setType(type);
        
        return commonCourt;
    }
    
    private CommonCourtDivision createCommonCourtDivision(int id, String code, String name, CommonCourt commonCourt) {
        CommonCourtDivision ccDivision = new CommonCourtDivision();
        
        ReflectionTestUtils.setField(ccDivision, "id", id);
        ccDivision.setCode(code);
        ccDivision.setName(name);
        ccDivision.setCourt(commonCourt);
        
        return ccDivision;
    }
    
    private CommonCourtJudgment createCommonCourtJudgment(int id, CommonCourtDivision division, List<CcJudgmentKeyword> keywords) {
        CommonCourtJudgment ccJudgment = new CommonCourtJudgment();
        
        ReflectionTestUtils.setField(ccJudgment, "id", id);
        for (CcJudgmentKeyword keyword : keywords) {
            ccJudgment.addKeyword(keyword);
        }
        ccJudgment.setCourtDivision(division);
        
        return ccJudgment;
    }
}
