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
import org.powermock.reflect.Whitebox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pl.edu.icm.saos.common.chart.Point;
import pl.edu.icm.saos.common.chart.Series;
import pl.edu.icm.saos.common.chart.value.CcCourtArea;
import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.search.SearchTestConfiguration;
import pl.edu.icm.saos.search.analysis.request.JudgmentSeriesCriteria;
import pl.edu.icm.saos.search.analysis.request.XField;
import pl.edu.icm.saos.search.analysis.request.XSettings;
import pl.edu.icm.saos.search.util.CcCourtAreaFieldValueCreator;

import com.google.common.collect.Lists;


/**
 * @author Łukasz Dumiszewski
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={ SearchTestConfiguration.class })
@Category(SlowTest.class)
public class SeriesGeneratorCcCourtIntTest {

    @Autowired
    private SeriesGenerator seriesGenerator;
    
    @Autowired
    private CcCourtAreaFieldValueCreator ccCourtAreaFieldValueCreator;
    
    @Autowired
    @Qualifier("solrJudgmentsServer")
    private SolrServer judgmentsServer;
    
    
    private CommonCourt appealCourt1 = createCourt(1, "Sąd Apelacyjny w Poznaniu");
    
    private CommonCourt regionalCourt1_1 = createCourt(11, "Sąd Okręgowy w Poznaniu");
    private CommonCourt districtCourt1_1_1 = createCourt(111, "Sąd Rejonowy w Gnieźnie");
    private CommonCourt districtCourt1_1_2 = createCourt(112, "Sąd Rejonowy w Pile");
    
    private CommonCourt regionalCourt1_2 = createCourt(12, "Sąd Okręgowy w Lesznie");
    
    
    
    private CommonCourt appealCourt2 = createCourt(2, "Sąd Apelacyjny we Wrocławiu");
    
    private CommonCourt regionalCourt2_1 = createCourt(21, "Sąd Okręgowy w Wałbrzychu");
    
    
    
    @Before
    public void before() throws Exception {
        
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
        expectedPoints.add(new Point<Object, Integer>(createCcCourtArea(appealCourt1), 8));
        expectedPoints.add(new Point<Object, Integer>(createCcCourtArea(appealCourt2), 6));
        
        Assert.assertEquals(expectedPoints, series.getPoints());
        
    }
    
    @Test
    public void generateSeries_APPEAL1_REGION() {
        
        // given
        JudgmentSeriesCriteria jsc = new JudgmentSeriesCriteria();
        jsc.setPhrase("content");
        
        XSettings xsettings = createXSettings(XField.CC_REGION, ""+appealCourt1.getId());
        
        
        // execute
        Series<Object, Integer> series = seriesGenerator.generateSeries(jsc, xsettings);
        
        // assert
        
        List<Point<Object, Integer>> expectedPoints = Lists.newArrayList();
        expectedPoints.add(new Point<Object, Integer>(createCcCourtArea(appealCourt1), 2));
        expectedPoints.add(new Point<Object, Integer>(createCcCourtArea(regionalCourt1_2), 1));
        expectedPoints.add(new Point<Object, Integer>(createCcCourtArea(regionalCourt1_1), 5));
        
        Assert.assertEquals(expectedPoints, series.getPoints());
        
    }
    
    @Test
    public void generateSeries_APPEAL2_REGION() {
        
        // given
        JudgmentSeriesCriteria jsc = new JudgmentSeriesCriteria();
        jsc.setPhrase("content");
        
        XSettings xsettings = createXSettings(XField.CC_REGION, ""+appealCourt2.getId());
        
        
        // execute
        Series<Object, Integer> series = seriesGenerator.generateSeries(jsc, xsettings);
        
        // assert
        
        List<Point<Object, Integer>> expectedPoints = Lists.newArrayList();
        expectedPoints.add(new Point<Object, Integer>(createCcCourtArea(appealCourt2), 2));
        expectedPoints.add(new Point<Object, Integer>(createCcCourtArea(regionalCourt2_1), 4));
        
        Assert.assertEquals(expectedPoints, series.getPoints());
        
    }
    
    @Test
    public void generateSeries_REGION_1_1_DISTRICT() {
        
        // given
        JudgmentSeriesCriteria jsc = new JudgmentSeriesCriteria();
        jsc.setPhrase("content");
        
        XSettings xsettings = createXSettings(XField.CC_DISTRICT, ""+regionalCourt1_1.getId());
        
        
        // execute
        Series<Object, Integer> series = seriesGenerator.generateSeries(jsc, xsettings);
        
        // assert
        
        List<Point<Object, Integer>> expectedPoints = Lists.newArrayList();
        expectedPoints.add(new Point<Object, Integer>(createCcCourtArea(regionalCourt1_1), 1));
        expectedPoints.add(new Point<Object, Integer>(createCcCourtArea(districtCourt1_1_1), 1));
        expectedPoints.add(new Point<Object, Integer>(createCcCourtArea(districtCourt1_1_2), 3));
        
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
        judgmentsServer.add(fetchDocument(1L, "content", createAreaField(null, appealCourt1), createAreaField(appealCourt1.getId(), appealCourt1), ""));
        
        judgmentsServer.add(fetchDocument(2L, "content", createAreaField(null, appealCourt1), createAreaField(appealCourt1.getId(), appealCourt1), ""));
        judgmentsServer.add(fetchDocument(3L, "content", createAreaField(null, appealCourt1), createAreaField(appealCourt1.getId(), regionalCourt1_1), createAreaField(regionalCourt1_1.getId(), regionalCourt1_1)));
        judgmentsServer.add(fetchDocument(31L,"other",   createAreaField(null, appealCourt1), createAreaField(appealCourt1.getId(), regionalCourt1_1), createAreaField(regionalCourt1_1.getId(), regionalCourt1_1)));
        judgmentsServer.add(fetchDocument(4L, "content", createAreaField(null, appealCourt1), createAreaField(appealCourt1.getId(), regionalCourt1_2), createAreaField(regionalCourt1_2.getId(), regionalCourt1_2)));
        judgmentsServer.add(fetchDocument(5L, "content", createAreaField(null, appealCourt1), createAreaField(appealCourt1.getId(), regionalCourt1_1), createAreaField(regionalCourt1_1.getId(), districtCourt1_1_1)));
        judgmentsServer.add(fetchDocument(51L,"content", createAreaField(null, appealCourt1), createAreaField(appealCourt1.getId(), regionalCourt1_1), createAreaField(regionalCourt1_1.getId(), districtCourt1_1_2)));
        judgmentsServer.add(fetchDocument(6L, "content", createAreaField(null, appealCourt1), createAreaField(appealCourt1.getId(), regionalCourt1_1), createAreaField(regionalCourt1_1.getId(), districtCourt1_1_2)));
        judgmentsServer.add(fetchDocument(7L, "content", createAreaField(null, appealCourt1), createAreaField(appealCourt1.getId(), regionalCourt1_1), createAreaField(regionalCourt1_1.getId(), districtCourt1_1_2)));
        
        judgmentsServer.add(fetchDocument(71L,"other", createAreaField(null, appealCourt2), createAreaField(appealCourt2.getId(), appealCourt2), ""));
        judgmentsServer.add(fetchDocument(8L, "content", createAreaField(null, appealCourt2), createAreaField(appealCourt2.getId(), appealCourt2), ""));
        judgmentsServer.add(fetchDocument(9L, "content", createAreaField(null, appealCourt2), createAreaField(appealCourt2.getId(), appealCourt2), ""));
        judgmentsServer.add(fetchDocument(91L,"content", createAreaField(null, appealCourt2), createAreaField(appealCourt2.getId(), regionalCourt2_1), createAreaField(regionalCourt2_1.getId(), regionalCourt2_1)));
        judgmentsServer.add(fetchDocument(92L,"content", createAreaField(null, appealCourt2), createAreaField(appealCourt2.getId(), regionalCourt2_1), createAreaField(regionalCourt2_1.getId(), regionalCourt2_1)));
        judgmentsServer.add(fetchDocument(93L,"content", createAreaField(null, appealCourt2), createAreaField(appealCourt2.getId(), regionalCourt2_1), createAreaField(regionalCourt2_1.getId(), regionalCourt2_1)));
        judgmentsServer.add(fetchDocument(10L,"content", createAreaField(null, appealCourt2), createAreaField(appealCourt2.getId(), regionalCourt2_1), createAreaField(regionalCourt2_1.getId(), regionalCourt2_1)));
        
        judgmentsServer.commit();
    }
    
    private SolrInputDocument fetchDocument(long id, String content, String appealArea, String regionArea, String districtArea) {
        SolrInputDocument doc = new SolrInputDocument();
        
        doc.addField("databaseId", id);
        doc.addField("content", content);
        doc.addField("ccAppealArea", appealArea);
        
        if (!StringUtils.isBlank(regionArea)) {
            doc.addField("ccRegionArea", regionArea);
        }
        if (!StringUtils.isBlank(districtArea)) {
            doc.addField("ccDistrictArea", districtArea);
        }
        
        
        return doc;
    }
    
    private String createAreaField(Long parentAreaCourtId, CommonCourt court) {
        return ccCourtAreaFieldValueCreator.createCcCourtAreaFieldValue(parentAreaCourtId, court);
    }
    
    private CcCourtArea createCcCourtArea(CommonCourt ccCourt) {
        CcCourtArea ccCourtArea = new CcCourtArea();
        ccCourtArea.setCourtId(ccCourt.getId());
        ccCourtArea.setName(ccCourt.getName());
        return ccCourtArea;
    }
    
    private CommonCourt createCourt(long id, String name) {
        CommonCourt court = new CommonCourt();
        Whitebox.setInternalState(court, "id", id);
        court.setName(name);
        return court;
    }
}
