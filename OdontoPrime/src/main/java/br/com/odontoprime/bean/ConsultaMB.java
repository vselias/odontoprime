package br.com.odontoprime.bean;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.ViewScoped;

import com.google.gson.JsonIOException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import br.com.odontoprime.entidade.Consulta;
import br.com.odontoprime.entidade.Entrada;
import br.com.odontoprime.entidade.EstadoConsulta;
import br.com.odontoprime.entidade.EstadoPagamento;
import br.com.odontoprime.entidade.FormaPagamento;
import br.com.odontoprime.entidade.Paciente;
import br.com.odontoprime.entidade.TipoConsulta;
import br.com.odontoprime.service.ConsultaService;
import br.com.odontoprime.service.EntradaService;
import br.com.odontoprime.service.PacienteService;

@Named
@ViewScoped
public class ConsultaMB implements Serializable {

	private static final long serialVersionUID = 7893116720238700716L;
	@Inject
	private ConsultaService consultaService;
	@Inject
	private PacienteService pacienteService;
	@Inject
	private EntradaService entradaService;
	private Consulta consulta;
	private Paciente paciente;
	private List<Consulta> consultas;
	private List<Consulta> consultasSelecionadas;
	private List<Consulta> consultasPesquisa;
	private List<Paciente> pacientes;
	private List<Consulta> consultasFuturas;
	private List<SelectItem> opcoesPgmto;
	private List<Consulta> consultasFiltradas;
	private List<TipoConsulta> tiposConsulta;
	private List<EstadoConsulta> estadosConsulta;
	private boolean desativarCampos;
	private List<SelectItem> quantidadePagamentos;
	private boolean desativarComboBoxParcela;
	private List<EstadoPagamento> estadosPagamentos;
	private Date dataPesquisa;
	private boolean editarConsultaPaga;
	private List<FormaPagamento> opcoesPagamento;
	private FormaPagamento formaPagamento;

	@PostConstruct
	public void init() {

		opcoesPagamento = Arrays.asList(FormaPagamento.values());
		desativarComboBoxParcela = Boolean.FALSE;
		consultas = consultaService.buscarTodos();
		recuperarConsultaFlash();
		paciente = new Paciente();
		consultasSelecionadas = new ArrayList<Consulta>();
		consultasPesquisa = new ArrayList<Consulta>();
		pacientes = pacienteService.buscarTodos();
		consultasFuturas = consultaService.buscarConsultasFuturas();
		consultasFuturas = consultaService.buscarConsultasFuturas();
		tiposConsulta = Arrays.asList(TipoConsulta.values());
		estadosConsulta = Arrays.asList(EstadoConsulta.values());
		quantidadePagamentos = consultaService.gerarQuantidadePagamento(recuperarValorComDesconto());
		estadosPagamentos = Arrays.asList(EstadoPagamento.values());
		JsonParser parser = new JsonParser();
		try {
			Object object = parser.parse(new FileReader("src/main/java/estados-cidades.json"));

		} catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public FormaPagamento getFormaPagamento() {
		return formaPagamento;
	}

	public List<FormaPagamento> getOpcoesPagamento() {
		return opcoesPagamento;
	}

	public void setFormaPagamento(FormaPagamento formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	public boolean isEditarConsultaPaga() {
		editarConsultaPaga = consultaService.existeConsultaPaga(consulta);
		return editarConsultaPaga;
	}

	public void setEditarConsultaPaga(boolean editarConsultaPaga) {
		this.editarConsultaPaga = editarConsultaPaga;
	}

	public Date getDataPesquisa() {
		return dataPesquisa;
	}

	public void setDataPesquisa(Date dataPesquisa) {
		this.dataPesquisa = dataPesquisa;
	}

	public List<EstadoPagamento> getEstadosPagamentos() {
		return estadosPagamentos;
	}

	public boolean isDesativarComboBoxParcela() {
		return desativarComboBoxParcela;
	}

	public boolean isDesativarCampos() {
		return desativarCampos;
	}

	public List<SelectItem> getQuantidadePagamentos() {
		return quantidadePagamentos;
	}

	public void setQuantidadePagamentos(List<SelectItem> quantidadePagamentos) {
		this.quantidadePagamentos = quantidadePagamentos;
	}

	public List<EstadoConsulta> getEstadosConsulta() {
		return estadosConsulta;
	}

	public List<Consulta> getConsultasFiltradas() {
		return consultasFiltradas;
	}

	public void setConsultasFiltradas(List<Consulta> consultasFiltradas) {
		this.consultasFiltradas = consultasFiltradas;
	}

	public void atualizar() {
		consultaService.atualizar(consulta);
	}

	public List<SelectItem> getOpcoesPgmto() {
		return opcoesPgmto;
	}

	public void setOpcoesPgmto(List<SelectItem> opcoesPgmto) {
		this.opcoesPgmto = opcoesPgmto;
	}

	public List<Consulta> getConsultasFuturas() {

		return consultasFuturas;
	}

	public void setConsultasFuturas(List<Consulta> consultasFuturas) {
		this.consultasFuturas = consultasFuturas;
	}

	public List<TipoConsulta> getTiposConsulta() {
		return tiposConsulta;
	}

	public void setTiposConsulta(List<TipoConsulta> tiposConsulta) {
		this.tiposConsulta = tiposConsulta;
	}

	public void limparCampos() {
		consulta = new Consulta();
		paciente = new Paciente();
		consultas = consultaService.buscarTodos();
		consultasFuturas = consultaService.buscarConsultasFuturas();
		pacientes = pacienteService.buscarTodos();
		consulta.setEntrada(new Entrada());
	}

	public List<Paciente> getPacientes() {

		return pacientes;
	}

	public void setPacientes(List<Paciente> pacientes) {
		this.pacientes = pacientes;
	}

	public List<Consulta> getConsultasPesquisa() {
		return consultasPesquisa;
	}

	public void setConsultasPesquisa(List<Consulta> consultasPesquisa) {
		this.consultasPesquisa = consultasPesquisa;
	}

	public Paciente getPaciente() {
		return paciente;
	}

	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}

	public List<Consulta> getConsultasSelecionadas() {
		return consultasSelecionadas;
	}

	public void setConsultasSelecionadas(List<Consulta> consultasSelecionadas) {
		this.consultasSelecionadas = consultasSelecionadas;
	}

	public Consulta getConsulta() {
		if (consulta == null) {
			consulta = new Consulta();
			consulta.setEntrada(new Entrada());
		}
		return consulta;
	}

	public void setConsulta(Consulta consulta) {
		this.consulta = consulta;
	}

	public List<Consulta> getConsultas() {
		return consultas;
	}

	public void setConsultas(List<Consulta> consultas) {
		this.consultas = consultas;
	}

	public void gerarRelatorioConsultasPDF(Object documento) {
		consultaService.gerarRelatorioCosultaPDF(documento);

	}

	public void excluir() {
		consultaService.remover(consulta);
		atualizarListaConsulta();
	}

	public void selecionarPacienteConsulta(Paciente paciente) {
		this.paciente = paciente;
	}

	public void efetuarConsulta() {

		if (consultaService.efetuarConsulta(this.consulta))
			limparCampos();
	}

	public double recuperarValorComDesconto() {
		if (consulta.getEntrada() == null) {
			return 0;
		} else if (consulta.getEntrada().getValorComDesconto() != null) {
			return consulta.getEntrada().getValorComDesconto();
		}
		return 0;
	}

	public void verificarHorarioConsulta() throws Exception {
		consultaService.existeHorarioConsulta(consulta);
	}

	public boolean entradaEditavel(Consulta consulta) {
		return consultaContemEntrada() && consulta.getEntrada().getId() != null && consulta.getEntrada().getId() > 0;
	}

	public String editarConsulta(Consulta consulta) {

		if (entradaEditavel(consulta)) {
			Entrada entradaVO = entradaService.buscarEntradaComParcelas(consulta.getEntrada().getId());
			consulta.setEntrada(entradaVO);

		}

		FacesContext.getCurrentInstance().getExternalContext().getFlash().put("consulta", consulta);
		return "Consulta?faces-redirect=true";

	}

	public void recuperarConsultaFlash() {
		consulta = (Consulta) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("consulta");
		if (consulta == null) {
			consulta = new Consulta();
		} else if (consulta.getId() != null) {
			consulta = consultaService.buscarPorId(consulta.getId());
		}

	}

	public void calcularDesconto() {
		if (consultaService.calcularDesconto(consulta.getEntrada())) {
			gerarQuantidadePagamentos();
		}

	}

	public List<Paciente> listarPacientesConsulta(String nome) {

		if (pacientes == null)
			pacientes = pacienteService.buscarTodos();

		List<Paciente> pacientesFiltrados = new ArrayList<>();

		pacientes.forEach(p -> {

			if (p.getNome().toLowerCase().startsWith(nome.toLowerCase())) {
				pacientesFiltrados.add(p);
			}

		});

		return pacientesFiltrados;
	}

	public boolean estadoCancelado() {
		if (consulta.getEstadoConsulta() != null) {
			if (consulta.getEstadoConsulta().equals(EstadoConsulta.CANCELADO)) {
				desativarCampos = Boolean.TRUE;
				desativarComboBoxParcela = Boolean.TRUE;
				consulta.getEntrada().setParcelado(Boolean.FALSE);
			} else {
				desativarCampos = false;
				desativarComboBoxParcela = Boolean.FALSE;
				consulta.getEntrada().setParcelado(Boolean.TRUE);
			}
		}
		return desativarCampos;
	}

	public void gerarQuantidadePagamentos() {
		quantidadePagamentos = consultaService.gerarQuantidadePagamento(recuperarValorComDesconto());

	}

	public void atualizarListaConsulta() {
		this.consultas = consultaService.buscarTodos();
	}

	public boolean consultaContemEntrada() {
		return consulta != null && consulta.getEntrada() != null;
	}

}
