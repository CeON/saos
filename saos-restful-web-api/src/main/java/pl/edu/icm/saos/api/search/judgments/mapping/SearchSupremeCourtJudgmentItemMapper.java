package pl.edu.icm.saos.api.search.judgments.mapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.icm.saos.api.search.judgments.item.representation.SupremeCourtJudgmentItem;
import pl.edu.icm.saos.api.services.links.LinksBuilder;
import pl.edu.icm.saos.search.search.model.JudgmentSearchResult;
import pl.edu.icm.saos.search.search.model.SupremeCourtChamberResult;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static pl.edu.icm.saos.api.search.judgments.item.representation.SupremeCourtJudgmentItem.*;

/**
 * Provides functionality for mapping from {@link pl.edu.icm.saos.search.search.model.JudgmentSearchResult JudgmentSearchResult}
 * fields into {@link pl.edu.icm.saos.api.search.judgments.item.representation.SupremeCourtJudgmentItem SupremeCourtJudgmentItem} fields.
 * @author pavtel
 */
@Service
public class SearchSupremeCourtJudgmentItemMapper {

    @Autowired
    private LinksBuilder linksBuilder;


    //------------------------ LOGIC --------------------------
    /**
     * Fills item (only {@link pl.edu.icm.saos.persistence.model.SupremeCourtJudgment SupremeCourtJudgment} related
     * fields) fields using {@link pl.edu.icm.saos.search.search.model.JudgmentSearchResult JudgmentSearchResult}
     * @param item representation.
     * @param judgment to process.
     */
    public void fillJudgmentsFieldsToItemRepresentation(SupremeCourtJudgmentItem item, JudgmentSearchResult judgment){
        item.setPersonnelType(judgment.getScPersonnelType());
        item.setDivision(toDivision(judgment));
    }

    //------------------------ PRIVATE --------------------------
    private Division toDivision(JudgmentSearchResult judgment) {
        Division view = new Division();

        view.setHref(linksBuilder.urlToScDivision(judgment.getScCourtDivisionId()));
        view.setId(judgment.getScCourtDivisionId());
        view.setName(judgment.getScCourtDivisionName());
        view.setChambers(toChambers(judgment.getScCourtChambers()));

        return view;
    }

    private List<Chamber> toChambers(List<SupremeCourtChamberResult> courtChambers) {
        if(courtChambers == null){
            courtChambers = Collections.emptyList();
        }

        return courtChambers.stream()
                .map(chamber -> toChamberView(chamber))
                .collect(Collectors.toList());
    }

    private Chamber toChamberView(SupremeCourtChamberResult chamber) {
        Chamber view = new Chamber();

        view.setHref(linksBuilder.urlToScChamber(chamber.getId()));
        view.setId(chamber.getId());
        view.setName(chamber.getName());

        return view;
    }


    //------------------------ SETTERS --------------------------
    public void setLinksBuilder(LinksBuilder linksBuilder) {
        this.linksBuilder = linksBuilder;
    }
}
