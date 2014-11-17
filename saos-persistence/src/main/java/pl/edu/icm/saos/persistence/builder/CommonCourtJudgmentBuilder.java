package pl.edu.icm.saos.persistence.builder;

import java.util.List;

import org.joda.time.LocalDate;

import pl.edu.icm.saos.persistence.model.CcJudgmentKeyword;
import pl.edu.icm.saos.persistence.model.CommonCourtDivision;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.CourtCase;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judgment.JudgmentType;
import pl.edu.icm.saos.persistence.model.JudgmentReferencedRegulation;
import pl.edu.icm.saos.persistence.model.SourceCode;

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

    CommonCourtJudgmentBuilder(){
        element = new CommonCourtJudgment();
    }

    public CommonCourtJudgmentBuilder addCourtCase(CourtCase courtCase){
        element.addCourtCase(courtCase);
        return this;
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

    public CommonCourtJudgmentBuilder decision(String decision){
        element.setDecision(decision);
        return this;
    }

    public CommonCourtJudgmentBuilder courtReporters(List<String> courtReporters){
        courtReporters.stream()
                .forEach(element::addCourtReporter);
        return this;
    }

    public CommonCourtJudgmentBuilder summary(String summary){
        element.setSummary(summary);
        return this;
    }

    public CommonCourtJudgmentBuilder type(JudgmentType judgmentType){
        element.setJudgmentType(judgmentType);
        return this;
    }


    public CommonCourtJudgmentBuilder referencedRegulations(List<JudgmentReferencedRegulation> referencedRegulations) {
        referencedRegulations.stream()
                .forEach(element::addReferencedRegulation);

        return this;
    }

    public CommonCourtJudgmentBuilder legalBases(List<String> legalBases) {
        legalBases.stream()
                .forEach(element::addLegalBase);

        return this;
    }

    public CommonCourtJudgmentBuilder keywords(List<CcJudgmentKeyword> keywords){
        keywords.stream()
                .forEach(element::addKeyword);

        return this;
    }

    public CommonCourtJudgmentBuilder textContent(String textContent) {
        element.setTextContent(textContent);
        return this;
    }
    
    
    public CommonCourtJudgmentBuilder sourceCode(SourceCode sourceCode) {
        element.getSourceInfo().setSourceCode(sourceCode);
        return this;
    }
    
    

    public CommonCourtJudgmentBuilder sourceJudgmentId(String sourceJudgmentId) {
        element.getSourceInfo().setSourceJudgmentId(sourceJudgmentId);
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
