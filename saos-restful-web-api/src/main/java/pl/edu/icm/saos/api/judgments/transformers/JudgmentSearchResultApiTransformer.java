package pl.edu.icm.saos.api.judgments.transformers;

import static pl.edu.icm.saos.api.builders.BuildersFactory.commonCourt;
import static pl.edu.icm.saos.api.builders.BuildersFactory.commonCourtDivision;
import static pl.edu.icm.saos.api.builders.BuildersFactory.commonCourtJudgmentWrapper;

import java.util.stream.Collectors;

import org.joda.time.LocalDate;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.api.builders.BuildersFactory;
import pl.edu.icm.saos.api.transformers.SearchResultApiTransformer;
import pl.edu.icm.saos.persistence.model.CourtCase;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.search.model.JudgmentSearchResult;

import com.google.common.base.Preconditions;

/**
 * @author pavtel
 */
@Service
public class JudgmentSearchResultApiTransformer implements SearchResultApiTransformer<JudgmentSearchResult, Judgment>{

    @Override
    public Judgment transform(JudgmentSearchResult element) {
        Preconditions.checkNotNull(element, "element can't be null");

        Judgment judgment = commonCourtJudgmentWrapper(Integer.parseInt(element.getId()))
                .courtCases(element.getCaseNumbers().stream().map(caseNumber->new CourtCase(caseNumber)).collect(Collectors.toList()))
                .judgmentType(Judgment.JudgmentType.valueOf(element.getJudgmentType()))
                .judgmentDate(new LocalDate(element.getJudgmentDate()))
                .division(commonCourtDivision(10) //TODO add division id field into JudgmentSearchResult and use it here
                        .name(element.getCourtDivisionName())
                        .court(commonCourt(100) //TODO add court id field into JudgmentSearchResult and use it here
                                .name(element.getCourtName())
                        )
                )
                .judges(
                        element.getJudges().stream()
                                .map(BuildersFactory::judge)
                                .collect(Collectors.toList())
                )
                .keywords(
                        element.getKeywords().stream()
                                .map(BuildersFactory::keyword)
                                .collect(Collectors.toList())
                )

                ;

        return judgment;
    }
}
