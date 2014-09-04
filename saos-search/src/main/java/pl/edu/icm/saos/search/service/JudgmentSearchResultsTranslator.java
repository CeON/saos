package pl.edu.icm.saos.search.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.stereotype.Component;

import pl.edu.icm.saos.search.config.model.JudgmentIndexField;
import pl.edu.icm.saos.search.model.JudgmentSearchResult;
import pl.edu.icm.saos.search.model.SearchResults;

@Component
public class JudgmentSearchResultsTranslator implements SearchResultsTranslator<JudgmentSearchResult> {

    @Override
    public SearchResults<JudgmentSearchResult> translate(QueryResponse response) {
        SolrDocumentList documents = response.getResults();
        SearchResults<JudgmentSearchResult> results = new SearchResults<JudgmentSearchResult>();
        
        
        for (int i=0; i<documents.size(); ++i) {
            SolrDocument document = documents.get(i);
            JudgmentSearchResult result = translateSingle(document);
            
            results.addResult(result);
        }
        
        results.setTotalResults(documents.getNumFound());
        
        return results;
    }
    
    protected JudgmentSearchResult translateSingle(SolrDocument document) {
        JudgmentSearchResult result = new JudgmentSearchResult();
        
        String databaseId = fetchValue(document, JudgmentIndexField.DATABASE_ID);
        String signature = fetchValue(document, JudgmentIndexField.SIGNATURE);
        result.setId(databaseId);
        result.setSignature(signature);
        
        String judgmentType = fetchValue(document, JudgmentIndexField.JUDGMENT_TYPE);
        result.setJudgmentType(judgmentType);
        
        Date judgmentDate = fetchDateValue(document, JudgmentIndexField.JUDGMENT_DATE);
        result.setJudgmentDate(judgmentDate);
        
        String court = fetchValue(document, JudgmentIndexField.COURT_NAME);
        String courtDivision = fetchValue(document, JudgmentIndexField.COURT_DIVISION_NAME);
        result.setCourtName(court);
        result.setCourtDivisionName(courtDivision);
        
        List<String> judges = fetchValues(document, JudgmentIndexField.JUDGE);
        result.setJudges(judges);
        
        List<String> keywords = fetchValues(document, JudgmentIndexField.KEYWORD);
        result.setKeywords(keywords);
        
        String content = fetchValue(document, JudgmentIndexField.CONTENT);
        result.setContent(content);
        
        return result;
    }
    
    private String fetchValue(SolrDocument doc, JudgmentIndexField field) {
        return (String)doc.getFirstValue(field.getFieldName());
    }
    
    private Date fetchDateValue(SolrDocument doc, JudgmentIndexField field) {
        return (Date)doc.getFirstValue(field.getFieldName());
    }
    
    private List<String> fetchValues(SolrDocument doc, JudgmentIndexField field) {
        Collection<Object> values = doc.getFieldValues(field.getFieldName());
        List<String> valuesList = new ArrayList<String>();
        for (Object val : values) {
            valuesList.add((String)val);
        }
        return valuesList;
    }

}
