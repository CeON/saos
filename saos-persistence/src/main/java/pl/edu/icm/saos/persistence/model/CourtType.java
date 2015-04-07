package pl.edu.icm.saos.persistence.model;

/**
 * Type of court
 * @author madryk
 */
public enum CourtType {
    
    /** pl. sąd powszechny */
    COMMON,
	
    /** pl. sąd najwyższy */
    SUPREME,
    
    /** pl. trybunał konstytucyjny */
    CONSTITUTIONAL_TRIBUNAL,
    
    /** pl. krajowa izba odwoławcza */
    NATIONAL_APPEAL_CHAMBER,
    
    /** pl. sąd administracyjny */
    ADMINISTRATIVE
}
