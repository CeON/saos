package pl.edu.icm.saos.search.search.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.solr.common.SolrDocument;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.Judge.JudgeRole;
import pl.edu.icm.saos.search.config.model.JudgmentIndexField;
import pl.edu.icm.saos.search.search.model.JudgeResult;
import pl.edu.icm.saos.search.search.model.JudgmentSearchResult;
import pl.edu.icm.saos.search.search.model.SupremeCourtChamberResult;

import com.google.common.collect.Lists;

/**
 * Translates document provided by Solr into {@link JudgmentSearchResult}
 * @author madryk
 */
@Service
public class JudgmentSearchResultTranslator implements SearchResultTranslator<JudgmentSearchResult> {

    private SolrFieldFetcher<JudgmentIndexField> fieldFetcher;
    
    private SolrHighlightFragmentsMerger<JudgmentIndexField> highlightFragmentsMerger;
    
    
    //------------------------ LOGIC --------------------------
    
    @Override
    public JudgmentSearchResult translateSingle(SolrDocument document) {
        JudgmentSearchResult result = new JudgmentSearchResult();
        
        Integer databaseId = fieldFetcher.fetchIntValue(document, JudgmentIndexField.DATABASE_ID);
        result.setId(databaseId);
        List<String> caseNumbers = fieldFetcher.fetchValues(document, JudgmentIndexField.CASE_NUMBER);
        result.setCaseNumbers(caseNumbers);
        
        String judgmentType = fieldFetcher.fetchValue(document, JudgmentIndexField.JUDGMENT_TYPE);
        result.setJudgmentType(judgmentType);
        
        LocalDate judgmentDate = fieldFetcher.fetchDateValue(document, JudgmentIndexField.JUDGMENT_DATE);
        result.setJudgmentDate(judgmentDate);
        
        translateCommonCourt(document, result);
        translateSupremeCourt(document, result);
                
        List<JudgeResult> judges = Lists.newArrayList();
        List<Pair<String, List<JudgeRole>>> judgesWithRoleAttributes = 
                fieldFetcher.fetchValuesWithEnumedAttributes(document, JudgmentIndexField.JUDGE, JudgeRole.class);
        judgesWithRoleAttributes.forEach(p -> judges.add(new JudgeResult(p.getLeft(), p.getRight().toArray(new JudgeRole[0]))));
        result.setJudges(judges);
        
        List<String> keywords = fieldFetcher.fetchValues(document, JudgmentIndexField.KEYWORD);
        result.setKeywords(keywords);
        
        String content = fieldFetcher.fetchValue(document, JudgmentIndexField.CONTENT);
        result.setContent(content);
        
        return result;
    }
    
    @Override
    public void applyHighlighting(Map<String, List<String>> documentHighlighting, JudgmentSearchResult result) {
        
        String highlightedContent = highlightFragmentsMerger.merge(documentHighlighting, JudgmentIndexField.CONTENT);
        result.setContent(highlightedContent);
    }

    
    //------------------------ PRIVATE --------------------------
    
    private void translateCommonCourt(SolrDocument document, JudgmentSearchResult result) {
        Integer courtId = fieldFetcher.fetchIntValue(document, JudgmentIndexField.CC_COURT_ID);
        String courtCode = fieldFetcher.fetchValue(document, JudgmentIndexField.CC_COURT_CODE);
        String court = fieldFetcher.fetchValue(document, JudgmentIndexField.CC_COURT_NAME);
        
        result.setCourtId(courtId);
        result.setCourtCode(courtCode);
        result.setCourtName(court);

        Integer courtDivisionId = fieldFetcher.fetchIntValue(document, JudgmentIndexField.CC_COURT_DIVISION_ID);
        String courtDivisionCode = fieldFetcher.fetchValue(document, JudgmentIndexField.CC_COURT_DIVISION_CODE);
        String courtDivision = fieldFetcher.fetchValue(document, JudgmentIndexField.CC_COURT_DIVISION_NAME);
        
        result.setCourtDivisionId(courtDivisionId);
        result.setCourtDivisionCode(courtDivisionCode);
        result.setCourtDivisionName(courtDivision);
    }
    
    private void translateSupremeCourt(SolrDocument document, JudgmentSearchResult result) { 
        String personnelType = fieldFetcher.fetchValue(document, JudgmentIndexField.SC_PERSONNEL_TYPE);
        result.setPersonnelType(personnelType);
        
        List<Pair<String, List<String>>> chambers = fieldFetcher.fetchValuesWithAttributes(document, JudgmentIndexField.SC_COURT_CHAMBER);
        List<SupremeCourtChamberResult> chambersResult = Lists.newLinkedList();
        
        chambers.forEach(x -> chambersResult.add(new SupremeCourtChamberResult(Integer.valueOf(x.getLeft()), x.getRight().get(0))));
        result.setCourtChambers(chambersResult);
        
        Integer divisionId = fieldFetcher.fetchIntValue(document, JudgmentIndexField.SC_COURT_DIVISION_ID);
        String divisionName = fieldFetcher.fetchValue(document, JudgmentIndexField.SC_COURT_DIVISION_NAME);
        
        result.setCourtChamberDivisionId(divisionId);
        result.setCourtChamberDivisionName(divisionName);
    }
    
    
    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setFieldFetcher(SolrFieldFetcher<JudgmentIndexField> fieldFetcher) {
        this.fieldFetcher = fieldFetcher;
    }

    @Autowired
    public void setHighlightFragmentsMerger(
            SolrHighlightFragmentsMerger<JudgmentIndexField> highlightFragmentsMerger) {
        this.highlightFragmentsMerger = highlightFragmentsMerger;
    }

}
