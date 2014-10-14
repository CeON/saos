package pl.edu.icm.saos.api.exceptions
import groovy.json.JsonSlurper
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.ResponseBody
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup
import static pl.edu.icm.saos.api.exceptions.status.ErrorStatus.*;
/**
 * @author pavtel
 */
class ControllersEntityExceptionHandlerTest extends Specification {

    public static final String PATH = "/special";



    def "should return error's representation for IllegalArgumentException (General internal error)"(){
        given:
            def exceptionMsg = "same illegal operation message"
            def specialController = new SpecialController(){
                @Override
                def ResponseEntity<Map<String, Object>> execute(){
                    throw new IllegalArgumentException(exceptionMsg)
                }
            }
            MockMvc mockMvc = standaloneSetup(specialController).build()

        when:
            ResultActions actions = mockMvc.perform(get(PATH)
                    .accept(MediaType.APPLICATION_JSON))


        then:
            actions.andExpect(status().isInternalServerError())

            def content = contentAsJson(actions)
            content.error.httpStatus == "500"
            content.error.name == GENERAL_INTERNAL_ERROR.errorName()
            content.error.message == exceptionMsg
            content.error.moreInfo.endsWith GENERAL_INTERNAL_ERROR.name()
    }

    def "should return error's representation for WrongRequestParameterException"(){
        given:
            def paramName = "limit"
            def message = "not appropriate value"
            def specialController = new SpecialController(){
                @Override
                def ResponseEntity<Map<String, Object>> execute(){
                    throw new WrongRequestParameterException(paramName, message)
                }
            }
            MockMvc mockMvc = standaloneSetup(specialController).build()

        when:
            ResultActions actions = mockMvc.perform(get(PATH)
                    .accept(MediaType.APPLICATION_JSON))

        then:
            actions.andExpect(status().isBadRequest())

            def content = contentAsJson(actions)
            content.error.httpStatus == "400"
            content.error.name == WRONG_REQUEST_PARAMETER_ERROR.errorName()
            content.error.message.contains message
            content.error.moreInfo.endsWith WRONG_REQUEST_PARAMETER_ERROR.name()
            content.error.propertyName == paramName

    }

    def "should return error's representation for Exception"(){
        given:
            def exceptionMsg = "some exception message"
            def specialController = new SpecialController(){
                @Override
                def ResponseEntity<Map<String, Object>> execute(){
                    throw new Exception(exceptionMsg)
                }
            }
            MockMvc mockMvc = standaloneSetup(specialController).build()

        when:
            ResultActions actions = mockMvc.perform(get(PATH)
                    .accept(MediaType.APPLICATION_JSON))

        then:
            actions.andExpect(status().isInternalServerError())

            def content = contentAsJson(actions)
            content.error.httpStatus == "500"
            content.error.name == GENERAL_INTERNAL_ERROR.errorName()
            content.error.message == exceptionMsg
            content.error.moreInfo.endsWith GENERAL_INTERNAL_ERROR.name()
    }

    def "should return error's representation for ElementDoesNotExistException"(){
        given:
            def exceptionMsg = "Element ... "
            def elementId = 111
            def specialController = new SpecialController(){
                @Override
                def ResponseEntity<Map<String, Object>> execute(){
                    throw new ElementDoesNotExistException(exceptionMsg, elementId)
                }
            }
            MockMvc mockMvc = standaloneSetup(specialController).build()

        when:
            ResultActions actions = mockMvc.perform(get(PATH)
                    .accept(MediaType.APPLICATION_JSON))

        then:
            actions.andExpect(status().isNotFound())

            def content = contentAsJson(actions)
            content.error.httpStatus == "404"
            content.error.name == ELEMENT_DOES_NOT_EXIST_ERROR.errorName
            content.error.message.contains exceptionMsg
            content.error.message.contains elementId.toString()
            content.error.moreInfo.endsWith ELEMENT_DOES_NOT_EXIST_ERROR.name()
    }


    @Controller
    @RequestMapping("/special")
    static class SpecialController extends ControllersEntityExceptionHandler{
        @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
        @ResponseBody
        def ResponseEntity<Map<String, Object>> createView(){
            execute()
        }

        def ResponseEntity<Map<String, Object>> execute(){

        }
    }



    static Object contentAsJson(ResultActions actions){
        def content = actions.andReturn().getResponse().getContentAsString();
        return new JsonSlurper().parseText(content);
    }



}
