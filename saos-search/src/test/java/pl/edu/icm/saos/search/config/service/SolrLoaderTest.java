package pl.edu.icm.saos.search.config.service;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.junit.Before;
import org.junit.Test;

import pl.edu.icm.saos.search.config.model.IndexConfiguration;

import com.google.common.collect.Lists;

/**
 * @author madryk
 */
public class SolrLoaderTest {

    private SolrLoader solrLoader = new SolrLoader();
    
    private SolrIndexConfigurationCopier indexConfigurationCopier = mock(SolrIndexConfigurationCopier.class);
    
    private IndexReloader indexReloader = mock(IndexReloader.class);
    
    private IndexConfiguration firstIndexConfiguration = new IndexConfiguration();
    
    private IndexConfiguration secondIndexConfiguration = new IndexConfiguration();
    
    @Before
    public void setUp() {
        solrLoader.setConfigurationPath("some/path");
        solrLoader.setCopyConfiguration(true);
        solrLoader.setIndexesConfiguration(Lists.newArrayList(firstIndexConfiguration, secondIndexConfiguration));
        
        solrLoader.setIndexConfigurationCopier(indexConfigurationCopier);
        solrLoader.setIndexReloader(indexReloader);
    }
    
    @Test
    public void load_WITH_COPYING() {
        
        solrLoader.load();
        
        verify(indexConfigurationCopier).copyIndexConfiguration(firstIndexConfiguration, "some/path");
        verify(indexConfigurationCopier).copyIndexConfiguration(secondIndexConfiguration, "some/path");
        
        verify(indexReloader).reloadIndex(firstIndexConfiguration);
        verify(indexReloader).reloadIndex(secondIndexConfiguration);
        
        verifyNoMoreInteractions(indexConfigurationCopier, indexReloader);
    }
    
    @Test
    public void load_WITHOUT_COPYING() {
        solrLoader.setCopyConfiguration(false);
        
        solrLoader.load();
        
        verifyZeroInteractions(indexConfigurationCopier);
        
        verify(indexReloader).reloadIndex(firstIndexConfiguration);
        verify(indexReloader).reloadIndex(secondIndexConfiguration);
        verifyNoMoreInteractions(indexReloader);
    }
    
    @Test
    public void shutdown_WITH_CLEANUP() {
        
        solrLoader.shutdown();
        
        verify(indexConfigurationCopier).cleanupIndexConfiguration(firstIndexConfiguration, "some/path");
        verify(indexConfigurationCopier).cleanupIndexConfiguration(secondIndexConfiguration, "some/path");
        verifyNoMoreInteractions(indexConfigurationCopier);
        verifyZeroInteractions(indexReloader);
    }
    
    @Test
    public void shutdown_WITHOUT_CLEANUP() {
        solrLoader.setCopyConfiguration(false);
        
        solrLoader.shutdown();
        
        verifyZeroInteractions(indexConfigurationCopier, indexReloader);
    }
}
