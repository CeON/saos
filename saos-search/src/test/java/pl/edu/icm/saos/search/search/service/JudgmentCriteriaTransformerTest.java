package pl.edu.icm.saos.search.search.service;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import pl.edu.icm.saos.persistence.model.CommonCourt.CommonCourtType;
import pl.edu.icm.saos.persistence.model.Judgment.JudgmentType;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment.PersonnelType;
import pl.edu.icm.saos.search.config.model.JudgmentIndexField;
import pl.edu.icm.saos.search.search.model.CourtType;
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
                { "+content:word", new JudgmentCriteria("word") },
                { "*:*", new JudgmentCriteria(" ") },
                
                { "+judgeName:Adam\\ Nowak", new JudgmentCriteriaBuilder().withJudgeName("Adam Nowak").build() },
                { "+keyword:word", new JudgmentCriteriaBuilder().withKeyword("word").build() },
                { "+legalBases:someLegalBase", new JudgmentCriteriaBuilder().withLegalBase("someLegalBase").build() },
                { "+referencedRegulations:someReferencedRegulation", new JudgmentCriteriaBuilder().withReferencedRegulation("someReferencedRegulation").build() },
                
                { "+judgmentDate:[2014-04-01T00:00:00Z TO *]", new JudgmentCriteriaBuilder().withDateFrom(FIRST_DATE).build() },
                { "+judgmentDate:[* TO 2014-04-01T23:59:59Z]", new JudgmentCriteriaBuilder().withDateTo(FIRST_DATE).build() },
                { "+judgmentDate:[2014-04-01T00:00:00Z TO 2014-06-01T23:59:59Z]", new JudgmentCriteriaBuilder().withDateRange(FIRST_DATE, SECOND_DATE).build() },
                
                { "+caseNumber:ZXC1", new JudgmentCriteriaBuilder().withCaseNumber("ZXC1").build() },
                { "+judgmentType:DECISION", new JudgmentCriteriaBuilder().withJudgmentType(JudgmentType.DECISION).build() },
                { "+courtType:ADMINISTRATIVE", new JudgmentCriteriaBuilder().withCourtType(CourtType.ADMINISTRATIVE).build() },
                
                { "+commonCourtType:DISTRICT", new JudgmentCriteriaBuilder().withCommonCourtType(CommonCourtType.DISTRICT).build() },
                { "+courtId:11", new JudgmentCriteriaBuilder().withCourtId(11).build() },
                { "+courtCode:0050", new JudgmentCriteriaBuilder().withCourtCode("0050").build() },
                { "+courtName:someCourtName", new JudgmentCriteriaBuilder().withCourtName("someCourtName").build() },
                
                { "+courtChamberId:12", new JudgmentCriteriaBuilder().withChamberId(12).build() },
                { "+courtChamberName:someChamberName", new JudgmentCriteriaBuilder().withChamberName("someChamberName").build() },
                { "+courtChamberDivisionId:13", new JudgmentCriteriaBuilder().withChamberDivisionId(13).build() },
                { "+courtChamberDivisionName:someChamberDivisionName", new JudgmentCriteriaBuilder().withChamberDivisionName("someChamberDivisionName").build() },
                { "+personnelType:THREE_PERSON", new JudgmentCriteriaBuilder().withPersonnelType(PersonnelType.THREE_PERSON).build() },
                
                { "+courtDivisionId:14", new JudgmentCriteriaBuilder().withDivisionId(14).build() },
                { "+courtDivisionCode:0100", new JudgmentCriteriaBuilder().withDivisionCode("0100").build() },
                { "+courtDivisionName:someDivisionName", new JudgmentCriteriaBuilder().withDivisionName("someDivisionName").build() },
        };
    }
    
    
    //------------------------ LOGIC --------------------------
    
    @Test
    @UseDataProvider("criterionData")
    public void transformCriteria(String expectedQuery, JudgmentCriteria criteria) {
        String actualQuery = queryFactory.transformCriteria(criteria);
        
        Assert.assertEquals(expectedQuery, actualQuery);
    }
    
    @Test
    public void transformCriteria_TWO_CRITERIA() {
        JudgmentCriteria criteria = new JudgmentCriteria()
            .setCourtName("word1")
            .setJudgeName("word2");

        String solrQuery = queryFactory.transformCriteria(criteria);

        Assert.assertEquals("+courtName:word1 +judgeName:word2", solrQuery);
    }
    

}
