package com.javaannotations.cpfcnpj.service.utils;

public class StringUtils {
	
	public String removeCaracteresEspeciais(String value) {
		return value.replace("/", "").replace("-", "").replace(".", "");
	}

}
