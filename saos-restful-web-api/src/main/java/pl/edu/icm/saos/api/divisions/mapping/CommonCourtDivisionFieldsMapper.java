package pl.edu.icm.saos.api.divisions.mapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.icm.saos.api.links.LinksBuilder;
import pl.edu.icm.saos.api.mapping.FieldsMapper;
import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.CommonCourtDivision;

import java.util.LinkedHashMap;
import java.util.Map;

import static pl.edu.icm.saos.api.ApiConstants.COURT;
import static pl.edu.icm.saos.api.ApiConstants.HREF;
import static pl.edu.icm.saos.api.ApiConstants.NAME;

/**
 * @author pavtel
 */
@Component("commonCourtDivisionFieldsMapper")
public class CommonCourtDivisionFieldsMapper implements FieldsMapper<CommonCourtDivision> {

    @Autowired
    private LinksBuilder linksBuilder;

    @Autowired
    private FieldsMapper<CommonCourt> commonCourtFieldsMapper;


    @Override
    public Map<String, Object> basicsFieldsToMap(CommonCourtDivision division) {
        Map<String, Object> item = new LinkedHashMap<>();
        item.put(HREF, linksBuilder.linkToDivision(division.getId()));
        item.put(NAME, division.getName());
        item.put(COURT, commonCourtFieldsMapper.basicsFieldsToMap(division.getCourt()));

        return item;
    }


    //*** setters ***
    public void setLinksBuilder(LinksBuilder linksBuilder) {
        this.linksBuilder = linksBuilder;
    }


    public void setCommonCourtFieldsMapper(FieldsMapper<CommonCourt> commonCourtFieldsMapper) {
        this.commonCourtFieldsMapper = commonCourtFieldsMapper;
    }
}
