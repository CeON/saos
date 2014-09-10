package pl.edu.icm.saos.api.builders;

import pl.edu.icm.saos.persistence.model.Judge;

import java.util.Arrays;

/**
 * @author pavtel
 */
public class JudgeBuilder extends Judge {

    JudgeBuilder(String name){
        setName(name);
    }

    public JudgeBuilder judgesRoles(JudgeRole ... judgeRoles){
        setSpecialRoles(Arrays.asList(judgeRoles));
        return this;
    }

}