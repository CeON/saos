package pl.edu.icm.saos.persistence.builder;

import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.CommonCourtDivision;
import pl.edu.icm.saos.persistence.model.CommonCourtDivisionType;

/**
 * @author pavtel
 * Simplified {@link pl.edu.icm.saos.persistence.model.CommonCourtDivision CommonCourtDivision} creation.
 * Do not use it in conjugation with persistence's repositories.
 */
public class CommonCourtDivisionBuilder {

    private CommonCourtDivision element;

    CommonCourtDivisionBuilder(int divisionId){
        element = new SpecialCommonCourtDivision(divisionId);
    }

    CommonCourtDivisionBuilder(){
        element = new CommonCourtDivision();
    }

    public CommonCourtDivisionBuilder court(CommonCourt court){
        element.setCourt(court);
        return this;
    }

    public CommonCourtDivisionBuilder name(String name){
        element.setName(name);
        return this;
    }

    public CommonCourtDivisionBuilder code(String code){
        element.setCode(code);
        return this;
    }

    public CommonCourtDivisionBuilder type(CommonCourtDivisionType type){
        element.setType(type);
        return this;
    }

    public CommonCourtDivision build(){
        return element;
    }

    private static class SpecialCommonCourtDivision extends CommonCourtDivision {
        private SpecialCommonCourtDivision(int divisionId){
            setId(divisionId);
        }
    }
}
