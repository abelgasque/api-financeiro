package com.br.financeiro.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.br.financeiro.model.Usuario;

public interface UsuarioRepository extends CrudRepository<Usuario, Long>{
	
	public Optional<Usuario> findByEmail(String email);
	
	public List<Usuario> findByPermissoesDescricao(String permissao);
}
