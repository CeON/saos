package pl.edu.icm.saos.api.builders;

import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.CommonCourtDivision;
import pl.edu.icm.saos.persistence.model.CommonCourtDivisionType;

/**
 * @author pavtel
 */
public class CommonCourtDivisionBuilder extends CommonCourtDivision {

    CommonCourtDivisionBuilder(int divisionId){
        setId(divisionId);
    }

    public CommonCourtDivisionBuilder court(CommonCourt court){
        setCourt(court);
        return this;
    }

    public CommonCourtDivisionBuilder name(String name){
        setName(name);
        return this;
    }

    public CommonCourtDivisionBuilder code(String code){
        setCode(code);
        return this;
    }

    public CommonCourtDivisionBuilder type(CommonCourtDivisionType type){
        setType(type);
        return this;
    }
}
