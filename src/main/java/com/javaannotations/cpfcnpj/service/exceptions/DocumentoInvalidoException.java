package com.javaannotations.cpfcnpj.service.exceptions;

public class DocumentoInvalidoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DocumentoInvalidoException() {
		super();
	}

	public DocumentoInvalidoException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public DocumentoInvalidoException(String message, Throwable cause) {
		super(message, cause);
	}

	public DocumentoInvalidoException(String message) {
		super(message);
	}

	public DocumentoInvalidoException(Throwable cause) {
		super(cause);
	}
	
}
