package com.br.financeiro.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.br.financeiro.exceptionhandler.CustomRuntimeException;
import com.br.financeiro.model.Pessoa;
import com.br.financeiro.model.Usuario;
import com.br.financeiro.repository.UsuarioRepository;

@Service
public class UsuarioService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private PessoaService pessoaService;
	
	public Usuario salvar(Usuario entidade) {
		Optional<Usuario> pesquisarPorEmail = this.usuarioRepository.findByEmail(entidade.getEmail());
		if(entidade.getPermissoes().isEmpty()) {
			throw new CustomRuntimeException("Selecione pelos menos 1 permissão!");
		}
		if(pesquisarPorEmail.isPresent()) {
			throw new CustomRuntimeException("e-mail já cadastrado!");
		}
		entidade.setSenha(gerarHash(entidade.getSenha()));
		return this.usuarioRepository.save(entidade);
	}
	
	public Usuario editar(Usuario entidade) {
		Optional<Usuario> entidadeSalva = this.usuarioRepository.findById(entidade.getId());
		Optional<Usuario> pesquisarPorEmail = this.usuarioRepository.findByEmail(entidade.getEmail());
		if(!entidadeSalva.isPresent()) {
			throw new EmptyResultDataAccessException(1);
		}
		if(pesquisarPorEmail.isPresent() && entidadeSalva.get().getId() != entidade.getId()) {
			throw new CustomRuntimeException("E-mail já existe!");	
		}
		if(entidade.getPermissoes().isEmpty()) {
			throw new CustomRuntimeException("Selecione pelos menos 1 permissão!");
		}
		if(entidade.getSenha() == null) {
			entidade.setSenha(entidadeSalva.get().getSenha());
		}else {
			entidade.setSenha(gerarHash(entidade.getSenha()));
		}
		BeanUtils.copyProperties(entidade, entidadeSalva, "id");
		return this.usuarioRepository.save(entidade);
	}

	public Optional<Usuario> buscarPorId(Long id) {
		Optional<Usuario> usuario = this.usuarioRepository.findById(id);
		usuario.get().setSenha(null);
		return usuario;
	}
	
	public void excluir(Long id) {
		Optional<Usuario> entidadeSalva = this.usuarioRepository.findById(id);
		Optional<Pessoa> entidadeAssociada = this.pessoaService.buscarUsuarioById(id);
		if(!entidadeSalva.isPresent()) {
			throw new EmptyResultDataAccessException(1);
		}
		if(entidadeAssociada.isPresent()){
			throw new CustomRuntimeException("Erro ao excluir pessoa, existe usuário vinculado!");	
		}
		this.usuarioRepository.deleteById(id);
	}
	
	public Iterable<Usuario> listar() {
		return usuarioRepository.findAll();
	}	
	
	public List<Usuario> listaUsuariosDisponiveis() {
		return this.usuarioRepository.listaUsuariosDisponiveis();
	}
	
	public String gerarHash(String password) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder.encode(password);
	}
	
}
