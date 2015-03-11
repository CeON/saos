package pl.edu.icm.saos.search.analysis.solr;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pl.edu.icm.saos.search.analysis.request.XField;

import com.google.common.collect.ImmutableMap;

/**
 * @author madryk
 */
public class XFieldNameMapperTest {

    private XFieldNameMapper xFieldNameMapper = new XFieldNameMapper();
    
    
    //------------------------ TESTS --------------------------
    
    @Test
    public void mapXField_MAPPING_FOUND() {
        // given
        xFieldNameMapper.setFieldNamesMappings(ImmutableMap.of(XField.JUDGMENT_DATE, "judgmentDate"));
        
        // execute & assert
        assertEquals("judgmentDate", xFieldNameMapper.mapXField(XField.JUDGMENT_DATE));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void mapXField_MAPPING_NOT_FOUND() {
        // execute
        xFieldNameMapper.mapXField(XField.JUDGMENT_DATE);
    }
}
