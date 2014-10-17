package pl.edu.icm.saos.api.single.court.mapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.icm.saos.api.services.links.LinksBuilder;
import pl.edu.icm.saos.api.services.mapping.FieldsMapper;
import pl.edu.icm.saos.persistence.model.CommonCourt;

import java.util.LinkedHashMap;
import java.util.Map;

import static pl.edu.icm.saos.api.ApiConstants.*;

/** {@inheritDoc}
 * @author pavtel
 * Converts {@link pl.edu.icm.saos.persistence.model.CommonCourt CommonCourt's} fields.
 */
@Component("commonCourtFieldsMapper")
public class CourtFieldsMapper implements FieldsMapper<CommonCourt> {

    //******** fields *************
    @Autowired
    private LinksBuilder linksBuilder;


    @Override
    public Map<String, Object> basicFieldsToMap(CommonCourt court) {
        Map<String, Object> item = new LinkedHashMap<>();
        item.putAll(commonFieldsToMap(court));
        return item;
    }

    //********* END fields **************



    //********* business methods ***************
    @Override
    public Map<String, Object> fieldsToMap(CommonCourt court, boolean useIdInsteadOfLinks) {
        Map<String, Object> item = new LinkedHashMap<>();
        item.putAll(commonFieldsToMap(court, useIdInsteadOfLinks));

        item.put(CODE, court.getCode());
        item.put(TYPE, court.getType());

        if(court.getParentCourt() != null){

            if(useIdInsteadOfLinks){
                Map<String, Object> parentCourt = new LinkedHashMap<>();
                parentCourt.put(ID, court.getParentCourt().getId());
                item.put(PARENT_COURT, parentCourt);
            }else{
                item.put(PARENT_COURT, toHref(court.getParentCourt()));
            }

        }

        return item;
    }


    @Override
    public Map<String, Object> commonFieldsToMap(CommonCourt court, boolean useIdInsteadOfLinks) {
        Map<String, Object> item = new LinkedHashMap<>();

        if(useIdInsteadOfLinks){
            item.put(ID, court.getId());
        } else {
            item.put(HREF, linksBuilder.urlToCourt(court.getId()));
        }

        item.put(NAME, court.getName());

        return item;
    }

    private Map<String, Object> toHref(CommonCourt court){
        Map<String, Object> item = new LinkedHashMap<>();
        item.put(HREF, linksBuilder.urlToCourt(court.getId()));

        return item;
    }

    //************* END business methods *****************


    //*** setters ***
    public void setLinksBuilder(LinksBuilder linksBuilder) {
        this.linksBuilder = linksBuilder;
    }
}
