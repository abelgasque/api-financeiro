package com.br.financeiro.service;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.br.financeiro.mailer.Mailer;
import com.br.financeiro.model.Lancamento;
import com.br.financeiro.model.Pessoa;
import com.br.financeiro.model.TipoLancamento;
import com.br.financeiro.model.Usuario;
import com.br.financeiro.model.dto.LancamentoEstatisticaDia;
import com.br.financeiro.model.dto.LancamentoEstatisticaMes;
import com.br.financeiro.model.dto.LancamentoEstatisticaPessoa;
import com.br.financeiro.model.filter.LancamentoFilter;
import com.br.financeiro.repository.LancamentoRepository;
import com.br.financeiro.repository.PessoaRepository;
import com.br.financeiro.repository.UsuarioRepository;
import com.br.financeiro.service.exception.PessoaInexistenteOuInativaException;
import com.br.financeiro.storage.S3;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class LancamentoService {
	
	private static final String DESTINATARIOS = "ROLE_PESQUISAR_LANCAMENTO";
	
	private static final Logger logger = LoggerFactory.getLogger(LancamentoService.class);
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private Mailer mailer;
	
	@Autowired
	private S3 s3;
	
	@Scheduled(cron = "0 0 6 * * *")
	public void avisarSobreLancamentosVencidos() {
		if (logger.isDebugEnabled()) {
			logger.debug("Preparando envio de "
					+ "e-mails de aviso de lançamentos vencidos.");
		}
		
		List<Lancamento> vencidos = lancamentoRepository
				.findByDataVencimentoLessThanEqualAndDataPagamentoIsNull(LocalDate.now());
		
		if (vencidos.isEmpty()) {
			logger.info("Sem lançamentos vencidos para aviso.");
			
			return;
		}
		
		logger.info("Exitem {} lançamentos vencidos.", vencidos.size());
		
		List<Usuario> destinatarios = usuarioRepository
				.findByPermissoesDescricao(DESTINATARIOS);
		
		if (destinatarios.isEmpty()) {
			logger.warn("Existem lançamentos vencidos, mas o "
					+ "sistema não encontrou destinatários.");
			
			return;
		}
		
		mailer.avisarSobreLancamentosVencidos(vencidos, destinatarios);
		
		logger.info("Envio de e-mail de aviso concluído."); 
	}
	
	public byte[] relatorioPorPessoa(LocalDate inicio, LocalDate fim) throws JRException {
		List<LancamentoEstatisticaPessoa> dados = lancamentoRepository.porPessoa(inicio, fim);
		
		Map<String, Object> parametros = new HashMap<>();
		parametros.put("DT_INICIO", Date.valueOf(inicio));
		parametros.put("DT_FIM", Date.valueOf(fim));
		parametros.put("REPORT_LOCALE", new Locale("pt", "BR"));
		
		InputStream inputStream = this.getClass().getResourceAsStream(
				"/relatorios/lancamentos-por-pessoa.jasper");
		
		JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parametros,
				new JRBeanCollectionDataSource(dados));
		
		return JasperExportManager.exportReportToPdf(jasperPrint);
	}
	
	public List<LancamentoEstatisticaMes> estatisticasPorMes(int anoReferencia, Long idPessoa) {
		LocalDate mesReferencia = LocalDate.now();
		List<LancamentoEstatisticaMes> retorno = new ArrayList<LancamentoEstatisticaMes>();
		for(int i = 1; i<=12;i++) {
			mesReferencia = mesReferencia.withMonth(i);
			List<LancamentoEstatisticaDia> estatisticas = this.lancamentoRepository.porDia(mesReferencia, idPessoa);
			BigDecimal totalReceitas = new BigDecimal(0.0);
			BigDecimal totalDespesas = new BigDecimal(0.0);
			TipoLancamento tipo = null; 
			LancamentoEstatisticaMes estatisticaReceitasMes = new LancamentoEstatisticaMes(tipo.RECEITA, i, null);
			LancamentoEstatisticaMes estatisticaDespesasMes = new LancamentoEstatisticaMes(tipo.DESPESA, i, null);
			for(LancamentoEstatisticaDia estistica : estatisticas) {
				if(estistica.getTipo().toString() == "RECEITA" && estistica.getDia().getMonthValue() == i) {
					totalReceitas = totalReceitas.add(estistica.getTotal());
				}else if(estistica.getTipo().toString() == "DESPESA" && estistica.getDia().getMonthValue() == i){
					totalDespesas = totalDespesas.add(estistica.getTotal());
				}
			}
			estatisticaReceitasMes.setTotal(totalReceitas);
			estatisticaDespesasMes.setTotal(totalDespesas);
			retorno.add(estatisticaReceitasMes);
			retorno.add(estatisticaDespesasMes);
		}
		return retorno;
	}
	
	public Page<Lancamento> pesquisar(LancamentoFilter filtro, Pageable pageable){
		return this.lancamentoRepository.filtrar(filtro, pageable);
	}
	
	public Lancamento salvar(Lancamento entidade) {
	    validarPessoa(entidade);
		
	    if (StringUtils.hasText(entidade.getAnexo())) {
			s3.salvar(entidade.getAnexo());
		}
	    
	    return lancamentoRepository.save(entidade);
	}
	
	public Lancamento editar(Lancamento entidade) {
		Optional<Lancamento> entidadeSalva = buscarPorId(entidade.getId());
		if (!entidade.getPessoa().equals(entidadeSalva.get().getPessoa())) {
			validarPessoa(entidade);
		}
		
		if (StringUtils.isEmpty(entidade.getAnexo()) && StringUtils.hasText(entidadeSalva.get().getAnexo())) {
			s3.remover(entidadeSalva.get().getAnexo());
		} else if (StringUtils.hasText(entidade.getAnexo()) && !entidade.getAnexo().equals(entidadeSalva.get().getAnexo())) {
			s3.substituir(entidadeSalva.get().getAnexo(), entidade.getAnexo());
		}
		
		BeanUtils.copyProperties(entidade, entidadeSalva, "id");
		return lancamentoRepository.save(entidade);
	}

	public Optional<Lancamento> buscarPorId(Long id) {
		return this.lancamentoRepository.findById(id);
	}
	
	public void excluir(Long id) {
		this.lancamentoRepository.deleteById(id);
	}
	
	public Iterable<Lancamento> listar() {
		return lancamentoRepository.findAll();
	}
	
	private void validarPessoa(Lancamento lancamento) {
		Optional<Pessoa> pessoa = null;
		if (lancamento.getPessoa().getId() != null) {
			pessoa = pessoaRepository.findById(lancamento.getPessoa().getId());
		}

		if (pessoa == null || pessoa.get().getSituacao().toString() == "INATIVO") {
			throw new PessoaInexistenteOuInativaException();
		}
	}

}
