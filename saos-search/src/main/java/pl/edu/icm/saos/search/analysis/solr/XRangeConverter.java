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
     * 
     * @param xRange
     * @return
     */
    boolean isApplicable(XRange xRange);

    /**
     * Converts {@link XRange} into solr range start param
     * 
     * @param xRange
     * @return
     */
    String convertStart(XRange xRange);
    
    /**
     * Converts {@link XRange} into solr range end param
     * 
     * @param xRange
     * @return
     */
    String convertEnd(XRange xRange);
    
    /**
     * Converts {@link XRange} into solr range gap param
     * 
     * @param xRange
     * @return
     */
    String convertGap(XRange xRange);
    
}
