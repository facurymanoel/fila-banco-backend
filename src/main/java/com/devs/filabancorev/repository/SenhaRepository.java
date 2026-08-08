package com.devs.filabancorev.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devs.filabancorev.model.Senha;

public interface SenhaRepository extends JpaRepository<Senha, Long> {

}
