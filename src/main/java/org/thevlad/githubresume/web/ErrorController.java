package org.thevlad.githubresume.web;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class ErrorController {

	@ExceptionHandler(Throwable.class)
	public ModelAndView handleConstraintViolationException(Throwable ex) {
		ModelAndView model = new ModelAndView();

		model.addObject("errorMessage", ex.getMessage());
		model.setViewName("search");
		return model;
	}
	
	
}
