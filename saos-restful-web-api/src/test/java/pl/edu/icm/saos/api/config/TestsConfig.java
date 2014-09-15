package pl.edu.icm.saos.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;

import pl.edu.icm.saos.search.search.model.JudgmentCriteria;
import pl.edu.icm.saos.search.search.model.JudgmentSearchResult;
import pl.edu.icm.saos.search.search.service.SearchService;
import static org.mockito.Mockito.mock;
/**
 * @author pavtel
 */
@Configuration
@ComponentScan(basePackages = "pl.edu.icm.saos.api", excludeFilters = {@ComponentScan.Filter(Configuration.class), @ComponentScan.Filter(Controller.class)})
public class TestsConfig {

    @Bean
    public SearchService<JudgmentSearchResult, JudgmentCriteria> judgmentsSearchService(){
        SearchService<JudgmentSearchResult, JudgmentCriteria> judgmentsSearchService = mock(SearchService.class);

        return judgmentsSearchService;
    }

}
