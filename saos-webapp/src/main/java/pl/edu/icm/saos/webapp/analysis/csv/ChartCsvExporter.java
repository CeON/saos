package pl.edu.icm.saos.webapp.analysis.csv;

import java.io.IOException;
import java.io.Writer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.common.chart.Chart;
import pl.edu.icm.saos.common.chart.Series;
import au.com.bytecode.opencsv.CSVWriter;

/**
 * A {@link Chart} to csv format exporting service
 * 
 * @author Łukasz Dumiszewski
 */
@Service("chartCsvExporter")
public class ChartCsvExporter {

    private ChartCsvGenerator chartCsvGenerator;
    
    private char fieldSeparator = ';'; 
    
    private char quoteCharacter = CSVWriter.NO_QUOTE_CHARACTER;
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Converts the given chart to csv and writes the csv to the given writer
     * @throws IOException in case of I/O Error during writing to the writer
     */
    public void exportChartToCsv(Chart<Object, Number> chart, Writer writer) throws IOException {
        
        CSVWriter csvWriter = new CSVWriter(writer, fieldSeparator, quoteCharacter);
        
        try {
            csvWriter.writeNext(chartCsvGenerator.generateHeader(chart));
            
            for (Series<Object, Number> series : chart.getSeriesList()) {
                
                csvWriter.writeNext(chartCsvGenerator.generateRow(series));
            }
            
        } finally {
        
            csvWriter.close();
        
        }
    }


    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setChartCsvGenerator(ChartCsvGenerator chartCsvGenerator) {
        this.chartCsvGenerator = chartCsvGenerator;
    }
}
