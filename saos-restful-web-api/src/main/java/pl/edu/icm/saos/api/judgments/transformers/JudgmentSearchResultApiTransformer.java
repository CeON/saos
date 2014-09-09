package pl.edu.icm.saos.api.judgments.transformers;

import com.google.common.base.Preconditions;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Service;
import pl.edu.icm.saos.api.builders.BuildersFactory;
import pl.edu.icm.saos.api.transformers.SearchResultApiTransformer;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.search.model.JudgmentSearchResult;

import java.util.stream.Collectors;

import static pl.edu.icm.saos.api.builders.BuildersFactory.*;

/**
 * @author pavtel
 */
@Service
public class JudgmentSearchResultApiTransformer implements SearchResultApiTransformer<JudgmentSearchResult, Judgment>{

    @Override
    public Judgment transform(JudgmentSearchResult element) {
        Preconditions.checkNotNull(element, "element can't be null");

        Judgment judgment = commonCourtJudgmentWrapper(Integer.parseInt(element.getId()))
                .caseNumber(element.getSignature())
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
