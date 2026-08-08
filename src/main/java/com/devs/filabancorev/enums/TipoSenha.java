package com.devs.filabancorev.enums;

public enum TipoSenha {

	NORMAL("Normal"), 
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
