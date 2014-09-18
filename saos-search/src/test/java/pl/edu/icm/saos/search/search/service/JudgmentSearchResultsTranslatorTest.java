package pl.edu.icm.saos.search.search.service;

import java.util.GregorianCalendar;

import junit.framework.Assert;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.util.NamedList;
import org.junit.Test;

import pl.edu.icm.saos.search.search.model.JudgmentSearchResult;
import pl.edu.icm.saos.search.search.model.SearchResults;

/**
 * @author madryk
 */
public class JudgmentSearchResultsTranslatorTest {

    private JudgmentSearchResultsTranslator resultsTranslator = new JudgmentSearchResultsTranslator();
    
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
        doc.addField("courtId", "15200000");
        doc.addField("courtName", "Sąd Apelacyjny w Krakowie");
        doc.addField("courtDivisionId", "0000503");
        doc.addField("courtDivisionName", "I Wydział Cywilny");
        
        doc.addField("judge", "Jan Kowalski");
        doc.addField("judge", "Adam Nowak");
        doc.addField("judgeWithRole_#_PRESIDING_JUDGE", "Jan Kowalski");
        
        
        JudgmentSearchResult result = resultsTranslator.translateSingle(doc);
        
        
        Assert.assertEquals("1", result.getId());
        Assert.assertEquals(1, result.getCaseNumbers().size());
        Assert.assertTrue(result.getCaseNumbers().contains("AAAB1A"));
        
        Assert.assertEquals(calendar.getTime(), result.getJudgmentDate());
        Assert.assertEquals("SENTENCE", result.getJudgmentType());
        
        Assert.assertEquals("Sąd Apelacyjny w Krakowie", result.getCourtName());
        Assert.assertEquals("I Wydział Cywilny", result.getCourtDivisionName());
        
        Assert.assertEquals(2, result.getJudges().size());
        Assert.assertTrue(result.getJudges().contains("Jan Kowalski"));
        Assert.assertTrue(result.getJudges().contains("Adam Nowak"));
    }
    
    
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
