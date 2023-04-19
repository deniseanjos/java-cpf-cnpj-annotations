package com.javaannotations.cpfcnpj.service;

import com.javaannotations.cpfcnpj.model.Persona;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

public interface PersonaService {

	@Transactional
	Persona salvar(@Valid Persona persona);

}
