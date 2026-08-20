package com.devs.filabancorev.dto;

import java.time.LocalDateTime;

import com.devs.filabancorev.enums.StatusSenha;
import com.devs.filabancorev.model.Senha;

import lombok.Getter;

/**
 * DTO utilizado para retornar os dados de uma
 * senha após a finalização do atendimento.
 * 
 * Contém o código, o status atualizado e a
 * data de término do atendimento.
 */
@Getter
public class FinalizarSenhaDTO {

	private String codigo;
	private StatusSenha status;
	private LocalDateTime dataFimAtendimento;

	/**
	 * Cria um DTO a partir de uma entidade {@link Senha}.
	 * 
	 * @param senha entidade que contém os dados da senha
	 *        finalizada.
	 */
	public FinalizarSenhaDTO(Senha senha) {
		this.codigo = senha.getCodigo();
		this.status = senha.getStatus();
		this.dataFimAtendimento = senha.getDataFimAtendimento();
	}

}
