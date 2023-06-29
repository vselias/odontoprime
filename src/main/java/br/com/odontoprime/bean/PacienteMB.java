package br.com.odontoprime.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.ViewScoped;
import org.primefaces.event.CaptureEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.CroppedImage;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import br.com.odontoprime.entidade.Consulta;
import br.com.odontoprime.entidade.Estado;
import br.com.odontoprime.entidade.Paciente;
import br.com.odontoprime.service.ConsultaService;
import br.com.odontoprime.service.PacienteService;

@Named
@ViewScoped
public class PacienteMB implements Serializable {

	private static final long serialVersionUID = -1460318146874590950L;
	private Paciente paciente;
	private List<Paciente> listaPesquisa;
	private List<Paciente> pacientesSelecionados;
	private Consulta consulta;
	private UploadedFile file;
	private List<Consulta> consultas;
	private List<Paciente> pacientes;
	private StreamedContent imgStream;
	private StreamedContent exibImg;
	@Inject
	private ConsultaService consultaService;
	@Inject
	private PacienteService pacienteService;
	private boolean exibirImagemCapturada;
	private List<Paciente> pacientesFiltrados;
	private CroppedImage croppedImage;
	private boolean pacienteEditar;
	private boolean webCam;
	private boolean exibirImagemCropper;
	private boolean exibirImagemRecortada;
	private List<Estado> estados;

	public boolean isExibirImagemRecortada() {
		return exibirImagemRecortada;
	}

	public void setExibirImagemRecortada(boolean exibirImagemRecortada) {
		this.exibirImagemRecortada = exibirImagemRecortada;
	}

	public boolean isExibirImagemCropper() {
		return exibirImagemCropper;
	}

	public void setExibirImagemCropper(boolean exibirImagemCropper) {
		this.exibirImagemCropper = exibirImagemCropper;
	}

	public List<Paciente> getPacientesFiltrados() {
		return pacientesFiltrados;
	}

	public void setPacientesFiltrados(List<Paciente> pacientesFiltrados) {
		this.pacientesFiltrados = pacientesFiltrados;
	}

	public boolean isWebCam() {
		return webCam;
	}

	public boolean isPacienteEditar() {
		return pacienteEditar;
	}

	public void setPacienteEditar(boolean editarPaciente) {
		this.pacienteEditar = editarPaciente;
	}

	public CroppedImage getCroppedImage() {
		if (croppedImage == null)
			croppedImage = new CroppedImage();
		return croppedImage;
	}

	public void setCroppedImage(CroppedImage croppedImage) {
		this.croppedImage = croppedImage;
	}

	public boolean isExibirImagemCapturada() {
		return exibirImagemCapturada;
	}

	public void setExibirImagemCapturada(boolean exibirImagemCapturada) {
		this.exibirImagemCapturada = exibirImagemCapturada;
	}

	public void selecionarPacienteAmpliarImagem(Paciente paciente) {
		this.paciente = paciente;
	}

	public StreamedContent getExibImg() {
		if (paciente.getId() != null && paciente.getByteImg() != null) {
			exibImg = pacienteService.exibirImg(paciente);
		}

		return exibImg;
	}

	public void setExibImg(StreamedContent exibImg) {
		this.exibImg = exibImg;
	}

	public void setImgStream(StreamedContent imgStream) {
		this.imgStream = imgStream;
	}

	public Paciente getPaciente() {
		if (paciente == null) {
			paciente = new Paciente();
		}
		return paciente;
	}

	public List<Paciente> getPacientesSelecionados() {

		return pacientesSelecionados;
	}

	public void setPacientesSelecionados(List<Paciente> pacientesDropados) {
		this.pacientesSelecionados = pacientesDropados;
	}

	public List<Paciente> getPacientes() {
		return pacientes;
	}

	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}

	public void setPacientes(List<Paciente> pacientes) {
		this.pacientes = pacientes;
	}

	public void setConsulta(Consulta consulta) {
		this.consulta = consulta;
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

	public List<Estado> getEstados() {
		return estados;
	}

	public Consulta getConsulta() {
		if (consulta == null) {

			consulta = new Consulta();
		}
		return consulta;
	}

	public List<Paciente> getListaPesquisa() {

		return listaPesquisa;
	}

	public List<Consulta> getConsultas() {
		consultas = consultaService.buscarTodos();

		if (consultas == null) {
			consultas = new ArrayList<Consulta>();
		}
		return consultas;
	}

	public void setConsultas(List<Consulta> consultas) {
		this.consultas = consultas;
	}

	public void setListaPesquisa(List<Paciente> listaPesquisa) {
		this.listaPesquisa = listaPesquisa;
	}

	@PostConstruct
	public void initi() {
		paciente = new Paciente();
		consulta = new Consulta();
		pacientesSelecionados = new ArrayList<Paciente>();
		pacientes = pacienteService.buscarTodos();
		croppedImage = new CroppedImage();
		exibirImagemCapturada = Boolean.FALSE;
		webCam = Boolean.FALSE;
		exibirImagemCropper = Boolean.FALSE;
		exibirImagemRecortada = Boolean.FALSE;
		estados = Arrays.asList(Estado.values());
	}

	public List<Paciente> buscarTodos() {
		return pacienteService.buscarTodos();
	}

	public void salvar() {
		// resetar valores do bean para limpar os campos...
		if (pacienteService.salvar(paciente))
			limpar();

	}

	public boolean verificarCPF() {
		return pacienteService.verificaCpf(paciente);

	}

	public void gerarRelatorioPDF(Object documento) {
		pacienteService.gerarRelatorioPDF(documento);

	}

	public void limpar() {
		// resetando os beans que estão populados na tela
		paciente = new Paciente();
		consulta = new Consulta();
		pacientes = pacienteService.buscarTodos();
	}

	public void excluirPaciente() {
		pacienteService.remover(paciente);
		limpar();
	}

	public StreamedContent getImgStream() {
		FacesContext currentInstance = FacesContext.getCurrentInstance();
		if (currentInstance.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
			return new DefaultStreamedContent();
		}
		imgStream = pacienteService.exibirFotoPaciente(paciente);
		return imgStream;
	}

	public void recortarImagem() {
		exibirImagemRecortada = pacienteService.recortarImagem(paciente, croppedImage);
	}

	public void subirImagem(FileUploadEvent event) {
		exibirImagemCropper = pacienteService.subirImagem(event, paciente);
	}

	public void tirarFoto(CaptureEvent captureEvent) {
		exibirImagemCropper = pacienteService.tirarFoto(captureEvent.getData(), paciente);
	}

	public void gerarNomeImagemAleatoria() {
		paciente.setNomeImagem(pacienteService.gerarNomeImagemAleatoria());
	}

	public boolean verificaCpfBd() {
		return pacienteService.existeCpfBd(paciente);
	}

	public void salvarImagem() {
		pacienteService.salvarImagem(paciente);
	}

	public void salvarImagemRecortada() {
		pacienteService.salvarImagemRecortada(croppedImage, paciente);
	}

	public void ativarWebCam() {
		webCam = Boolean.TRUE;
	}

	public void desativarWebCam() {
		webCam = Boolean.FALSE;
	}
}
