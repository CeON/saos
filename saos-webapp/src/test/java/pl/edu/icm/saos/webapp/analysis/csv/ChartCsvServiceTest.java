package pl.edu.icm.saos.webapp.analysis.csv;

import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import pl.edu.icm.saos.common.chart.Chart;
import pl.edu.icm.saos.webapp.analysis.generator.ChartGenerator;
import pl.edu.icm.saos.webapp.analysis.request.AnalysisForm;
import pl.edu.icm.saos.webapp.analysis.result.ChartCode;

/**
 * @author Łukasz Dumiszewski
 */

public class ChartCsvServiceTest {

    
    @InjectMocks
    private ChartCsvService chartCsvService;
    
    @Mock
    private ChartGenerator chartGenerator;
    
    @Mock
    private ChartCsvExporter chartCsvExporter;
    
    
    @Before
    public void before() {
        
        MockitoAnnotations.initMocks(this);
    
    }
    
    
    //------------------------ TESTS --------------------------
    
    @SuppressWarnings("unchecked")
    @Test
    public void generateChartCsv() throws IOException {
        
        
        // given
        
        ChartCode chartCode = ChartCode.MAIN_CHART;
        AnalysisForm analysisForm = mock(AnalysisForm.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        Locale locale = Locale.CANADA_FRENCH;
        
        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);
        
        Chart<Object, Number> chart = mock(Chart.class);
        when(chartGenerator.generateChart(chartCode, analysisForm)).thenReturn(chart);
        
        
        
        // execute
        
        chartCsvService.generateChartCsv(chartCode, analysisForm, locale, response);
        
        
        // assert
        
        verify(chartCsvExporter).exportChartToCsv(same(chart), eq(chartCode), same(analysisForm), eq(locale), same(writer));
        
        verify(response).flushBuffer();
    }
    
}
