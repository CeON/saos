package pl.edu.icm.saos.webapp.analysis.csv;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
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
        
        Chart<Object, Number> chart = mock(Chart.class);
        Series<Object, Number> series1 = mock(Series.class);
        Series<Object, Number> series2 = mock(Series.class);
        when(chart.getSeriesList()).thenReturn(Lists.newArrayList(series1, series2));
        
        String[] header = new String[]{"A","B"};
        String[] row1 = new String[]{"44", "55"};
        String[] row2 = new String[]{"45", "56"};
        
        when(chartCsvGenerator.generateHeader(chart)).thenReturn(header);
        when(chartCsvGenerator.generateRow(series1)).thenReturn(row1);
        when(chartCsvGenerator.generateRow(series2)).thenReturn(row2);
        
        
        // execute
        
        chartCsvExporter.exportChartToCsv(chart, writer);
        
        
        // assert
        
        verify(csvWriter).writeNext(header);
        verify(csvWriter).writeNext(row1);
        verify(csvWriter).writeNext(row2);
        verify(csvWriter).close();
        verifyNoMoreInteractions(csvWriter);
    }
    
    
}
