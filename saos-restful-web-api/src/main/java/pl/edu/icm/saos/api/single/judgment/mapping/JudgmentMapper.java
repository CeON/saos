package pl.edu.icm.saos.api.single.judgment.mapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;
import pl.edu.icm.saos.api.services.dates.DateMapping;
import pl.edu.icm.saos.api.services.links.LinksBuilder;
import pl.edu.icm.saos.api.single.judgment.views.JudgmentView;
import pl.edu.icm.saos.api.single.judgment.data.representation.JudgmentData;
import pl.edu.icm.saos.persistence.model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static pl.edu.icm.saos.api.single.judgment.data.representation.JudgmentData.Source;

/**
 * Converts {@link pl.edu.icm.saos.persistence.model.Judgment Judgmnent's} fields.
 * @author pavtel
 */
@Service
public class JudgmentMapper {

    @Autowired
    private LinksBuilder linksBuilder;

    @Autowired
    private DateMapping dateMapping;


    //------------------------ LOGIC --------------------------
    /**
     * Fill {@link pl.edu.icm.saos.persistence.model.Judgment udgment}  fields values
     * into {@link pl.edu.icm.saos.api.single.judgment.views.JudgmentView JudgmentView}.
     * @param representation in which to add values.
     * @param judgment to process.
     */
    public void fillJudgmentsFieldToRepresentation(JudgmentView<?> representation, Judgment judgment){
        representation.setLinks(toLinks(judgment));
        fillData(representation.getData(), judgment);
    }



    private List<Link> toLinks(Judgment judgment) {
        Link link = linksBuilder.linkToJudgment(judgment.getId());
        return Arrays.asList(link);
    }

    private void fillData(JudgmentData data, Judgment judgment) {
        data.setHref(linksBuilder.urlToJudgment(judgment.getId()));
        data.setJudgmentType(judgment.getJudgmentType());
        data.setJudgmentDate(dateMapping.toISO8601Format(judgment.getJudgmentDate()));
        data.setJudges(toJudges(judgment.getJudges()));
        data.setCourtCases(toCourtCases(judgment.getCourtCases()));
        data.setSource(toSource(judgment.getSourceInfo()));
        data.setCourtReporters(toSimpleList(judgment.getCourtReporters()));
        data.setDecision(judgment.getDecision());
        data.setSummary(judgment.getSummary());
        data.setTextContent(judgment.getTextContent());
        data.setLegalBases(toSimpleList(judgment.getLegalBases()));
        data.setReferencedRegulations(toReferencedRegulation(judgment.getReferencedRegulations()));
    }

    public List<JudgmentData.Judge> toJudges(List<Judge> judges) {
        if(judges == null)
            judges = Collections.emptyList();

        List<JudgmentData.Judge> judgesView = new ArrayList<>();

        for(Judge judge : judges){
            List<Judge.JudgeRole> judgeRoles = judge.getSpecialRoles();
            if(judgeRoles == null) {
                judgeRoles = Collections.emptyList();
            }

            List<String> rolesNames = judgeRoles.stream()
                    .map(role -> role.name())
                    .collect(Collectors.toList());

            JudgmentData.Judge judgeView = new JudgmentData.Judge();
            judgeView.setName(judge.getName());
            judgeView.setSpecialRoles(rolesNames);

            judgesView.add(judgeView);
        }

        return judgesView;
    }


    public List<JudgmentData.CourtCase> toCourtCases(List<CourtCase> courtCases) {
        if(courtCases==null){
            courtCases = Collections.emptyList();
        }

        return courtCases.stream()
                .map(courtCase -> toCourtCaseView(courtCase))
                .collect(Collectors.toList());
    }


    private JudgmentData.CourtCase toCourtCaseView(CourtCase courtCase) {
        JudgmentData.CourtCase courtCaseView = new JudgmentData.CourtCase();
        courtCaseView.setCaseNumber(courtCase.getCaseNumber());
        return courtCaseView;
    }


    public Source toSource(JudgmentSourceInfo info) {
        Source source = new Source();

        source.setCode(info.getSourceCode());
        source.setJudgmentUrl(info.getSourceJudgmentUrl());
        source.setJudgmentId(info.getSourceJudgmentId());
        source.setPublisher(info.getPublisher());
        source.setReviser(info.getReviser());
        source.setPublicationDate(dateMapping.toISO8601Format(info.getPublicationDate()));

        return source;
    }

    public List<String> toSimpleList(List<String> elements) {
        if(elements == null)
            elements = Collections.emptyList();

        return elements;
    }

    public List<JudgmentData.ReferencedRegulations> toReferencedRegulation(List<JudgmentReferencedRegulation> referencedRegulations) {

        if(referencedRegulations == null) {
            referencedRegulations = Collections.emptyList();
        }

        List<JudgmentData.ReferencedRegulations> regulations = new ArrayList<>();

        for(JudgmentReferencedRegulation referencedRegulation: referencedRegulations) {
            JudgmentData.ReferencedRegulations regulationRepresentation = new JudgmentData.ReferencedRegulations();

            if (referencedRegulation.getLawJournalEntry() != null) {
                LawJournalEntry lawJournalEntry = referencedRegulation.getLawJournalEntry();

                regulationRepresentation.setJournalTitle(lawJournalEntry.getTitle());
                regulationRepresentation.setJournalNo(lawJournalEntry.getJournalNo());
                regulationRepresentation.setJournalYear(lawJournalEntry.getYear());
                regulationRepresentation.setJournalEntry(lawJournalEntry.getEntry());
            }

            regulationRepresentation.setText(referencedRegulation.getRawText());
            regulations.add(regulationRepresentation);
        }

        return regulations;
    }

    //------------------------ SETTERS --------------------------

    public void setLinksBuilder(LinksBuilder linksBuilder) {
        this.linksBuilder = linksBuilder;
    }

    public void setDateMapping(DateMapping dateMapping) {
        this.dateMapping = dateMapping;
    }

}
