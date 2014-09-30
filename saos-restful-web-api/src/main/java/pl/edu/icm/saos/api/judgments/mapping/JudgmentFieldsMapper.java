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
 * Converts {@link pl.edu.icm.saos.persistence.model.Judgment Judgment's} fields.
 */
@Component("judgmentFieldsMapper")
public class JudgmentFieldsMapper implements FieldsMapper<Judgment> {

    //******** fields **********
    private static final String DATE_FORMAT = "YYYY-MM-dd";

    @Autowired
    private LinksBuilder linksBuilder;

    @Autowired
    private FieldsMapper<CommonCourtDivision> divisionFieldsMapper;
    //********* END fields **********


    //******** business methods *************
    @Override
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

        item.put(COURT_CASES, toListOfCourtCaseMaps(element.getCourtCases()));
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


    private List<String> toListFromKeywords(List<CcJudgmentKeyword> keywords) {
        if(keywords == null)
            keywords = Collections.emptyList();

        List<String> list = keywords.stream()
                .map(CcJudgmentKeyword::getPhrase)
                .collect(Collectors.toList());

        return list;
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
    
    private List<Map<String, Object>> toListOfCourtCaseMaps(List<CourtCase> courtCases) {
        if(courtCases == null) {
            courtCases = Collections.emptyList();
        }
        
        List<Map<String, Object>> list = new LinkedList<>();

        for(CourtCase courtCase : courtCases){
            Map<String, Object> map = new LinkedHashMap<>();
            map.put(CASE_NUMBER, courtCase.getCaseNumber());
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
            return localDate.toString();
        }
    }

    //******** END business methods ****************



    //*** setters ****
    public void setLinksBuilder(LinksBuilder linksBuilder) {
        this.linksBuilder = linksBuilder;
    }

    public void setDivisionFieldsMapper(FieldsMapper<CommonCourtDivision> divisionFieldsMapper) {
        this.divisionFieldsMapper = divisionFieldsMapper;
    }
}
