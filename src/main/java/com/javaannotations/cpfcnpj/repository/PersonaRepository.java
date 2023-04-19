package com.javaannotations.cpfcnpj.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.javaannotations.cpfcnpj.model.Persona;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Integer>{

}
