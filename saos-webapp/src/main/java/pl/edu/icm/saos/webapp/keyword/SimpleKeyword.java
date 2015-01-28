package pl.edu.icm.saos.webapp.keyword;

/**
 * Simple keyword DTO for use with ajax search form, mainly for autocompletion  
 * 
 * @author Łukasz Pawełczak
 * 
 */
public class SimpleKeyword {

	private long id;
	private String phrase;
	
	
	//------------------------ GETTERS --------------------------
	
	public long getId() {
		return id;
	}
	
	public String getPhrase() {
		return phrase;
	}

	
	//------------------------ SETTERS --------------------------
	
	public void setId(long id) {
		this.id = id;
	}
	
	public void setPhrase(String phrase) {
		this.phrase = phrase;
	}
}
