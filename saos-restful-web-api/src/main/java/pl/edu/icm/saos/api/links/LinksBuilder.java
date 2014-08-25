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


    public String linkToJudgment(int judgmentId){
        return linkToElement(JudgmentController.class, judgmentId);
    }

    public String linkToDivision(int divisionId){
        return linkToElement(DivisionController.class, divisionId);
    }

    public String linkToCourt(int courtId) {
        return linkToElement(CourtController.class, courtId);
    }



    private String linkToElement(Class<?> controller, int elementId){
        return linkFor(controller, String.valueOf(elementId)).getHref();
    }

    private Link linkFor(Class<?> controller, String elementId){
        return linkFor(controller, elementId, SELF);
    }

    private Link linkFor(Class<?> controller, String elementId, String relName){
        return linkTo(controller, elementId).withRel(relName);
    }


}
