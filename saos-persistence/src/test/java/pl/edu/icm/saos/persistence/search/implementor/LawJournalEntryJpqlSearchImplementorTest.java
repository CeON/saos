package pl.edu.icm.saos.persistence.search.implementor;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;

import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.PersistenceTestSupport;
import pl.edu.icm.saos.persistence.model.LawJournalEntry;
import pl.edu.icm.saos.persistence.repository.LawJournalEntryRepository;
import pl.edu.icm.saos.persistence.search.DatabaseSearchService;
import pl.edu.icm.saos.persistence.search.dto.LawJournalEntrySearchFilter;
import pl.edu.icm.saos.persistence.search.result.SearchResult;

import com.google.common.collect.Lists;

/**
 * @author madryk
 */
@Category(SlowTest.class)
public class LawJournalEntryJpqlSearchImplementorTest extends PersistenceTestSupport {

    @Autowired
    private DatabaseSearchService databaseSearchService;

    @Autowired
    private LawJournalEntryJpqlSearchImplementor lawJournalSearchImplementor;

    @Autowired
    private LawJournalEntryRepository lawJournalEntryRepository;


    private LawJournalEntry firstLawJournalEntry;
    private LawJournalEntry secondLawJournalEntry;
    private LawJournalEntry thirdLawJournalEntry;

    @Before
    public void setUp() throws Exception {

        firstLawJournalEntry = new LawJournalEntry(1964, 43, 296, "Ustawa z dnia 17 listopada 1964 r. - Kodeks postępowania cywilnego");
        secondLawJournalEntry = new LawJournalEntry(1964, 60, 9, "Ustawa z dnia 25 lutego 1964 r. Przepisy wprowadzające kodeks rodzinny i opiekuńczy");
        thirdLawJournalEntry = new LawJournalEntry(1973, 160, 29, "Dekret z dnia 14 lipca 1973 r. o dodatkowych dniach wolnych od pracy");

        lawJournalEntryRepository.save(Lists.newArrayList(firstLawJournalEntry, secondLawJournalEntry, thirdLawJournalEntry));

    }
    
    
    //------------------------ TESTS --------------------------

    @Test
    public void search_NO_FILTERS() {
        // given
        LawJournalEntrySearchFilter searchFilter = LawJournalEntrySearchFilter.builder().filter();

        // execute
        SearchResult<LawJournalEntry> searchResult = databaseSearchService.search(searchFilter);

        // assert
        assertThat(searchResult.getResultRecords(), containsInAnyOrder(firstLawJournalEntry, secondLawJournalEntry, thirdLawJournalEntry));
    }

    @Test
    public void search_BY_YEAR() {
        // given
        LawJournalEntrySearchFilter searchFilter = LawJournalEntrySearchFilter.builder()
                .year(1964).filter();

        // execute
        SearchResult<LawJournalEntry> searchResult = databaseSearchService.search(searchFilter);

        // assert
        assertThat(searchResult.getResultRecords(), containsInAnyOrder(firstLawJournalEntry, secondLawJournalEntry));
    }

    @Test
    public void search_BY_JOURNAL_NO() {
        // given
        LawJournalEntrySearchFilter searchFilter = LawJournalEntrySearchFilter.builder()
                .journalNo(60).filter();

        // execute
        SearchResult<LawJournalEntry> searchResult = databaseSearchService.search(searchFilter);

        // assert
        assertThat(searchResult.getResultRecords(), containsInAnyOrder(secondLawJournalEntry));
    }

    @Test
    public void search_BY_ENTRY() {
        // given
        LawJournalEntrySearchFilter searchFilter = LawJournalEntrySearchFilter.builder()
                .entry(9).filter();

        // execute
        SearchResult<LawJournalEntry> searchResult = databaseSearchService.search(searchFilter);

        // assert
        assertThat(searchResult.getResultRecords(), containsInAnyOrder(secondLawJournalEntry));
    }

    @Test
    public void search_BY_TEXT() {
        // given
        LawJournalEntrySearchFilter searchFilter = LawJournalEntrySearchFilter.builder()
                .text("Dekret").filter();

        // execute
        SearchResult<LawJournalEntry> searchResult = databaseSearchService.search(searchFilter);

        // assert
        assertThat(searchResult.getResultRecords(), containsInAnyOrder(thirdLawJournalEntry));
    }

    @Test
    public void search_BY_TEXT_BEGINNING_OF_WORD() {
        // given
        LawJournalEntrySearchFilter searchFilter = LawJournalEntrySearchFilter.builder()
                .text("postępow").filter();

        // execute
        SearchResult<LawJournalEntry> searchResult = databaseSearchService.search(searchFilter);

        // assert
        assertThat(searchResult.getResultRecords(), containsInAnyOrder(firstLawJournalEntry));
    }

    @Test
    public void search_BY_TEXT_NOT_BEGINNING_OF_WORD() {
        // given
        LawJournalEntrySearchFilter searchFilter = LawJournalEntrySearchFilter.builder()
                .text("odeks").filter();

        // execute
        SearchResult<LawJournalEntry> searchResult = databaseSearchService.search(searchFilter);

        // assert
        assertThat(searchResult.getResultRecords(), is(empty()));
    }

    @Test
    public void search_BY_TEXT_CASE_INSENSITIVE() {
        // given
        LawJournalEntrySearchFilter searchFilter = LawJournalEntrySearchFilter.builder()
                .text("dekreT")
                .filter();

        // execute
        SearchResult<LawJournalEntry> searchResult = databaseSearchService.search(searchFilter);

        // assert
        assertThat(searchResult.getResultRecords(), containsInAnyOrder(thirdLawJournalEntry));
    }

    @Test
    public void search_BY_ALL_FILTERS() {
        // given
        LawJournalEntrySearchFilter searchFilter = LawJournalEntrySearchFilter.builder()
                .year(1964)
                .journalNo(43)
                .entry(296)
                .text("Kodeks postępowania")
                .filter();

        // execute
        SearchResult<LawJournalEntry> searchResult = databaseSearchService.search(searchFilter);

        // assert
        assertThat(searchResult.getResultRecords(), containsInAnyOrder(firstLawJournalEntry));
    }

    @Test
    public void search_WITH_OFFSET() {
        // given
        List<Long> sortedIds = Lists.newArrayList(firstLawJournalEntry.getId(), secondLawJournalEntry.getId(), thirdLawJournalEntry.getId());
        Collections.sort(sortedIds);

        LawJournalEntrySearchFilter searchFilter = LawJournalEntrySearchFilter.builder()
                .offset(1).filter();

        // execute
        SearchResult<LawJournalEntry> searchResult = databaseSearchService.search(searchFilter);

        // assert
        List<Long> searchResultIds = searchResult.getResultRecords().stream().map(x -> x.getId()).collect(Collectors.toList());
        assertThat(searchResultIds, contains(sortedIds.get(1), sortedIds.get(2)));
    }

    @Test
    public void search_WITH_LIMIT() {
        // given
        List<Long> sortedIds = Lists.newArrayList(firstLawJournalEntry.getId(), secondLawJournalEntry.getId(), thirdLawJournalEntry.getId());
        Collections.sort(sortedIds);

        LawJournalEntrySearchFilter searchFilter = LawJournalEntrySearchFilter.builder()
                .limit(2).filter();

        // execute
        SearchResult<LawJournalEntry> searchResult = databaseSearchService.search(searchFilter);

        // assert
        List<Long> searchResultIds = searchResult.getResultRecords().stream().map(x -> x.getId()).collect(Collectors.toList());
        assertThat(searchResultIds, contains(sortedIds.get(0), sortedIds.get(1)));
    }
}
