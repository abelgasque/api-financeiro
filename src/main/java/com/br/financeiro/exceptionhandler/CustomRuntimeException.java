package com.br.financeiro.exceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class CustomRuntimeException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public CustomRuntimeException(String message) {
		super(message);
	}
	
}
