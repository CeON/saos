package pl.edu.icm.saos.persistence.builder;

import pl.edu.icm.saos.persistence.model.CcJudgmentKeyword;
import pl.edu.icm.saos.persistence.model.CommonCourtDivisionType;
import pl.edu.icm.saos.persistence.model.SourceCode;

/**
 * @author pavtel
 */
public class BuildersFactory {

    public static LawJournalEntryBuilder lawJournalEntry(){
        return new LawJournalEntryBuilder();
    }

    public static JudgmentReferencedRegulationBuilder judgmentReferencedRegulation(){
        return new JudgmentReferencedRegulationBuilder();
    }

    public static JudgeBuilder judge(String name){
        return new JudgeBuilder(name);
    }

    public static JudgmentSourceInfoBuilder judgmentSourceInfo(SourceCode sourceCode){
        return new JudgmentSourceInfoBuilder(sourceCode);
    }

    public static CommonCourtDivisionBuilder commonCourtDivision(int divisionId){
        return new CommonCourtDivisionBuilder(divisionId);
    }

    public static CommonCourtDivisionType commonCourtDivisionType(String name, String code){
        CommonCourtDivisionType divisionType = new CommonCourtDivisionType();
        divisionType.setCode(code);
        divisionType.setName(name);

        return divisionType;
    }

    public static CommonCourtJudgmentBuilder commonCourtJudgmentWrapper(int id){
        return new CommonCourtJudgmentBuilder(id);
    }

    public static CommonCourtBuilder commonCourt(int courtId){
        return new CommonCourtBuilder(courtId);
    }

    public static CcJudgmentKeyword keyword(String keyword){
        return new CcJudgmentKeyword(keyword);
    }

}
