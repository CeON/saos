package pl.edu.icm.saos.api.single.division.mapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.icm.saos.api.services.links.LinksBuilder;
import pl.edu.icm.saos.api.services.mapping.FieldsMapper;
import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.CommonCourtDivision;

import java.util.LinkedHashMap;
import java.util.Map;

import static pl.edu.icm.saos.api.ApiConstants.*;
import static pl.edu.icm.saos.api.ApiConstants.TYPE;

/** {@inheritDoc}
 * @author pavtel
 * Converts {@link pl.edu.icm.saos.persistence.model.CommonCourtDivision CommonCourtDivision's} fields.
 */
@Component("commonCourtDivisionFieldsMapper")
public class CommonCourtDivisionFieldsMapper implements FieldsMapper<CommonCourtDivision> {

    //********* fields *************
    @Autowired
    private LinksBuilder linksBuilder;

    @Autowired
    private FieldsMapper<CommonCourt> commonCourtFieldsMapper;

    //********* END fields ***********


    //******* business methods ***********
    @Override
    public Map<String, Object> basicFieldsToMap(CommonCourtDivision division) {
        Map<String, Object> item = new LinkedHashMap<>();
        item.putAll(commonFieldsToMap(division));
        item.put(COURT, commonCourtFieldsMapper.basicFieldsToMap(division.getCourt()));

        return item;
    }


    @Override
    public Map<String, Object> commonFieldsToMap(CommonCourtDivision division, boolean useIdInsteadOfLinks) {
        Map<String, Object> item = new LinkedHashMap<>();

        if(useIdInsteadOfLinks){
            item.put(ID, division.getId());
        }else{
            item.put(HREF, linksBuilder.urlToDivision(division.getId()));
        }

        item.put(NAME, division.getName());

        return item;
    }


    @Override
    public Map<String, Object> fieldsToMap(CommonCourtDivision division, boolean useIdInsteadOfLinks) {
        Map<String, Object> item = new LinkedHashMap<>();
        item.putAll(commonFieldsToMap(division, useIdInsteadOfLinks));

        item.put(CODE, division.getCode());
        item.put(TYPE, division.getType().getName());


        if(useIdInsteadOfLinks){
            Map<String,Object> court = new LinkedHashMap<>();
            court.put(ID, division.getCourt().getId());
            item.put(COURT, court);
        }else{
            item.put(COURT, commonCourtFieldsMapper.fieldsToMap(division.getCourt()));
        }


        return item;
    }

    //********* END business methods ************


    //*** setters ***
    public void setLinksBuilder(LinksBuilder linksBuilder) {
        this.linksBuilder = linksBuilder;
    }


    public void setCommonCourtFieldsMapper(FieldsMapper<CommonCourt> commonCourtFieldsMapper) {
        this.commonCourtFieldsMapper = commonCourtFieldsMapper;
    }
}
