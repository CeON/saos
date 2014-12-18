package pl.edu.icm.saos.api.search.judgments.mapping;

import org.springframework.stereotype.Service;
import pl.edu.icm.saos.api.search.judgments.item.representation.ConstitutionalTribunalJudgmentItem;
import pl.edu.icm.saos.search.search.model.JudgmentSearchResult;

import java.util.Collections;
import java.util.List;

import static pl.edu.icm.saos.api.single.judgment.data.representation.ConstitutionalTribunalJudgmentData.DissentingOpinion;

/**
 * Provides functionality for mapping from {@link pl.edu.icm.saos.search.search.model.JudgmentSearchResult JudgmentSearchResult}
 * fields into {@link pl.edu.icm.saos.api.search.judgments.item.representation.ConstitutionalTribunalJudgmentItem ConstitutionalTribunalJudgmentItem} fields.
 * @author pavtel
 */
@Service
public class SearchConstitutionalTribunalJudgmentItemMapper {


    //------------------------ LOGIC --------------------------

    /**
     * Fills item (only {@link pl.edu.icm.saos.persistence.model.CommonCourtJudgment CommonCourtJudgment} related
     * fields) fields using {@link pl.edu.icm.saos.search.search.model.JudgmentSearchResult JudgmentSearchResult}
     * @param item representation.
     * @param judgment to process.
     */
    public void fillJudgmentsFieldsToItemRepresentation(ConstitutionalTribunalJudgmentItem item, JudgmentSearchResult judgment){
        item.setDissentingOpinions(toOpinions(judgment));
    }

    private List<DissentingOpinion> toOpinions(JudgmentSearchResult judgment) {
        //TODO add implementation when ctJudgment will be in search index
        return Collections.emptyList();
    }
}
