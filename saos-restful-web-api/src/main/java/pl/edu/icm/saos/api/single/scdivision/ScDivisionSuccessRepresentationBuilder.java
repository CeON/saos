package pl.edu.icm.saos.api.single.scdivision;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;
import pl.edu.icm.saos.api.services.links.LinksBuilder;
import pl.edu.icm.saos.api.single.scdivision.views.DivisionView;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamber;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamberDivision;

import java.util.Arrays;
import java.util.List;

import static pl.edu.icm.saos.api.single.scdivision.views.DivisionView.*;
import static pl.edu.icm.saos.api.ApiConstants.*;

/**
 * Provides functionality for building success object view for single supreme court division.
 * @author pavtel
 */
@Service
public class ScDivisionSuccessRepresentationBuilder {

    @Autowired
    private LinksBuilder linksBuilder;


    //------------------------ LOGIC --------------------------
    /**
     * Constructs division's view {@link pl.edu.icm.saos.api.single.scdivision.views.DivisionView DivisionView}.
     * @param division to process.
     * @return representation.
     */
    public DivisionView build(SupremeCourtChamberDivision division){
        DivisionView divisionView = new DivisionView();

        fillDivisionFieldsToRepresentation(divisionView, division);

        return divisionView;
    }



    //------------------------ PRIVATE --------------------------
    private void fillDivisionFieldsToRepresentation(DivisionView representation, SupremeCourtChamberDivision division) {
        representation.setLinks(toLinks(division));
        fillData(representation.getData(), division);
    }


    private List<Link> toLinks(SupremeCourtChamberDivision division) {
        Link divisionLink = linksBuilder.linkToScDivision(division.getId());
        Link chamberLink = linksBuilder.linkToScChamber(division.getScChamber().getId(), CHAMBER);

        List<Link> links = Arrays.asList(
                divisionLink,
                chamberLink
        );

        return links;
    }

    private void fillData(Data view, SupremeCourtChamberDivision division) {
        view.setId(division.getId());
        view.setHref(linksBuilder.urlToScDivision(division.getId()));
        view.setName(division.getName());
        view.setFullName(division.getFullName());
        view.setChamber(toChamber(division.getScChamber()));
    }

    private Chamber toChamber(SupremeCourtChamber scChamber) {
        Chamber view = new Chamber();

        view.setId(scChamber.getId());
        view.setHref(linksBuilder.urlToScChamber(scChamber.getId()));
        view.setName(scChamber.getName());

        return view;
    }


    //------------------------ SETTERS --------------------------

    public void setLinksBuilder(LinksBuilder linksBuilder) {
        this.linksBuilder = linksBuilder;
    }
}
