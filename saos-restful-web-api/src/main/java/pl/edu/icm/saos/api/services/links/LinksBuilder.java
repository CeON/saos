package pl.edu.icm.saos.api.services.links;

import static pl.edu.icm.saos.api.ApiConstants.SELF;
import static pl.edu.icm.saos.api.services.links.ControllerProxyLinkBuilder.linkTo;

import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.api.single.ccdivision.CcDivisionController;
import pl.edu.icm.saos.api.single.court.CommonCourtController;
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
        return urlToElement(CcDivisionController.class, divisionId);
    }

    public Link linkToCcDivision(long divisionId) {
        return linkFor(CcDivisionController.class, divisionId);
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

    public String urlToCommonCourt(long courtId) {
        return urlToElement(CommonCourtController.class, courtId);
    }

    public Link linkToCommonCourt(long courtId){
        return linkFor(CommonCourtController.class, courtId);
    }

    public Link linkToCommonCourt(long courtId, String relName){
        return linkFor(CommonCourtController.class, courtId, relName);
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
