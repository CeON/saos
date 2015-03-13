package pl.edu.icm.saos.search.analysis.solr;

import pl.edu.icm.saos.search.analysis.request.XRange;

/**
 * Converter of {@link XRange} to solr params defining range facet 
 * 
 * @author madryk
 */
public interface XRangeConverter {

    /**
     * Tells if {@link XRange} can be processed by this converter 
     */
    boolean isApplicable(Class<? extends XRange> clazz);

    /**
     * Converts {@link XRange} into solr range start param
     */
    String convertStart(XRange xRange);
    
    /**
     * Converts {@link XRange} into solr range end param
     */
    String convertEnd(XRange xRange);
    
    /**
     * Converts {@link XRange} into solr range gap param
     */
    String convertGap(XRange xRange);
    
}
