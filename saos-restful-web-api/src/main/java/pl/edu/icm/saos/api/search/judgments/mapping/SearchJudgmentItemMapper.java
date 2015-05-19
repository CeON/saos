package pl.edu.icm.saos.api.search.judgments.mapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.icm.saos.api.search.judgments.item.representation.SearchJudgmentItem;
import pl.edu.icm.saos.api.services.dates.DateMapping;
import pl.edu.icm.saos.api.services.links.LinksBuilder;
import pl.edu.icm.saos.api.single.judgment.data.representation.JudgmentData;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.search.search.model.JudgeResult;
import pl.edu.icm.saos.search.search.model.JudgmentSearchResult;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Provides functionality for mapping from {@link pl.edu.icm.saos.search.search.model.JudgmentSearchResult JudgmentSearchResult}
 * fields into {@link pl.edu.icm.saos.api.search.judgments.item.representation.SearchJudgmentItem SearchJudgmentItem}.
 * @author pavtel
 */
@Service
public class SearchJudgmentItemMapper {

    @Autowired
    private LinksBuilder linksBuilder;

    @Autowired
    private DateMapping dateMapping;

    //------------------------ LOGIC --------------------------

    /**
     * Fills item's fields using {@link JudgmentSearchResult} values.
     * @param item representation.
     * @param judgment to process.
     */
    public void fillJudgmentsFieldsToItemRepresentation(SearchJudgmentItem item, JudgmentSearchResult judgment){
        item.setId(judgment.getId());
        item.setHref(linksBuilder.urlToJudgment(judgment.getId()));
        item.setCourtType(judgment.getCourtType());
        item.setJudgmentType(judgment.getJudgmentType());
        item.setTextContent(judgment.getContent());
        item.setJudges(toJudges(judgment.getJudges()));
        item.setCourtCases(toCourtCases(judgment.getCaseNumbers()));
        item.setJudgmentDate(dateMapping.toISO8601Format(judgment.getJudgmentDate()));
        item.setKeywords(toKeywords(judgment.getKeywords()));
    }



    private List<JudgmentData.Judge> toJudges(List<JudgeResult> judges) {
        if(judges==null){
            judges = Collections.emptyList();
        }

        return judges.stream()
                .map(judgeResult -> toJudge(judgeResult))
                .collect(Collectors.toList());
    }

    private JudgmentData.Judge toJudge(JudgeResult judgeResult) {
        JudgmentData.Judge view = new JudgmentData.Judge();
        view.setName(judgeResult.getName());
        view.setSpecialRoles(toSpecialRoles(judgeResult.getSpecialRoles()));
        return view;
    }

    private List<String> toSpecialRoles(List<Judge.JudgeRole> specialRoles) {
        if(specialRoles==null){
            specialRoles = Collections.emptyList();
        }

        return specialRoles.stream()
                .map(role -> role.name())
                .collect(Collectors.toList());
    }

    private List<JudgmentData.CourtCase> toCourtCases(List<String> caseNumbers) {
        if(caseNumbers==null){
            caseNumbers = Collections.emptyList();
        }

        return caseNumbers.stream()
                .map(caseNumber -> toCourtCase(caseNumber))
                .collect(Collectors.toList());
    }

    private JudgmentData.CourtCase toCourtCase(String caseNumber) {
        JudgmentData.CourtCase view = new JudgmentData.CourtCase();
        view.setCaseNumber(caseNumber);
        return view;
    }

    private List<String> toKeywords(List<String> keywords) {
        if(keywords==null){
            return Collections.emptyList();
        } else {
            return keywords;
        }
    }

    //------------------------ SETTERS --------------------------

    public void setLinksBuilder(LinksBuilder linksBuilder) {
        this.linksBuilder = linksBuilder;
    }

    public void setDateMapping(DateMapping dateMapping) {
        this.dateMapping = dateMapping;
    }
}
