package pl.edu.icm.saos.persistence.search.dto;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.icm.saos.persistence.common.DataObject;
import pl.edu.icm.saos.persistence.search.model.Order;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Search filter.
 * @author lukdumi
 *
 */
@SuppressWarnings("serial")
public abstract class SearchFilter implements Serializable {
   
    private static Logger log = LoggerFactory.getLogger(SearchFilter.class);
        
    protected int first = 0;
    protected int limit = 10;
    private QueryLanguage globalTermSyntax = QueryLanguage.SEARCH_TERMS;
    protected String globalTerm;
    protected String omit;
    protected List<Order> orders = new ArrayList<Order>();
    
    /**
     * Oczekiwany typ wyniku
     */
    public abstract Class<? extends DataObject> getReqType();
  
    public SearchFilter byAll(String aAll) {
        setGlobalTerm(aAll);
        return this;
    }

    /**
     * Omit results marked with this omit flag
     *
     * @param omit the flag for results to omit
     * @return <code>this</this>
     * @see FieldNames#F_OMIT
     */
    public SearchFilter omit(String omit) {
        setOmit(omit);
        return this;
    }

    //******************** LOGIC ********************
    
    /**
     * pierwsza strona ma nr 0
     */
    public void setPage(int pageNo, int pageSize) {
        setLimit(pageSize);
        setFirst(pageNo * pageSize);
    }
    
    public boolean isEmpty() {
        return isGlobalTermEmpty();
    }
    
    public boolean isGlobalTermEmpty() {
        return StringUtils.isEmpty(globalTerm);
    }
    
    public String toString() {
        return String.format("globalKey=%s", globalTerm);
    }
    
    /**
     * see {@link #addOrderBy(Order)}
     */
    public void addOrderBy(String fieldName, boolean ascending) {
        addOrderBy( new Order(fieldName, ascending) );
    }
    
    public void removeOrderBy(String fieldName) {
    	for (Order order : new ArrayList<Order>(orders)) {
    		if (order.getField().equals(fieldName)) {
    			orders.remove(order);
    		}
    	}
    }
  
    
    /**
     * Nazwa pola z {@link FieldNames}
     * Uwaga:
     * - jeśli wybierzesz pole niesortowalne - solr rzuci wyjątek
     */
    public void addOrderBy(Order order) {
        orders.add(order);
    }    
    
    public SearchFilter orderByScore() {
    	orders.clear();
    	orders.add(Order.relevanceOrder());
    	return this;
    }
    
    
    public Order getOrderBy(String fieldName) {
        for (Order order : orders) {
            if (order.getField().equals(fieldName)) {
                return order;
            }
        }
        return null;
    }
    
    //******************** GETTERS ********************
    /**
     * @return the {@link QueryLanguage} that the query string is expressed in
     */
    public QueryLanguage getGlobalTermSyntax() {
        return globalTermSyntax;
    }

    /**
     * Returns global query string (for example the value from the main GUI search input field).
     * 
     * bw: aka ALL field search
     */
    public String getGlobalTerm() {
        return globalTerm;
    }

    /**
     * @return the marker of results that should be excluded
     * @see FieldNames#F_OMIT
     */
    public String getOmit() {
        return omit;
    }

    /**
     * pierwszy rekordy w result set, start licznika od 0
     */
    public int getFirst() {
        return first;
    }
    
    /**
     * lmit rekordów w result set
     */
    public int getLimit() {
        return limit;
    }
    
    public List<Order> getOrders() {
        return orders;
    }
           
    //******************** SETTERS ********************
    public void setGlobalTermSyntax(QueryLanguage queryType) {
        this.globalTermSyntax = queryType;
    }

    public void setGlobalTerm(String globalTerm) {
        this.globalTerm = globalTerm;
    }

    public void setOmit(String omit) {
        this.omit = omit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
    
    public void setFirst(int first) {
        this.first = first;
    }
    
}