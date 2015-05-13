package pl.edu.icm.saos.search.search.service;

import static org.junit.Assert.assertEquals;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import pl.edu.icm.saos.persistence.model.CommonCourt.CommonCourtType;
import pl.edu.icm.saos.persistence.model.CourtType;
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
                
                { "+judgeName:Nowak", JudgmentCriteriaBuilder.create().withJudgeName("Nowak").build() },
                { "+judgeName:Adam +judgeName:Nowak", JudgmentCriteriaBuilder.create().withJudgeName("Adam Nowak").build() },
                { "+judgeName:\"Adam Nowak\"", JudgmentCriteriaBuilder.create().withJudgeName("\"Adam Nowak\"").build() },
                { "+(judgeName:Nowak judgeName:Kowalski)", JudgmentCriteriaBuilder.create().withJudgeName("Nowak or Kowalski").build() },
                { "+keyword:word", JudgmentCriteriaBuilder.create().withKeyword("word").build() },
                { "+keyword:word1 +keyword:word2", JudgmentCriteriaBuilder.create().withKeyword("word1").withKeyword("word2").build() },
                { "+legalBases:someLegalBase", JudgmentCriteriaBuilder.create().withLegalBase("someLegalBase").build() },
                { "+legalBases:\"some legal base\"", JudgmentCriteriaBuilder.create().withLegalBase("\"some legal base\"").build() },
                { "+referencedRegulations:someReferencedRegulation", JudgmentCriteriaBuilder.create().withReferencedRegulation("someReferencedRegulation").build() },
                { "+referencedRegulations:\"some referenced regulation\"", JudgmentCriteriaBuilder.create().withReferencedRegulation("\"some referenced regulation\"").build() },
                { "+lawJournalEntryId:41", JudgmentCriteriaBuilder.create().withLawJournalEntryId(41).build() },
                { "+referencedCourtCasesIds:21", JudgmentCriteriaBuilder.create().withReferencedCourtCaseId(21L).build() },
                
                { "+judgmentDate:[2014-04-01T00:00:00Z TO *]", JudgmentCriteriaBuilder.create().withDateFrom(FIRST_DATE).build() },
                { "+judgmentDate:[* TO 2014-04-01T23:59:59Z]", JudgmentCriteriaBuilder.create().withDateTo(FIRST_DATE).build() },
                { "+judgmentDate:[2014-04-01T00:00:00Z TO 2014-06-01T23:59:59Z]", JudgmentCriteriaBuilder.create().withDateRange(FIRST_DATE, SECOND_DATE).build() },
                
                { "+caseNumber:ZXC1", JudgmentCriteriaBuilder.create().withCaseNumber("ZXC1").build() },
                { "+judgmentType:DECISION", JudgmentCriteriaBuilder.create().withJudgmentType(JudgmentType.DECISION).build() },
                { "+(judgmentType:DECISION judgmentType:SENTENCE)", JudgmentCriteriaBuilder.create()
                        .withJudgmentType(JudgmentType.DECISION).withJudgmentType(JudgmentType.SENTENCE).build() },
                { "+courtType:ADMINISTRATIVE", JudgmentCriteriaBuilder.create().withCourtType(CourtType.ADMINISTRATIVE).build() },
                
                { "+ccCourtType:DISTRICT", JudgmentCriteriaBuilder.create().withCcCourtType(CommonCourtType.DISTRICT).build() },
                { "+ccCourtId:11", JudgmentCriteriaBuilder.create().withCcCourtId(11).build() },
                { "+ccCourtCode:0050", JudgmentCriteriaBuilder.create().withCcCourtCode("0050").build() },
                { "+ccCourtName:someCourtName", JudgmentCriteriaBuilder.create().withCcCourtName("someCourtName").build() },
                
                { "+scCourtChamberId:12", JudgmentCriteriaBuilder.create().withScChamberId(12).build() },
                { "+scCourtChamberName:someChamberName", JudgmentCriteriaBuilder.create().withScChamberName("someChamberName").build() },
                { "+scCourtChamberDivisionId:13", JudgmentCriteriaBuilder.create().withScChamberDivisionId(13).build() },
                { "+scCourtChamberDivisionName:someChamberDivisionName", JudgmentCriteriaBuilder.create().withScChamberDivisionName("someChamberDivisionName").build() },
                { "+scPersonnelType:THREE_PERSON", JudgmentCriteriaBuilder.create().withScPersonnelType(PersonnelType.THREE_PERSON).build() },
                { "+scJudgmentFormId:56", JudgmentCriteriaBuilder.create().withScJudgmentFormId(56L).build() },
                { "+scJudgmentFormName:judgmentFormName", JudgmentCriteriaBuilder.create().withScJudgmentFormName("judgmentFormName").build() },
                
                { "+ccCourtDivisionId:14", JudgmentCriteriaBuilder.create().withCcDivisionId(14).build() },
                { "+ccCourtDivisionCode:0100", JudgmentCriteriaBuilder.create().withCcDivisionCode("0100").build() },
                { "+ccCourtDivisionName:someDivisionName", JudgmentCriteriaBuilder.create().withCcDivisionName("someDivisionName").build() },
                
                { "+ctDissentingOpinion:someOpinion", JudgmentCriteriaBuilder.create().withCtDissentingOpinion("someOpinion").build() },
                { "+ctDissentingOpinion:\"some opinion\"", JudgmentCriteriaBuilder.create().withCtDissentingOpinion("\"some opinion\"").build() },
                { "+ctDissentingOpinionAuthor:someAuthor", JudgmentCriteriaBuilder.create().withCtDissentingOpinionAuthor("someAuthor").build() },
                { "+(ctDissentingOpinionAuthor:author1 ctDissentingOpinionAuthor:author2)", JudgmentCriteriaBuilder.create().withCtDissentingOpinionAuthor("author1 or author2").build() },
                { "+(ccAppealCourtId:12 ccRegionalCourtId:12 ccDistrictCourtId:12)", JudgmentCriteriaBuilder.create().withCcDirectOrSuperiorCourtId(12L).build() },
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
