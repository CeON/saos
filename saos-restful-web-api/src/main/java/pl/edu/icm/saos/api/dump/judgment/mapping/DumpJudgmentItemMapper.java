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
     * @param judgmentItem representation.
     * @param judgment to process.
     */
    public void fillJudgmentFieldsToRepresentation(JudgmentItem judgmentItem, Judgment judgment){
        judgmentItem.setId(judgment.getId());
        judgmentItem.setCourtCases(judgmentMapper.toCourtCases(judgment.getCourtCases()));
        judgmentItem.setCourtReporters(judgmentMapper.toSimpleList(judgment.getCourtReporters()));
        judgmentItem.setJudges(judgmentMapper.toJudges(judgment.getJudges()));
        judgmentItem.setJudgmentDate(dateMapping.toISO8601Format(judgment.getJudgmentDate()));
        judgmentItem.setJudgmentType(judgment.getJudgmentType());
        judgmentItem.setLegalBases(judgmentMapper.toSimpleList(judgment.getLegalBases()));
        judgmentItem.setSummary(judgment.getSummary());
        judgmentItem.setTextContent(judgment.getRawTextContent());
        judgmentItem.setSource(judgmentMapper.toSource(judgment.getSourceInfo()));
        judgmentItem.setDecision(judgment.getDecision());
        judgmentItem.setReferencedRegulations(judgmentMapper.toReferencedRegulations(judgment.getReferencedRegulations()));
        judgmentItem.setCourtType(judgment.getCourtType());
        judgmentItem.setKeywords(judgmentMapper.toListFromKeywords(judgment.getKeywords()));
        judgmentItem.setReferencedCourtCases(judgmentMapper.toReferencedCourtCases(judgment.getReferencedCourtCases()));
        judgmentItem.setReceiptDate(dateMapping.toISO8601Format(judgment.getReceiptDate()));
        judgmentItem.setMeansOfAppeal((judgment.getMeansOfAppeal() == null) ? null : judgment.getMeansOfAppeal().getName());
        judgmentItem.setJudgmentResult((judgment.getJudgmentResult() == null) ? null : judgment.getJudgmentResult().getText());
        judgmentItem.setLowerCourtJudgments(judgmentMapper.toSimpleList(judgment.getLowerCourtJudgments()));
        
    }

    //------------------------ SETTERS --------------------------

    public void setJudgmentMapper(JudgmentMapper judgmentMapper) {
        this.judgmentMapper = judgmentMapper;
    }

    public void setDateMapping(DateMapping dateMapping) {
        this.dateMapping = dateMapping;
    }
}
