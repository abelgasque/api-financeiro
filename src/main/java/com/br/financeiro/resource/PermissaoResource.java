package com.br.financeiro.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.br.financeiro.model.Permissao;
import com.br.financeiro.repository.PermissaoRepository;

@RestController
@RequestMapping("/permissoes")
public class PermissaoResource {
	
	@Autowired
	private PermissaoRepository permissaoRepository;
	
	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_ADMINISTRADOR') and #oauth2.hasScope('read')")
	public ResponseEntity<?> listar(){
		Iterable<Permissao> lista = this.permissaoRepository.findAll();
		return new ResponseEntity<Iterable<Permissao>>(lista,HttpStatus.OK);
	}
}
