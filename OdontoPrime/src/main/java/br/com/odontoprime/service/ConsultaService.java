package br.com.odontoprime.service;

import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
import javax.inject.Inject;
import javax.persistence.PersistenceException;

import org.hibernate.exception.ConstraintViolationException;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

import br.com.odontoprime.dao.ConsultaDAO;
import br.com.odontoprime.dao.EntradaDAO;
import br.com.odontoprime.dao.MovimentacaoCaixaDAO;
import br.com.odontoprime.dao.PacienteDAO;
import br.com.odontoprime.entidade.Consulta;
import br.com.odontoprime.entidade.Entrada;
import br.com.odontoprime.entidade.EstadoConsulta;
import br.com.odontoprime.entidade.EstadoPagamento;
import br.com.odontoprime.entidade.StatusCadastro;
import br.com.odontoprime.entidade.Paciente;
import br.com.odontoprime.entidade.Parcela;
import br.com.odontoprime.entidade.Usuario;
import br.com.odontoprime.util.FacesUtil;
import br.com.odontoprime.util.JsfUtil;
import br.com.odontoprime.util.MensagemUtil;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.draw.LineSeparator;

public class ConsultaService implements Serializable {

	private static final long serialVersionUID = -4575922553148091096L;

	@Inject
	private ConsultaDAO consultaDAO;
	@Inject
	private PacienteDAO pacienteDAO;
	@Inject
	private MovimentacaoCaixaDAO movimentacaoCaixaDAO;
	@Inject
	private EntradaDAO entradaDAO;
	@Inject
	private ParcelaDAO parcelaDAO;
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
	@Inject
	private ParcelaService parcelaService;

	public Consulta buscarPorId(Long id) {
		return consultaDAO.buscarPorId(id, Consulta.class);
	}

	public List<Consulta> vendasPorDia() {
		return consultaDAO.buscarVendasPorDia();
	}

	public void atualizar(Consulta consulta) {
		try {
			consultaDAO.atualizar(consulta);
			MensagemUtil.enviarMensagem("Consulta atualizada com sucesso!", FacesMessage.SEVERITY_INFO);
		} catch (Exception e) {
			MensagemUtil.enviarMensagem(
					"Erro ao atualizar consulta. Tente novamente mais tarde ou contate o administrador do sistema!",
					FacesMessage.SEVERITY_INFO);
		}
	}

	public void remover(Consulta consulta) {
		try {

			if (!movimentacaoCaixaDAO.existeFechamentoPorData(consulta.getEntrada().getDataLancamento())) {
				consultaDAO.remover(consultaDAO.getReference(Consulta.class, consulta.getId()));
				MensagemUtil.enviarMensagem("Consulta removida com sucesso!", FacesMessage.SEVERITY_INFO);
				return;
			}

			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			String data = sdf.format(consulta.getEntrada().getDataLancamento().getTime());
			MensagemUtil.enviarMensagem("Não é possível remover consulta com caixa fechado para data: " + data,
					FacesMessage.SEVERITY_WARN);
		} catch (Exception e) {
			e.printStackTrace();
			MensagemUtil.enviarMensagem(
					"Erro ao remover consulta.Contate o administrador ou tente novamente mais tarde!",
					FacesMessage.SEVERITY_ERROR);
		}
	}

	public List<Consulta> buscarTodos() {
		return consultaDAO.buscarTodos(Consulta.class);
	}

	public Double buscarVendasPorData(Date dataInicio, Date dataFinal) {

		String inicio = simpleDateFormat.format(dataInicio.getTime());
		String dtFinal = simpleDateFormat.format(dataFinal.getTime());
		System.out.println("Inicio : " + inicio + " Final: " + dtFinal);
		return consultaDAO.valorTotalVendaPorData(dataInicio, dataFinal);
	}

	public List<Consulta> buscarConsultasPorPaciente() {
		return consultaDAO.buscarConsultasPorPaciente();
	}

	public List<Object[]> vendasPorAnoGrafico(int ano) {
		return consultaDAO.vendasPorAno(ano);
	}

	public boolean efetuarConsulta(Consulta consulta) {
		boolean consultaEfetuada = Boolean.FALSE;
		try {

			/*
			 * buscando instancia atualizada com as consultas referente ao
			 * paciente resolvendo problema do lazyLoading
			 */
			Paciente paciente = pacienteDAO.buscarPacienteComConsultas(consulta.getPaciente().getId());

			Date dataVencimento = consulta.getEntrada().getDataVencimento();

			if (!fluxoCaixaFechado(consulta.getEntrada().getDataLancamento()) && consultaPossuiPaciente(consulta)
					&& !existeHorarioConsulta(consulta) && validarEstadoNovaConsulta(consulta)
					&& validarCalculoDesconto(consulta) && validarDataPagamento(dataVencimento)) {

				// registrar estatus do cadastro
				registrarEstatusCadastroConsulta(consulta);

				// remover referencia antiga caso atualizar
				// consulta

				// metodo para parcelar divida
				if (!existeConsultaPaga(consulta)) {
					removerReferenciaParcela(consulta);
					parcelarConsulta(consulta);
				}

				registrarUsuarioLogado(consulta);

				paciente.getConsultas().add(consulta);
				consulta.setPaciente(paciente);

				if (consultaEditavel(consulta))
					pacienteDAO.atualizar(paciente);
				else
					pacienteDAO.salvar(paciente);

				consultaEfetuada = Boolean.TRUE;

				enviarMensagemCadastroConsulta(consulta, consultaEfetuada);
			}

		} catch (PersistenceException e) {
			Throwable t = e.getCause();
			while ((t != null) && !(t instanceof ConstraintViolationException)) {
				t = t.getCause();
			}
			if (t instanceof ConstraintViolationException) {
				String msg = t.getCause().getMessage();
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				if (msg.contains(sdf.format(consulta.getDataConsulta()))) {
					MensagemUtil.enviarMensagem("Esse horário ja foi definido. Escolha novo horário!",
							FacesMessage.SEVERITY_WARN);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			MensagemUtil.enviarMensagem("Erro ao efetuar consulta! Contate o administrador. " + e.getMessage(),
					FacesMessage.SEVERITY_ERROR);
		}
		return consultaEfetuada;

	}

	private boolean fluxoCaixaFechado(Date dataLancamento) {

		if (dataLancamento == null)
			dataLancamento = new Date();

		if (movimentacaoCaixaDAO.existeFechamentoPorData(dataLancamento)) {

			MensagemUtil.enviarMensagem("Não pode cadastrar ou atualizar consultas com caixa fechado na data: "
					+ simpleDateFormat.format(dataLancamento.getTime()), FacesMessage.SEVERITY_WARN);

			return true;
		}

		return false;
	}

	public boolean isConsultaAusente(Consulta consulta) {
		return consulta.getEstadoConsulta().equals(EstadoConsulta.AUSENTE);
	}

	public boolean isConsultaCancelada(Consulta consulta) {
		return consulta.getEstadoConsulta().equals(EstadoConsulta.CANCELADO);
	}

	public void enviarMensagemCadastroConsulta(Consulta consulta, boolean consultaEfetuada) {

		if (consultaEfetuada && consulta.getStatusCadastro().equals(StatusCadastro.CADASTRO)) {
			MensagemUtil.enviarMensagem("Consulta cadastrada com sucesso!", FacesMessage.SEVERITY_INFO);
		} else if (consultaEfetuada && consulta.getStatusCadastro().equals(StatusCadastro.ATUALIZACAO)) {
			MensagemUtil.enviarMensagem("Consulta atualizada com sucesso!", FacesMessage.SEVERITY_INFO);
		}
	}

	public boolean existeHorarioConsulta(Consulta consulta) throws Exception {
		List<Consulta> consultas = null;

		if (novaConsulta(consulta) && consultaPossuiData(consulta)) {

			if (consultaEditavel(consulta))
				consultas = consultaDAO.buscarConsultasPorData(consulta.getDataConsulta(), consulta.getId());
			else {
				consultas = consultaDAO.buscarConsultasPorData(consulta.getDataConsulta());
			}

			if (consultas != null && !consultas.isEmpty()) {

				if (consultas.size() > 0) {
					MensagemUtil.enviarMensagem("Esse horário ja foi definido. Escolha novo horário!",
							FacesMessage.SEVERITY_WARN);
					return true;
				}
			}
		}
		return false;
	}

	public boolean validarDataPagamento(Date dataVencimento) {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		Date dataAtual = new Date();
		boolean flagDataVencimento = Boolean.FALSE;
		String txtDataAtual = format.format(dataAtual);
		String txtDataVencimento = format.format(dataVencimento);

		if (txtDataVencimento.compareTo(txtDataAtual) < 0) {
			MensagemUtil.enviarMensagem("Data vencimento não pode ser anterior atual", FacesMessage.SEVERITY_WARN);
			flagDataVencimento = Boolean.FALSE;
		} else {
			flagDataVencimento = Boolean.TRUE;
		}

		if (flagDataVencimento) {
			return Boolean.TRUE;
		}

		return Boolean.FALSE;
	}

	public void parcelarConsulta(Consulta consulta) {
		// capturamos dados da tela
		Entrada dadosEntrada = consulta.getEntrada();
		// se a consulta for parcela dividir as entradas para
		// adicionar
		// na lista
		int numParcela = 0;

		double valorParcela = 0;

		Entrada entradaVO = null;

		if (consulta.getEntrada().getParcelado()) {
			String[] dadosParcela = consulta.getEntrada().getParcela().split("X");

			// capturar quantidade de parcela e alterar o formato dos valores.
			numParcela = Integer.parseInt(dadosParcela[0].trim());
			if (!dadosParcela[1].contains(".")) {
				valorParcela = Double.parseDouble(dadosParcela[1].trim().replace(",", "."));
			}
			valorParcela = Double.parseDouble(dadosParcela[1].trim().replace(".", "").replace(",", "."));
		} else {
			DecimalFormat decimalFormat = new DecimalFormat("R$ #0.00");
			numParcela = 1;
			valorParcela = dadosEntrada.getValorComDesconto();
			consulta.getEntrada().setParcela(numParcela + " X " + decimalFormat.format(valorParcela));
		}

		if (entradaEditavel(dadosEntrada))
			entradaVO = entradaDAO.buscarEntradaComParcelas(consulta.getEntrada().getId());

		if (entradaVO != null)
			consulta.getEntrada().setParcelas(entradaVO.getParcelas());

		if (consulta.getEntrada().getParcelas() == null) {

			consulta.getEntrada().setParcelas(new ArrayList<>());
		}

		for (int i = 0; i < numParcela; i++) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

			// objeto para manipular as parcelas
			Parcela parcela = new Parcela();

			Calendar dataVencimento = new GregorianCalendar();
			dataVencimento.setTime(dadosEntrada.getDataVencimento());

			/*
			 * i começa do zero logo primeiro mês é a propria data de vencimento
			 * sem alteração
			 */
			dataVencimento.add(Calendar.MONTH, +i);

			parcela.setEstadoPagamento(EstadoPagamento.PENDENTE);
			parcela.setDataVencimento(dataVencimento.getTime());
			parcela.setNumParcela((i + 1));
			parcela.setValor(valorParcela);
			parcela.setDataRegistro(new Date());

			// logica pra modificar a data do lancamento
			if (consulta.getEntrada().getDataLancamento() == null) {
				consulta.getEntrada().setDataLancamento(new Date());
			}

			consulta.getEntrada().getParcelas().add(parcela);

		}
	}

	public boolean existeConsultaPaga(Consulta consulta) {
		/*
		 * Método para verificar se existe alguma parcela da consulta paga. Caso
		 * exista, é retornado true para não permitir a alteração da consulta.
		 */

		Entrada dadosEntrada = consulta.getEntrada();
		Entrada entradaVO = null;
		try {
			if (consultaEditavel(consulta)) {

				// buscando entrada pra evitar lazyLoading
				if (entradaEditavel(dadosEntrada))
					entradaVO = entradaDAO.buscarEntradaComParcelas(dadosEntrada.getId());

				if (entradaVO != null) {
					if (entradaVO.getParcelas() != null)
						if (parcelaService.existeParcelaPaga(entradaVO.getParcelas())) {
							return true;
						}
				}
			}
		} catch (Exception e) {

		}

		return false;
	}

	public List<Consulta> buscarConsultasFuturas() {
		return consultaDAO.buscarConsultasFuturas();
	}

	public void gerarRelatorioCosultaPDF(Object documento) {

		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		try {
			Document pdf = (Document) documento;
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
			Paragraph paragraph = new Paragraph("Relatório de Consultas");
			paragraph.setAlignment(Element.ALIGN_CENTER);
			paragraph.setSpacingAfter(10);
			pdf.add(paragraph);
		} catch (DocumentException | IOException e) {
			e.printStackTrace();
			System.out.println("Erro ao gerar documento paciente. " + e);
		}

	}

	public void registrarEstatusCadastroConsulta(Consulta consulta) {
		if (consultaEditavel(consulta)) {

			// remover entrada no caixa caso a consulta for
			// cancelada
			if (isConsultaCancelada(consulta)) {
				consulta.setEntrada(null);
			}

			// setando o estatus de atualização para consulta
			consulta.setStatusCadastro(StatusCadastro.ATUALIZACAO);
		} else {
			/*
			 * caso for uma nova consulta é definido como cadastro o estatus
			 */
			consulta.setStatusCadastro(StatusCadastro.CADASTRO);

		}
	}

	public boolean validarEstadoNovaConsulta(Consulta consulta) {
		// condicao que verifica se a consulta é nova
		if (consultaNaoEditavel(consulta)) {

			// verificar o estado de ausente e cancelamento da
			// consulta
			// caso de edição
			if (isConsultaCancelada(consulta)) {
				MensagemUtil.enviarMensagem("Estado CANCELADO deve ser uma consulta existente!",
						FacesMessage.SEVERITY_ERROR);
				return Boolean.FALSE;
			} else if (isConsultaAusente(consulta)) {

				MensagemUtil.enviarMensagem("Estado AUSENTE deve ser uma consulta existente!",
						FacesMessage.SEVERITY_ERROR);
				return Boolean.FALSE;
			}
		}
		return Boolean.TRUE;
	}

	public boolean calcularDesconto(Entrada entrada) {
		boolean flag = false;
		try {
			DecimalFormat format = new DecimalFormat("R$ #,##0.00");
			// recuperando o valor antigo {TOTAL}
			double valorAntigo = entrada.getValorComDesconto();

			// SETANDO O VALOR TOTAL
			entrada.setValor(valorAntigo);

			// calculando novo valor com desconto
			double valorComDesconto = entrada.getValor() - ((entrada.getDesconto() * entrada.getValor()) / 100);

			// SETANDO O VALOR COM DESCONTO
			entrada.setValorComDesconto(valorComDesconto);

			MensagemUtil.enviarMensagem("Valor SEM desconto: " + format.format(valorAntigo),
					FacesMessage.SEVERITY_INFO);
			MensagemUtil.enviarMensagem("Desconto de " + entrada.getDesconto() + "%", FacesMessage.SEVERITY_INFO);
			MensagemUtil.enviarMensagem("Valor COM desconto: " + format.format(entrada.getValorComDesconto()),
					FacesMessage.SEVERITY_INFO);
			flag = true;
		} catch (Exception e) {
			MensagemUtil.enviarMensagem("Erro ao calcular desconto!", FacesMessage.SEVERITY_FATAL);
		}

		return flag;

	}

	public boolean consultaPossuiEstado(Consulta consulta) {
		return novaConsulta(consulta) && consulta.getEstadoConsulta() != null;
	}

	public boolean consultaEditavel(Consulta consulta) {
		return consulta != null && consulta.getId() != null && consulta.getId() > 0;
	}

	public boolean consultaNaoNula(Consulta consulta) {
		return consulta != null;
	}

	public boolean consultaNaoEditavel(Consulta consulta) {
		return !consultaEditavel(consulta);
	}

	public boolean novaConsulta(Consulta consulta) {
		return consulta != null && (consulta.getId() == null || consulta.getId() == 0);
	}

	public boolean consultaPossuiData(Consulta consulta) {
		return novaConsulta(consulta) && consulta.getDataConsulta() != null;
	}

	public boolean consultaPossuiEntrada(Consulta consulta) {
		return novaConsulta(consulta) && consulta.getEntrada() != null;
	}

	public boolean entradaEditavel(Entrada entrada) {
		return entrada != null && entrada.getId() != null && entrada.getId() > 0;
	}

	public boolean consultaPossuiPaciente(Consulta consulta) {
		if (consultaNaoNula(consulta) && consulta.getPaciente() != null) {
			return Boolean.TRUE;
		} else {
			MensagemUtil.enviarMensagem("Paciente deve ser selecionado!", FacesMessage.SEVERITY_ERROR);
			return Boolean.FALSE;

		}
	}

	public boolean consultaNaoPossuiEntrada(Consulta consulta) {
		return !consultaPossuiEntrada(consulta);
	}

	public boolean usuarioExiste(Usuario usuarioLogado) {
		return usuarioLogado != null && usuarioLogado.getId() != null && usuarioLogado.getId() > 0;
	}

	public boolean validarCalculoDesconto(Consulta consulta) {
		if (!isConsultaCancelada(consulta)) {

			// vinculando objetos da entrada na consulta
			if (consultaNaoEditavel(consulta))
				consulta.getEntrada().setDataLancamento(new Date());

			if (consulta.getEntrada().getValor() == null) {

				consulta.getEntrada().setValor(consulta.getEntrada().getValorComDesconto());
			}

			/*
			 * verificar calculo desconto para valor não ser burlado pelo
			 * front-end
			 */
			if (consulta.getEntrada().getDesconto() > 0) {

				Double valorComDesconto = consulta.getEntrada().getValor()
						- ((consulta.getEntrada().getDesconto() * consulta.getEntrada().getValor()) / 100);
				if (!valorComDesconto.equals(consulta.getEntrada().getValorComDesconto())) {
					MensagemUtil.enviarMensagem("Valor da consulta não compatível com desconto.",
							FacesMessage.SEVERITY_WARN);
					return false;
				}
			}

			/*
			 * caso não aplique o desconto o valor total é recebido no valor com
			 * desconto
			 */
			if (consulta.getEntrada().getDesconto() == 0) {
				consulta.getEntrada().setValor(consulta.getEntrada().getValorComDesconto());
			}
		}
		return true;
	}

	public List<SelectItem> gerarQuantidadePagamento(Double valor) {
		List<SelectItem> quantidadePagamentos = new ArrayList<>();

		DecimalFormat format = new DecimalFormat("#,###.00");

		if (valor == null) {
			valor = new Double(0);
		}
		SelectItemGroup dividido = new SelectItemGroup("Prestação");
		dividido.setSelectItems(new SelectItem[] { new SelectItem(valor > 0 ? "1 X " + format.format(valor) : "1 X"),
				new SelectItem(valor > 0 ? "2 X " + format.format((valor / 2)) : "2 X"),
				new SelectItem(valor > 0 ? "3 X " + format.format((valor / 3)) : "3 X"),
				new SelectItem(valor > 0 ? "4 X " + format.format((valor / 4)) : "4 X"),
				new SelectItem(valor > 0 ? "5 X " + format.format((valor / 5)) : "5 X"),
				new SelectItem(valor > 0 ? "6 X " + format.format((valor / 6)) : "6 X"),
				new SelectItem(valor > 0 ? "7 X " + format.format((valor / 7)) : "7 X"),
				new SelectItem(valor > 0 ? "8 X " + format.format((valor / 8)) : "8 X"),
				new SelectItem(valor > 0 ? "9 X " + format.format((valor / 9)) : "9 X"),
				new SelectItem(valor > 0 ? "10 X " + format.format((valor / 10)) : "10 X"), });
		quantidadePagamentos.add(dividido);
		return quantidadePagamentos;
	}

	public List<Consulta> buscarConsultasFechamento(Date dataMovimentacao) {
		return consultaDAO.buscarConsultasFechamento(dataMovimentacao);
	}

	public void removerReferenciaParcela(Consulta consulta) throws Exception {
		if (consultaEditavel(consulta)) {
			parcelaDAO.removerParcelasPorId(consulta.getEntrada().getId());
		}
	}

	public void registrarUsuarioLogado(Consulta consulta) {
		// buscar usuário logado no sistema pra cadastrar na
		// consulta
		Usuario usuarioLogado = (Usuario) FacesUtil.getSessionAttribute("usuario");
		if (usuarioExiste(usuarioLogado)) {

			consulta.setNomeUsuarioCadastrou(usuarioLogado.getLogin());
			consulta.setDataCadastro(new Date());
		}
	}
}
