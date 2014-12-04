package pl.edu.icm.saos.persistence.repository;

import java.util.List;

import pl.edu.icm.saos.persistence.model.CourtType;
import pl.edu.icm.saos.persistence.model.JudgmentKeyword;

/**
 * @author Łukasz Pawełczak
 *
 */
public interface JudgmentKeywordRepositoryCustom {

	/**
     * Returns list of {@link JudgmentKeyword}s with the given courtType and phrase (the case is ignored) that begins with the phrasePart 
     * (see: {@link JudgmentKeyword#getCourtType} and {@link JudgmentKeyword#getPhrase()}) <br/>
     * Returned keywords are sorted alphabetically by phrase {@link CcJudgmentKeyword#getPhrase()}.
     */
	List<JudgmentKeyword> findAllByCourtTypeAndPhrasePart(CourtType courtType, String phrasePart);
}
