package pl.edu.icm.saos.api.courts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;
import pl.edu.icm.saos.api.courts.assemblers.CourtAssembler;
import pl.edu.icm.saos.api.links.LinksBuilder;
import pl.edu.icm.saos.api.response.representations.SuccessRepresentation;
import pl.edu.icm.saos.persistence.model.CommonCourt;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static pl.edu.icm.saos.api.ApiConstants.*;

/**
 * @author pavtel
 */
@Component
public class SingleCourtSuccessRepresentationBuilder {

    @Autowired
    private CourtAssembler courtAssembler;

    @Autowired
    private LinksBuilder linksBuilder;


    public Map<String, Object> build(CommonCourt court){
        SuccessRepresentation.Builder builder = new SuccessRepresentation.Builder();

        builder.data(courtAssembler.fieldsToItemRepresentation(court));
        builder.links(toLinks(court));

        return builder.build();
    }

    private List<Link> toLinks(CommonCourt court) {
        List<Link> links = new LinkedList<>();

        Link link = linksBuilder.linkToCourt(court.getId());
        links.add(link);

        if(court.getParentCourt()!=null){
            Link parentLink = linksBuilder.linkToCourt(court.getParentCourt().getId(), PARENT_COURT);
            links.add(parentLink);
        }

        return links;
    }


    //*** setters ****
    public void setCourtAssembler(CourtAssembler courtAssembler) {
        this.courtAssembler = courtAssembler;
    }

    public void setLinksBuilder(LinksBuilder linksBuilder) {
        this.linksBuilder = linksBuilder;
    }
}
