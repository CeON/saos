package pl.edu.icm.saos.webapp.division;

/**
 * Simple division DTO for use with ajax in search form select 
 * 
 * @author Łukasz Pawełczak
 */
public class SimpleDivision {
	
	
	private String id;
	private String name;
	
	
	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setId(String id) {
		this.id = id;
	}
}
