package pl.edu.icm.saos.api.entry.point

import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import pl.edu.icm.saos.api.dump.DumpEntryPointController
import pl.edu.icm.saos.api.search.SearchEntryPointController
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup
import static pl.edu.icm.saos.api.utils.JsonHelper.contentAsJson;
/**
 * @author pavtel
 */
class MainEntryPointControllerTest extends Specification {

    def "should contain links to the dump and search services"(){
        given:
            MockMvc mockMvc = standaloneSetup(
                    new MainEntryPointController(),
                    new DumpEntryPointController(),
                    new SearchEntryPointController()
            ).build()

        when:
            ResultActions actions = mockMvc.perform(get("/api")
                    .accept(MediaType.APPLICATION_JSON))

        then:
            actions.andExpect(status().isOk())

            def content = contentAsJson(actions)

            content.links[0].rel == "dump"
            content.links[0].href.endsWith  "/api/dump"
            !content.links[0].description.empty

            content.links[1].rel == "search"
            content.links[1].href.endsWith "/api/search"
            !content.links[1].description.empty

    }
}
