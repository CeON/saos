package pl.edu.icm.saos.importer.commoncourt;

import java.nio.charset.Charset;
import java.util.List;

import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.web.client.RestTemplate;

import pl.edu.icm.saos.common.xml.XmlTagContentExtractor;
import pl.edu.icm.saos.importer.commoncourt.court.XmlCommonCourt;
import pl.edu.icm.saos.importer.commoncourt.xml.SourceCcJudgment;

import com.google.common.collect.Lists;

/**
 * @author Łukasz Dumiszewski
 */
@Configuration
public class CommonCourtImportConfiguration {

    @Value("${import.commonCourt.connection.timeout}")
    private int ccImportConnectionTimeoutMs = 1000;
    
    @Value("${import.commonCourt.read.timeout}")
    private int ccImportReadTimeoutMs = 1000;
    
    
    //-------------------------- CommonCourt judgment importer --------------------------
    
    @Bean
    public Jaxb2Marshaller ccJudgmentJaxb2Marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(SourceCcJudgment.class);
        return marshaller;
    }
    
    @Bean
    public XmlTagContentExtractor tagContentExtractor() {
        return new XmlTagContentExtractor();
    }
    
    @Bean
    public RestTemplate ccImportRestTemplate() {
        
        SimpleClientHttpRequestFactory clientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        clientHttpRequestFactory.setConnectTimeout(ccImportConnectionTimeoutMs);
        clientHttpRequestFactory.setReadTimeout(ccImportReadTimeoutMs);
        
        RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);
        
        List<HttpMessageConverter<?>> converters = Lists.newArrayList();
        converters.add(utf8StringHttpMessageConverter());
        restTemplate.setMessageConverters(converters);
        
        return restTemplate;
    }
    
    
    @Bean
    public StringHttpMessageConverter utf8StringHttpMessageConverter() {
        return new StringHttpMessageConverter(Charset.forName("UTF-8"));
    }
    
    
    
    //------------------------ CommonCourtImporter --------------------------
    
    @Value("${import.commonCourt.court.xml.filePath}")
    private String ccXmlFilePath;
    
    
    @Bean
    public StaxEventItemReader<XmlCommonCourt> commonCourtImportReader() {
        StaxEventItemReader<XmlCommonCourt> reader = new StaxEventItemReader<>();
        reader.setFragmentRootElementName("court");
        reader.setResource(new FileSystemResource(ccXmlFilePath));
        reader.setUnmarshaller(xmlCommonCourtJaxb2Marshaller());
        return reader;
    }
    
    @Bean
    public Jaxb2Marshaller xmlCommonCourtJaxb2Marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(XmlCommonCourt.class);
        return marshaller;
    }
    
}
