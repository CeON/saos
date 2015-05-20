package pl.edu.icm.saos.enrichment.apply.refregulations;

import java.util.List;

import org.assertj.core.util.Lists;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;

import pl.edu.icm.saos.enrichment.apply.EnrichmentTagValueConverter;
import pl.edu.icm.saos.persistence.model.JudgmentReferencedRegulation;
import pl.edu.icm.saos.persistence.model.LawJournalEntry;

/**
 * @author madryk
 */
@Service
public class ReferencedRegulationsTagValueConverter implements EnrichmentTagValueConverter<ReferencedRegulationsTagValueItem[], List<JudgmentReferencedRegulation>> {

    
    //------------------------ LOGIC --------------------------
    
    @Override
    public List<JudgmentReferencedRegulation> convert(ReferencedRegulationsTagValueItem[] refrencedRegulationsTagValueItems) {
        
        Preconditions.checkNotNull(refrencedRegulationsTagValueItems);
        
        
        List<JudgmentReferencedRegulation> referencedRegulations = Lists.newArrayList();
        
        for (ReferencedRegulationsTagValueItem referencedRegulationsTagValueItem : refrencedRegulationsTagValueItems) {
            
            referencedRegulations.add(convert(referencedRegulationsTagValueItem));
            
        }
        
        return referencedRegulations;
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private JudgmentReferencedRegulation convert(ReferencedRegulationsTagValueItem referencedRegulationsTagValueItem) {
        
        JudgmentReferencedRegulation referencedRegulation = new JudgmentReferencedRegulation();
        LawJournalEntry lawJournalEntry = new LawJournalEntry();
        
        lawJournalEntry.setTitle(referencedRegulationsTagValueItem.getJournalTitle());
        lawJournalEntry.setJournalNo(referencedRegulationsTagValueItem.getJournalNo());
        lawJournalEntry.setYear(referencedRegulationsTagValueItem.getJournalYear());
        lawJournalEntry.setEntry(referencedRegulationsTagValueItem.getJournalEntry());
        
        referencedRegulation.setLawJournalEntry(lawJournalEntry);
        referencedRegulation.setRawText(referencedRegulationsTagValueItem.getText());
        
        referencedRegulation.markGenerated();
        
        return referencedRegulation;
    }

}
