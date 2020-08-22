package com.br.financeiro.repository.pessoaImpl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.br.financeiro.model.Pessoa;
import com.br.financeiro.model.filter.PessoaFilter;

public interface PessoaRepositoryQuery {
	
	public Page<Pessoa> filtrar(PessoaFilter filtro, Pageable pageable);
}
