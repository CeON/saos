package pl.edu.icm.saos.api.dump.judgment.mapping;

import org.springframework.stereotype.Service;
import pl.edu.icm.saos.api.dump.judgment.item.representation.SupremeCourtJudgmentItem;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamber;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamberDivision;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgmentForm;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Provides functionality for mapping from {@link pl.edu.icm.saos.persistence.model.SupremeCourtJudgment SupremeCourtJudgment}
 * into {@link pl.edu.icm.saos.api.dump.judgment.item.representation.SupremeCourtJudgmentItem SupremeCourtJudgmentItem}.
 * @author pavtel
 */
@Service
public class DumpSupremeCourtJudgmentItemMapper {


    //------------------------ LOGIC --------------------------
    /**
     * Fills item (only {@link pl.edu.icm.saos.persistence.model.SupremeCourtJudgment SupremeCourtJudgment} related
     * fields) fields using {@link pl.edu.icm.saos.persistence.model.SupremeCourtJudgment SupremeCourtJudgment} fields.
     * @param item representation.
     * @param judgment to process.
     */
    public void fillJudgmentsFieldsToItemRepresentation(SupremeCourtJudgmentItem item, SupremeCourtJudgment judgment){
        item.setDivision(toDivision(judgment.getScChamberDivision()));
        item.setPersonnelType(judgment.getPersonnelType());
        item.setForm(toForm(judgment.getScJudgmentForm()));
        item.setChambers(toChambers(judgment.getScChambers()));
    }


    //------------------------ PRIVATE --------------------------
    private SupremeCourtJudgmentItem.Division toDivision(SupremeCourtChamberDivision scChamberDivision) {
        SupremeCourtJudgmentItem.Division view = new SupremeCourtJudgmentItem.Division();
        view.setId(scChamberDivision.getId());
        return view;
    }

    private SupremeCourtJudgmentItem.Form toForm(SupremeCourtJudgmentForm scJudgmentForm) {
        SupremeCourtJudgmentItem.Form view = new SupremeCourtJudgmentItem.Form();
        view.setName(scJudgmentForm.getName());
        return view;
    }

    private List<SupremeCourtJudgmentItem.Chamber> toChambers(List<SupremeCourtChamber> scChambers) {
        if(scChambers == null){
            scChambers = Collections.emptyList();
        }

        return scChambers.stream()
                .map(chamber -> toChamberView(chamber))
                .collect(Collectors.toList())
                ;
    }

    private SupremeCourtJudgmentItem.Chamber toChamberView(SupremeCourtChamber chamber) {
        SupremeCourtJudgmentItem.Chamber view = new SupremeCourtJudgmentItem.Chamber();
        view.setId(chamber.getId());
        return view;
    }
}
