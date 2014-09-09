package pl.edu.icm.saos.api.builders;

import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.CommonCourtDivision;

/**
 * @author pavtel
 */
public class CommonCourtBuilder extends CommonCourt{

    CommonCourtBuilder(int courtId) {
        setId(courtId);
    }

    public CommonCourtBuilder code(String code){
        setCode(code);
        return this;
    }

    public CommonCourtBuilder name(String name){
        setName(name);
        return this;
    }

    public CommonCourtBuilder type(CommonCourt.CommonCourtType type){
        setType(type);
        return this;
    }

    public CommonCourtBuilder parent(CommonCourt parent){
        setParentCourt(parent);
        return this;
    }

    public CommonCourtBuilder divisions(CommonCourtDivision... divisions){
        for(CommonCourtDivision division: divisions){
            addDivision(division);
        }

        return this;
    }
}
