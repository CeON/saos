package pl.edu.icm.saos.api.single.judgment.mapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.api.services.dates.DateMapping;
import pl.edu.icm.saos.api.services.links.LinksBuilder;
import pl.edu.icm.saos.api.single.judgment.data.representation.JudgmentData;
import pl.edu.icm.saos.api.single.judgment.data.representation.JudgmentData.Source;
import pl.edu.icm.saos.api.single.judgment.views.JudgmentView;
import pl.edu.icm.saos.persistence.model.CourtCase;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.JudgmentKeyword;
import pl.edu.icm.saos.persistence.model.JudgmentReferencedRegulation;
import pl.edu.icm.saos.persistence.model.JudgmentSourceInfo;
import pl.edu.icm.saos.persistence.model.LawJournalEntry;
import pl.edu.icm.saos.persistence.model.ReferencedCourtCase;

import com.google.common.collect.Lists;

/**
 * Converts {@link pl.edu.icm.saos.persistence.model.Judgment Judgment} fields.
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
     * Fill {@link pl.edu.icm.saos.persistence.model.Judgment judgment}  fields values
     * into {@link pl.edu.icm.saos.api.single.judgment.views.JudgmentView JudgmentView}.
     * @param representation in which to add values.
     * @param judgment to process.
     */
    public void fillJudgmentsFieldToRepresentation(JudgmentView<?> representation, Judgment judgment){
        representation.setLinks(toLinks(judgment));
        fillData(representation.getData(), judgment);
    }



    public List<JudgmentData.Judge> toJudges(List<Judge> judges) {
        if(judges == null) {
            judges = Collections.emptyList();
        }

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
            judgeView.setFunction(judge.getFunction());

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


    public Source toSource(JudgmentSourceInfo info) {
        if(info == null){
            return null;
        }
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
        if(elements == null) {
            elements = Collections.emptyList();
        }

        return elements;
    }

    public List<JudgmentData.ReferencedRegulation> toReferencedRegulations(List<JudgmentReferencedRegulation> referencedRegulations) {

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
    
    
    public List<JudgmentData.ReferencedCourtCase> toReferencedCourtCases(List<ReferencedCourtCase> referencedCourtCases) {
        
        List<JudgmentData.ReferencedCourtCase> apiRefCourtCases = Lists.newArrayList();
        
        for (ReferencedCourtCase referencedCourtCase : referencedCourtCases) {
            
            JudgmentData.ReferencedCourtCase apiRefCourtCase = new JudgmentData.ReferencedCourtCase();
            apiRefCourtCase.setCaseNumber(referencedCourtCase.getCaseNumber());
            apiRefCourtCase.setJudgmentIds(referencedCourtCase.getJudgmentIds());
            apiRefCourtCase.setGenerated(referencedCourtCase.isGenerated());
            apiRefCourtCases.add(apiRefCourtCase);
            
        }
        
        return apiRefCourtCases;
        
    }
    
    /**
     * Maps {@link pl.edu.icm.saos.persistence.model.JudgmentKeyword keywords} into their names.
     * @param keywords to process.
     * @return list of keywords names.
     */
    public List<String> toListFromKeywords(List<JudgmentKeyword> keywords) {
        if(keywords == null) {
            keywords = Collections.emptyList();
        }

        List<String> list = keywords.stream()
                .map(JudgmentKeyword::getPhrase)
                .collect(Collectors.toList());

        return list;
    }


    //------------------------ PRIVATE --------------------------
    
    private List<Link> toLinks(Judgment judgment) {
        Link link = linksBuilder.linkToJudgment(judgment.getId());
        return Arrays.asList(link);
    }
    

    private void fillData(JudgmentData data, Judgment judgment) {
        data.setId(judgment.getId());
        data.setCourtType(judgment.getCourtType());
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
        data.setReferencedRegulations(toReferencedRegulations(judgment.getReferencedRegulations()));
        data.setKeywords(toListFromKeywords(judgment.getKeywords()));
        data.setReferencedCourtCases(toReferencedCourtCases(judgment.getReferencedCourtCases()));
        data.setReceiptDate(dateMapping.toISO8601Format(judgment.getReceiptDate()));
        data.setMeansOfAppeal((judgment.getMeansOfAppeal() == null) ? null : judgment.getMeansOfAppeal().getName());
        data.setJudgmentResult((judgment.getJudgmentResult() == null) ? null : judgment.getJudgmentResult().getText());
        data.setLowerCourtJudgments(toSimpleList(judgment.getLowerCourtJudgments()));
        
    }
    
    private JudgmentData.CourtCase toCourtCaseView(CourtCase courtCase) {
        JudgmentData.CourtCase courtCaseView = new JudgmentData.CourtCase();
        courtCaseView.setCaseNumber(courtCase.getCaseNumber());
        return courtCaseView;
    }



    
    //------------------------ SETTERS --------------------------

    public void setLinksBuilder(LinksBuilder linksBuilder) {
        this.linksBuilder = linksBuilder;
    }

    public void setDateMapping(DateMapping dateMapping) {
        this.dateMapping = dateMapping;
    }

}
