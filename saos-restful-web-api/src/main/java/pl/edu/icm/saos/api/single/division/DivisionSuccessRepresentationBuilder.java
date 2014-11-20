package pl.edu.icm.saos.api.single.division;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;
import pl.edu.icm.saos.api.services.links.LinksBuilder;
import pl.edu.icm.saos.api.single.division.views.DivisionView;
import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.CommonCourtDivision;

import java.util.Arrays;
import java.util.List;

import static pl.edu.icm.saos.api.ApiConstants.COURT;

/**
 * Provides functionality for building success object view for single division.
 * Success object can be easily serialized as json.
 * @author pavtel
 */
@Component
public class DivisionSuccessRepresentationBuilder {

    @Autowired
    private LinksBuilder linksBuilder;


    //------------------------ LOGIC --------------------------
    /**
     * Constructs division's view {@link pl.edu.icm.saos.api.single.division.views.DivisionView DivisionView}.
     * @param division to process.
     * @return representation.
     */
    public DivisionView build(CommonCourtDivision division){
        DivisionView divisionView = new DivisionView();
        fillDivisionFieldsToRepresentation(divisionView, division);

        return divisionView;
    }

    //------------------------ PRIVATE --------------------------
    private void fillDivisionFieldsToRepresentation(DivisionView representation, CommonCourtDivision division){
        representation.setLinks(toLinks(division));
        fillData(representation.getData(), division);
    }


    private List<Link> toLinks(CommonCourtDivision division) {
        Link divisionLink = linksBuilder.linkToCcDivision(division.getId());
        Link courtLink = linksBuilder.linkToCourt(division.getCourt().getId(), COURT);

        return Arrays.asList(divisionLink, courtLink);
    }

    private void fillData(DivisionView.Data data, CommonCourtDivision division) {
        data.setHref(linksBuilder.urlToCcDivision(division.getId()));
        data.setName(division.getName());
        data.setCode(division.getCode());
        data.setType(division.getType().getName());
        data.setCourt(toCourt(division.getCourt()));
    }

    private DivisionView.Court toCourt(CommonCourt court) {
        DivisionView.Court courtView = new DivisionView.Court();
        courtView.setHref(linksBuilder.urlToCourt(court.getId()));
        courtView.setName(court.getName());
        courtView.setCode(court.getCode());
        courtView.setType(court.getType());

        if(court.getParentCourt() != null){
            DivisionView.ParentCourt parentCourt = new DivisionView.ParentCourt();
            parentCourt.setHref(linksBuilder.urlToCourt(court.getParentCourt().getId()));
            courtView.setParentCourt(parentCourt);
        }

        return courtView;
    }

    //------------------------ SETTERS --------------------------

    public void setLinksBuilder(LinksBuilder linksBuilder) {
        this.linksBuilder = linksBuilder;
    }
}
