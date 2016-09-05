package pl.edu.icm.saos.webapp.analysis.csv;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.common.chart.Chart;
import pl.edu.icm.saos.webapp.analysis.generator.ChartGenerator;
import pl.edu.icm.saos.webapp.analysis.request.AnalysisForm;
import pl.edu.icm.saos.webapp.analysis.result.ChartCode;

/**
 * 
 * A service for generating charts in csv form
 * 
 * @author Łukasz Dumiszewski
 */
@Service("chartCsvService")
public class ChartCsvService {

    private ChartGenerator chartGenerator;
    
    private ChartCsvExporter chartCsvExporter;
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Generates csv representation of the chart generated for the given chartCode and analysisForm. Writes
     * the generated csv and appropriate http headers to the passed response object.
     * @throws IOException in case of an IO error during writing to the response object
     */
    public void generateChartCsv(ChartCode chartCode, AnalysisForm analysisForm, HttpServletResponse response) throws IOException {
        
        createHttpHeaders(chartCode, response);
        
        Chart<Object, Number> chart = chartGenerator.generateChart(chartCode, analysisForm);
        
        chartCsvExporter.exportChartToCsv(chart, chartCode, analysisForm, response.getWriter());
        
        response.flushBuffer();
    }


    //------------------------ PRIVATE --------------------------
    
    private void createHttpHeaders(ChartCode chartCode, HttpServletResponse response) {
        response.setContentType("text/plain");      
        String fileName = chartCode.name().toLowerCase() + ".csv";
        response.setHeader("Content-Disposition", "attachment; filename="+fileName);
        response.setCharacterEncoding("UTF-8");
    }

    
    //------------------------ SETTERS --------------------------
    
    
    @Autowired
    public void setChartGenerator(ChartGenerator chartGenerator) {
        this.chartGenerator = chartGenerator;
    }

    @Autowired
    public void setChartCsvExporter(ChartCsvExporter chartCsvExporter) {
        this.chartCsvExporter = chartCsvExporter;
    }
}
