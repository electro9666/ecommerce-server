package org.example.config;

import org.example.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.sentry.Sentry;

//@Aspect
//@Component
@RestControllerAdvice
public class ControllerAspect {
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponseDto> handleCustomException(Exception ex) {
		// TODO 일단...
		ex.printStackTrace();
		Sentry.captureException(ex);
		return new ResponseEntity<>(ErrorResponseDto.of(ex.getMessage()), null, HttpStatus.BAD_REQUEST);
	}
}
