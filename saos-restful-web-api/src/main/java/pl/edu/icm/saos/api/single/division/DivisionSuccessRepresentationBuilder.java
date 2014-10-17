package pl.edu.icm.saos.api.single.division;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;
import pl.edu.icm.saos.api.services.links.LinksBuilder;
import pl.edu.icm.saos.api.services.mapping.FieldsMapper;
import pl.edu.icm.saos.api.services.representations.SuccessRepresentation;
import pl.edu.icm.saos.persistence.model.CommonCourtDivision;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static pl.edu.icm.saos.api.ApiConstants.COURT;

/**
 * Provides functionality for building success object view for single division.
 * Success object can be easily serialized as json.
 * @author pavtel
 */
@Component
public class DivisionSuccessRepresentationBuilder {

    //******* fields *********
    @Autowired
    private FieldsMapper<CommonCourtDivision> divisionFieldsMapper;

    @Autowired
    private LinksBuilder linksBuilder;
    //******* END fields **********


    //******** business methods ***********
    /**
     * From division constructs the success view representation (representation details: {@link pl.edu.icm.saos.api.services.representations.SuccessRepresentation SuccessRepresentation})
     * @param division to process.
     * @return map - success representation
     */
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

    //********* END business methods ************


    //*** setters ***
    public void setDivisionFieldsMapper(FieldsMapper<CommonCourtDivision> divisionFieldsMapper) {
        this.divisionFieldsMapper = divisionFieldsMapper;
    }

    public void setLinksBuilder(LinksBuilder linksBuilder) {
        this.linksBuilder = linksBuilder;
    }
}
