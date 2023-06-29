package br.com.odontoprime.bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.ViewScoped;

import br.com.odontoprime.entidade.Consulta;
import br.com.odontoprime.entidade.Entrada;
import br.com.odontoprime.entidade.FormaPagamento;
import br.com.odontoprime.entidade.MovimentacaoCaixa;
import br.com.odontoprime.entidade.Parcela;
import br.com.odontoprime.entidade.Saida;
import br.com.odontoprime.entidade.TipoMovimentacao;
import br.com.odontoprime.entidade.Usuario;
import br.com.odontoprime.service.ConsultaService;
import br.com.odontoprime.service.EntradaService;
import br.com.odontoprime.service.MovimentacaoCaixaService;
import br.com.odontoprime.service.SaidaService;
import br.com.odontoprime.util.FacesUtil;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class MovimentacaoCaixaMB implements Serializable {

	@Inject
	private MovimentacaoCaixaService movimentacaoCaixaService;
	@Inject
	private ConsultaService consultaService;
	@Inject
	private EntradaService entradaService;
	@Inject
	private SaidaService saidaService;
	private MovimentacaoCaixa movimentacaoCaixa;
	private List<MovimentacaoCaixa> movimentacaoCaixas;
	private boolean exibirValorTotal;
	private List<TipoMovimentacao> tiposMovimentacao;
	private List<Consulta> consultasFechamento;
	private Date dataFechamento;
	private List<Saida> saidasFechamento;
	private Saida selecaoSaidaFechamento;
	private Entrada entradaVO;
	private String dataAtual;
	private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

	public Entrada getEntradaVO() {
		return entradaVO;
	}

	public void setDataAtual(String dataAtual) {
		this.dataAtual = dataAtual;
	}

	public void setEntradaVO(Entrada entradaVO) {
		this.entradaVO = entradaVO;
	}

	public Saida getSelecaoSaidaFechamento() {
		return selecaoSaidaFechamento;
	}

	public void setSelecaoSaidaFechamento(Saida selecaoSaidaFechamento) {
		this.selecaoSaidaFechamento = selecaoSaidaFechamento;
	}

	public List<Saida> getSaidasFechamento() {
		return saidasFechamento;
	}

	public void setSaidasFechamento(List<Saida> saidasFechamento) {
		this.saidasFechamento = saidasFechamento;
	}

	public Date getDataFechamento() {
		return dataFechamento;
	}

	public void setDataFechamento(Date dataFechamento) {
		this.dataFechamento = dataFechamento;
	}

	public List<Consulta> getConsultasFechamento() {
		return consultasFechamento;
	}

	public void setConsultasFechamento(List<Consulta> consultasFechamento) {
		this.consultasFechamento = consultasFechamento;
	}

	public MovimentacaoCaixaService getMovimentacaoCaixaService() {
		return movimentacaoCaixaService;
	}

	public void setMovimentacaoCaixaService(MovimentacaoCaixaService movimentacaoCaixaService) {
		this.movimentacaoCaixaService = movimentacaoCaixaService;
	}

	public MovimentacaoCaixa getMovimentacaoCaixa() {
		if (movimentacaoCaixa == null) {
			recuperarMovimentacaoEdicao();
		}
		return movimentacaoCaixa;
	}

	public boolean isExibirValorTotal() {
		return exibirValorTotal;
	}

	public void setMovimentacaoCaixa(MovimentacaoCaixa movimentacaoCaixa) {
		this.movimentacaoCaixa = movimentacaoCaixa;
	}

	public List<MovimentacaoCaixa> getMovimentacaoCaixas() {
		return movimentacaoCaixas;
	}

	public List<TipoMovimentacao> getTiposMovimentacao() {
		return tiposMovimentacao;
	}

	@PostConstruct
	public void init() {

		entradaVO = new Entrada();
		dataFechamento = new Date();
		movimentacaoCaixas = movimentacaoCaixaService.buscarTodos();
		tiposMovimentacao = Arrays.asList(TipoMovimentacao.values());
		exibirValorTotal = Boolean.FALSE;
		consultasFechamento = new ArrayList<>();
		recuperarMovimentacaoEdicao();
		saidasFechamento = saidaService.buscarSaidasFechamento(movimentacaoCaixa.getData());
		injetarUsuarioLogado();

	}

	public void adicionarEntradaMovimentacao() {
		movimentacaoCaixa.setEntrada(entradaService.buscarTotalEntradaFechamento(movimentacaoCaixa.getData()));
	}

	public void adicionarValorInicialMovimentacao() {
		movimentacaoCaixa
				.setValorInicial(movimentacaoCaixaService.buscarValorInicialFechamento(movimentacaoCaixa.getData()));
	}

	public void adicionarValorSaidaMovimentacao() {
		movimentacaoCaixa.setSaida(saidaService.buscarSaidaFechamento(movimentacaoCaixa.getData()));
	}

	public void recuperarMovimentacaoEdicao() {
		movimentacaoCaixa = (MovimentacaoCaixa) FacesContext.getCurrentInstance().getExternalContext().getFlash()
				.get("movimentacao");

		// não existe movimentacao para editar
		if (movimentacaoCaixa == null) {

			// buscar movimentacao do tipo abertura
			movimentacaoCaixa = movimentacaoCaixaService.buscarMovimentacaoAbertura();

			if (movimentacaoCaixa == null)
				movimentacaoCaixa = new MovimentacaoCaixa();

			adicionarEntradaMovimentacao();
			adicionarValorInicialMovimentacao();
			adicionarValorSaidaMovimentacao();
			calcularValorTotal();
		} else {

			// atualiza os valores referente a antiga movimentacao
			adicionarEntradaMovimentacao();
			adicionarValorInicialMovimentacao();
			adicionarValorSaidaMovimentacao();
			calcularValorTotal();
		}
	}

	public void salvar() {
		movimentacaoCaixaService.salvar(movimentacaoCaixa);
		recuperarMovimentacaoEdicao();
	}

	public void buscarConsultasFechamento() {
		consultasFechamento = consultaService.buscarConsultasFechamento(movimentacaoCaixa.getData());
	}

	public void injetarUsuarioLogado() {
		Usuario usuarioLogado = (Usuario) FacesUtil.getSessionAttribute("usuario");
		movimentacaoCaixa.setUsuario(usuarioLogado);
	}

	public String editarMovimentacao(MovimentacaoCaixa movimentacaoCaixa) {

		FacesContext.getCurrentInstance().getExternalContext().getFlash().put("movimentacao", movimentacaoCaixa);
		return "FechamentoCaixa?faces-redirect=true";
	}

	public void buscarEntradaComParcela(Entrada entrada) {
		entradaVO = entradaService.buscarEntradaComParcelas(entrada.getId());
	}

	public void calcularValorTotal() {
		if (movimentacaoCaixa != null) {
			movimentacaoCaixa.setValorTotal((movimentacaoCaixa.getValorInicial() + movimentacaoCaixa.getEntrada())
					- movimentacaoCaixa.getSaida());
		}
	}

	public boolean isParcelaPaga(Parcela parcela) {

		if (parcela != null && parcela.getDataPagamento() != null) {

			dataAtual = format.format(Calendar.getInstance().getTime());
			String dataPagamento = format.format(parcela.getDataPagamento().getTime());

			if (dataAtual.compareTo(dataPagamento) == 0)
				return Boolean.TRUE;
		}

		return Boolean.FALSE;

	}

	public boolean parcelaPaga(Parcela parcela) {

		if (parcela != null) {

			if (parcela.getEstadoPagamento().getDescricao().equalsIgnoreCase("pago")) {
				return true;
			}
		}
		return false;
	}

}
