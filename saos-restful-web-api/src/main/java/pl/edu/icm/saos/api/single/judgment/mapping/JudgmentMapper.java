package pl.edu.icm.saos.api.single.judgment.mapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;
import pl.edu.icm.saos.api.services.dates.DateMapping;
import pl.edu.icm.saos.api.services.links.LinksBuilder;
import pl.edu.icm.saos.api.single.judgment.JudgmentView;
import pl.edu.icm.saos.api.single.judgment.representation.JudgmentData;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.JudgmentReferencedRegulation;
import pl.edu.icm.saos.persistence.model.JudgmentSourceInfo;
import pl.edu.icm.saos.persistence.model.LawJournalEntry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static pl.edu.icm.saos.api.single.judgment.representation.JudgmentData.Source;

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

    public void fillJudgmentsFieldToRepresentation(JudgmentView<? extends JudgmentData> representation, Judgment judgment){
        representation.setLinks(toLinks(judgment));
        fillData(representation.getData(), judgment);
    }


    //------------------------ PRIVATE --------------------------

    private List<Link> toLinks(Judgment judgment) {
        Link link = linksBuilder.linkToJudgment(judgment.getId());
        return Arrays.asList(link);
    }

    private void fillData(JudgmentData data, Judgment judgment) {
        data.setSource(toSource(judgment.getSourceInfo()));
        data.setCourtReporters(toSimpleList(judgment.getCourtReporters()));
        data.setDecision(judgment.getDecision());
        data.setSummary(judgment.getSummary());
        data.setTextContent(judgment.getTextContent());
        data.setLegalBases(toSimpleList(judgment.getLegalBases()));
        data.setReferencedRegulation(toReferencedRegulation(judgment.getReferencedRegulations()));
    }


    private Source toSource(JudgmentSourceInfo info) {
        Source source = new Source();

        source.setCode(info.getSourceCode());
        source.setJudgmentUrl(info.getSourceJudgmentUrl());
        source.setJudgmentId(info.getSourceJudgmentId());
        source.setPublisher(info.getPublisher());
        source.setReviser(info.getReviser());
        source.setPublicationDate(dateMapping.toISO8601Format(info.getPublicationDate()));

        return source;
    }

    private List<String> toSimpleList(List<String> elements) {
        if(elements == null)
            elements = Collections.emptyList();

        return elements;
    }

    private List<JudgmentData.ReferencedRegulation> toReferencedRegulation(List<JudgmentReferencedRegulation> referencedRegulations) {

        if(referencedRegulations == null) {
            referencedRegulations = Collections.emptyList();
        }

        List<JudgmentData.ReferencedRegulation> regulations = new ArrayList<>();

        for(JudgmentReferencedRegulation referencedRegulation: referencedRegulations) {
            JudgmentData.ReferencedRegulation regulationRepresentation = new JudgmentData.ReferencedRegulation();

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
