package pl.edu.icm.saos.api.judgments.transformers;



import com.google.common.base.Preconditions;

import org.joda.time.LocalDate;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.api.transformers.SearchResultApiTransformer;
import pl.edu.icm.saos.persistence.builder.BuildersFactory;
import pl.edu.icm.saos.persistence.model.CourtCase;
import pl.edu.icm.saos.persistence.model.Judge.JudgeRole;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.search.search.model.JudgmentSearchResult;

import java.util.stream.Collectors;

import static pl.edu.icm.saos.persistence.builder.BuildersFactory.*;

/**
 * {@inheritDoc}
 * @author pavtel
 * Transforms {@pl.edu.icm.saos.search.search.model.JudgmentSearchResult JudgmentSearchResutl}
 * into {@link pl.edu.icm.saos.persistence.model.Judgment Judgment}.
 */
@Service
public class JudgmentSearchResultApiTransformer implements SearchResultApiTransformer<JudgmentSearchResult, Judgment>{

    //******** business methods ***********

    @Override
    public Judgment transform(JudgmentSearchResult element) {
        Preconditions.checkNotNull(element, "element can't be null");

        Judgment judgment = commonCourtJudgmentWrapper(Integer.parseInt(element.getId()))
                .courtCases(element.getCaseNumbers().stream().map(CourtCase::new).collect(Collectors.toList()))
                .judgmentType(Judgment.JudgmentType.valueOf(element.getJudgmentType()))
                .judgmentDate(new LocalDate(element.getJudgmentDate()))
                .division(commonCourtDivision(element.getCourtDivisionId())
                                .code(element.getCourtDivisionCode())
                                .name(element.getCourtDivisionName())
                                .court(commonCourt(element.getCourtId())
                                                .code(element.getCourtCode())
                                                .name(element.getCourtName()).build()
                                ).build()
                )
                .judges(
                        element.getJudges().stream()
                                .map((judge) -> judge(judge.getName()).judgesRoles(judge.getSpecialRoles().stream().toArray(JudgeRole[]::new)).build())
                                .collect(Collectors.toList())
                )
                .keywords(
                        element.getKeywords().stream()
                                .map(BuildersFactory::keyword)
                                .collect(Collectors.toList())
                ).build()

                ;

        return judgment;
    }

    //****** END business methods ***********
}
