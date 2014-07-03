package pl.edu.icm.saos.importer.common;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.Judgment.JudgmentType;
import pl.edu.icm.saos.persistence.model.JudgmentReasoning;
import pl.edu.icm.saos.persistence.model.JudgmentSource;
import pl.edu.icm.saos.persistence.model.JudgmentSourceType;
import pl.edu.icm.saos.persistence.model.ReferencedRegulation;

/**
 * An abstract judgment converter. <br/>
 * Implementations of this class can focus on converting specific simple
 * properties without paying attention on setting them in Judgment.
 * 
 * @author Łukasz Dumiszewski
 */

public abstract class AbstractJudgmentConverter<JUDGMENT extends Judgment, SOURCE_JUDGMENT> implements JudgmentConverter<JUDGMENT, SOURCE_JUDGMENT> {

    
    
    /**
     * A template method that converts sourceJudgment into JUDGMENT
     */
    public final JUDGMENT convertJudgment(SOURCE_JUDGMENT sourceJudgment) {

        JUDGMENT judgment = createNewJudgment();
       
        judgment.setCaseNumber(extractCaseNumber(sourceJudgment));
        
        judgment.setJudgmentDate(extractJudgmentDate(sourceJudgment));
        
        judgment.setCourtReporters(extractCourtReporters(sourceJudgment));
       
        judgment.setDecision(extractDecision(sourceJudgment));
        
        judgment.setSummary(extractThesis(sourceJudgment));
        
        judgment.setLegalBases(extractLegalBases(sourceJudgment));
        
        judgment.setTextContent(extractTextContent(sourceJudgment));
        
        judgment.setJudgmentSource(extractJudgmentSource(sourceJudgment));
        
        judgment.setJudgmentType(extractJudgmentType(sourceJudgment));
        
        List<ReferencedRegulation> regulations = extractReferencedRegulations(sourceJudgment);
        for (ReferencedRegulation regulation : regulations) {
            judgment.addReferencedRegulation(regulation);
        }
        
        List<Judge> judges = extractJudges(sourceJudgment);
        for (Judge judge : judges) {
            judgment.addJudge(judge);
        }
        
        JudgmentReasoning reasoning = extractReasoning(sourceJudgment);
        reasoning.setJudgment(judgment);
        judgment.setReasoning(reasoning);
        
        
        extractSpecific(sourceJudgment, judgment);
        
        
        return judgment;
    }
    
    
    protected abstract JUDGMENT createNewJudgment();
    
    protected abstract String extractCaseNumber(SOURCE_JUDGMENT sourceJudgment);

    protected abstract String extractTextContent(SOURCE_JUDGMENT sourceJudgment);
    
    protected abstract DateTime extractPublicationDate(SOURCE_JUDGMENT sourceJudgment);
    
    protected abstract String extractPublisher(SOURCE_JUDGMENT sourceJudgment);
    
    protected abstract String extractReviser(SOURCE_JUDGMENT sourceJudgment);
    
    protected abstract void extractSpecific(SOURCE_JUDGMENT sourceJudgment, JUDGMENT judgment);

    protected abstract String extractReasoningText(SOURCE_JUDGMENT sourceJudgment);

    protected abstract List<Judge> extractJudges(SOURCE_JUDGMENT sourceJudgment);

    protected abstract List<ReferencedRegulation> extractReferencedRegulations(SOURCE_JUDGMENT sourceJudgment);

    protected abstract JudgmentType extractJudgmentType(SOURCE_JUDGMENT sourceJudgment);

    protected abstract List<String> extractLegalBases(SOURCE_JUDGMENT sourceJudgment);

    protected abstract String extractThesis(SOURCE_JUDGMENT sourceJudgment);

    protected abstract String extractDecision(SOURCE_JUDGMENT sourceJudgment);

    protected abstract List<String> extractCourtReporters(SOURCE_JUDGMENT sourceJudgment);

    protected abstract LocalDate extractJudgmentDate(SOURCE_JUDGMENT sourceJudgment);

    protected abstract String extractSourceJudgmentId(SOURCE_JUDGMENT sourceJudgment);
    
    protected abstract String extractSourceUrl(SOURCE_JUDGMENT sourceJudgment);
    
    protected abstract JudgmentSourceType getSourceType();
    
    
    /**
     * uses: {@link #extractPublicationDate(Object)}
     */
    protected DateTime extractReasoningPublicationDate(SOURCE_JUDGMENT sourceJudgment) {
        return extractPublicationDate(sourceJudgment);
    }
    
    /**
     * uses: {@link #extractPublisher(Object)}
     */
    protected String extractReasoningPublisher(SOURCE_JUDGMENT sourceJudgment) {
        return extractPublisher(sourceJudgment);
    }
    
    /**
     * uses: {@link #extractReviser(Object)}
     */
    protected String extractReasoningReviser(SOURCE_JUDGMENT sourceJudgment) {
        return extractReviser(sourceJudgment);
    }
    
    
    //------------------------ PRIVATE --------------------------
    
    private JudgmentReasoning extractReasoning(SOURCE_JUDGMENT sourceJudgment) {
        JudgmentReasoning reasoning = new JudgmentReasoning(); 
        reasoning.setText(extractReasoningText(sourceJudgment));
        reasoning.setPublicationDate(extractReasoningPublicationDate(sourceJudgment));
        reasoning.setPublisher(extractReasoningPublisher(sourceJudgment));
        reasoning.setReviser(extractReasoningReviser(sourceJudgment));
        return reasoning;
    }

    
    private JudgmentSource extractJudgmentSource(SOURCE_JUDGMENT sourceJudgment) {
        JudgmentSource judgmentSource = new JudgmentSource();
        judgmentSource.setSourceJudgmentId(extractSourceJudgmentId(sourceJudgment));
        judgmentSource.setSourceJudgmentUrl(extractSourceUrl(sourceJudgment));
        judgmentSource.setPublicationDate(extractPublicationDate(sourceJudgment));
        judgmentSource.setSourceType(getSourceType());
        judgmentSource.setReviser(extractReviser(sourceJudgment));
        judgmentSource.setPublisher(extractPublisher(sourceJudgment));
        return judgmentSource;
    }



    
    
    
    
}
