package pl.edu.icm.saos.webapp.court;

/**
 * Simple division DTO for use with ajax in search form select 
 * 
 * @author Łukasz Pawełczak
 */
public class SimpleDivision {
	
	
	private String id;
	private String name;
	
	
	//------------------------ GETTERS --------------------------
	
	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	
	//------------------------ SETTERS --------------------------
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setId(String id) {
		this.id = id;
	}
}
