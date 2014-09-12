package pl.edu.icm.saos.api.builders;

import java.util.List;

import org.joda.time.LocalDate;

import pl.edu.icm.saos.persistence.model.CcJudgmentKeyword;
import pl.edu.icm.saos.persistence.model.CommonCourtDivision;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.CourtCase;
import pl.edu.icm.saos.persistence.model.Judge;

/**
 * @author pavtel
 */
public class CommonCourtJudgmentBuilder extends CommonCourtJudgment {
    CommonCourtJudgmentBuilder(int id){
        setId(id);
    }

    public CommonCourtJudgmentBuilder courtCases(List<CourtCase> courtCases){
        courtCases.stream().forEach(this::addCourtCase);
        return this;
    }

    public CommonCourtJudgmentBuilder judgmentType(JudgmentType judgmentType){
        setJudgmentType(judgmentType);
        return this;
    }

    public CommonCourtJudgmentBuilder judgmentDate(LocalDate judgmentDate){
        setJudgmentDate(judgmentDate);
        return this;
    }

    public CommonCourtJudgmentBuilder division(CommonCourtDivision division){
        setCourtDivision(division);
        return this;
    }

    public CommonCourtJudgmentBuilder judges(List<Judge> judges){
        judges.stream()
                .forEach(this::addJudge);

        return this;
    }

    public CommonCourtJudgmentBuilder keywords(List<CcJudgmentKeyword> keywords){
        keywords.stream()
                .forEach(this::addKeyword);

        return this;
    }
}
