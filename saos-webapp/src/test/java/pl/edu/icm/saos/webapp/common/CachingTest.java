package pl.edu.icm.saos.webapp.common;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import net.sf.ehcache.Cache;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.web.WebAppConfiguration;

import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamberDivision;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgmentForm;
import pl.edu.icm.saos.persistence.repository.ScChamberDivisionRepository;
import pl.edu.icm.saos.persistence.repository.ScJudgmentFormRepository;
import pl.edu.icm.saos.webapp.WebappTestSupport;
import pl.edu.icm.saos.webapp.court.ScListService;
import pl.edu.icm.saos.webapp.court.SimpleDivision;
import pl.edu.icm.saos.webapp.court.SimpleDivisionConverter;

import com.google.common.collect.Lists;

/**
 * @author Łukasz Dumiszewski
 */
@WebAppConfiguration
@Category(SlowTest.class)
public class CachingTest extends WebappTestSupport {

    
    private static Logger log = LoggerFactory.getLogger(CachingTest.class);
    
    @Autowired
    private CacheManager cacheManager;
    
    @Autowired
    private ScListService scListService;
    
    @Autowired
    private ScJudgmentFormRepository scJudgmentFormRepository;
    
    @Autowired
    private ScChamberDivisionRepository scChamberDivisionRepository;
    
    @Autowired
    private SimpleDivisionConverter simpleDivisionConverter;
    
    
    private ScJudgmentFormRepository mockedScJudgmentFormRepository = mock(ScJudgmentFormRepository.class);
    
    private ScChamberDivisionRepository mockedScChamberDivisionRepository = mock(ScChamberDivisionRepository.class);
    
    private SimpleDivisionConverter mockedSimpleDivisionConverter = mock(SimpleDivisionConverter.class);
    
    
    
    @Before
    public void before() {
    
        scListService.setScJudgmentFormRepository(mockedScJudgmentFormRepository);
        scListService.setScChamberDivisionRepository(mockedScChamberDivisionRepository);
        scListService.setSimpleDivisionConverter(mockedSimpleDivisionConverter);
    }
    
    @After
    public void after() {
    
        scListService.setScJudgmentFormRepository(scJudgmentFormRepository);
        scListService.setScChamberDivisionRepository(scChamberDivisionRepository);
        scListService.setSimpleDivisionConverter(simpleDivisionConverter);
    
    }
    
    
    //------------------------ LOGIC --------------------------
    
    @Test
    public void findScJudgmentForms_NoArgumentFunctionCaching() throws InterruptedException {
    
        // given
        SupremeCourtJudgmentForm scJudgmentForm1 = mock(SupremeCourtJudgmentForm.class);
        SupremeCourtJudgmentForm scJudgmentForm2 = mock(SupremeCourtJudgmentForm.class);
        
        List<SupremeCourtJudgmentForm> scJudgmentForms = Lists.newArrayList(scJudgmentForm1, scJudgmentForm2);
        when(mockedScJudgmentFormRepository.findAll()).thenReturn(scJudgmentForms);
        
        // execute & assert
        
        // should fetch from db
        List<SupremeCourtJudgmentForm> retScJudgmentForms = scListService.findScJudgmentForms();
        assertTrue(scJudgmentForms == retScJudgmentForms);
        verify(mockedScJudgmentFormRepository).findAll();
        
        // should use cache
        retScJudgmentForms = scListService.findScJudgmentForms();
        assertTrue(scJudgmentForms == retScJudgmentForms);
        verifyNoMoreInteractions(mockedScJudgmentFormRepository);
        
        // should fetch from db (cache invalidated after some time)
        Thread.sleep(1200);
        retScJudgmentForms = scListService.findScJudgmentForms();
        assertTrue(scJudgmentForms == retScJudgmentForms);
        verify(mockedScJudgmentFormRepository, Mockito.times(2)).findAll();
        
        
        
    }
    

    @Test
    public void findScJudgmentForms_ArgumentFunctionCaching() throws InterruptedException {
    
        // given
        
        SupremeCourtChamberDivision scDivision1 = mock(SupremeCourtChamberDivision.class);
        SupremeCourtChamberDivision scDivision2 = mock(SupremeCourtChamberDivision.class);
        SupremeCourtChamberDivision scDivision3 = mock(SupremeCourtChamberDivision.class);
        
        when(scDivision1.getName()).thenReturn("Wydział1");
        when(scDivision2.getName()).thenReturn("Wydział2");
        when(scDivision3.getName()).thenReturn("Wydział3");
        
        List<SupremeCourtChamberDivision> scDivisions_1_2 = Lists.newArrayList(scDivision1, scDivision2);
        List<SupremeCourtChamberDivision> scDivisions_3 = Lists.newArrayList(scDivision3);
        when(mockedScChamberDivisionRepository.findAllByScChamberId(1)).thenReturn(scDivisions_1_2);
        when(mockedScChamberDivisionRepository.findAllByScChamberId(2)).thenReturn(scDivisions_3);
        
        SimpleDivision simpleDivision1 = mock(SimpleDivision.class);
        SimpleDivision simpleDivision2 = mock(SimpleDivision.class);
        SimpleDivision simpleDivision3 = mock(SimpleDivision.class);
        
        List<SimpleDivision> simpleDivisions_1_2 = Lists.newArrayList(simpleDivision1, simpleDivision2);
        List<SimpleDivision> simpleDivisions_3 = Lists.newArrayList(simpleDivision3);
        when(mockedSimpleDivisionConverter.convertScChamberDivisions(scDivisions_1_2)).thenReturn(simpleDivisions_1_2);
        when(mockedSimpleDivisionConverter.convertScChamberDivisions(scDivisions_3)).thenReturn(simpleDivisions_3);
        
        
        // execute & assert
        
        // for id = 1 should fetch from db
        List<SimpleDivision> retScDivisions = scListService.findScChamberDivisions(1);
        assertTrue(simpleDivisions_1_2 == retScDivisions);
        verify(mockedScChamberDivisionRepository).findAllByScChamberId(1);
        
        // for id = 2 should fetch from db
        retScDivisions = scListService.findScChamberDivisions(2);
        assertTrue(simpleDivisions_3 == retScDivisions);
        verify(mockedScChamberDivisionRepository).findAllByScChamberId(2);
        
        // for id = 1 should use cache
        retScDivisions = scListService.findScChamberDivisions(1);
        assertTrue(simpleDivisions_1_2 == retScDivisions);
        verifyNoMoreInteractions(mockedScChamberDivisionRepository);
        
        // for id = 1 should fetch from db (cache invalidated after some time)
        Thread.sleep(1200);
        retScDivisions = scListService.findScChamberDivisions(1);
        assertTrue(simpleDivisions_1_2 == retScDivisions);
        verify(mockedScChamberDivisionRepository, Mockito.times(2)).findAllByScChamberId(1);
        
        printCacheStatistics();
       
        
        
    }
    
    //------------------------ PRIVATE --------------------------
    
    private void printCacheStatistics() {
        for (String cacheName : cacheManager.getCacheNames()) {
            Cache cache = (Cache)cacheManager.getCache(cacheName).getNativeCache();
            log.info(cacheName+" - "+ cache.getStatistics());
            log.info("keys" + cache.getKeys());
        }
    }

}
