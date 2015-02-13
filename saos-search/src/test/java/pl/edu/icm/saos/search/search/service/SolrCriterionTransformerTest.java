package pl.edu.icm.saos.search.search.service;

import static org.junit.Assert.assertEquals;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;

import pl.edu.icm.saos.persistence.model.CourtType;
import pl.edu.icm.saos.persistence.model.Judgment.JudgmentType;
import pl.edu.icm.saos.search.config.model.JudgmentIndexField;
import pl.edu.icm.saos.search.search.service.SolrCriterionTransformer.Operator;

import com.google.common.collect.Lists;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

/**
 * @author madryk
 */
@RunWith(DataProviderRunner.class)
public class SolrCriterionTransformerTest {

    private SolrCriterionTransformer<JudgmentIndexField> criterionTransformer = new SolrCriterionTransformer<JudgmentIndexField>();
    
    @DataProvider
    public static Object[][] transformCriterionWithParsingData() {
        return new Object[][] {
                {"+content:word", "word"},
                {"+content:word1 +content:word2", "word1 word2"},
                
                // with OR operator
                {"+(content:word1 content:word2)", "word1 OR word2"},
                {"+(content:word1 content:word2) +content:word3", "word1 OR word2 word3"},
                {"+(content:word1 content:word2 content:word3)", "word1 OR word2 OR word3"},
                {"+(content:word1 content:word2) +(content:word3 content:word4)", "word1 OR word2 word3 OR word4"},
                {"+content:OR", "OR"},
                {"+content:OR +content:OR", "OR OR"},
                {"+(content:OR content:OR)", "OR OR OR"},
                {"+(content:OR content:OR) +content:OR", "OR OR OR OR"},
                {"+(content:word1 content:word2)", "word1 or word2"},
                {"+(content:word1 content:word2)", "word1 oR word2"},
                {"+content:word1OR +content:word2", "word1OR word2"},
                {"+content:word1 +content:oRword2", "word1 oRword2"},
                
                // with quote ("")
                {"+content:\"some phrase\"", "\"some phrase\""},
                {"+content:\"word1 word2\" +content:word3", "\"word1 word2\" word3"},
                {"+content:\"word1 word2 \" +content:word3", "\"word1 word2 \"word3"},
                {"+content:word1 +content:\"word2\" +content:word3", "word1 \"word2\" word3"},
                {"+content:\"word1 word2\" +content:\"word3 word4\"", "\"word1 word2\" \"word3 word4\""},
                {"+content:\"word1 word2 \" +content:\" word3 word4\"", "\"word1 word2 \"\" word3 word4\""},
                {"+content:wordword +content:\"word\" +content:word", "wordword\"word\"word"},
                {"+content:word1 +content:\"word2 word3\"", "word1 \"word2 word3\""},
                {"+content:word1 +content:\" word2 word3\"", "word1\" word2 word3\""},
                
                {"+content:some +content:\\\"phrase", "some \"phrase"},
                {"+content:\"word1 word2\" +content:\\\" +content:word3", "\"word1 word2\"\" word3"},
                
                // with exclusion (minus sign)
                {"-content:word", "-word"},
                {"-content:\\-word", "--word"},
                {"+content:word1 -content:word2", "word1 -word2"},
                {"+content:word1 -content:word2 -content:word3", "word1 -word2 -word3"},
                {"+content:word1 -content:word2 +content:word3 -content:word4", "word1 -word2 word3 -word4"},
                {"+content:word1 +content:\\- +content:word2", "word1 - word2"},
                
                // with OR operator and quote
                {"+(content:word1 content:\"word2 word3\")", "word1 OR \"word2 word3\""},
                
                // with quote and exclusion
                {"-content:\"word1 word2\"", "-\"word1 word2\""},
                {"+content:word1 -content:\"word2 word3\"", "word1 -\"word2 word3\""},
                
                // with OR operator and exclusion
                {"+(content:word1 -content:word2)", "word1 OR -word2"},
                {"+(content:word1 content:word2) -content:word3", "word1 OR word2 -word3"},
                
                // with OR operator, quote and exclusion
                {"+(content:word1 -content:\"word2 word3\")", "word1 OR -\"word2 word3\""},
                {"+(-content:word1 content:\"word2 word3\")", "-word1 OR \"word2 word3\""},
                
                {"", ""},
        };
    }
    
    @Test
    @UseDataProvider("transformCriterionWithParsingData")
    public void transformAll(String expectedQuery, String criterionValue) {
        String actualQuery = criterionTransformer.transformCriterionWithParsing(JudgmentIndexField.CONTENT, criterionValue);
        
        assertEquals(expectedQuery, actualQuery);
    }
    
    @Test
    public void transformCriterion_STRING() {
        String actual = criterionTransformer.transformEqualsCriterion(JudgmentIndexField.KEYWORD, "word");
        
        assertEquals("+keyword:word", actual);
    }
    
    @Test
    public void transformCriterion_INT() {
        String actual = criterionTransformer.transformEqualsCriterion(JudgmentIndexField.CC_COURT_ID, 12);
        
        assertEquals("+ccCourtId:12", actual);
    }
    
    @Test
    public void transformCriterion_ENUM() {
        String actual = criterionTransformer.transformEqualsCriterion(JudgmentIndexField.COURT_TYPE, CourtType.SUPREME);
        
        assertEquals("+courtType:SUPREME", actual);
    }
    
    @Test
    public void transformCriterion_MULTI_AND() {
        String actual = criterionTransformer.transformEqualsCriteria(JudgmentIndexField.KEYWORD, Lists.newArrayList("word1", "word2", "word3"), Operator.AND);
        
        assertEquals("+keyword:word1 +keyword:word2 +keyword:word3", actual);
    }
    
    @Test
    public void transformCriterion_MULTI_OR() {
        String actual = criterionTransformer.transformEqualsCriteria(JudgmentIndexField.JUDGMENT_TYPE, 
                Lists.newArrayList(JudgmentType.DECISION.name(), JudgmentType.SENTENCE.name()), Operator.OR);
        
        assertEquals("+(judgmentType:DECISION judgmentType:SENTENCE)", actual);
    }
    
    @Test
    public void transformCriterion_SINGLE_OR() {
        String actual = criterionTransformer.transformEqualsCriteria(JudgmentIndexField.JUDGMENT_TYPE, 
                Lists.newArrayList(JudgmentType.DECISION.name()), Operator.OR);
        
        assertEquals("+judgmentType:DECISION", actual);
    }
    
    @Test
    public void transformCriterion_MULTI_ENUM() {
        String actual = criterionTransformer.transformEqualsEnumCriteria(JudgmentIndexField.JUDGMENT_TYPE, 
                Lists.newArrayList(JudgmentType.DECISION, JudgmentType.REASONS, JudgmentType.SENTENCE), Operator.OR);
        
        assertEquals("+(judgmentType:DECISION judgmentType:REASONS judgmentType:SENTENCE)", actual);
    }
    
    @Test
    public void transformDateRangeCriterion() {
        String actual = criterionTransformer.transformDateRangeCriterion(
                JudgmentIndexField.JUDGMENT_DATE, new LocalDate(2012, 9, 23), new LocalDate(2013, 2, 6));
        
        assertEquals("+judgmentDate:[2012-09-23T00:00:00Z TO 2013-02-06T23:59:59Z]", actual);
    }
    
    @Test
    public void transformRangeCriterion() {
        String actual = criterionTransformer.transformRangeCriterion(JudgmentIndexField.KEYWORD, "aa", "ba");
        
        assertEquals("+keyword:[aa TO ba]", actual);
    }
    
}
