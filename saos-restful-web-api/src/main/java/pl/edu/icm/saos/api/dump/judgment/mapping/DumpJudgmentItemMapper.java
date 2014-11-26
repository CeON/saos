package pl.edu.icm.saos.api.dump.judgment.mapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.icm.saos.api.dump.judgment.item.representation.JudgmentItem;
import pl.edu.icm.saos.api.services.dates.DateMapping;
import pl.edu.icm.saos.api.single.judgment.mapping.JudgmentMapper;
import pl.edu.icm.saos.persistence.model.Judgment;

/**
 * Provides functionality for mapping {@link pl.edu.icm.saos.persistence.model.Judgment Judgment} fields
 * into {@link pl.edu.icm.saos.api.dump.judgment.item.representation.JudgmentItem JudgmentItem}.
 * @author pavtel
 */
@Service
public class DumpJudgmentItemMapper {

    @Autowired
    private JudgmentMapper judgmentMapper;

    @Autowired
    private DateMapping dateMapping;

    //------------------------ LOGIC --------------------------

    /**
     * Fills item's fields using judgment.
     * @param item representation.
     * @param judgment to process.
     */
    public void fillJudgmentsFieldsToRepresentation(JudgmentItem item, Judgment judgment){
        item.setId(judgment.getId());
        item.setCourtCases(judgmentMapper.toCourtCases(judgment.getCourtCases()));
        item.setCourtReporters(judgmentMapper.toSimpleList(judgment.getCourtReporters()));
        item.setJudges(judgmentMapper.toJudges(judgment.getJudges()));
        item.setJudgmentDate(dateMapping.toISO8601Format(judgment.getJudgmentDate()));
        item.setJudgmentType(judgment.getJudgmentType());
        item.setLegalBases(judgmentMapper.toSimpleList(judgment.getLegalBases()));
        item.setSummary(judgment.getSummary());
        item.setTextContent(judgment.getTextContent());
        item.setSource(judgmentMapper.toSource(judgment.getSourceInfo()));
        item.setDecision(judgment.getDecision());
        item.setReferencedRegulations(judgmentMapper.toReferencedRegulation(judgment.getReferencedRegulations()));
        item.setCourtType(judgment.getCourtType());
    }

    //------------------------ SETTERS --------------------------

    public void setJudgmentMapper(JudgmentMapper judgmentMapper) {
        this.judgmentMapper = judgmentMapper;
    }

    public void setDateMapping(DateMapping dateMapping) {
        this.dateMapping = dateMapping;
    }
}
