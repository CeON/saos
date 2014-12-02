package pl.edu.icm.saos.persistence.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.CcJudgmentKeyword;


/**
 * @author Łukasz Pawełczak
 *
 */
@Service("CcJudgmentKeywordRepositoryCustom")
public class CcJudgmentKeywordRepositoryCustomImpl implements CcJudgmentKeywordRepositoryCustom {

	
	@Autowired
    private EntityManager entityManager;

	
	//------------------------ LOGIC --------------------------
	
    @Override
    @Transactional
	public List<CcJudgmentKeyword> findAllByPhrasePart(String phrasePart) {
    	
    	Query query = entityManager.createQuery("select keyword from " + CcJudgmentKeyword.class.getName() + " keyword where lower(keyword.phrase) like :phrasePart");
    	query.setParameter("phrasePart", phrasePart.toLowerCase() + "%");
    	
    	List<CcJudgmentKeyword> keywords = query.getResultList();
    	
    	return keywords;
	}
	
}
