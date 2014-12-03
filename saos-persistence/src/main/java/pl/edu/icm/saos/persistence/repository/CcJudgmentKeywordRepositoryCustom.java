package pl.edu.icm.saos.persistence.repository;

import java.util.List;

import pl.edu.icm.saos.persistence.model.CcJudgmentKeyword;

/**
 * @author Łukasz Pawełczak
 *
 */
public interface CcJudgmentKeywordRepositoryCustom {

	/**
     * Finds list of {@link CcJudgmentKeyword} with the given part of phrase ({@link CcJudgmentKeyword#getPhrase()})
     * and returns it. Returned list contains keywords sorted alphabetically by phrase {@link CcJudgmentKeyword#getPhrase()}.
     */
	List<CcJudgmentKeyword> findAllByPhrasePart(String phrasePart);
}
