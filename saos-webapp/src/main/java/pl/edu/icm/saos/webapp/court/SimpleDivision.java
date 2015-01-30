package pl.edu.icm.saos.webapp.court;

/**
 * Simple division DTO for use with ajax in search form select 
 * 
 * @author Łukasz Pawełczak
 */
public class SimpleDivision {
	
	
	private long id;
	private String name;
	
	
	//------------------------ GETTERS --------------------------
	
	public long getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	
	//------------------------ SETTERS --------------------------
	
	public void setId(long id) {
		this.id = id;
	}
	
	public void setName(String name) {
		this.name = name;
	}

}
