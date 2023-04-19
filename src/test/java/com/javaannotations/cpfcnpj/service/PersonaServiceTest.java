package com.javaannotations.cpfcnpj.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import com.javaannotations.cpfcnpj.model.Persona;
import com.javaannotations.cpfcnpj.repository.PersonaRepository;
import com.javaannotations.cpfcnpj.service.exceptions.DocumentoInvalidoException;
import com.javaannotations.cpfcnpj.service.impl.PersonaServiceImpl;
import com.javaannotations.cpfcnpj.service.utils.CNPJUtils;
import com.javaannotations.cpfcnpj.service.utils.CPFUtils;
import com.javaannotations.cpfcnpj.service.utils.StringUtils;

@ExtendWith(MockitoExtension.class)
class PersonaServiceTest {

	@Autowired
	PersonaServiceImpl service;

	@Mock
	PersonaRepository repository;

	@Mock
	CNPJUtils cnpjUtils;

	@Mock
	CPFUtils cpfUtils;

	@Mock
	StringUtils stringUtils;

	@Captor
	ArgumentCaptor<Persona> personaCaptor;

	@BeforeEach
	void setup() {
		this.service = new PersonaServiceImpl(repository, cpfUtils, cnpjUtils, stringUtils);
	}

	@Test
	@DisplayName("Caso sucesso documentos sem pontuacao")
	void testeEsperaSucessoAoIncluirNovaPersonaComDocumentosValidosSemPontuacao() {
		// teste cenario sucesso com documentos sem pontuacao
		Persona persona = Persona.builder().cpf("43052288318").cnpj("99877404000125").build();

		when(stringUtils.removeCaracteresEspeciais(persona.getCnpj())).thenCallRealMethod();
		when(stringUtils.removeCaracteresEspeciais(persona.getCpf())).thenCallRealMethod();
		when(cnpjUtils.isCNPJ(persona.getCnpj())).thenCallRealMethod();
		when(cpfUtils.isCPF(persona.getCpf())).thenCallRealMethod();
		when(repository.save(persona)).thenReturn(persona);
		service.salvar(persona);

		verify(repository).save(personaCaptor.capture());

		var personaResponse = personaCaptor.getValue();

		assertNotNull(personaResponse);
		assertEquals("99877404000125", personaResponse.getCnpj());
		assertEquals("43052288318", personaResponse.getCpf());
	}

	@Test
	@DisplayName("Caso sucesso documentos com pontuacao")
	void testeEsperaSucessoAoIncluirNovaPersonaComDocumentosValidosComPontuacao() {
		// teste cenario sucesso com documentos sem pontuacao
		Persona persona = Persona.builder().cpf("430.522.883-18").cnpj("99.877.404/0001-25").build();

		when(stringUtils.removeCaracteresEspeciais(persona.getCnpj())).thenCallRealMethod();
		when(stringUtils.removeCaracteresEspeciais(persona.getCpf())).thenCallRealMethod();
		when(cnpjUtils.isCNPJ(anyString())).thenCallRealMethod();
		when(cpfUtils.isCPF(anyString())).thenCallRealMethod();
		when(repository.save(persona)).thenReturn(persona);

		service.salvar(persona);

		verify(repository).save(personaCaptor.capture());

		var personaResponse2 = personaCaptor.getValue();

		assertNotNull(personaResponse2);
		assertEquals("99877404000125", personaResponse2.getCnpj());
		assertEquals("43052288318", personaResponse2.getCpf());
	}

	@Test
	@DisplayName("Caso exception documentos invalidos")
	void testeEsperaExceptionAoTentarIncluirDocumentosInvalidos() {
		// documento invalido - caracteres nao permitidos com pontuacao
		Persona persona = Persona.builder().cpf("430.522.883-1G").cnpj("99.877.404/0001-25").build();

		when(stringUtils.removeCaracteresEspeciais(persona.getCnpj())).thenCallRealMethod();
		when(stringUtils.removeCaracteresEspeciais(persona.getCpf())).thenCallRealMethod();
		when(cnpjUtils.isCNPJ(anyString())).thenCallRealMethod();
		when(cpfUtils.isCPF(anyString())).thenCallRealMethod();

		var responseThrows = assertThrows(DocumentoInvalidoException.class, () -> service.salvar(persona));

		assertEquals("O documento informado nao e valido.", responseThrows.getMessage());

		// documento invalido - caracteres repetidos com pontuacao
		Persona persona2 = Persona.builder().cpf("000.000.000-00").cnpj("00.000.000/0000-00").build();

		when(stringUtils.removeCaracteresEspeciais(persona2.getCnpj())).thenCallRealMethod();
		when(stringUtils.removeCaracteresEspeciais(persona2.getCpf())).thenCallRealMethod();
		when(cnpjUtils.isCNPJ(anyString())).thenCallRealMethod();
		when(cpfUtils.isCPF(anyString())).thenCallRealMethod();

		var responseThrows2 = assertThrows(DocumentoInvalidoException.class, () -> service.salvar(persona2));

		assertEquals("O documento informado nao e valido.", responseThrows2.getMessage());

		// documento invalido - caracteres nao permitidos sem pontuacao
		Persona persona3 = Persona.builder().cpf("4305228831G").cnpj("99877404000175").build();

		when(stringUtils.removeCaracteresEspeciais(persona3.getCnpj())).thenCallRealMethod();
		when(stringUtils.removeCaracteresEspeciais(persona3.getCpf())).thenCallRealMethod();
		when(cnpjUtils.isCNPJ(anyString())).thenCallRealMethod();
		when(cpfUtils.isCPF(anyString())).thenCallRealMethod();

		var responseThrows3 = assertThrows(DocumentoInvalidoException.class, () -> service.salvar(persona3));

		assertEquals("O documento informado nao e valido.", responseThrows3.getMessage());

		// documento invalido - caracteres repetidos sem pontuacao
		Persona persona4 = Persona.builder().cpf("000000000-00").cnpj("0000000000000").build();

		when(stringUtils.removeCaracteresEspeciais(persona4.getCnpj())).thenCallRealMethod();
		when(stringUtils.removeCaracteresEspeciais(persona4.getCpf())).thenCallRealMethod();
		when(cnpjUtils.isCNPJ(anyString())).thenCallRealMethod();
		when(cpfUtils.isCPF(anyString())).thenCallRealMethod();

		var responseThrows4 = assertThrows(DocumentoInvalidoException.class, () -> service.salvar(persona4));

		assertEquals("O documento informado nao e valido.", responseThrows4.getMessage());

	}

}
