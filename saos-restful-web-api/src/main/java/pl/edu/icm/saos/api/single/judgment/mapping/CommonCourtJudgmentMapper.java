package pl.edu.icm.saos.api.single.judgment.mapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.api.services.links.LinksBuilder;
import pl.edu.icm.saos.api.single.judgment.data.representation.CommonCourtJudgmentData;
import pl.edu.icm.saos.api.single.judgment.data.representation.CommonCourtJudgmentData.Division;
import pl.edu.icm.saos.api.single.judgment.views.CommonCourtJudgmentView;
import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.CommonCourtDivision;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;

/**
 * Converts {@link pl.edu.icm.saos.persistence.model.CommonCourtJudgment CommonCourtJudgmnet} specific fields.
 * @author pavtel
 */
@Service
public class CommonCourtJudgmentMapper {

    @Autowired
    private LinksBuilder linksBuilder;

    //------------------------ LOGIC --------------------------
    /**
     * Fill {@link pl.edu.icm.saos.persistence.model.CommonCourtJudgment CommonCourtJudgment} specific fields values
     * into {@link pl.edu.icm.saos.api.single.judgment.views.CommonCourtJudgmentView CommonCourtJudgmentView}.
     * @param representation in which to add values.
     * @param judgment to process.
     */
    public void fillJudgmentsFieldToRepresentation(CommonCourtJudgmentView representation, CommonCourtJudgment judgment){
        fillData(representation.getData(), judgment);
    }


    //------------------------ PRIVATE --------------------------
    private void fillData(CommonCourtJudgmentData data, CommonCourtJudgment judgment) {
        data.setDivision(toDivision(judgment.getCourtDivision()));
    }

    private Division toDivision(CommonCourtDivision courtDivision) {
        Division divisionView = new Division();

        divisionView.setHref(linksBuilder.urlToCcDivision(courtDivision.getId()));
        divisionView.setName(courtDivision.getName());
        divisionView.setCode(courtDivision.getCode());
        divisionView.setType(courtDivision.getType().getName());
        divisionView.setCourt(toCourt(courtDivision.getCourt()));

        return divisionView;
    }

    private CommonCourtJudgmentData.Court toCourt(CommonCourt court) {
        CommonCourtJudgmentData.Court courtView = new CommonCourtJudgmentData.Court();

        courtView.setHref(linksBuilder.urlToCourt(court.getId()));
        courtView.setName(court.getName());
        courtView.setCode(court.getCode());
        courtView.setType(court.getType());


        return courtView;
    }

    //------------------------ SETTERS --------------------------

    public void setLinksBuilder(LinksBuilder linksBuilder) {
        this.linksBuilder = linksBuilder;
    }
}
