package pl.edu.icm.saos.webapp;

import javax.persistence.EntityNotFoundException;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class DefaultExceptionHandler {
	
	
	@ExceptionHandler(EntityNotFoundException.class)
	public String entityNotFoundExceptionErrorHandler(EntityNotFoundException e) {
		e.printStackTrace();
		return "judgmentNotFound";
	}
	
}

