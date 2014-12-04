package pl.edu.icm.saos.persistence.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.common.util.StringTools;
import pl.edu.icm.saos.persistence.model.CourtType;
import pl.edu.icm.saos.persistence.model.JudgmentKeyword;


/**
 * @author Łukasz Pawełczak
 *
 */
@Service("judgmentKeywordRepositoryCustom")
public class JudgmentKeywordRepositoryCustomImpl implements JudgmentKeywordRepositoryCustom {

	
	@Autowired
	private EntityManager entityManager;

	
	//------------------------ LOGIC --------------------------
	
	@Override
	@Transactional
	public List<JudgmentKeyword> findAllByCourtTypeAndPhrasePart(CourtType courtType, String phrasePart) {
		Query query = entityManager.createQuery("select keyword from " + JudgmentKeyword.class.getName() 
						+ " keyword where courtType=:courtType and lower(keyword.phrase) like :phrasePart "
						+ " order by keyword.phrase asc");
		
		query.setParameter("courtType", courtType);
        query.setParameter("phrasePart", StringTools.toRootLowerCase(phrasePart) + "%");
		
        @SuppressWarnings("unchecked")
        List<JudgmentKeyword> keywords = query.getResultList();
		
		return keywords;
	}
	
}
