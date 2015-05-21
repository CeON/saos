package pl.edu.icm.saos.search.analysis.solr;

import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
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

import pl.edu.icm.saos.common.chart.Point;
import pl.edu.icm.saos.common.chart.Series;
import pl.edu.icm.saos.common.chart.value.DayPeriod;
import pl.edu.icm.saos.common.chart.value.MonthPeriod;
import pl.edu.icm.saos.common.chart.value.SimpleLocalDate;
import pl.edu.icm.saos.common.chart.value.YearPeriod;
import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.search.SearchTestConfiguration;
import pl.edu.icm.saos.search.analysis.request.JudgmentSeriesCriteria;
import pl.edu.icm.saos.search.analysis.request.Period;
import pl.edu.icm.saos.search.analysis.request.Period.PeriodUnit;
import pl.edu.icm.saos.search.analysis.request.XDateRange;
import pl.edu.icm.saos.search.analysis.request.XField;
import pl.edu.icm.saos.search.analysis.request.XSettings;

import com.google.common.collect.Lists;


/**
 * @author madryk
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={ SearchTestConfiguration.class })
@Category(SlowTest.class)
public class SeriesGeneratorJudgmentDateIntTest {

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
    public void generateSeries_YEARS() {
        
        // given
        JudgmentSeriesCriteria jsc = new JudgmentSeriesCriteria();
        jsc.setPhrase("content");
        
        XSettings xSettings = createDateXSettings(
                XField.JUDGMENT_DATE,
                new LocalDate(2000, 1, 1),
                new LocalDate(2004, 5, 31),
                new Period(2, PeriodUnit.YEAR));
        
        
        // execute
        Series<Object, Integer> series = seriesGenerator.generateSeries(jsc, xSettings);
        
        // assert
        
        List<Point<Object, Integer>> expectedPoints = Lists.newArrayList();
        expectedPoints.add(new Point<Object, Integer>(new YearPeriod(2000, 2001),  12));
        expectedPoints.add(new Point<Object, Integer>(new YearPeriod(2002, 2003), 1));
        expectedPoints.add(new Point<Object, Integer>(new YearPeriod(2004, 2005), 0)); 
        
        Assert.assertEquals(expectedPoints, series.getPoints());
        
    }
    
    
    @Test
    public void generateSeries_MONTHS() {
        
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
        expectedPoints.add(new Point<Object, Integer>(new MonthPeriod(2000, 3, 2000, 3), 2));
        expectedPoints.add(new Point<Object, Integer>(new MonthPeriod(2000, 4, 2000, 4), 0));
        expectedPoints.add(new Point<Object, Integer>(new MonthPeriod(2000, 5, 2000, 5), 4));
        expectedPoints.add(new Point<Object, Integer>(new MonthPeriod(2000, 6, 2000, 6), 3)); 
        
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
                new Period(14, PeriodUnit.DAY));
        
        
        // execute
        Series<Object, Integer> series = seriesGenerator.generateSeries(jsc, xSettings);
        
        // assert
        
        List<Point<Object, Integer>> expectedPoints = Lists.newArrayList();
        expectedPoints.add(new Point<Object, Integer>(new DayPeriod(new SimpleLocalDate(2000, 5, 1), new SimpleLocalDate(2000, 5, 14)), 1));
        expectedPoints.add(new Point<Object, Integer>(new DayPeriod(new SimpleLocalDate(2000, 5, 15), new SimpleLocalDate(2000, 5, 28)), 3));
        expectedPoints.add(new Point<Object, Integer>(new DayPeriod(new SimpleLocalDate(2000, 5, 29), new SimpleLocalDate(2000, 6, 11)), 2)); 
        
        Assert.assertEquals(expectedPoints, series.getPoints());
        
    }
    
    
    @Test
    public void generateSeries_DAYS() {
        
        // given
        JudgmentSeriesCriteria jsc = new JudgmentSeriesCriteria();
        jsc.setPhrase("content");
        
        XSettings xSettings = createDateXSettings(
                XField.JUDGMENT_DATE,
                new LocalDate(2000, 5, 20),
                new LocalDate(2000, 5, 26),
                new Period(1, PeriodUnit.DAY));
        
        
        // execute
        Series<Object, Integer> series = seriesGenerator.generateSeries(jsc, xSettings);
        
        // assert
        
        List<Point<Object, Integer>> expectedPoints = Lists.newArrayList();
        expectedPoints.add(new Point<Object, Integer>(new DayPeriod(new SimpleLocalDate(2000, 5, 20), new SimpleLocalDate(2000, 5, 20)), 0));
        expectedPoints.add(new Point<Object, Integer>(new DayPeriod(new SimpleLocalDate(2000, 5, 21), new SimpleLocalDate(2000, 5, 21)), 2));
        expectedPoints.add(new Point<Object, Integer>(new DayPeriod(new SimpleLocalDate(2000, 5, 22), new SimpleLocalDate(2000, 5, 22)), 0));
        expectedPoints.add(new Point<Object, Integer>(new DayPeriod(new SimpleLocalDate(2000, 5, 23), new SimpleLocalDate(2000, 5, 23)), 0));
        expectedPoints.add(new Point<Object, Integer>(new DayPeriod(new SimpleLocalDate(2000, 5, 24), new SimpleLocalDate(2000, 5, 24)), 0));
        expectedPoints.add(new Point<Object, Integer>(new DayPeriod(new SimpleLocalDate(2000, 5, 25), new SimpleLocalDate(2000, 5, 25)), 1));
        expectedPoints.add(new Point<Object, Integer>(new DayPeriod(new SimpleLocalDate(2000, 5, 26), new SimpleLocalDate(2000, 5, 26)), 0));
        
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
        judgmentsServer.add(fetchDocument(51L, "2000-05-21T00:00:00Z", "content"));
        judgmentsServer.add(fetchDocument(6L, "2000-05-25T00:00:00Z", "content"));
        judgmentsServer.add(fetchDocument(7L, "2000-06-02T00:00:00Z", "content"));
        judgmentsServer.add(fetchDocument(71L, "2000-06-02T00:00:00Z", "other"));
        judgmentsServer.add(fetchDocument(8L, "2000-06-03T00:00:00Z", "content"));
        judgmentsServer.add(fetchDocument(9L, "2000-06-12T00:00:00Z", "content"));
        
        judgmentsServer.add(fetchDocument(91L, "2001-06-12T00:00:00Z", "content"));
        judgmentsServer.add(fetchDocument(92L, "2001-06-12T00:00:00Z", "content"));
        
        judgmentsServer.add(fetchDocument(93L, "2002-06-12T00:00:00Z", "content"));
        
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
