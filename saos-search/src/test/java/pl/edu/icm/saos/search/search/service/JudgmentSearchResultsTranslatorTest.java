package pl.edu.icm.saos.search.search.service;

import java.util.GregorianCalendar;

import junit.framework.Assert;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.util.NamedList;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import pl.edu.icm.saos.persistence.model.Judge.JudgeRole;
import pl.edu.icm.saos.search.config.model.JudgmentIndexField;
import pl.edu.icm.saos.search.search.model.JudgeResult;
import pl.edu.icm.saos.search.search.model.JudgmentSearchResult;
import pl.edu.icm.saos.search.search.model.SearchResults;

/**
 * @author madryk
 */
public class JudgmentSearchResultsTranslatorTest {

    private JudgmentSearchResultsTranslator resultsTranslator = new JudgmentSearchResultsTranslator();
    
    private SolrFieldFetcher<JudgmentIndexField> fieldFetcher = new SolrFieldFetcher<JudgmentIndexField>();
    
    @Before
    public void setUp() {
        resultsTranslator.setFieldFetcher(fieldFetcher);
    }
    
    @Test
    public void translate() {
        SolrDocument firstDocument = fetchBasicDocument("FIRST_ID", "1", "some content");
        SolrDocument secondDocument = fetchBasicDocument("SECOND_ID", "2", "some other content");
        SolrDocument thirdDocument = fetchBasicDocument("THIRD_ID", "4", "other content");
        
        QueryResponse response = createBasicSolrResponse(205, firstDocument, secondDocument, thirdDocument);
        
        
        SearchResults<JudgmentSearchResult> results = resultsTranslator.translate(response);
        
        
        Assert.assertEquals(3, results.getResults().size());
        Assert.assertEquals(205, results.getTotalResults());
        
        JudgmentSearchResult firstResult = results.getResults().get(0);
        Assert.assertEquals("some content", firstResult.getContent());
        Assert.assertEquals("1", firstResult.getId());
        
        JudgmentSearchResult secondResult = results.getResults().get(1);
        Assert.assertEquals("2", secondResult.getId());
        Assert.assertEquals("some other content", secondResult.getContent());
        
        JudgmentSearchResult thirdResult = results.getResults().get(2);
        Assert.assertEquals("4", thirdResult.getId());
        Assert.assertEquals("other content", thirdResult.getContent());
        
    }
    
    @Test
    public void translateSingle() {
        SolrDocument doc = fetchBasicDocument("ID", "1", "some content");
        
        doc.addField("caseNumber", "AAAB1A");
        
        GregorianCalendar calendar = new GregorianCalendar(2014, 9, 7);
        doc.addField("judgmentDate", calendar.getTime());
        doc.addField("judgmentType", "SENTENCE");
        doc.addField("legalBases", "art. 1234 kc");
        doc.addField("referencedRegulations", "Ustawa 1");
        doc.addField("referencedRegulations", "Ustawa 2");
        
        doc.addField("courtType", "APPEAL");
        doc.addField("courtId", "123");
        doc.addField("courtCode", "15200000");
        doc.addField("courtName", "Sąd Apelacyjny w Krakowie");
        doc.addField("courtDivisionId", "816");
        doc.addField("courtDivisionCode", "0000503");
        doc.addField("courtDivisionName", "I Wydział Cywilny");
        
        doc.addField("keyword", "some keyword");
        doc.addField("keyword", "some other keyword");
        
        doc.addField("judge", "Jan Kowalski|PRESIDING_JUDGE|REPORTING_JUDGE");
        doc.addField("judge", "Jacek Zieliński|REPORTING_JUDGE");
        doc.addField("judge", "Adam Nowak");
        
        
        JudgmentSearchResult result = resultsTranslator.translateSingle(doc);
        
        
        Assert.assertEquals("1", result.getId());
        Assert.assertEquals(1, result.getCaseNumbers().size());
        Assert.assertTrue(result.getCaseNumbers().contains("AAAB1A"));
        
        Assert.assertEquals(new LocalDate(2014, 10, 7), result.getJudgmentDate());
        Assert.assertEquals("SENTENCE", result.getJudgmentType());
        
        Assert.assertEquals(Integer.valueOf(123), result.getCourtId());
        Assert.assertEquals("15200000", result.getCourtCode());
        Assert.assertEquals("Sąd Apelacyjny w Krakowie", result.getCourtName());

        Assert.assertEquals(Integer.valueOf(816), result.getCourtDivisionId());
        Assert.assertEquals("0000503", result.getCourtDivisionCode());
        Assert.assertEquals("I Wydział Cywilny", result.getCourtDivisionName());
        
        Assert.assertEquals(2, result.getKeywords().size());
        Assert.assertTrue(result.getKeywords().contains("some keyword"));
        Assert.assertTrue(result.getKeywords().contains("some other keyword"));
        
        Assert.assertEquals(3, result.getJudges().size());
        Assert.assertTrue(result.getJudges().contains(new JudgeResult("Jan Kowalski", JudgeRole.PRESIDING_JUDGE, JudgeRole.REPORTING_JUDGE)));
        Assert.assertTrue(result.getJudges().contains(new JudgeResult("Jacek Zieliński", JudgeRole.REPORTING_JUDGE)));
        Assert.assertTrue(result.getJudges().contains(new JudgeResult("Adam Nowak")));
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private SolrDocument fetchBasicDocument(String id, String databaseId, String content) {
        SolrDocument doc = new SolrDocument();
        doc.addField("id", id);
        doc.addField("databaseId", databaseId);
        
        doc.addField("content", content);
        
        return doc;
    }
    
    private QueryResponse createBasicSolrResponse(long totalResults, SolrDocument ... documents) {
        QueryResponse response = new QueryResponse();
        
        SolrDocumentList documentList = new SolrDocumentList();
        documentList.setNumFound(totalResults);
        for (SolrDocument doc : documents) {
            documentList.add(doc);
        }
        
        NamedList<Object> namedList = new NamedList<Object>();
        namedList.add("response", documentList);
        response.setResponse(namedList);
        
        return response;
    }
}
