package com.br.financeiro.model;

public enum Situacao {
	ATIVO("Ativo"),
	INATIVO("Inativo");
	
	private final String descricao;
	
	Situacao(String descricao){
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}
