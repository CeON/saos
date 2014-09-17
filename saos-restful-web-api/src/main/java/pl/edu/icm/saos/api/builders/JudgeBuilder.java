package pl.edu.icm.saos.api.builders;

import java.util.Arrays;

import pl.edu.icm.saos.persistence.model.Judge;

/**
 * @author pavtel
 */
public class JudgeBuilder extends Judge {

    JudgeBuilder(String name, JudgeRole ... judgeRoles){
        super(name, judgeRoles);
    }

    public JudgeBuilder judgesRoles(JudgeRole ... judgeRoles){
        setSpecialRoles(Arrays.asList(judgeRoles));
        return this;
    }

}