package br.com.odontoprime.service;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.inject.Inject;

import br.com.odontoprime.dao.EntradaDAO;
import br.com.odontoprime.entidade.Entrada;
import br.com.odontoprime.util.MensagemUtil;

@SuppressWarnings("serial")
public class EntradaService implements Serializable {
	@Inject
	public EntradaDAO entradaDAO;

	public void salvar(Entrada entrada) {
		try {
			if (entrada.getId() == null) {
				entradaDAO.salvar(entrada);
			} else {
				entradaDAO.atualizar(entrada);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void remover(Entrada entrada) {
		entradaDAO.remover(entrada);
	}

	public Entrada pesquisarPorId(Entrada entrada) {
		return entradaDAO.buscarPorId(entrada.getId(), Entrada.class);
	}

	public List<Entrada> pesquisarTodos() {
		return entradaDAO.buscarTodos(Entrada.class);
	}

	public List<Entrada> buscarEntrada() {

		return entradaDAO.buscarTodasEntradas();
	}

	public List<Entrada> buscarEntradaPorData(Date data) {
		List<Entrada> lista = null;
		if (data == null) {
			return entradaDAO.buscarTodasEntradas();
		} else {
			lista = entradaDAO.buscarEntradaPorData(data);
			System.out.println("Data = " + data.getTime());
		}
		if (lista == null || lista.isEmpty()) {
			MensagemUtil.enviarMensagem("Nenhum lançamento para essa data!", FacesMessage.SEVERITY_INFO);
		}
		return lista;
	}

	public String buscarValorTotal() {
		DecimalFormat decimalFormat = new DecimalFormat("R$ #,##0.00");
		Double valorTotal = entradaDAO.buscarValorTotalEntrada();
		if (valorTotal == null || valorTotal == 0) {
			return null;
		}

		return decimalFormat.format(valorTotal);

	}

	public Double buscarTotalEntradaFechamento(Date dataMovimentacao) {
		if (dataMovimentacao == null) {
			dataMovimentacao = new Date();
		}
		return entradaDAO.buscarValorTotalEntradaFechamento(dataMovimentacao);
	}

	public double buscarValorTotalUltimaEntrada(Date date) {
		return entradaDAO.buscarValorTotalUltimaEntrada(date);
	}

	public Entrada buscarEntradaComParcelas(Long entradaId) {
		return entradaDAO.buscarEntradaComParcelas(entradaId);
	}
}
