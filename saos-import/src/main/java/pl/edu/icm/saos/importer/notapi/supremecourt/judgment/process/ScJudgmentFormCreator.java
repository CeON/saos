package pl.edu.icm.saos.importer.notapi.supremecourt.judgment.process;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.persistence.model.SupremeCourtJudgmentForm;
import pl.edu.icm.saos.persistence.repository.ScJudgmentFormRepository;

import com.google.common.base.Preconditions;

/**
 * @author Łukasz Dumiszewski
 */
@Service("scJudgmentFormCreator")
class ScJudgmentFormCreator {

    
    private ScJudgmentFormRepository scJudgmentFormRepository;
    
    
    
    //------------------------ LOGIC --------------------------
    
    /**
     * Finds {@link SupremeCourtJudgmentForm} with the given name and returns it. <br/>
     * If no {@link SupremeCourtJudgmentForm} with this name can be found, then creates one, saves it
     * in a datasource and returns. 
     */
    public SupremeCourtJudgmentForm getOrCreateScJudgmentForm(String judgmentFormName) {
        Preconditions.checkArgument(!StringUtils.isBlank(judgmentFormName));
        
        SupremeCourtJudgmentForm judgmentForm = scJudgmentFormRepository.findOneByName(judgmentFormName);
        if (judgmentForm == null) {
            judgmentForm = new SupremeCourtJudgmentForm();
            judgmentForm.setName(judgmentFormName);
            scJudgmentFormRepository.saveAndFlush(judgmentForm);
        }
        return judgmentForm;
        
    }

    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setScJudgmentFormRepository(ScJudgmentFormRepository scJudgmentFormRepository) {
        this.scJudgmentFormRepository = scJudgmentFormRepository;
    }
    
}
