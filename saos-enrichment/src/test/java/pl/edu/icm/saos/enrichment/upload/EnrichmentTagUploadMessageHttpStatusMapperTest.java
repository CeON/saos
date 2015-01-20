package pl.edu.icm.saos.enrichment.upload;

import static org.junit.Assert.assertNotNull;
import static pl.edu.icm.saos.enrichment.upload.EnrichmentTagUploadResponseMessages.ERROR_AUTHENTICATION_FAILED;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.junit.Test;

/**
 * 
 * @author Łukasz Dumiszewski
 *
 */
public class EnrichmentTagUploadMessageHttpStatusMapperTest {

	private EnrichmentTagUploadMessageHttpStatusMapper enrichmentTagUploadMessageHttpStatusMapper = new EnrichmentTagUploadMessageHttpStatusMapper();
	
	
	
	
	//------------------------ TESTS --------------------------
	
	@Test
	public void getHttpStatus_OK() {
		
		// execute & assert
		
		assertNotNull(enrichmentTagUploadMessageHttpStatusMapper.getHttpStatus(ERROR_AUTHENTICATION_FAILED));
		
	}
	
	
	@Test(expected=IllegalArgumentException.class)
	public void getHttpStatus_NotFound() {
		
		// execute & assert
		
		assertNotNull(enrichmentTagUploadMessageHttpStatusMapper.getHttpStatus("not existing one"));
		
	}
	
	
	@Test
	public void getHttpStatus_HttpStatusShouldBeAssignedToEachMessage() throws IllegalArgumentException, IllegalAccessException {
		
		Field[] fields = EnrichmentTagUploadResponseMessages.class.getDeclaredFields();
		
		for (Field field : fields) {
			if (Modifier.isStatic(field.getModifiers()) && field.getType().isAssignableFrom(String.class)) {
				assertNotNull(enrichmentTagUploadMessageHttpStatusMapper.getHttpStatus((String)field.get(null)));
			}
		}
	}
	
	
}
