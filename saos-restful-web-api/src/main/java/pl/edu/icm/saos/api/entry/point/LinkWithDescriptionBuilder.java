package pl.edu.icm.saos.api.entry.point;

import static pl.edu.icm.saos.api.services.links.ControllerProxyLinkBuilder.linkTo;

import java.util.LinkedHashMap;
import java.util.Map;


/**
 * @author pavtel
 */
public class LinkWithDescriptionBuilder {

    private LinkWithDescription linkWithDescription = new LinkWithDescription();

    public LinkWithDescriptionBuilder rel(String rel){
        linkWithDescription.setRel(rel);
        return this;
    }

    public LinkWithDescriptionBuilder href(Class<?> controller){
        linkWithDescription.setHref(linkTo(controller).withSelfRel().getHref());
        return this;
    }

    public LinkWithDescriptionBuilder description(String description){
        linkWithDescription.setDescription(description);
        return this;
    }

    public LinkWithDescription build(){
        return linkWithDescription;
    }

    public static Object createLinksRepresentation(LinkWithDescription ... linkWithDescriptions){
        Map<String, Object> links = new LinkedHashMap<>();
        links.put("links", linkWithDescriptions);
        return links;
    }
}
