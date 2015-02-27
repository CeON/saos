package pl.edu.icm.saos.api.single.court;

import static pl.edu.icm.saos.api.ApiConstants.PARENT_COURT;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.api.services.links.LinksBuilder;
import pl.edu.icm.saos.api.single.court.views.CommonCourtView;
import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.CommonCourtDivision;

/**
 * Provides functionality for building success object view for single court.
 * Success object can be serialized as json.
 * @author pavtel
 */
@Service
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

        Link link = linksBuilder.linkToCommonCourt(court.getId());
        links.add(link);

        if(court.getParentCourt()!=null){
            Link parentLink = linksBuilder.linkToCommonCourt(court.getParentCourt().getId(), PARENT_COURT);
            links.add(parentLink);
        }

        return links;
    }

    private void fillData(CommonCourtView.Data data, CommonCourt court) {
        data.setId(court.getId());
        data.setHref(linksBuilder.urlToCommonCourt(court.getId()));
        data.setCode(court.getCode());
        data.setName(court.getName());
        data.setType(court.getType());

        if(court.getParentCourt() != null){
            CommonCourtView.ParentCourt parentCourtView = new CommonCourtView.ParentCourt();
            parentCourtView.setId(court.getParentCourt().getId());
            parentCourtView.setHref(linksBuilder.urlToCommonCourt(court.getParentCourt().getId()));
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
        divisionView.setId(division.getId());
        divisionView.setHref(linksBuilder.urlToCcDivision(division.getId()));
        divisionView.setName(division.getName());
        return divisionView;
    }

    //------------------------ SETTERS --------------------------
    public void setLinksBuilder(LinksBuilder linksBuilder) {
        this.linksBuilder = linksBuilder;
    }
}
