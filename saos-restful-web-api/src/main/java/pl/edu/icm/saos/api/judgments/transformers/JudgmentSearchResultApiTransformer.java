package pl.edu.icm.saos.api.judgments.transformers;



import com.google.common.base.Preconditions;
import org.joda.time.LocalDate;
import org.springframework.stereotype.Service;
import pl.edu.icm.saos.api.transformers.SearchResultApiTransformer;
import pl.edu.icm.saos.persistence.builder.BuildersFactory;
import pl.edu.icm.saos.persistence.model.CourtCase;
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
                .division(commonCourtDivision(10) //TODO add division id field into JudgmentSearchResult and use it here
                                .name(element.getCourtDivisionName())
                                .court(commonCourt(100) //TODO add court id field into JudgmentSearchResult and use it here
                                                .name(element.getCourtName()).build()
                                ).build()
                )
                .judges(
                        element.getJudges().stream()
                                .map((name) -> judge(name).build())
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
