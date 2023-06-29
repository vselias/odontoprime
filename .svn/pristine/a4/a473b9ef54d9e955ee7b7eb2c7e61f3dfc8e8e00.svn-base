package br.com.odontoprime.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import br.com.odontoprime.dao.MovimentacaoCaixaDAO;
import br.com.odontoprime.dao.SaidaDAO;
import br.com.odontoprime.dao.UsuarioDAO;
import br.com.odontoprime.entidade.StatusCadastro;
import br.com.odontoprime.entidade.Saida;
import br.com.odontoprime.entidade.Usuario;
import br.com.odontoprime.util.FacesUtil;
import br.com.odontoprime.util.MensagemUtil;

@SuppressWarnings("serial")
public class SaidaService implements Serializable {
	@Inject
	private SaidaDAO saidaDAO;
	@Inject
	private UsuarioDAO usuarioDAO;
	@Inject
	private MovimentacaoCaixaDAO movimentacaoCaixaDAO;

	public void salvar(Saida saida) {
		try {
			Usuario usuario = (Usuario) FacesUtil.getSessionAttribute("usuario");
			usuario = usuarioDAO.buscarPorId(usuario.getId(), Usuario.class);

			if (!isCaixaFechado(saida.getDataSaida())) {

				if (saida.novaSaida()) {
					if (usuario != null)
						saida.setUsuario(usuario);
					saida.setStatusCadastro(StatusCadastro.CADASTRO);
					saida.setDataCadastro(new Date());
					saidaDAO.salvar(saida);
					MensagemUtil.enviarMensagem("Saída cadastrada com sucesso!", FacesMessage.SEVERITY_INFO);
				} else {
					saida.setUsuario(usuario);
					saida.setDataCadastro(new Date());
					saida.setStatusCadastro(StatusCadastro.ATUALIZACAO);
					saidaDAO.salvar(saida);
					MensagemUtil.enviarMensagem("Saída atualizada com sucesso!", FacesMessage.SEVERITY_INFO);
				}
			} else {
				MensagemUtil.enviarMensagem("Não pode realizar saída com caixa fechado!", FacesMessage.SEVERITY_WARN);
			}
		} catch (Exception e) {
			MensagemUtil.enviarMensagem(
					"Erro ao lançar saída. Contate o administrador do sistema ou tente novamente mais tarde.",
					FacesMessage.SEVERITY_ERROR);
			e.printStackTrace();
		}
	}

	public List<Saida> buscarTodas() {
		try {
			return saidaDAO.buscarTodos(Saida.class);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public Double buscarSaidaFechamento(Date dataMovimentacao) {
		if (dataMovimentacao == null)
			dataMovimentacao = new Date();

		return saidaDAO.buscarSaidaFechamento(dataMovimentacao);
	}

	public List<Saida> buscarSaidasFechamento(Date dataSaida) {
		return saidaDAO.buscarSaidasFechamento(dataSaida);
	}

	public double buscarValorTotalUltimaSaida(Date data) {
		return saidaDAO.buscarValorTotalUltimaSaida(data);
	}

	public void remover(Saida saida) {
		try {
			saidaDAO.remover(saidaDAO.getReference(Saida.class, saida.getId()));
			MensagemUtil.enviarMensagem("Saída removida com sucesso!", FacesMessage.SEVERITY_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			MensagemUtil.enviarMensagem("Erro ao remover saída.", FacesMessage.SEVERITY_ERROR);
		}
	}

	public boolean isCaixaFechado(Date dataSaida) {
		return movimentacaoCaixaDAO.existeFechamentoPorData(dataSaida);
	}
}
