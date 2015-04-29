package pl.edu.icm.saos.enrichment.apply.moneyamount;

import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;

import pl.edu.icm.saos.enrichment.apply.JudgmentUpdater;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.MoneyAmount;

/**
 * @author madryk
 */
@Service
public class MaxMoneyAmountJudgmentUpdater implements JudgmentUpdater<MoneyAmount> {

    
    //------------------------ LOGIC --------------------------
    
    @Override
    public void addToJudgment(Judgment judgment, MoneyAmount maxMoneyAmount) {
        
        Preconditions.checkNotNull(judgment);
        Preconditions.checkNotNull(maxMoneyAmount);
        
        judgment.setMaxMoneyAmount(maxMoneyAmount);
        
    }

}
