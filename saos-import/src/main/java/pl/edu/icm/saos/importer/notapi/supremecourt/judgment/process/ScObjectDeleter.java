package pl.edu.icm.saos.importer.notapi.supremecourt.judgment.process;

import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.SupremeCourtChamber;
import pl.edu.icm.saos.persistence.model.SupremeCourtChamberDivision;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgment;
import pl.edu.icm.saos.persistence.model.SupremeCourtJudgmentForm;
import pl.edu.icm.saos.persistence.repository.ScChamberDivisionRepository;
import pl.edu.icm.saos.persistence.repository.ScChamberRepository;
import pl.edu.icm.saos.persistence.repository.ScJudgmentFormRepository;

/**
 * Contains methods deleting supreme court objects meeting certain criteria 
 * 
 * @author Łukasz Dumiszewski
 */
@Service("scObjectDeleter")
class ScObjectDeleter {

    private static Logger log = LoggerFactory.getLogger(ScObjectDeleter.class);
    
    private EntityManager entityManager;
    
    private ScChamberRepository scChamberRepository;
    
    private ScChamberDivisionRepository scChamberDivisionRepository;
    
    private ScJudgmentFormRepository scJudgmentFormRepository;
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Deletes {@link SupremeCourtChamber}s that are not referenced from any {@link SupremeCourtJudgment}
     */
    @Transactional
    void deleteScChambersWithoutJudgments() {
        
        log.debug("Deleting scChambers without referring judgments ");
        
        
        String q = "select scChamber.id from " + SupremeCourtChamber.class.getName() + " scChamber " +
                    " where not exists  (select judgment from "+ SupremeCourtJudgment.class.getName() + " judgment " +
                                            " join judgment.scChambers_ jscChamber " +
                                            "  where scChamber.id = jscChamber.id)";
        @SuppressWarnings("unchecked")
        List<Integer> scChamberIds = entityManager.createQuery(q).getResultList();
        
        scChamberIds.stream().forEach(scChamberRepository::delete);
        
        scChamberRepository.flush();
        
    }
    
    /**
     * Deletes {@link SupremeCourtChamberDivision}s that are not referenced from any {@link SupremeCourtJudgment}
     */
    @Transactional
    void deleteScChamberDivisionsWithoutJudgments() {
        
        log.debug("Deleting scChamberDivisions without referring judgments ");
        
        
        String q = "select scChamberDivision.id from " + SupremeCourtChamberDivision.class.getName() + " scChamberDivision " +
                " where not exists  (select judgment from "+ SupremeCourtJudgment.class.getName() + " judgment " +
                                        "  where scChamberDivision.id = judgment.scChamberDivision.id)";
    
        @SuppressWarnings("unchecked")
        List<Integer> scChamberDivisionIds = entityManager.createQuery(q).getResultList();
        
        scChamberDivisionIds.stream().forEach(scChamberDivisionRepository::delete);
        
        scChamberDivisionRepository.flush();
        
    }

    /**
     * Deletes {@link SupremeCourtJudgmentForm}s that are not referenced from any {@link SupremeCourtJudgment}
     */
    @Transactional
    void deleteScjFormsWithoutJudgments() {
        
        log.debug("Deleting scjForms without referring judgments ");
        
        
        String q = "select scjForm.id from " + SupremeCourtJudgmentForm.class.getName() + " scjForm " +
                    " where not exists  (select judgment from "+ SupremeCourtJudgment.class.getName() + " judgment " +
                                            " join judgment.scJudgmentForm scjForm2 " +
                                            "  where scjForm.id = scjForm2.id)";
        @SuppressWarnings("unchecked")
        List<Integer> scjFormIds = entityManager.createQuery(q).getResultList();
        
        scjFormIds.stream().forEach(scJudgmentFormRepository::delete);
        
        
    }
    
    //------------------------ SETTERS --------------------------
    
    
    @Autowired
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Autowired
    public void setScChamberRepository(ScChamberRepository scChamberRepository) {
        this.scChamberRepository = scChamberRepository;
    }
    
    @Autowired
    public void setScJudgmentFormRepository(ScJudgmentFormRepository scJudgmentFormRepository) {
        this.scJudgmentFormRepository = scJudgmentFormRepository;
    }

    @Autowired
    public void setScChamberDivisionRepository(ScChamberDivisionRepository scChamberDivisionRepository) {
        this.scChamberDivisionRepository = scChamberDivisionRepository;
    }
    
}
