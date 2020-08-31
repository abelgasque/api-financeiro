package com.br.financeiro.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.br.financeiro.model.Pessoa;
import com.br.financeiro.repository.pessoaImpl.PessoaRepositoryQuery;

public interface PessoaRepository extends CrudRepository<Pessoa, Long>, PessoaRepositoryQuery{
	
	Optional<Pessoa> findByCpf(String cpf);
	
	
	@Query(value = "SELECT * FROM pessoa WHERE EXISTS (SELECT * FROM usuario WHERE pessoa.id_usuario = ?1)", nativeQuery = true)
	Optional<Pessoa> buscarUsuarioById(Long id);
}
