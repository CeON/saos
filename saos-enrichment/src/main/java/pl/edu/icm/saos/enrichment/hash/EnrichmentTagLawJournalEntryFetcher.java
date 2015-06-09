package pl.edu.icm.saos.enrichment.hash;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTagTypes;
import pl.edu.icm.saos.persistence.model.LawJournalEntry;

import com.google.common.collect.Lists;

/**
 * Fetcher of law journal entries from enrichment tags
 * 
 * @author madryk
 */
@Service
public class EnrichmentTagLawJournalEntryFetcher {

    @Autowired
    private EntityManager entityManager;
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Returns law journal entries from enrichment tags that do not exist
     * in LawJournalEntry database table
     */
    public List<LawJournalEntry> fetchTagLawJournalEntriesWihoutCorrespondingEntryInDb() {
        String select = "SELECT"
                + "    (tagValue->'journalYear')\\:\\:text journalYear,"
                + "    (tagValue->'journalNo')\\:\\:text journalNo,"
                + "    (tagValue->'journalEntry')\\:\\:text journalEntry,"
                + "    max((tagValue->'journalTitle')\\:\\:text) title"
                + " FROM enrichment_tag tag"
                + " JOIN json_array_elements(tag.value) tagValue ON"
                + "     NOT EXISTS (SELECT 1 FROM law_journal_entry lje WHERE "
                + "         replace((tagValue->'journalYear')\\:\\:text, '\"', '')\\:\\:int = lje.year AND"
                + "         replace((tagValue->'journalNo')\\:\\:text, '\"', '')\\:\\:int = lje.journal_no AND"
                + "         replace((tagValue->'journalEntry')\\:\\:text, '\"', '')\\:\\:int = lje.entry)"
                + " WHERE tag.tag_type = '" + EnrichmentTagTypes.REFERENCED_REGULATIONS + "'"
                + " GROUP BY journalYear, journalNo, journalEntry;";
        
        Query query = entityManager.createNativeQuery(select);
        
        @SuppressWarnings("unchecked")
        List<Object[]> results = query.getResultList();
        
        return convertToLawJournalEntries(results);
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private List<LawJournalEntry> convertToLawJournalEntries(List<Object[]> results) {
        List<LawJournalEntry> lawJournalEntries = Lists.newLinkedList();
        
        for (Object[] result : results) {
            int journalYear = Integer.parseInt(stripQuotes(result[0]));
            int journalNo = Integer.parseInt(stripQuotes(result[1]));
            int journalEntry = Integer.parseInt(stripQuotes(result[2]));
            String journalTitle = stripQuotes(result[3]);
            
            LawJournalEntry lawJournalEntry = new LawJournalEntry(journalYear, journalNo, journalEntry, journalTitle);
            
            lawJournalEntries.add(lawJournalEntry);
        }
        
        return lawJournalEntries;
    }
    
    /**
     * Strips value from quotes at the beginning and at the end of string.
     * It is necessary for json text values returned by postgresql.
     */
    private String stripQuotes(Object value) {
        return StringUtils.strip((String)value, "\"");
    }
}
