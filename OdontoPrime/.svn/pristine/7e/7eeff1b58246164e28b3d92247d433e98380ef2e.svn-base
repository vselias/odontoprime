package br.com.odontoprime.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.ViewScoped;

import br.com.odontoprime.entidade.Saida;
import br.com.odontoprime.service.SaidaService;

@Named
@ViewScoped
public class SaidaMB implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2468251531874081316L;
	private Saida saida;
	private List<Saida> saidas;

	@Inject
	private SaidaService saidaService;
	private Date dataPesquisa;
	private List<Saida> saidasFiltradas;

	public List<Saida> getSaidasFiltradas() {
		return saidasFiltradas;
	}

	public void setSaidasFiltradas(List<Saida> saidasFiltradas) {
		this.saidasFiltradas = saidasFiltradas;
	}

	public Date getDataPesquisa() {
		return dataPesquisa;
	}

	public void setDataPesquisa(Date dataPesquisa) {
		this.dataPesquisa = dataPesquisa;
	}

	public Saida getSaida() {
		if (saida == null) {
			saida = new Saida();
		}
		return saida;
	}

	public void setSaida(Saida saida) {
		this.saida = saida;
	}

	public List<Saida> getSaidas() {
		return saidas;
	}

	public void setSaidas(List<Saida> saidas) {
		this.saidas = saidas;
	}

	public void remover() {
		saidaService.remover(saida);
		recarregarLista();
	}

	@PostConstruct
	public void initi() {
		saida = new Saida();
		saidas = saidaService.buscarTodas();
	}

	public void recarregarLista() {
		saidas = saidaService.buscarTodas();
	}

	public void salvar() {
		saidaService.salvar(saida);
		recarregarLista();
		limparDados();
	}

	public void limparDados() {
		saida = new Saida();
	}
}
