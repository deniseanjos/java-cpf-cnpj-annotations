package com.javaannotations.cpfcnpj.controller.resources;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.hibernate.validator.constraints.CompositionType;
import org.hibernate.validator.constraints.ConstraintComposition;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@CPF
@CNPJ
@ConstraintComposition(CompositionType.OR)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@Documented
public @interface CPFouCNPJ {
	
	String message() default "Documento informado invalido.";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

}
