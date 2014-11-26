package pl.edu.icm.saos.api.single.scchamber;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;
import pl.edu.icm.saos.api.services.links.LinksBuilder;
import pl.edu.icm.saos.api.single.scchamber.views.ChamberView;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamber;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamberDivision;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static pl.edu.icm.saos.api.single.scchamber.views.ChamberView.Data;
import static pl.edu.icm.saos.api.single.scchamber.views.ChamberView.Division;

/**
 * Provides functionality for building success object view for single supreme court chamber.
 * @author pavtel
 */
@Service
public class ScChamberSuccessRepresentationBuilder {


    @Autowired
    private LinksBuilder linksBuilder;


    //------------------------ LOGIC --------------------------
    /**
     * Constructs chamber's view {@link pl.edu.icm.saos.api.single.scchamber.views.ChamberView ChamberView}.
     * @param chamber to process.
     * @return representation.
     */
    public ChamberView build(SupremeCourtChamber chamber){
        ChamberView representation = new ChamberView();

        fillChamberFieldsToRepresentation(representation, chamber);

        return representation;
    }

    //------------------------ PRIVATE --------------------------
    private void fillChamberFieldsToRepresentation(ChamberView view, SupremeCourtChamber chamber) {
        view.setLinks(toLinks(chamber));
        fillData(view.getData(), chamber);
    }



    private List<Link> toLinks(SupremeCourtChamber chamber) {
        Link chambersLink = linksBuilder.linkToScChamber(chamber.getId());
        return Arrays.asList(chambersLink);
    }

    private void fillData(Data view, SupremeCourtChamber chamber) {
        view.setId(chamber.getId());
        view.setHref(linksBuilder.urlToScChamber(chamber.getId()));
        view.setName(chamber.getName());
        view.setDivisions(toDivisions(chamber.getDivisions()));
    }

    private List<Division> toDivisions(List<SupremeCourtChamberDivision> divisions) {
        if(divisions == null){
            divisions = Collections.emptyList();
        }

        return divisions.stream()
                .map(division -> toView(division))
                .collect(Collectors.toList());
    }

    private Division toView(SupremeCourtChamberDivision division) {
        Division view = new Division();
        view.setId(division.getId());
        view.setHref(linksBuilder.urlToScDivision(division.getId()));
        view.setName(division.getName());
        return view;
    }


    //------------------------ SETTERS --------------------------
    public void setLinksBuilder(LinksBuilder linksBuilder) {
        this.linksBuilder = linksBuilder;
    }
}
