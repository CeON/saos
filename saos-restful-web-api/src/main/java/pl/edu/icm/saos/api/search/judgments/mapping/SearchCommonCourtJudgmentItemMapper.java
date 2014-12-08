package pl.edu.icm.saos.api.search.judgments.mapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.icm.saos.api.search.judgments.item.representation.CommonCourtJudgmentItem;
import pl.edu.icm.saos.api.services.links.LinksBuilder;
import pl.edu.icm.saos.search.search.model.JudgmentSearchResult;

import static pl.edu.icm.saos.api.search.judgments.item.representation.CommonCourtJudgmentItem.*;
/**
 * Provides functionality for mapping from {@link pl.edu.icm.saos.search.search.model.JudgmentSearchResult JudgmentSearchResult}
 * fields into {@link pl.edu.icm.saos.api.search.judgments.item.representation.CommonCourtJudgmentItem CommonCourtJudgmentItem} fields.
 * @author pavtel
 */
@Service
public class SearchCommonCourtJudgmentItemMapper {

    @Autowired
    private LinksBuilder linksBuilder;


    //------------------------ LOGIC --------------------------

    /**
     * Fills item (only {@link pl.edu.icm.saos.persistence.model.CommonCourtJudgment CommonCourtJudgment} related
     * fields) fields using {@link pl.edu.icm.saos.search.search.model.JudgmentSearchResult JudgmentSearchResult}
     * @param item representation.
     * @param judgment to process.
     */
    public void fillJudgmentsFieldsToItemRepresentation(CommonCourtJudgmentItem item, JudgmentSearchResult judgment){
        item.setDivision(toDivision(judgment));
    }

    //------------------------ PRIVATE --------------------------

    private Division toDivision(JudgmentSearchResult judgment) {
        Division view = new Division();
        view.setHref(linksBuilder.urlToCcDivision(judgment.getCcCourtDivisionId()));
        view.setId(judgment.getCcCourtDivisionId());
        view.setCode(judgment.getCcCourtDivisionCode());
        view.setName(judgment.getCcCourtDivisionName());
        view.setCourt(toCourt(judgment));
        return view;
    }

    private Court toCourt(JudgmentSearchResult judgment) {
        Court view = new Court();
        view.setHref(linksBuilder.urlToCourt(judgment.getCcCourtId()));
        view.setId(judgment.getCcCourtId());
        view.setCode(judgment.getCcCourtCode());
        view.setName(judgment.getCcCourtName());
        return view;
    }


    //------------------------ SETTERS --------------------------

    public void setLinksBuilder(LinksBuilder linksBuilder) {
        this.linksBuilder = linksBuilder;
    }
}
