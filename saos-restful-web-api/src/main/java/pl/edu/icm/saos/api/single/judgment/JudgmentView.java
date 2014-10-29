package pl.edu.icm.saos.api.single.judgment;

import org.springframework.hateoas.Link;

import java.io.Serializable;
import java.util.List;

/**
 * @author pavtel
 */
public class JudgmentView implements Serializable{

    private static final long serialVersionUID = -7645243331934338429L;


    private List<Link> links;

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }
}
