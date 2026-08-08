package com.devs.filabancorev.enums;

public enum StatusSenha {
	
	AGUARDANDO("Aguardando"),
	ATENDENDO("Atendendo"),
    FINALIZADO("Finalizado");
    
    private final String descricao;

	private StatusSenha(String descricao) {
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
