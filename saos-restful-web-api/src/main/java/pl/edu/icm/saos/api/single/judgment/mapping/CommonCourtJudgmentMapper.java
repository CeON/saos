package pl.edu.icm.saos.api.single.judgment.mapping;

import org.springframework.beans.factory.annotation.Autowired;
import pl.edu.icm.saos.api.services.links.LinksBuilder;
import pl.edu.icm.saos.api.services.representations.success.Href;
import pl.edu.icm.saos.api.single.judgment.CommonCourtJudgmentView;
import pl.edu.icm.saos.api.single.judgment.representation.CommonCourtJudgmentData;
import pl.edu.icm.saos.persistence.model.CcJudgmentKeyword;
import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.CommonCourtDivision;
import pl.edu.icm.saos.persistence.model.CommonCourtJudgment;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static pl.edu.icm.saos.api.single.judgment.representation.CommonCourtJudgmentData.Division;

/**
 * Converts {@link pl.edu.icm.saos.persistence.model.CommonCourtJudgment CommonCourtJudgmnet} fields.
 * @author pavtel
 */
public class CommonCourtJudgmentMapper {

    @Autowired
    private LinksBuilder linksBuilder;

    public void fillJudgmentsFieldToRepresentation(CommonCourtJudgmentView representation, CommonCourtJudgment judgment){
        fillData(representation.getData(), judgment);
    }

    private void fillData(CommonCourtJudgmentData data, CommonCourtJudgment judgment) {
        data.setKeywords(toListFromKeywords(judgment.getKeywords()));
        data.setDivision(toDivision(judgment.getCourtDivision()));
    }



    private List<String> toListFromKeywords(List<CcJudgmentKeyword> keywords) {
        if(keywords == null)
            keywords = Collections.emptyList();

        List<String> list = keywords.stream()
                .map(CcJudgmentKeyword::getPhrase)
                .collect(Collectors.toList());

        return list;
    }

    private Division toDivision(CommonCourtDivision courtDivision) {
        Division divisionView = new Division();

        Href href = new Href();
        href.setHref(linksBuilder.urlToDivision(courtDivision.getId()));
        divisionView.setHref(href);

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

        Href courtParent = new Href();


        return null;
    }
}
