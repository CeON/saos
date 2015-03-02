package pl.edu.icm.saos.webapp;

import org.hibernate.ObjectNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class DefaultExceptionHandler {
	
	
	@ExceptionHandler(ObjectNotFoundException.class)
	public String objectNotFoundExceptionErrorHandler(ObjectNotFoundException e) {
		e.printStackTrace();
		
		return "judgmentNotFound";
	}
	
}

