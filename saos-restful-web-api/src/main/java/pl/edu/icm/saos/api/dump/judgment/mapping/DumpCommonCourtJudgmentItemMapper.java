package pl.edu.icm.saos.api.dump.judgment.mapping;

import org.springframework.stereotype.Service;
import pl.edu.icm.saos.api.dump.judgment.item.representation.CommonCourtJudgmentItem;
import pl.edu.icm.saos.persistence.model.CommonCourtDivision;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;

/**
 * Provides functionality for mapping from {@link pl.edu.icm.saos.persistence.model.CommonCourtJudgment CommonCourtJudgment}
 * into {@link pl.edu.icm.saos.api.dump.judgment.item.representation.CommonCourtJudgmentItem CommonCourtJudgmentItem}.
 * @author pavtel
 */
@Service
public class DumpCommonCourtJudgmentItemMapper {

    //------------------------ LOGIC --------------------------

    /**
     * Fills item (only {@link pl.edu.icm.saos.persistence.model.CommonCourtJudgment CommonCourtJudgment} related
     * fields) fields using {@link pl.edu.icm.saos.persistence.model.CommonCourtJudgment CommonCourtJudgment} fields.
     * @param item representation.
     * @param judgment to process.
     */
    public void fillJudgmentsFieldsToItemRepresentation(CommonCourtJudgmentItem item, CommonCourtJudgment judgment){
        item.setDivision(toDivision(judgment.getCourtDivision()));
    }

    //------------------------ PRIVATE --------------------------
    private CommonCourtJudgmentItem.Division toDivision(CommonCourtDivision courtDivision) {
        CommonCourtJudgmentItem.Division view = new CommonCourtJudgmentItem.Division();
        view.setId(courtDivision.getId());
        return view;
    }

}
