package com.devs.filabancorev.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.devs.filabancorev.model.Senha;

/**
 * Repository responsável pelo acesso e persistência dos dados
 * das senhas no banco de dados.
 * 
 * Estende {@link JpaRepository} para disponibilizar as 
 * operações padrão de persistência e possui consultas
 * personalizadas utilizadas pelas regras de negócio da aplicação.
 *
 */
public interface SenhaRepository extends JpaRepository<Senha, Long> {
	
	/**
	 * Busca a última senha emitida de um determinado tipo
	 * na data atual.
	 * 
	 * A consulta é utilizada para determinar o próximo número
	 * da sequência de senhas Normais ou Preferenciais.
	 * 
	 * @param tipo tipo da senha a ser consultada
	 * @return última senha do tipo informado no dia atual,
	 *         ou {@link Optional#empty()} caso não exista.
	 */
	@Query(nativeQuery = true, value = """
			SELECT *
	        FROM senha
	        WHERE tipo = :tipo
	        AND data_criacao::date = CURRENT_DATE
	        ORDER BY id DESC
	        LIMIT 1
			""")
	Optional<Senha> buscarUltimaSenha(@Param("tipo") String tipo);
	
	
	/**
	 * Busca uma senha que esteja atualmente em atendimento.
	 * 
	 * É utilizada para verificar se já existe um atendimento
	 * em andamento antes de chamar uma nova senha.
	 * 
	 * @return senha com status {@code ATENDENDO}
	 *         ou {@link Optional#empty()} caso não exista.
	 */
	@Query("""
			SELECT s
			FROM Senha s
			 WHERE s.status = 'ATENDENDO'
			 """)
	Optional<Senha> buscarSenhaEmAtendimento();
	
	/**
	 * Busca a próxima senha preferencial aguardando
	 * atendimento.
	 * 
	 * A senha mais antiga é priorizada de acordo com a ordem
	 * crescente do identificador.
	 * 
	 * @return próxima senha preferencial aguardando atendimento,
	 *         ou {@link Optional#empty()} caso não exista.
	 */
	@Query(nativeQuery = true, value = """
			SELECT *
			FROM senha
			WHERE tipo = 'PREFERENCIAL'
            AND status = 'AGUARDANDO'
			ORDER BY id ASC
			LIMIT 1
			""")
	Optional<Senha>buscarProximaPreferencial();
	
	 /**
	  * Busca a próxima senha Normal aguardando atendimento.
	  * 
	  * A senha mais antiga é priorizada de acordo com a ordem
	  * crescente do identificador.
	  * 
	  * @return próxima senha Normal aguardando atendimento,
	  *         ou {@link Optional#empty()} caso não exista.
	  */
	@Query(nativeQuery = true, value = """
			SELECT *
			FROM senha
			WHERE tipo = 'NORMAL'
            AND status = 'AGUARDANDO'
			ORDER BY id ASC
			LIMIT 1
			""")
	Optional<Senha>buscarProximaNormal();
	
	/**
	 * Busca a senha que está atualmente em atendimento
	 * para permitir finalização do atendimento.
	 * 
	 * @return senha com status {@code ATENDENDO}
	 *         ou {@link Optional#empty()} caso não exista.
	 */
	@Query(nativeQuery = true, value = """
			SELECT *
			FROM senha
			WHERE status = 'ATENDENDO'
			ORDER BY id ASC
			LIMIT 1
			""")
	Optional<Senha>finalizarAtendimentoSenha();
	
	 
	
	

}
