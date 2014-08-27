package pl.edu.icm.saos.api.divisions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;
import pl.edu.icm.saos.api.links.LinksBuilder;
import pl.edu.icm.saos.api.mapping.FieldsMapper;
import pl.edu.icm.saos.api.response.representations.SuccessRepresentation;
import pl.edu.icm.saos.persistence.model.CommonCourtDivision;
import static pl.edu.icm.saos.api.ApiConstants.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author pavtel
 */
@Component
public class DivisionSuccessRepresentationBuilder {

    @Autowired
    private FieldsMapper<CommonCourtDivision> divisionFieldsMapper;

    @Autowired
    private LinksBuilder linksBuilder;

    public Map<String, Object> build(CommonCourtDivision division){
        SuccessRepresentation.Builder builder = new SuccessRepresentation.Builder();

        builder.data(divisionFieldsMapper.fieldsToMap(division));
        builder.links(toLinks(division));

        return builder.build();
    }

    private List<Link> toLinks(CommonCourtDivision division) {
        Link divisionLink = linksBuilder.linkToDivision(division.getId());
        Link courtLink = linksBuilder.linkToCourt(division.getCourt().getId(), COURT);

        return Arrays.asList(divisionLink, courtLink);
    }


    //*** setters ***
    public void setDivisionFieldsMapper(FieldsMapper<CommonCourtDivision> divisionFieldsMapper) {
        this.divisionFieldsMapper = divisionFieldsMapper;
    }

    public void setLinksBuilder(LinksBuilder linksBuilder) {
        this.linksBuilder = linksBuilder;
    }
}
