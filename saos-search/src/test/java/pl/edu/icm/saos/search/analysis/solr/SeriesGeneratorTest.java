package pl.edu.icm.saos.search.analysis.solr;

import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.assertj.core.util.Lists;
import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.search.SearchTestConfiguration;
import pl.edu.icm.saos.search.analysis.request.JudgmentSeriesCriteria;
import pl.edu.icm.saos.search.analysis.request.Period;
import pl.edu.icm.saos.search.analysis.request.Period.PeriodUnit;
import pl.edu.icm.saos.search.analysis.request.XDateRange;
import pl.edu.icm.saos.search.analysis.request.XField;
import pl.edu.icm.saos.search.analysis.request.XSettings;
import pl.edu.icm.saos.search.analysis.result.Point;
import pl.edu.icm.saos.search.analysis.result.Series;


/**
 * @author madryk
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={ SearchTestConfiguration.class })
@Category(SlowTest.class)
public class SeriesGeneratorTest {

    @Autowired
    private SeriesGenerator seriesGenerator;
    
    @Autowired
    @Qualifier("solrJudgmentsServer")
    private SolrServer judgmentsServer;
    
    @Before
    public void setUp() throws Exception {
        
        judgmentsServer.deleteByQuery("*:*");
        judgmentsServer.commit();
        
        indexJudgments();
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void generateSeries_MONTH() {
        // given
        JudgmentSeriesCriteria jsc = new JudgmentSeriesCriteria();
        jsc.setPhrase("content");
        
        XSettings xSettings = createDateXSettings(
                XField.JUDGMENT_DATE,
                new LocalDate(2000, 3, 1),
                new LocalDate(2000, 6, 1),
                new Period(1, PeriodUnit.MONTH));
        
        
        // execute
        Series<Object, Integer> series = seriesGenerator.generateSeries(jsc, xSettings);
        
        // assert
        
        List<Point<Object, Integer>> expectedPoints = Lists.newArrayList();
        expectedPoints.add(new Point<Object, Integer>(new LocalDate(2000, 3, 1), 2));
        expectedPoints.add(new Point<Object, Integer>(new LocalDate(2000, 4, 1), 0));
        expectedPoints.add(new Point<Object, Integer>(new LocalDate(2000, 5, 1), 3));
        
        Assert.assertEquals(expectedPoints, series.getPoints());
        
    }
    
    @Test
    public void generateSeries_WEEKS() {
        // given
        JudgmentSeriesCriteria jsc = new JudgmentSeriesCriteria();
        jsc.setPhrase("content");
        
        XSettings xSettings = createDateXSettings(
                XField.JUDGMENT_DATE,
                new LocalDate(2000, 5, 1),
                new LocalDate(2000, 5, 31),
                new Period(2, PeriodUnit.WEEK));
        
        
        // execute
        Series<Object, Integer> series = seriesGenerator.generateSeries(jsc, xSettings);
        
        // assert
        
        List<Point<Object, Integer>> expectedPoints = Lists.newArrayList();
        expectedPoints.add(new Point<Object, Integer>(new LocalDate(2000, 5, 1), 1));
        expectedPoints.add(new Point<Object, Integer>(new LocalDate(2000, 5, 15), 2));
        expectedPoints.add(new Point<Object, Integer>(new LocalDate(2000, 5, 29), 2)); // to 2000-06-12
        
        Assert.assertEquals(expectedPoints, series.getPoints());
        
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private XSettings createDateXSettings(XField xField, LocalDate startDate, LocalDate endDate, Period gap) {
        XSettings xSettings = new XSettings();
        
        xSettings.setField(XField.JUDGMENT_DATE);
        
        XDateRange xDateRange = new XDateRange(startDate, endDate, gap);
        xSettings.setRange(xDateRange);
        
        return xSettings;
    }
    
    
    private void indexJudgments() throws SolrServerException, IOException {
        judgmentsServer.add(fetchDocument(1L, "2000-01-01T00:00:00Z", "content"));
        
        judgmentsServer.add(fetchDocument(2L, "2000-03-11T00:00:00Z", "content"));
        judgmentsServer.add(fetchDocument(3L, "2000-03-25T00:00:00Z", "content"));
        judgmentsServer.add(fetchDocument(31L, "2000-03-25T00:00:00Z", "other"));
        judgmentsServer.add(fetchDocument(4L, "2000-05-01T00:00:00Z", "content"));
        judgmentsServer.add(fetchDocument(5L, "2000-05-21T00:00:00Z", "content"));
        judgmentsServer.add(fetchDocument(6L, "2000-05-25T00:00:00Z", "content"));
        judgmentsServer.add(fetchDocument(7L, "2000-06-02T00:00:00Z", "content"));
        judgmentsServer.add(fetchDocument(71L, "2000-06-02T00:00:00Z", "other"));
        judgmentsServer.add(fetchDocument(8L, "2000-06-03T00:00:00Z", "content"));
        judgmentsServer.add(fetchDocument(9L, "2000-06-12T00:00:00Z", "content"));
        
        judgmentsServer.add(fetchDocument(10L, "2010-02-04T00:00:00Z", "content"));
        
        judgmentsServer.commit();
    }
    
    private SolrInputDocument fetchDocument(long id, String judgmentDateString, String content) {
        SolrInputDocument doc = new SolrInputDocument();
        
        doc.addField("databaseId", id);
        doc.addField("judgmentDate", judgmentDateString);
        doc.addField("content", content);
        
        return doc;
    }
}
