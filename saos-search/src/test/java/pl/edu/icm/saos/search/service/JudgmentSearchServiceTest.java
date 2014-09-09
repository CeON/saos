package pl.edu.icm.saos.search.service;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pl.edu.icm.saos.common.testcommon.category.SlowTest;
import pl.edu.icm.saos.search.SearchTestConfiguration;
import pl.edu.icm.saos.search.model.JudgmentCriteria;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={ SearchTestConfiguration.class })
@Category(SlowTest.class)
public class JudgmentSearchServiceTest {

    @Autowired
    private JudgmentSearchService judgmentSearchService;
    
    @Test
    public void shouldSearch() {
        judgmentSearchService.search(new JudgmentCriteria("sss"), null);
    }
}
