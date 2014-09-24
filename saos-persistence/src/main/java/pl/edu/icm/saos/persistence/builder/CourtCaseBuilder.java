package pl.edu.icm.saos.persistence.builder;

import pl.edu.icm.saos.persistence.model.CourtCase;

/**
 * @author Łukasz Dumiszewski
 * Simplified {@link pl.edu.icm.saos.persistence.model.CourtCase CourtCase} creation.
 * Do not use it in conjugation with persistence's repositories.
 */

public class CourtCaseBuilder {
    
    private CourtCase courtCase;
    
    private CourtCaseBuilder(String caseNumber) {
        this.courtCase = new CourtCase(caseNumber);
    }

    public static CourtCaseBuilder create(String caseNumber) {
        CourtCaseBuilder builder = new CourtCaseBuilder(caseNumber);
        return builder;
    }
    
    
    public CourtCase build() {
        return this.courtCase;
    }
}