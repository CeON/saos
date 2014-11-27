package pl.edu.icm.saos.webapp.keyword;

/**
 * Simple keyword DTO for use with ajax search form, mainly for autocomplition  
 * 
 * @author Łukasz Pawełczak
 * 
 */
public class SimpleKeyword {

	private String id;
	private String phrase;
	
	
	//------------------------ SETTERS --------------------------
	
	public String getId() {
		return id;
	}
	
	public String getPhrase() {
		return phrase;
	}

	
	//------------------------ GETTERS --------------------------
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void setPhrase(String phrase) {
		this.phrase = phrase;
	}
}
