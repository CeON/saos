package pl.edu.icm.saos.api.judgments.mapping;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.icm.saos.api.links.LinksBuilder;
import pl.edu.icm.saos.api.mapping.FieldsMapper;
import pl.edu.icm.saos.persistence.model.*;

import java.util.*;
import java.util.stream.Collectors;

import static pl.edu.icm.saos.api.ApiConstants.*;

/** {@inheritDoc}
 * @author pavtel
 */
@Component("judgmentFieldsMapper")
public class JudgmentFieldsMapper implements FieldsMapper<Judgment> {

    private static final String DATE_FORMAT = "YYYY-MM-dd";

    @Autowired
    private LinksBuilder linksBuilder;

    @Autowired
    private FieldsMapper<CommonCourtDivision> divisionFieldsMapper;




    public Map<String, Object> basicFieldsToMap(Judgment element){
        Map<String, Object> item = new LinkedHashMap<>();
        item.putAll(commonFieldsToMap(element));

        if(element instanceof CommonCourtJudgment){
            CommonCourtJudgment commonCourtJudgment = (CommonCourtJudgment) element;
            item.put(DIVISION, divisionFieldsMapper.basicFieldsToMap(commonCourtJudgment.getCourtDivision()));
        }

        return item;
    }

    @Override
    public Map<String, Object> commonFieldsToMap(Judgment element, boolean useIdInsteadOfLinks) {
        Map<String, Object> item = new LinkedHashMap<>();

        if(useIdInsteadOfLinks){
            item.put(ID, element.getId());
        } else {
            item.put(HREF, linksBuilder.urlToJudgment(element.getId()));
        }

        item.put(CASE_NUMBER, element.getCaseNumber());
        item.put(JUDGMENT_TYPE, element.getJudgmentType());
        item.put(JUDGMENT_DATE, toString(element.getJudgmentDate()));
        item.put(JUDGES, toListOfMaps(element.getJudges()));

        return item;
    }



    @Override
    public Map<String, Object> fieldsToMap(Judgment element, boolean useIdInsteadOfLinks) {
        Map<String, Object> item = new LinkedHashMap<>();
        item.putAll(commonFieldsToMap(element, useIdInsteadOfLinks));

        item.put(SOURCE, toMap(element.getSourceInfo()));
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

        if(element.isInstanceOfCommonCourtJudgment()){
            CommonCourtJudgment commonCourtJudgment = (CommonCourtJudgment) element;

            if(useIdInsteadOfLinks){
                Map<String, Object> division = new LinkedHashMap<>();
                division.put(ID, commonCourtJudgment.getCourtDivision().getId());
                item.put(DIVISION, division);
            }else{
                item.put(DIVISION, divisionFieldsMapper.fieldsToMap(commonCourtJudgment.getCourtDivision()));
            }

            item.put(KEYWORDS, toListFromKeywords(commonCourtJudgment.getKeywords()));
        }

        return item;
    }


    public Map<String, Object> toMap(Judgment element, boolean expandAll) {
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


        if(expandAll) {
            item.putAll(mapOptionalFields(element));
        }


        return item;
    }

    private Map<String, Object> mapOptionalFields(Judgment element){
        Map<String , Object> map = new LinkedHashMap<>();
        if (element instanceof CommonCourtJudgment) {
            CommonCourtJudgment commonJudgment = (CommonCourtJudgment) element;
            Map<String, Object> commonJudgmentFields = toMapCommonJudgmentFields(commonJudgment);

            map.putAll(commonJudgmentFields);
        }
        return map;
    }

    private Map<String, Object> toMapCommonJudgmentFields(CommonCourtJudgment commonJudgment) {
        Map<String, Object> item = new LinkedHashMap<>();

        CommonCourtDivision division = commonJudgment.getCourtDivision();
        item.put(DIVISION, toMap(division));
        item.put(KEYWORDS, toListFromKeywords(commonJudgment.getKeywords()));

        return item;

    }

    private List<String> toListFromKeywords(List<CcJudgmentKeyword> keywords) {
        if(keywords == null)
            keywords = Collections.emptyList();

        List<String> list = keywords.stream()
                .map(CcJudgmentKeyword::getPhrase)
                .collect(Collectors.toList());

        return list;
    }

    private Map<String, Object> toMap(CommonCourtDivision division) {
        Map<String, Object> item = new LinkedHashMap<>();
        item.put(NAME, division.getName());
        item.put(CODE, division.getCode());
        item.put(TYPE, division.getType().getName());

        item.put(COURT, toMap(division.getCourt()));

        return item;
    }

    private Map<String, Object> toMap(CommonCourt court) {
        Map<String, Object> item = new LinkedHashMap<>();

        item.put(CODE, court.getCode());
        item.put(NAME, court.getName());
        item.put(TYPE, court.getType());

        return item;

    }

    private List<Map<String, Object>> toListOfMapsFromJRR(List<JudgmentReferencedRegulation> referencedRegulations) {
        if(referencedRegulations == null)
            referencedRegulations = Collections.emptyList();

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



    //*** setters ****
    public void setLinksBuilder(LinksBuilder linksBuilder) {
        this.linksBuilder = linksBuilder;
    }

    public void setDivisionFieldsMapper(FieldsMapper<CommonCourtDivision> divisionFieldsMapper) {
        this.divisionFieldsMapper = divisionFieldsMapper;
    }
}
