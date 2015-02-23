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
                {"+content:\\OR", "OR"},
                {"+content:\\OR +content:\\OR", "OR OR"},
                {"+(content:\\OR content:\\OR)", "OR OR OR"},
                {"+(content:\\OR content:\\OR) +content:\\OR", "OR OR OR OR"},
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
                
                {"+content:\\AND", "AND"},
                {"+content:\\NOT", "NOT"},
                
                {"", ""},
        };
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    @UseDataProvider("transformCriterionWithParsingData")
    public void transformToEqualsCriterionWithParsing(String expectedQuery, String criterionValue) {
        // execute
        String actualQuery = criterionTransformer.transformToEqualsCriterionWithParsing(JudgmentIndexField.CONTENT, criterionValue);
        // assert
        assertEquals(expectedQuery, actualQuery);
    }
    
    @Test
    public void transformToEqualsCriterion_STRING() {
        // execute
        String actual = criterionTransformer.transformToEqualsCriterion(JudgmentIndexField.KEYWORD, "word");
        // assert
        assertEquals("+keyword:word", actual);
    }
    
    @Test
    public void transformToEqualsCriterion_ESCAPING() {
        // execute & assert
        assertEquals("+keyword:\\AND", criterionTransformer.transformToEqualsCriterion(JudgmentIndexField.KEYWORD, " AND "));
        assertEquals("+keyword:\\OR", criterionTransformer.transformToEqualsCriterion(JudgmentIndexField.KEYWORD, " OR "));
        assertEquals("+keyword:\\NOT", criterionTransformer.transformToEqualsCriterion(JudgmentIndexField.KEYWORD, " NOT "));
    }
    
    @Test
    public void transformToEqualsCriterion_INT() {
        // execute
        String actual = criterionTransformer.transformToEqualsCriterion(JudgmentIndexField.CC_COURT_ID, 12);
        // assert
        assertEquals("+ccCourtId:12", actual);
    }
    
    @Test
    public void transformToEqualsCriterion_ENUM() {
        // execute
        String actual = criterionTransformer.transformToEqualsCriterion(JudgmentIndexField.COURT_TYPE, CourtType.SUPREME);
        // assert
        assertEquals("+courtType:SUPREME", actual);
    }
    
    @Test
    public void transformToEqualsCriteria_MULTI_AND() {
        // execute
        String actual = criterionTransformer.transformToEqualsCriteria(JudgmentIndexField.KEYWORD, Lists.newArrayList("word1", "word2", "word3"), Operator.AND);
        // assert
        assertEquals("+keyword:word1 +keyword:word2 +keyword:word3", actual);
    }
    
    @Test
    public void transformToEqualsCriteria_MULTI_OR() {
        // execute
        String actual = criterionTransformer.transformToEqualsCriteria(JudgmentIndexField.JUDGMENT_TYPE, 
                Lists.newArrayList(JudgmentType.DECISION.name(), JudgmentType.SENTENCE.name()), Operator.OR);
        // assert
        assertEquals("+(judgmentType:DECISION judgmentType:SENTENCE)", actual);
    }
    
    @Test
    public void transformToEqualsCriteria_SINGLE_OR() {
        // execute
        String actual = criterionTransformer.transformToEqualsCriteria(JudgmentIndexField.JUDGMENT_TYPE, 
                Lists.newArrayList(JudgmentType.DECISION.name()), Operator.OR);
        // assert
        assertEquals("+judgmentType:DECISION", actual);
    }
    
    @Test
    public void transformToEqualsEnumCriteria_MULTI_ENUM() {
        // execute
        String actual = criterionTransformer.transformToEqualsEnumCriteria(JudgmentIndexField.JUDGMENT_TYPE, 
                Lists.newArrayList(JudgmentType.DECISION, JudgmentType.REASONS, JudgmentType.SENTENCE), Operator.OR);
        // assert
        assertEquals("+(judgmentType:DECISION judgmentType:REASONS judgmentType:SENTENCE)", actual);
    }
    
    @Test
    public void transformToDateRangeCriterion() {
        // execute
        String actual = criterionTransformer.transformToDateRangeCriterion(
                JudgmentIndexField.JUDGMENT_DATE, new LocalDate(2012, 9, 23), new LocalDate(2013, 2, 6));
        // assert
        assertEquals("+judgmentDate:[2012-09-23T00:00:00Z TO 2013-02-06T23:59:59Z]", actual);
    }
    
    @Test
    public void transformToRangeCriterion() {
        // execute
        String actual = criterionTransformer.transformToRangeCriterion(JudgmentIndexField.KEYWORD, "aa", "ba");
        // assert
        assertEquals("+keyword:[aa TO ba]", actual);
    }
    
}
