package com.devs.filabancorev.dto;

import java.time.LocalDateTime;

import com.devs.filabancorev.enums.StatusSenha;
import com.devs.filabancorev.model.Senha;

import lombok.Getter;

@Getter
public class ProximaSenhaDTO {

	private String codigo;
	private StatusSenha status;
	private LocalDateTime dataInicioAtendimento;

	public ProximaSenhaDTO(Senha senha) {
		this.codigo = senha.getCodigo();
		this.status = senha.getStatus();
		this.dataInicioAtendimento = senha.getDataInicioAtendimento();
	}

}
