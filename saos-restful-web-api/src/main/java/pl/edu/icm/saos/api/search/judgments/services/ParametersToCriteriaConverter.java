package pl.edu.icm.saos.api.search.judgments.services;

import org.springframework.stereotype.Service;
import pl.edu.icm.saos.api.search.judgments.parameters.JudgmentsParameters;
import pl.edu.icm.saos.api.search.parameters.Pagination;
import pl.edu.icm.saos.search.search.model.JudgmentCriteria;
import pl.edu.icm.saos.search.search.model.Paging;
import pl.edu.icm.saos.search.search.model.Sorting;

import static pl.edu.icm.saos.search.config.model.JudgmentIndexField.ID;

/**
 * @author pavtel
 */
@Service
public class ParametersToCriteriaConverter {

    public JudgmentCriteria toCriteria(JudgmentsParameters params){
        JudgmentCriteria criteria = new JudgmentCriteria();

        criteria.setAll(params.getAll());
        criteria.setCourtName(params.getCourtName());
        criteria.setReferencedRegulation(params.getReferencedRegulation());
        criteria.setJudgeName(params.getJudgeName());
        criteria.setLegalBase(params.getLegalBase());
        criteria.setKeyword(params.getKeyword());

        criteria.setDateFrom(params.getJudgmentDateFrom());
        criteria.setDateTo(params.getJudgmentDateTo());

        return criteria;
    }

    public Paging toPaging(Pagination pagination){
        return new Paging(pagination.getPageNumber(), pagination.getPageSize(), new Sorting(ID.getFieldName(), Sorting.Direction.ASC));
    }
}