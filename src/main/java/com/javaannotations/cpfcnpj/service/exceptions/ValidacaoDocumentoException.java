package com.javaannotations.cpfcnpj.service.exceptions;

public class ValidacaoDocumentoException extends DocumentoInvalidoException {

	private static final long serialVersionUID = 1L;

	public ValidacaoDocumentoException() {
		super();
	}

	public ValidacaoDocumentoException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ValidacaoDocumentoException(String message, Throwable cause) {
		super(message, cause);
	}

	public ValidacaoDocumentoException(String message) {
		super(message);
	}

	public ValidacaoDocumentoException(Throwable cause) {
		super(cause);
	}
	
}
