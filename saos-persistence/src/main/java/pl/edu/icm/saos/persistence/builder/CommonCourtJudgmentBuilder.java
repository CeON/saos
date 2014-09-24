package pl.edu.icm.saos.persistence.builder;

import org.joda.time.LocalDate;
import pl.edu.icm.saos.persistence.model.*;
import pl.edu.icm.saos.persistence.model.Judgment.JudgmentType;

import java.util.List;

/**
 * @author pavtel
 * Simplified {@link pl.edu.icm.saos.persistence.model.CommonCourtJudgment CommonCourtJudgment} creation.
 * Do not use it in conjugation with persistence's repositories.
 */
public class CommonCourtJudgmentBuilder {

    private CommonCourtJudgment element;

    CommonCourtJudgmentBuilder(int id){
        element = new SpecialCommonCourtJudgment(id);
    }

    public CommonCourtJudgmentBuilder courtCases(List<CourtCase> courtCases){
        courtCases.stream().forEach(element::addCourtCase);
        return this;
    }

    public CommonCourtJudgmentBuilder judgmentType(JudgmentType judgmentType){
        element.setJudgmentType(judgmentType);
        return this;
    }

    public CommonCourtJudgmentBuilder judgmentDate(LocalDate judgmentDate){
        element.setJudgmentDate(judgmentDate);
        return this;
    }

    public CommonCourtJudgmentBuilder division(CommonCourtDivision division){
        element.setCourtDivision(division);
        return this;
    }

    public CommonCourtJudgmentBuilder judges(List<Judge> judges){
        judges.stream()
                .forEach(element::addJudge);

        return this;
    }

    public CommonCourtJudgmentBuilder keywords(List<CcJudgmentKeyword> keywords){
        keywords.stream()
                .forEach(element::addKeyword);

        return this;
    }

    public CommonCourtJudgment build(){
        return element;
    }

    private static class SpecialCommonCourtJudgment extends CommonCourtJudgment {
        private SpecialCommonCourtJudgment(int id){
            setId(id);
        }
    }
}
