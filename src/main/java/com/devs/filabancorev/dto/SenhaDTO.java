package com.devs.filabancorev.dto;

import java.time.LocalDateTime;

import com.devs.filabancorev.model.Senha;

import lombok.Getter;

@Getter
public class SenhaDTO {

	private String codigo;
	private LocalDateTime dataCriacao;

	public SenhaDTO(Senha senha) {
		this.codigo = senha.getCodigo();
		this.dataCriacao = senha.getDataCriacao();
	}

}
