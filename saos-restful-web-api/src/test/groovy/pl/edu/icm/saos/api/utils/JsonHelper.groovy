package pl.edu.icm.saos.api.utils

import groovy.json.JsonSlurper
import org.springframework.test.web.servlet.ResultActions

/**
 * @author pavtel
 */
class JsonHelper {

    static Object contentAsJson(ResultActions actions){
        def content = actions.andReturn().getResponse().getContentAsString();
        return new JsonSlurper().parseText(content);
    }
}
