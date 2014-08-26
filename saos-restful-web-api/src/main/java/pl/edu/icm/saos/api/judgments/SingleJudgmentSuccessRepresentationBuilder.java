package pl.edu.icm.saos.api.judgments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;
import pl.edu.icm.saos.api.judgments.assemblers.JudgmentAssembler;
import pl.edu.icm.saos.api.links.LinksBuilder;
import pl.edu.icm.saos.api.response.representations.SuccessRepresentation;
import pl.edu.icm.saos.persistence.model.Judgment;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author pavtel
 */
@Component("singleJudgmentSuccessRepresentationBuilder")
public class SingleJudgmentSuccessRepresentationBuilder {


    @Autowired
    private JudgmentAssembler judgmentAssembler;

    @Autowired
    private LinksBuilder linksBuilder;


    public Map<String, Object> build(Judgment judgment){
        SuccessRepresentation.Builder builder = new SuccessRepresentation.Builder();
        builder.data(judgmentAssembler.fieldsToItemRepresentation(judgment));
        builder.links(toLinks(judgment));

        return builder.build();
    }

    private List<Link> toLinks(Judgment judgment) {
        Link link = linksBuilder.linkToJudgment(judgment.getId());
        return Arrays.asList(link);
    }

    //*** setters ***
    public void setJudgmentAssembler(JudgmentAssembler judgmentAssembler) {
        this.judgmentAssembler = judgmentAssembler;
    }

    public void setLinksBuilder(LinksBuilder linksBuilder) {
        this.linksBuilder = linksBuilder;
    }
}
