package pl.edu.icm.saos.search;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * Helper class for <code>Map&lt;String, List&lt;String&gt;&gt;</code> creation
 * 
 * @author madryk
 */
public class StringListMap {

    /**
     * Transforms array of {@link String} into map
     * <pre>
     * <code>
     * Map&lt;String, List&lt;String&gt;&gt; map = StringListMap.of(new String[][] {
     *     { "key1", "listValue1", "listValue2" },
     *     { "key2", "listValue1", "listValue2", "listValue3" }
     * });
     * </code>
     * </pre>
     * will result in map
     * <pre>
     * { key1 = [listValue1, listValue2],
     *   key2 = [listValue1, listValue2, listValue3] } 
     * </pre>
     * 
     * @param data
     * @return
     */
    public static Map<String, List<String>> of(String[][] data) {
        Map<String, List<String>> map = Maps.newHashMap();
        
        for (int i=0; i<data.length; ++i) {
            String key = data[i][0];
            
            List<String> values = Lists.newArrayList();
            for (int j=1; j<data[i].length; ++j) {
                values.add(data[i][j]);
            }
            map.put(key, values);
        }
        
        return map;
    }
}
