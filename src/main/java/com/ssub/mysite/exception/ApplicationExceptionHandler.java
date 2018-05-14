package com.ssub.mysite.exception;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Component
@ControllerAdvice
public class ApplicationExceptionHandler {
	
	@ExceptionHandler( Exception.class )
	public String handleDaoException( Exception e ) {
		return "/WEB-INF/views/error/exception.jsp";
	}
}

