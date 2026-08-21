package com.devs.filabancorev.enums;

/**
 * Enum que representa os possíveis estados de 
 * uma senha durante o fluxo de atendimento.
 */
public enum StatusSenha {
	
	/**
	 * Senha emitida e aguardando ser chamada.
	 */
	AGUARDANDO("Aguardando"),
	
	/**
	 * Senha que está sendo atendida no momento.
	 */
	ATENDENDO("Atendendo"),
    
	/**
	 * Senha cujo o atendimento foi finalizado.
	 */
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
