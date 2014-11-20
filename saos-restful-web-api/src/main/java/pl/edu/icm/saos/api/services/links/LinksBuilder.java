package pl.edu.icm.saos.api.services.links;

import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import pl.edu.icm.saos.api.single.court.CourtController;
import pl.edu.icm.saos.api.search.courts.CourtsController;
import pl.edu.icm.saos.api.single.division.DivisionController;
import pl.edu.icm.saos.api.single.judgment.JudgmentController;
import pl.edu.icm.saos.api.single.scchamber.ScChamberController;
import pl.edu.icm.saos.api.single.scdivision.ScDivisionController;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static pl.edu.icm.saos.api.ApiConstants.*;

/**
 * Provides functionality for links creation for linked data.
 * @author pavtel
 */
@Component("linksBuilder")
public class LinksBuilder {

    //------------------------ LOGIC --------------------------

    public String urlToJudgment(int judgmentId){
        return urlToElement(JudgmentController.class, judgmentId);
    }

    public Link linkToJudgment(int judgmentId){
        return linkFor(JudgmentController.class, judgmentId);
    }

    public String urlToCcDivision(int divisionId){
        return urlToElement(DivisionController.class, divisionId);
    }

    public Link linkToCcDivision(int divisionId) {
        return linkFor(DivisionController.class, divisionId);
    }

    public String urlToScDivision(int divisionId){
        return urlToElement(ScDivisionController.class, divisionId);
    }

    public Link linkToScDivision(int divisionId){
        return linkFor(ScDivisionController.class, divisionId);
    }

    public Link linkToScDivision(int divisionId, String relName){
        return linkFor(ScDivisionController.class, divisionId, relName);
    }


    public String urlToScChamber(int chamberId){
        return urlToElement(ScChamberController.class, chamberId);
    }

    public Link linkToScChamber(int chamberId){
        return linkFor(ScChamberController.class, chamberId);
    }

    public Link linkToScChamber(int chamberId, String relName){
        return linkFor(ScChamberController.class, chamberId, relName);
    }

    public String urlToCourt(int courtId) {
        return urlToElement(CourtController.class, courtId);
    }

    public Link linkToCourt(int courtId){
        return linkFor(CourtController.class, courtId);
    }

    public UriComponentsBuilder courtsUriBuilder(){
        return linkTo(CourtsController.class).toUriComponentsBuilder();
    }

    public Link linkToCourt(int courtId, String relName){
        return linkFor(CourtController.class, courtId, relName);
    }

    private String urlToElement(Class<?> controller, int elementId){
        return linkFor(controller, elementId).getHref();
    }

    private Link linkFor(Class<?> controller, int elementId){
        return linkFor(controller, elementId, SELF);
    }

    private Link linkFor(Class<?> controller , int id, String relName){
        return linkFor(controller, String.valueOf(id), relName);
    }

    private Link linkFor(Class<?> controller, String elementId, String relName){
        return linkTo(controller, elementId).withRel(relName);
    }




}
