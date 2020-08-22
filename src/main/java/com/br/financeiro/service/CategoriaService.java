package com.br.financeiro.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.br.financeiro.model.Categoria;
import com.br.financeiro.repository.CategoriaRepository;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	public Categoria salvar(Categoria entidade){
		return this.categoriaRepository.save(entidade);
	}
	
	public Categoria editar(Categoria entidade){
		Optional<Categoria> categoriaSalva = this.categoriaRepository.findById(entidade.getId());
		if(!categoriaSalva.isPresent()) {
			throw new EmptyResultDataAccessException(1);
		}
		entidade.setId(categoriaSalva.get().getId());
		return this.categoriaRepository.save(entidade);
	}
	
	public Optional<Categoria> buscarPorId(Long id){
		return this.categoriaRepository.findById(id);
	}
	
	public void excluir(Long id){
		Optional<Categoria> categoriaSalva = this.categoriaRepository.findById(id);
		if(!categoriaSalva.isPresent()) {
			throw new EmptyResultDataAccessException(1);
		}
		this.categoriaRepository.deleteById(id);
	}
	
	public Iterable<Categoria> listar() {
		return categoriaRepository.findAll();
	}
}
