package com.devs.filabancorev.dto;

import java.time.LocalDateTime;

import com.devs.filabancorev.enums.StatusSenha;
import com.devs.filabancorev.model.Senha;

import lombok.Getter;

@Getter
public class FinalizarSenhaDTO {

	private String codigo;
	private StatusSenha status;
	private LocalDateTime dataFimAtendimento;

	public FinalizarSenhaDTO(Senha senha) {
		this.codigo = senha.getCodigo();
		this.status = senha.getStatus();
		this.dataFimAtendimento = senha.getDataFimAtendimento();
	}

}
