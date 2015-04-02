package pl.edu.icm.saos.search.analysis.solr.result;

import java.util.Objects;

import org.jadira.usertype.spi.utils.lang.StringUtils;

import com.google.common.base.Preconditions;

/**
 * Representation of facet single count.
 * 
 * @author madryk
 */
public class FacetCount {

    private String value;
    private int count;
    
    
    //------------------------ CONSTRUCTORS --------------------------
    
    public FacetCount(String value, int count) {
        Preconditions.checkNotNull(value);
        Preconditions.checkArgument(StringUtils.isNotEmpty(value));
        
        this.value = value;
        this.count = count;
    }
    
    
    //------------------------ GETTERS --------------------------
    
    /**
     * Returns textual value of facet
     */
    public String getValue() {
        return value;
    }
    
    /**
     * Returns number of found items
     */
    public int getCount() {
        return count;
    }

    
    //------------------------ equals & hashCode --------------------------

    @Override
    public int hashCode() {
        return Objects.hash(this.value, this.count);
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof FacetCount)) {
            return false;
        }
        final FacetCount other = (FacetCount) obj;
        
        return Objects.equals(this.value, other.value)
                && Objects.equals(this.count, other.count);
    }


    //------------------------ toString --------------------------
    
    @Override
    public String toString() {
        return "FacetCount [value=" + value + ", count=" + count + "]";
    }
    
}
