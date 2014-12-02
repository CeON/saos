package pl.edu.icm.saos.api.search
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import pl.edu.icm.saos.api.search.judgments.JudgmentsController
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup
import static pl.edu.icm.saos.api.utils.JsonHelper.contentAsJson
/**
 * @author pavtel
 */
class SearchEntryPointControllerTest extends Specification {

    def "should contain links to the court and judgment search services"(){
        given:
            MockMvc mockMvc = standaloneSetup(
                    new SearchEntryPointController(),
                    new JudgmentsController()
            ).build()

        when:
            ResultActions actions = mockMvc.perform(get("/api/search")
                    .accept(MediaType.APPLICATION_JSON))

        then:
            actions.andExpect(status().isOk())

            def content = contentAsJson(actions)


            content.links[0].rel == "judgments"
            content.links[0].href.endsWith "/api/search/judgments"
            !content.links[0].description.empty

    }
}
