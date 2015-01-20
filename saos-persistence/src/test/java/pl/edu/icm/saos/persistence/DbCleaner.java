package pl.edu.icm.saos.persistence;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.correction.model.JudgmentCorrection;
import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTag;
import pl.edu.icm.saos.persistence.enrichment.model.UploadEnrichmentTag;
import pl.edu.icm.saos.persistence.model.CommonCourt;
import pl.edu.icm.saos.persistence.model.CommonCourtDivision;
import pl.edu.icm.saos.persistence.model.CommonCourtDivisionType;
import pl.edu.icm.saos.persistence.model.ConstitutionalTribunalJudgmentDissentingOpinion;
import pl.edu.icm.saos.persistence.model.CourtCase;
import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.JudgmentKeyword;
import pl.edu.icm.saos.persistence.model.JudgmentReferencedRegulation;
import pl.edu.icm.saos.persistence.model.LawJournalEntry;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamber;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamberDivision;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgmentForm;
import pl.edu.icm.saos.persistence.model.importer.RawSourceCcJudgment;
import pl.edu.icm.saos.persistence.model.importer.notapi.RawSourceCtJudgment;
import pl.edu.icm.saos.persistence.model.importer.notapi.RawSourceNacJudgment;
import pl.edu.icm.saos.persistence.model.importer.notapi.RawSourceScJudgment;

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
        
        deleteAll(UploadEnrichmentTag.class);
        deleteAll(EnrichmentTag.class);
        
        deleteAll(SupremeCourtJudgmentForm.class);
        deleteAll(SupremeCourtChamberDivision.class);
        deleteAll(SupremeCourtChamber.class);
        
        deleteAll(CommonCourtDivision.class);
        deleteAll(CommonCourtDivisionType.class);
        deleteAll(CommonCourt.class);
        
        deleteAll(RawSourceCcJudgment.class);
        deleteAll(RawSourceScJudgment.class);
        deleteAll(RawSourceCtJudgment.class);
        deleteAll(RawSourceNacJudgment.class);
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
