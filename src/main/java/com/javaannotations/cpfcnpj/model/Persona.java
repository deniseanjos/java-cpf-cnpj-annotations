package com.javaannotations.cpfcnpj.model;

import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;

import com.javaannotations.cpfcnpj.controller.resources.CPFouCNPJ;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@ToString
@Entity(name = "db_persona")
public class Persona {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@CPF(message = "@CPF informado invalido.")
	private String cpf;

	@CNPJ(message = "@CNPJ informado invalido.")
	private String cnpj;

	@CPFouCNPJ
	@Pattern(regexp = "([0-9]{11}|[0-9]{14})", message = "CPF deve conter 11 digitos e CNPJ deve conter 14 digitos. Nao e aceito pontuacoes.")
	@Pattern(regexp = "^(?:(?![0]{11}|[1]{11}|[2]{11}|[3]{11}|[4]{11}|[5]{11}|[6]{11}|[7]{11}|[8]{11}|[9]{11}).)*$", message = "CPF nao deve conter digitos repetidos.")
	@Pattern(regexp = "^(?:(?![0]{14}|[1]{14}|[2]{14}|[3]{14}|[4]{14}|[5]{14}|[6]{14}|[7]{14}|[8]{14}|[9]{14}).)*$", message = "CNPJ nao deve conter digitos repetidos.")
	private String documento;
}
