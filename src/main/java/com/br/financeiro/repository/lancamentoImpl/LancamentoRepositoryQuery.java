package com.br.financeiro.repository.lancamentoImpl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.br.financeiro.model.Lancamento;
import com.br.financeiro.model.dto.LancamentoEstatisticaCategoria;
import com.br.financeiro.model.dto.LancamentoEstatisticaDia;
import com.br.financeiro.model.dto.LancamentoEstatisticaPessoa;
import com.br.financeiro.model.filter.LancamentoFilter;
import com.br.financeiro.model.projection.ResumoLancamento;

public interface LancamentoRepositoryQuery {
	
	public List<LancamentoEstatisticaPessoa> porPessoa(LocalDate inicio, LocalDate fim);
	public List<LancamentoEstatisticaCategoria> porCategoria(LocalDate mesReferencia);
	public List<LancamentoEstatisticaDia> porDia(LocalDate mesReferencia);
	public Page<Lancamento> filtrar(LancamentoFilter lancamentoFilter, Pageable pageable);
	public Page<ResumoLancamento> resumir(LancamentoFilter lancamentoFilter, Pageable pageable);
}
