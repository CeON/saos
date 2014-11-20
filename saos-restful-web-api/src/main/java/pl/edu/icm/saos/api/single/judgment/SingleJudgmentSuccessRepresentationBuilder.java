package pl.edu.icm.saos.api.single.judgment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.icm.saos.api.single.judgment.mapping.CommonCourtJudgmentMapper;
import pl.edu.icm.saos.api.single.judgment.mapping.JudgmentMapper;
import pl.edu.icm.saos.api.single.judgment.mapping.SupremeCourtJudgmentMapper;
import pl.edu.icm.saos.api.single.judgment.views.CommonCourtJudgmentView;
import pl.edu.icm.saos.api.single.judgment.views.JudgmentView;
import pl.edu.icm.saos.api.single.judgment.views.SupremeCourtJudgmentView;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment;

/**
 * Provides functionality for building success object view for single judgment.
 * Success object can be easily serialized as json.
 * @author pavtel
 */
@Component("singleJudgmentSuccessRepresentationBuilder")
public class SingleJudgmentSuccessRepresentationBuilder {

    @Autowired
    private JudgmentMapper judgmentMapper;

    @Autowired
    private CommonCourtJudgmentMapper commonCourtJudgmentMapper;

    @Autowired
    private SupremeCourtJudgmentMapper supremeCourtJudgmentMapper;


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
        if(judgment.isInstanceOfCommonCourtJudgment()) {
            CommonCourtJudgment commonCourtJudgment = (CommonCourtJudgment) judgment;
            CommonCourtJudgmentView judgmentView = new CommonCourtJudgmentView();
            commonCourtJudgmentMapper.fillJudgmentsFieldToRepresentation(judgmentView, commonCourtJudgment);
            return judgmentView;
        } else if(judgment.isInstanceOfSupremeCourtJudgment()){
            SupremeCourtJudgment scJudgment = (SupremeCourtJudgment) judgment;
            SupremeCourtJudgmentView judgmentView = new SupremeCourtJudgmentView();
            supremeCourtJudgmentMapper.fillJudgmentsFieldToRepresentation(judgmentView, scJudgment);
            return judgmentView;
        } else {
            //default
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
}
