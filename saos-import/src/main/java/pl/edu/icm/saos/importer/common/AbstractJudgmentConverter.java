package pl.edu.icm.saos.importer.common;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import pl.edu.icm.saos.persistence.model.Judge;
import pl.edu.icm.saos.persistence.model.Judgment;
import pl.edu.icm.saos.persistence.model.Judgment.JudgmentType;
import pl.edu.icm.saos.persistence.model.JudgmentReasoning;
import pl.edu.icm.saos.persistence.model.JudgmentSourceInfo;
import pl.edu.icm.saos.persistence.model.JudgmentReferencedRegulation;
import pl.edu.icm.saos.persistence.model.SourceCode;

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
        
        convertCourtReporters(judgment, sourceJudgment);
        
        judgment.setDecision(extractDecision(sourceJudgment));
        
        judgment.setSummary(extractThesis(sourceJudgment));
        
        convertLegalBases(judgment, sourceJudgment);
        
        judgment.setTextContent(extractTextContent(sourceJudgment));
        
        judgment.setSourceInfo(extractSourceInfo(sourceJudgment));
        
        judgment.setJudgmentType(extractJudgmentType(sourceJudgment));
        
        convertReferencedRegulations(judgment, sourceJudgment);
        
        convertJudges(judgment, sourceJudgment);
        
        JudgmentReasoning reasoning = extractReasoning(sourceJudgment);
        reasoning.setJudgment(judgment);
        judgment.setReasoning(reasoning);
        
        
        extractSpecific(judgment, sourceJudgment);
        
        
        return judgment;
    }


 
    
    
    protected abstract JUDGMENT createNewJudgment();
    
    protected abstract String extractCaseNumber(SOURCE_JUDGMENT sourceJudgment);

    protected abstract String extractTextContent(SOURCE_JUDGMENT sourceJudgment);
    
    protected abstract DateTime extractPublicationDate(SOURCE_JUDGMENT sourceJudgment);
    
    protected abstract String extractPublisher(SOURCE_JUDGMENT sourceJudgment);
    
    protected abstract String extractReviser(SOURCE_JUDGMENT sourceJudgment);
    
    protected abstract String extractReasoningText(SOURCE_JUDGMENT sourceJudgment);

    protected abstract List<Judge> extractJudges(SOURCE_JUDGMENT sourceJudgment);

    protected abstract List<JudgmentReferencedRegulation> extractReferencedRegulations(SOURCE_JUDGMENT sourceJudgment);

    protected abstract JudgmentType extractJudgmentType(SOURCE_JUDGMENT sourceJudgment);

    protected abstract List<String> extractLegalBases(SOURCE_JUDGMENT sourceJudgment);

    protected abstract String extractThesis(SOURCE_JUDGMENT sourceJudgment);

    protected abstract String extractDecision(SOURCE_JUDGMENT sourceJudgment);

    protected abstract List<String> extractCourtReporters(SOURCE_JUDGMENT sourceJudgment);

    protected abstract LocalDate extractJudgmentDate(SOURCE_JUDGMENT sourceJudgment);

    protected abstract String extractSourceJudgmentId(SOURCE_JUDGMENT sourceJudgment);
    
    protected abstract String extractSourceUrl(SOURCE_JUDGMENT sourceJudgment);
    
    protected abstract SourceCode getSourceType();
    
    protected abstract void extractSpecific(JUDGMENT judgment, SOURCE_JUDGMENT sourceJudgment);

    
    
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
        reasoning.setSourceInfo(extractSourceInfo(sourceJudgment));
        return reasoning;
    }

    
    private JudgmentSourceInfo extractSourceInfo(SOURCE_JUDGMENT sourceJudgment) {
        JudgmentSourceInfo judgmentSource = new JudgmentSourceInfo();
        judgmentSource.setSourceJudgmentId(extractSourceJudgmentId(sourceJudgment));
        judgmentSource.setSourceJudgmentUrl(extractSourceUrl(sourceJudgment));
        judgmentSource.setPublicationDate(extractPublicationDate(sourceJudgment));
        judgmentSource.setSourceCode(getSourceType());
        judgmentSource.setReviser(extractReviser(sourceJudgment));
        judgmentSource.setPublisher(extractPublisher(sourceJudgment));
        return judgmentSource;
    }

    private void convertJudges(JUDGMENT judgment, SOURCE_JUDGMENT sourceJudgment) {
        List<Judge> judges = extractJudges(sourceJudgment);
        for (Judge judge : judges) {
            if (!judgment.containsJudge(judge)) {
                judgment.addJudge(judge);
            }
        }
    }


    private void convertReferencedRegulations(JUDGMENT judgment,
            SOURCE_JUDGMENT sourceJudgment) {
        List<JudgmentReferencedRegulation> regulations = extractReferencedRegulations(sourceJudgment);
        for (JudgmentReferencedRegulation regulation : regulations) {
            if (!judgment.containsReferencedRegulation(regulation)) {
                judgment.addReferencedRegulation(regulation);
            }
        }
    }


    private void convertLegalBases(JUDGMENT judgment,
            SOURCE_JUDGMENT sourceJudgment) {
        List<String> legalBases = extractLegalBases(sourceJudgment);
        for (String legalBase : legalBases) {
            if (!judgment.containsLegalBase(legalBase)) {
                judgment.addLegalBase(legalBase);
            }
        }
    }


    private void convertCourtReporters(JUDGMENT judgment,
            SOURCE_JUDGMENT sourceJudgment) {
        List<String> courtReporters = extractCourtReporters(sourceJudgment);
        for (String courtReporter : courtReporters) {
            if (!judgment.containsCourtReporter(courtReporter)) {
                judgment.addCourtReporter(courtReporter);
            }
        }
    }

    
    
    
    
}
