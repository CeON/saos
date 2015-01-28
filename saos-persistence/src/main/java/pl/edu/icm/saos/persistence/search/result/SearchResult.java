package pl.edu.icm.saos.persistence.search.result;

import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * A class for storing search result.
 * @author lukdumi
 *
 */
public class SearchResult<T> implements Serializable {

    //private Logger log = LoggerFactory.getLogger(SearchResult.class);
    private static final long serialVersionUID = 1L;


    private List<T> resultRecords = Lists.newArrayList();
    
    private int limitOfRecords = 0; 
    private int firstRecordPosition = 0;
    private Long allRecordsCount = null;
    private boolean moreRecordsExist = false;
    
    
    /** 
     * @deprecated use {@link #SearchResult(java.util.List, Long, int, int)}
     * */
    @Deprecated 
    public SearchResult(List<T> resultRecords) {
        this.resultRecords = resultRecords;
    }
    
    public SearchResult(List<T> resultRecords, Long allRecordsCount, int first, int limit) {
        this.resultRecords = resultRecords;
        setAllRecordsCount(allRecordsCount);
        setFirstRecordPosition(first);
        setLimitOfRecords(limit);
        if (resultRecords.size() > limit) {  // one can get one record more to see if there are more records
            setMoreRecordsExist(true);
            resultRecords.remove(resultRecords.size()-1);
        }
        if (allRecordsCount != null && allRecordsCount > resultRecords.size()) {
            setMoreRecordsExist(true);
        }
    }

    //******************** GETTERS ********************
    
    /**
     * Number of records on the current page
     * @return
     */
    public int getPageRecordsCount() {
        return resultRecords.size();
    }
    
    /**
     * Position of the first record (counting from 1) among all records that matched the query
     * @return
     */
    public int getFirstRecordPosition() {
        return firstRecordPosition;
    }
    
    /**
     * Number of all records. Can be null.
     * @return
     */
    public Long getAllRecordsCount() {
        return allRecordsCount;
    }
    
    
    public List<T> getResultRecords() {
        return resultRecords;
    }

    /**
     * Limit of records set in request
     */
    public int getLimitOfRecords() {
        return limitOfRecords;
    }

    
    /**
     * True if more records than {{@link #getLimitOfRecords()} exist in the data source
     */
    public boolean isMoreRecordsExist() {
        return moreRecordsExist;
    }

    
    
    
    //******************** HELPER ********************
    
    public void addResultRecord(T resultRecord) {
        this.resultRecords.add(resultRecord);
    }

    public void removeResultRecord(T resultRecord) {
        this.resultRecords.remove(resultRecord);
    }
    
    public void addResultRecords(List<T> resultRecords) {
        this.resultRecords.addAll(resultRecords);
    }
    
    
    //******************** SETTERS ********************


    public void setFirstRecordPosition(int firstRecordPosition) {
        this.firstRecordPosition = firstRecordPosition;
    }


    public void setAllRecordsCount(Long allRecordsCount) {
        this.allRecordsCount = allRecordsCount;
    }


    public void setResultRecords(List<T> resultRecords) {
        this.resultRecords = resultRecords;
    }

    public void setLimitOfRecords(int limitOfRecords) {
        this.limitOfRecords = limitOfRecords;
    }

    public void setMoreRecordsExist(boolean moreRecordsExist) {
        this.moreRecordsExist = moreRecordsExist;
    }
}