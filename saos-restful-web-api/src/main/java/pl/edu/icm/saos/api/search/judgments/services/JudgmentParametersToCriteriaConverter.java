package pl.edu.icm.saos.api.search.judgments.services;

import org.springframework.stereotype.Service;
import pl.edu.icm.saos.api.search.judgments.parameters.JudgmentsParameters;
import pl.edu.icm.saos.api.search.parameters.Pagination;
import pl.edu.icm.saos.search.search.model.JudgmentCriteria;
import pl.edu.icm.saos.search.search.model.Paging;
import pl.edu.icm.saos.search.search.model.Sorting;

import static pl.edu.icm.saos.search.config.model.JudgmentIndexField.DATABASE_ID;

/**
 * @author pavtel
 */
@Service
public class JudgmentParametersToCriteriaConverter {

    //------------------------ LOGIC --------------------------

    /**
     * Converts {@link pl.edu.icm.saos.api.search.judgments.parameters.JudgmentsParameters JudgmentsParameters}
     * into {@link pl.edu.icm.saos.search.search.model.JudgmentCriteria JudgmentCriteria}
     * @param params to process.
     * @return JudgmentCriteria
     */
    public JudgmentCriteria toCriteria(JudgmentsParameters params){
        JudgmentCriteria criteria = new JudgmentCriteria();

        criteria.setAll(params.getAll());
        criteria.setCaseNumber(params.getCaseNumber());
        criteria.setReferencedRegulation(params.getReferencedRegulation());
        criteria.setJudgeName(params.getJudgeName());
        criteria.setLegalBase(params.getLegalBase());

        criteria.setCcCourtId(params.getCcCourtId());
        criteria.setCcCourtName(params.getCcCourtName());
        criteria.setCcCourtCode(params.getCcCourtCode());
        criteria.setCcCourtType(params.getCcCourtType());

        criteria.setCcCourtDivisionId(params.getCcDivisionId());
        criteria.setCcCourtDivisionName(params.getCcDivisionName());
        criteria.setCcCourtDivisionCode(params.getCcDivisionCode());

        criteria.setScPersonnelType(params.getScPersonnelType());

        criteria.setScCourtChamberId(params.getScChamberId());
        criteria.setScCourtChamberName(params.getScChamberName());

        criteria.setScCourtChamberDivisionId(params.getScDivisionId());
        criteria.setScCourtChamberDivisionName(params.getScDivisionName());

        criteria.setDateFrom(params.getJudgmentDateFrom());
        criteria.setDateTo(params.getJudgmentDateTo());

        criteria.setJudgmentType(params.getJudgmentTypes());
        criteria.setKeywords(params.getKeywords());

        return criteria;
    }

    /**
     * Converts {@link pl.edu.icm.saos.api.search.parameters.Pagination Pagination} into
     * {@link pl.edu.icm.saos.search.search.model.Paging Paging}
     * @param pagination to process.
     * @return Paging
     */
    public Paging toPaging(Pagination pagination){
        return new Paging(pagination.getPageNumber(), pagination.getPageSize(), new Sorting(DATABASE_ID.getFieldName(), Sorting.Direction.ASC));
    }
}
