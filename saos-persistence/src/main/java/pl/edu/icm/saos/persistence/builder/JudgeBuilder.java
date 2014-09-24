package pl.edu.icm.saos.persistence.builder;

import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judge.JudgeRole;

import java.util.Arrays;

/**
 * @author pavtel
 * Simplified {@link pl.edu.icm.saos.persistence.model.Judge Judge} creation.
 * Do not use it in conjugation with persistence's repositories.
 */
public class JudgeBuilder {

    private Judge element;

    JudgeBuilder(String name, JudgeRole... judgeRoles){
        element = new Judge(name, judgeRoles);
    }

    public JudgeBuilder judgesRoles(JudgeRole... judgeRoles){
        element.setSpecialRoles(Arrays.asList(judgeRoles));
        return this;
    }

    public Judge build(){
        return element;
    }

}