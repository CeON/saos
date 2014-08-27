package pl.edu.icm.saos.api.links;

import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;
import pl.edu.icm.saos.api.courts.CourtController;
import pl.edu.icm.saos.api.divisions.DivisionController;
import pl.edu.icm.saos.api.judgments.JudgmentController;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static pl.edu.icm.saos.api.ApiConstants.*;

/**
 * @author pavtel
 */
@Component("linksBuilder")
public class LinksBuilder {


    public String urlToJudgment(int judgmentId){
        return urlToElement(JudgmentController.class, judgmentId);
    }

    public Link linkToJudgment(int judgmentId){
        return linkFor(JudgmentController.class, judgmentId);
    }

    public String urlToDivision(int divisionId){
        return urlToElement(DivisionController.class, divisionId);
    }

    public Link linkToDivision(int divisionId) {
        return linkFor(DivisionController.class, divisionId);
    }

    public String urlToCourt(int courtId) {
        return urlToElement(CourtController.class, courtId);
    }

    public Link linkToCourt(int courtId){
        return linkFor(CourtController.class, courtId);
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
