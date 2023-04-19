package com.javaannotations.cpfcnpj.service;

import java.util.InputMismatchException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.javaannotations.cpfcnpj.model.Persona;
import com.javaannotations.cpfcnpj.repository.PersonaRepository;
import com.javaannotations.cpfcnpj.service.exceptions.DocumentoInvalidoException;
import com.javaannotations.cpfcnpj.service.exceptions.ValidacaoDocumentoException;
import com.javaannotations.cpfcnpj.service.utils.CNPJUtils;
import com.javaannotations.cpfcnpj.service.utils.CPFUtils;
import com.javaannotations.cpfcnpj.service.utils.StringUtils;

import jakarta.validation.Valid;

@Service
@Validated
public class PersonaService {
	
	private final PersonaRepository repository;
	private final CPFUtils cpfUtils;
	private final CNPJUtils cnpjUtils;
	private final StringUtils stringUtils;
	
	@Autowired
	public PersonaService(PersonaRepository repository, CPFUtils cpfUtils, CNPJUtils cnpjUtils, StringUtils stringUtils) {
		this.repository = repository;
		this.cnpjUtils = cnpjUtils;
		this.cpfUtils = cpfUtils;
		this.stringUtils = stringUtils;
	}

	@Transactional
	public Persona salvar(@Valid Persona persona) {
		boolean cpfValido = false;
		boolean cnpjValido = false;
		try {
			if(!persona.getCpf().isEmpty()) {
				persona.setCpf(stringUtils.removeCaracteresEspeciais(persona.getCpf()));
				cpfValido = cpfUtils.isCPF(persona.getCpf());
			}
			
			if(!persona.getCnpj().isBlank()) {
				persona.setCnpj(stringUtils.removeCaracteresEspeciais(persona.getCnpj()));
				cnpjValido = cnpjUtils.isCNPJ(persona.getCnpj());
			}
			
			if(!cnpjValido || !cpfValido) {
				throw new DocumentoInvalidoException("O documento informado nao e valido.");
			}
		} catch (InputMismatchException e) {
			throw new ValidacaoDocumentoException("Ocorreu um problema ao validar o documento informado. Tente novamente.", e);
		}
		return repository.save(persona);
	}

}
