package pl.edu.icm.saos.api.judgments;

import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static pl.edu.icm.saos.api.utils.Constansts.JUDGMENTS_PATH;

import org.springframework.test.web.servlet.ResultActions;

import pl.edu.icm.saos.api.utils.FieldsDefinition;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judgment;

/**
 * @author pavtel
 */
public class JudgmentRepresentationVerifier {


    public static void verifyBasicFields(ResultActions actions, String pathPrefix) throws Exception{
        actions
                .andExpect(jsonPath(pathPrefix+".href").value(endsWith(JUDGMENTS_PATH+"/"+ FieldsDefinition.JC.JUDGMENT_ID)))
                .andExpect(jsonPath(pathPrefix+".courtCases").value(iterableWithSize(1)))
                .andExpect(jsonPath(pathPrefix+".courtCases.[0].caseNumber").value(FieldsDefinition.JC.CASE_NUMBER))
                .andExpect(jsonPath(pathPrefix+".judgmentType").value(Judgment.JudgmentType.SENTENCE.name()))


                .andExpect(jsonPath(pathPrefix+".judgmentDate").value(FieldsDefinition.JC.DATE_YEAR + "-" + FieldsDefinition.JC.DATE_MONTH + "-" + FieldsDefinition.JC.DATE_DAY))

                .andExpect(jsonPath(pathPrefix+".judges").isArray())
                .andExpect(jsonPath(pathPrefix+".judges").value(iterableWithSize(3)))
                .andExpect(jsonPath(pathPrefix+".judges.[0].name").value(FieldsDefinition.JC.PRESIDING_JUDGE_NAME))
                .andExpect(jsonPath(pathPrefix+".judges.[0].specialRoles").value(iterableWithSize(1)))
                .andExpect(jsonPath(pathPrefix+".judges.[0].specialRoles.[0]").value(Judge.JudgeRole.PRESIDING_JUDGE.name()))
                .andExpect(jsonPath(pathPrefix+".judges.[1].name").value(FieldsDefinition.JC.SECOND_JUDGE_NAME))
                .andExpect(jsonPath(pathPrefix+".judges.[1].specialRoles").value(emptyIterable()))
                .andExpect(jsonPath(pathPrefix+".judges.[2].name").value(FieldsDefinition.JC.THIRD_JUDGE_NAME))
                .andExpect(jsonPath(pathPrefix+".judges.[2].specialRoles").value(emptyIterable()))
                ;
    }





}
