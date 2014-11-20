package pl.edu.icm.saos.api.single.court;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;
import pl.edu.icm.saos.api.services.links.LinksBuilder;
import pl.edu.icm.saos.api.single.court.views.CommonCourtView;
import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.CommonCourtDivision;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static pl.edu.icm.saos.api.ApiConstants.PARENT_COURT;

/**
 * Provides functionality for building success object view for single court.
 * Success object can be serialized as json.
 * @author pavtel
 */
@Component
public class SingleCourtSuccessRepresentationBuilder {

    @Autowired
    private LinksBuilder linksBuilder;


    //------------------------ LOGIC --------------------------
    /**
     * Constructs division's view {@link pl.edu.icm.saos.api.single.court.views.CommonCourtView CommonCourtView}.
     * @param court to process.
     * @return representation.
     */
    public CommonCourtView build(CommonCourt court){
        CommonCourtView commonCourtView = new CommonCourtView();
        fillCourtFieldsToRepresentation(commonCourtView, court);

        return commonCourtView;
    }

    //------------------------ PRIVATE --------------------------
    private void fillCourtFieldsToRepresentation(CommonCourtView commonCourtView, CommonCourt court) {
        commonCourtView.setLinks(toLinks(court));
        fillData(commonCourtView.getData(), court);
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

    private void fillData(CommonCourtView.Data data, CommonCourt court) {
        data.setHref(linksBuilder.urlToCourt(court.getId()));
        data.setCode(court.getCode());
        data.setName(court.getName());
        data.setType(court.getType());

        if(court.getParentCourt() != null){
            CommonCourtView.ParentCourt parentCourtView = new CommonCourtView.ParentCourt();
            parentCourtView.setHref(linksBuilder.urlToCourt(court.getParentCourt().getId()));
            data.setParentCourt(parentCourtView);
        }
        data.setDivisions(toDivisions(court));
    }

    private List<CommonCourtView.Division> toDivisions(CommonCourt court) {
        List<CommonCourtView.Division> divisionsView = court.getDivisions()
                .stream()
                .map(division -> toDivision(division))
                .collect(Collectors.toList());

        return divisionsView;
    }

    private CommonCourtView.Division toDivision(CommonCourtDivision division){
        CommonCourtView.Division divisionView = new CommonCourtView.Division();
        divisionView.setHref(linksBuilder.urlToCcDivision(division.getId()));
        divisionView.setName(division.getName());
        return divisionView;
    }

    //------------------------ SETTERS --------------------------
    public void setLinksBuilder(LinksBuilder linksBuilder) {
        this.linksBuilder = linksBuilder;
    }
}
