package com.javaannotations.cpfcnpj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.javaannotations.cpfcnpj.model.Persona;
import com.javaannotations.cpfcnpj.service.PersonaService;

import jakarta.validation.Valid;

@RestController
@Validated
public class PersonaController {

	private final PersonaService service;
	
	@Autowired
	public PersonaController(PersonaService service) {
		this.service = service;
	}

	@PostMapping("/doc-validator")
	public ResponseEntity<Persona> salvarPersona(@Valid @RequestBody Persona persona) {
		return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(persona));
	}
	
}
