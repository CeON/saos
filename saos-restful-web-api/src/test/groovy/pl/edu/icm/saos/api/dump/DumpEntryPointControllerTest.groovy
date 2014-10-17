package pl.edu.icm.saos.api.dump
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import pl.edu.icm.saos.api.dump.court.DumpCourtsController
import pl.edu.icm.saos.api.dump.judgment.DumpJudgmentsController
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup
import static pl.edu.icm.saos.api.utils.JsonHelper.contentAsJson
/**
 * @author pavtel
 */
class DumpEntryPointControllerTest extends Specification {

    def "should contain links to the court and judgment dump services"(){
        given:
            MockMvc mockMvc = standaloneSetup(
                    new DumpEntryPointController(),
                    new DumpCourtsController(),
                    new DumpJudgmentsController()
            ).build()

        when:
            ResultActions actions = mockMvc.perform(get("/api/dump")
                    .accept(MediaType.APPLICATION_JSON))

        then:
            actions.andExpect(status().isOk())

            def content = contentAsJson(actions)

            content.links[0].rel == "courts"
            content.links[0].href.endsWith  "/api/dump/courts"
            !content.links[0].description.empty

            content.links[1].rel == "judgments"
            content.links[1].href.endsWith "/api/dump/judgments"
            !content.links[1].description.empty

    }
}
