package pl.edu.icm.saos.api.builders;

import org.joda.time.LocalDate;
import pl.edu.icm.saos.persistence.model.CcJudgmentKeyword;
import pl.edu.icm.saos.persistence.model.CommonCourtDivision;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.Judge;

import java.util.List;

/**
 * @author pavtel
 */
public class CommonCourtJudgmentBuilder extends CommonCourtJudgment {
    CommonCourtJudgmentBuilder(int id){
        setId(id);
    }

    public CommonCourtJudgmentBuilder caseNumber(String caseNumber){
        setCaseNumber(caseNumber);
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
