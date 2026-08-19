package com.devs.filabancorev.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
/**
 * Responsável pelo tratamento global das exceções
 * lançadas durante a execução da API.
 * 
 * Converte exeções do tipo {@link RuntimeException}
 * em respostas HTTP com status {@code 400 BAD_REQUEST},
 * retornando ao cliente a mensagem da exceção.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
	
	/**
	 * Trata exceções do tipo {@link RuntimeException}.
	 *  
	 * @param ex exceção lançada durante o processamento
	 *        da requisição.
	 * @return mensagem associada à exceção. 
	 */
    @ExceptionHandler(RuntimeException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public String tratarRuntimeException(RuntimeException ex) {
		return ex.getMessage();

	}

}
