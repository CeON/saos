package pl.edu.icm.saos.enrichment.apply.refregulations;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import pl.edu.icm.saos.enrichment.apply.EnrichmentTagValueConverter;
import pl.edu.icm.saos.persistence.model.JudgmentReferencedRegulation;
import pl.edu.icm.saos.persistence.model.LawJournalEntry;
import pl.edu.icm.saos.persistence.repository.LawJournalEntryRepository;

/**
 * @author madryk
 */
@Service
public class ReferencedRegulationsTagValueConverter implements EnrichmentTagValueConverter<ReferencedRegulationsTagValueItem[], List<JudgmentReferencedRegulation>> {

    private Logger log = LoggerFactory.getLogger(getClass()); 


    private LawJournalEntryRepository lawJournalEntryRepository;
    
    
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
        
        LawJournalEntry lawJournalEntry = lawJournalEntryRepository.findOneByYearAndEntry(referencedRegulationsTagValueItem.getJournalYear(), referencedRegulationsTagValueItem.getJournalEntry());
        
        if (lawJournalEntry == null) {
            log.warn("LawJournalEntry defined in enrichment tag not found in database (year={}, entry={})",
                    referencedRegulationsTagValueItem.getJournalYear(), referencedRegulationsTagValueItem.getJournalEntry());
            
            lawJournalEntry = new LawJournalEntry();
            
            lawJournalEntry.setTitle(referencedRegulationsTagValueItem.getJournalTitle());
            lawJournalEntry.setJournalNo(referencedRegulationsTagValueItem.getJournalNo());
            lawJournalEntry.setYear(referencedRegulationsTagValueItem.getJournalYear());
            lawJournalEntry.setEntry(referencedRegulationsTagValueItem.getJournalEntry());
        }
        
        referencedRegulation.setLawJournalEntry(lawJournalEntry);
        referencedRegulation.setRawText(referencedRegulationsTagValueItem.getText());
        
        referencedRegulation.markGenerated();
        
        return referencedRegulation;
    }


    //------------------------ SETTERS --------------------------
    
    @Autowired
    public void setLawJournalEntryRepository(
            LawJournalEntryRepository lawJournalEntryRepository) {
        this.lawJournalEntryRepository = lawJournalEntryRepository;
    }

}
