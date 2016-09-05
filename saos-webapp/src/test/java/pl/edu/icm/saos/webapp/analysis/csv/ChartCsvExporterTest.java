package pl.edu.icm.saos.webapp.analysis.csv;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.IOException;
import java.io.Writer;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import pl.edu.icm.saos.common.chart.Chart;
import pl.edu.icm.saos.common.chart.Series;
import pl.edu.icm.saos.webapp.analysis.request.AnalysisForm;
import pl.edu.icm.saos.webapp.analysis.result.ChartCode;
import au.com.bytecode.opencsv.CSVWriter;

/**
 * @author Łukasz Dumiszewski
 */

public class ChartCsvExporterTest {

    @InjectMocks
    private ChartCsvExporter chartCsvExporter = spy(new ChartCsvExporter());
    
    @Mock
    private ChartCsvGenerator chartCsvGenerator;
    
    @Mock
    private CSVWriter csvWriter;
    
    
    @Before
    public void before() {
        
        initMocks(this);
    }
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    @SuppressWarnings("unchecked")
    public void exportChartToCsv() throws IOException {
        
        // given
        
        Writer writer = mock(Writer.class);
        doReturn(csvWriter).when(chartCsvExporter).createCsvWriter(writer); 
        
        Series<Object, Number> series1 = new Series<>();
        series1.addPoint("x1", 4);
        series1.addPoint("x2", 5);
        Series<Object, Number> series2 = mock(Series.class);
        
        Chart<Object, Number> chart = new Chart<>(Lists.newArrayList(series1, series2));
        
        ChartCode chartCode = ChartCode.MAIN_CHART;
        AnalysisForm analysisForm = mock(AnalysisForm.class);
        
        
        String[] header = new String[]{"Y", "A", "B"};
        String[] row1 = new String[]{"2012", "44", "55"};
        String[] row2 = new String[]{"2013", "45", "56"};
        
        when(chartCsvGenerator.generateHeader(chartCode, analysisForm)).thenReturn(header);
        when(chartCsvGenerator.generateRow(chart, 0)).thenReturn(row1);
        when(chartCsvGenerator.generateRow(chart, 1)).thenReturn(row2);
        
        
        // execute
        
        chartCsvExporter.exportChartToCsv(chart, chartCode, analysisForm, writer);
        
        
        // assert
        
        verify(csvWriter).writeNext(header);
        verify(csvWriter).writeNext(row1);
        verify(csvWriter).writeNext(row2);
        verify(csvWriter).close();
        verifyNoMoreInteractions(csvWriter);
    }
    
    
}
