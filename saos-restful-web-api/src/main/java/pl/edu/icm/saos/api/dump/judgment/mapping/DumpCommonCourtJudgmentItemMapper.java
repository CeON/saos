package pl.edu.icm.saos.api.dump.judgment.mapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.icm.saos.api.dump.judgment.item.representation.CommonCourtJudgmentItem;
import pl.edu.icm.saos.api.single.judgment.mapping.CommonCourtJudgmentMapper;
import pl.edu.icm.saos.persistence.model.CommonCourtDivision;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;

/**
 * @author pavtel
 */
@Service
public class DumpCommonCourtJudgmentItemMapper {

    @Autowired
    private CommonCourtJudgmentMapper commonCourtJudgmentMapper;

    public void fillJudgmentsFieldsToItemRepresentation(CommonCourtJudgmentItem representation, CommonCourtJudgment judgment){
        representation.setDivision(toDivision(judgment.getCourtDivision()));
        representation.setKeywords(commonCourtJudgmentMapper.toListFromKeywords(judgment.getKeywords()));
    }

    private CommonCourtJudgmentItem.Division toDivision(CommonCourtDivision courtDivision) {
        CommonCourtJudgmentItem.Division view = new CommonCourtJudgmentItem.Division();
        view.setId(courtDivision.getId());
        return view;
    }
}
