package com.devs.filabancorev.enums;

/**
 * Enum que representa os tipos de senha disponíveis
 * no sistema de atendimento bancário.
 */
public enum TipoSenha {

	/**
	 * Senha de atendimento normal.
	 */
	NORMAL("Normal"), 
	
	/**
	 * Senha de atendimento preferencial.
	 */
	PREFERENCIAL("Preferencial");

	private final String descricao;

	private TipoSenha(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	@Override
	public String toString() {

		return this.descricao;
	}

}
