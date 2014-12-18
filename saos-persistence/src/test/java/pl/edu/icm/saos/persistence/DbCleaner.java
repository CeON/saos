package pl.edu.icm.saos.persistence;

import org.springframework.stereotype.Service;
import pl.edu.icm.saos.persistence.correction.model.JudgmentCorrection;
import pl.edu.icm.saos.persistence.model.*;
import pl.edu.icm.saos.persistence.model.importer.RawSourceCcJudgment;
import pl.edu.icm.saos.persistence.model.importer.notapi.RawSourceCtJudgment;
import pl.edu.icm.saos.persistence.model.importer.notapi.RawSourceScJudgment;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

/**
 * @author Łukasz Dumiszewski
 */

@Service
public class DbCleaner {

    @PersistenceContext
    private EntityManager entityManager;
    
    @Transactional
    public void clean() {
        deleteAll(JudgmentReferencedRegulation.class);
        deleteAll(LawJournalEntry.class);
        deleteAllSql("judgment_court_reporter");
        deleteAllSql("judge_role");
        deleteAllSql("assigned_judgment_keyword");
        deleteAll(JudgmentKeyword.class);
        deleteAll(Judge.class);
        deleteAllSql("judgment_legal_bases");
        deleteAll(CourtCase.class);
        
        deleteAllSql("supreme_court_judgment_chamber");

        deleteAllSql("ct_judgment_opinion_author");
        deleteAll(ConstitutionalTribunalJudgmentDissentingOpinion.class);

        deleteAll(JudgmentCorrection.class);
        
        deleteAll(Judgment.class);
        
        deleteAll(SupremeCourtJudgmentForm.class);
        deleteAll(SupremeCourtChamberDivision.class);
        deleteAll(SupremeCourtChamber.class);
        
        deleteAll(CommonCourtDivision.class);
        deleteAll(CommonCourtDivisionType.class);
        deleteAll(CommonCourt.class);
        
        deleteAll(RawSourceCcJudgment.class);
        deleteAll(RawSourceScJudgment.class);
        deleteAll(RawSourceCtJudgment.class);
    }
    
    
    public void deleteAll(Class<?> clazz) {
        Query query = entityManager.createQuery("delete from " + clazz.getName());
        query.executeUpdate();
    }
    
    public void deleteAllSql(String tableName) {
        Query query = entityManager.createNativeQuery("delete from " + tableName);
        query.executeUpdate();
    }
}
