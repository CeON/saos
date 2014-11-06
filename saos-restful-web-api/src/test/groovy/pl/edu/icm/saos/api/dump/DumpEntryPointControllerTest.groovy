package pl.edu.icm.saos.api.dump
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import pl.edu.icm.saos.api.dump.court.DumpCourtsController
import pl.edu.icm.saos.api.dump.judgment.DumpJudgmentsController
import pl.edu.icm.saos.api.dump.supreme.court.chamber.DumpSupremeCourtChambersController
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup
import static pl.edu.icm.saos.api.utils.JsonHelper.contentAsJson
/**
 * @author pavtel
 */
class DumpEntryPointControllerTest extends Specification {

    def "should contain links to the court, judgment and supreme court chambers dump services"(){
        given:
            MockMvc mockMvc = standaloneSetup(
                    new DumpEntryPointController(),
                    new DumpCourtsController(),
                    new DumpJudgmentsController(),
                    new DumpSupremeCourtChambersController()
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

            content.links[2].rel == "scChambers"
            content.links[2].href.endsWith "/api/dump/scChambers"
            !content.links[2].description.empty

    }
}
