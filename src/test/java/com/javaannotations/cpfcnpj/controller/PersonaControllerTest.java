package com.javaannotations.cpfcnpj.controller;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javaannotations.cpfcnpj.model.Persona;
import com.javaannotations.cpfcnpj.service.PersonaService;

@RunWith(SpringRunner.class)
@WebMvcTest(PersonaController.class)
class PersonaControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private PersonaService service;

	@Test
	@DisplayName("")
	void testEsperaSucessoAoIncluirDadosValidosSemPontuacao() throws Exception {
		// teste cenario sucesso com documentos validos sem pontuacao
		Persona persona = Persona.builder().cpf("43052288318").cnpj("99877404000125").build();
		when(service.salvar(any(Persona.class))).thenReturn(persona);
		var result = mvc.perform(MockMvcRequestBuilders.post("/doc-validator")
				.content(convertObjectToJsonString(persona))
			    .contentType(MediaType.APPLICATION_JSON)
			    .accept(MediaType.APPLICATION_JSON))
			    .andExpect(MockMvcResultMatchers.status().is(201))
			    .andExpect(MockMvcResultMatchers.content().string(convertObjectToJsonString(persona)))
			    .andReturn();
		
		assertNotNull(result.getResponse());
		assertEquals(201, result.getResponse().getStatus());
		assertTrue(result.getResponse().getContentAsString().contains("43052288318"));
		
	}
	
	@Test
	void testEsperaExceptionAotentarIncluirDadosInvalidos() throws Exception {
		// teste cenario exception com documentos invalidos sem pontuacao
		Persona persona = Persona.builder().cpf(null).cnpj("0000000000001").build();
		var result = mvc.perform(MockMvcRequestBuilders.post("/doc-validator")
				.content(convertObjectToJsonString(persona))
			    .contentType(MediaType.APPLICATION_JSON)
			    .accept(MediaType.APPLICATION_JSON))
			    .andExpect(MockMvcResultMatchers.status().is(400))
			    .andReturn();
		
		assertTrue(result.getResolvedException().getMessage().contains("@CNPJ informado invalido."));
		verifyNoInteractions(service);
		
	}
	
	//Converts Object to Json String
	private String convertObjectToJsonString(Object any) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
	    return mapper.writeValueAsString(any);
	}

}
