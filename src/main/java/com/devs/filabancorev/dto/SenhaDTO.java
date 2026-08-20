package com.devs.filabancorev.dto;

import java.time.LocalDateTime;

import com.devs.filabancorev.model.Senha;

import lombok.Getter;

/**
 * DTO utilizado para retornar os dados básicos de uma 
 * senha após sua emissão.
 * 
 * Contém o código da senha e a data de sua criação,
 * evitando a exposição direta da entidade {@link Senha}
 * pela API.
 */
@Getter
public class SenhaDTO {

	private String codigo;
	private LocalDateTime dataCriacao;
	
	/**
	 * Cria um DTO a partir de uma entidade {@link Senha}.
	 *  
	 * @param senha entidade que contém os dados da senha 
	 *        emitida. 
	 */
     public SenhaDTO(Senha senha) {
		this.codigo = senha.getCodigo();
		this.dataCriacao = senha.getDataCriacao();
	}

}
