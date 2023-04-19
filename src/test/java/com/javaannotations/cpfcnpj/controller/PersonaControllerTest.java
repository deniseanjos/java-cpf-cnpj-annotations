package com.javaannotations.cpfcnpj.controller;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Disabled;
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
	@DisplayName("Teste sucesso documentos validos SEM pontuacao")
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
		assertTrue(result.getResponse().getContentAsString().contains("99877404000125"));
		
	}
	
	@Test
	@DisplayName("Teste sucesso documentos validos COM pontuacao")
	void testEsperaSucessoAoIncluirDadosValidosComPontuacao() throws Exception {
		// teste cenario sucesso com documentos validos com pontuacao
		Persona persona = Persona.builder().cpf("430.522.883-18").cnpj("99.877.404/0001-25").build();
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
		assertTrue(result.getResponse().getContentAsString().contains("430.522.883-18"));
		assertTrue(result.getResponse().getContentAsString().contains("99.877.404/0001-25"));
		
	}
	
	@Test
	@DisplayName("Teste exception documentos invalidos SEM pontuacao")
	void BUG_testEsperaExceptionAotentarIncluirDadosInvalidosSemPontuacao() throws Exception {
		// teste cenario exception com CNPJ invalido sem pontuacao
		// TODO: BUG pattern hoje aceita todos os digitos 0 no CNPJ "00000000000000"
		Persona persona = Persona.builder().cnpj("1111111111111").build();
		var result = mvc.perform(MockMvcRequestBuilders.post("/doc-validator")
				.content(convertObjectToJsonString(persona))
			    .contentType(MediaType.APPLICATION_JSON)
			    .accept(MediaType.APPLICATION_JSON))
			    .andExpect(MockMvcResultMatchers.status().is(400))
			    .andReturn();
		
		assertTrue(result.getResolvedException().getMessage().contains("@CNPJ informado invalido."));
		verifyNoInteractions(service);
		
		// teste cenario exception com CNPJ invalido sem pontuacao
		persona = Persona.builder().cnpj("11111111111111").build();
		result = mvc.perform(MockMvcRequestBuilders.post("/doc-validator")
				.content(convertObjectToJsonString(persona))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(400)).andReturn();

		assertTrue(result.getResolvedException().getMessage().contains("@CNPJ informado invalido."));
		verifyNoInteractions(service);
		
		// teste cenario exception com CPF invalido sem pontuacao
		persona = Persona.builder().cpf("00000000000").build();
		result = mvc.perform(MockMvcRequestBuilders.post("/doc-validator")
				.content(convertObjectToJsonString(persona))
			    .contentType(MediaType.APPLICATION_JSON)
			    .accept(MediaType.APPLICATION_JSON))
			    .andExpect(MockMvcResultMatchers.status().is(400))
			    .andReturn();
		
		assertTrue(result.getResolvedException().getMessage().contains("@CPF informado invalido."));
		verifyNoInteractions(service);
	}
	
	@Test
	@DisplayName("Teste exception documentos invalidos COM pontuacao")
	void BUG_testEsperaExceptionAotentarIncluirDadosInvalidosComPontuacao() throws Exception {
		// teste cenario exception com CNPJ invalido com pontuacao
		// TODO: BUG pattern hoje aceita todos os digitos 0 "00.000.000/0000-00"
		Persona persona = Persona.builder().cnpj("11.111.111/1111-11").build();
		var result = mvc.perform(MockMvcRequestBuilders.post("/doc-validator")
				.content(convertObjectToJsonString(persona))
			    .contentType(MediaType.APPLICATION_JSON)
			    .accept(MediaType.APPLICATION_JSON))
			    .andExpect(MockMvcResultMatchers.status().is(400))
			    .andReturn();
		
		assertTrue(result.getResolvedException().getMessage().contains("@CNPJ informado invalido."));
		verifyNoInteractions(service);
		
		// teste cenario exception com CNPJ invalido com pontuacao
		// TODO: BUG pattern hoje aceita todos os digitos 0
		persona = Persona.builder().cnpj("11.111.111/1111-11").build();
		result = mvc.perform(MockMvcRequestBuilders.post("/doc-validator")
				.content(convertObjectToJsonString(persona))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().is(400))
				.andReturn();

		assertTrue(result.getResolvedException().getMessage().contains("@CNPJ informado invalido."));
		verifyNoInteractions(service);
		
		// teste cenario exception com CPF invalido com pontuacao
		persona = Persona.builder().cpf("000.000.000-00").build();
		result = mvc.perform(MockMvcRequestBuilders.post("/doc-validator")
				.content(convertObjectToJsonString(persona))
			    .contentType(MediaType.APPLICATION_JSON)
			    .accept(MediaType.APPLICATION_JSON))
			    .andExpect(MockMvcResultMatchers.status().is(400))
			    .andReturn();
		
		assertTrue(result.getResolvedException().getMessage().contains("@CPF informado invalido."));
		verifyNoInteractions(service);
	}
	
	@Test
	@DisplayName("Teste metodo inclusao documento valido COM pontuacao e com ambas anotacoes antes da implementacao da regra de negocio")
	@Disabled("Implementacao de regra que nao permite pontuacao.")
	void testeEsperaSucessoAoIncluirDadosValidosComPontuacao() throws Exception {
		// teste cenario passando CPF com pontuacao
		Persona persona = Persona.builder().documento("430.522.883-18").build();
		when(service.salvar(any(Persona.class))).thenReturn(persona);
		var result = mvc.perform(MockMvcRequestBuilders.post("/doc-validator")
				.content(convertObjectToJsonString(persona))
			    .contentType(MediaType.APPLICATION_JSON)
			    .accept(MediaType.APPLICATION_JSON))
			    .andExpect(MockMvcResultMatchers.status().is(201))
			    .andReturn();
		
		assertNotNull(result.getResponse());
		assertEquals(201, result.getResponse().getStatus());
		assertTrue(result.getResponse().getContentAsString().contains("430.522.883-18"));
		
		// teste cenario passando CNPJ com pontuacao
		persona = Persona.builder().documento("99.877.404/0001-25").build();
		when(service.salvar(any(Persona.class))).thenReturn(persona);
		result = mvc.perform(MockMvcRequestBuilders.post("/doc-validator")
				.content(convertObjectToJsonString(persona))
			    .contentType(MediaType.APPLICATION_JSON)
			    .accept(MediaType.APPLICATION_JSON))
			    .andExpect(MockMvcResultMatchers.status().is(201))
			    .andReturn();
				
		assertNotNull(result.getResponse());
		assertEquals(201, result.getResponse().getStatus());
		assertTrue(result.getResponse().getContentAsString().contains("99.877.404/0001-25"));
	}
	
	@Test
	@DisplayName("Teste exception ao tentar incluir documento valido com pontuacao")
	void testeEsperaExcecaoAoTentarIncluirDadosValidosComPontuacao() throws Exception {
		// teste cenario passando CPF com pontuacao
		Persona persona = Persona.builder().documento("430.522.883-18").build();
		var result = mvc.perform(MockMvcRequestBuilders.post("/doc-validator")
				.content(convertObjectToJsonString(persona))
			    .contentType(MediaType.APPLICATION_JSON)
			    .accept(MediaType.APPLICATION_JSON))
			    .andExpect(MockMvcResultMatchers.status().is(400))
			    .andReturn();
		
		assertTrue(result.getResolvedException().getMessage().contains("CPF deve conter 11 digitos e CNPJ deve conter 14 digitos. Nao e aceito pontuacoes"));
		verifyNoInteractions(service);
		
		// teste cenario passando CNPJ com pontuacao
		persona = Persona.builder().documento("99.877.404/0001-25").build();
		result = mvc.perform(MockMvcRequestBuilders.post("/doc-validator")
				.content(convertObjectToJsonString(persona))
			    .contentType(MediaType.APPLICATION_JSON)
			    .accept(MediaType.APPLICATION_JSON))
			    .andExpect(MockMvcResultMatchers.status().is(400))
			    .andReturn();
				
		assertTrue(result.getResolvedException().getMessage().contains("CPF deve conter 11 digitos e CNPJ deve conter 14 digitos. Nao e aceito pontuacoes"));
		verifyNoInteractions(service);
	}
	
	@Test
	@DisplayName("Teste exception ao tentar incluir documento invalido SEM pontuacao")
	void testeEsperaExcecaoAoTentarIncluirDadosInvalidosRepetidos() throws Exception {
		// teste cenario passando CPF invalido com numeros repetidos
		Persona persona = Persona.builder().documento("00000000000").build();
		var result = mvc.perform(MockMvcRequestBuilders.post("/doc-validator")
				.content(convertObjectToJsonString(persona))
			    .contentType(MediaType.APPLICATION_JSON)
			    .accept(MediaType.APPLICATION_JSON))
			    .andExpect(MockMvcResultMatchers.status().is(400))
			    .andReturn();
		
		assertTrue(result.getResolvedException().getMessage().contains("invalid Brazilian individual taxpayer registry number (CPF)"));
		verifyNoInteractions(service);
		
		// teste cenario passando CPF invalido com numeros repetidos
		persona = Persona.builder().documento("11111111111").build();
		result = mvc.perform(MockMvcRequestBuilders.post("/doc-validator")
				.content(convertObjectToJsonString(persona))
			    .contentType(MediaType.APPLICATION_JSON)
			    .accept(MediaType.APPLICATION_JSON))
			    .andExpect(MockMvcResultMatchers.status().is(400))
			    .andReturn();
		
		assertTrue(result.getResolvedException().getMessage().contains("invalid Brazilian individual taxpayer registry number (CPF)"));
		verifyNoInteractions(service);
		
		// teste cenario passando CPF invalido com numeros repetidos
		persona = Persona.builder().documento("22222222222").build();
		result = mvc.perform(MockMvcRequestBuilders.post("/doc-validator")
				.content(convertObjectToJsonString(persona))
			    .contentType(MediaType.APPLICATION_JSON)
			    .accept(MediaType.APPLICATION_JSON))
			    .andExpect(MockMvcResultMatchers.status().is(400))
			    .andReturn();
		
		assertTrue(result.getResolvedException().getMessage().contains("invalid Brazilian individual taxpayer registry number (CPF)"));
		verifyNoInteractions(service);
		
		// teste cenario passando CPF invalido com numeros repetidos
		persona = Persona.builder().documento("33333333333").build();
		result = mvc.perform(MockMvcRequestBuilders.post("/doc-validator")
				.content(convertObjectToJsonString(persona))
			    .contentType(MediaType.APPLICATION_JSON)
			    .accept(MediaType.APPLICATION_JSON))
			    .andExpect(MockMvcResultMatchers.status().is(400))
			    .andReturn();
		
		assertTrue(result.getResolvedException().getMessage().contains("invalid Brazilian individual taxpayer registry number (CPF)"));
		verifyNoInteractions(service);
		
		// teste cenario passando CPF invalido com numeros repetidos
		persona = Persona.builder().documento("44444444444").build();
		result = mvc.perform(MockMvcRequestBuilders.post("/doc-validator")
				.content(convertObjectToJsonString(persona))
			    .contentType(MediaType.APPLICATION_JSON)
			    .accept(MediaType.APPLICATION_JSON))
			    .andExpect(MockMvcResultMatchers.status().is(400))
			    .andReturn();
		
		assertTrue(result.getResolvedException().getMessage().contains("invalid Brazilian individual taxpayer registry number (CPF)"));
		verifyNoInteractions(service);
		
		// teste cenario passando CPF invalido com numeros repetidos
		persona = Persona.builder().documento("55555555555").build();
		result = mvc.perform(MockMvcRequestBuilders.post("/doc-validator")
				.content(convertObjectToJsonString(persona))
			    .contentType(MediaType.APPLICATION_JSON)
			    .accept(MediaType.APPLICATION_JSON))
			    .andExpect(MockMvcResultMatchers.status().is(400))
			    .andReturn();
		
		assertTrue(result.getResolvedException().getMessage().contains("invalid Brazilian individual taxpayer registry number (CPF)"));
		verifyNoInteractions(service);
		
		// teste cenario passando CPF invalido com numeros repetidos
		persona = Persona.builder().documento("66666666666").build();
		result = mvc.perform(MockMvcRequestBuilders.post("/doc-validator")
				.content(convertObjectToJsonString(persona))
			    .contentType(MediaType.APPLICATION_JSON)
			    .accept(MediaType.APPLICATION_JSON))
			    .andExpect(MockMvcResultMatchers.status().is(400))
			    .andReturn();
		
		assertTrue(result.getResolvedException().getMessage().contains("invalid Brazilian individual taxpayer registry number (CPF)"));
		verifyNoInteractions(service);
		
		// teste cenario passando CPF invalido com numeros repetidos
		persona = Persona.builder().documento("77777777777").build();
		result = mvc.perform(MockMvcRequestBuilders.post("/doc-validator")
				.content(convertObjectToJsonString(persona))
			    .contentType(MediaType.APPLICATION_JSON)
			    .accept(MediaType.APPLICATION_JSON))
			    .andExpect(MockMvcResultMatchers.status().is(400))
			    .andReturn();
		
		assertTrue(result.getResolvedException().getMessage().contains("invalid Brazilian individual taxpayer registry number (CPF)"));
		verifyNoInteractions(service);
		// teste cenario passando CPF invalido com numeros repetidos
		persona = Persona.builder().documento("88888888888").build();
		result = mvc.perform(MockMvcRequestBuilders.post("/doc-validator")
				.content(convertObjectToJsonString(persona))
			    .contentType(MediaType.APPLICATION_JSON)
			    .accept(MediaType.APPLICATION_JSON))
			    .andExpect(MockMvcResultMatchers.status().is(400))
			    .andReturn();
		
		assertTrue(result.getResolvedException().getMessage().contains("invalid Brazilian individual taxpayer registry number (CPF)"));
		verifyNoInteractions(service);
		
		// teste cenario passando CPF invalido com numeros repetidos
		persona = Persona.builder().documento("99999999999").build();
		result = mvc.perform(MockMvcRequestBuilders.post("/doc-validator")
				.content(convertObjectToJsonString(persona))
			    .contentType(MediaType.APPLICATION_JSON)
			    .accept(MediaType.APPLICATION_JSON))
			    .andExpect(MockMvcResultMatchers.status().is(400))
			    .andReturn();
		
		assertTrue(result.getResolvedException().getMessage().contains("invalid Brazilian individual taxpayer registry number (CPF)"));
		verifyNoInteractions(service);
				
		// teste cenario passando CNPJ invalido com numeros repetidos
		persona = Persona.builder().documento("00000000000000").build();
		result = mvc.perform(MockMvcRequestBuilders.post("/doc-validator")
				.content(convertObjectToJsonString(persona))
			    .contentType(MediaType.APPLICATION_JSON)
			    .accept(MediaType.APPLICATION_JSON))
			    .andExpect(MockMvcResultMatchers.status().is(400))
			    .andReturn();
				
		assertTrue(result.getResolvedException().getMessage().contains("invalid Brazilian corporate taxpayer registry number (CNPJ)"));
		verifyNoInteractions(service);
		
		// teste cenario passando CNPJ invalido com numeros repetidos
		persona = Persona.builder().documento("11111111111111").build();
		result = mvc.perform(MockMvcRequestBuilders.post("/doc-validator")
				.content(convertObjectToJsonString(persona))
			    .contentType(MediaType.APPLICATION_JSON)
			    .accept(MediaType.APPLICATION_JSON))
			    .andExpect(MockMvcResultMatchers.status().is(400))
			    .andReturn();
				
		assertTrue(result.getResolvedException().getMessage().contains("invalid Brazilian corporate taxpayer registry number (CNPJ)"));
		verifyNoInteractions(service);
		
		// teste cenario passando CNPJ invalido com numeros repetidos
		persona = Persona.builder().documento("22222222222222").build();
		result = mvc.perform(MockMvcRequestBuilders.post("/doc-validator")
				.content(convertObjectToJsonString(persona))
			    .contentType(MediaType.APPLICATION_JSON)
			    .accept(MediaType.APPLICATION_JSON))
			    .andExpect(MockMvcResultMatchers.status().is(400))
			    .andReturn();
				
		assertTrue(result.getResolvedException().getMessage().contains("invalid Brazilian corporate taxpayer registry number (CNPJ)"));
		verifyNoInteractions(service);	
		
		// teste cenario passando CNPJ invalido com numeros repetidos
		persona = Persona.builder().documento("33333333333333").build();
		result = mvc.perform(MockMvcRequestBuilders.post("/doc-validator")
				.content(convertObjectToJsonString(persona))
			    .contentType(MediaType.APPLICATION_JSON)
			    .accept(MediaType.APPLICATION_JSON))
			    .andExpect(MockMvcResultMatchers.status().is(400))
			    .andReturn();
				
		assertTrue(result.getResolvedException().getMessage().contains("invalid Brazilian corporate taxpayer registry number (CNPJ)"));
		verifyNoInteractions(service);
		
		// teste cenario passando CNPJ invalido com numeros repetidos
		persona = Persona.builder().documento("44444444444444").build();
		result = mvc.perform(MockMvcRequestBuilders.post("/doc-validator")
				.content(convertObjectToJsonString(persona))
			    .contentType(MediaType.APPLICATION_JSON)
			    .accept(MediaType.APPLICATION_JSON))
			    .andExpect(MockMvcResultMatchers.status().is(400))
			    .andReturn();
				
		assertTrue(result.getResolvedException().getMessage().contains("invalid Brazilian corporate taxpayer registry number (CNPJ)"));
		verifyNoInteractions(service);
		
		// teste cenario passando CNPJ invalido com numeros repetidos
		persona = Persona.builder().documento("555555555555").build();
		result = mvc.perform(MockMvcRequestBuilders.post("/doc-validator")
				.content(convertObjectToJsonString(persona))
			    .contentType(MediaType.APPLICATION_JSON)
			    .accept(MediaType.APPLICATION_JSON))
			    .andExpect(MockMvcResultMatchers.status().is(400))
			    .andReturn();
				
		assertTrue(result.getResolvedException().getMessage().contains("invalid Brazilian corporate taxpayer registry number (CNPJ)"));
		verifyNoInteractions(service);
		
		// teste cenario passando CNPJ invalido com numeros repetidos
		persona = Persona.builder().documento("66666666666666").build();
		result = mvc.perform(MockMvcRequestBuilders.post("/doc-validator")
				.content(convertObjectToJsonString(persona))
			    .contentType(MediaType.APPLICATION_JSON)
			    .accept(MediaType.APPLICATION_JSON))
			    .andExpect(MockMvcResultMatchers.status().is(400))
			    .andReturn();
				
		assertTrue(result.getResolvedException().getMessage().contains("invalid Brazilian corporate taxpayer registry number (CNPJ)"));
		verifyNoInteractions(service);	
		
		// teste cenario passando CNPJ invalido com numeros repetidos
		persona = Persona.builder().documento("77777777777777").build();
		result = mvc.perform(MockMvcRequestBuilders.post("/doc-validator")
				.content(convertObjectToJsonString(persona))
			    .contentType(MediaType.APPLICATION_JSON)
			    .accept(MediaType.APPLICATION_JSON))
			    .andExpect(MockMvcResultMatchers.status().is(400))
			    .andReturn();
				
		assertTrue(result.getResolvedException().getMessage().contains("invalid Brazilian corporate taxpayer registry number (CNPJ)"));
		verifyNoInteractions(service);
		
		// teste cenario passando CNPJ invalido com numeros repetidos
		persona = Persona.builder().documento("88888888888888").build();
		result = mvc.perform(MockMvcRequestBuilders.post("/doc-validator")
				.content(convertObjectToJsonString(persona))
			    .contentType(MediaType.APPLICATION_JSON)
			    .accept(MediaType.APPLICATION_JSON))
			    .andExpect(MockMvcResultMatchers.status().is(400))
			    .andReturn();
				
		assertTrue(result.getResolvedException().getMessage().contains("invalid Brazilian corporate taxpayer registry number (CNPJ)"));
		verifyNoInteractions(service);
		
		// teste cenario passando CNPJ invalido com numeros repetidos
		persona = Persona.builder().documento("99999999999999").build();
		result = mvc.perform(MockMvcRequestBuilders.post("/doc-validator")
				.content(convertObjectToJsonString(persona))
			    .contentType(MediaType.APPLICATION_JSON)
			    .accept(MediaType.APPLICATION_JSON))
			    .andExpect(MockMvcResultMatchers.status().is(400))
			    .andReturn();
				
		assertTrue(result.getResolvedException().getMessage().contains("invalid Brazilian corporate taxpayer registry number (CNPJ)"));
		verifyNoInteractions(service);
		
		// teste cenario passando CNPJ invalido com numeros repetidos
		persona = Persona.builder().documento("14572457000188").build();
		result = mvc.perform(MockMvcRequestBuilders.post("/doc-validator")
				.content(convertObjectToJsonString(persona))
			    .contentType(MediaType.APPLICATION_JSON)
			    .accept(MediaType.APPLICATION_JSON))
			    .andExpect(MockMvcResultMatchers.status().is(400))
			    .andReturn();
				
		assertTrue(result.getResolvedException().getMessage().contains("invalid Brazilian corporate taxpayer registry number (CNPJ)"));
		verifyNoInteractions(service);
	}
	
	@Test
	@DisplayName("Teste sucesso ao tentar incluir documento valido SEM pontuacao")
	void testeEsperaSucessoAoIncluirDadosValidosSemPontuacao() throws Exception {
		// teste cenario passando CPF sem pontuacao
		Persona persona = Persona.builder().documento("43052288318").build();
		when(service.salvar(any(Persona.class))).thenReturn(persona);
		var result = mvc.perform(MockMvcRequestBuilders.post("/doc-validator")
				.content(convertObjectToJsonString(persona))
			    .contentType(MediaType.APPLICATION_JSON)
			    .accept(MediaType.APPLICATION_JSON))
			    .andExpect(MockMvcResultMatchers.status().is(201))
			    .andReturn();
		
		assertNotNull(result.getResponse());
		assertEquals(201, result.getResponse().getStatus());
		assertTrue(result.getResponse().getContentAsString().contains("43052288318"));
		
		// teste cenario passando CNPJ sem pontuacao
		persona = Persona.builder().documento("99877404000125").build();
		when(service.salvar(any(Persona.class))).thenReturn(persona);
		result = mvc.perform(MockMvcRequestBuilders.post("/doc-validator")
				.content(convertObjectToJsonString(persona))
			    .contentType(MediaType.APPLICATION_JSON)
			    .accept(MediaType.APPLICATION_JSON))
			    .andExpect(MockMvcResultMatchers.status().is(201))
			    .andReturn();
				
		assertNotNull(result.getResponse());
		assertEquals(201, result.getResponse().getStatus());
		assertTrue(result.getResponse().getContentAsString().contains("99877404000125"));
	}
	
	//Converts Object to Json String
	private String convertObjectToJsonString(Object any) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
	    return mapper.writeValueAsString(any);
	}

}
