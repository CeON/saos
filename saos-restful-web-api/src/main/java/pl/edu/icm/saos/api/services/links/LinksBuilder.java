package pl.edu.icm.saos.api.services.links;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static pl.edu.icm.saos.api.ApiConstants.SELF;

import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.api.single.court.CourtController;
import pl.edu.icm.saos.api.single.division.DivisionController;
import pl.edu.icm.saos.api.single.judgment.JudgmentController;
import pl.edu.icm.saos.api.single.scchamber.ScChamberController;
import pl.edu.icm.saos.api.single.scdivision.ScDivisionController;

/**
 * Provides functionality for links creation for linked data.
 * @author pavtel
 */
@Service("linksBuilder")
public class LinksBuilder {

    //------------------------ LOGIC --------------------------

    public String urlToJudgment(long judgmentId){
        return urlToElement(JudgmentController.class, judgmentId);
    }

    public Link linkToJudgment(long judgmentId){
        return linkFor(JudgmentController.class, judgmentId);
    }

    public String urlToCcDivision(long divisionId){
        return urlToElement(DivisionController.class, divisionId);
    }

    public Link linkToCcDivision(long divisionId) {
        return linkFor(DivisionController.class, divisionId);
    }

    public String urlToScDivision(long divisionId){
        return urlToElement(ScDivisionController.class, divisionId);
    }

    public Link linkToScDivision(long divisionId){
        return linkFor(ScDivisionController.class, divisionId);
    }

    public Link linkToScDivision(long divisionId, String relName){
        return linkFor(ScDivisionController.class, divisionId, relName);
    }


    public String urlToScChamber(long chamberId){
        return urlToElement(ScChamberController.class, chamberId);
    }

    public Link linkToScChamber(long chamberId){
        return linkFor(ScChamberController.class, chamberId);
    }

    public Link linkToScChamber(long chamberId, String relName){
        return linkFor(ScChamberController.class, chamberId, relName);
    }

    public String urlToCourt(long courtId) {
        return urlToElement(CourtController.class, courtId);
    }

    public Link linkToCourt(long courtId){
        return linkFor(CourtController.class, courtId);
    }

    public Link linkToCourt(long courtId, String relName){
        return linkFor(CourtController.class, courtId, relName);
    }

    private String urlToElement(Class<?> controller, long elementId){
        return linkFor(controller, elementId).getHref();
    }

    private Link linkFor(Class<?> controller, long elementId){
        return linkFor(controller, elementId, SELF);
    }

    private Link linkFor(Class<?> controller , long id, String relName){
        return linkFor(controller, String.valueOf(id), relName);
    }

    private Link linkFor(Class<?> controller, String elementId, String relName){
        return linkTo(controller, elementId).withRel(relName);
    }




}
