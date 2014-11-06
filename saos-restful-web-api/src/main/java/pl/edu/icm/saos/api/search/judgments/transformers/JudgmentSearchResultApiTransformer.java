package pl.edu.icm.saos.api.search.judgments.transformers;



import com.google.common.base.Preconditions;

import org.joda.time.LocalDate;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.api.search.transformers.SearchResultApiTransformer;
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
    public Judgment transform(JudgmentSearchResult judgmentSearchResult) {
        Preconditions.checkNotNull(judgmentSearchResult, "judgmentSearchResult can't be null");

        Judgment judgment = commonCourtJudgmentWrapper(judgmentSearchResult.getId())
                .courtCases(judgmentSearchResult.getCaseNumbers().stream().map(CourtCase::new).collect(Collectors.toList()))
                .judgmentType(Judgment.JudgmentType.valueOf(judgmentSearchResult.getJudgmentType()))
                .judgmentDate(new LocalDate(judgmentSearchResult.getJudgmentDate()))
                .division(commonCourtDivision(judgmentSearchResult.getCourtDivisionId())
                                .code(judgmentSearchResult.getCourtDivisionCode())
                                .name(judgmentSearchResult.getCourtDivisionName())
                                .court(commonCourt(judgmentSearchResult.getCourtId())
                                                .code(judgmentSearchResult.getCourtCode())
                                                .name(judgmentSearchResult.getCourtName()).build()
                                ).build()
                )
                .judges(
                        judgmentSearchResult.getJudges().stream()
                                .map((judge) -> judge(judge.getName()).judgesRoles(judge.getSpecialRoles().stream().toArray(JudgeRole[]::new)).build())
                                .collect(Collectors.toList())
                )
                .keywords(
                        judgmentSearchResult.getKeywords().stream()
                                .map(BuildersFactory::keyword)
                                .collect(Collectors.toList())
                ).build()

                ;

        return judgment;
    }

    //****** END business methods ***********
}
