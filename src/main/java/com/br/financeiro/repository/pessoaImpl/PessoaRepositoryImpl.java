package com.br.financeiro.repository.pessoaImpl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import com.br.financeiro.model.Pessoa;
import com.br.financeiro.model.Pessoa_;
import com.br.financeiro.model.filter.PessoaFilter;

public class PessoaRepositoryImpl implements PessoaRepositoryQuery{
	
	@PersistenceContext
	private EntityManager manager;

	@Override
	public Page<Pessoa> filtrar(PessoaFilter filtro, Pageable pageable) {
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Pessoa> criteria = builder.createQuery(Pessoa.class);
		Root<Pessoa> root = criteria.from(Pessoa.class);
		
		Predicate[] predicates = criarRestricoes(filtro, builder, root);
		criteria.where(predicates);
		TypedQuery<Pessoa> query = manager.createQuery(criteria);
		
		adicionarRestricoesDePaginacao(query, pageable);
		
		return new PageImpl<>(query.getResultList(), pageable, total(filtro));
	}

	private Predicate[] criarRestricoes(PessoaFilter filtro, CriteriaBuilder builder, Root<Pessoa> root) {
		List<Predicate> predicates = new ArrayList<>();
		
		if (!StringUtils.isEmpty(filtro.getNome())) {
			predicates.add(builder.like(
					builder.lower(root.get(Pessoa_.nome)),"%"+filtro.getNome().toLowerCase()+"%"));
		}
		
		if (!StringUtils.isEmpty(filtro.getCpf())) {
			predicates.add(builder.like((root.get(Pessoa_.cpf)),"%"+filtro.getCpf()+"%"));
		}
		
//		if (!StringUtils.isEmpty(filtro.getSituacao())) {
//			predicates.add(builder.equal((root.get(Pessoa_.situacao)),"%"+filtro.getSituacao()+"%"));
//		}
		
		return predicates.toArray(new Predicate[predicates.size()]);
	}
	
	private void adicionarRestricoesDePaginacao(TypedQuery<Pessoa> query, Pageable pageable) {
		int paginaAtual = pageable.getPageNumber();
		int totalRegistrosPorPagina = pageable.getPageSize();
		int primeiroResgistroDaPagina = paginaAtual * totalRegistrosPorPagina;
		
		query.setFirstResult(primeiroResgistroDaPagina);
		query.setMaxResults(totalRegistrosPorPagina);
	}
	
	private Long total(PessoaFilter filtro) {
		CriteriaBuilder builder= manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
		Root<Pessoa> root = criteria.from(Pessoa.class);
		
		Predicate[] predicates = criarRestricoes(filtro, builder, root);
		criteria.where(predicates);
		
		criteria.select(builder.count(root));
		return manager.createQuery(criteria).getSingleResult();
	}
}
