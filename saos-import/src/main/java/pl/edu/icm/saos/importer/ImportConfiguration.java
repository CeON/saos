package pl.edu.icm.saos.importer;

import java.nio.charset.Charset;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.web.client.RestTemplate;

import pl.edu.icm.saos.common.xml.XmlTagContentExtractor;
import pl.edu.icm.saos.importer.commoncourt.xml.SourceCcJudgment;

import com.google.common.collect.Lists;

/**
 * @author Łukasz Dumiszewski
 */
@Configuration
@ComponentScan
public class ImportConfiguration {

    @Value("${import.commonCourt.connection.timeout}")
    private int ccImportConnectionTimeoutMs = 1000;
    
    @Value("${import.commonCourt.read.timeout}")
    private int ccImportReadTimeoutMs = 1000;
    
    
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
}
