package pl.edu.icm.saos.enrichment;

/**
 * @author Łukasz Dumiszewski
 */


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Service;

import pl.edu.icm.saos.common.json.JsonFormatter;
import pl.edu.icm.saos.common.json.JsonStringParser;
import pl.edu.icm.saos.common.validation.CommonValidator;
import pl.edu.icm.saos.enrichment.apply.DefaultEnrichmentTagApplier;
import pl.edu.icm.saos.enrichment.apply.moneyamount.MaxMoneyAmountJudgmentUpdater;
import pl.edu.icm.saos.enrichment.apply.moneyamount.MoneyAmountTagValue;
import pl.edu.icm.saos.enrichment.apply.moneyamount.MoneyAmountTagValueConverter;
import pl.edu.icm.saos.enrichment.apply.refcases.ReferencedCourtCasesJudgmentUpdater;
import pl.edu.icm.saos.enrichment.apply.refcases.ReferencedCourtCasesTagValueConverter;
import pl.edu.icm.saos.enrichment.apply.refcases.ReferencedCourtCasesTagValueItem;
import pl.edu.icm.saos.enrichment.apply.refregulations.ReferencedRegulationsJudgmentUpdater;
import pl.edu.icm.saos.enrichment.apply.refregulations.ReferencedRegulationsTagValueConverter;
import pl.edu.icm.saos.enrichment.apply.refregulations.ReferencedRegulationsTagValueItem;
import pl.edu.icm.saos.enrichment.reference.CompositeTagJudgmentReferenceRemover;
import pl.edu.icm.saos.enrichment.reference.PageableDelegatingJudgmentReferenceRemover;
import pl.edu.icm.saos.enrichment.reference.TagJudgmentReferenceRemover;
import pl.edu.icm.saos.enrichment.reference.refcases.RefCourtCasesJudgmentReferenceRemover;
import pl.edu.icm.saos.persistence.enrichment.model.EnrichmentTagTypes;
import pl.edu.icm.saos.persistence.model.JudgmentReferencedRegulation;
import pl.edu.icm.saos.persistence.model.MoneyAmount;
import pl.edu.icm.saos.persistence.model.ReferencedCourtCase;

import com.fasterxml.jackson.core.JsonFactory;
import com.google.common.collect.Lists;

/**
 * 
 * @author Łukasz Dumiszewski
 */
@Configuration
@ComponentScan(useDefaultFilters=false, includeFilters={@Filter(type=FilterType.ANNOTATION, value=Service.class)})
public class EnrichmentConfiguration {


    
    
    @Autowired 
    private CommonValidator commonValidator;   
    
    @Autowired
    private JsonFactory jsonFactory;
   
    
    
    

    //------------------------ ENRICHMENT TAG APPLIERS --------------------------
    
    
    //---- REFERENCED_COURT_CASES TAG ----
    
    
    @Autowired
    private ReferencedCourtCasesTagValueConverter referencedCourtCasesTagValueConverter;
    
    @Autowired
    private ReferencedCourtCasesJudgmentUpdater referencedCourtCasesJudgmentUpdater;
    
    @Bean
    public JsonStringParser<ReferencedCourtCasesTagValueItem[]> referencedCourtCasesJsonParser() {
        return new JsonStringParser<>(ReferencedCourtCasesTagValueItem[].class);
    }
    
    @Bean
    public DefaultEnrichmentTagApplier<ReferencedCourtCasesTagValueItem[], List<ReferencedCourtCase>> referencedCourtCasesTagApplier() {
        
        DefaultEnrichmentTagApplier<ReferencedCourtCasesTagValueItem[], List<ReferencedCourtCase>> tagApplier = new DefaultEnrichmentTagApplier<ReferencedCourtCasesTagValueItem[], List<ReferencedCourtCase>>(EnrichmentTagTypes.REFERENCED_COURT_CASES);
        
        tagApplier.setJsonStringParser(referencedCourtCasesJsonParser());
        tagApplier.setEnrichmentTagValueConverter(referencedCourtCasesTagValueConverter);
        tagApplier.setJudgmentUpdater(referencedCourtCasesJudgmentUpdater);

        return tagApplier;
    }
    
    
    //---- MAX_REFERENCED_MONEY TAG ----
    
    
    @Autowired
    private MoneyAmountTagValueConverter moneyAmountTagValueConverter;
    
    @Autowired
    private MaxMoneyAmountJudgmentUpdater maxMoneyAmountJudgmentUpdater;
    
    @Bean
    public JsonStringParser<MoneyAmountTagValue> moneyAmountJsonParser() {
        return new JsonStringParser<>(MoneyAmountTagValue.class);
    }
    
    @Bean
    public DefaultEnrichmentTagApplier<MoneyAmountTagValue, MoneyAmount> maxAmountTagApplier() {
        
        DefaultEnrichmentTagApplier<MoneyAmountTagValue, MoneyAmount> tagApplier = new DefaultEnrichmentTagApplier<MoneyAmountTagValue, MoneyAmount>(EnrichmentTagTypes.MAX_REFERENCED_MONEY);
        
        tagApplier.setEnrichmentTagValueConverter(moneyAmountTagValueConverter);
        tagApplier.setJsonStringParser(moneyAmountJsonParser());
        tagApplier.setJudgmentUpdater(maxMoneyAmountJudgmentUpdater);
        
        return tagApplier;
    }
    
    
    //---- REFERENCED_REGULATIONS TAG ----
    
    @Autowired
    private ReferencedRegulationsTagValueConverter referencedRegulationsTagValueConverter;
    
    @Autowired
    private ReferencedRegulationsJudgmentUpdater referencedRegulationsJudgmentUpdater;
    
    @Bean
    public JsonStringParser<ReferencedRegulationsTagValueItem[]> referencedRegulationsJsonParser() {
        return new JsonStringParser<>(ReferencedRegulationsTagValueItem[].class);
    }
    
    @Bean
    public DefaultEnrichmentTagApplier<ReferencedRegulationsTagValueItem[], List<JudgmentReferencedRegulation>> referecedRegulationsTagApplier() {
        
        DefaultEnrichmentTagApplier<ReferencedRegulationsTagValueItem[], List<JudgmentReferencedRegulation>> tagApplier = new DefaultEnrichmentTagApplier<>(EnrichmentTagTypes.REFERENCED_REGULATIONS);
        
        tagApplier.setEnrichmentTagValueConverter(referencedRegulationsTagValueConverter);
        tagApplier.setJsonStringParser(referencedRegulationsJsonParser());
        tagApplier.setJudgmentUpdater(referencedRegulationsJudgmentUpdater);
        
        return tagApplier;
    }
    
    
    //------------------------ ENRICHMENT TAG JUDGMENT REFERENCE REMOVER --------------------------
    
    @Bean
    public JsonFormatter jsonFormatter() {
        return new JsonFormatter();
    }
    
    
    @Bean
    public TagJudgmentReferenceRemover tagJudgmentReferenceRemover() {
        CompositeTagJudgmentReferenceRemover judgmentReferenceRemover = new CompositeTagJudgmentReferenceRemover();
        
        judgmentReferenceRemover.setTagJudgmentReferenceRemovers(Lists.newArrayList(
                new PageableDelegatingJudgmentReferenceRemover(refCourtCasesJudgmentReferenceRemover())));
        
        return judgmentReferenceRemover;
    }
    
    @Bean
    public RefCourtCasesJudgmentReferenceRemover refCourtCasesJudgmentReferenceRemover() {
        return new RefCourtCasesJudgmentReferenceRemover();
    }

    
}
