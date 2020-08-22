package com.br.financeiro.repository.listener;

import javax.persistence.PostLoad;

import org.springframework.util.StringUtils;

import com.br.financeiro.FinanceiroApiApplication;
import com.br.financeiro.model.Lancamento;
import com.br.financeiro.storage.S3;

public class LancamentoAnexoListener {
	
	@PostLoad
	public void postLoad(Lancamento lancamento) {
		if (StringUtils.hasText(lancamento.getAnexo())) {
			S3 s3 = FinanceiroApiApplication.getBean(S3.class);
			lancamento.setUrlAnexo(s3.configurarUrl(lancamento.getAnexo()));
		}
	}

}
