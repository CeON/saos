package pl.edu.icm.saos.webapp.analysis;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static pl.edu.icm.saos.common.testcommon.IntToLongMatcher.equalsLong;
import static pl.edu.icm.saos.search.config.model.JudgmentIndexField.CC_DISTRICT_AREA;
import static pl.edu.icm.saos.search.config.model.JudgmentIndexField.CC_DISTRICT_COURT_ID;
import static pl.edu.icm.saos.search.config.model.JudgmentIndexField.CC_REGIONAL_COURT_ID;
import static pl.edu.icm.saos.search.config.model.JudgmentIndexField.CC_REGION_AREA;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import pl.edu.icm.saos.common.chart.value.DayPeriod;
import pl.edu.icm.saos.common.chart.value.MonthPeriod;
import pl.edu.icm.saos.common.chart.value.SimpleLocalDate;
import pl.edu.icm.saos.common.chart.value.YearPeriod;
import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.CommonCourt.CommonCourtType;
import pl.edu.icm.saos.persistence.model.CommonCourtDivision;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamber;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamberDivision;
import pl.edu.icm.saos.persistence.repository.CcDivisionRepository;
import pl.edu.icm.saos.persistence.repository.CommonCourtRepository;
import pl.edu.icm.saos.persistence.repository.ScChamberDivisionRepository;
import pl.edu.icm.saos.persistence.repository.ScChamberRepository;
import pl.edu.icm.saos.search.config.model.JudgmentIndexField;
import pl.edu.icm.saos.search.util.CcCourtAreaFieldValueCreator;
import pl.edu.icm.saos.webapp.WebappTestSupport;
import pl.edu.icm.saos.webapp.analysis.request.JudgmentSeriesFilter;
import pl.edu.icm.saos.webapp.analysis.request.MonthYearRange;
import pl.edu.icm.saos.webapp.analysis.request.UiySettings.UiyValueType;
import pl.edu.icm.saos.webapp.court.SimpleCommonCourt;
import pl.edu.icm.saos.webapp.court.SimpleEntity;

import com.google.common.collect.Lists;

/**
 * @author madryk
 */
@Category(SlowTest.class)
@WebAppConfiguration
public class AnalysisControllerTest extends WebappTestSupport {

    @Autowired
    private AnalysisController analysisController;
    
    @Autowired
    @Qualifier("solrJudgmentsServer")
    private SolrServer judgmentsServer;
    
    @Autowired
    private CommonCourtRepository commonCourtRepository;
    
    @Autowired
    private CcDivisionRepository commonCourtDivisionRepository;
    
    @Autowired
    private ScChamberRepository scChamberRepository;
    
    @Autowired
    private ScChamberDivisionRepository scChamberDivisionRepository;
    
    @Autowired
    private CcCourtAreaFieldValueCreator ccCourtAreaFieldValueCreator;
    
    
    private MockMvc mockMvc;
    
    @Autowired
    private WebApplicationContext webApplicationCtx;
    
    private static final String URL_BASE = "/analysis";
    
    
    // --- common courts ---
    
    private CommonCourt appealCourt1;
    
    private CommonCourt regionalCourt1_1;
    
    private CommonCourt districtCourt1_1_1;
    private CommonCourt districtCourt1_1_2;
    
    private CommonCourt regionalCourt1_2;
    
    
    private CommonCourt appealCourt2;
    
    private CommonCourt regionalCourt2_1;
    
    
    // --- common court divisions ---
    
    private CommonCourtDivision appealCourt1_division_1;
    private CommonCourtDivision appealCourt1_division_2;
    private CommonCourtDivision appealCourt1_division_3;
    
    
    // --- supreme court chambers ---
    
    private SupremeCourtChamber scChamber_1;
    private SupremeCourtChamber scChamber_2;
    
    
    // --- supreme court chamber divisions ---
    
    private SupremeCourtChamberDivision scChamber_1_division_1;
    private SupremeCourtChamberDivision scChamber_1_division_2;
    
    private SupremeCourtChamberDivision scChamber_2_division_1;
    
    
    
    @Before
    public void setUp() throws Exception {
        
        judgmentsServer.deleteByQuery("*:*");
        judgmentsServer.commit();
        
        generateCommonCourts();
        generateSupremeCourtChambers();
        indexJudgments();
        
        
        mockMvc = webAppContextSetup(webApplicationCtx)
                .build();
    }
    
    @After
    public void cleanup() throws SolrServerException, IOException {
        judgmentsServer.deleteByQuery("*:*");
        judgmentsServer.commit();
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void showAnalysis_initial() throws Exception {
        // execute
        ResultActions result = mockMvc.perform(get(URL_BASE));
        
        
        // assert
        MonthYearRange expectedMonthYearRange = new MonthYearRange();
        expectedMonthYearRange.setStartMonth(1);
        expectedMonthYearRange.setStartYear((new LocalDate()).getYear() - 20);
        expectedMonthYearRange.setEndMonth((new LocalDate()).getMonthOfYear());
        expectedMonthYearRange.setEndYear((new LocalDate()).getYear());
        
        result
            .andExpect(status().isOk())
            .andExpect(view().name("analysis"))
            .andExpect(model().attribute("analysisForm",
                    allOf(
                            hasProperty("seriesFilters", contains(createJudgmentSeriesFilter(null))),
                            hasProperty("ysettings", hasProperty("valueType", is(UiyValueType.NUMBER))),
                            hasProperty("globalFilter", hasProperty("judgmentDateRange", is(expectedMonthYearRange)))
                    )
            ));
    }
    
    @Test
    public void showAnalysis_checkExposeParams() throws Exception {
        // execute
        ResultActions result = mockMvc.perform(get(URL_BASE)
                .param("seriesFilters[0].phrase", "phrase1")
                .param("seriesFilters[1].phrase", "phrase2")
                .param("globalFilter.judgmentDateRange.startMonth", "1")
                .param("globalFilter.judgmentDateRange.startYear", "1991")
                .param("globalFilter.judgmentDateRange.endMonth", "5")
                .param("globalFilter.judgmentDateRange.endYear", "2001")
                .param("ysettings.valueType", "PERCENT"));
        
        
        // assert
        MonthYearRange expectedMonthYearRange = new MonthYearRange();
        expectedMonthYearRange.setStartMonth(1);
        expectedMonthYearRange.setStartYear(1991);
        expectedMonthYearRange.setEndMonth(5);
        expectedMonthYearRange.setEndYear(2001);
        
        result
            .andExpect(status().isOk())
            .andExpect(view().name("analysis"))
            .andExpect(model().attribute("analysisForm",
                    allOf(
                            hasProperty("seriesFilters", contains(createJudgmentSeriesFilter("phrase1"), createJudgmentSeriesFilter("phrase2"))),
                            hasProperty("ysettings", hasProperty("valueType", is(UiyValueType.PERCENT))),
                            hasProperty("globalFilter", hasProperty("judgmentDateRange", is(expectedMonthYearRange)))
                    )
        ));
    }
    
    @Test
    public void showAnalysis_checkCommonCourtsInModel() throws Exception {
        // execute
        ResultActions result = mockMvc.perform(get(URL_BASE)
                .param("globalFilter.courtCriteria.courtType", "COMMON"));
        
        // assert
        result
            .andExpect(status().isOk())
            .andExpect(view().name("analysis"))
            .andExpect(model().attribute("commonCourts", containsInAnyOrder(
                    createSimpleCommonCourt(appealCourt1),
                    createSimpleCommonCourt(regionalCourt1_1),
                    createSimpleCommonCourt(districtCourt1_1_1),
                    createSimpleCommonCourt(districtCourt1_1_2),
                    createSimpleCommonCourt(regionalCourt1_2),
                    createSimpleCommonCourt(appealCourt2),
                    createSimpleCommonCourt(regionalCourt2_1)
            )
        ));
    }
    
    @Test
    public void showAnalysis_checkCommonCourtDivisionsInModel() throws Exception {
        // execute
        ResultActions result = mockMvc.perform(get(URL_BASE)
                .param("globalFilter.courtCriteria.courtType", "COMMON")
                .param("globalFilter.courtCriteria.ccCourtId", String.valueOf(appealCourt1.getId())));
        
        // assert
        result
            .andExpect(status().isOk())
            .andExpect(view().name("analysis"))
            .andExpect(model().attribute("commonCourtDivisions", containsInAnyOrder(
                    createSimpleEntity(appealCourt1_division_1),
                    createSimpleEntity(appealCourt1_division_2),
                    createSimpleEntity(appealCourt1_division_3)
            )
        ));
    }
    
    @Test
    public void showAnalysis_checkSupremeCourtChambersInModel() throws Exception {
        // execute
        ResultActions result = mockMvc.perform(get(URL_BASE)
                .param("globalFilter.courtCriteria.courtType", "SUPREME"));
        
        
        // assert
        result
            .andExpect(status().isOk())
            .andExpect(view().name("analysis"))
            .andExpect(model().attribute("supremeChambers", containsInAnyOrder(
                    createSimpleEntity(scChamber_1),
                    createSimpleEntity(scChamber_2)
            )
        ));
    }
    
    @Test
    public void showAnalysis_checkSupremeCourtChamberDivisionsInModel() throws Exception {
        // execute
        ResultActions result = mockMvc.perform(get(URL_BASE)
                .param("globalFilter.courtCriteria.courtType", "SUPREME")
                .param("globalFilter.courtCriteria.scCourtChamberId", String.valueOf(scChamber_1.getId())));
        
        // assert
        result
            .andExpect(status().isOk())
            .andExpect(view().name("analysis"))
            .andExpect(model().attribute("supremeChamberDivisions", containsInAnyOrder(
                    createSimpleEntity(scChamber_1_division_1),
                    createSimpleEntity(scChamber_1_division_2)
            )
        ));
    }
    
    @Test
    public void removeSeriesSearchCriteria() throws Exception {
        // execute
        ResultActions result = mockMvc.perform(post(URL_BASE + "/removePhrase")
                .param("seriesFilters[0].phrase", "phrase1")
                .param("seriesFilters[1].phrase", "phrase2")
                .param("seriesFilters[2].phrase", "phrase3")
                .param("filterIndexToRemove", "1"));
        
        // assert
        result
            .andExpect(status().isOk())
            .andExpect(view().name("analysisForm"))
            .andExpect(model().attribute("analysisForm",
                    hasProperty("seriesFilters", contains(
                            createJudgmentSeriesFilter("phrase1"),
                            createJudgmentSeriesFilter("phrase3"))
                    )
            ));
    }
    
    @Test
    public void addNewSeriesSearchCriteria() throws Exception {
        // execute
        ResultActions result = mockMvc.perform(post(URL_BASE + "/addNewPhrase")
                .param("seriesFilters[0].phrase", "phrase1")
                .param("seriesFilters[1].phrase", "phrase2"));
        
        // assert
        result
            .andExpect(status().isOk())
            .andExpect(view().name("analysisForm"))
            .andExpect(model().attribute("analysisForm",
                    hasProperty("seriesFilters", contains(
                            createJudgmentSeriesFilter("phrase1"),
                            createJudgmentSeriesFilter("phrase2"),
                            createJudgmentSeriesFilter(null))
                    )
            ));
    }
    
    @Test
    public void generate_WithOneYearPeriods() throws Exception {
        // execute
        ResultActions result = mockMvc.perform(get(URL_BASE + "/generate")
                .param("seriesFilters[0].phrase", "phrase1")
                .param("seriesFilters[1].phrase", "phrase2")
                .param("globalFilter.judgmentDateRange.startMonth", "1")
                .param("globalFilter.judgmentDateRange.startYear", "1991")
                .param("globalFilter.judgmentDateRange.endMonth", "1")
                .param("globalFilter.judgmentDateRange.endYear", "2001")
                .param("ysettings.valueType", "NUMBER")
                .accept(MediaType.APPLICATION_JSON));
        
        
        // assert
        
        assertMainChartSeries(0, result, 2, 0, 0, 0, 0, 1, 0, 0, 2, 21, 0);
        assertMainChartSeries(1, result, 0, 1, 0, 0, 1, 1, 1, 1, 1, 21, 0);
        
        assertMainChartYearPeriodXticks(result,
                new YearPeriod(1991, 1991),
                new YearPeriod(1992, 1992),
                new YearPeriod(1993, 1993),
                new YearPeriod(1994, 1994),
                new YearPeriod(1995, 1995),
                new YearPeriod(1996, 1996),
                new YearPeriod(1997, 1997),
                new YearPeriod(1998, 1998),
                new YearPeriod(1999, 1999),
                new YearPeriod(2000, 2000),
                new YearPeriod(2001, 2001));
        
        assertAggregatedChartSeries(0, result, 1, 26);
        assertAggregatedChartSeries(1, result, 3, 27);
    }
    
    @Test
    public void generate_WithSixMonthsPeriods() throws Exception {
        // execute
        ResultActions result = mockMvc.perform(get(URL_BASE + "/generate")
                .param("seriesFilters[0].phrase", "phrase1")
                .param("seriesFilters[1].phrase", "phrase2")
                .param("globalFilter.judgmentDateRange.startMonth", "1")
                .param("globalFilter.judgmentDateRange.startYear", "1996")
                .param("globalFilter.judgmentDateRange.endMonth", "1")
                .param("globalFilter.judgmentDateRange.endYear", "2001")
                .param("ysettings.valueType", "NUMBER")
                .accept(MediaType.APPLICATION_JSON));
        
        
        // assert
        
        assertMainChartSeries(0, result, 1, 0, 0, 0, 0, 0, 2, 0, 18, 3, 0);
        assertMainChartSeries(1, result, 1, 0, 0, 1, 0, 1, 0, 1, 17, 4, 0);
        
        assertMainChartMonthPeriodXticks(result,
                new MonthPeriod(1996, 1, 1996, 6),
                new MonthPeriod(1996, 7, 1996, 12),
                new MonthPeriod(1997, 1, 1997, 6),
                new MonthPeriod(1997, 7, 1997, 12),
                new MonthPeriod(1998, 1, 1998, 6),
                new MonthPeriod(1998, 7, 1998, 12),
                new MonthPeriod(1999, 1, 1999, 6),
                new MonthPeriod(1999, 7, 1999, 12),
                new MonthPeriod(2000, 1, 2000, 6),
                new MonthPeriod(2000, 7, 2000, 12),
                new MonthPeriod(2001, 1, 2001, 6));
        
        assertAggregatedChartSeries(0, result, 1, 24);
        assertAggregatedChartSeries(1, result, 3, 25);
    }
    
    @Test
    public void generate_WithOneMonthPeriods() throws Exception {
        // execute
        ResultActions result = mockMvc.perform(get(URL_BASE + "/generate")
                .param("seriesFilters[0].phrase", "phrase1")
                .param("seriesFilters[1].phrase", "phrase2")
                .param("globalFilter.judgmentDateRange.startMonth", "1")
                .param("globalFilter.judgmentDateRange.startYear", "2000")
                .param("globalFilter.judgmentDateRange.endMonth", "1")
                .param("globalFilter.judgmentDateRange.endYear", "2001")
                .param("ysettings.valueType", "NUMBER")
                .accept(MediaType.APPLICATION_JSON));
        
        
        // assert
        
        assertMainChartSeries(0, result, 4, 4, 0, 9, 1, 0, 0, 0, 1, 0, 0, 2, 0);
        assertMainChartSeries(1, result, 6, 3, 0, 7, 1, 0, 0, 0, 3, 0, 0, 1, 0);
        
        assertMainChartXticksFor2000Year(result);
        
        assertAggregatedChartSeries(0, result, 1, 21);
        assertAggregatedChartSeries(1, result, 3, 21);
        
    }
    
    @Test
    public void generate_WithOneWeekPeriods() throws Exception {
        // execute
        ResultActions result = mockMvc.perform(get(URL_BASE + "/generate")
                .param("seriesFilters[0].phrase", "phrase1")
                .param("seriesFilters[1].phrase", "phrase2")
                .param("globalFilter.judgmentDateRange.startMonth", "1")
                .param("globalFilter.judgmentDateRange.startYear", "2000")
                .param("globalFilter.judgmentDateRange.endMonth", "3")
                .param("globalFilter.judgmentDateRange.endYear", "2000")
                .param("ysettings.valueType", "NUMBER")
                .accept(MediaType.APPLICATION_JSON));
        
        
        // assert
        
        assertMainChartSeries(0, result, 1, 0, 0, 1, 3, 0, 0, 3, 0, 0, 0, 0, 0);
        assertMainChartSeries(1, result, 2, 0, 0, 1, 3, 0, 0, 1, 2, 0, 0, 0, 0);
        
        assertMainChartDayPeriodXticks(result,
                new DayPeriod(new SimpleLocalDate(2000, 1, 1),  new SimpleLocalDate(2000, 1, 7)  ),
                new DayPeriod(new SimpleLocalDate(2000, 1, 8),  new SimpleLocalDate(2000, 1, 14) ),
                new DayPeriod(new SimpleLocalDate(2000, 1, 15), new SimpleLocalDate(2000, 1, 21) ),
                new DayPeriod(new SimpleLocalDate(2000, 1, 22), new SimpleLocalDate(2000, 1, 28) ),
                new DayPeriod(new SimpleLocalDate(2000, 1, 29), new SimpleLocalDate(2000, 2, 4)  ),
                new DayPeriod(new SimpleLocalDate(2000, 2, 5),  new SimpleLocalDate(2000, 2, 11) ),
                new DayPeriod(new SimpleLocalDate(2000, 2, 12), new SimpleLocalDate(2000, 2, 18) ),
                new DayPeriod(new SimpleLocalDate(2000, 2, 19), new SimpleLocalDate(2000, 2, 25) ),
                new DayPeriod(new SimpleLocalDate(2000, 2, 26), new SimpleLocalDate(2000, 3, 3)  ),
                new DayPeriod(new SimpleLocalDate(2000, 3, 4),  new SimpleLocalDate(2000, 3, 10) ),
                new DayPeriod(new SimpleLocalDate(2000, 3, 11), new SimpleLocalDate(2000, 3, 17) ),
                new DayPeriod(new SimpleLocalDate(2000, 3, 18), new SimpleLocalDate(2000, 3, 24) ),
                new DayPeriod(new SimpleLocalDate(2000, 3, 25), new SimpleLocalDate(2000, 3, 31) ));
        
        assertAggregatedChartSeries(0, result, 1, 8);
        assertAggregatedChartSeries(1, result, 3, 9);
    }
    
    @Test
    public void generate_WithOneDayPeriods() throws Exception {
        // execute
        ResultActions result = mockMvc.perform(get(URL_BASE + "/generate")
                .param("seriesFilters[0].phrase", "phrase1")
                .param("seriesFilters[1].phrase", "phrase2")
                .param("globalFilter.judgmentDateRange.startMonth", "1")
                .param("globalFilter.judgmentDateRange.startYear", "2000")
                .param("globalFilter.judgmentDateRange.endMonth", "1")
                .param("globalFilter.judgmentDateRange.endYear", "2000")
                .param("ysettings.valueType", "NUMBER")
                .accept(MediaType.APPLICATION_JSON));
        
        
        // assert
        
        assertMainChartSeries(0, result,
                0, 0, 1, 0, 0,  0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,  0, 0, 0, 0, 0,
                0, 0, 0, 0, 1,  0, 0, 0, 1, 0, 1);
        assertMainChartSeries(1, result,
                0, 0, 0, 0, 1,  0, 1, 0, 0, 0,
                0, 0, 0, 0, 0,  0, 0, 0, 0, 0,
                0, 0, 0, 0, 0,  1, 0, 0, 1, 1, 1);
        
        List<DayPeriod> oneDayPeriods = Lists.newArrayList();
        for (int i=1; i<=31; ++i) {
            oneDayPeriods.add(new DayPeriod(new SimpleLocalDate(2000, 1, i), new SimpleLocalDate(2000, 1, i)));
        }
        assertMainChartDayPeriodXticks(result, oneDayPeriods.toArray(new DayPeriod[31]));
        
        
        assertAggregatedChartSeries(0, result, 1, 4);
        assertAggregatedChartSeries(1, result, 3, 6);
    }
    
    @Test
    public void generate_WithPercentYValues() throws Exception {
        // execute
        ResultActions result = mockMvc.perform(get(URL_BASE + "/generate")
                .param("seriesFilters[0].phrase", "phrase1")
                .param("seriesFilters[1].phrase", "phrase2")
                .param("globalFilter.judgmentDateRange.startMonth", "1")
                .param("globalFilter.judgmentDateRange.startYear", "2000")
                .param("globalFilter.judgmentDateRange.endMonth", "1")
                .param("globalFilter.judgmentDateRange.endYear", "2001")
                .param("ysettings.valueType", "PERCENT")
                .accept(MediaType.APPLICATION_JSON));
        
        
        // assert
        List<Double> series0Values = Lists.newArrayList(
                (double) 4/10 * 100,
                (double) 4/7  * 100,
                (double) 0,
                (double) 9/16 * 100,
                (double) 1/1  * 100,
                (double) 0,
                (double) 0,
                (double) 0,
                (double) 1/3  * 100,
                (double) 0,
                (double) 0,
                (double) 2/2  * 100,
                (double) 0);
        
        List<Double> series1Values = Lists.newArrayList(
                (double) 6/10 * 100,
                (double) 3/7  * 100,
                (double) 0,
                (double) 7/16 * 100,
                (double) 1/1  * 100,
                (double) 0,
                (double) 0,
                (double) 0,
                (double) 3/3  * 100,
                (double) 0,
                (double) 0,
                (double) 1/2  * 100,
                (double) 0);
        
        assertMainChartSeriesDouble(0, result, ArrayUtils.toPrimitive(series0Values.toArray(new Double[0])));
        assertMainChartSeriesDouble(1, result,ArrayUtils.toPrimitive(series1Values.toArray(new Double[0])));
        
        assertMainChartXticksFor2000Year(result);
        
        double avgSeries0 = series0Values.stream().collect(Collectors.averagingDouble(x -> x));
        assertAggregatedChartSeriesDouble(0, result, 1, avgSeries0);
        
        double avgSeries1 = series1Values.stream().collect(Collectors.averagingDouble(x -> x));
        assertAggregatedChartSeriesDouble(1, result, 3, avgSeries1);
    }
    
    @Test
    public void generate_WithNumberPer1000YValues() throws Exception {
        // execute
        ResultActions result = mockMvc.perform(get(URL_BASE + "/generate")
                .param("seriesFilters[0].phrase", "phrase1")
                .param("seriesFilters[1].phrase", "phrase2")
                .param("globalFilter.judgmentDateRange.startMonth", "1")
                .param("globalFilter.judgmentDateRange.startYear", "2000")
                .param("globalFilter.judgmentDateRange.endMonth", "1")
                .param("globalFilter.judgmentDateRange.endYear", "2001")
                .param("ysettings.valueType", "NUMBER_PER_1000")
                .accept(MediaType.APPLICATION_JSON));
        
        
        // assert
        
        List<Double> series0Values = Lists.newArrayList(
                (double) 4 * 1000 / 10,
                (double) 4 * 1000 / 7,
                (double) 0,
                (double) 9 * 1000 / 16,
                (double) 1 * 1000 / 1,
                (double) 0,
                (double) 0,
                (double) 0,
                (double) 1 * 1000 / 3,
                (double) 0,
                (double) 0,
                (double) 2 * 1000 / 2,
                (double) 0);
        List<Double> series1Values = Lists.newArrayList(
                (double) 6 * 1000 / 10,
                (double) 3 * 1000 / 7,
                (double) 0,
                (double) 7 * 1000 / 16,
                (double) 1 * 1000 / 1,
                (double) 0,
                (double) 0,
                (double) 0,
                (double) 3 * 1000 / 3,
                (double) 0,
                (double) 0,
                (double) 1 * 1000 / 2,
                (double) 0);
        
        assertMainChartSeriesDouble(0, result, ArrayUtils.toPrimitive(series0Values.toArray(new Double[0])));
        assertMainChartSeriesDouble(1, result,ArrayUtils.toPrimitive(series1Values.toArray(new Double[0])));
        
        assertMainChartXticksFor2000Year(result);
        
        double avgSeries0 = series0Values.stream().collect(Collectors.averagingDouble(x -> x));
        assertAggregatedChartSeriesDouble(0, result, 1, avgSeries0);
        
        double avgSeries1 = series1Values.stream().collect(Collectors.averagingDouble(x -> x));
        assertAggregatedChartSeriesDouble(1, result, 3, avgSeries1);
    }
    
    @Test
    public void generate_ForCommonCourts() throws Exception {
        // execute
        ResultActions result = mockMvc.perform(get(URL_BASE + "/generate")
                .param("seriesFilters[0].phrase", "phrase1")
                .param("seriesFilters[1].phrase", "phrase2")
                .param("globalFilter.judgmentDateRange.startMonth", "1")
                .param("globalFilter.judgmentDateRange.startYear", "2000")
                .param("globalFilter.judgmentDateRange.endMonth", "1")
                .param("globalFilter.judgmentDateRange.endYear", "2001")
                .param("globalFilter.courtCriteria.courtType", "COMMON")
                .param("ysettings.valueType", "NUMBER")
                .accept(MediaType.APPLICATION_JSON));
        
        
        // assert
        
        assertMainChartSeries(0, result, 4, 0, 0, 6, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        assertMainChartSeries(1, result, 5, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        
        assertMainChartXticksFor2000Year(result);
        
        
        assertAggregatedChartSeries(0, result, 1, 10);
        assertAggregatedChartSeries(1, result, 3, 9);
        
        
        assertCcCourtChartSeries(0, result, 8, 2);
        assertCcCourtChartSeries(1, result, 8, 1);
        
        assertCcCourtChartXticks(result, appealCourt1, appealCourt2);
    }
    
    @Test
    public void generate_ForCommonCourt() throws Exception {
        // execute
        ResultActions result = mockMvc.perform(get(URL_BASE + "/generate")
                .param("seriesFilters[0].phrase", "phrase1")
                .param("seriesFilters[1].phrase", "phrase2")
                .param("globalFilter.judgmentDateRange.startMonth", "1")
                .param("globalFilter.judgmentDateRange.startYear", "2000")
                .param("globalFilter.judgmentDateRange.endMonth", "1")
                .param("globalFilter.judgmentDateRange.endYear", "2001")
                .param("globalFilter.courtCriteria.courtType", "COMMON")
                .param("globalFilter.courtCriteria.ccCourtId", String.valueOf(appealCourt1.getId()))
                .param("ysettings.valueType", "NUMBER")
                .accept(MediaType.APPLICATION_JSON));
        
        
        // assert
        
        assertMainChartSeries(0, result, 3, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        assertMainChartSeries(1, result, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        
        assertMainChartXticksFor2000Year(result);
        
        assertAggregatedChartSeries(0, result, 1, 5);
        assertAggregatedChartSeries(1, result, 3, 5);
    }
    
    @Test
    public void generate_ForCommonCourtWithDependent() throws Exception {
        // execute
        ResultActions result = mockMvc.perform(get(URL_BASE + "/generate")
                .param("seriesFilters[0].phrase", "phrase1")
                .param("seriesFilters[1].phrase", "phrase2")
                .param("globalFilter.judgmentDateRange.startMonth", "1")
                .param("globalFilter.judgmentDateRange.startYear", "2000")
                .param("globalFilter.judgmentDateRange.endMonth", "1")
                .param("globalFilter.judgmentDateRange.endYear", "2001")
                .param("globalFilter.courtCriteria.courtType", "COMMON")
                .param("globalFilter.courtCriteria.ccCourtId", String.valueOf(appealCourt1.getId()))
                .param("globalFilter.courtCriteria.ccIncludeDependentCourtJudgments", "true")
                .param("ysettings.valueType", "NUMBER")
                .accept(MediaType.APPLICATION_JSON));
        
        
        // assert
        
        assertMainChartSeries(0, result, 3, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        assertMainChartSeries(1, result, 5, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        
        assertMainChartXticksFor2000Year(result);
        
        assertAggregatedChartSeries(0, result, 1, 8);
        assertAggregatedChartSeries(1, result, 3, 8);
        
        assertCcCourtChartXticks(result, appealCourt1, regionalCourt1_2, regionalCourt1_1);
        assertCcCourtChartSeries(0, result, 5, 0, 3);
        assertCcCourtChartSeries(1, result, 5, 1, 2);
        
    }
    
    @Test
    public void generate_ForCommonCourtDivision() throws Exception {
        // execute
        ResultActions result = mockMvc.perform(get(URL_BASE + "/generate")
                .param("seriesFilters[0].phrase", "phrase1")
                .param("seriesFilters[1].phrase", "phrase2")
                .param("globalFilter.judgmentDateRange.startMonth", "1")
                .param("globalFilter.judgmentDateRange.startYear", "2000")
                .param("globalFilter.judgmentDateRange.endMonth", "1")
                .param("globalFilter.judgmentDateRange.endYear", "2001")
                .param("globalFilter.courtCriteria.courtType", "COMMON")
                .param("globalFilter.courtCriteria.ccCourtId", String.valueOf(appealCourt1.getId()))
                .param("globalFilter.courtCriteria.ccCourtDivisionId", String.valueOf(appealCourt1_division_1.getId()))
                .param("ysettings.valueType", "NUMBER")
                .accept(MediaType.APPLICATION_JSON));
        
        
        // assert
        
        assertMainChartSeries(0, result, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        assertMainChartSeries(1, result, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        
        assertMainChartXticksFor2000Year(result);
        
        assertAggregatedChartSeries(0, result, 1, 2);
        assertAggregatedChartSeries(1, result, 3, 2);
        
    }
    
    @Test
    public void generate_ForSupremeCourtChamber() throws Exception {
        // execute
        ResultActions result = mockMvc.perform(get(URL_BASE + "/generate")
                .param("seriesFilters[0].phrase", "phrase1")
                .param("seriesFilters[1].phrase", "phrase2")
                .param("globalFilter.judgmentDateRange.startMonth", "1")
                .param("globalFilter.judgmentDateRange.startYear", "2000")
                .param("globalFilter.judgmentDateRange.endMonth", "1")
                .param("globalFilter.judgmentDateRange.endYear", "2001")
                .param("globalFilter.courtCriteria.courtType", "SUPREME")
                .param("globalFilter.courtCriteria.scCourtChamberId", String.valueOf(scChamber_1.getId()))
                .param("ysettings.valueType", "NUMBER")
                .accept(MediaType.APPLICATION_JSON));
        
        
        // assert
        
        assertMainChartSeries(0, result, 0, 3, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        assertMainChartSeries(1, result, 1, 3, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        
        assertMainChartXticksFor2000Year(result);
        
        assertAggregatedChartSeries(0, result, 1, 5);
        assertAggregatedChartSeries(1, result, 3, 5);
    }
    
    @Test
    public void generate_ForSupremeCourtChamberDivision() throws Exception {
        // execute
        ResultActions result = mockMvc.perform(get(URL_BASE + "/generate")
                .param("seriesFilters[0].phrase", "phrase1")
                .param("seriesFilters[1].phrase", "phrase2")
                .param("globalFilter.judgmentDateRange.startMonth", "1")
                .param("globalFilter.judgmentDateRange.startYear", "2000")
                .param("globalFilter.judgmentDateRange.endMonth", "1")
                .param("globalFilter.judgmentDateRange.endYear", "2001")
                .param("globalFilter.courtCriteria.courtType", "SUPREME")
                .param("globalFilter.courtCriteria.scCourtChamberId", String.valueOf(scChamber_1.getId()))
                .param("globalFilter.courtCriteria.scCourtChamberDivisionId", String.valueOf(scChamber_1_division_1.getId()))
                .param("ysettings.valueType", "NUMBER")
                .accept(MediaType.APPLICATION_JSON));
        
        
        // assert
        
        assertMainChartSeries(0, result, 0, 2, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        assertMainChartSeries(1, result, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        
        assertMainChartXticksFor2000Year(result);
        
        assertAggregatedChartSeries(0, result, 1, 3);
        assertAggregatedChartSeries(1, result, 3, 2);
    }
    
    @Test
    public void generateCsv_MAIN_CHART() throws Exception {
        // execute
        ResultActions result = mockMvc.perform(get(URL_BASE + "/generateCsv")
                .param("seriesFilters[0].phrase", "phrase1")
                .param("seriesFilters[1].phrase", "phrase2")
                .param("globalFilter.judgmentDateRange.startMonth", "1")
                .param("globalFilter.judgmentDateRange.startYear", "2000")
                .param("globalFilter.judgmentDateRange.endMonth", "1")
                .param("globalFilter.judgmentDateRange.endYear", "2001")
                .param("chartCode", "MAIN_CHART"));
        
        
        // assert
        
        String expectedCsvContent = 
                buildCsvHeader("1/2000", "2/2000", "3/2000", "4/2000", "5/2000", "6/2000", "7/2000", "8/2000", "9/2000", "10/2000", "11/2000", "12/2000", "1/2001") + "\n"
                + buildCsvSeriesLine(4, 4, 0, 9, 1, 0, 0, 0, 1, 0, 0, 2, 0) + "\n"
                + buildCsvSeriesLine(6, 3, 0, 7, 1, 0, 0, 0, 3, 0, 0, 1, 0) + "\n";
        
        result.andExpect(content().string(expectedCsvContent));
    }

    @Test
    public void generateCsv_CC_COURT_CHART() throws Exception {
        // execute
        ResultActions result = mockMvc.perform(get(URL_BASE + "/generateCsv")
                .param("seriesFilters[0].phrase", "phrase1")
                .param("seriesFilters[1].phrase", "phrase2")
                .param("globalFilter.courtCriteria.courtType", "COMMON")
                .param("chartCode", "CC_COURT_CHART"));

        
        // assert
        
        String expectedCsvContent = 
                buildCsvHeader(appealCourt1.getName(), appealCourt2.getName()) + "\n"
                + buildCsvSeriesLine(8, 2) + "\n"
                + buildCsvSeriesLine(8, 1) + "\n";
        
        result.andExpect(content().string(expectedCsvContent));
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private void assertMainChartSeries(int seriesNumber, ResultActions result, int ... counts) throws Exception {
        
        for (int i=0; i<counts.length; ++i) {
            result.andExpect(jsonPath("$.charts.MAIN_CHART.seriesList.[%d].points.[%d]", seriesNumber, i).value(contains(i, counts[i])));
        }
    }
    
    private void assertMainChartSeriesDouble(int seriesNumber, ResultActions result, double ... numbers) throws Exception {
        
        for (int i=0; i<numbers.length; ++i) {
            result
                .andExpect(jsonPath("$.charts.MAIN_CHART.seriesList.[%d].points.[%d].[0]", seriesNumber, i).value(i))
                .andExpect(jsonPath("$.charts.MAIN_CHART.seriesList.[%d].points.[%d].[1]", seriesNumber, i).value(closeTo(numbers[i], 0.0001)));
        }
    }
    
    private void assertCcCourtChartSeries(int seriesNumber, ResultActions result, int ... counts) throws Exception {
        
        for (int i=0; i<counts.length; ++i) {
            result.andExpect(jsonPath("$.charts.CC_COURT_CHART.seriesList.[%d].points.[%d]", seriesNumber, i).value(contains(i, counts[i])));
        }
    }
    
    private void assertAggregatedChartSeries(int seriesNumber, ResultActions result, int pointNumber, int count) throws Exception {
        
        result.andExpect(jsonPath("$.charts.AGGREGATED_MAIN_CHART.seriesList.[%d].points.[0]", seriesNumber).value(contains(pointNumber, count)));
        
    }
    
    private void assertAggregatedChartSeriesDouble(int seriesNumber, ResultActions result, int pointNumber, double number) throws Exception {
        
        result
            .andExpect(jsonPath("$.charts.AGGREGATED_MAIN_CHART.seriesList.[%d].points.[0].[0]", seriesNumber).value(pointNumber))
            .andExpect(jsonPath("$.charts.AGGREGATED_MAIN_CHART.seriesList.[%d].points.[0].[1]", seriesNumber).value(closeTo(number, 0.0001)));
        
    }
    
    private void assertMainChartXticksFor2000Year(ResultActions result) throws Exception {
        assertMainChartMonthPeriodXticks(result,
                new MonthPeriod(2000, 1, 2000, 1),
                new MonthPeriod(2000, 2, 2000, 2),
                new MonthPeriod(2000, 3, 2000, 3),
                new MonthPeriod(2000, 4, 2000, 4),
                new MonthPeriod(2000, 5, 2000, 5),
                new MonthPeriod(2000, 6, 2000, 6),
                new MonthPeriod(2000, 7, 2000, 7),
                new MonthPeriod(2000, 8, 2000, 8),
                new MonthPeriod(2000, 9, 2000, 9),
                new MonthPeriod(2000, 10, 2000, 10),
                new MonthPeriod(2000, 11, 2000, 11),
                new MonthPeriod(2000, 12, 2000, 12),
                new MonthPeriod(2001, 1, 2001, 1));
    }
    
    private void assertMainChartYearPeriodXticks(ResultActions result, YearPeriod ... periods) throws Exception {
        String jsonBasePath = "$.charts.MAIN_CHART.xticks";
        
        for (int i=0; i<periods.length; ++i) {
            result
                .andExpect(jsonPath(jsonBasePath +".[%d].[0]", i).value(i))
                .andExpect(jsonPath(jsonBasePath +".[%d].[1].startYear", i).value(periods[i].getStartYear()))
                .andExpect(jsonPath(jsonBasePath +".[%d].[1].endYear", i).value(periods[i].getEndYear()))
                .andExpect(jsonPath(jsonBasePath +".[%d].[1].period", i).value("YEAR"))
            ;
        }
    }
    
    private void assertMainChartMonthPeriodXticks(ResultActions result, MonthPeriod ... periods) throws Exception {
        String jsonBasePath = "$.charts.MAIN_CHART.xticks";
        
        for (int i=0; i<periods.length; ++i) {
            result
                .andExpect(jsonPath(jsonBasePath +".[%d].[0]", i).value(i))
                .andExpect(jsonPath(jsonBasePath +".[%d].[1].startYear", i).value(periods[i].getStartYear()))
                .andExpect(jsonPath(jsonBasePath +".[%d].[1].startMonthOfYear", i).value(periods[i].getStartMonthOfYear()))
                .andExpect(jsonPath(jsonBasePath +".[%d].[1].endYear", i).value(periods[i].getEndYear()))
                .andExpect(jsonPath(jsonBasePath +".[%d].[1].endMonthOfYear", i).value(periods[i].getEndMonthOfYear()))
                .andExpect(jsonPath(jsonBasePath +".[%d].[1].period", i).value("MONTH"))
            ;
        }
    }
    
    private void assertMainChartDayPeriodXticks(ResultActions result, DayPeriod ... periods) throws Exception {
        String jsonBasePath = "$.charts.MAIN_CHART.xticks";
        
        for (int i=0; i<periods.length; ++i) {
            result
                .andExpect(jsonPath(jsonBasePath +".[%d].[0]", i).value(i))
                .andExpect(jsonPath(jsonBasePath +".[%d].[1].startDay.year", i).value(periods[i].getStartDay().getYear()))
                .andExpect(jsonPath(jsonBasePath +".[%d].[1].startDay.monthOfYear", i).value(periods[i].getStartDay().getMonthOfYear()))
                .andExpect(jsonPath(jsonBasePath +".[%d].[1].startDay.dayOfMonth", i).value(periods[i].getStartDay().getDayOfMonth()))
                .andExpect(jsonPath(jsonBasePath +".[%d].[1].endDay.year", i).value(periods[i].getEndDay().getYear()))
                .andExpect(jsonPath(jsonBasePath +".[%d].[1].endDay.monthOfYear", i).value(periods[i].getEndDay().getMonthOfYear()))
                .andExpect(jsonPath(jsonBasePath +".[%d].[1].endDay.dayOfMonth", i).value(periods[i].getEndDay().getDayOfMonth()))
                .andExpect(jsonPath(jsonBasePath +".[%d].[1].period", i).value("DAY"))
            ;
        }
    }
    
    private void assertCcCourtChartXticks(ResultActions result, CommonCourt ... commonCourts) throws Exception {
        String jsonBasePath = "$.charts.CC_COURT_CHART.xticks";
        
        for (int i=0; i<commonCourts.length; ++i) {
            result
                .andExpect(jsonPath(jsonBasePath +".[%d].[0]", i).value(i))
                .andExpect(jsonPath(jsonBasePath +".[%d].[1].courtId", i).value(equalsLong(commonCourts[i].getId())))
                .andExpect(jsonPath(jsonBasePath +".[%d].[1].name", i).value(commonCourts[i].getName()))
            ;
        }
    }
    
    
    private JudgmentSeriesFilter createJudgmentSeriesFilter(String phrase) {
        JudgmentSeriesFilter judgmentSeriesFilter = new JudgmentSeriesFilter();
        judgmentSeriesFilter.setPhrase(phrase);
        return judgmentSeriesFilter;
    }
    
    @Transactional
    private void generateCommonCourts() {
        
        appealCourt1 = createCommonCourt("Sąd Apelacyjny w Warszawie", "0001", CommonCourtType.APPEAL, null);
        appealCourt1_division_1 = createCcDivision("I Wydział Cywilny", "001", appealCourt1);
        appealCourt1_division_2 = createCcDivision("II Wydział Karny", "002", appealCourt1);
        appealCourt1_division_3 = createCcDivision("III Wydział Pracy i Ubezpieczeń Społecznych", "003", appealCourt1);
        
        
        regionalCourt1_1 = createCommonCourt("Sąd Okręgowy w Warszawie", "0011", CommonCourtType.REGIONAL, appealCourt1);
        
        districtCourt1_1_1 = createCommonCourt("Sąd Rejonowy dla Warszawy-Mokotowa w Warszawie", "0111", CommonCourtType.DISTRICT, regionalCourt1_1);
        districtCourt1_1_2 = createCommonCourt("Sąd Rejonowy w Piasecznie", "0211", CommonCourtType.DISTRICT, regionalCourt1_1);
        
        regionalCourt1_2 = createCommonCourt("Sąd Okręgowy Warszawa-Praga w Warszawie", "0021", CommonCourtType.REGIONAL, appealCourt1);
        
        appealCourt2 = createCommonCourt("Sąd Apelacyjny we Wrocławiu", "0002", CommonCourtType.APPEAL, null);
        regionalCourt2_1 = createCommonCourt("Sąd Okręgowy w Legnicy", "0012", CommonCourtType.REGIONAL, appealCourt2);
        
        commonCourtRepository.save(Lists.newArrayList(
                appealCourt1, regionalCourt1_1, districtCourt1_1_1, districtCourt1_1_2, regionalCourt1_2,
                appealCourt2, regionalCourt2_1));
        
        commonCourtDivisionRepository.save(Lists.newArrayList(appealCourt1_division_1, appealCourt1_division_2, appealCourt1_division_3));
        
    }
    
    private CommonCourt createCommonCourt(String name, String code, CommonCourtType commonCourtType, CommonCourt parent) {
        CommonCourt court = new CommonCourt();
        court.setName(name);
        court.setCode(code);
        court.setType(commonCourtType);
        court.setParentCourt(parent);
        return court;
    }
    
    private CommonCourtDivision createCcDivision(String name, String code, CommonCourt court) {
        CommonCourtDivision division = new CommonCourtDivision();
        division.setName(name);
        division.setCode(code);
        division.setCourt(court);
        return division;
    }
    
    private String buildCsvHeader(String ... headerValues) {
        return Lists.newArrayList(headerValues).stream().collect(Collectors.joining(";"));
    }
    
    private String buildCsvSeriesLine(Integer ... counts) {
        return Arrays.asList(counts).stream().map(x -> String.valueOf(x)).collect(Collectors.joining(";"));
    }
    
    @Transactional
    private void generateSupremeCourtChambers() {
        scChamber_1 = createSupremeCourtChamber("Izba Cywilna");
        scChamber_1_division_1 = createSupremeCourtChamberDivision("Wydział I", "Izba Cywilna - Wydział I", scChamber_1);
        scChamber_1_division_2 = createSupremeCourtChamberDivision("Wydział II", "Izba Cywilna - Wydział II", scChamber_1);
        
        scChamber_2 = createSupremeCourtChamber("Izba Karna");
        scChamber_2_division_1 = createSupremeCourtChamberDivision("Wydział I", "Izba Karna - Wydział I", scChamber_2);
        
        scChamberRepository.save(Lists.newArrayList(scChamber_1, scChamber_2));
        scChamberDivisionRepository.save(Lists.newArrayList(scChamber_1_division_1, scChamber_1_division_2, scChamber_2_division_1));
    }
    
    private SupremeCourtChamber createSupremeCourtChamber(String name) {
        SupremeCourtChamber chamber = new SupremeCourtChamber();
        chamber.setName(name);
        return chamber;
    }
    
    private SupremeCourtChamberDivision createSupremeCourtChamberDivision(String name, String fullName, SupremeCourtChamber chamber) {
        SupremeCourtChamberDivision division = new SupremeCourtChamberDivision();
        division.setName(name);
        division.setFullName(fullName);
        division.setScChamber(chamber);
        return division;
    }
    
    private SimpleCommonCourt createSimpleCommonCourt(CommonCourt commonCourt) {
        SimpleCommonCourt simpleCommonCourt = new SimpleCommonCourt();
        simpleCommonCourt.setId(commonCourt.getId());
        simpleCommonCourt.setName(commonCourt.getName());
        simpleCommonCourt.setType(commonCourt.getType());
        return simpleCommonCourt;
    }
    
    private SimpleEntity createSimpleEntity(CommonCourtDivision ccDivision) {
        SimpleEntity simpleEntity = new SimpleEntity();
        simpleEntity.setId(ccDivision.getId());
        simpleEntity.setName(ccDivision.getName());
        return simpleEntity;
    }
    
    private SimpleEntity createSimpleEntity(SupremeCourtChamber scChamber) {
        SimpleEntity simpleEntity = new SimpleEntity();
        simpleEntity.setId(scChamber.getId());
        simpleEntity.setName(scChamber.getName());
        return simpleEntity;
    }
    
    private SimpleEntity createSimpleEntity(SupremeCourtChamberDivision scDivision) {
        SimpleEntity simpleEntity = new SimpleEntity();
        simpleEntity.setId(scDivision.getId());
        simpleEntity.setName(scDivision.getName());
        return simpleEntity;
    }
    
    private void indexJudgments() throws SolrServerException, IOException {
        
        judgmentsServer.add(fetchDocument(19910503L, "1991-05-03T00:00:00Z", "phrase1"));
        judgmentsServer.add(fetchDocument(19910505L, "1991-05-05T00:00:00Z", "phrase1"));
        judgmentsServer.add(fetchDocument(19920910L, "1992-09-10T00:00:00Z", "phrase2"));
        judgmentsServer.add(fetchDocument(19950510L, "1995-05-15T00:00:00Z", "phrase2"));
        judgmentsServer.add(fetchDocument(19960305L, "1996-03-05T00:00:00Z", "phrase1"));
        judgmentsServer.add(fetchDocument(19960404L, "1996-04-04T00:00:00Z", "phrase2"));
        judgmentsServer.add(fetchDocument(19971130L, "1997-11-30T00:00:00Z", "phrase2"));
        judgmentsServer.add(fetchDocument(19981221L, "1998-12-21T00:00:00Z", "phrase2"));
        judgmentsServer.add(fetchDocument(19990108L, "1999-01-08T00:00:00Z", "phrase1"));
        judgmentsServer.add(fetchDocument(19990305L, "1999-03-05T00:00:00Z", "phrase1"));
        judgmentsServer.add(fetchDocument(19990805L, "1999-08-05T00:00:00Z", "phrase2"));
        
        judgmentsServer.add(fetchDocument(103L, "2000-01-03T00:00:00Z", "phrase1", appealCourt1, appealCourt1_division_1));
        judgmentsServer.add(fetchDocument(105L, "2000-01-05T00:00:00Z", "phrase2", appealCourt1, appealCourt1_division_1));
        judgmentsServer.add(fetchDocument(107L, "2000-01-07T00:00:00Z", "phrase2", appealCourt1, appealCourt1_division_2));
        judgmentsServer.add(fetchDocument(121L, "2000-01-21T00:00:00Z", "phrase3", appealCourt1, appealCourt1_division_1));
        judgmentsServer.add(fetchDocument(125L, "2000-01-25T00:00:00Z", "phrase1", appealCourt1, appealCourt1_division_1));
        judgmentsServer.add(fetchDocument(126L, "2000-01-26T00:00:00Z", "phrase2 phrase3", appealCourt1, appealCourt1_division_2));
        judgmentsServer.add(fetchDocument(129L, "2000-01-29T00:00:00Z", "phrase1 phrase2", appealCourt1, appealCourt1_division_3));
        judgmentsServer.add(fetchDocument(130L, "2000-01-30T00:00:00Z", "phrase2", appealCourt1, appealCourt1_division_1));
        judgmentsServer.add(fetchDocument(1311L, "2000-01-31T00:00:00Z", "phrase1", appealCourt2, null));
        judgmentsServer.add(fetchDocument(1312L, "2000-01-31T00:00:00Z", "phrase2", scChamber_1, scChamber_1_division_2));
        
        judgmentsServer.add(fetchDocument(204L, "2000-02-04T00:00:00Z", "phrase1", scChamber_1, scChamber_1_division_1));
        judgmentsServer.add(fetchDocument(222L, "2000-02-22T00:00:00Z", "phrase1", scChamber_1, scChamber_1_division_2));
        judgmentsServer.add(fetchDocument(223L, "2000-02-23T00:00:00Z", "phrase1", scChamber_2, scChamber_2_division_1));
        judgmentsServer.add(fetchDocument(224L, "2000-02-24T00:00:00Z", "phrase1", scChamber_1, scChamber_1_division_1));
        judgmentsServer.add(fetchDocument(225L, "2000-02-25T00:00:00Z", "phrase2", scChamber_1, scChamber_1_division_1));
        judgmentsServer.add(fetchDocument(226L, "2000-02-26T00:00:00Z", "phrase2", scChamber_1, scChamber_1_division_1));
        judgmentsServer.add(fetchDocument(227L, "2000-02-27T00:00:00Z", "phrase2", scChamber_1, scChamber_1_division_2));
        
        judgmentsServer.add(fetchDocument(403L, "2000-04-03T00:00:00Z", "phrase1", appealCourt1, null));
        judgmentsServer.add(fetchDocument(404L, "2000-04-04T00:00:00Z", "phrase1", appealCourt1, null));
        judgmentsServer.add(fetchDocument(405L, "2000-04-05T00:00:00Z", "phrase2", appealCourt2, null));
        judgmentsServer.add(fetchDocument(406L, "2000-04-06T00:00:00Z", "phrase1", regionalCourt1_1, null));
        judgmentsServer.add(fetchDocument(407L, "2000-04-07T00:00:00Z", "phrase2", regionalCourt1_1, null));
        judgmentsServer.add(fetchDocument(408L, "2000-04-08T00:00:00Z", "phrase2", regionalCourt1_2, null));
        judgmentsServer.add(fetchDocument(409L, "2000-04-09T00:00:00Z", "phrase1", regionalCourt2_1, null));
        judgmentsServer.add(fetchDocument(410L, "2000-04-10T00:00:00Z", "phrase1", districtCourt1_1_1, null));
        judgmentsServer.add(fetchDocument(411L, "2000-04-11T00:00:00Z", "phrase1", districtCourt1_1_1, null));
        judgmentsServer.add(fetchDocument(412L, "2000-04-12T00:00:00Z", "phrase2", districtCourt1_1_2, null));
        judgmentsServer.add(fetchDocument(415L, "2000-04-15T00:00:00Z", "phrase1", scChamber_1, scChamber_1_division_1));
        judgmentsServer.add(fetchDocument(416L, "2000-04-16T00:00:00Z", "phrase1", scChamber_1, scChamber_1_division_2));
        judgmentsServer.add(fetchDocument(417L, "2000-04-17T00:00:00Z", "phrase2", scChamber_1, scChamber_1_division_2));
        judgmentsServer.add(fetchDocument(418L, "2000-04-18T00:00:00Z", "phrase2", scChamber_2, scChamber_2_division_1));
        judgmentsServer.add(fetchDocument(419L, "2000-04-19T00:00:00Z", "phrase2", scChamber_2, scChamber_2_division_1));
        judgmentsServer.add(fetchDocument(420L, "2000-04-20T00:00:00Z", "phrase1", scChamber_2, scChamber_2_division_1));
        
        judgmentsServer.add(fetchDocument(504L, "2000-05-04T00:00:00Z", "phrase1 phrase2 phrase3"));
        
        judgmentsServer.add(fetchDocument(909L, "2000-09-09T00:00:00Z", "phrase1 phrase2"));
        judgmentsServer.add(fetchDocument(9301L, "2000-09-30T00:00:00Z", "phrase2"));
        judgmentsServer.add(fetchDocument(9302L, "2000-09-30T00:00:00Z", "phrase2"));
        
        judgmentsServer.add(fetchDocument(1223L, "2000-12-23T00:00:00Z", "phrase1"));
        judgmentsServer.add(fetchDocument(1227L, "2000-12-27T00:00:00Z", "phrase1 phrase2"));
        
        
        judgmentsServer.commit();
    }
    
    
    
    private SolrInputDocument fetchDocument(long id, String judgmentDateString, String content) {
        SolrInputDocument doc = new SolrInputDocument();
        
        doc.addField("databaseId", id);
        doc.addField("judgmentDate", judgmentDateString);
        doc.addField("content", content);
        
        return doc;
    }
    
    private SolrInputDocument fetchDocument(long id, String judgmentDateString, String content, CommonCourt court, CommonCourtDivision division) {
        SolrInputDocument doc = fetchDocument(id, judgmentDateString, content);
        fillCommonCourtFields(doc, court, division);
        
        return doc;
    }
    
    private SolrInputDocument fetchDocument(long id, String judgmentDateString, String content, SupremeCourtChamber chamber, SupremeCourtChamberDivision division) {
        SolrInputDocument doc = fetchDocument(id, judgmentDateString, content);
        fillSupremeCourtFields(doc, chamber, division);
        
        return doc;
    }
    
    private void fillCommonCourtFields(SolrInputDocument doc, CommonCourt court, CommonCourtDivision division) {
        doc.addField(JudgmentIndexField.COURT_TYPE.getFieldName(), "COMMON");
        if (court != null) {
            doc.addField(JudgmentIndexField.CC_COURT_ID.getFieldName(), court.getId());
            
            doc.addField(JudgmentIndexField.CC_APPEAL_COURT_ID.getFieldName(), court.getAppealCourt().getId());
            doc.addField(JudgmentIndexField.CC_APPEAL_AREA.getFieldName(), ccCourtAreaFieldValueCreator.createCcCourtAreaFieldValue(null, court.getAppealCourt()));
            
            if (court.isAppealCourt()) {
                doc.addField(CC_REGION_AREA.getFieldName(), ccCourtAreaFieldValueCreator.createCcCourtAreaFieldValue(court.getId(), court));
            }
            
            if (court.isRegionalCourt()) {
                doc.addField(CC_REGIONAL_COURT_ID.getFieldName(), court.getId());
                doc.addField(CC_REGION_AREA.getFieldName(), ccCourtAreaFieldValueCreator.createCcCourtAreaFieldValue(court.getAppealCourt().getId(), court.getRegionalCourt()));
                doc.addField(CC_DISTRICT_AREA.getFieldName(), ccCourtAreaFieldValueCreator.createCcCourtAreaFieldValue(court.getId(), court));
            }
            
            if (court.isDistrictCourt()) {
                doc.addField(CC_REGIONAL_COURT_ID.getFieldName(), court.getRegionalCourt().getId());
                doc.addField(CC_REGION_AREA.getFieldName(), ccCourtAreaFieldValueCreator.createCcCourtAreaFieldValue(court.getAppealCourt().getId(), court.getRegionalCourt()));
                doc.addField(CC_DISTRICT_COURT_ID.getFieldName(), court.getId());
                doc.addField(CC_DISTRICT_AREA.getFieldName(), ccCourtAreaFieldValueCreator.createCcCourtAreaFieldValue(court.getRegionalCourt().getId(), court));
            }
        }
        if (division != null) {
            doc.addField(JudgmentIndexField.CC_COURT_DIVISION_ID.getFieldName(), division.getId());
        }
    }
    
    private void fillSupremeCourtFields(SolrInputDocument doc, SupremeCourtChamber chamber, SupremeCourtChamberDivision division) {
        doc.addField(JudgmentIndexField.COURT_TYPE.getFieldName(), "SUPREME");
        if (chamber != null) {
            doc.addField(JudgmentIndexField.SC_COURT_CHAMBER_ID.getFieldName(), chamber.getId());
        }
        if (division != null) {
            doc.addField(JudgmentIndexField.SC_COURT_DIVISION_ID.getFieldName(), division.getId());
        }
    }
}
