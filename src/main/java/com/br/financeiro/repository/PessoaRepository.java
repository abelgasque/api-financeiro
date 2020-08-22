package com.br.financeiro.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.br.financeiro.model.Pessoa;
import com.br.financeiro.repository.pessoaImpl.PessoaRepositoryQuery;

public interface PessoaRepository extends CrudRepository<Pessoa, Long>, PessoaRepositoryQuery{
	
	Optional<Pessoa> findByCpf(String cpf);
}
