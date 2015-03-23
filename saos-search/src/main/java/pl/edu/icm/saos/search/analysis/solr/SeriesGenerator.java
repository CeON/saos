package pl.edu.icm.saos.search.analysis.solr;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.common.chart.Series;
import pl.edu.icm.saos.search.analysis.request.JudgmentSeriesCriteria;
import pl.edu.icm.saos.search.analysis.request.XSettings;
import pl.edu.icm.saos.search.analysis.solr.request.JudgmentSeriesCriteriaConverter;
import pl.edu.icm.saos.search.analysis.solr.request.XSettingsToQueryApplier;
import pl.edu.icm.saos.search.analysis.solr.result.SeriesResultConverter;
import pl.edu.icm.saos.search.search.model.JudgmentCriteria;
import pl.edu.icm.saos.search.search.model.Paging;
import pl.edu.icm.saos.search.search.service.SearchQueryFactory;
import pl.edu.icm.saos.search.search.service.SolrQueryExecutor;

import com.google.common.base.Preconditions;

/**
 * Solr specific series generator
 * 
 * @author Łukasz Dumiszewski
 * @author madryk
 */
@Service("seriesGenerator")
public class SeriesGenerator {
    
    private JudgmentSeriesCriteriaConverter judgmentSeriesCriteriaConverter;
    
    private SearchQueryFactory<JudgmentCriteria> judgmentSearchQueryFactory;
    
    private XSettingsToQueryApplier xSettingsToQueryApplier;
    
    private SeriesResultConverter seriesResultConverter;
    
    private SolrQueryExecutor solrQueryExecutor;
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Generates and returns {@link Series} based on the given criteria and x settings. The y-values
     * of the series are absolute numbers of judgments meeting specified criteria for a given x value. 
     */
    public Series<Object, Integer> generateSeries(JudgmentSeriesCriteria judgmentSeriesCriteria, XSettings xsettings) {
        
        Preconditions.checkNotNull(judgmentSeriesCriteria);
        Preconditions.checkNotNull(xsettings);
        
        JudgmentCriteria judgmentCriteria = judgmentSeriesCriteriaConverter.convert(judgmentSeriesCriteria);
         
        SolrQuery query = judgmentSearchQueryFactory.createQuery(judgmentCriteria, new Paging(0, 0));
        
        xSettingsToQueryApplier.applyXSettingsToQuery(query, xsettings);
        
        
        QueryResponse response = solrQueryExecutor.executeQuery(query);
        
        
        Series<Object, Integer> series = seriesResultConverter.convert(response, xsettings);
        
        
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
    public void setxSettingsToQueryApplier(XSettingsToQueryApplier xSettingsToQueryApplier) {
        this.xSettingsToQueryApplier = xSettingsToQueryApplier;
    }

    @Autowired
    public void setSeriesResultConverter(SeriesResultConverter seriesResultsConverter) {
        this.seriesResultConverter = seriesResultsConverter;
    }

    @Autowired
    public void setSolrQueryExecutor(SolrQueryExecutor solrQueryExecutor) {
        this.solrQueryExecutor = solrQueryExecutor;
    }
    
}
