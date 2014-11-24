package pl.edu.icm.saos.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import pl.edu.icm.saos.search.search.model.JudgmentCriteria;
import pl.edu.icm.saos.search.search.model.JudgmentSearchResult;
import pl.edu.icm.saos.search.search.service.SearchService;

import static org.mockito.Mockito.mock;

/**
 * @author pavtel
 */
@Configuration
@Import(ApiTestConfiguration.class)
public class ApiWithMockSearchTestConfiguration {

    @Bean(name = "mockJudgmentsSearchService")
    public SearchService<JudgmentSearchResult, JudgmentCriteria> judgmentsSearchService(){
        SearchService<JudgmentSearchResult, JudgmentCriteria> judgmentsSearchService = mock(SearchService.class);

        return judgmentsSearchService;
    }

}
