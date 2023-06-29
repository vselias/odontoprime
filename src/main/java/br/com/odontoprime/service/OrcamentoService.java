package br.com.odontoprime.service;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.inject.Inject;

import br.com.odontoprime.dao.OrcamentoDAO;
import br.com.odontoprime.dao.PacienteDAO;
import br.com.odontoprime.dao.UsuarioDAO;
import br.com.odontoprime.entidade.StatusCadastro;
import br.com.odontoprime.entidade.Orcamento;
import br.com.odontoprime.entidade.Paciente;
import br.com.odontoprime.entidade.Usuario;
import br.com.odontoprime.util.FacesUtil;
import br.com.odontoprime.util.MensagemUtil;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.draw.LineSeparator;

@SuppressWarnings("serial")
public class OrcamentoService implements Serializable {

	@Inject
	private OrcamentoDAO orcamentoDAO;
	@Inject
	private PacienteDAO pacienteDAO;
	@Inject
	private UsuarioDAO usuarioDAO;

	public void salvar(Orcamento orcamento) {
		try {
			// if (validacaoCadastroOrcamentoServer(orcamento)) {

			Paciente paciente = pacienteDAO.buscarPorId(orcamento.getPaciente().getId(), Paciente.class);
			orcamento.setPaciente(paciente);
			orcamento.setData(new Date());

			Usuario usuarioLogado = (Usuario) FacesUtil.getSessionAttribute("usuario");
			usuarioLogado = usuarioDAO.buscarPorId(usuarioLogado.getId(), Usuario.class);
			orcamento.setUsuario(usuarioLogado);
			orcamento.setDataCadastro(new Date());
			if (!isNovoOrcamento(orcamento))
				orcamento.setStatusCadastro(StatusCadastro.ATUALIZACAO);
			else
				orcamento.setStatusCadastro(StatusCadastro.CADASTRO);

			orcamentoDAO.salvar(orcamento);
			if (orcamento.getStatusCadastro().equals(StatusCadastro.CADASTRO))
				MensagemUtil.enviarMensagem("Orçamento salvo com sucesso!", FacesMessage.SEVERITY_INFO);
			else
				MensagemUtil.enviarMensagem("Orçamento atualizado com sucesso!", FacesMessage.SEVERITY_INFO);
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void remover(Orcamento orcamento) {
		try {
			if (!isNovoOrcamento(orcamento)) {
				orcamentoDAO.remover(orcamentoDAO.getReference(Orcamento.class, orcamento.getId()));
				MensagemUtil.enviarMensagem("Orçamento removido com sucesso.", FacesMessage.SEVERITY_INFO);
			} else {
				MensagemUtil.enviarMensagem("Não é possível remover orçamento não cadastrado no sistema.",
						FacesMessage.SEVERITY_ERROR);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Orcamento buscarPorId(Long id) {
		return orcamentoDAO.buscarPorId(id, Orcamento.class);
	}

	public List<Orcamento> buscarTodos() {
		return orcamentoDAO.buscarTodos(Orcamento.class);
	}

	public void gerarPDF(Object document) {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		try {
			Document pdf = (Document) document;
			pdf.addCreationDate();
			pdf.setPageSize(PageSize.A4);
			pdf.open();
			String dataAtual = format.format(new Date().getTime());
			Paragraph paragrafo = new Paragraph();
			Image image = Image.getInstance(this.getClass().getResource("/img/logo-login.png"));
			image.scaleToFit(80, 40);
			image.setAlignment(Element.ALIGN_LEFT);
			paragrafo.add(image);
			paragrafo.setAlignment(Element.ALIGN_RIGHT);
			paragrafo.setSpacingAfter(10);
			paragrafo.add(dataAtual);
			pdf.add(paragrafo);
			LineSeparator lineSeparator = new LineSeparator();
			pdf.add(lineSeparator);
			Paragraph paragraph = new Paragraph("Relatório de Orçamentos");
			paragraph.setAlignment(Element.ALIGN_CENTER);
			paragraph.setSpacingAfter(10);
			pdf.add(paragraph);
		} catch (DocumentException | IOException e) {
			e.printStackTrace();
		}

	}

	public boolean validacaoCadastroOrcamentoServer(Orcamento orcamento) {
		if (orcamento != null && orcamento.getPaciente() != null && orcamento.getPaciente().getId() != null
				&& orcamento.getPaciente().getId() > 0 && orcamento.getValor() != null && orcamento.getValor() > 0) {
			return Boolean.TRUE;
		}
		MensagemUtil.enviarMensagem("Os valores informados não são compatíveis. Preencha novamente corretamente.",
				FacesMessage.SEVERITY_ERROR);
		return Boolean.FALSE;
	}

	public boolean isNovoOrcamento(Orcamento orcamento) {
		if (orcamento != null)
			if (orcamento.getId() == null || orcamento.getId() == 0)
				return true;

		return false;

	}
}
