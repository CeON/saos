package pl.edu.icm.saos.api.dump.judgment.mapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.icm.saos.api.dump.judgment.item.representation.ConstitutionalTribunalJudgmentItem;
import pl.edu.icm.saos.api.single.judgment.mapping.ConstitutionalTribunalJudgmentMapper;
import pl.edu.icm.saos.persistence.model.ConstitutionalTribunalJudgment;

/**
 * Provides functionality for mapping from {@link pl.edu.icm.saos.persistence.model.ConstitutionalTribunalJudgment ConstitutionalTribunalJudgment}
 * into {@link pl.edu.icm.saos.api.dump.judgment.item.representation.CommonCourtJudgmentItem CommonCourtJudgmentItem}.
 * @author pavtel
 */
@Service
public class DumpConstitutionalTribunalJudgmentItemMapper {

    @Autowired
    private ConstitutionalTribunalJudgmentMapper constitutionalTribunalJudgmentMapper;


    //------------------------ LOGIC --------------------------

    /**
     * Fills item (only {@link pl.edu.icm.saos.persistence.model.ConstitutionalTribunalJudgment ConstitutionalTribunalJudgment} related
     * fields) fields using {@link pl.edu.icm.saos.persistence.model.ConstitutionalTribunalJudgment ConstitutionalTribunalJudgment} fields.
     * @param item representation.
     * @param judgment to process.
     */
    public void fillJudgmentsFieldsToItemRepresentation(ConstitutionalTribunalJudgmentItem item, ConstitutionalTribunalJudgment judgment){
        item.setDissentingOpinions(constitutionalTribunalJudgmentMapper.toOpinions(judgment.getDissentingOpinions()));
    }

    //------------------------ SETTERS --------------------------

    public void setConstitutionalTribunalJudgmentMapper(ConstitutionalTribunalJudgmentMapper constitutionalTribunalJudgmentMapper) {
        this.constitutionalTribunalJudgmentMapper = constitutionalTribunalJudgmentMapper;
    }
}
