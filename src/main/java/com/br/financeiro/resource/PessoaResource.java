package com.br.financeiro.resource;

import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.br.financeiro.event.RecursoCriadoEvent;
import com.br.financeiro.model.Pessoa;
import com.br.financeiro.model.filter.PessoaFilter;
import com.br.financeiro.service.PessoaService;

@RestController
@RequestMapping("/pessoas")
public class PessoaResource {
	
	@Autowired
	private PessoaService pessoaService;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@PreAuthorize("hasAuthority('ROLE_ADMINISTRADOR') and #oauth2.hasScope('read')")
	@GetMapping("/pesquisar")
	public ResponseEntity<?> pesquisar(PessoaFilter filtro, Pageable pageable) {
		 Page<Pessoa> lista = pessoaService.pesquisar(filtro, pageable);
		 return new ResponseEntity<Page<Pessoa>>(lista,HttpStatus.OK);
	}
	
	@PreAuthorize("hasAuthority('ROLE_ADMINISTRADOR') and #oauth2.hasScope('write')")
	@PostMapping
	public ResponseEntity<?> salvar(@Valid @RequestBody Pessoa entidade, HttpServletResponse response) {
		Pessoa entidadeSalva = pessoaService.salvar(entidade);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, entidadeSalva.getId()));
		return ResponseEntity.status(HttpStatus.CREATED).body(entidadeSalva);
	}
	
	@PreAuthorize("hasAuthority('ROLE_ADMINISTRADOR') and #oauth2.hasScope('write')")
	@PutMapping
	public ResponseEntity<?> editar(@RequestBody Pessoa entidade){
		Pessoa entidadeSalva = this.pessoaService.editar(entidade);	
		return new  ResponseEntity<Pessoa>(entidadeSalva,HttpStatus.OK);
	}
	
	@PreAuthorize("hasAuthority('ROLE_ADMINISTRADOR') and #oauth2.hasScope('read')")
	@GetMapping("/{id}")
	public ResponseEntity<?> buscar(@PathVariable("id") Long id) {
		 Optional<Pessoa> entidade = pessoaService.buscarPorId(id);
		 return entidade != null ? ResponseEntity.ok(entidade) : ResponseEntity.notFound().build();
	}
	
	@PreAuthorize("hasAuthority('ROLE_ADMINISTRADOR') and #oauth2.hasScope('read')")
	@GetMapping("/buscar-por-usuario/{idUsuario}")
	public ResponseEntity<?> buscarUsuarioById(@PathVariable("idUsuario") Long idUsuario) {
		Optional<Pessoa> entidade = pessoaService.buscarUsuarioById(idUsuario);
		return entidade != null ? ResponseEntity.ok(entidade) : ResponseEntity.notFound().build();
	}
	
	@PreAuthorize("hasAuthority('ROLE_ADMINISTRADOR') and #oauth2.hasScope('read')")
	@GetMapping
	public ResponseEntity<?> listar(){
		Iterable<Pessoa> lista = this.pessoaService.listar();
		return new ResponseEntity<Iterable<Pessoa>>(lista,HttpStatus.OK);
	}
	
	@PreAuthorize("hasAuthority('ROLE_ADMINISTRADOR')")
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void excluir(@PathVariable("id") Long id){
		this.pessoaService.excluir(id);
	}
}
