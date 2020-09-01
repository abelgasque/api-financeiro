package com.br.financeiro.resource;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
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
import com.br.financeiro.model.Usuario;
import com.br.financeiro.service.UsuarioService;

@RestController
@RequestMapping("/usuarios")
public class UsuarioResource {
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
//	@PreAuthorize("hasAuthority('ROLE_ADMINISTRADOR') and #oauth2.hasScope('read')")
//	@GetMapping("/pesquisar")
//	public ResponseEntity<?> pesquisar(UsuarioFilter filtro, Pageable pageable) {
//		 Page<Usuario> lista = usuarioService.pesquisar(filtro, pageable);
//		 return new ResponseEntity<Page<Usuario>>(lista,HttpStatus.OK);
//	}
	
	@PostMapping("/adicionar")
	public ResponseEntity<?> salvar(@Valid @RequestBody Usuario entidade, HttpServletResponse response) {
		Usuario entidadeSalva = usuarioService.salvar(entidade);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, entidadeSalva.getId()));
		return ResponseEntity.status(HttpStatus.CREATED).body(entidadeSalva);
	}
	
	@PreAuthorize("hasAuthority('ROLE_ADMINISTRADOR') and #oauth2.hasScope('write')")
	@PutMapping
	public ResponseEntity<?> editar(@RequestBody Usuario entidade){
		Usuario entidadeSalva = this.usuarioService.editar(entidade);	
		return new  ResponseEntity<Usuario>(entidadeSalva,HttpStatus.OK);
	}
	
	@PreAuthorize("hasAuthority('ROLE_ADMINISTRADOR') and #oauth2.hasScope('read')")
	@GetMapping("/{id}")
	public ResponseEntity<?> buscar(@PathVariable("id") Long id) {
		 Optional<Usuario> entidade = usuarioService.buscarPorId(id);
		 return entidade != null ? ResponseEntity.ok(entidade) : ResponseEntity.notFound().build();
	}
	
	@PreAuthorize("hasAuthority('ROLE_ADMINISTRADOR') and #oauth2.hasScope('read')")
	@GetMapping
	public ResponseEntity<?> listar(){
		Iterable<Usuario> lista = this.usuarioService.listar();
		return new ResponseEntity<Iterable<Usuario>>(lista,HttpStatus.OK);
	}
	
	@PreAuthorize("hasAuthority('ROLE_ADMINISTRADOR') and #oauth2.hasScope('read')")
	@GetMapping("/disponiveis")
	public ResponseEntity<?> listaUsuariosDisponiveis(){
		List<Usuario> lista = this.usuarioService.listaUsuariosDisponiveis();
		return new ResponseEntity<List<Usuario>>(lista,HttpStatus.OK);
	}
	
	@PreAuthorize("hasAuthority('ROLE_ADMINISTRADOR')")
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void excluir(@PathVariable("id") Long id){
		this.usuarioService.excluir(id);
	}
}
