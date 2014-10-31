package pl.edu.icm.saos.api.single.court;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;
import pl.edu.icm.saos.api.single.court.assemblers.CourtAssembler;
import pl.edu.icm.saos.api.services.links.LinksBuilder;
import pl.edu.icm.saos.api.services.representations.SuccessRepresentationDep;
import pl.edu.icm.saos.persistence.model.CommonCourt;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static pl.edu.icm.saos.api.ApiConstants.*;

/**
 * Provides functionality for building success object view for single court.
 * Success object can be easily serialized as json.
 * @author pavtel
 */
@Component
public class SingleCourtSuccessRepresentationBuilder {

    //******* fields ***********
    @Autowired
    private CourtAssembler courtAssembler;

    @Autowired
    private LinksBuilder linksBuilder;
    //******* END fields **********


    //******* business methods ***********
    /**
     * Constructs, from court, the success view representation (representation details: {@link pl.edu.icm.saos.api.services.representations.SuccessRepresentationDep SuccessRepresentation})
     * @param court to process.
     * @return map - success representation
     */
    public Map<String, Object> build(CommonCourt court){
        SuccessRepresentationDep.Builder builder = new SuccessRepresentationDep.Builder();

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
    //********** END business methods ********

    //*** setters ****
    public void setCourtAssembler(CourtAssembler courtAssembler) {
        this.courtAssembler = courtAssembler;
    }

    public void setLinksBuilder(LinksBuilder linksBuilder) {
        this.linksBuilder = linksBuilder;
    }
}
