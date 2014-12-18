package pl.edu.icm.saos.api.single.judgment.mapping;

import org.springframework.stereotype.Service;
import pl.edu.icm.saos.api.single.judgment.data.representation.ConstitutionalTribunalJudgmentData;
import pl.edu.icm.saos.api.single.judgment.views.ConstitutionalTribunalJudgmentView;
import pl.edu.icm.saos.persistence.model.ConstitutionalTribunalJudgment;
import pl.edu.icm.saos.persistence.model.ConstitutionalTribunalJudgmentDissentingOpinion;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static pl.edu.icm.saos.api.single.judgment.data.representation.ConstitutionalTribunalJudgmentData.DissentingOpinion;

/**
 * Converts {@link pl.edu.icm.saos.persistence.model.ConstitutionalTribunalJudgment ConstitutionalTribunalJudgment} specific fields.
 * @author pavtel
 */
@Service
public class ConstitutionalTribunalJudgmentMapper {

    //------------------------ LOGIC --------------------------
    /**
     * Fill {@link pl.edu.icm.saos.persistence.model.ConstitutionalTribunalJudgment ConstitutionalTribunalJudgment} specific fields values
     * into {@link pl.edu.icm.saos.api.single.judgment.views.ConstitutionalTribunalJudgmentView ConstitutionalTribunalJudgmentView}.
     * @param representation in which to add values.
     * @param judgment to process.
     */
    public void fillJudgmentsFieldToRepresentation(ConstitutionalTribunalJudgmentView representation, ConstitutionalTribunalJudgment judgment){
        fillData(representation.getData(), judgment);
    }

    /**
     * Converts list of {@link ConstitutionalTribunalJudgmentDissentingOpinion} into list of {@link DissentingOpinion}.
     * @param dissentingOpinions to process.
     * @return list of DissentingOpinions view.
     */
    public List<DissentingOpinion> toOpinions(List<ConstitutionalTribunalJudgmentDissentingOpinion> dissentingOpinions) {
        if(dissentingOpinions == null){
            dissentingOpinions = Collections.emptyList();
        }

        List<DissentingOpinion> viewsDissentingOpinions = dissentingOpinions.stream()
                .map(opinion -> toViewOpinion(opinion))
                .collect(Collectors.toList());

        return viewsDissentingOpinions;
    }

    //------------------------ PRIVATE --------------------------
    private void fillData(ConstitutionalTribunalJudgmentData data, ConstitutionalTribunalJudgment judgment) {
        data.setDissentingOpinions(toOpinions(judgment.getDissentingOpinions()));
    }

    private DissentingOpinion toViewOpinion(ConstitutionalTribunalJudgmentDissentingOpinion opinion) {
        DissentingOpinion view = new DissentingOpinion();

        view.setTextContent(opinion.getTextContent());
        view.setAuthors(toAuthors(opinion.getAuthors()));

        return view;
    }

    private List<String> toAuthors(List<String> authors) {
        if(authors == null){
            authors = Collections.emptyList();
        }
        return authors;
    }


}
