package br.com.odontoprime.service;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.persistence.Transient;

import br.com.odontoprime.dao.EntradaDAO;
import br.com.odontoprime.entidade.Entrada;
import br.com.odontoprime.entidade.EstadoPagamento;
import br.com.odontoprime.entidade.Parcela;
import br.com.odontoprime.util.MensagemUtil;

@SuppressWarnings("serial")
public class ParcelaService implements Serializable {
	@Inject
	private ParcelaDAO parcelaDAO;
	@Inject
	private EntradaDAO entradaDAO;
	@Inject
	private MovimentacaoCaixaService movimentacaoCaixaService;
	private SimpleDateFormat formatData = new SimpleDateFormat("dd/MM/yyyy");

	public void efetuarPagamentoParcela(Parcela parcela, Entrada entrada) {

		if (parcelaNotNull(parcela) && verificarDataPagamento(parcela)) {

			try {
				if (!movimentacaoCaixaService.existeFechamentoPorData(parcela.getDataPagamento())) {
					parcela.setEstadoPagamento(EstadoPagamento.PAGO);
					parcelaDAO.efetuarPagamentoParcela(parcela);
					atualizarEstadoPagamentoEntrada(entrada);
					MensagemUtil.enviarMensagem("Pagamento efetuado com sucesso!", FacesMessage.SEVERITY_INFO);

				} else {
					MensagemUtil.enviarMensagem(
							"Não é possível pagar parcela com caixa fechado referente a data "
									+ formatData.format(parcela.getDataPagamento().getTime()),
							FacesMessage.SEVERITY_WARN);
					parcela.setDataPagamento(null);
				}
			} catch (Exception e) {
				MensagemUtil.enviarMensagem(
						"Erro ao efetuar pagamento da parcela. Tente novamente mais tarde ou contate o administrador!",
						FacesMessage.SEVERITY_ERROR);
				parcela.setDataPagamento(null);
				e.printStackTrace();
			}
		} else {
			/*
			 * Caso pagamento der errado, remova a data do pagamento para não
			 * exibi-la na tabela
			 */
			parcela.setDataPagamento(null);
		}
	}

	public void atualizarEstadoPagamentoEntrada(Entrada entrada) {
		if (isPagamentoConcluido(entrada)) {

			entrada.setEstadoPagamento(EstadoPagamento.PAGO);
		} else {
			entrada.setEstadoPagamento(EstadoPagamento.PENDENTE);
		}

		entradaDAO.atualizar(entrada);
	}

	@Transient
	public boolean verificarDataPagamento(Parcela parcela) {

		if (parcela.getDataPagamento() == null) {
			MensagemUtil.enviarMensagem("Data do pagamento deve ser informada.", FacesMessage.SEVERITY_ERROR);
			return Boolean.FALSE;
		}

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String txtDataPagamento = sdf.format(parcela.getDataPagamento());
		if (txtDataPagamento.compareTo(sdf.format(Calendar.getInstance().getTime())) < 0) {
			MensagemUtil.enviarMensagem("É necessário que seja informada uma data igual ou superior atual "
					+ sdf.format(Calendar.getInstance().getTime()) + ".", FacesMessage.SEVERITY_ERROR);
			return false;
		}
		return true;
	}

	@Transient
	public boolean parcelaEstaPaga(Parcela parcela) {
		if (parcelaNotNull(parcela) && parcela.getEstadoPagamento().equals(EstadoPagamento.PAGO)
				&& parcela.getDataPagamento() != null) {
			MensagemUtil.enviarMensagem("Parcela já paga.", FacesMessage.SEVERITY_WARN);
			return true;
		}
		return false;
	}

	public boolean existeParcelaPaga(List<Parcela> parcelas) {

		for (Parcela parcela : parcelas) {
			if (parcela.getEstadoPagamento().getDescricao().equalsIgnoreCase("pago")) {
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}

	@Transient
	public boolean parcelaNotNull(Parcela parcela) {
		return parcela != null;
	}

	@Transient
	public boolean isPagamentoConcluido(Entrada entrada) {

		for (Parcela p : entrada.getParcelas()) {
			if (p.getEstadoPagamento().equals(EstadoPagamento.PENDENTE)) {
				return Boolean.FALSE;

			}

		}
		return Boolean.TRUE;
	}

	public void cancelarPagamento(Parcela parcela) {
		try {

			if (!movimentacaoCaixaService.existeFechamentoPorData(parcela.getDataPagamento())) {

				parcela.setDataPagamento(null);
				parcela.setEstadoPagamento(EstadoPagamento.PENDENTE);
				parcelaDAO.salvar(parcela);
				MensagemUtil.enviarMensagem("Pagamento cancelado com sucesso", FacesMessage.SEVERITY_INFO);
			} else {

				MensagemUtil.enviarMensagem("Não é possível cancelar pagamento com caixa fechado referente a data "
						+ formatData.format(parcela.getDataPagamento().getTime()), FacesMessage.SEVERITY_WARN);
			}

		} catch (Exception e) {
			MensagemUtil.enviarMensagem(
					"Erro ao efetuar pagamento! Tente novamente mais tarde ou contate o admnistrador do sistema.",
					FacesMessage.SEVERITY_INFO);
			e.printStackTrace();
		}

	}

	@Transient
	public void limparDataPagamentoParcela(Parcela parcela) {
		if (parcelaNotNull(parcela)) {
			parcela.setDataPagamento(null);
		}
	}
}
