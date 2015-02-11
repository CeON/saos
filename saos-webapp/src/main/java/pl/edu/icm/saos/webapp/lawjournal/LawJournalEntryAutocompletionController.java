package pl.edu.icm.saos.webapp.lawjournal;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.edu.icm.saos.persistence.model.LawJournalEntry;
import pl.edu.icm.saos.persistence.search.DatabaseSearchService;
import pl.edu.icm.saos.persistence.search.dto.LawJournalEntrySearchFilter;
import pl.edu.icm.saos.persistence.search.result.SearchResult;

/**
 * @author madryk
 */
@Controller
public class LawJournalEntryAutocompletionController {

    private final static String DEFAULT_MAX_RESULTS_COUNT = "10";
    
    @Autowired
    private DatabaseSearchService databaseSearchService;
    
    @Autowired
    private SimpleLawJournalEntryConverter simpleLawJournalEntryConverter;
    
    
    //------------------------ LOGIC --------------------------
    
    @RequestMapping(value = "/lawjournal/search", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public ResponseEntity<List<SimpleLawJournalEntry>> lawJournalSearch(
            @RequestParam(value = "year", required = false) Integer year,
            @RequestParam(value = "journalNo", required = false) Integer journalNo,
            @RequestParam(value = "entry", required = false) Integer entry,
            @RequestParam(value = "text", required = false) String text,
            @RequestParam(value = "maxResults", defaultValue = DEFAULT_MAX_RESULTS_COUNT) int maxResults) {
        
        LawJournalEntrySearchFilter searchFilter = LawJournalEntrySearchFilter.builder()
                .year(year)
                .journalNo(journalNo)
                .entry(entry)
                .text(text)
                .limit(maxResults)
                .filter();
        
        SearchResult<LawJournalEntry> results = databaseSearchService.search(searchFilter);
        
        List<SimpleLawJournalEntry> simpleLawJournalEntries = simpleLawJournalEntryConverter.convertLawJournalEntries(results.getResultRecords());
        
        
        return new ResponseEntity<>(simpleLawJournalEntries, new HttpHeaders(), HttpStatus.OK);
    }
    
}
