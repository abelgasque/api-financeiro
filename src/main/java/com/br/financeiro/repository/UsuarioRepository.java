package com.br.financeiro.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.br.financeiro.model.Usuario;

public interface UsuarioRepository extends CrudRepository<Usuario, Long>{
	
	public Optional<Usuario> findByEmail(String email);
	public List<Usuario> findByPermissoesDescricao(String permissao);
	
	@Query(value = "SELECT DISTINCT * FROM usuario WHERE NOT EXISTS (SELECT * FROM pessoa WHERE pessoa.id_usuario = usuario.id)", nativeQuery = true)
	List<Usuario> listaUsuariosDisponiveis();
}
