package com.devs.filabancorev.dto;

import java.time.LocalDateTime;

import com.devs.filabancorev.enums.StatusSenha;
import com.devs.filabancorev.model.Senha;

import lombok.Getter;

/**
 * DTO utilizado para retornar os dados de
 * de uma senha chamada para atendimento.
 * 
 * Também é utilizado para disponibilizar os dados
 * da senha atualmente em atendimento no painel.
 */
@Getter
public class ProximaSenhaDTO {

	private String codigo;
	private StatusSenha status;
	private LocalDateTime dataInicioAtendimento;

	/**
	 * Cria um DTO a partir de uma entidade {@link Senha}.
	 * 
	 * @param senha entidade que contém os dados da senha
	 *        chamada para atendimento.
	 */
	public ProximaSenhaDTO(Senha senha) {
		this.codigo = senha.getCodigo();
		this.status = senha.getStatus();
		this.dataInicioAtendimento = senha.getDataInicioAtendimento();
	}

}
