package pl.edu.icm.saos.api.dump.judgment.mapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.icm.saos.api.dump.judgment.item.representation.JudgmentItem;
import pl.edu.icm.saos.api.services.dates.DateMapping;
import pl.edu.icm.saos.api.single.judgment.mapping.JudgmentMapper;
import pl.edu.icm.saos.persistence.model.Judgment;

/**
 * @author pavtel
 */
@Service
public class DumpJudgmentItemMapper {

    @Autowired
    private JudgmentMapper judgmentMapper;

    @Autowired
    private DateMapping dateMapping;

    public void fillJudgmentsFieldsToRepresentation(JudgmentItem representation, Judgment judgment){
        representation.setId(judgment.getId());
        representation.setCourtCases(judgmentMapper.toCourtCases(judgment.getCourtCases()));
        representation.setCourtReporters(judgmentMapper.toSimpleList(judgment.getCourtReporters()));
        representation.setJudges(judgmentMapper.toJudges(judgment.getJudges()));
        representation.setJudgmentDate(dateMapping.toISO8601Format(judgment.getJudgmentDate()));
        representation.setJudgmentType(judgment.getJudgmentType());
        representation.setLegalBases(judgmentMapper.toSimpleList(judgment.getLegalBases()));
        representation.setSummary(judgment.getSummary());
        representation.setTextContent(judgment.getTextContent());
        representation.setSource(judgmentMapper.toSource(judgment.getSourceInfo()));
        representation.setDecision(judgment.getDecision());
        representation.setReferencedRegulations(judgmentMapper.toReferencedRegulation(judgment.getReferencedRegulations()));
    }
}
