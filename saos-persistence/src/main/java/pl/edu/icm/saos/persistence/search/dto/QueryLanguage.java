package pl.edu.icm.saos.persistence.search.dto;

/**
 * Enumeration of query syntaxes supported by the search service.
 *
 * @author mpol@icm.edu.pl
 */
public enum QueryLanguage {
    /**
     * A simple list of search terms
     */
    SEARCH_TERMS,
    /**
     *Contextual Query Language, as defined by http://www.loc.gov/standards/sru/specs/cql.html
     */
    CQL
}
