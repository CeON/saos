package pl.edu.icm.saos.api.single.judgment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.icm.saos.api.single.judgment.mapping.CommonCourtJudgmentMapper;
import pl.edu.icm.saos.api.single.judgment.mapping.ConstitutionalTribunalJudgmentMapper;
import pl.edu.icm.saos.api.single.judgment.mapping.JudgmentMapper;
import pl.edu.icm.saos.api.single.judgment.mapping.SupremeCourtJudgmentMapper;
import pl.edu.icm.saos.api.single.judgment.views.CommonCourtJudgmentView;
import pl.edu.icm.saos.api.single.judgment.views.ConstitutionalTribunalJudgmentView;
import pl.edu.icm.saos.api.single.judgment.views.JudgmentView;
import pl.edu.icm.saos.api.single.judgment.views.SupremeCourtJudgmentView;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.ConstitutionalTribunalJudgment;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment;

/**
 * Provides functionality for building success object view for single judgment.
 * Success object can be easily serialized as json.
 * @author pavtel
 */
@Service("singleJudgmentSuccessRepresentationBuilder")
public class SingleJudgmentSuccessRepresentationBuilder {

    @Autowired
    private JudgmentMapper judgmentMapper;

    @Autowired
    private CommonCourtJudgmentMapper commonCourtJudgmentMapper;

    @Autowired
    private SupremeCourtJudgmentMapper supremeCourtJudgmentMapper;

    @Autowired
    private ConstitutionalTribunalJudgmentMapper constitutionalTribunalJudgmentMapper;


    //------------------------ LOGIC --------------------------
    /**
     * Constructs judgment's view {@link pl.edu.icm.saos.api.single.judgment.views.JudgmentView JudgmentView}.
     * @param judgment to process.
     * @return representation.
     */
    public JudgmentView<?> build(Judgment judgment){
        JudgmentView<?> judgmentView = initializeViewAndFillSpecificFields(judgment);
        judgmentMapper.fillJudgmentsFieldToRepresentation(judgmentView, judgment);
        return judgmentView;
    }

    //------------------------ PRIVATE --------------------------

    private JudgmentView<?> initializeViewAndFillSpecificFields(Judgment judgment){
        switch (judgment.getCourtType()){
            case COMMON:
                CommonCourtJudgment commonCourtJudgment = (CommonCourtJudgment) judgment;
                CommonCourtJudgmentView ccJudgmentView = new CommonCourtJudgmentView();
                commonCourtJudgmentMapper.fillJudgmentsFieldToRepresentation(ccJudgmentView, commonCourtJudgment);
                return ccJudgmentView;
            case SUPREME:
                SupremeCourtJudgment scJudgment = (SupremeCourtJudgment) judgment;
                SupremeCourtJudgmentView scJudgmentView = new SupremeCourtJudgmentView();
                supremeCourtJudgmentMapper.fillJudgmentsFieldToRepresentation(scJudgmentView, scJudgment);
                return scJudgmentView;
            case CONSTITUTIONAL_TRIBUNAL:
                ConstitutionalTribunalJudgment ctJudgment = (ConstitutionalTribunalJudgment) judgment;
                ConstitutionalTribunalJudgmentView ctJudgmentView = new ConstitutionalTribunalJudgmentView();
                constitutionalTribunalJudgmentMapper.fillJudgmentsFieldToRepresentation(ctJudgmentView, ctJudgment);
                return ctJudgmentView;
            default:
                return new CommonCourtJudgmentView();
        }
    }

    //------------------------ SETTERS --------------------------
    public void setJudgmentMapper(JudgmentMapper judgmentMapper) {
        this.judgmentMapper = judgmentMapper;
    }

    public void setCommonCourtJudgmentMapper(CommonCourtJudgmentMapper commonCourtJudgmentMapper) {
        this.commonCourtJudgmentMapper = commonCourtJudgmentMapper;
    }

    public void setSupremeCourtJudgmentMapper(SupremeCourtJudgmentMapper supremeCourtJudgmentMapper) {
        this.supremeCourtJudgmentMapper = supremeCourtJudgmentMapper;
    }

    public void setConstitutionalTribunalJudgmentMapper(ConstitutionalTribunalJudgmentMapper constitutionalTribunalJudgmentMapper) {
        this.constitutionalTribunalJudgmentMapper = constitutionalTribunalJudgmentMapper;
    }
}
