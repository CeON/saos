package pl.edu.icm.saos.webapp.analysis.csv;

import java.io.IOException;
import java.io.Writer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import au.com.bytecode.opencsv.CSVWriter;
import pl.edu.icm.saos.common.chart.Chart;
import pl.edu.icm.saos.webapp.analysis.request.AnalysisForm;
import pl.edu.icm.saos.webapp.analysis.result.ChartCode;

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
    public void exportChartToCsv(Chart<Object, Number> chart, ChartCode chartCode, AnalysisForm analysisForm, Writer writer) throws IOException {
        
        CSVWriter csvWriter = createCsvWriter(writer);
        
        try {
            
            csvWriter.writeNext(chartCsvGenerator.generateHeader(chartCode, analysisForm));
            
            
            int rowCount = chart.getSeriesList().get(0).getPoints().size();
            
            for (int i=0; i<rowCount; ++i) {
                csvWriter.writeNext(chartCsvGenerator.generateRow(chart, i));
            }
            
        } finally {
        
            csvWriter.close();
        
        }
    }

    
    
    //------------------------ PRIVATE --------------------------

    CSVWriter createCsvWriter(Writer writer) {
        return new CSVWriter(writer, fieldSeparator, quoteCharacter);
    }


    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setChartCsvGenerator(ChartCsvGenerator chartCsvGenerator) {
        this.chartCsvGenerator = chartCsvGenerator;
    }
}
