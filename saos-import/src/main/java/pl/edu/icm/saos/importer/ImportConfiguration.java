package pl.edu.icm.saos.importer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import pl.edu.icm.saos.importer.commoncourt.xml.SourceCcJudgment;

/**
 * @author Łukasz Dumiszewski
 */
@Configuration
@ComponentScan
public class ImportConfiguration {

    
    @Bean
    public Jaxb2Marshaller ccJudgmentMarshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(SourceCcJudgment.class);
        return marshaller;
    }
}
