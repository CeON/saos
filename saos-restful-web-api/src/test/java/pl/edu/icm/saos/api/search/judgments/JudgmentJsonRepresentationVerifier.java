package pl.edu.icm.saos.api.search.judgments;

import org.springframework.test.web.servlet.ResultActions;
import pl.edu.icm.saos.api.services.FieldsDefinition;
import pl.edu.icm.saos.api.support.TestPersistenceObjectsContext;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judgment;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static pl.edu.icm.saos.api.services.Constansts.SINGLE_JUDGMENTS_PATH;

/**
 * @author pavtel
 */
public class JudgmentJsonRepresentationVerifier {

        @Deprecated
        /**
         * use #verifyBasicFields(ResultActions actions, String pathPrefix, TestPersistenceObjectsContext objectsContext) instead
         */
        public static void verifyBasicFields(ResultActions actions, String pathPrefix) throws Exception{
                actions
                        .andExpect(jsonPath(pathPrefix + ".href").value(endsWith(SINGLE_JUDGMENTS_PATH + "/" + FieldsDefinition.JC.JUDGMENT_ID)))
                        .andExpect(jsonPath(pathPrefix + ".courtCases").value(iterableWithSize(1)))
                        .andExpect(jsonPath(pathPrefix + ".courtCases.[0].caseNumber").value(FieldsDefinition.JC.CASE_NUMBER))
                        .andExpect(jsonPath(pathPrefix + ".judgmentType").value(Judgment.JudgmentType.SENTENCE.name()))

                        .andExpect(jsonPath(pathPrefix + ".judgmentDate").value(FieldsDefinition.JC.DATE_YEAR + "-" + FieldsDefinition.JC.DATE_MONTH + "-" + FieldsDefinition.JC.DATE_DAY))

                        .andExpect(jsonPath(pathPrefix + ".judges").isArray())
                        .andExpect(jsonPath(pathPrefix + ".judges").value(iterableWithSize(3)))
                        .andExpect(jsonPath(pathPrefix + ".judges.[0].name").value(FieldsDefinition.JC.PRESIDING_JUDGE_NAME))
                        .andExpect(jsonPath(pathPrefix + ".judges.[0].specialRoles").value(iterableWithSize(1)))
                        .andExpect(jsonPath(pathPrefix + ".judges.[0].specialRoles.[0]").value(Judge.JudgeRole.PRESIDING_JUDGE.name()))
                        .andExpect(jsonPath(pathPrefix + ".judges.[1].name").value(FieldsDefinition.JC.SECOND_JUDGE_NAME))
                        .andExpect(jsonPath(pathPrefix + ".judges.[1].specialRoles").value(emptyIterable()))
                        .andExpect(jsonPath(pathPrefix + ".judges.[2].name").value(FieldsDefinition.JC.THIRD_JUDGE_NAME))
                        .andExpect(jsonPath(pathPrefix + ".judges.[2].specialRoles").value(emptyIterable()))
                        ;
        }

        /**
         * Verifies basic judgment's fields representation
         * @param actions an instance of ResultActions ("represents json view")
         * @param pathPrefix json path prefix
         * @param objectsContext contains information about objects ids
         * @throws Exception if json is different than expected
         */
        public static void verifyBasicFields(ResultActions actions, String pathPrefix, TestPersistenceObjectsContext objectsContext) throws Exception{
                actions
                        .andExpect(jsonPath(pathPrefix+".href").value(endsWith(SINGLE_JUDGMENTS_PATH+"/"+ objectsContext.getJudgmentId())))
                        .andExpect(jsonPath(pathPrefix+".courtCases").value(iterableWithSize(1)))
                        .andExpect(jsonPath(pathPrefix+".courtCases.[0].caseNumber").value(FieldsDefinition.JC.CASE_NUMBER))
                        .andExpect(jsonPath(pathPrefix+".judgmentType").value(Judgment.JudgmentType.SENTENCE.name()))

                        .andExpect(jsonPath(pathPrefix + ".judgmentDate").value(FieldsDefinition.JC.DATE_YEAR + "-" + FieldsDefinition.JC.DATE_MONTH + "-" + FieldsDefinition.JC.DATE_DAY))

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
