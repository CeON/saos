package pl.edu.icm.saos.search.indexing;

import org.joda.time.LocalDate;
import org.springframework.test.util.ReflectionTestUtils;

import pl.edu.icm.saos.persistence.model.CcJudgmentKeyword;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.CourtCase;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judgment.JudgmentType;
import pl.edu.icm.saos.persistence.model.JudgmentReferencedRegulation;

/**
 * @author madryk
 */
public class CommonCourtJudgmentBuilder {

    private CommonCourtJudgment judgment = new CommonCourtJudgment();
    
    public CommonCourtJudgment build() {
        return judgment;
    }
    
    public CommonCourtJudgment buildAndReset() {
        CommonCourtJudgment temp = judgment;
        judgment = new CommonCourtJudgment();
        return temp;
    }
    
    public CommonCourtJudgmentBuilder withId(int id) {
        ReflectionTestUtils.setField(judgment, "id", id);
        return this;
    }
    
    public CommonCourtJudgmentBuilder withCourtCases(CourtCase ... courtCases) {
        for (CourtCase courtCase : courtCases) {
            judgment.addCourtCase(courtCase);
        }
        
        return this;
    }
    
    public CommonCourtJudgmentBuilder withKeywords(String ... keywords) {
        for (String keyword : keywords) {
            judgment.addKeyword(new CcJudgmentKeyword(keyword));
        }
        
        return this;
    }
    
    public CommonCourtJudgmentBuilder withJudges(Judge ... judges) {
        for (Judge judge : judges) {
            judgment.addJudge(judge);
        }
        
        return this;
    }
    
    public CommonCourtJudgmentBuilder withLegalBases(String ... legalBases) {
        for (String legalBase : legalBases) {
            judgment.addLegalBase(legalBase);
        }
        
        return this;
    }
    
    public CommonCourtJudgmentBuilder withReferencedRegulations(String ... referencedRegulations) {
        for (String referencedRegulationString : referencedRegulations) {
            JudgmentReferencedRegulation referencedRegulation = new JudgmentReferencedRegulation();
            referencedRegulation.setRawText(referencedRegulationString);
            judgment.addReferencedRegulation(referencedRegulation);
        }
        
        return this;
    }
    
    public CommonCourtJudgmentBuilder withJudgmentDate(LocalDate judgmentDate) {
        judgment.setJudgmentDate(new LocalDate(2014, 9, 4));
        return this;
    }
    
    public CommonCourtJudgmentBuilder withJudgmentType(JudgmentType judgmentType) {
        judgment.setJudgmentType(judgmentType);
        return this;
    }
    
    public CommonCourtJudgmentBuilder withTextContent(String content) {
        judgment.setTextContent(content);
        return this;
    }
}
