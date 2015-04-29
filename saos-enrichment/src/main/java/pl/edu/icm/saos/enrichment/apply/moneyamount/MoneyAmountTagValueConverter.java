package pl.edu.icm.saos.enrichment.apply.moneyamount;

import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;

import pl.edu.icm.saos.enrichment.apply.EnrichmentTagValueConverter;
import pl.edu.icm.saos.persistence.model.MoneyAmount;

/**
 * @author madryk
 */
@Service
public class MoneyAmountTagValueConverter implements EnrichmentTagValueConverter<MoneyAmountTagValue, MoneyAmount> {

    
    //------------------------ LOGIC --------------------------
    
    @Override
    public MoneyAmount convert(MoneyAmountTagValue tagValue) {
        Preconditions.checkNotNull(tagValue);
        
        MoneyAmount moneyAmount = new MoneyAmount();
        
        moneyAmount.setAmount(tagValue.getAmount());
        moneyAmount.setText(tagValue.getText());
        moneyAmount.markGenerated();
        
        return moneyAmount;
    }

}
