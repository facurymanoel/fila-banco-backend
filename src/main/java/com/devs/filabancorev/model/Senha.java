package com.devs.filabancorev.model;

import java.time.LocalDateTime;

import com.devs.filabancorev.enums.StatusSenha;
import com.devs.filabancorev.enums.TipoSenha;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/**
 * Entidade que representa uma senha de atendimento bancário.
 * 
 * Armazena as informações relacionadas à senha,
 * incluindo seu código, tipo, status e os horários
 * de criação, início e término do atendimento.
 * 
 * A entidade é persistida na tabela {@code senha}
 * do banco de dados.
 */
@Entity
@Table(name = "senha")
@Getter
@Setter
@NoArgsConstructor
public class Senha {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	/**
	 * Código identificador da senha, seguindo o padrão
	 * N001 para senhas Normais e P001 para senhas Preferenciais.
	 */
	private String codigo;

	/**
	 * Define o tipo da senha, podendo ser Normal ou Preferencial.
	 */
	@Enumerated(EnumType.STRING)
	private TipoSenha tipo;
	
	/**
	 * Define o estado atual da senha durante o fluxo 
	 * de atendimento.
	 */
    @Enumerated(EnumType.STRING)
	private StatusSenha status;

    /**
     * Data e hora em que a senha foi emitida.
     */
	private LocalDateTime dataCriacao;
	
	/**
	 * Data e hora em que o atendimento foi iniciado.
	 */
    private LocalDateTime dataInicioAtendimento;

    /**
     * Data e hora em que o atendimento foi finalizado.
     */
	private LocalDateTime dataFimAtendimento;
}
