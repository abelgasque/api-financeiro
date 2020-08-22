package com.br.financeiro.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.br.financeiro.model.Lancamento;
import com.br.financeiro.repository.lancamentoImpl.LancamentoRepositoryQuery;

public interface LancamentoRepository extends CrudRepository<Lancamento, Long>, LancamentoRepositoryQuery{

	List<Lancamento> findByDataVencimentoLessThanEqualAndDataPagamentoIsNull(LocalDate data);
}
