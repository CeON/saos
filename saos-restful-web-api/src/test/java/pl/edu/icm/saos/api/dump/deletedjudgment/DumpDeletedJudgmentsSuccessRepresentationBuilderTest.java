package pl.edu.icm.saos.api.dump.deletedjudgment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.util.UriComponentsBuilder;

import pl.edu.icm.saos.api.ApiConstants;
import pl.edu.icm.saos.api.dump.deletedjudgment.views.DumpDeletedJudgmentsView;

@RunWith(MockitoJUnitRunner.class)
public class DumpDeletedJudgmentsSuccessRepresentationBuilderTest {
    
    private DumpDeletedJudgmentsSuccessRepresentationBuilder representationBuilder = new DumpDeletedJudgmentsSuccessRepresentationBuilder();
    
    private UriComponentsBuilder uriComponentsBuilder = mock(UriComponentsBuilder.class, Mockito.RETURNS_DEEP_STUBS);
    
    
    
    //------------------------ TESTS --------------------------
    
    @Test(expected = NullPointerException.class)
    public void build_deletedJudgmentIds_NULL() {
        
        // execute
        representationBuilder.build(null, uriComponentsBuilder);
        
    }

    
    @Test(expected = NullPointerException.class)
    public void build_uriComponentsBuilder_NULL() {
        
        // execute
        representationBuilder.build(Lists.newArrayList(), null);
        
    }
    
    @Test
    public void build() {
        
        // given
        
        List<Long> judgmentIds = Lists.newArrayList(123L, 334L, 3344L, 11111L);
        when(uriComponentsBuilder.build().encode().toString()).thenReturn("http://www.somepath.com");
        
        
        // execute
        
        DumpDeletedJudgmentsView dumpDeletedJudgmentsView = representationBuilder.build(judgmentIds, uriComponentsBuilder);
        
        
        // assert
        
        assertNotNull(dumpDeletedJudgmentsView.getLinks());
        assertEquals(1, dumpDeletedJudgmentsView.getLinks().size());
        assertEquals("http://www.somepath.com", dumpDeletedJudgmentsView.getLinks().get(0).getHref());
        assertEquals(ApiConstants.SELF, dumpDeletedJudgmentsView.getLinks().get(0).getRel());
        assertNull(dumpDeletedJudgmentsView.getInfo());
        assertNull(dumpDeletedJudgmentsView.getQueryTemplate());
        assertEquals(judgmentIds, dumpDeletedJudgmentsView.getItems());
    }


}
