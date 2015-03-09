package pl.edu.icm.saos.search.analysis.solr;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.search.analysis.request.JudgmentSeriesCriteria;
import pl.edu.icm.saos.search.analysis.request.XSettings;
import pl.edu.icm.saos.search.analysis.result.Series;
import pl.edu.icm.saos.search.search.model.JudgmentCriteria;
import pl.edu.icm.saos.search.search.model.Paging;
import pl.edu.icm.saos.search.search.service.SearchQueryFactory;

/**
 * Solr specific series generator
 * 
 * @author Łukasz Dumiszewski
 */
@Service("seriesGenerator")
public class SeriesGenerator {
    
    private final static Logger log = LoggerFactory.getLogger(SeriesGenerator.class);

    
    private JudgmentSeriesCriteriaConverter judgmentSeriesCriteriaConverter;
    
    private SearchQueryFactory<JudgmentCriteria> judgmentSearchQueryFactory;
    
    private XSettingsFacetQueryApplier xSettingsFacetQueryApplier;
    
    private SeriesResultsConverter seriesResultsConverter;
    
    private SolrServer solrServer;
    
    
    /**
     * Generates and returns {@link Series} based on the given criteria and x settings. The y-values
     * of the series are absolute numbers of judgments meeting specified criteria for a given x value. 
     */
    public Series<Object, Integer> generateSeries(JudgmentSeriesCriteria judgmentSeriesCriteria, XSettings xsettings) {
        
        JudgmentCriteria judgmentCriteria = judgmentSeriesCriteriaConverter.convert(judgmentSeriesCriteria);
         
        SolrQuery query = judgmentSearchQueryFactory.createQuery(judgmentCriteria, new Paging(0, 0));
        
        xSettingsFacetQueryApplier.applyXSettingsToQuery(query, xsettings);
        
        QueryResponse response = null;
        try {
            response = solrServer.query(query);
        } catch (SolrServerException e) {
            log.warn("Error in generating series", e);
            return new Series<Object, Integer>();
        }
        
        Series<Object, Integer> series = seriesResultsConverter.convertToSeries(response, xsettings.getField());
        
        
        return series;
        
    }

    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setJudgmentSeriesCriteriaConverter(JudgmentSeriesCriteriaConverter judgmentSeriesCriteriaConverter) {
        this.judgmentSeriesCriteriaConverter = judgmentSeriesCriteriaConverter;
    }

    @Autowired
    public void setJudgmentSearchQueryFactory(SearchQueryFactory<JudgmentCriteria> judgmentSearchQueryFactory) {
        this.judgmentSearchQueryFactory = judgmentSearchQueryFactory;
    }

    @Autowired
    public void setxSettingsFacetQueryApplier(XSettingsFacetQueryApplier xSettingsFacetQueryApplier) {
        this.xSettingsFacetQueryApplier = xSettingsFacetQueryApplier;
    }

    @Autowired
    public void setSeriesResultsConverter(SeriesResultsConverter seriesResultsConverter) {
        this.seriesResultsConverter = seriesResultsConverter;
    }

    @Autowired
    @Qualifier("solrJudgmentsServer")
    public void setSolrServer(SolrServer solrServer) {
        this.solrServer = solrServer;
    }
    
}
