package pl.edu.icm.saos.search.analysis.solr;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import pl.edu.icm.saos.search.analysis.request.JudgmentSeriesCriteria;
import pl.edu.icm.saos.search.analysis.request.XField;
import pl.edu.icm.saos.search.analysis.request.XSettings;
import pl.edu.icm.saos.search.analysis.result.Series;
import pl.edu.icm.saos.search.search.model.JudgmentCriteria;
import pl.edu.icm.saos.search.search.model.Paging;
import pl.edu.icm.saos.search.search.service.SearchQueryFactory;
import pl.edu.icm.saos.search.search.service.SolrQueryExecutor;

/**
 * @author madryk
 */
@RunWith(MockitoJUnitRunner.class)
public class SeriesGeneratorTest {

    @InjectMocks
    private SeriesGenerator seriesGenerator = new SeriesGenerator();
    
    
    @Mock
    private JudgmentSeriesCriteriaConverter judgmentSeriesCriteriaConverter;
    
    @Mock
    private SearchQueryFactory<JudgmentCriteria> judgmentSearchQueryFactory;
    
    @Mock
    private XSettingsToQueryApplier xSettingsToQueryApplier;
    
    @Mock
    private SeriesResultConverter seriesResultConverter;
    
    @Mock
    private SolrQueryExecutor solrQueryExecutor;
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void generateSeries() {
        // given
        JudgmentSeriesCriteria judgmentSeriesCriteria = new JudgmentSeriesCriteria();
        judgmentSeriesCriteria.setPhrase("phrase");
        
        JudgmentCriteria judgmentCriteria = new JudgmentCriteria("all");
        
        SolrQuery solrQuery = new SolrQuery();
        
        QueryResponse response = new QueryResponse();
        
        XSettings xSettings = new XSettings();
        xSettings.setField(XField.JUDGMENT_DATE);
        
        Series<Object, Integer> series = new Series<Object, Integer>();
        
        when(judgmentSeriesCriteriaConverter.convert(judgmentSeriesCriteria)).thenReturn(judgmentCriteria);
        when(judgmentSearchQueryFactory.createQuery(judgmentCriteria, new Paging(0, 0))).thenReturn(solrQuery);
        when(seriesResultConverter.convertToSeries(response, XField.JUDGMENT_DATE)).thenReturn(series);
        when(solrQueryExecutor.executeQuery(solrQuery)).thenReturn(response);
        
        
        // execute
        Series<Object, Integer> retSeries = seriesGenerator.generateSeries(judgmentSeriesCriteria, xSettings);
        
        // assert
        assertTrue(retSeries == series);
        
        verify(judgmentSeriesCriteriaConverter).convert(judgmentSeriesCriteria);
        verify(judgmentSearchQueryFactory).createQuery(judgmentCriteria, new Paging(0, 0));
        verify(xSettingsToQueryApplier).applyXSettingsToQuery(solrQuery, xSettings);
        verify(seriesResultConverter).convertToSeries(response, XField.JUDGMENT_DATE);
        verify(solrQueryExecutor).executeQuery(solrQuery);
    }
    
    @Test(expected = NullPointerException.class)
    public void generateSeries_NULL_CRITERIA() {
        // execute
        seriesGenerator.generateSeries(null, new XSettings());
    }
    
    @Test(expected = NullPointerException.class)
    public void generateSeries_NULL_XSETTINGS() {
        // execute
        seriesGenerator.generateSeries(new JudgmentSeriesCriteria(), null);
    }
    
}
