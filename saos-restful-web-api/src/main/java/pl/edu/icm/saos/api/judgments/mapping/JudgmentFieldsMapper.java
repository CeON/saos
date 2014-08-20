package pl.edu.icm.saos.api.judgments.mapping;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Component;
import pl.edu.icm.saos.persistence.model.*;

import java.util.*;

import static pl.edu.icm.saos.api.ApiConstants.*;

/** {@inheritDoc}
 * @author pavtel
 */
@Component("judgmentFieldsMapper")
public class JudgmentFieldsMapper implements FieldsMapper<Judgment> {


    private static final String DATE_FORMAT = "YYYY-MM-dd";

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> toMap(Judgment element) {
        Map<String, Object> item = new LinkedHashMap<>();

        item.put(CASE_NUMBER, element.getCaseNumber());
        item.put(JUDGMENT_TYPE, element.getJudgmentType());
        item.put(SOURCE, toMap(element.getSourceInfo()));
        item.put(JUDGMENT_DATE, toString(element.getJudgmentDate()));
        item.put(JUDGES, toListOfMaps(element.getJudges()));
        item.put(COURT_REPORTERS, toSimpleList(element.getCourtReporters()));
        item.put(DECISION, element.getDecision());
        item.put(SUMMARY, element.getSummary());
        item.put(TEXT_CONTENT, element.getTextContent());

        JudgmentReasoning judgmentReasoning = element.getReasoning();
        Map<String, Object> reasoning = toMap(judgmentReasoning.getSourceInfo());
        reasoning.put(TEXT, judgmentReasoning.getText());

        item.put(REASONING, reasoning);
        item.put(LEGAL_BASES, toSimpleList(element.getLegalBases()));
        item.put(REFERENCED_REGULATIONS, toListOfMapsFromJRR(element.getReferencedRegulations()));



        return item;
    }

    private List<Map<String, Object>> toListOfMapsFromJRR(List<JudgmentReferencedRegulation> referencedRegulations) {
        if(referencedRegulations == null)
            referencedRegulations = Collections.EMPTY_LIST;

        List<Map<String, Object>> list = new LinkedList<>();

        for(JudgmentReferencedRegulation referencedRegulation: referencedRegulations){
            Map<String, Object> map = new LinkedHashMap<>();

            if(referencedRegulation.getLawJournalEntry() != null){
                LawJournalEntry lawJournalEntry = referencedRegulation.getLawJournalEntry();
                map.put(JOURNAL_TITLE, lawJournalEntry.getTitle());
                map.put(JOURNAL_NO, lawJournalEntry.getJournalNo());
                map.put(JOURNAL_YEAR, lawJournalEntry.getYear());
                map.put(JOURNAL_ENTRY, lawJournalEntry.getEntry());
            }

            map.put(TEXT, referencedRegulation.getRawText());

            list.add(map);
        }

        return list;
    }

    private List<String> toSimpleList(List<String> courtReporters) {
        if(courtReporters == null)
            courtReporters = Collections.emptyList();

        return courtReporters;
    }

    private List<Map<String, Object>> toListOfMaps(List<Judge> judges) {
        if(judges == null)
            judges = Collections.emptyList();

        List<Map<String, Object>> list = new LinkedList<>();

        for(Judge judge : judges){
            List<Judge.JudgeRole> judgeRoles = judge.getSpecialRoles();
            if(judgeRoles == null)
                judgeRoles = Collections.emptyList();

            Map<String, Object> map = new LinkedHashMap<>();
            map.put(NAME, judge.getName());
            map.put(SPECIAL_ROLES, judgeRoles);

            list.add(map);
        }

        return list;

    }

    private Map<String, Object> toMap(JudgmentSourceInfo info){
        Map<String, Object> item = new LinkedHashMap<>();

        item.put(CODE, info.getSourceCode());
        item.put(JUDGMENT_URL, info.getSourceJudgmentUrl());
        item.put(JUDGMENT_ID, info.getSourceJudgmentId());
        item.put(PUBLISHER, info.getPublisher());
        item.put(REVISER, info.getReviser());
        item.put(PUBLICATION_DATE, toString(info.getPublicationDate()));

        return item;
    }

    private String toString(DateTime dateTime){
        if(dateTime == null){
            return "";
        }else{
            return dateTime.toString(DATE_FORMAT);
        }
    }

    private String toString(LocalDate localDate){
        if(localDate == null){
            return "";
        } else {
            return localDate.toString(DATE_FORMAT);
        }
    }
}
