package pl.edu.icm.saos.persistence.search.implementor;

import static com.google.common.collect.Maps.newHashMap;

import java.util.Map;

import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.LawJournalEntry;
import pl.edu.icm.saos.persistence.search.dto.LawJournalEntrySearchFilter;

/**
 * @author madryk
 */
@Service
public class LawJournalEntryJpqlSearchImplementor extends AbstractJpqlSearchImplementor<LawJournalEntrySearchFilter, LawJournalEntry> {

    
    //------------------------ LOGIC --------------------------
    
    @Override
    protected String createQuery(LawJournalEntrySearchFilter searchFilter) {
        StringBuilder jpql = new StringBuilder(" select journalEntry from " + LawJournalEntry.class.getName() + " journalEntry ");
        return appendConditions(searchFilter, jpql);
    }

    @Override
    protected Map<String, Object> createParametersMap(LawJournalEntrySearchFilter searchFilter) {
        Map<String, Object> map = newHashMap();
        
        if(searchFilter.getYear() != null) {
            map.put("year", searchFilter.getYear());
        }

        if(searchFilter.getJournalNo() != null) {
            map.put("journalNo", searchFilter.getJournalNo());
        }

        if(searchFilter.getEntry() != null) {
            map.put("entry", searchFilter.getEntry());
        }
        
        if(searchFilter.getText() != null) {
            map.put("beginningText", searchFilter.getText() + '%');
            map.put("middleText", "% " + searchFilter.getText() + '%');
        }

        return map;
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private String appendConditions(LawJournalEntrySearchFilter searchFilter, StringBuilder jpql) {
        jpql.append(" where 1=1 ");

        if (searchFilter.getYear() != null) {
            jpql.append(" and year = :year");
        }

        if (searchFilter.getJournalNo() != null) {
            jpql.append(" and journalNo = :journalNo");
        }

        if (searchFilter.getEntry() != null) {
            jpql.append(" and entry = :entry");
        }
        
        if (searchFilter.getText() != null) {
            jpql.append(" and (lower(title) LIKE lower(:beginningText) OR lower(title) LIKE lower(:middleText))");
        }

        return jpql.toString();
    }

    
    
}
