package com.devs.filabancorev.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.devs.filabancorev.model.Senha;

public interface SenhaRepository extends JpaRepository<Senha, Long> {
	
	@Query(nativeQuery = true, value = """
			SELECT *
	        FROM senha
	        WHERE tipo = :tipo
	        AND data_criacao::date = CURRENT_DATE
	        ORDER BY id DESC
	        LIMIT 1
			""")
	Optional<Senha> buscarUltimaSenha(@Param("tipo") String tipo);

}
