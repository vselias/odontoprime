package br.com.odontoprime.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.ViewScoped;

import br.com.odontoprime.entidade.Consulta;
import br.com.odontoprime.service.ConsultaService;
import br.com.odontoprime.service.RegistroVendaService;
import br.com.odontoprime.util.MensagemUtil;

import com.lowagie.text.Document;

@Named
@ViewScoped
public class RegistroVendaMB implements Serializable {
	@Inject
	private ConsultaService consultaService;
	private static final long serialVersionUID = 7273256468107654152L;
	private Consulta consulta;
	private List<Consulta> consultasPorPaciente;
	private Date dataInicio;
	private Date dataFinal;
	private Double valorTotal;
	private List<Consulta> consultasDia;
	@Inject
	private RegistroVendaService registroVendaService;
	private List<Consulta> consultasPorData;
	private List<Consulta> consultasPorPacienteFiltrado;

	@PostConstruct
	public void init() {
		consulta = new Consulta();
		consultasPorPaciente = consultaService.buscarConsultasPorPaciente();
		dataInicio = new Date();
		dataFinal = new Date();
		consultasDia = consultaService.vendasPorDia();
		buscarConsultasPorData();
	}

	public List<Consulta> getConsultasPorPacienteFiltrado() {
		return consultasPorPacienteFiltrado;
	}

	public void setConsultasPorPacienteFiltrado(List<Consulta> consultasPorPacienteFiltrado) {
		this.consultasPorPacienteFiltrado = consultasPorPacienteFiltrado;
	}

	public List<Consulta> getConsultasPorData() {
		return consultasPorData;
	}

	public void setConsultasPorData(List<Consulta> consultasPorData) {
		this.consultasPorData = consultasPorData;
	}

	public List<Consulta> getConsultasDia() {
		return consultasDia;
	}

	public void setConsultasDia(List<Consulta> consultasDia) {
		this.consultasDia = consultasDia;
	}

	public Double getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(Double valorTotal) {
		this.valorTotal = valorTotal;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	public Consulta getConsulta() {
		return consulta;
	}

	public Date getDataInicio() {

		return dataInicio;
	}

	public void setDataInicio(Date datIninicio) {
		this.dataInicio = datIninicio;
	}

	public void setConsulta(Consulta consulta) {
		this.consulta = consulta;
	}

	public List<Consulta> getConsultasPorPaciente() {
		return consultasPorPaciente;
	}

	public void setConsultasPorPaciente(List<Consulta> consultas) {
		this.consultasPorPaciente = consultas;
	}

	public void calcularVendaPorData() {
		valorTotal = registroVendaService.valorTotalVendaPorData(dataInicio, dataFinal);
		buscarConsultasPorData();
	}

	public void buscarConsultasPorData() {

		consultasPorData = registroVendaService.buscarConsultasPorData(dataInicio, dataFinal);
		if (consultasPorData == null) {
			consultasPorData = new ArrayList<Consulta>();
		}
	}

	public void limpar() {
		this.dataInicio = null;
		this.dataFinal = null;
		this.valorTotal = null;
		MensagemUtil.enviarMensagem("Campos limpos.", FacesMessage.SEVERITY_INFO);
	}

	public void gerarConsultasPdf(Document documento) {
		registroVendaService.gerarPdf(documento);
	}
}
