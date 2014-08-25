package pl.edu.icm.saos.api.courts.mapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.icm.saos.api.links.LinksBuilder;
import pl.edu.icm.saos.api.mapping.FieldsMapper;
import pl.edu.icm.saos.persistence.model.CommonCourt;

import java.util.LinkedHashMap;
import java.util.Map;

import static pl.edu.icm.saos.api.ApiConstants.HREF;
import static pl.edu.icm.saos.api.ApiConstants.NAME;

/**
 * @author pavtel
 */
@Component("commonCourtFieldsMapper")
public class CourtFieldsMapper implements FieldsMapper<CommonCourt> {

    @Autowired
    private LinksBuilder linksBuilder;


    @Override
    public Map<String, Object> basicsFieldsToMap(CommonCourt court) {
        Map<String, Object> item = new LinkedHashMap<>();
        item.put(NAME, court.getName());
        item.put(HREF, linksBuilder.linkToCourt(court.getId()));

        return item;
    }


    //*** setters ***
    public LinksBuilder getLinksBuilder() {
        return linksBuilder;
    }
}
