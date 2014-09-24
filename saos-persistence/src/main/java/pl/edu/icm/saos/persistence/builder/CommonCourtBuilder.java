package pl.edu.icm.saos.persistence.builder;

import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.CommonCourtDivision;
import pl.edu.icm.saos.persistence.model.CommonCourt.CommonCourtType;

/**
 * @author pavtel
 * Simplified {@link pl.edu.icm.saos.persistence.model.CommonCourt CommonCourt} creation.
 * Do not use it in conjugation with persistence's repositories.
 */
public class CommonCourtBuilder{

    private CommonCourt  element;

    CommonCourtBuilder(int courtId) {
        element = new SpecialCommonCourt(courtId);
    }

    public CommonCourtBuilder code(String code){
        element.setCode(code);
        return this;
    }

    public CommonCourtBuilder name(String name){
        element.setName(name);
        return this;
    }

    public CommonCourtBuilder type(CommonCourtType type){
        element.setType(type);
        return this;
    }

    public CommonCourtBuilder parent(CommonCourt parent){
        element.setParentCourt(parent);
        return this;
    }

    public CommonCourtBuilder divisions(CommonCourtDivision... divisions){
        for(CommonCourtDivision division : divisions){
            element.addDivision(division);
        }

        return this;
    }

    public CommonCourt build(){
        return element;
    }

    private static class SpecialCommonCourt extends CommonCourt {
        private SpecialCommonCourt(int id){
            setId(id);
        }
    }
}
