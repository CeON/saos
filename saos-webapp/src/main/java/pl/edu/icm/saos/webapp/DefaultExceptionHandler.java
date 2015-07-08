package pl.edu.icm.saos.webapp;

import java.io.IOException;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import pl.edu.icm.saos.webapp.security.WrongParamValueException;


@ControllerAdvice
public class DefaultExceptionHandler {
	
	
    @ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(EntityNotFoundException.class)
	public String entityNotFoundExceptionErrorHandler(EntityNotFoundException e) {
		e.printStackTrace();
		return "entityNotFound";
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(WrongParamValueException.class)
	public ModelAndView wrongParamValueExceptionErrorHandler(WrongParamValueException e, HttpServletRequest request, HttpServletResponse response) throws IOException {
	    
	    ModelAndView modelAndView = new ModelAndView("wrongParamValue");
	    modelAndView.addObject("exception", e);
	    
	    return modelAndView;
	}
}

