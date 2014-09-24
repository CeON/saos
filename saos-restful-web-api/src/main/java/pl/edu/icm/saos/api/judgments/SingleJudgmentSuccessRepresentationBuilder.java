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
 * Provides functionality for building success object view for single judgment.
 * Success object can be easily serialized as json.
 * @author pavtel
 */
@Component("singleJudgmentSuccessRepresentationBuilder")
public class SingleJudgmentSuccessRepresentationBuilder {

    //******* fields ***********
    @Autowired
    private JudgmentAssembler judgmentAssembler;

    @Autowired
    private LinksBuilder linksBuilder;

    //********** END fields **************

    //********** business methods **************

    /**
     * Constructs, from judgment, the success view representation (representation details: {@link pl.edu.icm.saos.api.response.representations.SuccessRepresentation SuccessRepresentation})
     * @param judgment to process.
     * @return map - success representation
     */
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

    //************* END business methods **************

    //*** setters ***
    public void setJudgmentAssembler(JudgmentAssembler judgmentAssembler) {
        this.judgmentAssembler = judgmentAssembler;
    }

    public void setLinksBuilder(LinksBuilder linksBuilder) {
        this.linksBuilder = linksBuilder;
    }
}
