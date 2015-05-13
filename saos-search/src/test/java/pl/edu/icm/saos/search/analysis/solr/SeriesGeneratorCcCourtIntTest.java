package pl.edu.icm.saos.search.analysis.solr;



import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pl.edu.icm.saos.common.chart.Point;
import pl.edu.icm.saos.common.chart.Series;
import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.search.SearchTestConfiguration;
import pl.edu.icm.saos.search.analysis.request.JudgmentSeriesCriteria;
import pl.edu.icm.saos.search.analysis.request.XField;
import pl.edu.icm.saos.search.analysis.request.XSettings;
import pl.edu.icm.saos.search.util.FieldValuePrefixAdder;

import com.google.common.collect.Lists;


/**
 * @author madryk
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={ SearchTestConfiguration.class })
@Category(SlowTest.class)
public class SeriesGeneratorCcCourtIntTest {

    @Autowired
    private SeriesGenerator seriesGenerator;
    
    @Autowired
    private FieldValuePrefixAdder fieldValuePrefixAdder;
    
    @Autowired
    @Qualifier("solrJudgmentsServer")
    private SolrServer judgmentsServer;
    
    
    
    private final long APPEAL_1_ID = 1;
    private final String APPEAL_1 = "Sąd Apelacyjny w Poznaniu";
    
    private final long REGION_1_1_ID = 11;
    private final String REGION_1_1 = "Sąd Okręgowy w Poznaniu";
    private final String DISTRICT_1_1_1 = "Sąd Rejonowy w Gnieźnie";
    private final String DISTRICT_1_1_2 = "Sąd Rejonowy w Pile";
    
    private final long REGION_1_2_ID = 12;
    private final String REGION_1_2 = "Sąd Okręgowy w Lesznie";
    
    
    private final long APPEAL_2_ID = 2;
    private final String APPEAL_2 = "Sąd Apelacyjny we Wrocławiu";
    
    private final String REGION_2_1 = "Sąd Okręgowy w Wałbrzychu";
    
    
    @Before
    public void setUp() throws Exception {
        
        judgmentsServer.deleteByQuery("*:*");
        judgmentsServer.commit();
        
        indexJudgments();
    }
    
    
    //------------------------ TESTS --------------------------
    
    
    @Test
    public void generateSeries_APPEAL() {
        
        // given
        JudgmentSeriesCriteria jsc = new JudgmentSeriesCriteria();
        jsc.setPhrase("content");
        
        XSettings xsettings = createXSettings(XField.CC_APPEAL, "");
        
        
        // execute
        Series<Object, Integer> series = seriesGenerator.generateSeries(jsc, xsettings);
        
        // assert
        
        List<Point<Object, Integer>> expectedPoints = Lists.newArrayList();
        expectedPoints.add(new Point<Object, Integer>(APPEAL_1, 8));
        expectedPoints.add(new Point<Object, Integer>(APPEAL_2, 6));
        
        Assert.assertEquals(expectedPoints, series.getPoints());
        
    }
    
    @Test
    public void generateSeries_APPEAL1_REGION() {
        
        // given
        JudgmentSeriesCriteria jsc = new JudgmentSeriesCriteria();
        jsc.setPhrase("content");
        
        XSettings xsettings = createXSettings(XField.CC_REGION, ""+APPEAL_1_ID);
        
        
        // execute
        Series<Object, Integer> series = seriesGenerator.generateSeries(jsc, xsettings);
        
        // assert
        
        List<Point<Object, Integer>> expectedPoints = Lists.newArrayList();
        expectedPoints.add(new Point<Object, Integer>(APPEAL_1, 2));
        expectedPoints.add(new Point<Object, Integer>(REGION_1_2, 1));
        expectedPoints.add(new Point<Object, Integer>(REGION_1_1, 5));
        
        Assert.assertEquals(expectedPoints, series.getPoints());
        
    }
    
    @Test
    public void generateSeries_APPEAL2_REGION() {
        
        // given
        JudgmentSeriesCriteria jsc = new JudgmentSeriesCriteria();
        jsc.setPhrase("content");
        
        XSettings xsettings = createXSettings(XField.CC_REGION, ""+APPEAL_2_ID);
        
        
        // execute
        Series<Object, Integer> series = seriesGenerator.generateSeries(jsc, xsettings);
        
        // assert
        
        List<Point<Object, Integer>> expectedPoints = Lists.newArrayList();
        expectedPoints.add(new Point<Object, Integer>(APPEAL_2, 2));
        expectedPoints.add(new Point<Object, Integer>(REGION_2_1, 4));
        
        Assert.assertEquals(expectedPoints, series.getPoints());
        
    }
    
    @Test
    public void generateSeries_REGION_1_1_DISTRICT() {
        
        // given
        JudgmentSeriesCriteria jsc = new JudgmentSeriesCriteria();
        jsc.setPhrase("content");
        
        XSettings xsettings = createXSettings(XField.CC_DISTRICT, ""+REGION_1_1_ID);
        
        
        // execute
        Series<Object, Integer> series = seriesGenerator.generateSeries(jsc, xsettings);
        
        // assert
        
        List<Point<Object, Integer>> expectedPoints = Lists.newArrayList();
        expectedPoints.add(new Point<Object, Integer>(REGION_1_1, 1));
        expectedPoints.add(new Point<Object, Integer>(DISTRICT_1_1_1, 1));
        expectedPoints.add(new Point<Object, Integer>(DISTRICT_1_1_2, 3));
        
        Assert.assertEquals(expectedPoints, series.getPoints());
        
    }
    
 
    
    //------------------------ PRIVATE --------------------------
    
    private XSettings createXSettings(XField xField, String fieldValuePrefix) {
        XSettings xsettings = new XSettings();
        
        xsettings.setField(xField);
        xsettings.setFieldValuePrefix(fieldValuePrefix);
        
        return xsettings;
    }
    
    
    private void indexJudgments() throws SolrServerException, IOException {
        judgmentsServer.add(fetchDocument(1L, "content", APPEAL_1, prefixValue(APPEAL_1_ID, APPEAL_1), ""));
        
        judgmentsServer.add(fetchDocument(2L, "content", APPEAL_1, prefixValue(APPEAL_1_ID, APPEAL_1), ""));
        judgmentsServer.add(fetchDocument(3L, "content", APPEAL_1, prefixValue(APPEAL_1_ID, REGION_1_1), prefixValue(REGION_1_1_ID, REGION_1_1)));
        judgmentsServer.add(fetchDocument(31L,"other",   APPEAL_1, prefixValue(APPEAL_1_ID, REGION_1_1), prefixValue(REGION_1_1_ID, REGION_1_1)));
        judgmentsServer.add(fetchDocument(4L, "content", APPEAL_1, prefixValue(APPEAL_1_ID, REGION_1_2), prefixValue(REGION_1_2_ID, REGION_1_2)));
        judgmentsServer.add(fetchDocument(5L, "content", APPEAL_1, prefixValue(APPEAL_1_ID, REGION_1_1), prefixValue(REGION_1_1_ID, DISTRICT_1_1_1)));
        judgmentsServer.add(fetchDocument(51L,"content", APPEAL_1, prefixValue(APPEAL_1_ID, REGION_1_1), prefixValue(REGION_1_1_ID, DISTRICT_1_1_2)));
        judgmentsServer.add(fetchDocument(6L, "content", APPEAL_1, prefixValue(APPEAL_1_ID, REGION_1_1), prefixValue(REGION_1_1_ID, DISTRICT_1_1_2)));
        judgmentsServer.add(fetchDocument(7L, "content", APPEAL_1, prefixValue(APPEAL_1_ID, REGION_1_1), prefixValue(REGION_1_1_ID, DISTRICT_1_1_2)));
        
        judgmentsServer.add(fetchDocument(71L,"other",   APPEAL_2, prefixValue(APPEAL_2_ID, APPEAL_2), ""));
        judgmentsServer.add(fetchDocument(8L, "content", APPEAL_2, prefixValue(APPEAL_2_ID, APPEAL_2), ""));
        judgmentsServer.add(fetchDocument(9L, "content", APPEAL_2, prefixValue(APPEAL_2_ID, APPEAL_2), ""));
        judgmentsServer.add(fetchDocument(91L,"content", APPEAL_2, prefixValue(APPEAL_2_ID, REGION_2_1), prefixValue(APPEAL_2_ID, REGION_2_1)));
        judgmentsServer.add(fetchDocument(92L,"content", APPEAL_2, prefixValue(APPEAL_2_ID, REGION_2_1), prefixValue(APPEAL_2_ID, REGION_2_1)));
        judgmentsServer.add(fetchDocument(93L,"content", APPEAL_2, prefixValue(APPEAL_2_ID, REGION_2_1), prefixValue(APPEAL_2_ID, REGION_2_1)));
        judgmentsServer.add(fetchDocument(10L,"content", APPEAL_2, prefixValue(APPEAL_2_ID, REGION_2_1), prefixValue(APPEAL_2_ID, REGION_2_1)));
        
        judgmentsServer.commit();
    }
    
    private SolrInputDocument fetchDocument(long id, String content, String appealName, String regionName, String districtName) {
        SolrInputDocument doc = new SolrInputDocument();
        
        doc.addField("databaseId", id);
        doc.addField("content", content);
        doc.addField("ccAppealName", appealName);
        
        if (!StringUtils.isBlank(regionName)) {
            doc.addField("ccRegionName", regionName);
        }
        if (!StringUtils.isBlank(districtName)) {
            doc.addField("ccDistrictName", districtName);
        }
        
        
        return doc;
    }
    
    private String prefixValue(long id, String name) {
        return fieldValuePrefixAdder.addFieldPrefix(name, ""+id);
    }
}
