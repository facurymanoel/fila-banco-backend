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

@Entity
@Table(name = "senha")
@Getter
@Setter
@NoArgsConstructor
public class Senha {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	private String codigo;

	@Enumerated(EnumType.STRING)
	private TipoSenha tipo;

	@Enumerated(EnumType.STRING)
	private StatusSenha status;

	private LocalDateTime dataCriacao;

	private LocalDateTime dataInicioAtendimento;

	private LocalDateTime dataFimAtendimento;
}
