package pl.edu.icm.saos.api.services.interceptor
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.web.bind.annotation.*
import pl.edu.icm.saos.api.services.exceptions.ControllersEntityExceptionHandler
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup
/**
 * @author pavtel
 */
class RestrictParamsHandlerInterceptorTest extends Specification {

    MockMvc mockMvc;

    @Controller
    static class SpecialController extends ControllersEntityExceptionHandler{
        SpecialController() {
            
        }

        @RequestMapping(value = "/withoutRPNA", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
        @ResponseBody
        def ResponseEntity<Map<String, Object>> withoutRPNamesAnnotation(){
            return null;
        }

        @RequestMapping(value = "/withRPNA", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
        @RestrictParamsNames
        @ResponseBody
        def ResponseEntity<Map<String, Object>> withRPNamesAnnotation(){
            return null;
        }

        @RequestMapping(value = "/withRPNA_and_prefix", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
        @RestrictParamsNames(allowedPrefixes = ["xx", "yy"])
        @ResponseBody
        def ResponseEntity<Map<String, Object>> withRPNamesAnnotationWithAllowedPrefixes(){
            return null;
        }


        @RequestMapping(value = "/withRPNA_and_request_param", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
        @RestrictParamsNames
        @ResponseBody
        def ResponseEntity<Map<String, Object>> withRPNamesAnnotationWithRequestParam(@RequestParam("specialName") String specialName){
            return null;
        }


    }


    def setup() {
        mockMvc = standaloneSetup(new SpecialController())
                .addInterceptors(new RestrictParamsHandlerInterceptor())
                .build()
    }

    //------------------------ TESTS --------------------------

    def "it should not throw exception for incorrect request param name if method is not annotated with RestrictParamsNames"(){
        when:
            ResultActions actions = mockMvc.perform(get("/withoutRPNA")
                    .param("some_incorrect_parameter_name", "")
                    .accept(MediaType.APPLICATION_JSON))

        then:
            actions.andExpect(status().isOk())
    }

    def "it should throw exception for incorrect request param name if method is annotated with RestrictParamsNames"(){
        when:
            ResultActions actions = mockMvc.perform(get("/withRPNA")
                    .param("some_incorrect_parameter_name", "")
                    .accept(MediaType.APPLICATION_JSON))

        then:
            actions.andExpect(status().isBadRequest())
    }

    def "it should not throw exception for incorrect request param name if starts with allowed prefix"(){
        when:
            ResultActions actions = mockMvc.perform(get("/withRPNA_and_prefix")
                    .param("xx"+"some_incorrect_parameter_name", "")
                    .param("yy"+"some_incorrect_parameter_name", "")
                    .accept(MediaType.APPLICATION_JSON))

        then:
            actions.andExpect(status().isOk())
    }

    def "it should not throw exception for request param name that is used in method as argument with RequestParam annotation"(){
        when:
            ResultActions actions = mockMvc.perform(get("/withRPNA_and_request_param")
                    .param("specialName", "some value")
                    .accept(MediaType.APPLICATION_JSON))

        then:
            actions.andExpect(status().isOk())
    }


}
