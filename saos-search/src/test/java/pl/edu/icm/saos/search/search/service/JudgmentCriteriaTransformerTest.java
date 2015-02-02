package pl.edu.icm.saos.search.search.service;

import static org.junit.Assert.assertEquals;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import pl.edu.icm.saos.persistence.model.CourtType;
import pl.edu.icm.saos.persistence.model.CommonCourt.CommonCourtType;
import pl.edu.icm.saos.persistence.model.Judgment.JudgmentType;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment.PersonnelType;
import pl.edu.icm.saos.search.config.model.JudgmentIndexField;
import pl.edu.icm.saos.search.search.model.JudgmentCriteria;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

/**
 * @author madryk
 */
@RunWith(DataProviderRunner.class)
public class JudgmentCriteriaTransformerTest {

    private final static LocalDate FIRST_DATE = new LocalDate(2014, 4, 1);
    private final static LocalDate SECOND_DATE = new LocalDate(2014, 6, 1);
    
    private JudgmentCriteriaTransformer queryFactory = new JudgmentCriteriaTransformer();
    
    private SolrCriterionTransformer<JudgmentIndexField> criterionTransformer = new SolrCriterionTransformer<JudgmentIndexField>();
    
    @Before
    public void setUp() {
        queryFactory.setCriterionTransformer(criterionTransformer);
    }
    
    @DataProvider
    public static Object[][] criterionData() {
        return new Object[][] {
                { "+all:word", new JudgmentCriteria("word") },
                { "+(all:word1 all:word2)", new JudgmentCriteria("word1 or word2") },
                { "*:*", new JudgmentCriteria(" ") },
                
                { "+judgeName:Nowak", new JudgmentCriteriaBuilder().withJudgeName("Nowak").build() },
                { "+judgeName:Adam +judgeName:Nowak", new JudgmentCriteriaBuilder().withJudgeName("Adam Nowak").build() },
                { "+judgeName:\"Adam Nowak\"", new JudgmentCriteriaBuilder().withJudgeName("\"Adam Nowak\"").build() },
                { "+(judgeName:Nowak judgeName:Kowalski)", new JudgmentCriteriaBuilder().withJudgeName("Nowak or Kowalski").build() },
                { "+keyword:word", new JudgmentCriteriaBuilder().withKeyword("word").build() },
                { "+keyword:word1 +keyword:word2", new JudgmentCriteriaBuilder().withKeyword("word1").withKeyword("word2").build() },
                { "+legalBases:someLegalBase", new JudgmentCriteriaBuilder().withLegalBase("someLegalBase").build() },
                { "+legalBases:\"some legal base\"", new JudgmentCriteriaBuilder().withLegalBase("\"some legal base\"").build() },
                { "+referencedRegulations:someReferencedRegulation", new JudgmentCriteriaBuilder().withReferencedRegulation("someReferencedRegulation").build() },
                { "+referencedRegulations:\"some referenced regulation\"", new JudgmentCriteriaBuilder().withReferencedRegulation("\"some referenced regulation\"").build() },
                
                { "+judgmentDate:[2014-04-01T00:00:00Z TO *]", new JudgmentCriteriaBuilder().withDateFrom(FIRST_DATE).build() },
                { "+judgmentDate:[* TO 2014-04-01T23:59:59Z]", new JudgmentCriteriaBuilder().withDateTo(FIRST_DATE).build() },
                { "+judgmentDate:[2014-04-01T00:00:00Z TO 2014-06-01T23:59:59Z]", new JudgmentCriteriaBuilder().withDateRange(FIRST_DATE, SECOND_DATE).build() },
                
                { "+caseNumber:ZXC1", new JudgmentCriteriaBuilder().withCaseNumber("ZXC1").build() },
                { "+judgmentType:DECISION", new JudgmentCriteriaBuilder().withJudgmentType(JudgmentType.DECISION).build() },
                { "+(judgmentType:DECISION judgmentType:SENTENCE)", new JudgmentCriteriaBuilder()
                        .withJudgmentType(JudgmentType.DECISION).withJudgmentType(JudgmentType.SENTENCE).build() },
                { "+courtType:ADMINISTRATIVE", new JudgmentCriteriaBuilder().withCourtType(CourtType.ADMINISTRATIVE).build() },
                
                { "+ccCourtType:DISTRICT", new JudgmentCriteriaBuilder().withCcCourtType(CommonCourtType.DISTRICT).build() },
                { "+ccCourtId:11", new JudgmentCriteriaBuilder().withCcCourtId(11).build() },
                { "+ccCourtCode:0050", new JudgmentCriteriaBuilder().withCcCourtCode("0050").build() },
                { "+ccCourtName:someCourtName", new JudgmentCriteriaBuilder().withCcCourtName("someCourtName").build() },
                
                { "+scCourtChamberId:12", new JudgmentCriteriaBuilder().withScChamberId(12).build() },
                { "+scCourtChamberName:someChamberName", new JudgmentCriteriaBuilder().withScChamberName("someChamberName").build() },
                { "+scCourtChamberDivisionId:13", new JudgmentCriteriaBuilder().withScChamberDivisionId(13).build() },
                { "+scCourtChamberDivisionName:someChamberDivisionName", new JudgmentCriteriaBuilder().withScChamberDivisionName("someChamberDivisionName").build() },
                { "+scPersonnelType:THREE_PERSON", new JudgmentCriteriaBuilder().withScPersonnelType(PersonnelType.THREE_PERSON).build() },
                
                { "+ccCourtDivisionId:14", new JudgmentCriteriaBuilder().withCcDivisionId(14).build() },
                { "+ccCourtDivisionCode:0100", new JudgmentCriteriaBuilder().withCcDivisionCode("0100").build() },
                { "+ccCourtDivisionName:someDivisionName", new JudgmentCriteriaBuilder().withCcDivisionName("someDivisionName").build() },
                
                { "+ctDissentingOpinion:someOpinion", new JudgmentCriteriaBuilder().withCtDissentingOpinion("someOpinion").build() },
                { "+ctDissentingOpinion:\"some opinion\"", new JudgmentCriteriaBuilder().withCtDissentingOpinion("\"some opinion\"").build() },
                { "+ctDissentingOpinionAuthor:someAuthor", new JudgmentCriteriaBuilder().withCtDissentingOpinionAuthor("someAuthor").build() },
                { "+(ctDissentingOpinionAuthor:author1 ctDissentingOpinionAuthor:author2)", new JudgmentCriteriaBuilder().withCtDissentingOpinionAuthor("author1 or author2").build() },
        };
    }
    
    
    //------------------------ LOGIC --------------------------
    
    @Test
    @UseDataProvider("criterionData")
    public void transformCriteria(String expectedQuery, JudgmentCriteria criteria) {
        String actualQuery = queryFactory.transformCriteria(criteria);
        
        assertEquals(expectedQuery, actualQuery);
    }
    
    @Test
    public void transformCriteria_TWO_CRITERIA() {
        JudgmentCriteria criteria = new JudgmentCriteria();
        criteria.setCcCourtName("word1");
        criteria.setJudgeName("word2");

        String solrQuery = queryFactory.transformCriteria(criteria);

        assertEquals("+ccCourtName:word1 +judgeName:word2", solrQuery);
    }
    

}
