package pl.edu.icm.saos.api.single.judgment.mapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.icm.saos.api.services.links.LinksBuilder;
import pl.edu.icm.saos.api.single.judgment.data.representation.SupremeCourtJudgmentData;
import pl.edu.icm.saos.api.single.judgment.views.SupremeCourtJudgmentView;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamber;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamberDivision;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgmentForm;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static pl.edu.icm.saos.api.single.judgment.data.representation.SupremeCourtJudgmentData.Division;
import static pl.edu.icm.saos.api.single.judgment.data.representation.SupremeCourtJudgmentData.Chamber;
import static pl.edu.icm.saos.api.single.judgment.data.representation.SupremeCourtJudgmentData.Form;

/**
 * Converts {@link pl.edu.icm.saos.persistence.model.SupremeCourtJudgment SupremeCourtJudgment} specific fields.
 * @author pavtel
 */
@Service
public class SupremeCourtJudgmentMapper {

    @Autowired
    private LinksBuilder linksBuilder;

    //------------------------ LOGIC --------------------------
    /**
     * Fills {@link pl.edu.icm.saos.persistence.model.SupremeCourtJudgment SupremeCourtJudgment} specific fields values
     * into {@link pl.edu.icm.saos.api.single.judgment.views.SupremeCourtJudgmentView SupremeCourtJudgmentView}.
     * @param representation in which to add values.
     * @param judgment to process.
     */
    public void fillJudgmentsFieldToRepresentation(SupremeCourtJudgmentView representation, SupremeCourtJudgment judgment){
        fillData(representation.getData(), judgment);
    }

    //------------------------ PRIVATE --------------------------
    private void fillData(SupremeCourtJudgmentData data, SupremeCourtJudgment judgment) {
        data.setPersonnelType(judgment.getPersonnelType());
        data.setDivision(toDivisionView(judgment.getScChamberDivision()));
        data.setForm(toFormView(judgment.getScJudgmentForm()));
        data.setChambers(toChambersView(judgment.getScChambers()));
    }



    private Division toDivisionView(SupremeCourtChamberDivision division) {
        Division view = new Division();

        view.setHref(linksBuilder.urlToScDivision(division.getId()));
        view.setName(division.getName());
        view.setChamber(toChamberView(division.getScChamber()));

        return view;
    }

    private Chamber toChamberView(SupremeCourtChamber chamber) {
        Chamber view = new Chamber();

        view.setHref(linksBuilder.urlToScChamber(chamber.getId()));
        view.setName(chamber.getName());

        return view;
    }

    private Form toFormView(SupremeCourtJudgmentForm scForm) {
        if(scForm == null){
            return null;
        }

        Form view = new Form();

        view.setName(scForm.getName());

        return view;
    }

    private List<Chamber> toChambersView(List<SupremeCourtChamber> scChambers) {
        if(scChambers == null){
            scChambers = Collections.emptyList();
        }

        return scChambers.stream()
                .map(chamber -> toChamberView(chamber))
                .collect(Collectors.toList());
    }

    //------------------------ SETTERS --------------------------
    public void setLinksBuilder(LinksBuilder linksBuilder) {
        this.linksBuilder = linksBuilder;
    }
}
